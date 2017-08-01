package pp.objekte;

public class Pruefung
{
	private String	modul;
	private String	planungsgruppe;
	private int			moduladmin;
	private int			pruefer1;
	private String 	pruefer1S;
	private int			pruefer2;
	private String 	pruefer2S;
	private String	art;
	private int			dauer;
	private int			anzahl;
	
	public Pruefung ( String modul, String planungsgruppe, int moduladmin, int pruefer1, String pruefer1S, int pruefer2,
	    String pruefer2S, String art, int anzahl, int dauer )
	{
		this.modul=modul;
		this.planungsgruppe=planungsgruppe;
		this.moduladmin=moduladmin;
		this.pruefer1=pruefer1;
		this.pruefer1S=pruefer1S;
		this.pruefer2=pruefer2;
		this.pruefer2S=pruefer2S;
		this.art=art;
		this.anzahl=anzahl;
		this.dauer=dauer;		
	}	
	
	public String getModul ()
	{
		return modul;
	}

	public void setModul (String modul)
	{
		this.modul=modul;
	}

	public String getPlanungsgruppe ()
	{
		return planungsgruppe;
	}

	public void setPlanungsgruppe (String planungsgruppe)
	{
		this.planungsgruppe=planungsgruppe;
	}

	public int getModuladmin ()
	{
		return moduladmin;
	}

	public void setModuladmin (int moduladmin)
	{
		this.moduladmin=moduladmin;
	}

	public int getPruefer1 ()
	{
		return pruefer1;
	}

	public void setPruefer1 (int pruefer1)
	{
		this.pruefer1=pruefer1;
	}

	public String getPruefer1S ()
	{
		return pruefer1S;
	}

	public void setPruefer1S (String pruefer1s)
	{
		pruefer1S=pruefer1s;
	}

	public int getPruefer2 ()
	{
		return pruefer2;
	}

	public void setPruefer2 (int pruefer2)
	{
		this.pruefer2=pruefer2;
	}

	public String getPruefer2S ()
	{
		return pruefer2S;
	}

	public void setPruefer2S (String pruefer2s)
	{
		pruefer2S=pruefer2s;
	}

	public String getArt ()
	{
		return art;
	}

	public void setArt (String art)
	{
		this.art=art;
	}

	public int getAnzahl ()
	{
		return anzahl;
	}

	public void setAnzahl (int anzahl)
	{
		this.anzahl=anzahl;
	}
	
	public int getDauer ()
	{
		return dauer;
	}

	public void setDauer (int dauer)
	{
		this.dauer=dauer;
	}

	public String toString ()
	{
		return this.getModul()+"\t"+this.getPlanungsgruppe()+"\t"
		    +this.getPruefer1S()+"\t"+this.getPruefer2S()+"\t"+this.getArt()+"\t"+this.getAnzahl()+"\t"
		    +this.getDauer();
	}
}