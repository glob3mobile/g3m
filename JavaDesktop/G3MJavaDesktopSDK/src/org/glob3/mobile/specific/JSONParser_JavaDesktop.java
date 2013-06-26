

package org.glob3.mobile.specific;

import java.util.Map.Entry;
import java.util.Set;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


public class JSONParser_JavaDesktop
         extends
            IJSONParser {

   @Override
   public JSONBaseObject parse(final String string,
                               final boolean nullAsObject) {


      final JsonParser parser = new JsonParser();
      final JsonElement je = parser.parse(string);

      JSONBaseObject g3mJSONBaseObject = null;

      if (je.isJsonArray()) {
         final JsonArray jsonArray = je.getAsJsonArray();
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONArray();

         for (int i = 0; i < jsonArray.size(); i++) {
            final JSONBaseObject aux = parse(jsonArray.get(i).toString());
            g3mJSONBaseObject.asArray().add(aux);
         }
      }
      else if (je.isJsonObject()) {

         final JsonObject jsonObj = je.getAsJsonObject();
         final Set<Entry<String, JsonElement>> set = jsonObj.entrySet();


         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONObject();

         for (final Entry<String, JsonElement> entry : set) {
            final JSONBaseObject aux = parse(entry.getValue().toString());
            g3mJSONBaseObject.asObject().put(entry.getKey(), aux);
         }
      }
      else if (je.isJsonNull()) {
         g3mJSONBaseObject = nullAsObject ? new JSONNull() : null;
      }
      else if (je.isJsonPrimitive()) {
         final JsonPrimitive jp = je.getAsJsonPrimitive();
         if (jp.isString()) {
            // TODO Hack to return the full string if it contain ":"
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONString(jp.getAsString());
         }
         else if (jp.isBoolean()) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONBoolean(je.getAsBoolean());
         }
         else if (jp.isNumber()) {
            final Number number = jp.getAsNumber();
            try {
               final long l = number.longValue();
               if ((l <= Integer.MAX_VALUE) && (l >= Integer.MIN_VALUE)) {
                  g3mJSONBaseObject = new org.glob3.mobile.generated.JSONInteger(number.intValue());
               }
               else {
                  g3mJSONBaseObject = new org.glob3.mobile.generated.JSONLong(l);
               }
            }
            catch (final NumberFormatException e) {
               final double d = number.doubleValue();
               if ((d <= Float.MAX_VALUE) && (d >= Float.MIN_VALUE)) {
                  g3mJSONBaseObject = new org.glob3.mobile.generated.JSONFloat(number.floatValue());
               }
               else {
                  g3mJSONBaseObject = new org.glob3.mobile.generated.JSONDouble(d);
               }
            }
         }
      }

      return g3mJSONBaseObject;

   }


   @Override
   public JSONBaseObject parse(final IByteBuffer buffer,
                               final boolean nullAsObject) {
      return parse(buffer.getAsString(), nullAsObject);
   }


}
