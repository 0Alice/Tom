/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

/**
 *
 * @author geral_000
 */
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.*;
import jxl.write.Number;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class ExportToXLSX {

    private WritableWorkbook workbook;

    ExportToXLSX(String sciezka){
        try {
            System.out.println(sciezka + ".xls");
            workbook = Workbook.createWorkbook(new File(sciezka + ".xls"));
            WorkbookSettings setings = new WorkbookSettings();
            setings.setLocale(new Locale("pl", "PL"));
        } catch (IOException ex) {
            Logger.getLogger(ExportToXLSX.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie udało się utworzyć pliku");
        }
    }

    public void createSheet(double[][] data, String title, String sheetName, int sheetNumber) {
        try {
            WritableSheet arkusz = workbook.createSheet(sheetName, sheetNumber);
            Label label = new Label(0, 0, title);
            arkusz.setColumnView(0, 20);
            arkusz.addCell(label);
            label = new Label(1, 0, "Błąd średniokwadratowy");
            arkusz.setColumnView(0, 20);
            arkusz.addCell(label);
            
            WritableCellFormat format = new WritableCellFormat(NumberFormats.FLOAT);
            Number number;
            
            for (int i = 0; i < data.length; i++) {
                number = new Number(0, i, data[i][0], format);
                arkusz.addCell(number);
                number = new Number(1, i, data[i][1], format);
                arkusz.addCell(number);
            }
        } catch (WriteException ex) {
            Logger.getLogger(ExportToXLSX.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie udało się utworzyć arkusza");
        }
    }
    public void saveFile(){     
        try {
            workbook.write();
            workbook.close();
            System.out.println("Dokument został wygenerowany.");
        } catch (IOException | WriteException ex) {
            Logger.getLogger(ExportToXLSX.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie udało się zapisać pliku");
        }
    }
}
