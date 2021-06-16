package com.example.activity6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity6.api.ApiUtilities;
import com.example.activity6.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_totalcon,text_totalact, text_totalrec, text_totaldeath,text_totaltests;
    TextView text_todaycon,text_todayrec, text_todaydeath;
    TextView text_date;
    PieChart piechart;

    private List<CountryData> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        initial();

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCountry().equals("Philippines")) {
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int death = Integer.parseInt(list.get(i).getDeaths());


                        text_totalcon.setText(NumberFormat.getInstance().format(confirm));
                        text_totalact.setText(NumberFormat.getInstance().format(active));
                        text_totalrec.setText(NumberFormat.getInstance().format(recovered));
                        text_totaldeath.setText(NumberFormat.getInstance().format(death));

                        text_todaydeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        text_todaycon.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        text_todayrec.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        text_totaltests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));


                        setText(list.get(i).getUpdated());

                        piechart.addPieSlice(new PieModel("Confirm",confirm, Color.parseColor("#FFEB3B")));
                        piechart.addPieSlice(new PieModel("Active",active, Color.parseColor("#F44336")));
                        piechart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#8BC34A")));
                        piechart.addPieSlice(new PieModel("Death",death, Color.parseColor("#000000")));
                    }

                }

            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setText(String updated) {

        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        text_date.setText("Updated at "+format.format(calendar.getTime()));
    }
    private void initial(){
        text_totalcon = findViewById(R.id.txt_conf);
        text_totalact = findViewById(R.id.txt_active);
        text_totaldeath = findViewById(R.id.txt_death);
        text_totalrec = findViewById(R.id.txt_rec);
        text_totaltests = findViewById(R.id.txt_test);
        text_todaycon = findViewById(R.id.txt_tconf);
        text_todaydeath = findViewById(R.id.txt_tdeath);
        text_todayrec = findViewById(R.id.txt_trec);
        text_date = findViewById(R.id.txt_date);
        piechart = findViewById(R.id.piechart);



    }
}