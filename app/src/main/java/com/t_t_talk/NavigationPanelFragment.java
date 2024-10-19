package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class NavigationPanelFragment extends Fragment {
    private boolean isSettingVisible = false, isNextVisible = false;
    private boolean isEnglish = true;
    private AppCompatActivity activity;

    // Constructor (empty) - required for fragments
    public NavigationPanelFragment(AppCompatActivity activity, boolean isSettingVisible, boolean isNextVisible) {
        this.activity = activity;
        this.isSettingVisible = isSettingVisible;
        this.isNextVisible = isNextVisible;
    }

    public NavigationPanelFragment(AppCompatActivity activity, boolean isSettingVisible, boolean isNextVisible, boolean isEnglish) {
        this.activity = activity;
        this.isSettingVisible = isSettingVisible;
        this.isNextVisible = isNextVisible;
        this.isEnglish = isEnglish;
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
            Intent intent = new Intent(this.getContext(), InformationActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            // Handle settings button click
            SettingsDialog settingsDialog = new SettingsDialog(this.getContext());
            settingsDialog.show();
        });

        btnNext.setOnClickListener(v -> {
            // Handle next button click
            this.activity.finish();
        });

        return view;
    }
}
