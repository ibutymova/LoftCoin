package com.example.butymovaloftcoin.screens.welcome.welcomeFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.butymovaloftcoin.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomePageAdapter extends FragmentPagerAdapter {
    private List<WelcomePage> pages;

    public WelcomePageAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        pages = new ArrayList<>();
        pages.add(new WelcomePage(R.drawable.ic_welcome_page1, R.string.welcome_page1_title, R.string.welcome_page1_content));
        pages.add(new WelcomePage(R.drawable.ic_welcome_page2, R.string.welcome_page2_title, R.string.welcome_page2_content));
        pages.add(new WelcomePage(R.drawable.ic_welcome_page3, R.string.welcome_page3_title, R.string.welcome_page3_content));
    }

    @Override
    public Fragment getItem(int position) {
       return WelcomeFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
