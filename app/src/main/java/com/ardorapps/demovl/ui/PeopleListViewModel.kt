package com.ardorapps.demovl.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardorapps.demovl.EndOfPeopleListException
import com.ardorapps.demovl.common.ListUtils
import com.ardorapps.demovl.common.Resource
import com.ardorapps.demovl.model.People
import com.ardorapps.demovl.remote.PeopleListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val peopleListRepository: PeopleListRepository
) : ViewModel() {

    companion object {
        const val INITIAL_ADAPTER_PAGE_INDEX = 0;
        const val INITIAL_PAGE = 1;
        const val VISIBLE_THRESHOLD = 25;
        const val PAGE_MARGIN = 50;
        const val MAX_ALLOWED_RETRY_COUNT = 3;
    }

    var currAdapterPageIndex = INITIAL_ADAPTER_PAGE_INDEX;
    var currPage = INITIAL_PAGE;
    var currRetryCount = 0;

    private var peopleMutableLiveData: MutableLiveData<Resource<List<People>?>>

    init {
        peopleMutableLiveData = MutableLiveData();
        fetchPeople();
    }

    fun getPeopleMutableLiveData() = peopleMutableLiveData;

    fun fetchPeople(page: Int = INITIAL_PAGE, force: Boolean = false) {
        if (peopleMutableLiveData.value?.status != Resource.Status.LOADING
            || force
        ) {
            peopleMutableLiveData.value = Resource.loading(peopleMutableLiveData.value?.data);
            viewModelScope.launch(IO) {
                try {
                    val peopleResponse = peopleListRepository.getPeople(page)
                    if (peopleResponse.info.next == null) {
                        throw EndOfPeopleListException();
                    }
                    val concatList = ListUtils.mergeLists(
                        peopleMutableLiveData.value?.data,
                        peopleResponse.results
                    );
                    val distinctList = ListUtils.distinctById(concatList)
                    peopleMutableLiveData.postValue(Resource.success(distinctList));
                } catch (exception: Exception) {
                    peopleMutableLiveData.postValue(
                        Resource.error(
                            exception.localizedMessage ?: "",
                            peopleMutableLiveData.value?.data
                        )
                    );
                }

            }
        }
    }

}