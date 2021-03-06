package com.example.ezeecross;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter1 extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter1(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
     switch(position){
         case 0:
             ParentAcceptForm wardenAccept=new ParentAcceptForm();
             return wardenAccept;
         case 1:
             ParentHistory wardenHistory=new ParentHistory();
             return wardenHistory;
         case 2:
             ParentMyAccount wardenMyAccount=new ParentMyAccount();
             return wardenMyAccount;
         default:
             return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                  return "Requests";
            case 1:
                  return "History";
            case 2:
                  return "My account";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}