package com.mehboob.hunzabykea.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mehboob.hunzabykea.ui.fragments.ActiveNowFragment;
import com.mehboob.hunzabykea.ui.fragments.CancelledFragment;
import com.mehboob.hunzabykea.ui.fragments.CompletedFragment;

public class BookingTabsAdapter extends FragmentStateAdapter {

    public BookingTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new ActiveNowFragment();
            case 1:
                return new CompletedFragment();
            case 2:
                return new CancelledFragment();
            default:
                return new ActiveNowFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
