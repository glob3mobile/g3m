

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;


public class JSONParser_Android
         extends
            IJSONParser {

   @Override
   public org.glob3.mobile.generated.JSONBaseObject parse(final IByteBuffer buffer) {
      return parse(buffer.getAsString());
   }


   @Override
   public org.glob3.mobile.generated.JSONBaseObject parse(final String jsonString) {
      if (jsonString == null) {
         ILogger.instance().logError("Can't parse a null string");
         return null;
      }

      try {
         final Object jsonObject = new org.json.JSONTokener(jsonString).nextValue();
         return convert(jsonObject);
      }
      catch (final org.json.JSONException e) {
         e.printStackTrace();
         return null;
      }
   }


   private static org.glob3.mobile.generated.JSONBaseObject convert(final Object jsonObject) {
      // JSONObject, JSONArray, String, Boolean, Integer, Long, Double or NULL.

      if (jsonObject == null) {
         return null;
      }
      else if (jsonObject instanceof String) {
         return new org.glob3.mobile.generated.JSONString((String) jsonObject);
      }
      else if (jsonObject instanceof Boolean) {
         return new org.glob3.mobile.generated.JSONBoolean(((Boolean) jsonObject).booleanValue());
      }
      else if (jsonObject instanceof Integer) {
         return new org.glob3.mobile.generated.JSONInteger(((Integer) jsonObject).intValue());
      }
      else if (jsonObject instanceof Long) {
         final long longValue = ((Long) jsonObject).longValue();
         final int intValue = (int) longValue;
         if (longValue == intValue) {
            return new org.glob3.mobile.generated.JSONInteger(intValue);
         }
         return new org.glob3.mobile.generated.JSONLong(longValue);
      }
      else if (jsonObject instanceof Double) {
         final double doubleValue = ((Double) jsonObject).doubleValue();
         final float floatValue = (float) doubleValue;
         if (doubleValue == floatValue) {
            return new org.glob3.mobile.generated.JSONFloat(floatValue);
         }
         return new org.glob3.mobile.generated.JSONDouble(doubleValue);
      }
      else if (jsonObject instanceof Float) {
         return new org.glob3.mobile.generated.JSONFloat(((Float) jsonObject).floatValue());
      }
      else if (jsonObject instanceof org.json.JSONArray) {
         final org.json.JSONArray jsonArray = (org.json.JSONArray) jsonObject;
         final org.glob3.mobile.generated.JSONArray result = new org.glob3.mobile.generated.JSONArray();
         final int length = jsonArray.length();
         for (int i = 0; i < length; i++) {
            try {
               final Object child = jsonArray.isNull(i) ? null : jsonArray.get(i);
               result.add(convert(child));
            }
            catch (final org.json.JSONException e) {
               e.printStackTrace();
            }
         }
         return result;
      }
      else if (jsonObject instanceof org.json.JSONObject) {
         final org.json.JSONObject jsonObj = (org.json.JSONObject) jsonObject;
         final org.json.JSONArray attributes = jsonObj.names();
         final org.glob3.mobile.generated.JSONObject result = new org.glob3.mobile.generated.JSONObject();
         final int length = attributes.length();
         for (int i = 0; i < length; i++) {
            try {
               final String key = attributes.getString(i);
               final Object value = jsonObj.isNull(key) ? null : jsonObj.get(key);
               result.put(key, convert(value));
            }
            catch (final org.json.JSONException e) {
               e.printStackTrace();
            }
         }
         return result;
      }
      else {
         ILogger.instance().logError("Unsupported JSON type: " + jsonObject.getClass());
         return null;
      }
   }
}
