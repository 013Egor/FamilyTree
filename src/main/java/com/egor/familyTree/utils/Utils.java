package com.egor.familyTree.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.Person;



public class Utils {

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static void movePhotos(Iterable<Person> persons, String filename) {
        for (Person person : persons) {
            if (person.getPhoto().equals("") == false) {
                File file = new File(person.getPhoto());
                
                savePhoto(file, filename);
            }
        }
    }

    public static void savePhoto(File photo, String filename) {
        if (photo.exists() == false) {
            return;
        }
        File dir = new File(Constants.PHOTO_DIR + filename);
        
        dir.mkdirs();

        FileOutputStream writer = null;
        FileInputStream reader = null;

        try {
            writer = new FileOutputStream(dir.getCanonicalPath() + "/" + photo.getName());
            reader = new FileInputStream(photo);

            int part = -1;
            while ((part = reader.read()) != -1) {
                writer.write(part);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Не получилось сохранить фото");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) { }
        }
        
        
        
    }
}
