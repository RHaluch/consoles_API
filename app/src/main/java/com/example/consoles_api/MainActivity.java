package com.example.consoles_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textData;
    private EditText editName, editYear;
    private List<String> consoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textData = findViewById(R.id.textData);
        editName = findViewById(R.id.editName);
        editYear = findViewById(R.id.editYear);
        consoles = new ArrayList<>();

        carregarDados();


    }

    private void adicionarConsole(String url, Console console){
        JSONObject object = new JSONObject();
        try {
            object.put("nome",console.getName());
            object.put("year",console.getYear());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String data = response.toString();
                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        APISingleton.getInstance(this).addToRequestQueue(request);
    }

    private void carregarDados() {

        String url = "https://my-json-server.typicode.com/RHaluch/ConsolesAPI/consoles";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                String tmp = "Console: " + object.getString("nome");
                                tmp+="\nAno: " + object.getInt("year") + "\n";
                                consoles.add(tmp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        exibirDados();
                        
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        APISingleton.getInstance(this).addToRequestQueue(request);
    }
    private void exibirDados() {
        String tmp="";
        for (String dado:consoles){
            tmp += dado;
        }
        textData.setText(tmp);
    }

    public void btnSalvar(View view) {
        Console console = new Console();
        console.setName(String.valueOf(editName.getText()));
        console.setYear(Integer.parseInt(String.valueOf(editYear.getText())));

        String url = "https://my-json-server.typicode.com/RHaluch/ConsolesAPI/consoles";

        adicionarConsole(url, console);
    }
}
