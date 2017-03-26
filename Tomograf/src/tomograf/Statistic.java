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
    
    public Statistic(Picture img, int minangle,int maxangle, int mindetecotrs,int maxdetecotrs,int minemiters,int maxemiters){
        pictureWidth = img.getBi().getWidth();
        originalPicture=img;
    }
    /**
     * 
     * @param angle
     * @param detecotrs
     * @param emiters
     * @return [0/1] 0-nr iteracji , 1 - wartosci bledu sredniokwadratowego
     */
    private double[][] iterationsFunction(int angle, int detecotrs, int emiters){
        double [][] resoult = new double[2][100];
        int step = emiters/100;
        for(int i=0;i<100;i++){
            
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
