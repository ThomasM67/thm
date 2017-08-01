package gui.view;

import java.text.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pp.controller.PPFunktionen;
import pp.controller.PPdbActions;
import pp.objekte.Rahmen;

public class PPDatumAbfragenController
{
	@FXML
	private DatePicker	startdatum;
	
	@FXML
	private DatePicker	endedatum;
	
	@FXML
	private DatePicker	ausnahme1;
	
	@FXML
	private DatePicker	ausnahme2;
	
	@FXML
	private DatePicker	ausnahme3;
	
	private Stage				dialogStage;
	private TextArea		ausgabe;
	
	@FXML
	private void initialize ()
	{
	}
	
	public void setDialogStage (Stage dialogStage, TextArea ausgabe)
	{
		this.dialogStage=dialogStage;
		this.ausgabe=ausgabe;
	}
	
	@FXML
	protected void pruefungenPlanen (ActionEvent event) throws ParseException
	{
		String startdatumS=null, endedatumS=null, ausnahme1S=null, ausnahme2S=null, ausnahme3S=null;
		if (startdatum.getValue()!=null)
			startdatumS=startdatum.getValue().toString();
		if (endedatum.getValue()!=null)
			endedatumS=endedatum.getValue().toString();
		if (ausnahme1.getValue()!=null)
			ausnahme1S=ausnahme1.getValue().toString();
		if (ausnahme2.getValue()!=null)
			ausnahme2S=ausnahme2.getValue().toString();
		if (ausnahme3.getValue()!=null)
			ausnahme3S=ausnahme3.getValue().toString();
		
		if (startdatum.getValue()==null||endedatum.getValue()==null)
		{
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Fehlende Angaben");
			alert.setHeaderText(
			    "Für die Durchführung einer Planung wird der Zeitraum der Prüfungsphase benötigt!");
			alert.setContentText(
			    "Bitte geben Sie daher mindestens das Startdatum (ein Montag) und das Endedatum (ein Freitag) ein!");
			
			alert.show();
		}
		else
			if (startdatum.getValue().isAfter(endedatum.getValue()))
			{
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("Falsche Datumsangaben");
				alert.setHeaderText("Das Endedatum muss nach dem Startdatum liegen!!");
				alert.setContentText(
				    "Bitte geben Sie daher das korrekte Startdatum (ein Montag) und das korrekte Endedatum (ein Freitag) ein!");
				
				alert.show();
			}		
			else
			{
				ausgabe.setText("Vielen Dank für Ihre Angaben, die Suche kann beginnen!");
				dialogStage.close();
				
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("Lösung suchen");
				alert.setHeaderText(
				    "Es wird nun eine Lösung gesucht, welche die gemachten Vorgaben berücksichtigt."
				        +" Dies kann einen Moment dauern!");
				alert.setContentText(
				    "Wenn eine entsprechende Meldung in dem Ausgabefenster erscheint und diese Anzeige geschlossen wird, ist die Suche abgeschlossen!");
				
				alert.show();
				ausgabe.setText(PPFunktionen.pruefungenPlanen(
				    new Rahmen(startdatumS, endedatumS, ausnahme1S, ausnahme2S, ausnahme3S)));
				alert.close();
				PPdbActions.close();
			}
	}
}
