package com.weatherreport;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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


public class WeatherDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private allWeather allWeather = new allWeather();
    private ArrayList<allWeather> totalWeather = new ArrayList<allWeather>();
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(WeatherDetails.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mToolbar = (Toolbar) findViewById(R.id.tbDetails);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String location = getIntent().getStringExtra("cityName");
        setListData(location);
        //setListData("London");


    }

    private void setListData(String location) {

        String url16 = getResources().getString(R.string.weather14_url)+location;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url16, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("16data", response.toString());
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Log.d("16data",error.toString());
            }
        });

        Volley.newRequestQueue(WeatherDetails.this).add(jsonObjectRequest);
    }

    private void parseResponse(JSONObject response) {

        city city = new city();

        weatherList weatherList = new weatherList();
        Tempreture tempreture = new Tempreture();
        com.weatherreport.MODEL.weather weather = new weather();

        ArrayList<weatherList> weatherListArrayList = new ArrayList<weatherList>();

         JSONObject jsonResponse = response;
        try {
            JSONObject cityObj = jsonResponse.getJSONObject("city");
            city.setId(cityObj.getString("id"));
            city.setName(cityObj.getString("name"));
            city.setCountry(cityObj.getString("country"));

            JSONArray listArray = jsonResponse.getJSONArray("list");
            for (int i = 0;i<listArray.length();i++){
                weatherList = new weatherList();
                tempreture = new Tempreture();
                weather = new weather();
                weatherList.setDt(listArray.getJSONObject(i).getString("dt"));
                weatherList.setHumidity(listArray.getJSONObject(i).getString("humidity"));
                weatherList.setPressure(listArray.getJSONObject(i).getString("pressure"));

                JSONObject tempObj = listArray.getJSONObject(i).getJSONObject("temp");

                tempreture.setDay("" + tempObj.get("day"));
                tempreture.setEve("" + tempObj.get("eve"));
                tempreture.setMax("" + tempObj.get("max"));
                tempreture.setMin("" + tempObj.get("min"));
                tempreture.setMorn("" + tempObj.get("morn"));
                tempreture.setNight("" + tempObj.get("night"));
                weatherList.setTempreture(tempreture);

                JSONArray weatherArray = listArray.getJSONObject(i).getJSONArray("weather");

                weather.setId(weatherArray.getJSONObject(0).getString("id"));
                weather.setDescription(weatherArray.getJSONObject(0).getString("description"));
                weather.setIcon(weatherArray.getJSONObject(0).getString("icon"));
                weather.setMain(weatherArray.getJSONObject(0).getString("main"));

                weatherList.setWeather(weather);

                weatherListArrayList.add(weatherList);
                allWeather.setCity(city);
                allWeather.setList(weatherListArrayList);
                totalWeather.add(allWeather);
            }

            Log.d("listArray ", "" + weatherListArrayList.size() );

            if(totalWeather.size()==14){
                progressDialog.hide();
                intialiseComponents();

            }

            Log.d("allweather ", "" + totalWeather.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void intialiseComponents() {

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(WeatherDetails.this,totalWeather,"");
        recyclerView.setAdapter(recyclerViewAdapter);
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
