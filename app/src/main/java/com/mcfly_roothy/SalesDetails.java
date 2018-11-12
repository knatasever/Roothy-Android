package com.mcfly_roothy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SalesDetails extends Activity
{
	List<String> salesdetail=null;
	Spinner ODEMETIPI1, ODEMETIPI2;
	TextView ODEMETUTARI1;
	EditText ODEMETUTARI2;
	Button kaydet;
	Button gonder;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salesdetails);
		TextView FISNO=(TextView)findViewById(R.id.fisno);
		TextView MUSKODU=(TextView)findViewById(R.id.muskodu);
		TextView MUSADI=(TextView)findViewById(R.id.musadi);
		TextView ADRESI=(TextView)findViewById(R.id.adresi);
		ODEMETUTARI1=(TextView)findViewById(R.id.odemetutari1);
		ODEMETUTARI2=(EditText)findViewById(R.id.odemetutari2);
		TextView TOPLAMNET=(TextView)findViewById(R.id.toplamnet);
		TextView SATISTEMSILCISIKODU=(TextView)findViewById(R.id.satistemsilcisikodu);
		ODEMETIPI1=(Spinner)findViewById(R.id.spinner1);
		ODEMETIPI2=(Spinner)findViewById(R.id.spinner2);
		Button kaydet =(Button)findViewById(R.id.kaydet);
		Button gonder =(Button)findViewById(R.id.gonder);

		salesdetail=new DatabaseSettings(getApplicationContext()).GetSalesDetail(getIntent().getStringExtra("FISNO"));
		FISNO.setText(FISNO.getText().toString()+salesdetail.get(0).toString());
		MUSKODU.setText(MUSKODU.getText().toString()+salesdetail.get(1).toString());
		MUSADI.setText(MUSADI.getText().toString()+salesdetail.get(2).toString());
		ADRESI.setText(ADRESI.getText().toString()+salesdetail.get(3).toString());
		TOPLAMNET.setText(TOPLAMNET.getText().toString()+salesdetail.get(4).toString());
		SATISTEMSILCISIKODU.setText(SATISTEMSILCISIKODU.getText().toString()+salesdetail.get(5).toString());
		kaydet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				send();
			}
		});
		gonder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				send();			}
		});



		if (salesdetail.get(4).toString().equals("0"))
		{
			ODEMETIPI1.setEnabled(false);
		}
		ODEMETIPI1.setSelection(Integer.valueOf(salesdetail.get(6).toString()));
		ODEMETIPI1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (!(ODEMETIPI1.getSelectedItem().toString().equals("Ödeme Tipi 1 Seç")) && !(ODEMETIPI1.getSelectedItem().toString().equals("Fatura İptal")) && !(salesdetail.get(4).toString().equals("0")))
				{
					ODEMETIPI2.setEnabled(true);
				}
				else
				{
					ODEMETIPI2.setEnabled(false);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		if (salesdetail.get(7).toString().equals("0"))
		{
			ODEMETUTARI1.setText(salesdetail.get(4).toString());
		}
		else
		{
			ODEMETUTARI1.setText(salesdetail.get(7).toString());
		}
		if (salesdetail.get(8).toString().equals("0"))
		{
			ODEMETIPI2.setSelection(0);
			ODEMETIPI2.setEnabled(false);
		}
		else
		{
			ODEMETIPI2.setSelection(Integer.valueOf(salesdetail.get(8).toString()));
		}
		ODEMETIPI2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (!(ODEMETIPI2.getSelectedItem().toString().equals("Ödeme Tipi 2 Seç")) && !(ODEMETIPI2.getSelectedItem().toString().equals("Ödeme Tipi 2 İptal")) && !(ODEMETIPI1.getSelectedItem().toString().equals(ODEMETIPI2.getSelectedItem().toString())))
				{
					ODEMETUTARI2.setEnabled(true);
					if (!(salesdetail.get(9).toString().equals("0")))
					{
						ODEMETUTARI2.setText(salesdetail.get(9).toString());
					}
					else
					{
						ODEMETUTARI2.setText("");
					}
				}
				else
				{
					ODEMETUTARI2.setText("");
					ODEMETUTARI2.setEnabled(false);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		ODEMETUTARI2.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{//FISNO, MUSKODU, MUSADI, ADRESI, TOPLAMNET, SATISTEMSILCISIKODU, ODEMETIPI1, ODEMETUTARI1, ODEMETIPI2, ODEMETUTARI2
				if (!(ODEMETUTARI2.getText().toString().equals("")))
				{
					Float price=Float.valueOf(salesdetail.get(4).toString().replace(",", "."))-Float.valueOf(ODEMETUTARI2.getText().toString().replace(",", "."));
					if (price>0)
					{
						ODEMETUTARI1.setText(new DecimalFormat("0.00").format(price));
					}
					else
					{
						ODEMETUTARI2.setText(String.valueOf(Double.valueOf(salesdetail.get(4).toString().replace(",", "."))-Double.valueOf(ODEMETUTARI1.getText().toString().replace(",", "."))));
					}
				}
				else
				{
					ODEMETUTARI1.setText(salesdetail.get(4).toString());
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
	}


	public void send(){

		if (salesdetail.get(4).toString().equals("0"))//Bill==0
		{
			salesdetail.set(6, "10");
			new DatabaseSettings(getApplicationContext()).SetPayment(salesdetail);
			new SetPayments(SalesDetails.this).execute(getString(R.string.setbillsurljson));
		}
		else if (!(ODEMETIPI1.getSelectedItem().toString().equals("Ödeme Tipi 1 Seç")))
		{
			switch (String.valueOf(ODEMETIPI1.getSelectedItem().toString()))
			{
				case "Nakit" : salesdetail.set(6, "1");
					break;
				case "Kredi Kartı" : salesdetail.set(6, "2");
					break;
				case "Veresi - Açık Hesap" : salesdetail.set(6, "3");
					break;
				case "Senet" : salesdetail.set(6, "4");
					break;
				case "Fatura İptal" : salesdetail.set(6, "7");
					break;
				default : salesdetail.set(6, "0");
					break;
			}
			salesdetail.set(7, ODEMETUTARI1.getText().toString());
			// Float tutariki=Float.valueOf(ODEMETUTARI2.getText().toString());

			if (ODEMETUTARI2.isEnabled() && Float.valueOf(ODEMETUTARI2.getText().toString()) >0)
			{
				switch (String.valueOf(ODEMETIPI2.getSelectedItem().toString()))
				{
					case "Nakit" : salesdetail.set(8, "1");
						break;
					case "Kredi Kartı" : salesdetail.set(8, "2");
						break;
					case "Veresi - Açık Hesap" : salesdetail.set(8, "3");
						break;
					case "Senet" : salesdetail.set(8, "4");
						break;
					case "Ödeme Tipi 2 İptal" : salesdetail.set(8, "0");
						break;
					default : salesdetail.set(8, "0");
						break;
				}
			}
			salesdetail.set(9, ODEMETUTARI2.getText().toString());
			new DatabaseSettings(getApplicationContext()).SetPayment(salesdetail);
			new SetPayments(SalesDetails.this).execute(getString(R.string.setbillsurljson));
		}
		else
		{
			Toast.makeText(SalesDetails.this, "Ödeme Tipi 1 Seç", Toast.LENGTH_LONG).show();
		}}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.salesdetails, menu);
		//menu.setHeaderTitle("Context Menu");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case R.id.direction:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=38.678621, 29.444266")).setPackage("com.google.android.apps.maps"));
				break;
			case R.id.payment: send();
				break;
		}
		return true;
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
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
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
			startActivity(new Intent(SalesDetails.this, Distributor.class));
			finish();
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
}