package com.app.pandastock.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ReportePagerAdapter extends FragmentStateAdapter {

    public ReportePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ReporteFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

