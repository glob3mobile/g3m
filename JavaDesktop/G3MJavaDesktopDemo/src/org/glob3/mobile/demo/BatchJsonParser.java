

package org.glob3.mobile.demo;

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


public class BatchJsonParser {
   public static void main2(@SuppressWarnings("unused")
   final String[] args) {
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IFactory.setInstance(new Factory_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));

      // checkJsonParser(new String("{\"i0\":0}"), new File(
      // "/home/vidalete/bson1.bson"));
      // checkJsonParser(new String("{\"i0\":0,\"i1\":1,\"i100\":100}"), new
      // File(
      // "/home/vidalete/bson2.bson"));
      // checkJsonParser(new String("{\"d0\":0,\"d1\":1,\"d100\":100}"), new
      // File(
      // "/home/vidalete/bson3.bson"));
      // checkJsonParser(new String("{\"f\":false,\"t\":true}"), new File(
      // "/home/vidalete/bson4.bson"));
      // checkJsonParser(new String("{\"hello\":\"world\"}"), new File(
      // "/home/vidalete/bson5.bson"));

      checkJsonParser(new String("{\"array\":[3,null,4],\"formatted_address\" : \"10440 Aldeanueva, Cáceres, España\"}"),
               new File("/home/vidalete/array.bson"));
      checkJsonParser(new String(
               "{\"J\":5,\"0\":\"N\", \"objectNull\" : null, \"arrayWithNulls\" : [\"hola\", 1, 0.5, null, \"adios\"]}"),
               new File("/home/vidalete/nulles.bson"));
   }


   public static void main(final String[] args) {

      System.out.println("Batch JSON Parser Desktop 0.1");
      System.out.println("-----------------------------\n");

      if (args.length != 2) {
         System.out.println("No se han especificado los argumentos correctamente");
         System.exit(1);
      }

      // Inicializando
      final File fJson = new File(args[0]);
      final File fBson = new File(args[1]);

      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      IMathUtils.setInstance(new MathUtils_JavaDesktop());
      IFactory.setInstance(new Factory_JavaDesktop());
      ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));


      if (fJson.exists() && fJson.isFile() && fJson.getName().toLowerCase().endsWith(".json")) {
         final JSONBaseObject jbase = readJsonFile(fJson);
         if (jbase != null) {
            try {
               if (fBson.exists() || fBson.createNewFile()) {
                  writeBsonFile(jbase, fBson);
               }
               else {
                  System.out.println("El fichero de salida no existe o no se ha podido crear");
                  System.exit(1);
               }
            }
            catch (final IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         else {
            System.out.println("No se ha podido parsear correctamente el fichero json");
            System.exit(1);
         }
      }
      else {
         System.out.println("El fichero de entrada json no se ha especificado correctamente");
         System.exit(1);
      }
   }


   /**
    * @param fBson
    */
   private static void writeBsonFile(final JSONBaseObject jbase,
                                     final File fBson) {
      if (fBson.exists() && (jbase != null)) {
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


   /**
    * @param fJson
    */
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


   private static void checkJsonParser(final String json,
                                       final File fOut) {
      System.out.println("Using Gson.toJson() on a raw collection: " + json);
      final JSONParser_JavaDesktop jp = new JSONParser_JavaDesktop();
      final JSONBaseObject jbase = jp.parse(json);
      System.out.println("JSONBaseObject description: " + jbase.description());
      try {
         if (fOut.exists() || fOut.createNewFile()) {
            writeBsonFile(jbase, fOut);
         }
      }
      catch (final IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
