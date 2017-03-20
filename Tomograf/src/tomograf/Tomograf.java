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

    
        Picture picture = new Picture();
        
        
        TilePane tile=new TilePane();
        tile.setPrefColumns(3);
        ImageView iw1 = new ImageView(picture.getImg());
        iw1.setFitHeight(150);
        iw1.setFitWidth(150);
        tile.getChildren().add(iw1);
        ImageView iw2 = new ImageView();
        ImageView iw3 = new ImageView();
        ImageView iw4 = new ImageView();

        Pane pane = new BorderPane(hb, tile, iw2, iw3, iw4);

        /*
        label1.setIcon(imgIcon);
        Sinogram sinogram = new Sinogram(picture, 180, 200,1000);
        ImageIcon imgIcon2 = new ImageIcon(sinogram.getBuf());
      label2.setIcon(imgIcon2);
        TomographyPicture tomografPic=new TomographyPicture(picture,sinogram);
         ImageIcon imgIcon3 = new ImageIcon(tomografPic.getBuf());
        label3.setIcon(imgIcon3);
         */
        Scene scene = new Scene(pane, 700, 700);
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
