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

    private final Color[][] pixelsFromSinogram;//kolory pikseli znormalizowanego sinogramu
    private int[][][] endPix;//tablica sum kolorow skladajasych sie na piksel i ilosci tych skladowych
    private final int[][] pixColor;//kolor piksela w wynikowym obrazie przed normalizacja
    private Color[][] normalColor;//kolor piksela w wynikowym obrazie po normalizacji
    private BufferedImage buf;//wyniowy obraz
    private final int pictureWidth;

    private final int emitersAmount;//liczba emiterow
    private final int detectorsAmount;//liczba detektorow
    private final int angle;//kat rozwarcia stozka
    private final int radious;//promien okreku wpisanego w obraz
    private int processed;//liczba wykonanych iteracji

    //private Color[][] normalSplotedColor;//kolor piksela w przefiltrowanym obrazie po normalizacji
    //private BufferedImage splotedBuf;//wyniowy obraz przefiltrowany
    public TomographyPicture(Sinogram sinogram) {
        this.angle = sinogram.getAngle();
        emitersAmount = sinogram.getEmitersAmount();
        detectorsAmount = sinogram.getDetectorsAmount();
        pictureWidth = sinogram.getPictureWidth();
        buf = new BufferedImage(pictureWidth, pictureWidth, BufferedImage.TYPE_BYTE_GRAY);
        radious = sinogram.getRadious();
        pixelsFromSinogram = sinogram.getNormalizedPix();
        normalColor = new Color[pictureWidth][pictureWidth];
        pixColor = new int[pictureWidth][pictureWidth];
        //wysokosc na szerokosc na 2 (suma i licznik pikseli - bedziemy robili pozniej srednia)
        endPix = new int[pictureWidth][pictureWidth][2];
        processed = 0;
    }

    public BufferedImage sploting(int k) {
        //Color[][] normalSplotedColor= new Color[pictureWidth][pictureWidth];
        int[][] splotingPix = new int[pictureWidth][pictureWidth];
        for (int a = 0; a < pictureWidth; a++) {
            for (int b = 0; b < pictureWidth; b++) {
                for (int i = 0; i < k; i++) {
                    for (int j = 0; j < k; j++) {
                        if (i == 0 && j == 0) {
                            splotingPix[a][b] = pixColor[a][b];
                        } else {
                            double factor = 4 / ((Math.PI * Math.PI) * ((i * i) + (j * j)));
                            if (a + i < pictureWidth && b + j < pictureWidth) {
                               // System.out.println(splotingPix[a][b]+"+"+pixColor[a + i][b + j]+"*"+factor);
                                splotingPix[a][b] += pixColor[a + i][b + j] * factor;
                                //System.out.println(splotingPix[a][b]);
                            }
                            if (a + i < pictureWidth && b - j >= 0) {
                                splotingPix[a][b] += pixColor[a + i][b - j] * factor;
                            }
                            if (a - i >= 0 && b + j < pictureWidth) {
                                splotingPix[a][b] += pixColor[a - i][b + j] * factor;
                            }
                            if (a - i >= 0 && b - j >= 0) {
                                splotingPix[a][b] += pixColor[a - i][b - j] * factor;
                            }
                        }
                    }

                }
            }
        }
        Color[][] normalSplotedColor = normalize(splotingPix);
        BufferedImage splotingBuf = new BufferedImage(pictureWidth, pictureWidth, BufferedImage.TYPE_BYTE_GRAY);
        for (int a = 0; a < pictureWidth; a++) {
            for (int b = 0; b < pictureWidth; b++) {
                Color col = normalSplotedColor[a][b];
                splotingBuf.setRGB(a, b, col.getRGB());
            }
        }
        return splotingBuf;
    }

    public void fullProcess() {
        this.processing(emitersAmount);
    }

    public void processing(int iterations) {
        if(processed+iterations>=emitersAmount){
            iterations=emitersAmount-processed;
        }
        for (int i = processed; i < processed + iterations; i++) {
            double help0 = i * Math.PI / 180;
            Double EmiterX = Math.cos(help0) * radious + radious;
            Double EmiterY = Math.sin(help0) * (-radious) + radious;
            for (int j = 0; j < detectorsAmount; j++) {
                double help = help0 + Math.PI - (angle * Math.PI) / 360 + (angle * Math.PI * j) / (180 * (detectorsAmount - 1));
                Double DetektorX = Math.cos(help) * radious + radious;
                Double DetektorY = Math.sin(help) * (-radious) + radious;
                BresenhamLine(EmiterX.intValue(), EmiterY.intValue(), DetektorX.intValue(), DetektorY.intValue(), pixelsFromSinogram[i][j]);
            }
        }
        processed += iterations;

        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                pixColor[i][j] = 0;
                if (endPix[i][j][1] != 0) {
                    pixColor[i][j] = endPix[i][j][0] / endPix[i][j][1];
                }
            }

        }
        
        normalColor = normalize(pixColor);
        //equalization();
        
        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                Color col = normalColor[i][j];
                buf.setRGB(i, j, col.getRGB());
            }
        }
    }

    private Color[][] normalize(int[][] innerColor) {
        int max = 0;
        int min = 255;
        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                int pixelColor = innerColor[i][j];
                if (pixelColor > max) {
                    max = pixelColor;
                }
                if (pixelColor < min&&pixelColor!=0){
                    min = pixelColor;
                }
            }
        }
        Color[][] resultColor = new Color[pictureWidth][pictureWidth];
        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                int kol = (int) ((innerColor[i][j] - min) * (255.0 / (max - min)));
                if(kol<0)kol=0;
                resultColor[i][j] = new Color(kol, kol, kol);
            }
        }
        return resultColor;

    }

    private void equalization() {
        int n = pictureWidth * pictureWidth;
        double[] gray = new double[256];
        double[] dGray = new double[256];
        int[] lut = new int[256];
        double sumGray = 0;
        for (int i = 0; i < 256; i++) {
            gray[i] = 0;
            dGray[i] = 0;
        }
        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                try{
                gray[normalColor[i][j].getRed()]++;}catch(java.lang.NullPointerException e){
                    normalColor[i][j]=new Color(0,0,0);
                }
            }
        }
        for (int i = 0; i < 256; i++) {

            sumGray += (gray[i] / n);

            dGray[i] += sumGray;
        }
        //mamy juz dystrybuante dGray
        double D0min;
//znajdz pierwszą niezerową wartosc dystrybuanty
        int z = 0;
        //System.out.println(dGray[z]);
        while (dGray[z] == 0) {
            //  System.out.println(dGray[z]);
            z++;
        }
        //System.out.println(z);
        D0min = dGray[z];
        for (int i = 0; i < 256; i++) {
            lut[i] = (int) (((dGray[i] - D0min) / (1 - D0min)) * (256 - 1));
            //  System.out.println(i+" "+lut[i]);
        }
        for (int i = 0; i < pictureWidth; i++) {
            for (int j = 0; j < pictureWidth; j++) {
                int kol = lut[normalColor[i][j].getRed()];
                normalColor[i][j] = new Color(kol, kol, kol);
            }
        }

    }

    // x1 , y1 - współrzędne początku odcinka
    // x2 , y2 - współrzędne końca odcinka
    private void BresenhamLine(int x1, int y1, int x2, int y2, Color add) {
        // zmienne pomocnicze
        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;
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

        endPix[x][y][0] += add.getRed();
        endPix[x][y][1]++;

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
                endPix[x][y][0] += add.getRed();
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
                endPix[x][y][0] += add.getRed();
                endPix[x][y][1]++;
            }
        }
    }

    public BufferedImage getBuf() {
        return buf;
    }

    public void setBuf(BufferedImage buf) {
        this.buf = buf;
    }
}
