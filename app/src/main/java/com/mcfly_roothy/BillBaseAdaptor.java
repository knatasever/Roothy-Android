package com.mcfly_roothy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BillBaseAdaptor extends BaseAdapter
{
	private final LayoutInflater layoutinflater;
	private final List<Bill> billinglist;

	public BillBaseAdaptor(Activity activity, List<Bill> customerbillingsummarylists)
	{
		layoutinflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		billinglist=customerbillingsummarylists;
	}
	@Override
	public int getCount()
	{
		return billinglist.size();
	}
	@Override
	public Bill getItem(int position)
	{
		return billinglist.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view=layoutinflater.inflate(R.layout.billlistview, null);
		TextView fisno=(TextView)view.findViewById(R.id.fisno);
		TextView muskodu=(TextView)view.findViewById(R.id.muskodu);
		TextView musadi=(TextView)view.findViewById(R.id.musadi);
		TextView adresi=(TextView)view.findViewById(R.id.adresi);
		Bill customerbillingsummarylist=billinglist.get(position);
		fisno.setText(fisno.getText().toString()+customerbillingsummarylist.getFisno());
		muskodu.setText(muskodu.getText().toString()+customerbillingsummarylist.getMuskodu());
		musadi.setText(musadi.getText().toString()+customerbillingsummarylist.getMusadi());
		adresi.setText(adresi.getText().toString()+customerbillingsummarylist.getAdresi());
		return view;
	}
}
