package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MessageChatViewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_message_chat_view, container, false);

        ImageView tosetting;

        tosetting = viewRoot.findViewById(R.id.toSetting);


        tosetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetting = new Intent(viewRoot.getContext(), MessageSettingActivity.class);
                startActivity(toSetting);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); Error on Fragment
            }
        });

        return viewRoot;
    }
}