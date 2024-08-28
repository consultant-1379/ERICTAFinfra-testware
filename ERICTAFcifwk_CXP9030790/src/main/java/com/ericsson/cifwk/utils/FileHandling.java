package com.ericsson.cifwk.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.cifwk.taf.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandling{

    /**
     * The copyFindReplaceInFile method takes a Template File which contains placeholder strings and replaces these placeholders
     * with values cantained in the Map, this inturn output a temp file which can be consumed by a user
     * @param fileName
     * @param tempFileName
     * @param details
     * @return
     */
    public String copyFindReplaceInFile(File fileName, String tempFileName, String type, Map<String,String> details){

        Path tempFilePath = null;
        File tempFile = null;
        try {
            tempFile = File.createTempFile(tempFileName, type);
            tempFilePath = Paths.get(tempFile.getAbsolutePath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Charset charset = StandardCharsets.UTF_8;
        String content;

        try{
            copyContentsOfFile(fileName, tempFile);
            content = new String(Files.readAllBytes(tempFilePath), charset);
            Iterator<Entry<String, String>> iterator = details.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> pairs = (Map.Entry<String, String>)iterator.next();
                content = content.replaceAll(pairs.getKey().toString(), pairs.getValue().toString());
                iterator.remove();
                Files.write(tempFilePath, content.getBytes(charset));
            }
        }catch (Exception error){
            error.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }

    /**
     * The deleteFile method deleted a given file from the local file system
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName){
        Path filePath = Paths.get(fileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    /**
     * The getFileToDeploy method uses the TAF FileFinder which returns a File object based on an input string FileName
     * @param fileName
     * @return
     */
    public static File getFileToDeploy(String fileName) {
        
        List<String> fileNames = FileFinder.findFile(fileName);
        if (fileNames.size() == 0)
            fileNames = FileFinder.findFile(fileName, new File(FileUtils.getCurrentDir()).getParentFile().getAbsolutePath());
        return new File(fileNames.get(0));
    }
    
    /**
     * @param fin
     * @param dest
     */
    public void copyContentsOfFile(File fin, File dest) {
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fin);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
        FileWriter fstream = null;
        try {
            fstream = new FileWriter(dest.getAbsolutePath(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter out = new BufferedWriter(fstream);
 
        String aLine = null;
        try {
            while ((aLine = in.readLine()) != null) {
                out.write(aLine);
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

