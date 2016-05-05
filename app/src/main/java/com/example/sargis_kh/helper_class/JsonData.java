package com.example.sargis_kh.helper_class;

import android.util.Log;

import com.example.sargis_kh.model.Company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sargis_Kh on 5/3/2016.
 */
public class JsonData {

    private static JSONArray writeJSON() {
        JSONArray jsonArray = new JSONArray();

        JSONObject object0 = new JSONObject();
        try {
            object0.put("id", 0);
            object0.put("name", "Kamurj UCO");
            object0.put("lat", new Double(40.198140));
            object0.put("lon", new Double(44.491170));
            object0.put("logo", "kamurj_uco");
            jsonArray.put(object0);
        } catch (JSONException e) {
        }

        JSONObject object1 = new JSONObject();
        try {
            object1.put("id", 1);
            object1.put("name", "HayPost");
            object1.put("lat", new Double(40.198722));
            object1.put("lon", new Double(44.492884));
            object1.put("logo", "hay_post");
            jsonArray.put(object1);
        } catch (JSONException e) {
        }

        JSONObject object2 = new JSONObject();
        try {
            object2.put("id", 2);
            object2.put("name", "Estate.am");
            object2.put("lat", new Double(40.199751));
            object2.put("lon", new Double(44.491188));
            object2.put("logo", "estate_am");
            jsonArray.put(object2);
        } catch (JSONException e) {
        }

        JSONObject object3 = new JSONObject();
        try {
            object3.put("id", 3);
            object3.put("name", "Embassy of Romania");
            object3.put("lat", new Double(40.198127));
            object3.put("lon", new Double(44.486828));
            object3.put("logo", "embassy_of_romania");
            jsonArray.put(object3);
        } catch (JSONException e) {
        }

        JSONObject object4 = new JSONObject();
        try {
            object4.put("id", 4);
            object4.put("name", "Embassy of Egypt");
            object4.put("lat", new Double(40.197027));
            object4.put("lon", new Double(44.487718));
            object4.put("logo", "embassy_of_egypt");
            jsonArray.put(object4);
        } catch (JSONException e) {
        }

        JSONObject object5 = new JSONObject();
        try {
            object5.put("id", 5);
            object5.put("name", "Grand Candy Shop");
            object5.put("lat", new Double(40.216766));
            object5.put("lon", new Double(44.581317));
            object5.put("logo", "logo");
            jsonArray.put(object5);
        } catch (JSONException e) {
        }

        JSONObject object6 = new JSONObject();
        try {
            object6.put("id", 6);
            object6.put("name", "Kanaker-Zeytun MC");
            object6.put("lat", new Double(40.204457));
            object6.put("lon", new Double(44.533086));
            object6.put("logo", "logo");
            jsonArray.put(object6);
        } catch (JSONException e) {
        }

        JSONObject object7 = new JSONObject();
        try {
            object7.put("id", 7);
            object7.put("name", "Kerama Marazzi");
            object7.put("lat", new Double(40.201675));
            object7.put("lon", new Double(44.532742));
            object7.put("logo", "logo");
            jsonArray.put(object7);
        } catch (JSONException e) {
        }
        return jsonArray;
    }


    public static ArrayList<Company> readJSON() {

        JSONArray jsonArray = writeJSON();
        ArrayList<Company> companies = new ArrayList<Company>();

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.optLong("id");
                String name = jsonObject.optString("name");
                double lat = jsonObject.optDouble("lat");
                double lon = jsonObject.optDouble("lon");
                String logoName = jsonObject.optString("logo");

                Company company = new Company(id, name, lat, lon, logoName);
                companies.add(company);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return companies;
    }
}
