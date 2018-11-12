package com.mcfly_roothy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Login extends Activity
{
	EditText username;
	EditText password;
	Button giris;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if ((new DatabaseSettings(Login.this).GetLoginARACKODU())!="")
		{
			if ((new DatabaseSettings(Login.this).GetLogistics(new DatabaseSettings(Login.this).GetLoginARACKODU())).size()>0 || (new DatabaseSettings(Login.this).GetPaymentsLogistics(new DatabaseSettings(Login.this).GetLoginARACKODU())).size()>0)
			{
				startActivity(new Intent(Login.this, Distributor.class));
				finish();
			}
			else
			{
				new GetData(Login.this).execute(getString(R.string.getbillsurljson));
			}
		}
		else
		{
			setContentView(R.layout.login);
			username=(EditText)findViewById(R.id.editText1);
			password=(EditText)findViewById(R.id.editText2);
			giris=(Button)findViewById(R.id.giris);
			giris.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					log();
				}
			});
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.entry:
				log();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
	public class GetLogin extends AsyncTask<String, String, String>//doInBackground, onProgressUpdate, onPostExecute
	{
		private final Context context;
		private ProgressDialog progressDialog;
		String ARACKODU;
		String ARACPLAKA;
		String Password;
		public GetLogin(Context context)
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
			publishProgress("Veriler Toplaniyor...");
			String url=params[0];
			ARACKODU=params[1];
			Password=params[2];
			ARACPLAKA=context.getString(R.string.login_failed);
			publishProgress("Baglaniyor...");
			ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected())
			{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("ARACKODU", params[1]));
				nameValuePairs.add(new BasicNameValuePair("Password", params[2]));
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
				HttpConnectionParams.setSoTimeout(httpParams, 20000);
				DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
				HttpResponse httpResponse=null;
				String method="POST";
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
					publishProgress("Sonuc Aliniyor...");
					HttpEntity httpEntity = httpResponse.getEntity();
					/*File process...
					InputStream inputStream = httpEntity.getContent();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					while ((line = bufferedReader.readLine()) != null)
					{
						stringBuilder.append(line + "\n");
					}
					inputStream.close();
					jsonObject = new JSONObject(stringBuilder.toString());
					 *//////////
					String data=EntityUtils.toString(httpEntity);
					if (data!=null)
					{
						JSONObject customerbills=new JSONObject(data);
						ARACPLAKA=customerbills.getString("ARACPLAKA");
					}
					publishProgress("Giri� Kontrol Ediliyor...");
				}
				catch (UnsupportedEncodingException e)
				{
					ARACPLAKA="UnsupportedEncodingException";
				}
				catch (ClientProtocolException e)
				{
					ARACPLAKA="ClientProtocolException";
				}
				catch (IOException e)
				{
					ARACPLAKA="IOException";
				}
				catch (JSONException e)
				{
					ARACPLAKA="JSONException";
				}
			}
			else
			{
				ARACPLAKA=getString(R.string.internet_error);
			}
			return ARACPLAKA;
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
			super.onPostExecute(result);
			if (!result.equals(getString(R.string.internet_error)) && !result.equals(getString(R.string.login_failed)) && !result.equals("false") && !result.equals("UnsupportedEncodingException") && !result.equals("ClientProtocolException") && !result.equals("IOException") && !result.equals("JSONException"))
			{
				new DatabaseSettings(context).LoginInsert(ARACKODU, ARACPLAKA, Password);
				if ((new DatabaseSettings(context).GetLogistics(new DatabaseSettings(Login.this).GetLoginARACKODU())).size()>0)
				{
					startActivity(new Intent(Login.this, Distributor.class));
					finish();
				}
				else
				{
					new GetData(context).execute(getString(R.string.getbillsurljson));
				}
			}
			else
			{
				if (result.equals("false"))
				{
					Toast.makeText(context, "Giris Basarisiz.", Toast.LENGTH_LONG).show();
					username.setText("");
					password.setText("");
				}
			}
			//progressDialog.cancel();
			progressDialog.dismiss();
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
	public class GetData extends AsyncTask<String, Integer, String[][]>//doInBackground, onProgressUpdate, onPostExecute
	{
		private final Context context;
		private ProgressDialog progressDialog;
		String[][] safeiadelist;
		String[][] safepremixlist;
		private String message;
		public GetData(Context context)
		{
			super();
			this.context = context;
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog=new ProgressDialog(this.context);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		@Override
		protected String[][] doInBackground(String... params)
		{
			publishProgress(0);
			String url=params[0];
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ARACKODU", new DatabaseSettings(context).GetLoginARACKODU()));
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
			HttpConnectionParams.setSoTimeout(httpParams, 20000);
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpResponse httpResponse=null;
			String method="POST";
			String[][] billlist = null;
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
					HttpEntity httpEntity = httpResponse.getEntity();
					String data=EntityUtils.toString(httpEntity);
					if (data!=null)
					{
						JSONObject jsonObject=new JSONObject(data);
						JSONArray bills=jsonObject.getJSONArray("Bills");
						billlist=new String[bills.length()][13];
						for (int i = 0; i < bills.length(); i++)
						{
							JSONObject bill=bills.getJSONObject(i);
							billlist[i][0]= bill.getString("fisNo");
							billlist[i][1]= bill.getString("musKodu");
							billlist[i][2]= bill.getString("musAdi");
							billlist[i][3]= bill.getString("adres");
							billlist[i][4]= bill.getString("odemeTipi1");
							billlist[i][5]= bill.getString("odemeTutari1").replace(",", ".");
							billlist[i][6]= bill.getString("odemeTipi2");
							billlist[i][7]= bill.getString("odemeTutari2").replace(",", ".");
							billlist[i][8]= bill.getString("toplamNet").replace(",", ".");
							billlist[i][9]= bill.getString("preselKodu");
							billlist[i][10]= bill.getString("aracKodu");
							billlist[i][11]= bill.getString("aracPlaka");
							billlist[i][12]= bill.getString("sevkNo");
							publishProgress((i*100)/bills.length());
						}
						/////////////////////////////////////////////////////////////////////////
						JSONArray safeiade=jsonObject.getJSONArray("SafeIade");
						safeiadelist=new String[safeiade.length()][2];
						for (int i = 0; i < safeiade.length(); i++)
						{
							JSONObject bill=safeiade.getJSONObject(i);
							safeiadelist[i][0]= bill.getString("sevkno");
							safeiadelist[i][1]= bill.getString("iade");
							publishProgress((i*100)/bills.length());
						}
						JSONArray safepremix=jsonObject.getJSONArray("SafePremix");
						safepremixlist=new String[safepremix.length()][2];
						for (int i = 0; i < safepremix.length(); i++)
						{
							JSONObject bill=safepremix.getJSONObject(i);
							safepremixlist[i][0]= bill.getString("sevkno");
							safepremixlist[i][1]= bill.getString("premix");
							publishProgress((i*100)/bills.length());
						}
						////////////////////////////////////////////////////////////////////////
					}
					publishProgress(100);
				}
				catch (UnsupportedEncodingException e)
				{
					message="UnsupportedEncodingException";
				}
				catch (ClientProtocolException e)
				{
					message="ClientProtocolException";
				}
				catch (IOException e)
				{
					message="IOException";
				}
				catch (JSONException e)
				{
					message="JSONException";
				}
			}
			else
			{
				message=getString(R.string.internet_error);
			}
			return billlist;
		}
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
		}
		@Override
		protected void onPostExecute(String[][] result)
		{
			super.onPostExecute(result);
			if (result!=null)
			{
				new DatabaseSettings(context).DataInsert(result, safeiadelist, safepremixlist);
				startActivity(new Intent(context, Distributor.class));
				finish();
			}
			else
			{
				if (message.equals("JSONException"))
				{
					Toast.makeText(context, "Girdiginiz Kullaniciya Ait Fatura Bilgileri Bulunamadi.", Toast.LENGTH_LONG).show();
					new DatabaseSettings(getApplicationContext()).Delete();
					new DatabaseSettings(getApplicationContext()).Logout();
					setContentView(R.layout.login);
					username=(EditText)findViewById(R.id.editText1);
					password=(EditText)findViewById(R.id.editText2);
				}
			}
			progressDialog.dismiss();
		}
		@Override
		protected void onCancelled(String[][] result)
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
	public void log(){

		if ((new DatabaseSettings(Login.this).SetLoginSession(username.getText().toString(), password.getText().toString()))!="")
		{
			if ((new DatabaseSettings(Login.this).GetLogistics(new DatabaseSettings(Login.this).GetLoginARACKODU())).size()>0)
			{
				startActivity(new Intent(Login.this, Distributor.class));
				finish();
			}
			else
			{
				new GetData(Login.this).execute(getString(R.string.getbillsurljson));
			}
		}
		else
		{
			new GetLogin(Login.this).execute(getString(R.string.getloginurljson), username.getText().toString(), password.getText().toString());//Edit
		}




	}
}
