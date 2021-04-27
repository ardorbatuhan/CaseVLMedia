package com.ardorapps.demovl.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ardorapps.demovl.R;
import com.ardorapps.demovl.model.People;
import com.bumptech.glide.Glide;

import java.util.List;

public class PeopleListPagerAdapter extends PagerAdapter {

    private List<People> peopleList;

    PeopleListPagerAdapter(List<People> peopleList) {
        this.peopleList = peopleList;
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        People people = peopleList.get(position);
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
        ImageView image = itemView.findViewById(R.id.iv_person_image);
        TextView name = itemView.findViewById(R.id.tv_person_name);
        TextView lastLocation = itemView.findViewById(R.id.tv_person_last_location);
        TextView status = itemView.findViewById(R.id.tv_person_status);
        Glide.with(image.getContext())
                .load(people.getImage())
                .placeholder(R.drawable.list_item_placeholder)
                .error(R.drawable.list_item_placeholder)
                .into(image);
        name.setText(people.getName());
        lastLocation.setText(people.getLocation().getName());
        status.setText(people.getStatus());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    void addPeopleList(List<People> peopleList) {
        this.peopleList = peopleList;
    }

}