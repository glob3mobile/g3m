

package org.glob3.mobile.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONGenerator;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.specific.ByteBuffer_JavaDesktop;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;


public class BatchBsonParser {
   public static void main(final String[] args) {

      System.out.println("Batch BSON Parser Desktop 0.1");
      System.out.println("-----------------------------\n");

      if (args.length != 2) {
         System.out.println("Arguments must be properly specified.");
         System.exit(1);
      }

      // Inicializando
      final File fBson = new File(args[0]);
      final File fJson = new File(args[1]);

      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IFactory.setInstance(new Factory_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));

      if (fBson.exists() && fBson.isFile() && fBson.getName().toLowerCase().endsWith(".bson")) {
         final JSONBaseObject jbase = readBsonFile(fBson);
         if (jbase != null) {
            try {
               if (fJson.exists() || fJson.createNewFile()) {
                  writeJsonFile(jbase, fJson);
               }
               else {
                  System.out.println("Output file does not exist or it was not possible to be created");
                  System.exit(1);
               }
            }
            catch (final IOException e) {
               System.out.println("Error while creating new file");
               System.exit(1);
            }
         }
         else {
            System.out.println("BSON file was not properly parsed");
            System.exit(1);
         }
      }
      else {
         System.out.println("Input JSON file was not properly specified");
         System.exit(1);
      }
   }


   /**
    * @param fBson
    */
   private static JSONBaseObject readBsonFile(final File fBson) {
      JSONBaseObject jbase = null;
      if (fBson.exists()) {
         try {
            // create FileInputStream object
            final FileInputStream finBson = new FileInputStream(fBson);
            /*
             * Create byte array large enough to hold the content of the file.
             * Use File.length to determine size of the file in bytes.
             */
            final byte fileContent[] = new byte[(int) fBson.length()];

            /*
             * To read content of the file in byte array, use
             * int read(byte[] byteArray) method of java FileInputStream class.
             */
            finBson.read(fileContent);
            finBson.close();
            final ByteBuffer_JavaDesktop bb = new ByteBuffer_JavaDesktop(fileContent);
            jbase = BSONParser.parse(bb);
            System.out.println(jbase.description());
         }
         catch (final FileNotFoundException e) {
            System.out.println("File not found" + e);
         }
         catch (final IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
         }
      }

      return jbase;
   }


   /**
    * @param fJson
    */
   private static void writeJsonFile(final JSONBaseObject jbase,
                                     final File fJson) {
      if (fJson.exists() && (jbase != null)) {
         try {
            final FileOutputStream fout = new FileOutputStream(fJson);
            fout.write(JSONGenerator.generate(jbase).getBytes());
            fout.flush();
            fout.close();
         }
         catch (final FileNotFoundException e) {
            System.out.println("File not found" + e);
         }
         catch (final IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
         }
      }
   }
}
