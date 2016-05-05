package com.example.sargis_kh.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sargis_kh.model.Company;
import com.example.sargis_kh.studioone.R;

/**
 * Created by Sargis_Kh on 5/2/2016.
 */
public class CompanyDialogFragment extends DialogFragment {

    private Company company;
    private TextView nameTextView;
    private EditText latEditText;
    private EditText lonEditText;

    private OnCompanyPositionChangedListener listener;

    public interface OnCompanyPositionChangedListener {
        public void CompanyPositionChanged(long companyId, double newLatitude, double newLognitude);
    }

    // Empty constructor required for DialogFragment
    public CompanyDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public CompanyDialogFragment(Company company) {
        this.company = company;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompanyPositionChangedListener) {
            listener = (OnCompanyPositionChangedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implemenet MyListFragment.OnItemSelectedListener");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_dialog, container);

        // set this instance as callback for editor action
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Set New Position");

        nameTextView = (TextView)view.findViewById(R.id.name_text_view_fragment_company_dialog);
        latEditText = (EditText)view.findViewById(R.id.lat_edit_text_fragment_company_dialog);
        lonEditText = (EditText)view.findViewById(R.id.lon_edit_text_fragment_company_dialog);
        nameTextView.setText(String.valueOf(company.getName()));
        latEditText.setText(String.valueOf(company.getLatitude()));
        lonEditText.setText(String.valueOf(company.getLongitude()));

        Button okButton = (Button)view.findViewById(R.id.ok_button_fragment_company_dialog);
        Button cancelButton = (Button)view.findViewById(R.id.cancel_button_fragment_company_dialog);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonPressed();
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void okButtonPressed() {
        double lat;
        double lon;
        if (latEditText.getText().toString().equals("")) {
            lat = 0;
        } else {
            lat = Double.valueOf(latEditText.getText().toString());
        }

        if (lonEditText.getText().toString().equals("")) {
            lon = 0;
        } else {
            lon = Double.valueOf(lonEditText.getText().toString());
        }

        listener.CompanyPositionChanged(company.getID(), lat, lon);
        getDialog().dismiss();
    }
}
