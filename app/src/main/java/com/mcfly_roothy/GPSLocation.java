package com.mcfly_roothy;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GPSLocation
{
	private final LocationManager locationManager;
	private final Context context;
	public GPSLocation(Context context)
	{
		this.context=context;
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new GPSLocationListener());
	}
	public void showCurrentLocation()
	{//Business=38.678153, 29.444225
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null)
		{
			Toast.makeText(context, String.format("Gecerli Konum:\nEnlem: "+location.getLatitude()+"\nBoylam: "+ location.getLongitude()), Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(context, "Konum Tespit ediliyor...", Toast.LENGTH_LONG).show();
		}
	}
	private class GPSLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location location)
		{
			Toast.makeText(context, String.format("Yeni Konum:\nEnlem: "+location.getLatitude()+"\nBoylam: "+ location.getLongitude()), Toast.LENGTH_LONG).show();
		}
		@Override
		public void onStatusChanged(String s, int i, Bundle b)
		{
			Toast.makeText(context, "Konum Degisti",Toast.LENGTH_LONG).show();//Provider status changed
		}
		@Override
		public void onProviderDisabled(String s)
		{
			Toast.makeText(context,"GPS Kapali",Toast.LENGTH_LONG).show();//Provider disabled by the user. GPS turned off.
		}
		@Override
		public void onProviderEnabled(String s)
		{
			Toast.makeText(context, "GPS Acik", Toast.LENGTH_LONG).show();//Provider enabled by the user. GPS turned on.
		}
	}
}
