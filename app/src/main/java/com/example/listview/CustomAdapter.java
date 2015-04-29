//Laura Macaluso and William Barron
package com.example.listview;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

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
    private ViewHolder myvh;
    /**
     * @author lynn
     * class that holds pointers to all the views in listview_row_layout.xml
     * you can hold additional data as well, for instance I held a copy of the 
     * the images pictureID (its filename) so that I could match the picture 
     * to the bike when sorting so  
     */
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
    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {

        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_row_layout, parentView, false);
        } else {
            view = convertView;
        }

        myvh = new ViewHolder();
        myvh.textView = (TextView) view.findViewById(R.id.Model);
        myvh.PriceView = (TextView) view.findViewById(R.id.Price);
        myvh.DescView = (TextView)view.findViewById(R.id.Description);
        myvh.imageView = (ImageView)view.findViewById(R.id.imageView1);
        view.setTag(myvh);
        //set the Model
        myvh.textView.setText(data.get(position).Model);
        //set the Price
        myvh.PriceView.setText(String.valueOf(data.get(position).Price));
        //set the Description
        myvh.DescView.setText(data.get(position).Descripton);
        //set Image
        DownloadTask task = new DownloadTask(position,myvh);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL_of_JSON_host + getItem(position).Picture);

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

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        private static final String TAG = "DownloadTask";
        private static final int DEFAULTBUFFERSIZE = 50;
        private static final int NODATA = -1;
        private int mPosition;
        private ViewHolder myHolder;

        public DownloadTask(int position, ViewHolder vh)
        {
            mPosition = position;
            myHolder = vh;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            // site we want to connect to
            String url = params[0];

            // note streams are left willy-nilly here because it declutters the
            // example
            try {
                URL url1 = new URL(url);

                // this does no network IO
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

                // can further configure connection before getting data
                // cannot do this after connected
                // connection.setRequestMethod("GET");
                // connection.setReadTimeout(timeoutMillis);
                // connection.setConnectTimeout(timeoutMillis);

                // this opens a connection, then sends GET & headers
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

                // get our streams, a more concise implementation is
                // BufferedInputStream bis = new
                // BufferedInputStream(connection.getInputStream());
                InputStream is = connection.getInputStream();

                // the following buffer will grow as needed
                ByteArrayBuffer baf = new ByteArrayBuffer(DEFAULTBUFFERSIZE);
                int current;

                // wrap in finally so that stream bis is sure to close
                BufferedInputStream bis = new BufferedInputStream(is);
                try {
                    while ((current = bis.read()) != NODATA) {
                        baf.append((byte) current);
                    }

                    // convert to a bitmap
                    byte[] imageData = baf.toByteArray();
                    return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                } finally {
                    bis.close();
                }
                // close resource no matter what exception occurs

            } catch (Exception exc) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
               Drawable mImage = new BitmapDrawable(result);
               //ImageView v = (ImageView)findViewById(R.id.webImage);
               myHolder.imageView.setImageBitmap(result);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}