package com.mcfly_roothy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Repository extends Activity
{
	public DrawerLayout drawerlayout;
	private ListView listview;
	private ActionBarDrawerToggle mdrawertoggle;
	private CharSequence mdrawertitle;
	private CharSequence mtitle;
	private String[] navmenutitles;
	private TypedArray navmenuicons;
	private ArrayList<NavDrawerItem> navdraweritems;
	private NavDrawerListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mtitle = mdrawertitle = getTitle();
		navmenutitles = getResources().getStringArray(R.array.nav_drawer_items);
		navmenuicons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listview = (ListView) findViewById(R.id.list_slidermenu);
		navdraweritems = new ArrayList<NavDrawerItem>();
		navdraweritems.add(new NavDrawerItem(navmenutitles[0], navmenuicons.getResourceId(0, -1)));
		navdraweritems.add(new NavDrawerItem(navmenutitles[1], navmenuicons.getResourceId(1, -1)));
		navmenuicons.recycle();
		listview.setOnItemClickListener(new SlideMenuClickListener());//menu Liste  Click listener eklenir
		adapter = new NavDrawerListAdapter(getApplicationContext(), navdraweritems);
		listview.setAdapter(adapter);
		getActionBar().setHomeButtonEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mdrawertoggle = new ActionBarDrawerToggle(this, drawerlayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name)
		{
			@Override
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(mtitle);
				invalidateOptionsMenu();
			}
			@Override
			public void onDrawerOpened(View drawerview)
			{
				getActionBar().setTitle(mdrawertitle);
				invalidateOptionsMenu();
			}
		};
		drawerlayout.setDrawerListener(mdrawertoggle);
		if (savedInstanceState == null)
		{
			displayView(0);
		}
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null)
			{
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mdrawertoggle.onOptionsItemSelected(item))
		{
			return true;
		}
		switch (item.getItemId())
		{
			case R.id.send:
				new SetPayments(Repository.this).execute(getString(R.string.setbillsurljson));
				break;
			case R.id.logisticdetails:
				startActivity(new Intent(Repository.this, LogisticDetails.class).setPackage("com.google.android.apps.maps"));
				break;
			case R.id.location:
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=38.678621, 29.444266")).setPackage("com.google.android.apps.maps"));
				break;

			case R.id.exit:
				AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(Repository.this);
				alertdialogbuilder.setTitle(this.getTitle());
				alertdialogbuilder.setMessage(R.string.exitmessage);
				alertdialogbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						new DatabaseSettings(getApplicationContext()).Delete();
						new DatabaseSettings(getApplicationContext()).Logout();
						startActivity(new Intent(Repository.this, Login.class));
						finish();
					}
				});
				alertdialogbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				});
				AlertDialog alertdialog=alertdialogbuilder.create();
				alertdialog.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	private class SlideMenuClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			displayView(position);
		}
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		boolean draweropen = drawerlayout.isDrawerOpen(listview);
		menu.findItem(R.id.exit).setVisible(!draweropen);
		return super.onPrepareOptionsMenu(menu);
	}
	private void displayView(int position)
	{
		Fragment fragment = null;
		switch (position)
		{
			case 0:
				fragment = new Bills();
				break;
			case 1:
				fragment = new Payments();
				break;
		}
		if (fragment != null)
		{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			listview.setItemChecked(position, true);
			listview.setSelection(position);
			setTitle(navmenutitles[position]);
			drawerlayout.closeDrawer(listview);
		}
		else
		{
			Log.e("MainActivity", "Error in creating fragment");
			drawerlayout.closeDrawer(listview);
		}
	}
	@Override
	public void setTitle(CharSequence title)
	{
		mtitle = title;
		getActionBar().setTitle(mtitle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mdrawertoggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mdrawertoggle.onConfigurationChanged(newConfig);
	}
	public class SetPayments extends AsyncTask<String, String, String>
	{
		private final Context context;
		private ProgressDialog progressDialog;
		private List<NameValuePair> nameValuePairs;
		public SetPayments(Context context)
		{
			super();
			this.context = context;
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog=ProgressDialog.show(context, context.getString(R.string.please_wait), context.getString(R.string.loading),true);
		}
		@Override
		protected String doInBackground(String... params)
		{
			publishProgress("Veriler Toplanıyor...");
			String result = context.getString(R.string.try_again_don_t_send_payment);
			String url=params[0];
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs=new DatabaseSettings(context).GetPayments();
			if (nameValuePairs.size()>0)
			{
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
				HttpResponse httpResponse=null;
				String method="POST";
				publishProgress("Bağlanıyor...");
				ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected())
				{
					try
					{
						if (method == "POST")
						{
							HttpPost httpPost = new HttpPost(url);
							if (nameValuePairs != null)
							{
								httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
							}
							httpResponse = defaultHttpClient.execute(httpPost);
						}
						else if (method == "GET")
						{
							if (nameValuePairs!=null)
							{
								String values = URLEncodedUtils.format(nameValuePairs, "UTF-8");
								url += "?" + values;
							}
							HttpGet httpGet = new HttpGet(url);
							httpResponse = defaultHttpClient.execute(httpGet);
						}
						publishProgress("Sonuç Alınıyor...");
						HttpEntity httpEntity = httpResponse.getEntity();
						String data=EntityUtils.toString(httpEntity);
						if (data!=null)
						{
							JSONObject customerbills=new JSONObject(data);
							result=customerbills.getString("result");
						}
						publishProgress("Sonuç Alındı...");
					}
					catch (UnsupportedEncodingException e)
					{
						result="UnsupportedEncodingException";
					}
					catch (ClientProtocolException e)
					{
						result="ClientProtocolException";
					}
					catch (IOException e)
					{
						result="IOException";
					}
					catch (JSONException e)
					{
						result="JSONException";
					}
				}
				else
				{
					result=getString(R.string.internet_error);
				}
			}
			else
			{
				result="Gönderilmeyen Tahsilat Bulunamadı.";
			}
			return result;
		}
		@Override
		protected void onProgressUpdate(String... values)
		{
			super.onProgressUpdate(values);
			progressDialog.setMessage(values[0]);
		}
		@Override
		protected void onPostExecute(String result)
		{
			if (Boolean.valueOf(result))
			{
				new DatabaseSettings(context).SetSent(nameValuePairs);
				Toast.makeText(context, R.string.sent_payment, Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
		@Override
		protected void onCancelled(String result)
		{
			super.onCancelled(result);
			progressDialog.dismiss();
		}
		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}
	}
	public void StartSalesDetails(String FISNO)
	{
		startActivity(new Intent(Repository.this, SalesDetails.class).putExtra("FISNO", FISNO));
	}
}