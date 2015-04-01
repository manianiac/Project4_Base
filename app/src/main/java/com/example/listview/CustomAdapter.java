package com.example.listview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class CustomAdapter extends ArrayAdapter<BikeData> {

    private static final String TAG = "CustomAdapter";
    private static final String DOLLARSIGN = "$ ";
    private List<BikeData> data;
    Context context;
    private String URL_of_JSON_host = null; 
    
    /**
     * @author lynn
     * class that holds pointers to all the views in listview_row_layout.xml
     * you can hold additional data as well, for instance I held a copy of the 
     * the images pictureID (its filename) so that I could match the picture 
     * to the bike when sorting so  
     */
    private static class ViewHolder {
	ImageView imageView1;
	TextView Model;
	TextView Price;
	TextView Description;
	String pictureID ;
    }

    //TODO define your custom adapter, pass in your collection of bikedata
    public CustomAdapter(Context context, int textViewResourceId,
	    List<BikeData> data, String URL_of_JSON_host) {
	super(context, textViewResourceId, data );
	
	//yes thats a reference to same object
	//but I dont want to allocate too much memory
	this.data = data;
	this.context = context;
	this.URL_of_JSON_host = URL_of_JSON_host;
   }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	//TODO fill this out, use both Viewholder and convertview

	//give it to listview for display
	return convertView;
    }

    /**
     * @param string
     */
    public void setNewURL(String string) {
	//TODO change where data coming from and reload data
    }

    /**
     * @param string
     */
    public void sortList(int pos) {
	//TODO pos defines field to sort on
	//TODO based on that sort your dataset and then reload

    }
}