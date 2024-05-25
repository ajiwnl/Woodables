package com.intprog.woodablesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.Log;

import java.util.List;

public class AssessmentAdapter extends ArrayAdapter<Assessment> {

    public AssessmentAdapter(Context context, List<Assessment> assessments) {
        super(context, 0, assessments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Assessment assessment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_assessment, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.assessmentName);
        TextView courseTextView = convertView.findViewById(R.id.assessmentCourse);

        nameTextView.setText(assessment.getFirstName() + " " + assessment.getLastName());
        courseTextView.setText(assessment.getCourse());

        Log.d("Adapter", "Binding assessment: " + assessment.getFirstName() + " " + assessment.getLastName());

        return convertView;
    }
}