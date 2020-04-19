package com.example.butymovaloftcoin.screens.welcome.welcomeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.R;

public class WelcomeFragment extends Fragment {
    private static final String KEY_PAGE = "page";

    private WelcomeFragmentView contentView;

    public static WelcomeFragment newInstance(WelcomePage page){
        Bundle args = new Bundle();
        args.putParcelable(KEY_PAGE, page);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args!=null && args.containsKey(KEY_PAGE))
            contentView = new WelcomeFragmentViewImpl(view, args.getParcelable(KEY_PAGE));
    }

    @Override
    public void onDestroyView() {
        contentView.detach();
        super.onDestroyView();
    }
}
