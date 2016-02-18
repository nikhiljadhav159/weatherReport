package com.weatherreport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.weatherreport.GPS.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ImageView ivWeathImage;
    private TextView tvWethDesc,tvCityName,tvDateTime,tvNext14,tvMaxTemp,tvMinTemp,tvDayTemp,tvNightTemp,tvNoGps;
    private String location;
    private LineChartView linechart;
    private RelativeLayout rlReport;

    private CardView card_view;
    private final String[] mLabels= {"Jan", "Feb", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep","Oct","Nov"};
    private final float[][] mValues = {{35f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f,6.5f,0.1f},
            {4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f, 8.3f, 1.8f}};
    private Runnable mBaseAction;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseComponents();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        mToolbar = (Toolbar) findViewById(R.id.tbMain);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        tvNext14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchCityActivity.class));
            }
        });
    }

    private void initialiseComponents() {
        card_view = (CardView) findViewById(R.id.card_view);
        rlReport = (RelativeLayout) findViewById(R.id.rlReport);
        tvNext14 = (TextView)findViewById(R.id.tvNext14);
        ivWeathImage = (ImageView) findViewById(R.id.ivWeathImage);
        linechart = (LineChartView) findViewById(R.id.linechart);
        tvWethDesc = (TextView) findViewById(R.id.tvWethDesc);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvDateTime = (TextView) findViewById(R.id.tvDateTime);

        tvMaxTemp = (TextView) findViewById(R.id.tvMaxTemp);
        tvMinTemp = (TextView) findViewById(R.id.tvMinTemp);
        tvDayTemp = (TextView) findViewById(R.id.tvDayTemp);
        tvNightTemp = (TextView) findViewById(R.id.tvNightTemp);
        tvNoGps = (TextView) findViewById(R.id.tvNoGps);
    }

    private void getUserLocation() {
        GPSTracker gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()){
            progressDialog.show();
            rlReport.setVisibility(View.VISIBLE);
            tvNoGps.setVisibility(View.GONE);
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Geocoder geocoder = new Geocoder(
                    MainActivity.this, Locale
                    .getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                String CityName = addresses.get(0).getLocality()+","+addresses.get(0).getCountryCode();

                makeVolleyRequest(CityName);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else{
            showSettingsAlert();
            tvNoGps.setVisibility(View.VISIBLE);
            rlReport.setVisibility(View.GONE);
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    private void makeVolleyRequest(String location) {
        String url1 =MainActivity.this.getResources().getString(R.string.location_url)+location;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, url1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());

                setResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", error.toString());

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void setResponse(JSONObject response) {

        JSONObject jsonResponse = response;
        try {
            tvDateTime.setText(getDate(jsonResponse.get("dt").toString()));


            JSONArray weathArray = jsonResponse.getJSONArray("weather");
            tvWethDesc.setText(weathArray.getJSONObject(0).getString("description"));
            getWeathIcon(weathArray.getJSONObject(0).getString("icon"));

            JSONObject mainObj = jsonResponse.getJSONObject("main");
            tvMaxTemp.setText(mainObj.getString("temp_max")+" \u00b0C");
            tvMinTemp.setText(mainObj.getString("temp_min")+" \u00b0C");
            tvDayTemp.setText(mainObj.getString("temp")+" \u00b0C");
            tvNightTemp.setText(mainObj.getString("temp")+" \u00b0C");

            tvCityName.setText(jsonResponse.getString("name")+", "+mainObj.getString("temp")+" \u00b0C");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWeathIcon(String icon) {
        String imgUrl = getResources().getString(R.string.img_url)+icon+".png";

        ImageRequest imageRequest = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivWeathImage.setImageBitmap(bitmap);
                        progressDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        //ivWeathImage.setImageResource(R.drawable.image_load_error);
                    }
                });

        Volley.newRequestQueue(MainActivity.this).add(imageRequest);
    }

    public String getDate(String timeStampStr){

        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(Long.parseLong(timeStampStr)*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
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


       /* if (id == android.R.id.home) {
            this.finish();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserLocation();
    }
}
