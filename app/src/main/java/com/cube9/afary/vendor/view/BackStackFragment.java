package com.cube9.afary.vendor.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class BackStackFragment extends Fragment {
    public static boolean handleBackPressed(FragmentManager fm) {
        if (fm.getFragments() != null) {
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible() && frag instanceof BackStackFragment) {
                    if (((BackStackFragment) frag).onBackPressed()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean onBackPressed() {
        FragmentManager fm = getChildFragmentManager();
        if (handleBackPressed(fm)) {
            return true;
        } else if (getUserVisibleHint() && fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return true;
        }
        return false;
    }
}