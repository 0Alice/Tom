/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomograf;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.dcm4che2.tool.jpg2dcm.Jpg2Dcm;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;




/**
 *
 * @author Ania
 */
public class JpgDicom {
  //  String  patientID="123";   // Enter Patient ID
        String patientName; // Enter Pantient Name
       // String studyID="123"; // Enter Study ID
        String patientSex;
        String patientBirthDate;
        String comments;
       // String patientAge;
        String studyDate;
   Image img ;
    File jpgFileOS;
    public JpgDicom(Stage primaryStage,BufferedImage finalImage,String patientSex,String patientBirthDate,String comments,String studyDate,String patientName) throws FileNotFoundException, IOException
    {
    //  System.out.println("JPG2DCM_Convert.main()");
        jpgFileOS = new File("C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obraz.jpg");
        String end="C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obraz.dcm";
       String endFin="C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obrazF.dcm";
        
        
        Jpg2Dcm jpg2Dcm = new Jpg2Dcm();  //
         img = SwingFXUtils.toFXImage(finalImage, null);
         ImageIO.write(finalImage, "jpg", jpgFileOS);
       
       
        File dcmFileOS = new File("C:\\Users\\Ania\\Desktop\\New Folder\\Tomograf\\src\\tomograf\\obraz.dcm");

        try {
            jpg2Dcm.convert(jpgFileOS, dcmFileOS);   // Only Convert Jpg to Dicom without seting any dicom tag (without DicomHeader)
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1");
                        FileChooser fileChooser1 = new FileChooser();
                        System.out.println("2");
                        File file1 = fileChooser1.showSaveDialog(primaryStage);
                        System.out.println("3");
                        if(file1!=null){
                            System.out.println("4");
                           String str= file1.getAbsolutePath()+".dcm";
                           
      try {
           anonymizeDicom(new File(end),new File(str),patientSex,patientBirthDate,comments,studyDate,patientName);   // Set the DICOM Header
        } catch (Exception e) {
            e.printStackTrace();
        }}
    }
    
    
    
    public static final  int[] TAGS = {
        Tag.PatientID,
        Tag.StudyID, 
        Tag.PatientName,
        Tag.PatientSex,
        Tag.PatientBirthDate,
        Tag.StudyDate,
        Tag.ImageComments
    };
    public static void anonymizeDicom(File fileInput, File fileOutput,String sexForUpdate, String birthDateForUpdate,String commentsForUpdate,  String studyDateForUpdate,String patientNameForUpdate) throws FileNotFoundException, IOException 
    {
        int patientID = 1048608;
        int studyID = 2097168;
        
        int patientName=1048592;
        int comments=2113536;
        int studyDate=524320;
       // int patientAge=1052688;
        int birthDate=1048624;
        int sex=1048640;
        
        
        try 
        {
            FileInputStream fis = new FileInputStream(fileInput);
            DicomInputStream dis = new DicomInputStream(fis);
            DicomObject obj = dis.readDicomObject();
            for (int tag : TAGS) 
            {
               /* if (tag == patientID) {
                    replaceTag(obj, tag, patientIDForUpdate);
                }
                if (tag == studyID) {
                    replaceTag(obj, tag, studyIDForUpdate);
                }*/

                if (tag == patientName) {
                    replaceTag(obj, tag, patientNameForUpdate);
                }
                
                if(tag==comments){
                    replaceTag(obj, tag, commentsForUpdate);
                }
                
                if(tag==studyDate){
                    replaceTag(obj, tag, studyDateForUpdate);
                }
              //  if(tag==patientAge){
               //     replaceTag(obj, tag, patientAgeForUpdate);
               // }
                if(tag==birthDate){
                    replaceTag(obj, tag, birthDateForUpdate);
                }
                if(tag==sex){
                    replaceTag(obj, tag, sexForUpdate);
                }
                

            }
            fis.close();
            dis.close();
            writeDicomFile(obj, fileOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
   @SuppressWarnings("unused")
   private static String[] getValue(DicomObject object, int[]  PATIENT_ADDITIONAL_TAGS)
   {
       String [] value = new String [PATIENT_ADDITIONAL_TAGS.length];
       int i =0;
       while (i<PATIENT_ADDITIONAL_TAGS.length)
        {
            for (int tag : PATIENT_ADDITIONAL_TAGS) 
            {
                      value[i]=object.getString(tag);
                      i++;
            }
        }
        return value;
   }
*/
    public static void replaceTag(DicomObject dObj, int tag, String newValue) {
        if (tag != 0 && dObj.contains(tag)) {
            VR vr = dObj.vrOf(tag);
            try 
            {
                dObj.putString(tag, vr, newValue);
            } catch (Exception e) {
            //  System.err.println("Error replacing Tag: " + tag+ " with new value: " + newValue);
            }
        }
    }

    public static void writeDicomFile(DicomObject dObj, File f) {
        FileOutputStream fos;
        try 
        {
            fos = new FileOutputStream(f);
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
            return;
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DicomOutputStream dos = new DicomOutputStream(bos);
        try {
            dos.writeDicomFile(dObj);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                dos.close();
            } catch (IOException ignore) {
            }
        }
    }
    static int count1 = 0;
    
    /**
    public static void getFile(String dirPath, String destdirPath,String patientID, String studyID,String patientNameForUpdate) throws FileNotFoundException,IOException 
    {
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (files != null)
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    getFile(file.getAbsolutePath(), destdirPath, patientID,studyID,patientNameForUpdate);
                } else {
                    count1++;
                    String name = file.getAbsolutePath();
                //  test7 test = new test7();
                    anonymizeDicom(new File(name), new File(destdirPath+ "/" + file.getName()), patientID, studyID,patientNameForUpdate);
                }
            }
    }
*/

    
    
}
