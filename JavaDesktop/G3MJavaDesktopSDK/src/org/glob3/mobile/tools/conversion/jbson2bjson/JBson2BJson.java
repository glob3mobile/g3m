

package org.glob3.mobile.tools.conversion.jbson2bjson;

import java.io.*;

import org.glob3.mobile.generated.*;
import org.glob3.mobile.specific.*;


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
      IStringBuilder.setInstance(new StringBuilder_JavaDesktop(IStringBuilder.DEFAULT_FLOAT_PRECISION));
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


   public static void transform(final File firstFile,
                                final File secondFile,
                                final boolean overwrite) throws JBson2BJsonException, IOException {
      if ((firstFile.getName().toLowerCase().endsWith(".json") || firstFile.getName().toLowerCase().endsWith(".geojson"))
          && secondFile.getName().toLowerCase().endsWith(".bson")) {
         JBson2BJson.instance();
         JBson2BJson.json2bson(firstFile, secondFile, overwrite);
      }
      else if ((secondFile.getName().toLowerCase().endsWith(".json") || secondFile.getName().toLowerCase().endsWith(".geojson"))
               && firstFile.getName().toLowerCase().endsWith(".bson")) {
         JBson2BJson.instance();
         JBson2BJson.bson2json(firstFile, secondFile, overwrite);
      }
      else {
         throw new JBson2BJsonException("File format isn't ready: " + firstFile.getName() + " and " + secondFile.getName());
      }
   }


   static public void json2bson(final File fJson,
                                final File fBson,
                                final boolean overwrite) throws JBson2BJsonException, IOException {
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


   static public void jsons2bson(final File fBson,
                                 final boolean overwrite,
                                 final File... fsJson) throws JBson2BJsonException, IOException {
      if (overwrite && fBson.exists()) {
         if (!fBson.delete()) {
            throw new JBson2BJsonException("Output bson file exist and can't to delete it for overwrite", null);
         }
      }

      final JSONObject jRoot = new JSONObject();
      jRoot.put("type", new JSONString("FeatureCollection"));

      final JSONArray features = new JSONArray(10);
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


   public static void bson2json(final File fBson,
                                final File fJson,
                                final boolean overwrite) throws JBson2BJsonException, IOException {
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


   static private JSONBaseObject readBsonFile(final File fBson) throws IOException {
      JSONBaseObject jbase = null;
      if (fBson.exists()) {
         // create FileInputStream object
         try (final FileInputStream finBson = new FileInputStream(fBson)) {
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
            final ByteBuffer_JavaDesktop bb = new ByteBuffer_JavaDesktop(fileContent);
            jbase = BSONParser.parse(bb);
         }
      }
      return jbase;
   }


   static private void writeJsonFile(final JSONBaseObject jbase,
                                     final File fJson) throws IOException {
      if (fJson.exists() && (jbase != null)) {
         try (final FileOutputStream fout = new FileOutputStream(fJson)) {
            final int floatPrecision = IStringBuilder.DEFAULT_FLOAT_PRECISION;
            fout.write(JSONGenerator.generate(jbase, floatPrecision).getBytes());
            fout.flush();
         }
      }
   }


   static private void writeBsonFile(final JSONBaseObject jbase,
                                     final File fBson) throws IOException {
      if (fBson.exists() && (jbase != null)) {
         try (final FileOutputStream fout = new FileOutputStream(fBson)) {
            final ByteBuffer_JavaDesktop bb = (ByteBuffer_JavaDesktop) BSONGenerator.generate(jbase);
            fout.write(bb.getBuffer().array());
            fout.flush();
            bb.dispose();
         }
      }
   }


   static private JSONBaseObject readJsonFile(final File fJson) throws IOException {
      JSONBaseObject jbase = null;
      if (fJson.exists()) {
         // create FileInputStream object
         try (final FileInputStream finJson = new FileInputStream(fJson)) {
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

            final JSONParser_JavaDesktop jp = new JSONParser_JavaDesktop();
            jbase = jp.parse(new ByteBuffer_JavaDesktop(fileContent));
         }

      }

      return jbase;
   }


   private static class Utils {

      public static boolean checkFileIsJson(final File fJson) {
         return fJson.exists() && fJson.isFile()
                && (fJson.getName().toLowerCase().endsWith(".json") || fJson.getName().toLowerCase().endsWith(".geojson"));

      }


      public static boolean checkFileIsBson(final File fBson) {
         return fBson.exists() && fBson.isFile() && fBson.getName().toLowerCase().endsWith(".bson");

      }
   }
}
