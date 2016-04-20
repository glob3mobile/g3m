

package com.glob3mobile.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;


public class IOUtils {
   private IOUtils() {
   }

   private static final Charset UTF_8                    = Charset.forName("UTF-8");
   private static final Object  CREATE_DIRECTORIES_MUTEX = new Object();


   public static void ensureEmptyDirectory(final File directory) throws IOException {
      ensureDirectory(directory);
      emptyDirectory(directory);
   }


   public static void emptyDirectory(final File directory) throws IOException {
      for (final File file : directory.listFiles()) {
         if (file.isDirectory()) {
            emptyDirectory(file);
         }

         if (!file.delete()) {
            throw new IOException("Can't delete " + file);
         }
      }
   }


   public static void ensureDirectory(final File directory) throws IOException {
      if (directory.exists()) {
         if (!directory.isDirectory()) {
            throw new IOException(directory + " is not a directory");
         }
      }
      else {
         if (!directory.mkdirs()) {
            throw new IOException("Can't create directory " + directory);
         }
      }
   }


   public static void save(final File file,
                           final String... data) throws IOException {
      if (!file.exists()) {
         final File directory = file.getParentFile();
         synchronized (CREATE_DIRECTORIES_MUTEX) {
            if (!directory.exists()) {
               if (!directory.mkdirs()) {
                  throw new IOException("Can't create directory " + directory);
               }
            }
         }
      }

      Files.write( //
               file.toPath(), //
               Arrays.asList(data), //
               UTF_8, //
               StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
   }

}
