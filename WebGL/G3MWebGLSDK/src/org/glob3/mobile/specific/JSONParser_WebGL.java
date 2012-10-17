

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONBoolean;
import org.glob3.mobile.generated.JSONNumber;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.JSONString;


public class JSONParser_WebGL
         extends
            IJSONParser {

   @Override
   public JSONBaseObject parse(final IByteBuffer buffer) {
      return parse(buffer.getAsString());
   }


   @Override
   public JSONBaseObject parse(final String string) {
      final com.google.gwt.json.client.JSONValue value = com.google.gwt.json.client.JSONParser.parseLenient(string);

      return convert(value);
   }


   private static JSONBaseObject convert(final com.google.gwt.json.client.JSONValue value) {

      final com.google.gwt.json.client.JSONNull jsonNull = value.isNull();
      if (jsonNull != null) {
         return null;
      }

      final com.google.gwt.json.client.JSONBoolean jsonBoolean = value.isBoolean();
      if (jsonBoolean != null) {
         return new JSONBoolean(jsonBoolean.booleanValue());
      }

      final com.google.gwt.json.client.JSONNumber jsonNumber = value.isNumber();
      if (jsonNumber != null) {
         return new JSONNumber(jsonNumber.doubleValue());
      }

      final com.google.gwt.json.client.JSONString jsonString = value.isString();
      if (jsonString != null) {
         return new JSONString(jsonString.stringValue());
      }

      final com.google.gwt.json.client.JSONArray jsonArray = value.isArray();
      if (jsonArray != null) {
         final JSONArray array = new JSONArray();
         final int size = jsonArray.size();
         for (int i = 0; i < size; i++) {
            final com.google.gwt.json.client.JSONValue element = jsonArray.get(i);
            array.appendElement(convert(element));
         }
         return array;
      }

      final com.google.gwt.json.client.JSONObject jsonObject = value.isObject();
      if (jsonObject != null) {
         final JSONObject object = new JSONObject();
         for (final String key : jsonObject.keySet()) {
            object.putObject(key, convert(jsonObject.get(key)));
         }
         return object;
      }

      ILogger.instance().logError("Invalid type in \"" + value + "\"");

      return null;
   }


}
