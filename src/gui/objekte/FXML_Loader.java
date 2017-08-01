package gui.objekte;

import java.io.IOException;

import gui.Pruefungsplanung;
import gui.view.PPController;
import gui.view.PPDatumAbfragenController;
import gui.view.PPVerbindungAbfragenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXML_Loader
{
	public void initRootLayout (Pruefungsplanung mainapp)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Pruefungsplanung.class.getResource("view/PPRootLayout.fxml"));
			mainapp.setRootLayout((BorderPane)loader.load());
			
			Scene scene=new Scene(mainapp.getRootLayout());
			mainapp.getPrimaryStage().setScene(scene);
			mainapp.getPrimaryStage().show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showPruefungsPlanung (Pruefungsplanung mainapp)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Pruefungsplanung.class.getResource("view/PP.fxml"));
			AnchorPane pp=(AnchorPane)loader.load();
			
			mainapp.getRootLayout().setCenter(pp);
			PPController controller=loader.getController();
			controller.setMainApp(mainapp);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showVerbindungAbfragenDialog (TextArea ausgabe, Pruefungsplanung mainapp)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Pruefungsplanung.class.getResource("view/PPVerbindungAbfragen.fxml"));
			AnchorPane page=(AnchorPane)loader.load();
			
			Stage dialogStage=new Stage();
			dialogStage.setTitle("Verbindung zur DB abfragen");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainapp.getPrimaryStage());
			Scene scene=new Scene(page);
			dialogStage.setScene(scene);
			
			PPVerbindungAbfragenController controller=loader.getController();
			controller.setDialogStage(dialogStage, ausgabe);
			
			dialogStage.showAndWait();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showDatumAbfragenDialog (TextArea ausgabe, Pruefungsplanung mainapp)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Pruefungsplanung.class.getResource("view/PPDatumAbfragen.fxml"));
			AnchorPane page=(AnchorPane)loader.load();
			
			Stage dialogStage=new Stage();
			dialogStage.setTitle("Relevante Daten abfragen");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainapp.getPrimaryStage());
			Scene scene=new Scene(page);
			dialogStage.setScene(scene);
			
			PPDatumAbfragenController controller=loader.getController();
			controller.setDialogStage(dialogStage, ausgabe);
			
			dialogStage.showAndWait();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}