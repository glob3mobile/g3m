

package org.glob3.mobile.conversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.glob3.mobile.generated.BSONGenerator;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.specific.ByteBuffer_JavaDesktop;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.JSONParser_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;


public class XYZtoBSON {

   @SuppressWarnings("unused")
   private static int i;


   //THIS CLASS CONVERTS XYZ IN 4326 COORDINATES TO A JSON ARRAY AND AFTER THAT TO BSON
   public static void main(final String[] args) {
      try {

         IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
         IMathUtils.setInstance(new MathUtils_JavaDesktop());
         IFactory.setInstance(new Factory_JavaDesktop());
         ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));

         final String pathFileJson = "/Users/mdelacalle/Desktop/SUIZA/points_10.json";
         final String pathFileBson = "/Users/mdelacalle/Desktop/SUIZA/points_10.bson";

         final FileInputStream fstream = new FileInputStream("/Users/mdelacalle/Desktop/SUIZA/points.asc");

         final BufferedWriter out = new BufferedWriter(new FileWriter(pathFileJson));


         final DataInputStream in = new DataInputStream(fstream);
         final BufferedReader br = new BufferedReader(new InputStreamReader(in));
         String strLine;

         final int factor = 10;

         out.write("{\"points\": [");
         //Read File Line By Linel
         boolean firstComma = true;
         i = 0;
         while ((strLine = br.readLine()) != null) {
            // Print the content on the console


            System.out.println("" + strLine);
            if (firstComma) {
               out.write(strLine);
               firstComma = false;
            }
            else {
               if ((i % factor) == 0) {
                  out.write("," + strLine);
               }
            }


            i++;
         }
         out.write("]}");
         //Close the input stream
         in.close();
         out.close();


         final JSONBaseObject jbase = readJsonFile(new File(pathFileJson));
         writeBsonFile(jbase, new File(pathFileBson));

         System.out.println("Finished");

      }
      catch (final FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (final IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * @param fBson
    */
   private static void writeBsonFile(final JSONBaseObject jbase,
                                     final File fBson) {
      if (!fBson.exists() && (jbase != null)) {
         try {
            final FileOutputStream fout = new FileOutputStream(fBson);

            final ByteBuffer_JavaDesktop bb = (ByteBuffer_JavaDesktop) BSONGenerator.generate(jbase);
            fout.write(bb.getBuffer().array());
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


   private static JSONBaseObject readJsonFile(final File fJson) {
      JSONBaseObject jbase = null;
      if (fJson.exists()) {
         try {
            // create FileInputStream object
            final FileInputStream finJson = new FileInputStream(fJson);
            /*
             * Create byte array large enough to hold the content of the file.
             * Use File.length to determine size of the file in bytes.
             */
            final byte fileContent[] = new byte[(int) fJson.length()];

            /*
             * To read content of the file in byte array, use
             * int read(byte[] byteArray) method of java FileInputStream class.
             */
            finJson.read(fileContent);
            finJson.close();

            final JSONParser_JavaDesktop jp = new JSONParser_JavaDesktop();
            jbase = jp.parse(new ByteBuffer_JavaDesktop(fileContent));

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


}
