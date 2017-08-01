package gui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import pp.controller.PPdbActions;

public class PPVerbindungAbfragenController
{
	@FXML
	private ToggleGroup	verbindung;
	
	@FXML
	private TextField		ipAdresse;
	
	@FXML
	private TextField		port;
	
	@FXML
	private TextField		db;
	
	@FXML
	private TextField		benutzerName;
	
	@FXML
	private TextField		passwort;
	
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
	protected void verbindungWaehlen (ActionEvent event)
	{
		String ipAdresseS=null;
		String dbS=null;
		String portS=null;
		String benutzerNameS=null;
		String passwortS=null;
		
		RadioButton selected=(RadioButton)verbindung.getSelectedToggle();
		switch (selected.getText())
		{
			case "Verbindung zur lokalen Test-DB":
				ipAdresseS="localhost";
				dbS="pp_test";
				benutzerNameS="pp-admin";
				passwortS="pp-admin";
				break;
			case "Verbindung zum Liveserver":
				ipAdresseS="193.175.198.25";
				dbS="pp_live";
				portS="3306";
				benutzerNameS="pp-select";
				passwortS="K3w3pMF}{R!#Gj*XBbEAxC8zrs][dxye";
				break;
			case "Manuelle Eingabe":
				ipAdresseS=ipAdresse.getText();
				portS=port.getText();
				dbS=db.getText();
				benutzerNameS=benutzerName.getText();
				passwortS=passwort.getText();
				break;
		}
		ausgabe.setText(PPdbActions.connect(ipAdresseS, dbS, portS, benutzerNameS, passwortS));
		dialogStage.close();
	}
}
