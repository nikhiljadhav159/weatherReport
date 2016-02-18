package com.weatherreport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weatherreport.Adapters.MultiSelectSpinner;
import com.weatherreport.Adapters.RecyclerViewAdapter;
import com.weatherreport.MODEL.Tempreture;
import com.weatherreport.MODEL.allWeather;
import com.weatherreport.MODEL.city;
import com.weatherreport.MODEL.weather;
import com.weatherreport.MODEL.weatherList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchCityActivity extends AppCompatActivity {

    private MultiSelectSpinner spinnerCity;
    private RecyclerView rvSearch;
    private ArrayList<String> cityNames = new ArrayList<String>();
    private ArrayList<String> cityId = new ArrayList<String>();
    private String CityId="";
    private TextView tvSearch;
    private com.weatherreport.MODEL.allWeather allWeather = new allWeather();
    private ArrayList<allWeather> totalWeather = new ArrayList<allWeather>();
    private ProgressDialog progressDialog;
    private Toolbar tbSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        LinearLayoutManager llm = new LinearLayoutManager(SearchCityActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(llm);
        rvSearch.setHasFixedSize(true);
        rvSearch.setAdapter(null);

        tbSearch = (Toolbar) findViewById(R.id.tbSearch);

        setSupportActionBar(tbSearch);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        initialiseComponents();
    }

    private void initialiseComponents() {

        spinnerCity = (MultiSelectSpinner) findViewById(R.id.spinnerCity);
        tvSearch = (TextView) findViewById(R.id.tvSearch);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        cityNames.add("Pune");
        cityNames.add("Mumbai");
        cityNames.add("Nagpur");
        cityNames.add("Aurangabad");

        cityId.add("1259229");
        cityId.add("1275339");
        cityId.add("1262180");
        cityId.add("1278149");

        setData();

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CityId.equalsIgnoreCase("")) {
                    progressDialog.show();
                    makeRequest();
                } else {
                    Toast.makeText(SearchCityActivity.this, "Please select one city.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void makeRequest() {

        String cityUrl = getResources().getString(R.string.all_city_url)+CityId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(cityUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Citydat",response.toString());

                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Citydat",error.toString());
            }
        });
        Volley.newRequestQueue(SearchCityActivity.this).add(jsonObjectRequest);
    }

    private void parseResponse(JSONObject response) {

        try {
            totalWeather.clear();
            JSONObject jsonResponse = response;
            JSONArray responseArray = jsonResponse.getJSONArray("list");
            for(int i =0;i<responseArray.length();i++){
                city city = new city();
                weatherList weatherList = new weatherList();
                Tempreture tempreture = new Tempreture();
                com.weatherreport.MODEL.weather weather = new weather();
                ArrayList<weatherList> weatherListArrayList = new ArrayList<weatherList>();
                allWeather = new allWeather();

                JSONObject jsonObject = responseArray.getJSONObject(i);
                city.setName(jsonObject.getString("name"));
                city.setId(jsonObject.getString("id"));

                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                weather.setId(weatherArray.getJSONObject(0).getString("id"));
                weather.setMain(weatherArray.getJSONObject(0).getString("main"));
                weather.setIcon(weatherArray.getJSONObject(0).getString("icon"));
                weather.setDescription(weatherArray.getJSONObject(0).getString("description"));

                JSONObject tempretureObj = jsonObject.getJSONObject("main");
                tempreture.setMax(tempretureObj.getString("temp_max"));
                tempreture.setMin(tempretureObj.getString("temp_min"));
                tempreture.setDay(tempretureObj.getString("temp"));
                tempreture.setNight(tempretureObj.getString("temp"));

                weatherList.setDt(jsonObject.getString("dt"));
                weatherList.setWeather(weather);
                weatherList.setTempreture(tempreture);

                weatherListArrayList.add(weatherList);
                allWeather.setCity(city);
                allWeather.setList(weatherListArrayList);
                totalWeather.add(allWeather);

            }

            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(SearchCityActivity.this,totalWeather,"all");
            rvSearch.setAdapter(recyclerViewAdapter);
            progressDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        spinnerCity.setItems(SearchCityActivity.this,cityNames, "Select City", -1, new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                // your operation with code...

                CityId = "";
                //int counter = 0;
                for (int i = 0; i < selected.length; i++) {

                    if (selected[i]) {
                        //Log.i("COUNTER", ""+counter);

                        Log.i("TAG", i + " : " + cityId.get(i));
                        if (CityId == null)
                            CityId = cityId.get(i);
                        else
                            CityId += "," + cityId.get(i);
                    }
                }
                if (CityId != null)
                    Log.i("TAG", CityId);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
