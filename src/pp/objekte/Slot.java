package pp.objekte;

public class Slot
{
	private int			nummer;
	private String	start;
	private String	ende;
	
	public Slot ( int nummer, String start, String ende )
	{
		this.nummer=nummer;
		this.start=start;
		this.ende=ende;
	}
	
	public int getNummer ()
	{
		return nummer;
	}
	
	public void setNummer (int nummer)
	{
		this.nummer=nummer;
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
	
	public String toString ()
	{
		return this.getNummer()+"\t"+this.getStart()+"\t"+this.getEnde();
	}
}