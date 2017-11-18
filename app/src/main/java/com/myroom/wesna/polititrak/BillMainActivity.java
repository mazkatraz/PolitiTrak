package com.myroom.wesna.polititrak;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class BillMainActivity extends FragmentActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);

        PagerAdapter pagerAdapter
                = new PagerAdapter(getSupportFragmentManager(), this);

        //Set adapter to view pager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //Set up tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        //Select the corresponding tab when the user swipes between pages
        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        ActionBar actionBar = getActionBar();
                        if(actionBar != null) {
                            actionBar.setSelectedNavigationItem(position);
                        }
                    }
                }
        );
    }
}

/**
 * Page adapter for the view pager for Bills
 */
class PagerAdapter extends FragmentPagerAdapter {
    private Context context;

    /**
     * Fragment Pager Adapter constructor
     * @param fm The fragment manager
     * @param context The context of the view pager
     */
    PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * Gets the appropriate fragment bases on position
     * @param position The position of the fragment in the view pager
     * @return The fragment bases on the position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Fragment.instantiate(context, LocalBillsFragment.class.getName());
            case 1:
                return Fragment.instantiate(context, NationalBillsFragment.class.getName());

        }
        return null;
    }

    /**
     * Sets the titles for the tabs in the Bill view pager
     * @param position The position of the fragment in the view pager
     * @return The title char sequence
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.local_bills_tab);
            case 1:
                return context.getString(R.string.national_bills_tab);
        }

        return null;
    }

    /**
     * The amount of fragments the view pager holds
     * @return The amount of fragments in view pager
     */
    @Override
    public int getCount() {
        return 2;
    }
}
