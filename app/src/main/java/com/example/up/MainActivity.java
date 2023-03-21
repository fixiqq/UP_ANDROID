package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    ListView listView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = findViewById(R.id.image);
        recyclerView = findViewById(R.id.my_recycler_view);
        listView = findViewById(R.id.QuotesList);
        listView.setDivider(new ColorDrawable(ContextCompat.getColor(this, R.color.background)));
        listView.setDividerHeight(48);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // Установка ориентации горизонтальной
        recyclerView.setLayoutManager(layoutManager);
        String imageUrl = Class_User.avatar;
        Picasso.get()
                .load(imageUrl)
                .transform(new BeCircle())
                .into(image);
        TextView w_name = findViewById(R.id.welcome_name);
        w_name.setText("С возвращением, " + Class_User.nickName);

        getQuotes taskq = new getQuotes(listView, MainActivity.this);
        taskq.execute();
        getFeelings taskf = new getFeelings(recyclerView, MainActivity.this);
        taskf.execute();
    }

    class getQuotes extends AsyncTask<Void, Void, List<Class_Quote>>{
        private ListView listView;
        private Context context;

        public getQuotes(ListView listView, Context context) {
            this.listView = listView;
            this.context = context;
        }

        @Override
        protected List<Class_Quote> doInBackground(Void... voids) {
            List<Class_Quote> quoteList = new ArrayList<Class_Quote>();
            try {
                // создаем соединение
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/quotes");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                int responseCode = conn.getResponseCode();
                // Чтение ответа сервера
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    JSONObject response = new JSONObject(sb.toString());
                    // Обработка ответа сервера

                    JSONArray json_array = new JSONArray(response.getString("data"));
                    for (int i = 0; i < json_array.length(); i++) {
                        JSONObject json_object = json_array.getJSONObject(i);
                        Class_Quote quote = new Class_Quote();
                        quote.image = json_object.getString("image");
                        quote.title = json_object.getString("title");
                        quote.description = json_object.getString("description");
                        quoteList.add(quote);
                    }
                    //Заполняем список
                    Adapter_LW adapter = new Adapter_LW(MainActivity.this,quoteList);
                    listView.setAdapter(adapter);

                } else {
                    Log.e("TAG", "Error: " + responseCode);
                }

                conn.disconnect();
            }
            catch (Exception e){
                Log.d("e",e.toString());
            }
            return quoteList;
        }
        @Override
        protected void onPostExecute(List<Class_Quote> quoteList) {
            super.onPostExecute(quoteList);
            //Заполняем список
            Adapter_LW adapter = new Adapter_LW(MainActivity.this,quoteList);
            listView.setAdapter(adapter);
        }
    }

    class getFeelings extends AsyncTask<Void, Void, List<Class_Feel>>{
        private RecyclerView recyclerView;
        private Context context;

        public getFeelings(RecyclerView recyclerView, Context context) {
            this.recyclerView = recyclerView;
            this.context = context;
        }

        @Override
        protected List<Class_Feel> doInBackground(Void... voids) {
            List<Class_Feel> feelList = new ArrayList<Class_Feel>();
            try {
                // создаем соединение
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/feelings");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                int responseCode = conn.getResponseCode();
                // Чтение ответа сервера
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    JSONObject response = new JSONObject(sb.toString());
                    // Обработка ответа сервера
                    JSONArray json_array = new JSONArray(response.getString("data"));
                    for (int i = 0; i < json_array.length(); i++) {
                        JSONObject json_object = json_array.getJSONObject(i);
                        Class_Feel feel = new Class_Feel();
                        feel.image = json_object.getString("image");
                        feel.title = json_object.getString("title");
                        feel.position = json_object.getInt("position");
                        feelList.add(feel);
                    }
                    //Заполняем список
                    Adapter_RW adapter = new Adapter_RW(feelList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Log.e("TAG", "Error: " + responseCode);
                }

                conn.disconnect();
            }
            catch (Exception e){
                Log.d("e",e.toString());
            }
            return feelList;
        }
        @Override
        protected void onPostExecute(List<Class_Feel> feelList) {
            super.onPostExecute(feelList);
            //Заполняем список
            Adapter_RW adapter = new Adapter_RW(feelList);
            recyclerView.setAdapter(adapter);
        }
    }

    public void GoProfile(View v){
        Intent profile = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(profile);
    }

}