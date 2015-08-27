/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatesorteos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author NICOLAS
 */
public class Unzip
{
   final static int BUFFER = 2048;
   public static void unzip ()
   {
      try 
      {
         BufferedOutputStream dest = null;
         FileInputStream fis = new FileInputStream(new File("archi.zip"));
         
         ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
         ZipEntry entry;
         while((entry = zis.getNextEntry()) != null) 
         {
            System.out.println("Extracting: " + entry.getName().toString());
            int count;
            byte data[] = new byte[BUFFER];
            // write the files to the disk
             System.out.println(""+ entry.getName());
            FileOutputStream fos = new  FileOutputStream(new File(entry.getName().toString()));
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) 
            {
               dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
         }
         zis.close();
      } 
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
