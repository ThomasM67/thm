package pp.objekte;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.variables.IntVar;

import pp.controller.PPConstraintSolver;
import pp.controller.PPFunktionen;

public class ConstraintPlaner
{
	List<Slot>								slots;
	Map<Integer, Pruefung>		pruefungen;
	Map<Integer, Ausschluss>	ausschluesse;
	IntVar[]									zuPlanen;
	boolean										gibtEsEineLoesung;
	double 										bewertung;
	
	public ConstraintPlaner (Rahmen rahmen) throws IOException, ParseException
	{
		this.slots=PPFunktionen.slotsErzeugen(rahmen);
		this.pruefungen=PPFunktionen.pruefungenEinlesen();
		this.ausschluesse=PPFunktionen.ausschluesseEinlesen(slots);
		this.gibtEsEineLoesung = false;
	}
	
	public List<Slot> getSlots ()
	{
		return slots;
	}
	
	public void setSlots (List<Slot> slots)
	{
		this.slots=slots;
	}
	
	public Map<Integer, Pruefung> getPruefungen ()
	{
		return pruefungen;
	}
	
	public void setPruefungen (Map<Integer, Pruefung> pruefungen)
	{
		this.pruefungen=pruefungen;
	}
	
	public Map<Integer, Ausschluss> getAusschluesse ()
	{
		return ausschluesse;
	}
	
	public void setAusschluesse (Map<Integer, Ausschluss> ausschluesse)
	{
		this.ausschluesse=ausschluesse;
	}
	
	public IntVar[] getZuPlanen ()
	{
		return zuPlanen;
	}
	
	public void setZuPlanen (IntVar[] zuPlanen)
	{
		this.zuPlanen=zuPlanen;
	}
	
	public boolean isGibtEsEineLoesung ()
	{
		return gibtEsEineLoesung;
	}

	public void setGibtEsEineLoesung (boolean gibtEsEineLoesung)
	{
		this.gibtEsEineLoesung=gibtEsEineLoesung;
	}		

	public double getBewertung ()
	{
		return bewertung;
	}

	public void setBewertung (double bewertung)
	{
		this.bewertung=bewertung;
	}

	public ConstraintPlaner modelAndSolve () throws ParseException
	{
			if(this.pruefungen == null || this.ausschluesse == null)
				return null;
			else
				return PPConstraintSolver.modelAndSolve(this);
	}
}
