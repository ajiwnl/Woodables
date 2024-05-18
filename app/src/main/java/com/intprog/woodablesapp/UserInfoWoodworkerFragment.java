package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserInfoWoodworkerFragment extends Fragment {

    Button toReg;
    EditText lNameText,fNameText,mNameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_user_info_woodworker, container, false);

        lNameText = viewRoot.findViewById(R.id.lName);
        fNameText = viewRoot.findViewById(R.id.fName);
        mNameText = viewRoot.findViewById(R.id.mName);

        toReg = viewRoot.findViewById(R.id.toregbtn);

        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent toRegister = new Intent(viewRoot.getContext(), RegisterActivity.class);

                toRegister.putExtra("LName", lNameText.getText().toString());
                toRegister.putExtra("FName", fNameText.getText().toString());
                toRegister.putExtra("MName", mNameText.getText().toString());

                startActivity(toRegister);

                Toast.makeText(viewRoot.getContext(), toRegister.getExtras().toString() , Toast.LENGTH_LONG).show();
            }
        });

        return viewRoot;
    }
}