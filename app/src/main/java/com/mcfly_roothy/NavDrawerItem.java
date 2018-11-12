package com.mcfly_roothy;

public class NavDrawerItem {
	private String title;
	private int icon;
	private String count = "0";
	private boolean iscountervisible = false;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title, int icon, boolean iscountervisible,
			String count) {
		this.title = title;
		this.icon = icon;
		this.iscountervisible = iscountervisible;
		this.count = count;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return this.icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getCount() {
		return this.count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean getIscountervisible() {
		return this.iscountervisible;
	}

	public void setIscountervisible(boolean iscountervisible) {
		this.iscountervisible = iscountervisible;
	}
}
