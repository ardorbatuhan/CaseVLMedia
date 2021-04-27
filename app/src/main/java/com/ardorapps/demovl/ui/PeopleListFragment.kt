package com.ardorapps.demovl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ardorapps.demovl.R
import com.ardorapps.demovl.common.Resource
import com.ardorapps.demovl.databinding.PeopleListFragmentBinding
import com.ardorapps.demovl.ui.PeopleListViewModel.Companion.PAGE_MARGIN
import com.ardorapps.demovl.ui.PeopleListViewModel.Companion.VISIBLE_THRESHOLD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.people_list_fragment.*

@AndroidEntryPoint
class PeopleListFragment : Fragment() {
    companion object {
        fun newInstance() = PeopleListFragment()
    }

    lateinit var binding: PeopleListFragmentBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.people_list_fragment,
            container,
            false
        )
        binding.lifecycleOwner = (viewLifecycleOwner);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: PeopleListViewModel by viewModels()
        binding.viewModel = viewModel;

        val peopleListPagerAdapter = PeopleListPagerAdapter(
            listOf()
        );
        view_pager.adapter = peopleListPagerAdapter;
        view_pager.currentItem = viewModel.currAdapterPageIndex;
        view_pager.pageMargin = PAGE_MARGIN;

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val value = viewModel.getPeopleMutableLiveData().value
                if (value != null) {
                    if ((position + VISIBLE_THRESHOLD) > value.data?.size!!) {
                        viewModel.currPage++
                        viewModel.fetchPeople(viewModel.currPage);
                    }
                }
                if (position > viewModel.currAdapterPageIndex) {
                    viewModel.currAdapterPageIndex = position;
                } else if (position < viewModel.currAdapterPageIndex) {
                    // disabled to swipe right
                    view_pager.setCurrentItem(viewModel.currAdapterPageIndex, true);
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        viewModel.getPeopleMutableLiveData()
            .observe(viewLifecycleOwner, Observer { resourcePeopleResponse ->
                when (resourcePeopleResponse.status) {
                    Resource.Status.LOADING -> {
                        showLoading();
                    }
                    Resource.Status.ERROR -> {
                        if (viewModel.currRetryCount < PeopleListViewModel.MAX_ALLOWED_RETRY_COUNT) {
                            viewModel.currRetryCount++;
                            viewModel.fetchPeople(viewModel.currPage, true);
                        } else {
                            hideLoading();
                            Toast.makeText(
                                context,
                                resourcePeopleResponse.message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                    Resource.Status.SUCCESS -> {
                        hideLoading();
                        peopleListPagerAdapter.apply {
                            addPeopleList(resourcePeopleResponse.data)
                            notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE;
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE;
    }
}