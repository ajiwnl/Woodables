package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private ListView adminListView;
    private ArrayList<String> itemList;
    private ArrayAdapter<String> adapter;

    private Button toAssementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminListView = findViewById(R.id.adminListView);
        toAssementButton = findViewById(R.id.toAssement);

        itemList = new ArrayList<>();
        // Add some sample items
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");

        adapter = new ArrayAdapter<String>(this, R.layout.viewlisting_item, R.id.itemText, itemList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button deleteButton = view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                return view;
            }
        };

        adminListView.setAdapter(adapter);

        toAssementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminAssesmentActivity.class);
                startActivity(intent);
            }
        });
    }
}