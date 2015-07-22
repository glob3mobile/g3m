

package org.glob3.mobile.tools.conversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


public class JSONtoBSON {

   public static void main(final String[] args) {
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IFactory.setInstance(new Factory_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));


      //      final String pathFileJson = "/Users/mdelacalle/Desktop/A380/A380.json";

      //Path galileo
      //final String pathFileBson = "/Users/mdelacalle/Documents/java_src/IGO-GIT-Repository/galileo/Android/Galileo/assets/2479-3029-13-.bson";

      //Path Terra-Vision
      final String pathFileBson = "/Users/mdelacalle/Documents/java_src/IGO-GIT-Repository/galileo/Android/Galileo/assets/8586-5560-14-.bson";
      final String pathFileJson = "/Users/mdelacalle/Documents/java_src/workspaces10122013/OSM2World-h/8586-5550-14-.json";
      //    final String pathFileBson = "/Users/mdelacalle/Documents/java_src/IGO-GIT-Repository/wb/sldemo/Android/SLDemo/assets/boston.bson";
      //http://b.tile.openstreetmap.org/13/2478/3028.png
      //13/2479/3029.png

      final JSONBaseObject jbase = readJsonFile(new File(pathFileJson));
      writeBsonFile(jbase, new File(pathFileBson));

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

            System.out.println("finished");

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

}
