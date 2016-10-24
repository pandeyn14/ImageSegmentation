package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class ImgSeg_Controller 
{
    //@FXML is used for reference in fxml file
	@FXML
	//refereeing to the button in xml file
	private Button start_btn;
	@FXML
	// camera view
	private ImageView currentFrame;

	
	@FXML
	protected void startCamera(ActionEvent event) {
		
	}
	
}
