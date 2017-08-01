package gui;

import gui.objekte.FXML_Loader;
import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Pruefungsplanung extends Application
{
	private Stage				primaryStage;
	private BorderPane	rootLayout;
	private FXML_Loader	fxml;
	
	public Pruefungsplanung ()
	{
		this.rootLayout=new BorderPane();
		this.primaryStage=new Stage();
		this.fxml=new FXML_Loader();
	}
	
	@Override
	public void start (Stage primaryStage)
	{
		this.primaryStage.setTitle("Prüfungsplanung FB 6");
		
		fxml.initRootLayout(this);
		fxml.showPruefungsPlanung(this);
	}
	
	public void showVerbindungAbfragenDialog (TextArea ausgabe)
	{
		fxml.showVerbindungAbfragenDialog(ausgabe, this);
	}
	
	public void showDatumAbfragenDialog (TextArea ausgabe)
	{
		fxml.showDatumAbfragenDialog(ausgabe, this);
	}
	
	public BorderPane getRootLayout ()
	{
		return rootLayout;
	}
	
	public void setRootLayout (BorderPane rootLayout)
	{
		this.rootLayout=rootLayout;
	}
	
	public Stage getPrimaryStage ()
	{
		return primaryStage;
	}
	
	public void setPrimaryStage (Stage primaryStage)
	{
		this.primaryStage=primaryStage;
	}	
	
	public static void main (String[] args)
	{
		launch(args);
	}
}
