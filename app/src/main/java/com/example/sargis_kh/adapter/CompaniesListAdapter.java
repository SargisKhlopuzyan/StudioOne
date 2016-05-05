package com.example.sargis_kh.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sargis_kh.model.Company;
import com.example.sargis_kh.studioone.R;

import java.util.ArrayList;

/**
 * Created by Sargis_Kh on 5/2/2016.
 */
public class CompaniesListAdapter extends ArrayAdapter<Company> {

    private final Context context;
    private final ArrayList<Company> companies;


    public CompaniesListAdapter(Context context, ArrayList<Company> list) {
        super(context, R.layout.row_company_list_layout, list);
        this.context = context;
        this.companies = list;
    }

    static class ViewHolder {
        protected TextView nameTextView;
        protected TextView latTextView;
        protected TextView lonTextView;
        protected ImageView logoImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_company_list_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) view.findViewById(R.id.name_row_company_list_layout);
            viewHolder.latTextView = (TextView) view.findViewById(R.id.lat_row_company_list_layout);
            viewHolder.lonTextView = (TextView) view.findViewById(R.id.lon_row_company_list_layout);
            viewHolder.logoImageView = (ImageView) view.findViewById(R.id.logo_row_company_list_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Company company = companies.get(position);

        // set up the list item
        if (company != null) {
            viewHolder.nameTextView.setText(companies.get(position).getName());
            viewHolder.latTextView.setText(String.valueOf(companies.get(position).getLatitude()));
            viewHolder.lonTextView.setText(String.valueOf(companies.get(position).getLongitude()));

            Drawable logoDrawable;
            try {
                logoDrawable = context.getResources().getDrawable(context.getResources().getIdentifier(companies.get(position).getLogo(), "drawable", context.getPackageName()));
            } catch (Resources.NotFoundException e) {
                logoDrawable = context.getResources().getDrawable(context.getResources().getIdentifier("logo", "drawable", context.getPackageName()));
            }
            viewHolder.logoImageView.setImageDrawable(logoDrawable);
        }
        return view;
    }
}