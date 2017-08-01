package pp.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import pp.objekte.Ausschluss;
import pp.objekte.ConstraintPlaner;
import pp.objekte.Pruefung;

public class PPConstraintSolver
{
	public static ConstraintPlaner modelAndSolve (ConstraintPlaner planung) throws ParseException
	{
		int anzahlPruefungen=planung.getPruefungen().size();
		int anzahlSlots=planung.getSlots().size();
		int reduziere=0;
		int distanz=0;
		int durchlauf=0;
		Solution sol=null;
		
		while (sol==null)
		{
			// Model und zu planende Prüfungen als Variablen anlegen
			
			Model model=new Model("PrüfungsplanungFB6");
			
			IntVar[] zuPlanen=model.intVarArray(anzahlPruefungen, 1, anzahlPruefungen);			
			
			// Schritt 1: Prüfer trennen
			
			Set<Integer> pruefer=new TreeSet<Integer>();
			for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
			{
				pruefer.add(pruefung.getValue().getPruefer1());
				if (pruefung.getValue().getPruefer2()!=0)
					pruefer.add(pruefung.getValue().getPruefer2());
			}
			
			@SuppressWarnings("unchecked")
			List<Integer>[] prueferFeld=new ArrayList[pruefer.size()];
			for (int liste=0; liste<prueferFeld.length; liste++)
				prueferFeld[liste]=new ArrayList<Integer>();
			
			int prueferIndex=0;
			
			for (int prueferZuTrennen: pruefer)
			{
				for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
				{
					if (prueferZuTrennen==pruefung.getValue().getPruefer1()
					    ||prueferZuTrennen==pruefung.getValue().getPruefer2())
					{
						prueferFeld[prueferIndex].add(pruefung.getKey());
					}
				}
				prueferIndex++;
			}
			
			for (List<Integer> prueferTrennen: prueferFeld)
			{
				IntVar[] trennen=new IntVar[prueferTrennen.size()];
				int index=0;
				for (int indexZP: prueferTrennen)
				{
					trennen[index]=zuPlanen[indexZP];
					index++;
				}
				
				model.allDifferent(trennen).post();
			}
			
			// Schritt 2: Planungsgruppen so trennen, dass Studierende der gleichen
			// Planungsgruppe
			// nicht 2 Prüfungen an einem Tag haben
			
			// Zunächst alle Planungsgruppen in einem Set speichern, um die Duplikate
			// herauszufiltern
			
			Set<String> planungsgruppen=new TreeSet<String>();
			for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
			{
				planungsgruppen.add(pruefung.getValue().getPlanungsgruppe());
			}
			
			// Für jede Planungsgruppe eine Liste erzeugen, in der die dazugehörigen
			// Prüfungen gesammelt werden
			// können und diese Listen in einem Feld speichern
			
			@SuppressWarnings("unchecked")
			List<Integer>[] planungsgruppenFeld=new ArrayList[planungsgruppen.size()];
			for (int liste=0; liste<planungsgruppenFeld.length; liste++)
				planungsgruppenFeld[liste]=new ArrayList<Integer>();
				
			// Name der Planungsgruppe mit den Prüfungen vergleichen, bei einer
			// Übereinstimmung wird die
			// Prüfung in der Liste gespeichert
			
			int planungsgruppenIndex=0;
			for (String planungsgruppe: planungsgruppen)
			{
				for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
				{
					if (planungsgruppe.equals(pruefung.getValue().getPlanungsgruppe()))
						planungsgruppenFeld[planungsgruppenIndex].add(pruefung.getKey());
				}
				planungsgruppenIndex++;
			}
			
			for (List<Integer> planungsgruppeTrennen: planungsgruppenFeld)
			{
				IntVar[] trennen=new IntVar[planungsgruppeTrennen.size()];
				int index=0;
				for (int indexZP: planungsgruppeTrennen)
				{
					trennen[index]=zuPlanen[indexZP];
					index++;
				}
				
				int diff=0;
				for (int indexA=0; indexA<trennen.length-1; indexA++)
				{
					for (int indexI=indexA+1; indexI<trennen.length; indexI++)
					{
						if (anzahlSlots/2-trennen.length-distanz<2)
							diff=2;
						else
							diff=anzahlSlots/2-trennen.length-distanz;
						model.distance(trennen[indexA], trennen[indexI], ">", diff).post();
					}
				}
			}
			
			// Schritt 3: Ausschlüsse berücksichtigen
			
			for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
			{
				for (Entry<Integer, Ausschluss> ausschluss: planung.getAusschluesse().entrySet())
				{
					if (ausschluss.getValue().getInhaber()==pruefung.getValue().getPruefer1()
					    |ausschluss.getValue().getInhaber()==pruefung.getValue().getPruefer2())
					{
						model.arithm(zuPlanen[pruefung.getKey()], "!=", ausschluss.getValue().getSlot()).post();
					}
				}
			}
			
			// Schritt 4: Lange Prüfungen immer in den Vormittags-Slot setzen
			
			List<Integer> langePruefungen=new ArrayList<Integer>();
			for (Entry<Integer, Pruefung> pruefung: planung.getPruefungen().entrySet())
			{
				if (pruefung.getValue().getDauer()>150&&pruefung.getValue().getDauer()<=180)
				{
					langePruefungen.add(pruefung.getKey());
				}					
			}
			
			/*
			int [] vormittags = new int [anzahlSlots/3];
			for (int morgens = 0; morgens < vormittags.length; morgens++)
				vormittags [morgens] = 3*morgens+1; 	
			*/
			
			for (int langePruefung: langePruefungen)
			{
				//model.member(zuPlanen[langePruefung], vormittags).post();
				
				/*Alternative Lösung mit dem or-Constraint, führt zu besserem Ergebnis*/
				Constraint[] lang=new Constraint[anzahlSlots/3];
				for (int index=0; index<lang.length; index++)
				{
					lang[index]=model.arithm(zuPlanen[langePruefung], "=", 3*index+1);
				}
				model.or(lang).post();				
			}			
			
			// Schritt 5: Sicherstellen, dass nicht mehr Slots verwendet werden, als
			// zur Verfügung stehen
			
			for (int index=0; index<anzahlPruefungen; index++)
			{
				model.arithm(zuPlanen[index], "<=", anzahlSlots).post();
			}
			
			// Schritt 6: Möglichst alle Slots verwenden
			
			model.atLeastNValues(zuPlanen, model.intVar(anzahlSlots-reduziere), false)
			    .post();
			
			// Schritt 7: Lösung finden
			
			Solver solver=model.getSolver();
			solver.setSearch(Search.domOverWDegSearch(zuPlanen));
			sol=solver.findSolution();
			
			if (sol!=null)
			{				
				planung.setZuPlanen(zuPlanen);
				planung.setGibtEsEineLoesung(true);
				planung.setBewertung(PPFunktionen.loesungBewerten(zuPlanen, anzahlSlots));				
			}
			else
				if (durchlauf%2==0&&distanz<=10)
				{	
					distanz++;
					if (distanz<=5)
						durchlauf+=2;
					else
						durchlauf++;
				}
				else
				{
					if (durchlauf%2!=0)
						reduziere++;
					durchlauf++;
				}
		}
		
		return planung;
	}
}
