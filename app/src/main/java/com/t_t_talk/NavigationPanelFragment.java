package com.t_t_talk;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NavigationPanelFragment extends Fragment {
    private boolean isSettingVisible = false, isNextVisible = false;

    // Constructor (empty) - required for fragments
    public NavigationPanelFragment(boolean isSettingVisible, boolean isNextVisible) {
        this.isSettingVisible = isSettingVisible;
        this.isNextVisible = isNextVisible;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout (connects to XML)
        View view = inflater.inflate(R.layout.fragment_navigation_panel, container, false);

        // Find buttons by ID
        ImageButton btnInfo = view.findViewById(R.id.btn_info);
        ImageButton btnHome = view.findViewById(R.id.btn_home);
        ImageButton btnSettings = view.findViewById(R.id.btn_settings);
        ImageButton btnNext = view.findViewById(R.id.btn_next);

        // Set visibility of buttons based on parameters
        btnSettings.setVisibility(isSettingVisible ? View.VISIBLE : View.GONE);
        btnNext.setVisibility(isNextVisible ? View.VISIBLE : View.GONE);

        // Set up click listeners if needed
        btnInfo.setOnClickListener(v -> {
            // Handle info button click
        });

        btnHome.setOnClickListener(v -> {
            // Handle home button click
        });

        btnSettings.setOnClickListener(v -> {
            // Handle settings button click
            DialogSettings settingsDialog = new DialogSettings(this.getContext());
            settingsDialog.show();
        });

        return view;
    }
}
