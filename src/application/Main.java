package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

// create stage which is the window, scene is the container that holds all the elements
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//load the xml to populate our stage, the root element of the scene and the controller class
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ImgSeg.fxml"));
			BorderPane root = (BorderPane) loader.load();
			ImgSeg_Controller controller = loader.getController();
			
			
			// Create Image and ImageView objects
	        Image image = new Image("application/images/test.jpg");
	        ImageView imageView = new ImageView();
	        imageView.setImage(image);
	      
	        // Display image on screen
	        StackPane Stackpaneroot = new StackPane();
	        Stackpaneroot.getChildren().add(imageView);
	        Scene scene = new Scene(Stackpaneroot, 800, 400);
	        primaryStage.setTitle("Image Display");
	        primaryStage.setScene(scene);
	        primaryStage.show();
			
			//automatic generated code		
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
