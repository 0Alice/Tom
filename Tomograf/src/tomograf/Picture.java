/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import java.io.File;
import javafx.scene.image.Image;

/**
 *
 * @author Ania
 */
/*
public class Picture {

    File f = new File("file:C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obraz.bmp");
    Image img = new Image(f.toURI().toString());

    public Image getImg() {
        return img;
    }

    public Picture() {}

  

}
*/


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.PixelGrabber;

/**
 *
 * @author Ania
 */
public class Picture {

    BufferedImage bi;

    public Picture(File file) {
        readIn(getClass().getResourceAsStream(file.getName()));
        //tu trzeba zmienic na choosera
    }

    /**
     * Wczytywanie obrazu
     *
     * @param path stream pliku z obrazem tomografii
     */
    public void readIn(InputStream path) {
        try {
            bi = ImageIO.read(path);
        } catch (IOException ex) {
            Logger.getLogger(Tomograf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

}
