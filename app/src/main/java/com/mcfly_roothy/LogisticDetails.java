package com.mcfly_roothy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class LogisticDetails extends Activity
{
	List<String> logistics;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setContentView(R.layout.logisticdetails);
		final Spinner spinner=(Spinner)findViewById(R.id.spinner1);
		logistics=new ArrayList<String>();
		logistics=new DatabaseSettings(LogisticDetails.this).GetLogistics(new DatabaseSettings(LogisticDetails.this).GetLoginARACKODU());
		logistics.add("%");
		ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(LogisticDetails.this, android.R.layout.simple_spinner_item, logistics);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onItemSelected(AdapterView<?> parent, final View view, int position, long id)
			{
				view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable()
				{
					@Override
					public void run()
					{
						HashMap<String, String> logisticdetails=new HashMap<String, String>();
						logisticdetails=new DatabaseSettings(LogisticDetails.this).LogisticDetails(logistics.get(spinner.getSelectedItemPosition()).toString());

						TextView nakitadedi=(TextView)findViewById(R.id.nakitadedi);
						nakitadedi.setText(logisticdetails.get("NakitAdedi"));
						TextView nakittoplam=(TextView)findViewById(R.id.nakittoplam);
						nakittoplam.setText(logisticdetails.get("NakitToplam")+" TL");

						TextView kredikartiadedi=(TextView)findViewById(R.id.kredikartiadedi);
						kredikartiadedi.setText(logisticdetails.get("KrediKartıAdedi"));
						TextView kredikartitoplam=(TextView)findViewById(R.id.kredikartitoplam);
						kredikartitoplam.setText(logisticdetails.get("KrediKartıToplam")+" TL");

						TextView veresiacikhesapadedi=(TextView)findViewById(R.id.veresiacikhesapadedi);
						veresiacikhesapadedi.setText(logisticdetails.get("VeresiAcikHesapAdedi"));
						TextView veresiacikhesaptoplam=(TextView)findViewById(R.id.veresiacikhesaptoplam);
						veresiacikhesaptoplam.setText(logisticdetails.get("VeresiAcikHesapToplam")+" TL");

						TextView senetadedi=(TextView)findViewById(R.id.senetadedi);
						senetadedi.setText(logisticdetails.get("SenetAdedi"));
						TextView senettoplam=(TextView)findViewById(R.id.senettoplam);
						senettoplam.setText(logisticdetails.get("SenetToplam")+" TL");

						TextView iptaladedi=(TextView)findViewById(R.id.iptaladedi);
						iptaladedi.setText(logisticdetails.get("IptalAdedi"));
						TextView iptaltoplam=(TextView)findViewById(R.id.iptaltoplam);
						iptaltoplam.setText(logisticdetails.get("IptalToplam")+" TL");

						TextView yapilantahsilatadedi=(TextView)findViewById(R.id.yapilantahsilatadedi);
						yapilantahsilatadedi.setText(logisticdetails.get("YapılanTahsilatAdedi"));
						TextView yapilantahsilattoplam=(TextView)findViewById(R.id.yapilantahsilattoplam);
						yapilantahsilattoplam.setText(logisticdetails.get("YapılanTahsilatToplam")+" TL");

						TextView yapilmayantahsilatadedi=(TextView)findViewById(R.id.yapilmayantahsilatadedi);
						yapilmayantahsilatadedi.setText(logisticdetails.get("YapılmayanTahsilatAdedi"));
						TextView yapilmayantahsilattoplam=(TextView)findViewById(R.id.yapilmayantahsilattoplam);
						yapilmayantahsilattoplam.setText(logisticdetails.get("YapılmayanTahsilatToplamı")+" TL");

						TextView faturaadedi=(TextView)findViewById(R.id.faturaadedi);
						faturaadedi.setText(logisticdetails.get("FaturaAdedi"));
						TextView faturatoplam=(TextView)findViewById(R.id.faturatoplam);
						faturatoplam.setText(logisticdetails.get("FaturaToplam")+" TL");

						TextView kasaadedi=(TextView)findViewById(R.id.kasaadedi);
						kasaadedi.setText(logisticdetails.get("SafeIade"));

						TextView premixadedi=(TextView)findViewById(R.id.premixadedi);
						premixadedi.setText(logisticdetails.get("SafePremix"));

						view.setAlpha(255);
					}
				});
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

}
