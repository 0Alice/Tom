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
    private final int avgDetecors;
    private final int avgEmiters;
    private final int maxK;
    /**
     * błąd średniokwadratowy w funkcji iteracji kolejnych wierszy z sinogramu
     */
    private final double[][] meanSquaredErrorOnIterations;
   /**
     * błąd średniokwadratowy w funkcji konta rozwarcia stożka
     */
    //private final double[][] meanSquaredErrorOnAngle;
    
    /**
     * błąd średniokwadratowy w funkcji liczby detektorów
     */
    //private final double[][] meanSquaredErrorOnDetectors;
    /**
     * błąd średniokwadratowy w funkcji liczby emiterów
     */
    //private final double[][] meanSquaredErrorOnEmiters;
    /**
     * błąd średniokwadratowy w zalenosci od zastosowania Splotu i parametru k
     */
    //private final double[][] meanSquaredErrorOnSplot;
    
    public Statistic(Picture img, int minangle,int maxangle, int mindetecotrs,int maxdetecotrs,int minemiters,int maxemiters,int k){
        pictureWidth = img.getBi().getWidth();
        originalPicture=img;
        avgAngle=(minangle+maxangle)/2;
        avgDetecors=(mindetecotrs+maxdetecotrs)/2;
        avgEmiters=(minemiters+maxemiters)/2;
        maxK=k;
        meanSquaredErrorOnIterations=iterationsFunction();
        /*for(int i=0;i<100;i++){
            System.out.println("f("+meanSquaredErrorOnIterations[0][i]+")="+meanSquaredErrorOnIterations[1][i]);
        }*/
        //meanSquaredErrorOnAngle
        
    }
    /**
     * 
     * @param angle
     * @param detecotrs
     * @param emiters
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] iterationsFunction(){
        Sinogram sinogram = new Sinogram(originalPicture, avgAngle,avgDetecors, avgEmiters);
        sinogram.fullProcess(maxK);
        TomographyPicture tomografPic = new TomographyPicture(sinogram);
        double [][] resoult = new double[2][100];
        int step = avgEmiters/100;
        for(int i=0;i<100;i++){
            tomografPic.processing(i*step);
            resoult[0][i]=i*step;
            resoult[1][i]=meanSquaredError(originalPicture.getColorsOfPixels(),tomografPic.getColorsOfPixels());
        }
        return resoult;
    }
    
    /**
     * 
     * @param original pierwotny obrazek
     * @param resultant obrazek wynikowy, który chcemy porónać
     * @return błąd średniokwadratowy
     */
    public static double meanSquaredError(Color[][] original,Color[][] resultant){
        double sum=0;
        int licznik=0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
           sum+=pow((original[i][j].getRed()-resultant[i][j].getRed()),2); 
           licznik++;
            }}
    return sum/licznik;    
    }
    
}
