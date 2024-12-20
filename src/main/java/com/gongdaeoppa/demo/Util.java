package com.gongdaeoppa.demo;

import java.io.File;

public class Util {
    
    
    public static boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        
        return folder.delete();
    }
    
    
}
