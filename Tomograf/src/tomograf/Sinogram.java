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
public class Sinogram {

    /**
     * kolor piksela w sinogramie przed normalizacja
     */
    private int[][] pix;
    /**
     * kolor piksela w sionogramie po normalizacji
     */
    private Color[][] normalizedPix;
    /**
     * wynikowy obraz (sionogram)
     */
    private BufferedImage sinogram;
    /**
     * wejsciowy obraz (musi byc w kwadracie)
     */
    private final BufferedImage originalPicture;
    /**
     * liczba wykonanych iteracji
     */
    private int processed;
    /**
     * liczba emiterow
     */
    private final int emitersAmount;
    /**
     * liczba detektorow
     */
    private final int detectorsAmount;
    /**
     * kat rozwarcia stozka
     */
    private final int angle;
    /**
     * promien okreku wpisanego w obraz
     */
    private final int radious;
    /**
     * szerokosc wejsciowego obrazu
     */
    private final int pictureWidth;

    /**
     *
     * @param img Obrazek wejsciowy
     * @param angle kąt rozwarcia stożka
     * @param detecotrs liczba detektorów
     * @param emiters liczba emiterów
     */
    public Sinogram(Picture img, int angle, int detecotrs, int emiters) {
        this.angle = angle;
        emitersAmount = emiters;
        detectorsAmount = detecotrs;
        sinogram = new BufferedImage(emitersAmount, detectorsAmount, BufferedImage.TYPE_BYTE_GRAY);
        pictureWidth = img.getBi().getWidth();
        radious = (pictureWidth / 2) - 5;
        originalPicture = img.getBi();
        pix = new int[emitersAmount][detectorsAmount];
        normalizedPix = new Color[emitersAmount][detectorsAmount];
        processed = 0;

    }

    /**
     *
     * @param kForSplot jest parametrem k we wzorze na splot
     */
    public void fullProcess(int kForSplot) {
        this.processing(emitersAmount, kForSplot);
        makeResoultPicture();
    }

    /**
     *
     * @param iterations liczba iteracji do których ma się wykonać algorytm
     * @param kForSplot jest parametrem k we wzorze na splot
     */
    public void processing(int iterations, int kForSplot) {
        if (iterations >= emitersAmount) {
            iterations = emitersAmount;
        }
        if(iterations<processed){
            iterations =processed;
        }
        for (int i = processed; i <iterations; i++) {
            double help0 = i * Math.PI / 180;
            Double EmiterX = Math.cos(help0) * radious + radious;
            Double EmiterY = Math.sin(help0) * (-radious) + radious;
            for (int j = 0; j < detectorsAmount; j++) {
                double help = help0 + Math.PI - (angle * Math.PI) / 360 + (angle * Math.PI * j) / (180 * (detectorsAmount - 1));
                Double DetektorX = Math.cos(help) * radious + radious;
                Double DetektorY = Math.sin(help) * (-radious) + radious;
                pix[i][j] = BresenhamLine(EmiterX.intValue(), EmiterY.intValue(), DetektorX.intValue(), DetektorY.intValue(), originalPicture);
            }
            sploting(i, kForSplot);
        }
        normalize(iterations);
        /*
        for (int i = 0; i < processed + iterations; i++) {
            for (int j = 0; j < detectorsAmount; j++) {
                sinogram.setRGB(i, j, normalizedPix[i][j].getRGB());
            }
        }*/
        processed = iterations;
    }
    public void makeResoultPicture(){
        for (int i = 0; i < processed; i++) {
            for (int j = 0; j < detectorsAmount; j++) {
                sinogram.setRGB(i, j, normalizedPix[i][j].getRGB());
            }
        }
    }

    private void sploting(int row, int k) {
        for (int i = 0; i < detectorsAmount; i++) {
            int newColor = pix[row][i];
            for (int j = 1; j <= k; j++) {
                if(j%2==1){
                double factor = -4 / ((Math.PI * Math.PI) * (j * j));
                if (i - j >= 0) {
                    newColor += pix[row][i - j] * factor;
                }
                if (i + j < detectorsAmount) {
                    newColor += pix[row][i + j] * factor;
                }}
            }
            pix[row][i] = newColor;
        }
    }

    private void normalize(int iterations) {
        int max = 0;
        int min = 255;
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < detectorsAmount; j++) {
                int pixelColor = 0;
                try {
                    pixelColor = pix[i][j];
                } catch (java.lang.NullPointerException z) {
                    pixelColor = 0;
                    pix[i][j] = 0;
                }
                if (pixelColor > max) {
                    max = pixelColor;
                }
                if (pixelColor < min) {
                    min = pixelColor;
                }
            }
        }
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < detectorsAmount; j++) {
                int kol = (int) ((pix[i][j] - min) * (255.0 / (max - min)));
                normalizedPix[i][j] = new Color(kol, kol, kol);
            }

        }
    }

    // x1 , y1 - współrzędne początku odcinka
    // x2 , y2 - współrzędne końca odcinka
    private int BresenhamLine(int x1, int y1, int x2, int y2, BufferedImage image) {
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

        int pixels = image.getRGB(x, y);
        int red = (pixels & 0x00ff0000) >> 16;
        suma += red;
        licznik++;

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

                pixels = image.getRGB(x, y);
                red = (pixels & 0x00ff0000) >> 16;
                suma += red;
                licznik++;

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

                pixels = image.getRGB(x, y);
                red = (pixels & 0x00ff0000) >> 16;
                suma += red;
                licznik++;

            }
        }
        int kol = suma / licznik;
        return kol;
    }

    public int getEmitersAmount() {
        return emitersAmount;
    }

    public int getDetectorsAmount() {
        return detectorsAmount;
    }

    public BufferedImage getSinogram() {
        return sinogram;
    }

    public int getAngle() {
        return angle;
    }

    public Color[][] getNormalizedPix() {
        return normalizedPix;
    }

    public int getRadious() {
        return radious;
    }

    public int getPictureWidth() {
        return pictureWidth;
    }

}
