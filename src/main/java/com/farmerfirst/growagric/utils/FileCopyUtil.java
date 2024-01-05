package com.farmerfirst.growagric.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopyUtil {
    public static boolean copyFileToAppDirectory(String sourceFilePath, String destinationDirPath) {
        try {
            File sourceFile = new File(sourceFilePath);
            File destinationDir = new File(destinationDirPath);

            if(!sourceFile.exists() || !sourceFile.isFile() || !destinationDir.isDirectory()){
                return false;
            }

            if(!destinationDir.exists()){
                destinationDir.mkdirs();
            }

            String destinationFilePath = destinationDirPath + File.pathSeparator + sourceFile.getName();
            //-.create channel.
            FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel destinationChannel = new FileOutputStream(destinationFilePath).getChannel();

            destinationChannel.transferFrom(sourceChannel,0,sourceChannel.size());

            sourceChannel.close();
            destinationChannel.close();

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
