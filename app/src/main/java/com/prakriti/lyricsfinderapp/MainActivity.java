package com.prakriti.lyricsfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LYRICS_KEY = "lyrics";
    EditText edtArtist, edtSong;
    Button btnGetLyrics;
    TextView txtLyrics;
    // https://api.lyrics.ovh/v1/Rihanna/Diamonds#

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    String url;
    String errorText = "Invalid entry\nTry again";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // connect java variables to UI components
        edtArtist = findViewById(R.id.edtArtist);
        edtSong = findViewById(R.id.edtSong);
        btnGetLyrics = findViewById(R.id.btnGetLyrics);
        txtLyrics = findViewById(R.id.txtLyrics);

        btnGetLyrics.setOnClickListener(MainActivity.this);

        if(savedInstanceState != null) {
            String lyricsText = savedInstanceState.getString(LYRICS_KEY);
            txtLyrics.setText(lyricsText);
        }

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LYRICS_KEY, txtLyrics.getText().toString());
    }


    @Override
    public void onClick(View v) {
        if(edtArtist.getText().toString() == null || edtSong.getText().toString() == null) {
            txtLyrics.setText("Invalid entry\nTry again");
            return;
        }
        url = "https://api.lyrics.ovh/v1/" + edtArtist.getText().toString() + "/" + edtSong.getText().toString() + "#";
        url.replace(" ", "%20");
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            txtLyrics.setText(response.getString("lyrics"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtLyrics.setText(errorText);
                Log.i("LYRICSAPP", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}