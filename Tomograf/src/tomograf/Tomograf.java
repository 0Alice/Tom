/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import java.awt.Label;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import java.awt.image.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;

/**
 *
 * @author Ania
 */
public class Tomograf extends Application {

    @Override
    public void start(Stage primaryStage) {

        Slider slider = new Slider();
        slider.setOrientation(Orientation.HORIZONTAL);
        //tu1000 emiterow
        slider.setMax(1000);
        slider.setMin(0);
        slider.setMinSize(500, 10);
        slider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            int i = newvalue.intValue();
        });

        HBox hb = new HBox();

        TilePane tile = new TilePane();
        tile.setPadding(new Insets(3));
        tile.setPrefColumns(1);
        tile.setAlignment(Pos.CENTER);
        tile.setHgap(20);
        Picture picture = new Picture();
        Image image1 = SwingFXUtils.toFXImage(picture.getBi(), null);
        Sinogram sinogram = new Sinogram(picture, 180, 200,1000);
        Image image2 = SwingFXUtils.toFXImage(sinogram.getBuf(), null);
         TomographyPicture tomografPic=new TomographyPicture(picture,sinogram);
        Image image3 = SwingFXUtils.toFXImage(tomografPic.getBuf(), null);

        ImageView iw1 = new ImageView(image1);
        iw1.setFitHeight(300);
        iw1.setFitWidth(300);
        tile.getChildren().add(iw1);

        ImageView iw2 = new ImageView(image2);
        iw2.setFitHeight(300);
        iw2.setFitWidth(300);
        tile.getChildren().add(iw2);

        ImageView iw3 = new ImageView(image3);
        iw3.setFitHeight(300);
        iw3.setFitWidth(300);
        tile.getChildren().add(iw3);

        // label1.setIcon(imgIcon);
        /*   Label label2=new Label();
        Sinogram sinogram = new Sinogram(picture, 180, 200,1000);
        ImageIcon imgIcon2 = new ImageIcon(sinogram.getBuf());
        
         */
        //label2.setIcon(imgIcon2);
        /*
        TomographyPicture tomografPic=new TomographyPicture(picture,sinogram);
         ImageIcon imgIcon3 = new ImageIcon(tomografPic.getBuf());
        label3.setIcon(imgIcon3);
         */
        Pane pane = new BorderPane(tile, hb, null, slider, null);
       
        Scene scene = new Scene(pane, 1500  , 800);
        primaryStage.setTitle("Tomograf");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
