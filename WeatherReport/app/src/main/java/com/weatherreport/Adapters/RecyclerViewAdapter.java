package com.weatherreport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weatherreport.MODEL.allWeather;
import com.weatherreport.R;
import com.weatherreport.WeatherDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ajinkya.Pote on 17/02/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder> {

    private static Context mContext;
    private ArrayList<allWeather> totalWeather = new ArrayList<allWeather>();
    private LayoutInflater inflater;
    private String status;
    private static String cityName;


    public RecyclerViewAdapter(Context mContext, ArrayList<allWeather> totalWeather,String status) {
        this.mContext = mContext;
        this.totalWeather = totalWeather;
        inflater = LayoutInflater.from(mContext);
        this.status = status;
    }


    @Override
    public  WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.item, viewGroup, false);
        WeatherViewHolder pvh = new WeatherViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(WeatherViewHolder weatherViewHolder, int i) {
        if(!status.equalsIgnoreCase("all")){
            weatherViewHolder.tvCityName.setText(totalWeather.get(i).getCity().getName());
            weatherViewHolder.tvDateTime.setText(getDate(totalWeather.get(i).getList().get(i).getDt()));
            weatherViewHolder.tvDescription.setText(totalWeather.get(i).getList().get(i).getWeather().getDescription());
            weatherViewHolder.tvMaxTemp.setText(totalWeather.get(i).getList().get(i).getTempreture().getMax()+" \u00b0C");
            weatherViewHolder.tvMinTemp.setText(totalWeather.get(i).getList().get(i).getTempreture().getMin()+" \u00b0C");
            weatherViewHolder.tvDayTemp.setText(totalWeather.get(i).getList().get(i).getTempreture().getDay()+" \u00b0C");
            weatherViewHolder.tvNightTemp.setText(totalWeather.get(i).getList().get(i).getTempreture().getNight()+" \u00b0C");
            weatherViewHolder.tvWeathNext.setVisibility(View.INVISIBLE);
        }else{
            weatherViewHolder.tvCityName.setText(totalWeather.get(i).getCity().getName());
            weatherViewHolder.tvDateTime.setText(getDate(totalWeather.get(i).getList().get(0).getDt()));
            weatherViewHolder.tvDescription.setText(totalWeather.get(i).getList().get(0).getWeather().getDescription());
            weatherViewHolder.tvMaxTemp.setText(totalWeather.get(i).getList().get(0).getTempreture().getMax()+" \u00b0C");
            weatherViewHolder.tvMinTemp.setText(totalWeather.get(i).getList().get(0).getTempreture().getMin()+" \u00b0C");
            weatherViewHolder.tvDayTemp.setText(totalWeather.get(i).getList().get(0).getTempreture().getDay()+" \u00b0C");
            weatherViewHolder.tvNightTemp.setText(totalWeather.get(i).getList().get(0).getTempreture().getNight()+" \u00b0C");
            weatherViewHolder.tvWeathNext.setVisibility(View.VISIBLE);
        }
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
    public int getItemCount() {
        return this.totalWeather.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


public static class WeatherViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView tvCityName,tvDescription,tvDateTime,tvMinTemp,tvMaxTemp,tvNightTemp,tvDayTemp,tvWeathNext;

    WeatherViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view);
        tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
        tvDescription = (TextView) itemView.findViewById(R.id.tvWethDesc);
        tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
        tvMinTemp = (TextView) itemView.findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView) itemView.findViewById(R.id.tvMaxTemp);
        tvNightTemp = (TextView) itemView.findViewById(R.id.tvNightTemp);
        tvDayTemp = (TextView) itemView.findViewById(R.id.tvDayTemp);
        tvWeathNext = (TextView) itemView.findViewById(R.id.tvWeathNext);
        tvWeathNext.setVisibility(View.INVISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WeatherDetails.class);
                intent.putExtra("cityName", tvCityName.getText().toString());
                mContext.startActivity(intent);
                Log.d("cityName",tvCityName.getText().toString());
            }
        });

    }
}

}
