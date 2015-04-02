package com.example.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.listview.BikeData;

/**
 * @author lynn
 * static helper class for accessing JSON data
 * 
 * see:http://prativas.wordpress.com/category/android/part-1-retrieving-a-json-file/
 */
public class JSONHelper {

    //no need to instantiate this
    private JSONHelper(){}
    
    /**
     * @param jsonString
     * @return List<UserData>
     * takes a json string and parses it into json objects
     * You must know what is in the data and what to parse out of it
     */
    //TODO this function should return a collection of BikeData objects instead of void
    public static ArrayList<BikeData> parseAll(String jsonString) {
	//TODO create your collection of BikeData objects
	ArrayList<BikeData> collectionOfBikes = new ArrayList<BikeData>();
	try {
	    //create a json object with the JSON string passed in
	    JSONObject jAll = new JSONObject(jsonString);

	    //TODO get the raw array of bikes from jAll
        JSONArray bikes = jAll.getJSONArray("Bikes");
        System.out.print(true);
	    //TODO iterate through this array building 1 bike object at a time and adding it to the list
	    
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	//TODO return your list
        return collectionOfBikes;
    }

    

}
