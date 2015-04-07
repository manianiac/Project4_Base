/**
 * 
 */
package com.example.listview;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class activityPreference extends PreferenceActivity {

    private String downloadSite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		PrefsFragment mPrefsFragment = new PrefsFragment();

		//android.R.id.content gives you the root element of a view, without having to know 
		//its actual name/type/ID. Check out stackoverflow.com/questions/4486034/ 	
		//This is useful in fragment transactions like: 
		//mFragmentTransaction.add(android.R.id.content, myFragment)
		mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
		mFragmentTransaction.commit();
    }

	public static class PrefsFragment extends PreferenceFragment {
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}

