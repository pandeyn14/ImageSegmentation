package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * 1. Choose Image for Segmentation and Edge Detection
 * 2. Apply Green channel or Red Channel and save images
 * 3. Convert image to mat
 * 4. Perform edge detection using Canny  or Sobel operator
 * 5. Convert mat to image
 * 6. Output image to ImageView or save for future reference
 * 
 * */

public class ImgSegController extends Stage
{
	        private static final int List = 0;

			//@FXML is used for reference in fxml file
		    // FXML buttons
	        //load image for processing
	        @FXML
			private Button loadImageButton;
	        
	        @FXML
			Button saveImageButton = new Button("Save Image");
			
			@FXML
			//to display image 
			private ImageView currentFrame;
			
			//enable or disable canny and threshold slider
			@FXML
			private CheckBox canny;
			
			//enable or disable canny and threshold slider
			@FXML
			private CheckBox sobel;
			
			// checkbox for enabling/disabling greenChannel
			@FXML
			private CheckBox greenChannel;
			
			// checkbox for enabling/disabling redChannel
			@FXML
			private CheckBox redChannel;
			
			// checkbox for enabling/disabling blueChannel
			@FXML
			private CheckBox blueChannel;
			
			@FXML
			private Slider threshold;
			
			//Display threshold value on GUI
			@FXML
			private Label thresholdValue;
			
			@FXML 
			private Label thresholdLabel;
			
			@FXML 
			private MenuItem Exit;
			
			@FXML 
			private MenuItem Save;
			
			@FXML 
			private MenuItem About;
			
			@FXML 
			private MenuItem Help;

			private Mat image;
			
			File outputFile = new File("");
			File InputFile = new File("");
			
			private String imagepath = "";
			private String outputImgPath="";
			
			Image img ;
			Image outputImage;
		
			//Choose image for processing
			@FXML
			protected void chooseImage(ActionEvent event){
				
				FileChooser chooser = new FileChooser();
				
				//choose only image type 
				chooser.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.png", "*.jpg", "*.gif"));
				
			    chooser.setTitle("Open Image");
			    InputFile = chooser.showOpenDialog(new Stage());
			    if(InputFile != null) {
			    				   
					try {
						imagepath = InputFile.toURI().toURL().toString();
				        img = new Image(imagepath,/*width*/760,/*height*/460,/*orig size*/false, true);
				        this.image = imgToMat(img);
						this.currentFrame.setImage(this.matToImg(this.image));
						
						canny.setDisable(false);
						sobel.setDisable(false);
						greenChannel.setDisable(false);
						blueChannel.setDisable(false);
						redChannel.setDisable(false);
						saveImageButton.setDisable(false);
						thresholdLabel.setDisable(false);
						Save.setDisable(false);
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			    }
			}

			//Save the image
			@FXML
			protected void saveImage(ActionEvent event){
				
				FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Save Image");
				outputFile = fileChooser.showSaveDialog(new Stage());
	            if (outputFile != null) {
	                try {
	                	outputImgPath = outputFile.toURI().toURL().toString();
	                	outputImage = new Image(outputImgPath,/*width*/760,/*height*/460,/*orig size*/false, true);
	                    ImageIO.write(SwingFXUtils.fromFXImage(currentFrame.getImage(),
	                        null), "png", outputFile);
	                } catch (IOException ex) {
	                    System.out.println(ex.getMessage());
	                }
	            }
				
			}
			
			
			//load original by default if nothing is selected or load processed image as per user selection	
			@FXML
			protected void loadImage(ActionEvent event) {
		
				this.image = imgToMat(img);
				this.currentFrame.setImage(this.matToImg(this.image));
				Mat imageFrame = this.image;
				
				if (this.canny.isSelected()){
					
					imageFrame = this.applyCanny(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					thresholdValue.setText((Double.toString(threshold.getValue())).format("%.2f", threshold.getValue()));
					currentFrame.setImage(processedImage);
				}
				
				if (this.sobel.isSelected()){

					imageFrame = this.applySobel(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					thresholdValue.setText((Double.toString(threshold.getValue())).format("%.2f", threshold.getValue()));
					currentFrame.setImage(processedImage);
				}
				
				if (this.greenChannel.isSelected()){
					imageFrame = this.applyRedChannel(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					thresholdValue.setText(""); //reset
					currentFrame.setImage(processedImage);
				}
				
				if (this.redChannel.isSelected()){
					imageFrame = this.applyGreenChannel(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					thresholdValue.setText(""); //reset
					currentFrame.setImage(processedImage);
				}
				
				if (this.blueChannel.isSelected()){
					imageFrame = this.applyBlueChannel(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					thresholdValue.setText(""); //reset
					currentFrame.setImage(processedImage);
				}
								
			}
			
		   //convert javaFX image to mat object	
		   public static Mat imgToMat(Image image) {
				   
				    int Imageheight = (int) image.getHeight();	
				    int Imagewidth = (int) image.getWidth();				
					byte[] buffer = new byte[Imagewidth * Imageheight * 4];
			
					PixelReader pixelreader = image.getPixelReader();
					WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
					pixelreader.getPixels(0, 0, Imagewidth, Imageheight, format, buffer, 0, Imagewidth * 4);
			
					Mat mat = new Mat(Imageheight, Imagewidth, CvType.CV_8UC4);
					mat.put(0, 0, buffer);
					return mat;
		   }
			   
		   //convert mat object to javaFX image
		   private Image matToImg(Mat image)
		   {
					// create buffer
					MatOfByte buffer = new MatOfByte();
					// encode the image in the buffer, according to the PNG format
					Imgcodecs.imencode(".png", image, buffer);
					// build and return an Image created 
					return new Image(new ByteArrayInputStream(buffer.toArray()));
			}

            //apply canny operator for edge detection
			private Mat applyCanny(Mat image)
			{
				// create grayscale and final result objects
				Mat grayImage = new Mat();
				Mat cannymat = new Mat();
				
				// convert rgb to grayscale
				Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
				
				// reduce noise with a 3x3 kernel
				Imgproc.blur(grayImage, cannymat, new Size(3, 3));
				
				// canny detector, with ratio of lower:upper threshold of 3:1
				Imgproc.Canny(cannymat, cannymat, this.threshold.getValue(), this.threshold.getValue() * 3);
				
				// using Canny's output as a mask, display the result
				Mat canny = new Mat();
				image.copyTo(canny, cannymat);
				
				return canny;
			}
			
            //apply sobel for edge detection
			private Mat applySobel(Mat image)
			{
				// create grayscale and final result objects     
				Mat gray = new Mat();
		        Mat sobel = new Mat(); 

		        //Mat object to store gradient and absolute gradient
		        Mat x = new Mat();
		        Mat abs_x = new Mat();

		        Mat y = new Mat();
		        Mat abs_y = new Mat();

		        //Converting to grayscale image
		        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
		        
		      //Calculating gradient in vertical direction
		        Imgproc.Sobel(gray, y, CvType.CV_16S, 0, 1, 3, 1, 0);

		        //Calculating gradient in horizontal direction
		        Imgproc.Sobel(gray, x, CvType.CV_16S, 1, 0, 3, 1, 0);		        

		        //Calculating absolute value of gradients in both the direction
		        Core.convertScaleAbs(x, abs_x);
		        Core.convertScaleAbs(y, abs_y);

		        Core.addWeighted(abs_x, threshold.getValue(), abs_y, threshold.getValue(), threshold.getValue() *2, sobel);
						
				return sobel;
	
			}
            
			//split image into blue, red and green channel, return blue
			private Mat applyBlueChannel(Mat imageFrame) {

				List<Mat> rgb = new ArrayList<Mat>(3);
				Core.split(imageFrame, rgb);
				Mat Blue = rgb.get(0);
				return Blue;
				
			}
			
			//split image into blue, red and green channel, return red
			private Mat applyRedChannel(Mat imageFrame) {

				List<Mat> rgb = new ArrayList<Mat>(3);
				Core.split(imageFrame, rgb);
				Mat Red = rgb.get(1);
				return Red;
			}
			
			//split image into blue, red and green channel, return green
			private Mat applyGreenChannel(Mat imageFrame) {

				List<Mat> rgb = new ArrayList<Mat>(3);
				Core.split(imageFrame, rgb);
				Mat Green = rgb.get(2);
				return Green;
				
			}

/*****************************************************************************************************/
			//code executed when Sobel checkbox is selected
			@FXML
			protected void sobelSelected()
			{
				//uncheck background removal/dilateErode checkbox, disable inverse
				if (this.greenChannel.isSelected())  	this.greenChannel.setSelected(false);
				
			    if (this.redChannel.isSelected())       this.redChannel.setSelected(false);
			    
			    if (this.blueChannel.isSelected())       this.blueChannel.setSelected(false);
				
				if (this.canny.isSelected())            this.canny.setSelected(false);
				
				//if sobel checkbox is selected, enable its slider
				if (this.sobel.isSelected())            
					{
					    loadImageButton.setDisable(false);
						this.threshold.setDisable(false);
					}
					
				else                                    this.threshold.setDisable(true);
										
			}
			
			//code executed when Canny checkbox is selected
			@FXML
			protected void cannySelected()
			{
				//uncheck background removal/dilateErode checkbox, disable inverse
				if (this.greenChannel.isSelected())  	   this.greenChannel.setSelected(false);
				
			    if (this.redChannel.isSelected())          this.redChannel.setSelected(false);
			    
			    if (this.blueChannel.isSelected())         this.blueChannel.setSelected(false);
				
				if (this.sobel.isSelected())               this.sobel.setSelected(false);
				
				//if canny checkbox is selected, enable its slider
				if (this.canny.isSelected())      
					{
						this.threshold.setDisable(false);
						loadImageButton.setDisable(false);
					}
					
				else                                       this.threshold.setDisable(true);
					
			}
			
			//code executed when GreenChannel checkbox is selected
			@FXML
			protected void greenChannelSelected()
			{
				// uncheck canny checkbox, disable its slider
				if (this.canny.isSelected())
				{
					this.canny.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.sobel.isSelected())
				{
					this.sobel.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.redChannel.isSelected())       this.redChannel.setSelected(false);
				
				if (this.blueChannel.isSelected())      this.blueChannel.setSelected(false);
				
				if (this.greenChannel.isSelected())     loadImageButton.setDisable(false);
					
			}
			
			//code executed when redChannel checkbox is selected
			@FXML
			protected void redChannelSelected()
			{
				// uncheck canny checkbox, disable its slider
				if (this.canny.isSelected())
				{
					this.canny.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.sobel.isSelected())
				{
					this.sobel.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.greenChannel.isSelected())      this.greenChannel.setSelected(false);
				
				if (this.blueChannel.isSelected())       this.blueChannel.setSelected(false);
				
				if (this.redChannel.isSelected())        loadImageButton.setDisable(false);
					
			}
			
			//code executed when redChannel checkbox is selected
			@FXML
			protected void blueChannelSelected()
			{
				// uncheck canny checkbox, disable its slider
				if (this.canny.isSelected())
				{
					this.canny.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.sobel.isSelected())
				{
					this.sobel.setSelected(false);					
					this.threshold.setDisable(true);
				}
				
				if (this.greenChannel.isSelected())      this.greenChannel.setSelected(false);
				
				if (this.redChannel.isSelected())        this.redChannel.setSelected(false);
				
				if (this.blueChannel.isSelected())       loadImageButton.setDisable(false);
					
			}
			
			
			
			@FXML
			private void exitOnAction(){
				
				Exit.setOnAction(actionEvent -> Platform.exit());
			}
			
			@FXML
			public void PopUp(){
			    
			   this.setTitle("About");
			}
			
			@FXML
			public void help(){
			    
			    this.setTitle("Help");
			}
			
			
}// end of class
