package com.workstation.napnn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageAdapter mImageAdapter;
    List<String> image, title;
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = new ArrayList<String>();
        title = new ArrayList<String>();

        UpdateScreen();

        mImageAdapter = new ImageAdapter(this, image, title);

        mGridView = (GridView) findViewById(R.id.grid_main);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                int image_position = (int) mImageAdapter.getItemId(position);
//                String detail_image= image.get(image_position);
                String detail_title=title.get(image_position);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra("IMAGE",detail_image);
                intent.putExtra("TITLE",detail_title);

                startActivity(intent);
            }
        });

    }

    private void UpdateScreen() {
        FetchDetails categoryFetch = new FetchDetails();
        categoryFetch.execute();
    }

    public class FetchDetails extends AsyncTask<Void, Void, List<String>> {

        private final String LOG_TAG = FetchDetails.class.getSimpleName();

        private List<String> getdataFromJson(String json_string) throws JSONException {
            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject categoryList = jsonArray.getJSONObject(i);
                title.add(categoryList.getString("name"));
            }
            Log.v(LOG_TAG, "titles_categories" + title);
            return title;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String json_string = null;

            try {
                final String BASE_URL = "https://api.vimeo.com/categories";

                URL url = new URL(BASE_URL);
                Log.v(LOG_TAG, "url" + BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "bearer b2918607b4301812914f5a5141c4d7da");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    json_string = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "/n");
                }

                if (buffer.length() == 0) {
                    json_string = null;
                }

                json_string = buffer.toString();
                Log.v(LOG_TAG, "json_string" + json_string);
            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                json_string = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "error closing stream", e);
                    }
                }
            }
            try {
                return getdataFromJson(json_string);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result)
        {
            super.onPostExecute(result);
            mImageAdapter.clear();
            mImageAdapter.notifyDataSetChanged();
            for (String r:result)
            {
                mImageAdapter.add(r);
            }
        }
    }
}
