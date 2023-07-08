package com.example.Weather_360;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onboardingscreens.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = "a62f19204db4271d56c8d5f2dc4c0dc3";
    TextView cityNameView, temperature, weatherDescription;
    SearchView searchBox;
    RequestQueue mQueue;
    String url, urlTemplate, units;
    ImageView weatherIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSplashScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameView = findViewById(R.id.city);
        temperature = findViewById(R.id.temperature);
        weatherDescription = findViewById(R.id.description_weather);
        searchBox = findViewById(R.id.search_box);
        weatherIconView = findViewById(R.id.weather_icon);

        mQueue = Volley.newRequestQueue(this);

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jsonParse(query);
                Log.i("mine", "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void jsonParse(String query) {
        String units = "metric";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + query + "&appid=" + API_KEY + "&units=" + units;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Getting the array i want from the JSON
                    //For temperature
                    JSONObject jsonobj = response.getJSONObject("main");
                    int temperatureASInt = (int) jsonobj.getDouble("temp");
                    temperature.setText(temperatureASInt + getString(R.string.degree_symbol));
                    Log.i("mine", "temp: " + temperatureASInt);

                    //For Description
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObj = weather.getJSONObject(0);
                    String desc = weatherObj.getString("description");
                    weatherDescription.setText(desc);

                    //For City name
                    JSONArray cname = response.getJSONArray("name");
                    String name = (String) response.getString("name");
                    cityNameView.setText(name);

                  // Icon View
                    String iconCode = weatherObj.getString("icon");
                    switch (iconCode) {
                        // clear sky
                        case "01d":
                        case "01n":
                            weatherIconView.setImageResource(R.drawable.sunny);
                            break;

                        // few clouds
                        case "02d":
                        case "02n":
                            weatherIconView.setImageResource(R.drawable.less_clouds);
                            break;

                        // scattered clouds
                        case "03d":
                        case "03n":
                            weatherIconView.setImageResource(R.drawable.scattered_clouds);
                            break;

                        // broken clouds in the sky
                        case "04d":
                        case "04n":
                            weatherIconView.setImageResource(R.drawable.broken_cloud);
                            break;

                        // shower rain
                        case "09d":
                        case "09n":
                            weatherIconView.setImageResource(R.drawable.little_rain);
                            break;

                        // rain
                        case "10d":
                        case "10n":
                            weatherIconView.setImageResource(R.drawable.heavy_rain);
                            break;

                        // thunderstorm
                        case "11d":
                        case "11n":
                            weatherIconView.setImageResource(R.drawable.thunderstorm);
                            break;

                        // snow
                        case "13d":
                        case "13n":
                            weatherIconView.setImageResource(R.drawable.heavy_snow);
                            break;

                        // mist
                        case "50d":
                        case "50n":
                            weatherIconView.setImageResource(R.drawable.fog);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
