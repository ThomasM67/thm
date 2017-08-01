package pp.objekte;

public class Rahmen
{
	private String start;
	private String ende;
	private String ausnahme1;
	private String ausnahme2;
	private String ausnahme3;
	
	public Rahmen ( String start, String ende, String ausnahme1, String ausnahme2, String ausnahme3 )
	{
		this.start=start;
		this.ende=ende;
		this.ausnahme1=ausnahme1;
		this.ausnahme2=ausnahme2;
		this.ausnahme3=ausnahme3;
	}

	public String getStart ()
	{
		return start;
	}

	public void setStart (String start)
	{
		this.start=start;
	}

	public String getEnde ()
	{
		return ende;
	}

	public void setEnde (String ende)
	{
		this.ende=ende;
	}

	public String getAusnahme1 ()
	{
		return ausnahme1;
	}

	public void setAusnahme1 (String ausnahme1)
	{
		this.ausnahme1=ausnahme1;
	}

	public String getAusnahme2 ()
	{
		return ausnahme2;
	}

	public void setAusnahme2 (String ausnahme2)
	{
		this.ausnahme2=ausnahme2;
	}

	public String getAusnahme3 ()
	{
		return ausnahme3;
	}

	public void setAusnahme3 (String ausnahme3)
	{
		this.ausnahme3=ausnahme3;
	}	
	
}
