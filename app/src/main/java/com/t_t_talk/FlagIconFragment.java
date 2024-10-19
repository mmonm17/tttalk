package com.t_t_talk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FlagIconFragment extends Fragment{
    private int imageResource;
    public FlagIconFragment(int imageResource) {
        this.imageResource = imageResource;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flag_icon, container, false);

        ImageView img_flag_stroke = view.findViewById(R.id.img_flag_stroke);
        ImageView img_flag_content = view.findViewById(R.id.img_flag_content);

        img_flag_content.setImageResource(imageResource);


        img_flag_stroke.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getActivity(), LanguageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                    }
        );

        return view;
    }

    public void setImage(ImageView imageView, int imageResource) {
        imageView.setImageResource(imageResource);
    }
}
