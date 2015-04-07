package com.example.listview;

//TODO Questions: 1) Inflator Null, 2)Do we need to use Viewholder 3)Set the Imageview

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
    private Activity activity;
    private List<BikeData> data;
    Context context;
    public final int PRICE = 0, MODEL = 1;
    private String URL_of_JSON_host = null;
    private LayoutInflater inflater;
    private int layoutId;
    
    /**
     * @author lynn
     * class that holds pointers to all the views in listview_row_layout.xml
     * you can hold additional data as well, for instance I held a copy of the 
     * the images pictureID (its filename) so that I could match the picture 
     * to the bike when sorting so  
     */

    //TODO define your custom adapter, pass in your collection of bikedata
    public CustomAdapter(Context context, int textViewResourceId,
	    List<BikeData> data, String URL_of_JSON_host, Activity activity) {
	super(context, textViewResourceId, data );
    this.activity = activity;
    if (activity != null)
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //layoutId = context.;
    //yes thats a reference to same object
	//but I dont want to allocate too much memory
	this.data = data;
	this.context = context;
	this.URL_of_JSON_host = URL_of_JSON_host;


   }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView PriceView;
        TextView DescView;
        String pictureID ;
    }
//TODO viewholder
    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        ViewHolder myvh;
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_row_layout, parentView, false);
        } else {
            view = convertView;
           // myvh = (ViewHolder) convertView.getTag();
        }


        myvh = new ViewHolder();

        myvh.textView = (TextView) view.findViewById(R.id.Model);
        myvh.PriceView = (TextView) view.findViewById(R.id.Price);
        myvh.DescView = (TextView)view.findViewById(R.id.Description);
        myvh.imageView = (ImageView)view.findViewById(R.id.imageView1);
        //set the Model

        myvh.textView.setText(data.get(position).Model);

        //set the Price

        myvh.PriceView.setText(String.valueOf(data.get(position).Price));

        //set the Description

        myvh.DescView.setText(data.get(position).Descripton);

        return view;
        //give it to listview for display
    }

    /**
     * @param string
     */
    public void setNewURL(String string) {
	    URL_of_JSON_host = string;
        notifyDataSetChanged();
        //reload data
    }

//TODO figure out Cases to sort...
    public void sortList(int pos) {

        switch(pos){
            //Model is the first on Spinner, then Price
            case MODEL:
                Collections.sort(data, new BikeData.ComparatorModel());
                break;
            case PRICE:
                Collections.sort(data, new BikeData.ComparatorPrice());
                break;

        }
        notifyDataSetChanged();

    }
}