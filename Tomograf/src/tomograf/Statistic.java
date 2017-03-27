/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import java.awt.Color;
import static java.lang.Math.pow;

/**
 *
 * @author geral_000
 */
public class Statistic {

    /*
     *
     * @param img Obrazek wejsciowy
     * @param angle kąt rozwarcia stożka
     * @param detecotrs liczba detektorów
     * @param emiters liczba emiterów
     *//*
    public Sinogram(Picture img, int angle, int detecotrs, int emiters) {*/
    private final int pictureWidth;
    private final Picture originalPicture;
    private final int avgAngle;
    private final int avgDetectors;
    private final int avgEmiters;
    private final int maxK;
    /**
     * błąd średniokwadratowy w funkcji iteracji kolejnych wierszy z sinogramu
     */
    private double[][] meanSquaredErrorOnIterations;
    /**
     * błąd średniokwadratowy w funkcji konta rozwarcia stożka
     */
    private double[][] meanSquaredErrorOnAngle;

    /**
     * błąd średniokwadratowy w funkcji liczby detektorów
     */
    private double[][] meanSquaredErrorOnDetectors;
    /**
     * błąd średniokwadratowy w funkcji liczby emiterów
     */
    private double[][] meanSquaredErrorOnEmiters;
    /**
     * błąd średniokwadratowy w zalenosci od zastosowania Splotu i parametru k
     */
    private double[][] meanSquaredErrorOnSplot;
    private final boolean even;
    public Statistic(Picture img, int minangle, int maxangle, int mindetectors, int maxdetectors, int minemiters, int maxemiters, int k,boolean even,int iterations,String filePath) {
        pictureWidth = img.getBi().getWidth();
        originalPicture = img;
        avgAngle = (minangle + maxangle) / 2;
        avgDetectors = (mindetectors + maxdetectors) / 2;
        avgEmiters = (minemiters + maxemiters) / 2;
        maxK = k;
        this.even=even;
        ExportToXLSX file =new ExportToXLSX(filePath);
        
        //int iterations = 20;
        meanSquaredErrorOnIterations = iterationsFunction(iterations);
        file.createSheet(meanSquaredErrorOnIterations,iterations, "Iteracje", "Iteracje", 0);
        
        /*System.out.println("Funkcja iteracji");
        for (int i = 0; i < iterations; i++) {
            System.out.println("f(" + meanSquaredErrorOnIterations[0][i] + ")=" + meanSquaredErrorOnIterations[1][i]);
        }*/
        
        meanSquaredErrorOnAngle = anglesFunction(iterations, minangle, maxangle);
        file.createSheet(meanSquaredErrorOnAngle,iterations, "Kąt", "Kąt", 1);
        /*System.out.println("Funkcja kata rozwarcia");
        for (int i = 0; i < angles; i++) {
            System.out.println("f(" + meanSquaredErrorOnAngle[0][i] + ")=" + meanSquaredErrorOnAngle[1][i]);
        }*/
        meanSquaredErrorOnDetectors = detectorsFunction(iterations, mindetectors, maxdetectors);
        file.createSheet(meanSquaredErrorOnDetectors,iterations, "Detektory", "Detekt", 2);
        /*System.out.println("Funkcja zaleznosci od liczby detektorów");
        for (int i = 0; i < detectors; i++) {
            System.out.println("f(" + meanSquaredErrorOnDetectors[0][i] + ")=" + meanSquaredErrorOnDetectors[1][i]);
        }*/
        meanSquaredErrorOnEmiters = emitersFunction(iterations, minemiters, maxemiters);
        file.createSheet(meanSquaredErrorOnEmiters,iterations, "Emitery", "Emiter", 3);
        /*System.out.println("Funkcja zaleznosci od liczby emiterów");
        for (int i = 0; i < emiters; i++) {
            System.out.println("f(" + meanSquaredErrorOnEmiters[0][i] + ")=" + meanSquaredErrorOnEmiters[1][i]);
        }*/
        meanSquaredErrorOnSplot = splotedFunction();
        System.out.println("Funkcja zaleznosci od splotu");
        file.createSheet(meanSquaredErrorOnSplot,maxK+1, "Splot", "Splot", 4);
        /*for (int i = 0; i <= maxK; i++) {
            System.out.println("f(" + meanSquaredErrorOnSplot[0][i] + ")=" + meanSquaredErrorOnSplot[1][i]);
        }
        */
        file.saveFile();
    }

    /**
     *
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] iterationsFunction(int n) {
        Sinogram sinogram = new Sinogram(originalPicture, avgAngle, avgDetectors, avgEmiters,even);
        sinogram.fullProcess(maxK);
        TomographyPicture tomografPic = new TomographyPicture(sinogram);
        double[][] resoult = new double[2][n];
        int step = avgEmiters / (n - 1);
        for (int i = 0; i < n; i++) {
            tomografPic.processing(i * step);
            resoult[0][i] = i * step;
            resoult[1][i] = meanSquaredError(originalPicture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
        }
        return resoult;
    }

    /**
     *
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] anglesFunction(int n, int minAngle, int maxAngle) {
        double[][] resoult = new double[2][n];
        int step = (maxAngle - minAngle) / (n - 1);
        for (int i = 0; i < n; i++) {
            Sinogram sinogram = new Sinogram(originalPicture, minAngle + step * i, avgDetectors, avgEmiters,even);
            sinogram.fullProcess(maxK);
            TomographyPicture tomografPic = new TomographyPicture(sinogram);
            tomografPic.fullProcess();
            resoult[0][i] = minAngle + step * i;
            resoult[1][i] = meanSquaredError(originalPicture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
        }
        return resoult;
    }

    /**
     *
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] detectorsFunction(int n, int minDetectors, int maxDetectors) {
        double[][] resoult = new double[2][n];
        int step = (maxDetectors - minDetectors) / (n - 1);
        for (int i = 0; i < n; i++) {
            Sinogram sinogram = new Sinogram(originalPicture, avgAngle, minDetectors + step * i, avgEmiters,even);
            sinogram.fullProcess(maxK);
            TomographyPicture tomografPic = new TomographyPicture(sinogram);
            tomografPic.fullProcess();
            resoult[0][i] = minDetectors + step * i;
            resoult[1][i] = meanSquaredError(originalPicture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
        }
        return resoult;
    }

    /**
     *
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] emitersFunction(int n, int minEmiters, int maxEmiters) {
        double[][] resoult = new double[2][n];
        int step = (maxEmiters - minEmiters) / (n - 1);
        for (int i = 0; i < n; i++) {
            Sinogram sinogram = new Sinogram(originalPicture, avgAngle, avgDetectors, minEmiters + step * i,even);
            sinogram.fullProcess(maxK);
            TomographyPicture tomografPic = new TomographyPicture(sinogram);
            tomografPic.fullProcess();
            resoult[0][i] = minEmiters + step * i;
            resoult[1][i] = meanSquaredError(originalPicture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
        }
        return resoult;
    }

    /**
     *
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] splotedFunction() {
        double[][] resoult = new double[2][maxK + 1];
        for (int i = 0; i <= maxK; i++) {
            Sinogram sinogram = new Sinogram(originalPicture, avgAngle, avgDetectors, avgEmiters,even);
            sinogram.fullProcess(i);
            TomographyPicture tomografPic = new TomographyPicture(sinogram);
            tomografPic.fullProcess();
            resoult[0][i] = i;
            resoult[1][i] = meanSquaredError(originalPicture.getColorsOfPixels(), tomografPic.getColorsOfPixels());
        }
        return resoult;
    }

    /**
     *
     * @param original pierwotny obrazek
     * @param resultant obrazek wynikowy, który chcemy porónać
     * @return błąd średniokwadratowy
     */
    public static double meanSquaredError(Color[][] original, Color[][] resultant) {
        double sum = 0;
        int licznik = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                sum += pow((original[i][j].getRed() - resultant[i][j].getRed()), 2);
                licznik++;
            }
        }
        return sum / licznik;
    }

}
