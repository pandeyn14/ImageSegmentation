package application;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

/*
 * 1. Read Image
 * 2. Convert image to mat
 * 3. Isolate objects and save the output
 * 4. Perform edge detection using Canny operator
 * 5. Convert mat to image
 * 6. Output image to ImageView
 * */

public class ImgSegController 
{
	        //@FXML is used for reference in fxml file
		    // FXML buttons
			@FXML
			//load image for processing
			private Button loadImageButton;
			
			@FXML
			//to display image 
			private ImageView currentFrame;
			
			//enable canny and threshold slider
			@FXML
			private CheckBox canny;
			
			@FXML
			private Slider threshold;
			
			// checkbox for enabling/disabling background removal
			@FXML
			private CheckBox dilateErode;
			
			// inverse the threshold value for background removal
			@FXML
			private CheckBox inverse;
	        
			private Mat image;
			
			private String imgpath = "application/images/test.bmp";
			
			Image img = new Image(imgpath,/*width*/760,/*height*/460,/*orig size*/false, true);

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
				
			//load original or processed image as per user selection	
			@FXML
			protected void loadImage(ActionEvent event) {
		
				this.image = imgToMat(img);
				this.currentFrame.setImage(this.matToImg(this.image));
				Mat imageFrame = this.image;
				
				if (this.canny.isSelected()){
					
					imageFrame = this.applyCanny(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					currentFrame.setImage(processedImage);

				}
				
				if (this.dilateErode.isSelected()){
					imageFrame = this.applyBackgroundRemoval(imageFrame);	
					Image processedImage =  matToImg(imageFrame);
					currentFrame.setImage(processedImage);
				}
				
				
			}
			
			private Mat applyBackgroundRemoval(Mat imageFrame) {
				// TODO Auto-generated method stub
				return null;
			}

			private Mat applyCanny(Mat image)
			{
				// create objects
				Mat grayImage = new Mat();
				Mat detectedEdges = new Mat();
				
				// convert rgb to grayscale
				Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
				
				// reduce noise with a 3x3 kernel
				Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));
				
				// canny detector, with ratio of lower:upper threshold of 3:1
				Imgproc.Canny(detectedEdges, detectedEdges, this.threshold.getValue(), this.threshold.getValue() * 3);
				
				// using Canny's output as a mask, display the result
				Mat result = new Mat();
				image.copyTo(result, detectedEdges);
				
				return result;
			}
			
			
			//code executed when Canny checkbox is selected
			@FXML
			protected void cannySelected()
			{
				//uncheck background removal/dilateErode checkbox, disable inverse
				if (this.dilateErode.isSelected())
				{
					this.dilateErode.setSelected(false);
					this.inverse.setSelected(false);
					this.inverse.setDisable(true);
		
				}
				
				//if canny checkbox is selected, enable its slider
				if (this.canny.isSelected())
					this.threshold.setDisable(false);
				else
					this.threshold.setDisable(true);
					
			}
			
			//code executed when the background removal checkbox is selected
			@FXML
			protected void dilateErodeSelected()
			{
				// uncheck canny checkbox, disable its slider
				if (this.canny.isSelected())
				{
					this.canny.setSelected(false);
					this.threshold.setDisable(true);
				}
				
				// if dilateErode checkbox is selected, enable inverse
				if (this.dilateErode.isSelected())
					this.inverse.setDisable(false);
				else
					this.inverse.setDisable(true);
					
			}
		
	
}
