package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/*
 * Create stage and scene. 
 * stage which is the window
 * scene is the container that holds all the elements 
 */
 
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			            // load the FXML resource
						BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("imgSeg.fxml"));
						
						// create and style a scene
						Scene scene = new Scene(root, 800, 600);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						
						// create a stage with scene
	               		primaryStage.setTitle("Image Segmentation");
						primaryStage.setScene(scene);
						
						// show the GUI
						primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
