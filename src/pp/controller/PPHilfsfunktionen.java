package pp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import pp.objekte.ConstraintPlaner;
import pp.objekte.Slot;

public class PPHilfsfunktionen
{
	public static void slotsErzeugen (List<Slot> slots, int startTag, int endeTag, 
			int ausnahme1Tag, int ausnahme2Tag, int ausnahme3Tag, int startMonat, int jahr)
	{
		String tagS;
		String monatS;
		String jahrS;
		int nummer=slots.size();
		
		for (int tag=startTag; tag<=endeTag; tag++)
		{	
			if (tag == ausnahme1Tag || tag == ausnahme2Tag || tag == ausnahme3Tag || tag == startTag+5 || tag == startTag+6 )
				continue;
			
			if (tag<10)
				tagS="0"+Integer.toString(tag);
			else
				tagS=Integer.toString(tag);
			
			if (startMonat<10)
				monatS="0"+Integer.toString(startMonat);
			else
				monatS=Integer.toString(startMonat);
			
			jahrS=Integer.toString(jahr);					
			
			slots.add(new Slot(++nummer, jahrS + "-" + monatS + "-" + tagS + " 08:00:00", jahrS + "-" + monatS + "-" + tagS + " 11:00:00"));
			slots.add(new Slot(++nummer, jahrS + "-" + monatS + "-" + tagS + " 11:30:00", jahrS + "-" + monatS + "-" + tagS + " 14:00:00"));
			slots.add(new Slot(++nummer, jahrS + "-" + monatS + "-" + tagS + " 14:30:00", jahrS + "-" + monatS + "-" + tagS + " 17:00:00"));					
		}
	}
	
	public static String ergebnisAusgeben (long start, long ende, ConstraintPlaner planung)
	    throws ParseException
	{
		String modul;
		String tab1;
		String slot;
		String prof;
		String tab2;
		String planungsgruppe;
		
		String ergebnis="Keine Lösung gefunden!";
		if (planung.isGibtEsEineLoesung())
		{
			ergebnis="Folgende Lösung in "+(ende-start)/1000.0+" Sekunden gefunden:\n";
			ergebnis+="====================================================================================================================================================================\n";
			ergebnis+="Modul"+PPHilfsfunktionen.leerzeichen(50, "Modul")
			    +"Beginn\t\tPrüfer" + PPHilfsfunktionen.leerzeichen(35, "Prüfer") + "Planungsgruppe\n";
			ergebnis+="====================================================================================================================================================================\n";
			for (int slotIndex=0; slotIndex<planung.getSlots().size(); slotIndex++)
			{
				for (int index=0; index<planung.getZuPlanen().length; index++)
				{
					if (planung.getSlots().get(slotIndex).getNummer()==planung.getZuPlanen()[index]
					    .getValue())
					{
						modul=planung.getPruefungen().get(index).getModul();
						tab1=PPHilfsfunktionen.leerzeichen(50, modul);
						slot=datumWandeln(
						    planung.getSlots().get(planung.getZuPlanen()[index].getValue()-1).getStart());
						prof=planung.getPruefungen().get(index).getPruefer1S();
						tab2=PPHilfsfunktionen.leerzeichen(35, prof);
						planungsgruppe=planung.getPruefungen().get(index).getPlanungsgruppe();
						ergebnis+=modul+tab1+slot+"\t"+prof+tab2+planungsgruppe+"\n";
					}
				}
			}
			ergebnis+="====================================================================================================================================================================\n";
			ergebnis+="Diese Lösung ist mit " + runden(planung.getBewertung()) + " Fehlerpunkten zu bewerten, das fiktive Optimum liegt bei 0 !\n";
			ergebnis+="====================================================================================================================================================================\n";
		}
		return ergebnis;
	}
	
	private static double runden (double wert)
	{
		return Math.round(wert*100)/100.0;
	}

	public static String leerzeichen (int anzahl, String text)
	{
		String ergebnis="";
		for (int space=0; space<(anzahl-text.length()); space++)
			ergebnis+=" ";
		return ergebnis;
	}
	
	public static String datumWandeln (String datum) throws ParseException
	{
		return new SimpleDateFormat("dd.MM.yyyy HH:mm")
		    .format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datum));
	}
	
	public static int datumWandelnInt (String datum, int start, int diff)
	{
		String wert1 = datum.substring(start, start+diff);
		String wert2 = datum.substring(start+diff, start+2*diff);
		if (wert1.equals("0"))
			return Integer.valueOf(wert2);
		else
			return Integer.valueOf(wert1+wert2);
	}
}
