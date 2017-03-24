/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import javafx.scene.control.Label;
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
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 *
 * @author Ania
 */
public class Tomograf extends Application {

    /**
     * Szerokość okienka
     */
    int sceneWidth = 1500;

    /**
     * Wysokość okienka
     */
    int sceneHeight = 800;
    
    TilePane tile = new TilePane();
    File file;
    VBox vb = new VBox();
    BorderPane pane;
       Slider slider1 = new Slider();
    
    @Override
    public void start(Stage primaryStage) {
slider1.setVisible(false);
        /**
         * Menu
         */
        Menu menu = new Menu("Opcje");
        MenuItem item = new MenuItem("Wybierz obraz");
        menu.getItems().add(item);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBar.setMinSize(sceneWidth, 25);
        menuBar.setVisible(true);

        /**
         * Slidery
         */
        Label label1 = new Label("Sinogram");
        Label label2 = new Label("Obraz końcowy");
        label1.fontProperty().set(Font.font(20));
        label2.fontProperty().set(Font.font(20));
        Slider slider = new Slider();
        slider.setOrientation(Orientation.HORIZONTAL);
        //tu1000 emiterow
        slider.setMax(1000);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinSize(500, 10);
        //
        slider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            int i = newvalue.intValue();
            if(i==slider.getMax()){
                slider1.setVisible(true);
            }
            else{
                slider1.setVisible(false);
            }
        });
//
     
        slider1.setMin(0);
        //tu1000 emiterow
        slider1.setMax(1000);
        //tu emitery/10
        slider1.setMajorTickUnit(10);
        slider1.setMinorTickCount(0);
        slider1.setShowTickMarks(true);
        slider1.setShowTickLabels(true);
        slider1.showTickMarksProperty();
        slider1.setSnapToTicks(true);
        slider1.setMinSize(500, 10);
        slider1.setOrientation(Orientation.HORIZONTAL);
        //domyslna wartosc
        //slider1.adjustValue(10.0);
        slider1.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            int i = newvalue.intValue();
        });
        
        vb.setSpacing(20);
        vb.setPadding(new Insets(20, 30, 20, 30));
        vb.getChildren().add(label1);
        vb.getChildren().add(slider);
        
        vb.getChildren().add(label2);
        vb.getChildren().add(slider1);

        /**
         * Emitery, detektory, kąt
         */
        Label l1 = new Label("Ilość emiterów");
        l1.fontProperty().set(Font.font(15));
        TextField tf1 = new TextField("0");
        VBox vb1 = new VBox(l1, tf1);
        vb1.setSpacing(10);
        
        Label l2 = new Label("Ilość detektorów");
        l2.fontProperty().set(Font.font(15));
        TextField tf2 = new TextField("0");
        VBox vb2 = new VBox(l2, tf2);
        vb2.setSpacing(10);
        
        Label l3 = new Label("Kąt (stopnie)");
        l3.fontProperty().set(Font.font(15));
        TextField tf3 = new TextField("0");
        VBox vb3 = new VBox(l3, tf3);
        vb3.setSpacing(10);
        
        HBox hb = new HBox(vb1, vb2, vb3);
        hb.setSpacing(30);
        hb.setPadding(new Insets(0, 30, 30, 30));

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
        VBox vb4 = new VBox(menuBar, hb);
        vb4.setSpacing(30);

        /**
         * Wybieranie pliku
         */
        FileChooser fileChooser = new FileChooser();
        
        item.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    System.out.println("srodek");
                    Picture picture = new Picture(file);
                    Image image1 = SwingFXUtils.toFXImage(picture.getBi(), null);
                    Sinogram sinogram = new Sinogram(picture, 180, 200, 1000);
                    Image image2 = SwingFXUtils.toFXImage(sinogram.getBuf(), null);
                    TomographyPicture tomografPic = new TomographyPicture(picture, sinogram);
                    Image image3 = SwingFXUtils.toFXImage(tomografPic.getBuf(), null);

                    /**
                     * Obrazy
                     */
                    tile.setPadding(new Insets(3));
                    tile.setPrefColumns(1);
                    tile.setAlignment(Pos.CENTER);
                    tile.setHgap(20);
                    
                    ImageView iw1 = new ImageView(image1);
                    iw1.setFitHeight(400);
                    iw1.setFitWidth(400);
                    tile.getChildren().add(iw1);
                    
                    ImageView iw2 = new ImageView(image2);
                    iw2.setFitHeight(400);
                    iw2.setFitWidth(400);
                    tile.getChildren().add(iw2);
                    
                    ImageView iw3 = new ImageView(image3);
                    iw3.setFitHeight(400);
                    iw3.setFitWidth(400);
                    tile.getChildren().add(iw3);
                    
                   pane.setCenter(tile);
                    System.out.println("drugi");
                }
            }
        });
        if (file == null) {
            
            pane = new BorderPane(null, null, null, vb, null);
            System.out.println("pierwszy");
        } else {
            
        }
        pane.setTop(vb4);
        pane.setBottom(vb);
        pane.setPadding(new Insets(0, 0, 35, 0));
        
        Scene scene = new Scene(pane, sceneWidth, sceneHeight);
        
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
