package com.example.moviecatalogueega.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.moviecatalogueega.R;
import com.example.moviecatalogueega.tablayout.PageAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    private static TabPositionCallback positionCallback;

    public void setPositionCallback(TabPositionCallback positionCallback) {
        HomeFragment.positionCallback = positionCallback;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);

        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        PageAdapter pageAdapter = new PageAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                positionCallback.onPosition(position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface TabPositionCallback{
        void onPosition(int position);
    }


}