package com.mcfly_roothy;

public class Bill
{
	private String fisno;
	private String muskodu;
	private String musadi;
	private String adresi;
	public Bill(String fisno, String muskodu, String musadi, String adresi)
	{
		super();
		this.fisno = fisno;
		this.muskodu = muskodu;
		this.musadi = musadi;
		this.adresi = adresi;
	}
	@Override
	public String toString()
	{
		return "Bill [fisno=" + fisno + ", muskodu=" + muskodu + ", musadi=" + musadi + ", adresi=" + adresi + "]";
	}
	public String getFisno()
	{
		return fisno;
	}
	public void setFisno(String fisno)
	{
		this.fisno = fisno;
	}
	public String getMuskodu()
	{
		return muskodu;
	}
	public void setMuskodu(String muskodu)
	{
		this.muskodu = muskodu;
	}
	public String getMusadi()
	{
		return musadi;
	}
	public void setMusadi(String musadi)
	{
		this.musadi = musadi;
	}
	public String getAdresi()
	{
		return adresi;
	}
	public void setAdresi(String adresi)
	{
		this.adresi = adresi;
	}
}
