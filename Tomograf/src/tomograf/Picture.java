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
public class Picture {

    File f = new File("file:C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obraz.bmp");
    Image img = new Image(f.toURI().toString());

    public Image getImg() {
        return img;
    }

    public Picture() {}

  

}
