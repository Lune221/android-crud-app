package com.example.helloworldapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.helloworldapp.controllers.RecyclerAdapter;
import com.example.helloworldapp.models.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String KEY = "sarralioune@ept.sn";

    public static String ACTION = "ADD";
    private ArrayList<Person> personsList;
    private RecyclerView recyclerView;
    private EditText prenomEdit;
    private EditText nomEdit;
    private EditText emailEdit;
    private DatePicker dateNaissancepicker;
    private Button validerBtn;
    private Button annulerBtn;
    private RequestQueue mRequestQueue;
    private LinearLayout form;
    private JsonArrayRequest jaRequest;
    private String url = "http://185.98.128.121/api/" + KEY + "/personnes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        personsList = new ArrayList<>();

        this.linkComponents();
        this.setListeners();
        this.setRVConfig();
        this.sendAndRequestResponse();
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ajouterBtn) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
    public void linkComponents() {
        recyclerView = findViewById(R.id.rc_view);
        prenomEdit = findViewById(R.id.firstname_input);
        nomEdit = findViewById(R.id.lastname_input);
        dateNaissancepicker = findViewById(R.id.date_input);
        validerBtn = findViewById(R.id.validate);
        annulerBtn = findViewById(R.id.cancel);
        emailEdit = findViewById(R.id.email_input);
        form = findViewById(R.id.form);
    }

    public void setListeners(){
        validerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addOrUpdatePerson(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        annulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Action annulée", Toast.LENGTH_LONG).show();
                nomEdit.setText("");
                prenomEdit.setText("");
                emailEdit.setText("");
                ACTION = "ADD";
            }
        });
    }
    private void setAdapter(ArrayList<Person> persons) {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(persons, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setRVConfig() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void sendAndRequestResponse() {

        //String Request initialized
        jaRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                // Log.i(TAG, response.toString());
                ArrayList<Person> persons = new ArrayList<Person>();

                if (response != null && response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject personObj = response.getJSONObject(i);
                            personsList.add(
                                    new Person(
                                            (String) personObj.get("prenom"),
                                            (String) personObj.get("nom"),
                                            (String) personObj.get("email"),
                                            (String) personObj.get("clef"),
                                            (personObj.has("dateNaissance")) ? formatDate((String) personObj.get("dateNaissance")) : "-"
                                    )
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            // personsList = persons;
                        }
                    }
                    setAdapter(personsList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(jaRequest);
    }

    private void addOrUpdatePerson(View view) throws JSONException {
        if(prenomEdit.getText().toString().trim().equals("") || nomEdit.getText().toString().trim().equals("") || emailEdit.getText().toString().trim().equals("")){
            return;
        }
        JSONObject JSONBody = new JSONObject();
        final Person person = new Person(prenomEdit.getText().toString(), nomEdit.getText().toString(), emailEdit.getText().toString(), "SA-" + System.currentTimeMillis());
        JSONBody.put("prenom", person.getPrenom());
        JSONBody.put("nom", person.getNom());
        // JSONBody.put("dateNaissance", dateNaissancepicker.getMaxDate());
        JSONBody.put("clef", person.getClef());
        Log.i(this.getClass().getName(), "########## " + ACTION);
        if(ACTION.equals("ADD")) JSONBody.put("email", person.getEmail());

        final String requestBody = JSONBody.toString();
        Log.i(MainActivity.class.getName(), requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url + (ACTION.equals("UPDATE") ? "/" + person.getEmail() : ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")){
                    if(ACTION.equals("ADD")) {
                        personsList.add(person);
                        setAdapter(personsList);
                        Toast.makeText(getApplicationContext(), "Personne ajoutée.", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Personne modifiée.", Toast.LENGTH_LONG).show();
                        resetEditView();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        mRequestQueue.add(stringRequest);
    }

    public void delete(String email){
        Log.i("######## DELETING", "DDDDDDDDDDDDDD");
        JSONObject js = new JSONObject();
        try {
            js.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE, url + "/" + email, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);
        Context context = getApplicationContext();
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
    private String formatDate(String s) {
        return s.split("T")[0];
    }

    public void setEditView(Person person){
        prenomEdit.setText(person.getPrenom());
        nomEdit.setText(person.getNom());
        emailEdit.setText(person.getEmail());
        ACTION = "UPDATE";
    }
    public void resetEditView(){
        prenomEdit.setText("");
        nomEdit.setText("");
        emailEdit.setText("");
        ACTION = "ADD";
    }
}
