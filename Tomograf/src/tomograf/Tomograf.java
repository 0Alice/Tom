/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 */
package tomograf;

import com.sun.javafx.collections.ElementObservableListDecorator;
import java.awt.Color;
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
import java.io.IOException;
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
import static java.lang.Math.pow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

/**
 *
 * @author Ania
 */
public class Tomograf extends Application {

    /**
     * emitery
     */
    private int emiters = 500;

    /**
     * detektory
     */
    private int detectors = 500;

    /**
     * kat
     */
    private int angle = 360;

    /**
     * parametr splotu
     */
    private int splot = 16;

    /**
     * suwak sinogramu
     */
    private int sliderSinogramValue;

    /**
     * suwak obrazaka wynikowego
     */
    private int sliderPictureValue;

    /**
     * Szerokość okienka
     */
    private int sceneWidth = 1600;

    /**
     * Wysokość okienka
     */
    private int sceneHeight = 800;

    final ScrollBar sc = new ScrollBar();
    TilePane tile = new TilePane();
    File file;
    VBox vb = new VBox();
    BorderPane pane;
    Slider slider1 = new Slider();
    Image image2;
    TomographyPicture tomografPic;
    Image image3;
    ImageView iw3;
    ImageView iw2;
    Sinogram sinogram;
    String sex;
    ScrollPane scrollPane;
    ScrollPane scrollPane2;
    BufferedImage finalBufferedImage;
    ImageView iw1;
    Slider slider;
    Label label1;
    Label label2;
    Picture picture;
    File file1;

    @Override
    public void start(Stage primaryStage) {
        slider1.setVisible(false);

        /**
         * Menu
         */
        Menu menu = new Menu("Opcje");
        Menu menu2 = new Menu("Statystyki");
        MenuItem item = new MenuItem("Wybierz obraz");
        MenuItem item2 = new MenuItem("Zapis DICOM");
        MenuItem item3 = new MenuItem("Błąd średniokwadratowy");
        MenuItem item4 = new MenuItem("Szczegółowe");
        menu2.getItems().addAll(item3, item4);
        menu.getItems().add(item);
        menu.getItems().add(item2);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu, menu2);
        menuBar.setMinSize(sceneWidth, 25);
        menuBar.setVisible(true);

        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage stageDicom = new Stage();

                /**
                 * Identyfikator pacjenta Nazwisko pacjenta Data urodzin
                 * pacjenta Płeć pacjenta Wiek pacjenta Data badania Komentarze?
                 * Badana czesc cisla
                 */
                Label ld0 = new Label("   DANE PACJENTA");
                ld0.fontProperty().set(Font.font(20));
                ld0.setAlignment(Pos.CENTER);

                Label ld1 = new Label("Nazwisko");
                TextField tfd1 = new TextField();
                HBox hBD = new HBox(ld1, tfd1);
                hBD.setSpacing(40);

                Label ld2 = new Label("Data urodzenia");
                TextField tfd2 = new TextField();
                HBox hBD2 = new HBox(ld2, tfd2);
                hBD2.setSpacing(10);

                Label ld3 = new Label("Wiek");
                TextField tfd3 = new TextField();
                HBox hBD3 = new HBox(ld3, tfd3);
                hBD3.setSpacing(20);

                Label ld4 = new Label("Płeć pacjenta");
                ObservableList<String> options
                        = FXCollections.observableArrayList(
                                "F",
                                "M"
                        );

                final ComboBox comboBox = new ComboBox(options);
                comboBox.valueProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                        sex = (String) newValue;
                    }
                });

                HBox hBD4 = new HBox(ld4, comboBox);
                hBD4.setSpacing(20);

                Label ld6 = new Label("Data badania");
                TextField tfd6 = new TextField();
                HBox hBD6 = new HBox(ld6, tfd6);
                hBD6.setSpacing(20);

                Label ld5 = new Label("Komentarze");
                TextField tfd5 = new TextField();
                tfd5.setMinSize(150, 25);
                HBox hBD5 = new HBox(ld5, tfd5);
                hBD5.setSpacing(28);

                Button bt = new Button("Zapisz");
                bt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("plec");
                        System.out.println(sex);
                        try {
                            JpgDicom dicom = new JpgDicom(primaryStage, finalBufferedImage, sex, tfd2.getText(), tfd5.getText(), tfd6.getText(), tfd1.getText());
                        } catch (IOException ex) {
                            System.out.println("cos poszlo nie tak");
                            Logger.getLogger(Tomograf.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        stageDicom.close();
                    }
                });

                VBox vBDicom = new VBox(ld0, hBD, hBD2, hBD4, hBD6, hBD5, bt);
                vBDicom.setSpacing(20);
                vBDicom.setPadding(new Insets(20, 20, 20, 20));

                Scene scene1 = new Scene(vBDicom, 400, 400);

                // sc.set
                stageDicom.setMinWidth(300);
                stageDicom.setMinHeight(300);
                stageDicom.setTitle("Zapis do pliku DICOM");
                stageDicom.setScene(scene1);
                stageDicom.setResizable(false);

                stageDicom.show();

            }
        });

        item4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage stageStat = new Stage();

                /**
                 * Identyfikator pacjenta Nazwisko pacjenta Data urodzin
                 * pacjenta Płeć pacjenta Wiek pacjenta Data badania Komentarze?
                 * Badana czesc cisla
                 */
                Label ls0 = new Label("   STATYSTYKI");
                ls0.fontProperty().set(Font.font(20));
                ls0.setAlignment(Pos.CENTER);

                Label ls1 = new Label("Min liczba emiterów");
                TextField tfs1 = new TextField();
                HBox hBs1 = new HBox(ls1, tfs1);
                hBs1.setSpacing(20);

                Label ls2 = new Label("Max liczba emiterów");
                TextField tfs2 = new TextField();
                HBox hBs2 = new HBox(ls2, tfs2);
                hBs2.setSpacing(20);

                Label ls3 = new Label("Min liczba detektorów");
                TextField tfs3 = new TextField();
                HBox hBs3 = new HBox(ls3, tfs3);
                hBs3.setSpacing(20);

                Label ls4 = new Label("Max liczba detektorów");
                TextField tfs4 = new TextField();
                HBox hBs4 = new HBox(ls4, tfs4);
                hBs4.setSpacing(20);

                Label ls5 = new Label("Min kąt");
                TextField tfs5 = new TextField();
                HBox hBs5 = new HBox(ls5, tfs5);
                hBs5.setSpacing(20);

                Label ls6 = new Label("Max kąt");
                TextField tfs6 = new TextField();
                HBox hBs6 = new HBox(ls6, tfs6);
                hBs6.setSpacing(20);

                Label ls7 = new Label("Max parametr splotu");
                TextField tfs7 = new TextField();
                HBox hBs7 = new HBox(ls7, tfs7);
                hBs7.setSpacing(20);

                CheckBox chB = new CheckBox("Parzystość przy splocie");
chB.setSelected(true);
                Button bts1 = new Button("Oblicz");
                bts1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        FileChooser fileChooser1 = new FileChooser();
                        File file1 = fileChooser1.showSaveDialog(primaryStage);

                        // file1 = fileChooser1.showOpenDialog(primaryStage);
                        if (file != null) {
                            Statistic stat = new Statistic(picture, Integer.parseInt(tfs5.getText()), Integer.parseInt(tfs6.getText()), Integer.parseInt(tfs3.getText()), Integer.parseInt(tfs4.getText()), Integer.parseInt(tfs1.getText()), Integer.parseInt(tfs2.getText()), Integer.parseInt(tfs7.getText()), chB.isSelected(), 20, file1.getAbsolutePath());
                        }
                        stageStat.close();
                    }
                });

                VBox vbStat = new VBox(hBs1, hBs2, hBs3, hBs4, hBs5, hBs6, hBs7, chB, bts1);
                vbStat.setSpacing(20);
                vbStat.setPadding(new Insets(20, 20, 20, 20));

                Scene scene1 = new Scene(vbStat, 400, 400);

                // sc.set
                stageStat.setMinWidth(300);
                stageStat.setMinHeight(300);
                stageStat.setTitle("Szczegółowe statystyki");
                stageStat.setScene(scene1);
                stageStat.setResizable(false);

                stageStat.show();

            }
        });

        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double result = Statistic.meanSquaredError(picture.getColorsOfPixels(), tomografPic.getColorsOfPixels());

                Stage stage3 = new Stage();

                Label l1 = new Label("Błąd średniokwadratowy");
                System.out.println(result);
                l1.fontProperty().set(Font.font(15));
                Label l2 = new Label(String.valueOf(result));
                l2.fontProperty().set(Font.font(15));
                HBox hBox = new HBox(l1, l2);
                hBox.setSpacing(20);
                Button button = new Button("OK");
                button.fontProperty().set(Font.font(15));
                button.setAlignment(Pos.CENTER);
                VBox vbScene2 = new VBox(hBox, button);
                vbScene2.setAlignment(Pos.CENTER);
                vbScene2.setPadding(new Insets(0, 30, 0, 20));
                vbScene2.setSpacing(50);

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stage3.close();
                    }
                });

                Scene scene2 = new Scene(vbScene2, 400, 200);

                // sc.set
                stage3.setMinWidth(400);
                stage3.setMinHeight(200);
                stage3.setTitle("Błąd średniowadratowy");
                stage3.setScene(scene2);
                stage3.setResizable(false);

                stage3.show();

            }
        });
        /**
         * Slidery
         */
        /**
         * Emitery, detektory, kąt
         */
        Label l1 = new Label("Ilość emiterów");
        l1.fontProperty().set(Font.font(15));

        TextField tf1 = new TextField("500");
        VBox vb1 = new VBox(l1, tf1);
        vb1.setSpacing(10);

        Label l2 = new Label("Ilość detektorów");
        l2.fontProperty().set(Font.font(15));

        TextField tf2 = new TextField("500");
        VBox vb2 = new VBox(l2, tf2);
        vb2.setSpacing(10);

        Label l3 = new Label("Kąt (stopnie)");
        l3.fontProperty().set(Font.font(15));

        TextField tf3 = new TextField("360");
        VBox vb3 = new VBox(l3, tf3);
        vb3.setSpacing(10);

        Label l6 = new Label("Parametr splotu");
        l6.fontProperty().set(Font.font(15));

        TextField tf6 = new TextField("16");
        VBox vb6 = new VBox(l6, tf6);
        vb6.setSpacing(10);

        Button bt1 = new Button("Zapisz");
        bt1.fontProperty().set(Font.font(15));
        VBox vb7 = new VBox(bt1);
        vb7.setPadding(new Insets(15, 0, 0, 0));

        Button bt2 = new Button("Wygeneruj sinogram");
        bt2.fontProperty().set(Font.font(15));
        VBox vb8 = new VBox(bt2);
        vb8.setPadding(new Insets(15, 0, 0, 30));

        bt2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sinogram.fullProcess(splot);
                sinogram.makeResoultPicture();
                image2 = SwingFXUtils.toFXImage(sinogram.getSinogram(), null);
                iw2 = new ImageView(image2);
                iw2.setFitHeight(400);
                iw2.setFitWidth(400);
                tile.getChildren().add(iw2);
                tomografPic = new TomographyPicture(sinogram);
            }
        });

        Button bt3 = new Button("Wygeneruj końcowy obraz");
        bt3.fontProperty().set(Font.font(15));
        VBox vb9 = new VBox(bt3);
        vb9.setPadding(new Insets(15, 0, 0, 0));

        bt3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                image3 = SwingFXUtils.toFXImage(tomografPic.makeAndReturnFullResoultPicture(), null);
                finalBufferedImage = tomografPic.getBuf();
                iw3 = new ImageView(image3);
                iw3.setFitHeight(400);
                iw3.setFitWidth(400);
                tile.getChildren().add(iw3);
            }
        });

        CheckBox chB2 = new CheckBox("Parzystość przy splocie");
        chB2.setSelected(true);
        chB2.setAlignment(Pos.CENTER);
        chB2.fontProperty().set(Font.font(15));
        VBox vb5 = new VBox(chB2);
        vb5.setPadding(new Insets(20, 0, 0, 0));

        HBox hb = new HBox(vb1, vb2, vb3, vb6, vb5, vb7, vb8, vb9);
        hb.setSpacing(30);
        hb.setPadding(new Insets(0, 30, 30, 30));

        //////// listenera do przycisku
        bt1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                emiters = Integer.parseInt(tf1.getText());
                detectors = Integer.parseInt(tf2.getText());
                angle = Integer.parseInt(tf3.getText());
                splot = Integer.parseInt(tf6.getText());

            }
        });

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
                File file = fileChooser.showOpenDialog(primaryStage);
                tile.getChildren().removeAll(iw1, iw2, iw3);
                vb.getChildren().removeAll(label1, label2, slider, slider1);

                // file = new File("D:\\Projekty\\tomograf\\Tomograf\\src\\tomograf\\obraz3.bmp");
                if (file != null) {
                    label1 = new Label("Sinogram");
                    label2 = new Label("Obraz końcowy");
                    label1.fontProperty().set(Font.font(20));
                    label2.fontProperty().set(Font.font(20));

                    slider = new Slider();
                    slider.setOrientation(Orientation.HORIZONTAL);
                    slider.setMax(emiters);
                    slider.setMin(0);
                    slider.setMajorTickUnit(emiters / 10);
                    slider.setMinorTickCount(0);
                    slider.setShowTickMarks(true);
                    slider.setShowTickLabels(true);
                    slider.setMaxSize(400, 10);
                    slider.setMinSize(10, 10);
                    slider.valueProperty().addListener((observable, oldvalue, newvalue) -> {

                        sliderSinogramValue = newvalue.intValue();
                        // sinogram.fullProcess(12);
                        sinogram.processing(sliderSinogramValue, 16);
                        sinogram.makeResoultPicture();
                        /**
                         * tile.getChildren().clear();
                         * System.out.println(tile.getChildren());
                         * pane.setCenter(null);
                         *
                         */

                        image2 = SwingFXUtils.toFXImage(sinogram.getSinogram(), null);
                        // System.out.println("slider1");
                        tile.getChildren().remove(iw2);

                        iw2 = new ImageView(image2);
                        iw2.setFitHeight(400);
                        iw2.setFitWidth(400);

                        tile.getChildren().add(iw2);

                        if (sliderSinogramValue == slider.getMax()) {

                            slider1.setVisible(true);

                            tomografPic = new TomographyPicture(sinogram);
                            //tomografPic.fullProcess();
                            //tomografPic.processing(sinogram.getEmitersAmount()+10);
                            //Image image3 = SwingFXUtils.toFXImage(tomografPic.makeAndReturnFullResoultPicture(), null);
                        } else {
                            slider1.setVisible(false);
                        }
                    });
//

                    slider1.setMin(0);

                    slider1.setMax(emiters);
                    slider1.setMajorTickUnit(emiters / 10);
                    slider1.setMinorTickCount(0);
                    slider1.setShowTickMarks(true);
                    slider1.setShowTickLabels(true);
                    slider1.showTickMarksProperty();
                    slider1.setSnapToTicks(true);
                    // slider1.setMinSize(100, 10);
                    slider1.setOrientation(Orientation.HORIZONTAL);
                    slider1.resize(100, 10);
                    slider1.setMaxSize(400, 10);
                    //domyslna wartosc
                    //slider1.adjustValue(10.0);
                    slider1.valueProperty().addListener((observable, oldvalue, newvalue) -> {

                        sliderPictureValue = newvalue.intValue();
                        tomografPic.processing(sliderPictureValue);
                        tomografPic.makeResoultPicture();

                        image3 = SwingFXUtils.toFXImage(tomografPic.getBuf(), null);
                        tile.getChildren().remove(iw3);

                        if (sliderPictureValue == slider1.getMax()) {
                            finalBufferedImage = tomografPic.getBuf();
                        }
                        iw3 = new ImageView(image3);
                        iw3.setFitHeight(400);
                        iw3.setFitWidth(400);
                        tile.getChildren().add(iw3);
                        // pane.setCenter(tile);

                    });

                    vb.setSpacing(20);
                    vb.setPadding(new Insets(20, 30, 20, 30));
                    vb.getChildren().add(label1);
                    vb.getChildren().add(slider);

                    vb.getChildren().add(label2);
                    vb.getChildren().add(slider1);

                    // tile.getChildren().removeAll(iw1, iw2, iw3);
                    //  System.out.println("srodek");
                    picture = new Picture(file);
                    Image image1 = SwingFXUtils.toFXImage(picture.getBi(), null);

                    sinogram = new Sinogram(picture, angle, detectors, emiters, chB2.isSelected());
                    //sinogram.processing(sinogram.getEmitersAmount()+10,10);
                    //sinogram.makeResoultPicture();

                    // sinogram.fullProcess(12);
                    //  Image image2 = SwingFXUtils.toFXImage(sinogram.getSinogram(), null);
                    //     TomographyPicture tomografPic = new TomographyPicture(sinogram);
                    //tomografPic.fullProcess();
                    //tomografPic.processing(sinogram.getEmitersAmount()+10);
                    //    Image image3 = SwingFXUtils.toFXImage(tomografPic.makeAndReturnFullResoultPicture(), null);
                    //Image image3 = SwingFXUtils.toFXImage(tomografPic.sploting(20), null);
//                    double bladSrednioKwadratowy = Statistic.meanSquaredError(picture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
                    //     double pierw = pow(bladSrednioKwadratowy, 0.5);
                    //     System.out.println(bladSrednioKwadratowy + " po spierwiastowaniu " + pierw);
//Statistic stat = new Statistic(picture, 180, 360, 300, 500, 400, 500, 20,true,20,"D:\\Projekty\\nowyTomograf\\file");
                    /**
                     * Obrazy
                     */
                    tile.setPadding(new Insets(3));
                    tile.setPrefColumns(1);
                    tile.setAlignment(Pos.CENTER);
                    tile.setHgap(20);

                    iw1 = new ImageView(image1);

                    iw1.setFitHeight(400);
                    iw1.setFitWidth(400);
                    tile.getChildren().add(iw1);
                    //   iw1.fitWidthProperty().bind(primaryStage.widthProperty()); 
                    //                             iw1.fitWidthProperty().bind(primaryStage.maxWidthProperty().add(400)); 

                    tile.setPrefColumns(3);
                    //     tile.setMinSize(sceneWidth, 450);

                    tile.setAlignment(Pos.CENTER);
                    scrollPane2 = new ScrollPane(tile);
                    //   scrollPane2.setMinSize(sceneWidth, 455);
                    //              scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

                    pane.setCenter(scrollPane2);

                    //  System.out.println("drugi");
                }
            }
        });
        if (file == null) {

            pane = new BorderPane(null, null, null, vb, null);

            scrollPane = new ScrollPane(pane);
            scrollPane.setFitToHeight(true);
            //    System.out.println("pierwszy");
        } else {

        }
        pane.setTop(vb4);
        pane.setBottom(vb);
        pane.setPadding(new Insets(0, 0, 35, 0));
        //   Group group = new Group(sc,pane);

        sc.setOrientation(Orientation.VERTICAL);
        sc.setMinHeight(sceneHeight);

        Scene scene = new Scene(scrollPane, sceneWidth, sceneHeight);

        // sc.set
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Tomograf");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
