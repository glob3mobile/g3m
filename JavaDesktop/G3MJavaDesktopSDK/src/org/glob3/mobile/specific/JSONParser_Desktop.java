

package org.glob3.mobile.specific;

import java.util.Map.Entry;
import java.util.Set;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONBaseObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


public class JSONParser_Desktop
    extends
      IJSONParser {

  @Override
  public org.glob3.mobile.generated.JSONBaseObject parse(final String string) {


    final JsonParser parser = new JsonParser();
    final JsonElement je = parser.parse(string);

    org.glob3.mobile.generated.JSONBaseObject g3mJSONBaseObject = null;

    if (je.isJsonArray()) {
      final JsonArray jsonArray = je.getAsJsonArray();
      g3mJSONBaseObject = new org.glob3.mobile.generated.JSONArray();

      for (int i = 0; i < jsonArray.size(); i++) {
        g3mJSONBaseObject.asArray().add(parse(jsonArray.get(i).toString()));
      }
    }
    else if (je.isJsonObject()) {

      final JsonObject jsonObj = je.getAsJsonObject();
      final Set<Entry<String, JsonElement>> set = jsonObj.entrySet();


      g3mJSONBaseObject = new org.glob3.mobile.generated.JSONObject();

      for (final Entry<String, JsonElement> entry : set) {
        g3mJSONBaseObject.asObject().put(entry.getKey(),
            parse(entry.getValue().toString()));
      }
    }

    else if (je.isJsonPrimitive()) {
      final JsonPrimitive jp = je.getAsJsonPrimitive();

      if (jp.isString()) {
        // g3mJSONBaseObject = new
        // org.glob3.mobile.generated.JSONString((String) rawJson);

        // TODO Hack to return the full string if it contain ":"
        g3mJSONBaseObject = new org.glob3.mobile.generated.JSONString(
            jp.getAsString());
      }
      else if (jp.isBoolean()) {
        g3mJSONBaseObject = new org.glob3.mobile.generated.JSONBoolean(
            je.getAsBoolean());
      }
      else if (jp.isNumber()) {
        final Number number = jp.getAsNumber();
        try {
          final long l = number.longValue();
          if ((l <= Integer.MAX_VALUE) && (l >= Integer.MIN_VALUE)) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber(
                number.intValue());
          }
          else {
            // TODO: No hay constructor con long en JsonNumber
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber(
                number.doubleValue());
          }
        }
        catch (final NumberFormatException e) {
          final double d = number.doubleValue();
          if ((d <= Float.MAX_VALUE) && (d >= Float.MIN_VALUE)) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber(
                number.floatValue());
          }
          else {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber(d);
          }
        }
      }
    }


    return g3mJSONBaseObject;
  }


  @Override
  public JSONBaseObject parse(final IByteBuffer buffer) {
    return parse(buffer.getAsString());
  }


}
