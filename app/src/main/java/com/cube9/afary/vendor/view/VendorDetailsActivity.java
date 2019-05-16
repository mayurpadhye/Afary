package com.cube9.afary.vendor.view;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.avontell.pagerindicatorbinder.IndicatorBinder;
import com.cube9.afary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorDetailsActivity extends AppCompatActivity implements SelectSkillFragment.OnFragmentInteractionListener, TakePhotoFragment.OnFragmentInteractionListener,SelectCategoryFragment.OnFragmentInteractionListener,SelectDocumentFragment.OnFragmentInteractionListener{
    @BindView(R.id.view_pager_one)
    CustomViewPager view_pager_one;

    public static  String CURRENT_TAG="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        ButterKnife.bind(this);
        LinearLayout indicatorContainer = (LinearLayout) findViewById(R.id.indicator_cont);


        view_pager_one.setPagingEnabled(false);
        SignUpAdapter adapterViewPager = new SignUpAdapter(getSupportFragmentManager());
        view_pager_one.setAdapter(adapterViewPager);

        // Get resources for drawables
        int selectedImage = R.drawable.indicator_selected;
        int unselectedImage = R.drawable.indicator_unselected;

        // Bind the view pager to the indicatorContainer
        final IndicatorBinder sample = new IndicatorBinder().bind(this,
                view_pager_one,
                indicatorContainer,
                selectedImage,
                unselectedImage);

        // Set whether you want a progress style
        sample.setProgressStyle(true);
    }
    public void handleNextClick()
    {
        view_pager_one.setCurrentItem(getItem(+1), true);
    }
    public void previousClick()
    {
        view_pager_one.setCurrentItem(getItem(-1), true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private int getItem(int i) {
        return view_pager_one.getCurrentItem() + i;
    }

    @Override
    public void onBackPressed() {

        if (view_pager_one.getCurrentItem()>0)
        {
            previousClick();
        }
        else
        {
            super.onBackPressed();
        }


    }
}
