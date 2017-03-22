/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Ania
 */
public class TomographyPicture {

    private Color[][] pix;
    private int[][][] endPix;
    private int[][] normalColor;
    private BufferedImage buf;
    private int pictureWidth;

 
    private BufferedImage sinogram;
    private int emitersAmount;
    private int detectorsAmount;
    private int angle;

    /**
     *
     * @param img Obrazek
     * @param angle rozwarość stożka
     * @param n liczba detektorow
     */
    public TomographyPicture(Picture img, Sinogram sinogram) {
        this.angle = sinogram.getAngle();
        emitersAmount = sinogram.getEmitersAmount();
        detectorsAmount = sinogram.getDetectorsAmount();
        buf = new BufferedImage(img.getBi().getWidth(), img.getBi().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        pictureWidth=img.getBi().getWidth();

        int r = (img.getBi().getWidth() / 2) - 5;
        BufferedImage bi = img.getBi();
     //   pix = new Color[emitersAmount][detectorsAmount];
        //wysokosc na szerokosc na 2 (suma i licznik pikseli - bedziemy robili pozniej srednia)
        endPix = new int[img.getBi().getWidth()][img.getBi().getWidth()][2];

        
        
        
        
        
        
        //to trzeba dostosowac
        for (int i = 0; i < emitersAmount; i++) {
            double help0 = i * Math.PI / 180;
            Double EmiterX = Math.cos(help0) * r + r;
            Double EmiterY = Math.sin(help0) * (-r) + r;
            for (int j = 0; j < detectorsAmount; j++) {
                double help = help0 + Math.PI - (angle * Math.PI) / 360 + (angle * Math.PI * j) / (180 * (detectorsAmount - 1));
                Double DetektorX = Math.cos(help) * r + r;
                Double DetektorY = Math.sin(help) * (-r) + r;
                //pix[i][j] 
                //    System.out.println("r " + r + "   help  " + help);
                // System.out.println("ex " + EmiterX.intValue() + " ey " + EmiterY.intValue() + " dx " + DetektorX.intValue() + " dy " + DetektorY.intValue());

                BresenhamLine(EmiterX.intValue(), EmiterY.intValue(), DetektorX.intValue(), DetektorY.intValue(),sinogram.getPix()[i][j]);

                // System.out.println("x  " + i + "   j   " + j + "  rgb   " + pix[i][j]);
                //  PixelGrabber pixelGrab = new PixelGrabber(bi, EmiterX.intValue(), EmiterY.intValue(), 1, 1, pix, off, 1);
                // setRGB(pix[i][j].getRed(),pix[i][j].getGreen(),pix[i][j].getBlue());
                }
        }
        normalColor=new int[pictureWidth][pictureWidth];
        for(int i=0;i<pictureWidth;i++){
       for(int j=0;j<pictureWidth;j++){
           normalColor[i][j]=0;
           if(endPix[i][j][1]!=0){
               normalColor[i][j]=endPix[i][j][0]/endPix[i][j][1];
           }
       }
       
    }
        normalize();
        for(int i=0;i<img.getBi().getWidth();i++){
       for(int j=0;j<img.getBi().getWidth();j++){
         Color col=new Color(normalColor[i][j],normalColor[i][j],normalColor[i][j]);
            buf.setRGB(i, j, col.getRGB());  
       }
    }
        /*
        for(int i=0;i<img.getBi().getWidth();i++){
       for(int j=0;j<img.getBi().getWidth();j++){
           int colh=0;
           if(endPix[i][j][1]!=0){
               colh=endPix[i][j][0]/endPix[i][j][1];
           }
         Color col=new Color(colh,colh,colh);
            buf.setRGB(i, j, col.getRGB());  
       }
    }
        */
    }
    private void normalize(){
        int max=0;
        for(int []tab:normalColor){
            for(int e:tab){
                if(e>max){
                    max=e;
                }
            }
        }
         for(int i=0;i<pictureWidth;i++){
       for(int j=0;j<pictureWidth;j++){
           normalColor[i][j]=normalColor[i][j]*(255/max);
       }
        
    }
    }

      // x1 , y1 - współrzędne początku odcinka
    // x2 , y2 - współrzędne końca odcinka
   private void BresenhamLine(int x1, int y1, int x2, int y2, Color add) {
        // zmienne pomocnicze
        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;
        int licznik = 0;
        int suma = 0;
        // ustalenie kierunku rysowania
        if (x1 < x2) {
            xi = 1;
            dx = x2 - x1;
        } else {
            xi = -1;
            dx = x1 - x2;
        }
        // ustalenie kierunku rysowania
        if (y1 < y2) {
            yi = 1;
            dy = y2 - y1;
        } else {
            yi = -1;
            dy = y1 - y2;
        }

        //int pixels = image.getRGB(x, y);
        //int red = (pixels & 0x00ff0000) >> 16;
        //suma += red;
        
        endPix[x][y][0]+=add.getRed();
         endPix[x][y][1]++;
        // System.out.println(suma);

        licznik++;

        //image.getRGB(x, y);
        // oś wiodąca OX
        if (dx > dy) {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            // pętla po kolejnych x
            while (x != x2) {
                // test współczynnika
                if (d >= 0) {
                    x += xi;
                    y += yi;
                    d += ai;
                } else {
                    d += bi;
                    x += xi;
                }

/*
                pixels = image.getRGB(x, y);
                red = (pixels & 0x00ff0000) >> 16;
                //  System.out.println(red);
                suma += red;
                //  System.out.println(suma);

                licznik++;
                */
                endPix[x][y][0]+=add.getRed();
         endPix[x][y][1]++;
            }
        } // oś wiodąca OY
        else {
            ai = (dx - dy) * 2;
            bi = dx * 2;
            d = bi - dy;
            // pętla po kolejnych y
            while (y != y2) {
                // test współczynnika
                if (d >= 0) {
                    x += xi;
                    y += yi;
                    d += ai;
                } else {
                    d += bi;
                    y += yi;
                }
/*
                pixels = image.getRGB(x, y);

                red = (pixels & 0x00ff0000) >> 16;
                //  System.out.println("red" + red);
                suma += red;

                //  System.out.println(suma);
                licznik++;*/
                endPix[x][y][0]+=add.getRed();
         endPix[x][y][1]++;
            }
        }
        //  System.out.println("koniec");
        // System.out.println(suma);
        // System.out.println(licznik);
        //  System.out.println(kol);
    }

      public BufferedImage getBuf() {
        return buf;
    }

    public void setBuf(BufferedImage buf) {
        this.buf = buf;
    }
}
