

package org.glob3.mobile.tools.utils;

import java.io.File;
import java.io.IOException;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.tools.gdal.GDALException;


public class FileUtils {

   public static String getFileNameWithoutExtension(final String fileName) throws GDALException {
      if ((fileName != null) && (fileName.trim().length() != 0)) {
         final String[] aux = fileName.split("\\.");
         if (aux.length != 2) {
            System.out.println("This file name '" + fileName + "' is not formatted as 'name.extension'.");
         }
         return aux[0];
      }
      throw new GDALException("Filename is Wrong (null or empty", null);
   }


   public static boolean checkFile(final File file) {
      return ((file != null) && file.exists() && file.isFile());
   }


   public static boolean checkDir(final File dir) {
      return ((dir != null) && dir.exists() && dir.isDirectory());
   }


   public static boolean deleteDirectory(final File decompressDir) {
      try {
         org.apache.commons.io.FileUtils.deleteDirectory(decompressDir);
         ILogger.instance().logInfo("Temp decompress dir has been delete successfuly: " + decompressDir.getAbsolutePath());
         return true;
      }
      catch (final IOException e) {
         ILogger.instance().logError("Temp decompress dir hasn't been delete successfuly: " + decompressDir.getAbsolutePath(),
                  e.getMessage());
         return false;
      }
   }
}
