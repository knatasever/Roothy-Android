package com.mcfly_roothy;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;

public class EditSalesDetails extends Activity
{
	List<String> salesdetail=null;
	Spinner ODEMETIPI1, ODEMETIPI2;
	TextView ODEMETUTARI1;
	EditText ODEMETUTARI2;
	Button gonder;
	Button kaydet;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salesdetails);
		Button gonder=(Button)findViewById(R.id.gonder);
		Button kaydet=(Button)findViewById(R.id.kaydet);
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
		salesdetail=new DatabaseSettings(getApplicationContext()).GetSalesDetail(getIntent().getStringExtra("FISNO"));
		FISNO.setText(FISNO.getText().toString()+salesdetail.get(0).toString());
		MUSKODU.setText(MUSKODU.getText().toString()+salesdetail.get(1).toString());
		MUSADI.setText(MUSADI.getText().toString()+salesdetail.get(2).toString());
		ADRESI.setText(ADRESI.getText().toString()+salesdetail.get(3).toString());
		TOPLAMNET.setText(TOPLAMNET.getText().toString()+salesdetail.get(4).toString());
		SATISTEMSILCISIKODU.setText(SATISTEMSILCISIKODU.getText().toString()+salesdetail.get(5).toString());
		gonder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
gonder();
			}
		});
		kaydet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		ODEMETIPI2.setEnabled(false);
		ODEMETUTARI1.setText(salesdetail.get(4).toString());
		ODEMETUTARI2.setText("0");

		ODEMETIPI1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (!(ODEMETIPI1.getSelectedItem().toString().equals("odeme Tipi 1 Sec")) && !(ODEMETIPI1.getSelectedItem().toString().equals("Fatura iptal")))
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
		ODEMETIPI2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (!(ODEMETIPI2.getSelectedItem().toString().equals("odeme Tipi 2 Sec")))
				{
					ODEMETUTARI2.setText("");
					ODEMETUTARI2.setEnabled(true);
				}
				else
				{
					ODEMETUTARI2.setText("0");
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
			{
				if (!(ODEMETUTARI2.getText().toString().equals("")))
				{
					float price=Float.valueOf(salesdetail.get(4).toString().replace(",", "."))-Float.valueOf(ODEMETUTARI2.getText().toString().replace(",", "."));
					if (price>0)
					{
						ODEMETUTARI1.setText(String.valueOf(price));
					}
					else
					{
						ODEMETUTARI2.setText(String.valueOf(Float.valueOf(salesdetail.get(4).toString().replace(",", "."))-Float.valueOf(ODEMETUTARI1.getText().toString().replace(",", "."))));
					}
				}
				else if (ODEMETUTARI2.getText().toString().equals(""))
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
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.salesdetails, menu);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case R.id.payment: gonder();

				break;
		}
		return true;
	}
	public void gonder(){


		if (!(ODEMETIPI1.getSelectedItem().toString().equals("odeme Tipi 1 Sec")) && !(ODEMETIPI1.getSelectedItem().toString().equals("Fatura �ptal")))
		{
			switch (String.valueOf(ODEMETIPI1.getSelectedItem().toString()))
			{
				case "Nakit" : salesdetail.add("1");
					break;
				case "Kredi Kart�" : salesdetail.add("2");
					break;
				case "Veresi - A��k Hesap" : salesdetail.add("3");
					break;
				case "Senet" : salesdetail.add("4");
					break;
				case "Havale" : salesdetail.add("5");
					break;
				case "�ek" : salesdetail.add("6");
					break;
				case "Fatura �ptal" : salesdetail.add("7");
					break;
				default : salesdetail.add("0");
					break;
			}
			salesdetail.add(ODEMETUTARI1.getText().toString());
			switch (String.valueOf(ODEMETIPI2.getSelectedItem().toString()))
			{
				case "Nakit" : salesdetail.add("1");
					break;
				case "Kredi Kart�" : salesdetail.add("2");
					break;
				case "Veresi - A��k Hesap" : salesdetail.add("3");
					break;
				case "Senet" : salesdetail.add("4");
					break;
				case "Havale" : salesdetail.add("5");
					break;
				case "�ek" : salesdetail.add("6");
					break;
				case "�deme Tipi 2 �ptal" : salesdetail.add("0");
					break;
				default : salesdetail.add("0");
					break;
			}
			salesdetail.add(ODEMETUTARI2.getText().toString());
			new DatabaseSettings(getApplicationContext()).SetPayment(salesdetail);
			//new SetPayments(SalesDetails.this).execute(getString(R.string.setbillsurljson));
			startActivity(new Intent(EditSalesDetails.this, Distributor.class));
		}




	}
}