package gui.view;

import gui.Pruefungsplanung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PPController
{
	private Pruefungsplanung	mainApp;
	
	@FXML
	TextArea									ausgabe;
	
	public void setMainApp (Pruefungsplanung mainApp)
	{
		this.mainApp=mainApp;
	}
	
	@FXML
	protected void verbindungAbfragen (ActionEvent event)
	{
		mainApp.showVerbindungAbfragenDialog(ausgabe);
	}
	
	@FXML
	protected void datumAbfragen (ActionEvent event)
	{
		mainApp.showDatumAbfragenDialog(ausgabe);
	}
}
