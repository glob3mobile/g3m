
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class JSONParser_WebGL extends IJSONParser {

   @Override
   public JSONBaseObject parse(final IByteBuffer buffer, final boolean nullAsObject) {
      return parse(buffer.getAsString(), nullAsObject);
   }

   @Override
   public JSONBaseObject parse(final String string, final boolean nullAsObject) {
      final com.google.gwt.json.client.JSONValue value = com.google.gwt.json.client.JSONParser.parseStrict(string);

      return convert(value, nullAsObject);
   }

   private static JSONBaseObject convert(final com.google.gwt.json.client.JSONValue value, final boolean nullAsObject) {

      final com.google.gwt.json.client.JSONNull jsonNull = value.isNull();
      if (jsonNull != null) {
         return nullAsObject ? new JSONNull() : null;
      }

      final com.google.gwt.json.client.JSONBoolean jsonBoolean = value.isBoolean();
      if (jsonBoolean != null) {
         return new JSONBoolean(jsonBoolean.booleanValue());
      }

      final com.google.gwt.json.client.JSONNumber jsonNumber = value.isNumber();
      if (jsonNumber != null) {
         final double doubleValue = jsonNumber.doubleValue();
         final int    intValue    = (int) doubleValue;
         if (doubleValue == intValue) {
            return new JSONInteger(intValue);
         }
         final long longValue = (long) doubleValue;
         if (doubleValue == longValue) {
            return new JSONLong(longValue);
         }
         final float floatValue = (float) doubleValue;
         if (doubleValue == floatValue) {
            return new JSONFloat(floatValue);
         }
         return new JSONDouble(doubleValue);
      }

      final com.google.gwt.json.client.JSONString jsonString = value.isString();
      if (jsonString != null) {
         return new JSONString(jsonString.stringValue());
      }

      final com.google.gwt.json.client.JSONArray jsonArray = value.isArray();
      if (jsonArray != null) {
         final int       size  = jsonArray.size();
         final JSONArray array = new JSONArray(size);
         for (int i = 0; i < size; i++) {
            final com.google.gwt.json.client.JSONValue element = jsonArray.get(i);
            array.add(convert(element, nullAsObject));
         }
         return array;
      }

      final com.google.gwt.json.client.JSONObject jsonObject = value.isObject();
      if (jsonObject != null) {
         final JSONObject object = new JSONObject();
         for (final String key : jsonObject.keySet()) {
            object.put(key, convert(jsonObject.get(key), nullAsObject));
         }
         return object;
      }

      ILogger.instance().logError("Invalid type in \"" + value + "\"");

      return null;
   }

}
