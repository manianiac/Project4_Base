package com.example.listview;
//test this
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.JSONHelper;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Activity_ListView extends ListActivity {
    private static final String BIKEJSON = "bikes.json";
    private static final String URL_JSON = "URLPref";
    private static final String TAG = "Activity_ListView";
    private static final int TIMEOUT = 1000;
    private static final int SETTINGS_ACTIVITY = 42;
    private ArrayList<BikeData> bikeDataArrayList;
    private SharedPreferences myPreference;
    OnSharedPreferenceChangeListener listener;
    CustomAdapter adapter;


    OnItemSelectedListener mySpinnerListener;



    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // listen for a change to the URL_JSON key,
        //its the key part of the key value pair that holds the URL to load
        //when this key changes then the URL has changed, so reload it
        //make this listener an instance var so it is not GCed due to it being
        //saved as a weak reference
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.d(TAG, "Preference key =" + key);
                if (key.equals(URL_JSON)) {
                    showToast("BLARG");
                    //TODO handle URL changes
                }
            }
        };
        myPreference.registerOnSharedPreferenceChangeListener(listener);

        //listen for a spinner change
        mySpinnerListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                Log.d(TAG, "Spinner changed " + selectedItemView.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };

        ConnectivityCheck check = new ConnectivityCheck(this);
        if(check.isNetworkReachableAlertUserIfNot()) {
            downloadJSON task = new downloadJSON(this);
            task.execute(myPreference.getString("websiteName", "I AM FILLER") + BIKEJSON);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case SETTINGS_ACTIVITY: {
                if (resultCode == 0) {
                    mySpinnerListener = new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                            Log.d(TAG, "Spinner changed " + selectedItemView.toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    };
                    ConnectivityCheck check = new ConnectivityCheck(this);
                    if (check.isNetworkReachableAlertUserIfNot()) {
                            downloadJSON task = new downloadJSON(this);
                            task.execute(myPreference.getString("websiteName", "I AM FILLER") + BIKEJSON);
                    }
                }
            }
        }
    }

    //Part of the download JSON section
    private class downloadJSON extends AsyncTask<String, Void, String> {
        private final Activity_ListView myActivity;

        public downloadJSON(Activity_ListView myActivity){
            this.myActivity = myActivity;
        }

        @Override
        protected String doInBackground(String... params){
            // site we want to connect to
            String myURL = params[0];
//TODO why can this download the json from the CNU page?
            try {
                String myQuery = "";
                URL url = new URL(myURL + myQuery);

                // this does no network IO
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // can further configure connection before getting data
                // cannot do this after connected
                connection.setRequestMethod("GET");
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(TIMEOUT);
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                // this opens a connection, then sends GET & headers

                // wrap in finally so that stream bis is sure to close
                // and we disconnect the HttpURLConnection
                BufferedReader in = null;
                try {
                    connection.connect();

                    // lets see what we got make sure its one of
                    // the 200 codes (there can be 100 of them
                    // http_status / 100 != 2 does integer div any 200 code will = 2
                    int statusCode = connection.getResponseCode();
                    if (statusCode / 100 != 2) {
                        Log.e(TAG, "Error-connection.getResponseCode returned "
                                + Integer.toString(statusCode));
                        return null;
                    }

                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8096);

                    // the following buffer will grow as needed
                    String myData;
                    //StringBuffer sb = new StringBuffer();
                    StringBuilder sb = new StringBuilder();

                    while ((myData = in.readLine()) != null) {
                        sb.append(myData);
                    }
                    return sb.toString();

                } finally {
                    // close resource no matter what exception occurs
                    if (in != null) {
                        in.close();
                    }
                    connection.disconnect();
                    //return null;
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result){
            if (myActivity != null) {
                bikeDataArrayList = JSONHelper.parseAll(result);
                createCustomAdapter();
            }
        }
    }

    private void createCustomAdapter()
    {
        adapter = new CustomAdapter(this,R.layout.listview_row_layout, bikeDataArrayList, myPreference.getString("websiteName", "I AM FILLER"), this);
        // a custom data adapter
        setListAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        //get a reference to the action bar spinner
        Spinner s = (Spinner) menu.findItem(R.id.spinner).getActionView();
        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerSort, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setPrompt("Sort By:");
        s.setAdapter(mSpinnerAdapter);
        s.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            //MODEL is 1, PRICE is zero
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Spinner changed " + view.toString());
                if(position == 1){
                    //0 is compareModel
                    showToast("Spinner1: position=" + position + " id=" + id);
                    Activity_ListView.this.adapter.sortList(adapter.MODEL);
                }
                else if(position == 2){
                    //1 is comparePrice
                    showToast("Spinner1: position=" + position + " id=" + id);
                    Activity_ListView.this.adapter.sortList(adapter.PRICE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //doSettings();
                Intent myIntent = new Intent(this, activityPreference.class);
                startActivityForResult(myIntent, SETTINGS_ACTIVITY);
                break;
            case R.id.action_about:
                doPopUp();
                break;
            default:
                break;
        }
        return true;
    }

    public void doPopUp(){
        String appName = "Multithreaded Networked Listactivity";
        String createdBy = "Created by: Laura Macaluso and Will Barron";
        String version = "Version Awesome";
       // String webName = myPreference.getString("websiteName", "None Specified Yet");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Project 4:");
        builder.setMessage("Hello!" + "\n" + "\n" + appName + "\n" + createdBy + "\n" + "\n" + "\n" + version + "\n" );

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               // dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

    }


    /* (non-Javadoc)
     * @see android.app.ListActivity#onContentChanged()
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //if changed serialize the data to disk
    }


    /* (non-Javadoc)
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Bike:");
        Object c = l.getItemAtPosition(position);
        builder.setMessage(l.getItemAtPosition(position).toString());

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

}
