package com.mcfly_roothy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<NavDrawerItem> navdraweritems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navdraweritems) {
		this.context = context;
		this.navdraweritems = navdraweritems;
	}

	@Override
	public int getCount() {
		return navdraweritems.size();
	}

	@Override
	public Object getItem(int position) {
		return navdraweritems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (convertview == null) {
			LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertview = layoutinflater.inflate(R.layout.drawerlistitem, null);
		}
		ImageView imageview = (ImageView) convertview.findViewById(R.id.icon);
		TextView title = (TextView) convertview.findViewById(R.id.title);
		TextView count = (TextView) convertview.findViewById(R.id.counter);
		imageview.setImageResource(navdraweritems.get(position).getIcon());
		title.setText(navdraweritems.get(position).getTitle());
		if (navdraweritems.get(position).getIscountervisible()) {
			count.setText(navdraweritems.get(position).getCount());
		} else {
			count.setVisibility(View.GONE);
		}
		return convertview;
	}
}