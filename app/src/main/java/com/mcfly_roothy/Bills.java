package com.mcfly_roothy;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Bills extends Fragment
{
	View view;
	Spinner spinner;
	List<String> logistics;
	ArrayAdapter<String> arrayAdapter;
	List<Bill> billlist;
	BillBaseAdaptor billBaseAdaptor;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.bills, container, false);
		return view;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		spinner=(Spinner)view.findViewById(R.id.spinner1);
		final ListView billListView=(ListView)view.findViewById(R.id.listView1);
		logistics=new ArrayList<String>();
		logistics=new DatabaseSettings(getActivity()).GetLogistics(new DatabaseSettings(getActivity()).GetLoginARACKODU());
		arrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, logistics);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onItemSelected(AdapterView<?> parent, final View view, int position, long id)
			{
				//final long item=parent.getItemIdAtPosition(position);
				view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() //burda sıkıntı var
				{
					@Override
					public void run()
					{
						billlist=new ArrayList<Bill>();
						billlist=new DatabaseSettings(getActivity()).GetBills(logistics.get(spinner.getSelectedItemPosition()).toString());
						billBaseAdaptor=new BillBaseAdaptor(getActivity(), billlist);
						billListView.setAdapter(billBaseAdaptor);
						view.setAlpha(255);
					}
				});
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		billListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id)
			{
				view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable()
				{
					@Override
					public void run()
					{
						((Distributor)getActivity()).StartSalesDetails(billlist.get((int) parent.getItemIdAtPosition(position)).getFisno());
					}
				});
			}
		});
	}
}
