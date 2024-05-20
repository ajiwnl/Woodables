package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoClientFragment extends Fragment {

    Button toReg;
    EditText lNameText, fNameText, mNameText, cNameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_user_info_client, container, false);

        lNameText = viewRoot.findViewById(R.id.lName);
        fNameText = viewRoot.findViewById(R.id.fName);
        mNameText = viewRoot.findViewById(R.id.mName);
        cNameText = viewRoot.findViewById(R.id.cName);

        toReg = viewRoot.findViewById(R.id.toregbtn);

        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity activity = (UserInfoActivity) getActivity();
                Intent toRegister = new Intent(viewRoot.getContext(), RegisterActivity.class);

                toRegister.putExtra("LName", lNameText.getText().toString());
                toRegister.putExtra("FName", fNameText.getText().toString());
                toRegister.putExtra("MName", mNameText.getText().toString());
                toRegister.putExtra("CName", cNameText.getText().toString());
                toRegister.putExtra("Role", activity.getSelectedRole());

                startActivity(toRegister);
            }
        });

        return viewRoot;
    }
}