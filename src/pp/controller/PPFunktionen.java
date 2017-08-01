package pp.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.chocosolver.solver.variables.IntVar;

import pp.objekte.Ausschluss;
import pp.objekte.ConstraintPlaner;
import pp.objekte.Pruefung;
import pp.objekte.Rahmen;
import pp.objekte.Slot;

public class PPFunktionen
{
	public static String pruefungenPlanen (Rahmen rahmen) throws ParseException
	{
		ConstraintPlaner planung=null;
		
		long start=System.currentTimeMillis();
		try
		{
			planung=new ConstraintPlaner(rahmen).modelAndSolve();
		}
		catch (ParseException e)
		{
			System.out.println("Fehler beim Umwandeln eines Datums!");
		}
		catch (IOException e)
		{
			System.out.println("Fehler beim Einlesen der Daten!");
		}
		long ende=System.currentTimeMillis();
		
		if (planung == null)
			return "Konnte die Daten nicht auslesen, bitte erst eine Verbindung zur gewünschten Datenbank herstellen!";
		else
			return PPHilfsfunktionen.ergebnisAusgeben(start, ende, planung);
	}
	
	public static List<Slot> slotsErzeugen (Rahmen rahmen)
	{
		List<Slot> slots=new ArrayList<Slot>();

		int ausnahme1Tag=0, ausnahme2Tag=0, ausnahme3Tag=0;
		
		int startTag=PPHilfsfunktionen.datumWandelnInt(rahmen.getStart(), 8, 1);
		int endeTag=PPHilfsfunktionen.datumWandelnInt(rahmen.getEnde(), 8, 1);
		if (rahmen.getAusnahme1()!=null)
			ausnahme1Tag=PPHilfsfunktionen.datumWandelnInt(rahmen.getAusnahme1(), 8, 1);
		if (rahmen.getAusnahme2()!=null)
			ausnahme2Tag=PPHilfsfunktionen.datumWandelnInt(rahmen.getAusnahme2(), 8, 1);
		if (rahmen.getAusnahme3()!=null)
			ausnahme3Tag=PPHilfsfunktionen.datumWandelnInt(rahmen.getAusnahme3(), 8, 1);
		
		int startMonat=PPHilfsfunktionen.datumWandelnInt(rahmen.getStart(), 5, 1);
		int endeMonat=PPHilfsfunktionen.datumWandelnInt(rahmen.getEnde(), 5, 1);
		
		int jahr=PPHilfsfunktionen.datumWandelnInt(rahmen.getStart(), 0, 2);
		
		if (endeTag<startTag)
		{
			PPHilfsfunktionen.slotsErzeugen(slots, startTag, 11+startTag-endeTag, ausnahme1Tag,
			    ausnahme2Tag, ausnahme3Tag, startMonat, jahr);
			PPHilfsfunktionen.slotsErzeugen(slots, 1, endeTag, ausnahme1Tag, ausnahme2Tag, ausnahme3Tag,
			    endeMonat, jahr);			
		}		
		else
		{
			PPHilfsfunktionen.slotsErzeugen(slots, startTag, endeTag, ausnahme1Tag, ausnahme2Tag,
			    ausnahme3Tag, startMonat, jahr);
		}
		
		return slots;
	}	
	
	public static Map<Integer, Pruefung> pruefungenEinlesen () throws IOException
	{
		Map<Integer, Pruefung> pruefungen=new TreeMap<Integer, Pruefung>();
		return PPdbActions.pruefungenEinlesen(pruefungen);		
	}
	
	public static Map<Integer, Ausschluss> ausschluesseEinlesen (List<Slot> slots) throws IOException
	{
		Map<Integer, Ausschluss> ausschluesse=new TreeMap<Integer, Ausschluss>();
			return PPdbActions.ausschluesseEinlesen(ausschluesse, slots);		
	}

	public static double loesungBewerten (IntVar[] zuPlanen, int slots)
	{
		int [] anzahlen = new int [slots];
		double wert=0;
		double optimum = (double) zuPlanen.length/slots;
		
		for (IntVar loesung : zuPlanen)
		{
			anzahlen [loesung.getValue()-1]+=1;
		}
		for (int anzahl : anzahlen)
		{
			wert += Math.pow(2, Math.abs(anzahl-optimum));
		}
		return wert;
	}	
}
