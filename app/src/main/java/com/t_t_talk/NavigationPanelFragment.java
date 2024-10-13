package com.t_t_talk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NavigationPanelFragment extends Fragment {

    // Constructor (empty) - required for fragments
    public NavigationPanelFragment() {
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

        // Set up click listeners if needed
        btnInfo.setOnClickListener(v -> {
            // Handle info button click
        });

        btnHome.setOnClickListener(v -> {
            // Handle home button click
        });

        btnSettings.setOnClickListener(v -> {
            // Handle settings button click
        });

        return view;
    }
}
