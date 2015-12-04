

package com.glob3mobile.utils;


import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class IOUtils {
   private IOUtils() {
   }


   public static void gentlyClose(final Closeable closeable) {
      if (closeable != null) {
         try {
            closeable.close();
         }
         catch (final IOException e) {
         }
      }
   }


   public static void writeJPEG(final BufferedImage image,
                                final File output,
                                final float quality) throws IOException {
      ImageOutputStream ios = null;
      try {
         ios = ImageIO.createImageOutputStream(output);
         final Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
         final ImageWriter writer = iter.next();
         final ImageWriteParam iwp = writer.getDefaultWriteParam();
         iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
         iwp.setCompressionQuality(quality);
         writer.setOutput(ios);
         writer.write(null, new IIOImage(image, null, null), iwp);
         writer.dispose();
      }
      finally {
         IOUtils.gentlyClose(ios);
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


   public static void checkDirectory(final File directory) throws IOException {
      if (directory.exists()) {
         if (!directory.isDirectory()) {
            throw new IOException(directory + " is not a directory");
         }
      }
      else {
         throw new IOException(directory + " doesn't exist");
      }
   }


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


}
