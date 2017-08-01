package pp.controller;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pp.objekte.Ausschluss;
import pp.objekte.Pruefung;
import pp.objekte.Slot;

public class PPdbActions
{
	private static Statement	statement	=null;
	private static Connection	connection=null;
	private static String			sql				=null;
	private static ResultSet	rs				=null;
	
	public static String connect (String ipAdresse, String db, String port, String benutzerName,
	    String passwort)
	{
		try
		{
			connection=DriverManager.getConnection(
			    "jdbc:mariadb://"+ipAdresse+":"+port+"/"+db+"?useSSL=false", benutzerName, passwort);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Verbindung zur Datenbank "+db+" konnte mit \nBenutzername: "+benutzerName
			    +" und\nPasswort: "+passwort+"\nnicht hergestellt werden!\n";
		}
		return "Verbindung zur Datenbank "+db+" hergestellt!\n";
	}
	
	public static Map<Integer, Pruefung> pruefungenEinlesen (Map<Integer, Pruefung> pruefungen)
	{
		if (connection!=null)
		{
			int startIndex=0;
			int pruefer2=0;
			String pruefer2S=null;
			
			try
			{
				if (statement==null)
					statement=connection.createStatement();
				
				sql="select DISTINCT (SELECT pd.status from pruefungsdetails AS pd WHERE pd.pordnr = p.pordnr ORDER BY pd.zeit DESC LIMIT 1) AS statusid, "
				    +"p.modul, s.bezeichnung as studiengang, p.semester, p.modulbetreuer, p.pruefer1, p.pruefer2, "
				    +"CONCAT (u2.titel,' ',u2.vorname,' ',u2.nachname) AS prueferA, "
				    +"IF (p.pruefer2 IS NOT NULL, CONCAT (u3.titel,' ',u3.vorname,' ',u3.nachname), '') AS prueferB, "
				    +"(SELECT pd.pruefungsform from pruefungsdetails AS pd WHERE pd.pordnr = p.pordnr ORDER BY pd.zeit DESC LIMIT 1) AS pruefungsformid, "
				    +"(SELECT bezeichnung from pruefungsarten AS pa WHERE pa.id = pruefungsformid) AS pruefungsform, "
				    +"(SELECT pd.anzahl from pruefungsdetails AS pd WHERE pd.pordnr = p.pordnr ORDER BY pd.zeit DESC LIMIT 1) AS anzahl, "
				    +"(SELECT pd.dauer from pruefungsdetails AS pd WHERE pd.pordnr = p.pordnr ORDER BY pd.zeit DESC LIMIT 1) AS dauer "
				    +"from pruefungen AS p join studiengaenge as s join users as u2 join users as u3 "
				    +"where modul NOT like ('WM%') and p.pordnr > 1000 and p.studiengang = s.id and p.pruefer1 = u2.id and (p.pruefer2 is null or( p.pruefer2 is not null and p.pruefer2 = u3.id)) and p.semester > 1 "
				    +"having statusid = 1 and pruefungsformid != 5 "+"order by studiengang, semester";
				
				rs=statement.executeQuery(sql);
				while (rs.next())
				{
					String modul=rs.getString("modul");					
					String planungsgruppe=rs.getString("studiengang")+"-"+(rs.getInt("semester")-1)
					    +". Semester";
					int moduladmin=rs.getInt("modulbetreuer");
					int pruefer1=rs.getInt("pruefer1");
					String pruefer1S=rs.getString("prueferA");
					if (rs.getString("pruefer2")!=null)
					{
						pruefer2=rs.getInt("pruefer2");
						pruefer2S=rs.getString("prueferB");
					}
					
					String art=rs.getString("pruefungsform");
					int anzahl=rs.getInt("anzahl");
					int dauer=rs.getInt("dauer");
					
					pruefungen.put(startIndex, new Pruefung(modul, planungsgruppe, moduladmin, pruefer1,
					    pruefer1S, pruefer2, pruefer2S, art, anzahl, dauer));
					startIndex++;
					pruefer2=0;
					
				}
				rs.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return pruefungen;
		}
		else
		{
			return null;
		}
	}
	
	public static Map<Integer, Ausschluss> ausschluesseEinlesen (
	    Map<Integer, Ausschluss> ausschluesse, List<Slot> slots)
	{
		if (connection!=null)
		{
			int startIndex=0;
			
			try
			{
				if (statement==null)
					statement=connection.createStatement();
				
				sql="select pp.beginn, pp.ende, pp.user "+"from pruefungspraeferenzen as pp "
				    +"where pp.beginn is not null "+"and pp.ende is not null "+"and pp.aktiv = 1 "
				    +"and pp.user not in (46,74) "+"order by user";
				
				rs=statement.executeQuery(sql);
				
				while (rs.next())
				{
					String beginn=rs.getString("beginn");
					Date beginnDatum=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginn);
					String ende=rs.getString("ende");
					Date endeDatum=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ende);
					int user=rs.getInt("user");
					
					for (Slot slot: slots)
					{
						Date beginnSlot=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(slot.getStart());
						Date endeSlot=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(slot.getEnde());
						
						if (beginnDatum.getTime()<=endeSlot.getTime()
						    &&endeDatum.getTime()>=beginnSlot.getTime())
						{
							ausschluesse.put(startIndex, new Ausschluss(slot.getNummer(), user));
							startIndex++;
						}
					}
				}
				rs.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return ausschluesse;
		}
		else
		{
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Keine Verbindung zur Datenbank");
			alert.setHeaderText(
			    "Das Auslesen der Daten war nicht möglich, da keine Verbindung zu einer Datenbank besteht.");
			alert.setContentText(
			    "Bitte stellen Sie daher zunächst eine Verbindung zur gewünschten Datenbank her!");
			
			alert.show();
			return null;
		}
		
	}
	
	public static String close ()
	{
		if (connection!=null)
		{
			try
			{
				connection.close();
				if (statement!=null)
					statement.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			return "Verbindung getrennt!\n";
		}
		else
			return "Es ist keine Verbindung vorhanden!\n";
	}
}
