package net.group47.Quackstagram.util;


import java.io.File;

public class Util {


    public Util(){
    }

    public String getFileExtension(File file){
        String fileName = file.toString();
        int index = fileName.lastIndexOf('.');

        return fileName.substring(index + 1).toLowerCase();
    }

    public boolean isPhoto(File file){
        String extension = getFileExtension(file);
        return extension.equals("png");
    }
}
