package com.example.butymovaloftcoin.screens.welcome.welcomeFragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.butymovaloftcoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WelcomeFragmentViewImpl implements WelcomeFragmentView {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.subtitle)
    TextView subtitle;

    private View view;
    private Unbinder unbinder;

    public WelcomeFragmentViewImpl(View view, WelcomePage page) {
        this.view = view;
        unbinder = ButterKnife.bind(this, view);

        icon.setImageResource(page.getIcon());
        title.setText(page.getTitle());
        subtitle.setText(page.getSubtitle());
    }

    @Override
    public void detach() {
        view = null;
        unbinder.unbind();
    }
}
