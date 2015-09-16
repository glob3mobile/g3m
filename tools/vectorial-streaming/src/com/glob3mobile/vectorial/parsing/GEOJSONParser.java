

package com.glob3mobile.vectorial.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.utils.UndeterminateProgress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.GEOPoint;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


public class GEOJSONParser
   implements
      GEOParser {


   public final static GEOJSONParser INSTANCE = new GEOJSONParser();


   private GEOJSONParser() {
   }

   private static final Charset UTF8 = Charset.forName("UTF-8");


   @Override
   public long countFeatures(final File file,
                             final boolean showProgress) throws IOException, GEOParseException {
      final UndeterminateProgress progress = !showProgress ? null : new UndeterminateProgress() {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println(file + ": Counting features" + progressString(stepsDone, elapsed));
         }
      };

      long counter = 0;
      try (final JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), UTF8))) {
         reader.setLenient(true);

         counter = countFeaturesFromRoot(progress, reader);

         if (reader.peek() != JsonToken.END_DOCUMENT) {
            throw new GEOParseException("Parser logic error");
         }
      }
      catch (final IllegalStateException e) {
         throw new GEOParseException(e);
      }

      if (progress != null) {
         progress.finish();
      }
      return counter;
   }


   @Override
   public <E extends Exception> void parse(final File file,
                                           final GEOFeatureHandler<E> handler) throws IOException, GEOParseException, E {
      parse(new FileInputStream(file), handler);
   }


   public <E extends Exception> void parse(final InputStream is,
                                           final GEOFeatureHandler<E> handler) throws IOException, GEOParseException, E {
      parse(new InputStreamReader(is, UTF8), handler);
   }


   public <E extends Exception> void parse(final Reader in,
                                           final GEOFeatureHandler<E> handler) throws IOException, GEOParseException, E {
      boolean finishedOK = false;
      handler.onStart();
      try (final JsonReader reader = new JsonReader(in)) {
         reader.setLenient(true);

         parseRoot(handler, reader);

         if (reader.peek() != JsonToken.END_DOCUMENT) {
            throw new GEOParseException("Parser logic error");
         }
      }
      catch (final IllegalStateException e) {
         throw new GEOParseException(e);
      }
      handler.onFinish();
      finishedOK = true;
      if (!finishedOK) {
         handler.onFinishWithException();
      }
   }


   private static void expected(final JsonReader reader,
                                final String name,
                                final String expected) throws IOException, GEOParseException {
      final String value = reader.nextString();
      if (!value.equals(expected)) {
         throw new GEOParseException("expected \"" + name + "\" : \"" + expected + "\" but got \"" + value + "\" ("
                                     + reader.getPath() + ")");
      }
   }


   private long countFeaturesFromRoot(final UndeterminateProgress progress,
                                      final JsonReader reader) throws IOException, GEOParseException {
      reader.beginObject();
      long counter = 0;
      while (reader.peek() != JsonToken.END_OBJECT) {
         final String name = reader.nextName();
         switch (name) {
            case "type":
               expected(reader, "type", "FeatureCollection");
               break;
            case "crs":
               reader.skipValue();
               break;
            case "features":
               counter = countFeatures(progress, reader);
               break;
            default:
               throw new GEOParseException("Name \"" + name + "\" not supported (" + reader.getPath() + ")");
         }
      }
      reader.endObject();
      return counter;
   }


   private <E extends Exception> void parseRoot(final GEOFeatureHandler<E> handler,
                                                final JsonReader reader) throws IOException, GEOParseException, E {
      reader.beginObject();
      while (reader.peek() != JsonToken.END_OBJECT) {
         final String name = reader.nextName();
         switch (name) {
            case "type":
               expected(reader, "type", "FeatureCollection");
               break;
            case "crs":
               reader.skipValue();
               break;
            case "features":
               parseFeatures(handler, reader);
               break;
            default:
               throw new GEOParseException("Name \"" + name + "\" not supported (" + reader.getPath() + ")");
         }
      }
      reader.endObject();
   }


   private long countFeatures(final UndeterminateProgress progress,
                              final JsonReader reader) throws IOException {
      long counter = 0;
      reader.beginArray();
      while (reader.peek() == JsonToken.BEGIN_OBJECT) {
         reader.skipValue();
         counter++;
         if (progress != null) {
            progress.stepDone();
         }
      }
      reader.endArray();
      return counter;
   }


   private <E extends Exception> void parseFeatures(final GEOFeatureHandler<E> handler,
                                                    final JsonReader reader) throws IOException, GEOParseException, E {
      reader.beginArray();
      while (reader.peek() == JsonToken.BEGIN_OBJECT) {
         parseFeature(handler, reader);
      }
      reader.endArray();
   }


   private <E extends Exception> void parseFeature(final GEOFeatureHandler<E> handler,
                                                   final JsonReader reader) throws IOException, GEOParseException, E {

      Map<String, Object> properties = null;
      GEOGeometry geometry = null;

      reader.beginObject();
      while (reader.peek() != JsonToken.END_OBJECT) {
         final String name = reader.nextName();
         switch (name) {
            case "type":
               expected(reader, "type", "Feature");
               break;
            case "properties":
               properties = parseObject(reader);
               break;
            case "geometry":
               geometry = parseGeometry(reader);
               break;
            default:
               throw new GEOParseException("Name \"" + name + "\" not supported (" + reader.getPath() + ")");
         }
      }
      reader.endObject();

      if ((properties == null) || (geometry == null)) {
         handler.onError(properties, geometry);
      }
      else {
         handler.onFeature(properties, geometry);
      }
   }


   private GEOGeometry parseGeometry(final JsonReader reader) throws IOException, GEOParseException {
      String type = null;
      List<Object> coordinates = null;

      if (reader.peek() == JsonToken.NULL) {
         reader.nextNull();
         return null;
      }

      reader.beginObject();
      while (reader.peek() != JsonToken.END_OBJECT) {
         final String name = reader.nextName();
         switch (name) {
            case "type":
               type = reader.nextString();
               break;
            case "coordinates":
               coordinates = parseArray(reader);
               break;
            default:
               throw new GEOParseException("Name \"" + name + "\" not supported (" + reader.getPath() + ")");
         }
      }
      reader.endObject();

      if (coordinates == null) {
         throw new GEOParseException("Geometry coordinates not found");
      }

      if ("Point".equalsIgnoreCase(type)) {
         final double longitude = ((Number) coordinates.get(0)).doubleValue();
         final double latitude = ((Number) coordinates.get(1)).doubleValue();
         return new GEOPoint(Geodetic2D.fromDegrees(latitude, longitude));
      }

      throw new GEOParseException("Invalid Geometry type " + type);
   }


   private static Map<String, Object> parseObject(final JsonReader reader) throws IOException, GEOParseException {
      final Map<String, Object> result = new LinkedHashMap<>();
      reader.beginObject();
      while (reader.peek() != JsonToken.END_OBJECT) {
         final String name = reader.nextName();
         result.put(name, parseValue(reader));
      }
      reader.endObject();
      return result;
   }


   private static Object parseValue(final JsonReader reader) throws IOException, GEOParseException {
      final JsonToken token = reader.peek();
      switch (token) {
         case BEGIN_OBJECT:
            return parseObject(reader);
         case BEGIN_ARRAY:
            return parseArray(reader);
         case NUMBER:
            return parseNumber(reader);
         case STRING:
            return reader.nextString();
         case NULL:
            reader.nextNull();
            return null;
         default:
            throw new GEOParseException("Token \"" + token + "\" not supported (" + reader.getPath() + ")");
      }
   }


   private static Number parseNumber(final JsonReader reader) throws IOException {
      final String value = reader.nextString();
      if ((value.indexOf('.')) >= 0) {
         return Double.valueOf(value);
      }
      return Long.valueOf(value);
   }


   private static List<Object> parseArray(final JsonReader reader) throws IOException, GEOParseException {
      final List<Object> result = new ArrayList<>();
      reader.beginArray();
      while (reader.peek() != JsonToken.END_ARRAY) {
         result.add(parseValue(reader));
      }
      reader.endArray();
      return result;
   }

}
