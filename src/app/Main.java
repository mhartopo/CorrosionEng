package app;

import java.io.IOException;

import app.model.core.Calculation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private Calculation calc;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Test");
        initWindow();
	}
	
	public void initWindow() {
		calc = new Calculation();
		calc.calcAll();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MainField.fxml"));
		try {
			AnchorPane pane = (AnchorPane)loader.load();
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			
			primaryStage.setTitle("Corrosion Engineering");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
