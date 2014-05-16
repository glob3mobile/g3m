

package org.glob3.mobile.tools.conversion.jbson2bjson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.glob3.mobile.generated.BSONGenerator;
import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONGenerator;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.JSONString;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.specific.ByteBuffer_JavaDesktop;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.JSONParser_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;


public class JBson2BJson {


   private static JBson2BJson _jsonParser = null;


   public static synchronized JBson2BJson instance() {
      if (_jsonParser == null) {
         _jsonParser = new JBson2BJson();
      }

      return _jsonParser;
   }


   private JBson2BJson() {
      initialize();
   }


   private static void initialize() {
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
      if (IMathUtils.instance() == null) {
         IMathUtils.setInstance(new MathUtils_JavaDesktop());
      }
      if (IFactory.instance() == null) {
         IFactory.setInstance(new Factory_JavaDesktop());
      }
      if (ILogger.instance() == null) {
         ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));
      }
   }


   public void transform(final File firstFile,
                         final File secondFile,
                         final boolean overwrite) throws JBson2BJsonException {
      if ((firstFile.getName().toLowerCase().endsWith(".json") || firstFile.getName().toLowerCase().endsWith(".geojson"))
          && secondFile.getName().toLowerCase().endsWith(".bson")) {
         JBson2BJson.instance().json2bson(firstFile, secondFile, overwrite);
      }
      else if ((secondFile.getName().toLowerCase().endsWith(".json") || secondFile.getName().toLowerCase().endsWith(".geojson"))
               && firstFile.getName().toLowerCase().endsWith(".bson")) {
         JBson2BJson.instance().bson2json(firstFile, secondFile, overwrite);
      }
      else {
         throw new JBson2BJsonException("File format isn't ready: " + firstFile.getName() + " and " + secondFile.getName());
      }
   }


   /**
    * 
    * @param fJson
    * @param fBson
    * @throws JBson2BJsonException
    */
   public void json2bson(final File fJson,
                         final File fBson,
                         final boolean overwrite) throws JBson2BJsonException {
      if (Utils.checkFileIsJson(fJson)) {
         if (overwrite && fBson.exists()) {
            if (!fBson.delete()) {
               throw new JBson2BJsonException("Output bson file exist and can't to delete it for overwrite", null);
            }
         }

         final JSONBaseObject jbase = readJsonFile(fJson);
         if (jbase != null) {
            try {
               if (fBson.exists() || fBson.createNewFile()) {
                  writeBsonFile(jbase, fBson);
               }
               else {
                  throw new JBson2BJsonException("Output bson file does not exist or it was not possible to be created");
               }
            }
            catch (final IOException e) {
               throw new JBson2BJsonException(e.getMessage(), e.getCause());
            }
         }
         else {
            throw new JBson2BJsonException("JSON file was not properly parsed");
         }
      }
      else {
         throw new JBson2BJsonException("Input JSON file was not properly specified");
      }
   }


   /**
    * 
    * @param jsonString
    * @param fBson
    * @throws JBson2BJsonException
    */
   public void json2bson(final String jsonString,
                         final File fBson,
                         final boolean overwrite) throws JBson2BJsonException {

      if (overwrite && fBson.exists()) {
         if (!fBson.delete()) {
            throw new JBson2BJsonException("Output bson file exist and can't to delete it for overwrite", null);
         }
      }

      final JSONBaseObject jbase = parseJsonString(jsonString);
      if (jbase != null) {
         try {
            if (fBson.exists() || fBson.createNewFile()) {
               writeBsonFile(jbase, fBson);
            }
            else {
               throw new JBson2BJsonException("Output bson file does not exist or it was not possible to be created");
            }
         }
         catch (final IOException e) {
            throw new JBson2BJsonException(e.getMessage(), e.getCause());
         }
      }
      else {
         throw new JBson2BJsonException("JSON String was not properly parsed");
      }

   }


   /**
    * 
    * @param fBson
    * @param overwrite
    * @param fsJson
    * @throws JBson2BJsonException
    */
   public void jsons2bson(final File fBson,
                          final boolean overwrite,
                          final File... fsJson) throws JBson2BJsonException {
      if (overwrite && fBson.exists()) {
         if (!fBson.delete()) {
            throw new JBson2BJsonException("Output bson file exist and can't to delete it for overwrite", null);
         }
      }

      final JSONObject jRoot = new JSONObject();
      jRoot.put("type", new JSONString("FeatureCollection"));

      final JSONArray features = new JSONArray();
      for (final File fJson : fsJson) {
         if (Utils.checkFileIsJson(fJson)) {
            final JSONBaseObject jbase = readJsonFile(fJson);
            if (jbase != null) {
               final JSONArray auxFeatures = jbase.asObject().getAsArray("features");
               if (auxFeatures != null) {
                  final int size = auxFeatures.size();
                  for (int i = 0; i < size; i++) {
                     features.add(auxFeatures.getAsObject(i));
                  }
               }
            }
         }
      }
      jRoot.put("features", features);

      try {
         if (fBson.exists() || fBson.createNewFile()) {
            writeBsonFile(jRoot, fBson);
         }
         else {
            throw new JBson2BJsonException("Output bson file does not exist or it was not possible to be created");
         }
      }
      catch (final IOException e) {
         throw new JBson2BJsonException(e.getMessage(), e.getCause());
      }
   }


   /**
    * 
    * @param fBson
    * @param fJson
    * @param overwrite
    * @throws JBson2BJsonException
    */
   public void bson2json(final File fBson,
                         final File fJson,
                         final boolean overwrite) throws JBson2BJsonException {
      if (overwrite && fJson.exists()) {
         if (!fJson.delete()) {
            throw new JBson2BJsonException("Output JSON file exist and can't to delete it for overwrite", null);
         }
      }
      if (Utils.checkFileIsBson(fBson)) {
         final JSONBaseObject jbase = readBsonFile(fBson);
         if (jbase != null) {
            try {
               if (fJson.exists() || fJson.createNewFile()) {
                  writeJsonFile(jbase, fJson);
               }
               else {
                  throw new JBson2BJsonException("Output JSON file does not exist or it was not possible to be created");
               }
            }
            catch (final IOException e) {
               throw new JBson2BJsonException(e.getMessage(), e.getCause());
            }
         }
         else {
            throw new JBson2BJsonException("BSON file was not properly parsed");
         }
      }
      else {
         throw new JBson2BJsonException("Input BSON file was not properly specified");
      }
   }


   /**
    * @param fBson
    */
   private JSONBaseObject readBsonFile(final File fBson) {
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
    * 
    * @param jbase
    * @param fJson
    */
   private void writeJsonFile(final JSONBaseObject jbase,
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


   /**
    * 
    * @param jbase
    * @param fBson
    */
   private void writeBsonFile(final JSONBaseObject jbase,
                              final File fBson) {
      if (fBson.exists() && (jbase != null)) {
         try {
            final FileOutputStream fout = new FileOutputStream(fBson);

            final ByteBuffer_JavaDesktop bb = (ByteBuffer_JavaDesktop) BSONGenerator.generate(jbase);
            fout.write(bb.getBuffer().array());
            fout.flush();
            fout.close();
            bb.dispose();
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
   private JSONBaseObject readJsonFile(final File fJson) {
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


   private JSONBaseObject parseJsonString(final String jsonString) {

      final byte content[] = jsonString.getBytes();
      final JSONParser_JavaDesktop jp = new JSONParser_JavaDesktop();
      return jp.parse(new ByteBuffer_JavaDesktop(content));
   }


   private static class Utils {

      public static boolean checkFileIsJson(final File fJson) {
         if (fJson.exists() && fJson.isFile()
             && (fJson.getName().toLowerCase().endsWith(".json") || fJson.getName().toLowerCase().endsWith(".geojson"))) {
            return true;
         }

         return false;
      }


      public static boolean checkFileIsBson(final File fBson) {
         if (fBson.exists() && fBson.isFile() && fBson.getName().toLowerCase().endsWith(".bson")) {
            return true;
         }

         return false;
      }
   }
}
