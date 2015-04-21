

package org.glob3.mobile.specific;

import java.util.Map;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONBoolean;
import org.glob3.mobile.generated.JSONDouble;
import org.glob3.mobile.generated.JSONFloat;
import org.glob3.mobile.generated.JSONInteger;
import org.glob3.mobile.generated.JSONLong;
import org.glob3.mobile.generated.JSONNull;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.JSONString;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


public class JSONParser_JavaDesktop
         extends
            IJSONParser {

   @Override
   public JSONBaseObject parse(final IByteBuffer buffer,
                               final boolean nullAsObject) {
      return parse(buffer.getAsString(), nullAsObject);
   }


   @Override
   public JSONBaseObject parse(final String string,
                               final boolean nullAsObject) {
      final JsonParser parser = new JsonParser();
      final JsonElement element = parser.parse(string);

      return convert(element, nullAsObject);
   }


   private JSONBaseObject convert(final JsonElement element,
                                  final boolean nullAsObject) {
      if (element.isJsonNull()) {
         return nullAsObject ? new JSONNull() : null;
      }
      else if (element.isJsonObject()) {
         final JsonObject jsonObject = (JsonObject) element;
         final JSONObject result = new JSONObject();
         for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            result.put(entry.getKey(), convert(entry.getValue(), nullAsObject));
         }
         return result;
      }
      else if (element.isJsonPrimitive()) {
         final JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
         if (jsonPrimitive.isBoolean()) {
            return new JSONBoolean(jsonPrimitive.getAsBoolean());
         }
         else if (jsonPrimitive.isNumber()) {
            final double doubleValue = jsonPrimitive.getAsDouble();
            final long longValue = (long) doubleValue;
            if (doubleValue == longValue) {
               final int intValue = (int) longValue;
               return (intValue == longValue) ? new JSONInteger(intValue) : new JSONLong(longValue);
            }
            final float floatValue = (float) doubleValue;
            return (floatValue == doubleValue) ? new JSONFloat(floatValue) : new JSONDouble(doubleValue);
         }
         else if (jsonPrimitive.isString()) {
            return new JSONString(jsonPrimitive.getAsString());
         }
         else {
            throw new RuntimeException("JSON unsopoerted" + element.getClass());
         }
      }
      else if (element.isJsonArray()) {
         final JsonArray jsonArray = (JsonArray) element;
         final JSONArray result = new JSONArray();
         for (final JsonElement child : jsonArray) {
            result.add(convert(child, nullAsObject));
         }
         return result;
      }
      else {
         throw new RuntimeException("JSON unsopoerted" + element.getClass());
      }

   }


}
