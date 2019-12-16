package com.example.moviecatalogueega.tablayout;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviecatalogueega.R;

public class PageAdapter extends FragmentPagerAdapter {
    private Context mcontext;

    public PageAdapter(@Nullable Context context,@Nullable  FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mcontext = context;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TabMovies();
                break;
            case 1:
                fragment = new TabTvShow();
                break;
            case 2:
                fragment = new TabSearching();
                break;
        }
        return fragment;
    }



    @Override
    public int getCount() {
        return 3;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab1,
            R.string.tab2,
            R.string.tab3
    };

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mcontext.getResources().getString(TAB_TITLES[position]);
    }


}
