package pp.objekte;

public class Ausschluss
{
	private int	slot;
	private int	inhaber;
	
	public Ausschluss ( int slot, int inhaber )
	{
		this.slot=slot;
		this.inhaber=inhaber;
	}
	
	public int getSlot ()
	{
		return slot;
	}
	
	public void setSlot (int slot)
	{
		this.slot=slot;
	}
	
	public int getInhaber ()
	{
		return inhaber;
	}
	
	public void setInhaber (int inhaber)
	{
		this.inhaber=inhaber;
	}
	
	public String toString ()
	{
		return this.getSlot()+"\t"+this.getInhaber();
	}
}
