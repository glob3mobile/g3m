

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONBaseObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class JSONParser_Android
         extends
            IJSONParser {

   @Override
   public JSONBaseObject parse(final String string) {
      org.glob3.mobile.generated.JSONBaseObject g3mJSONBaseObject = new JSONBaseObject();

      Object rawJson;
      try {
         rawJson = new JSONTokener(string).nextValue();

         if (rawJson instanceof JSONArray) {

            final JSONArray jsonArray = (JSONArray) rawJson;
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
               g3mJSONBaseObject.getArray().appendElement(parse(jsonArray.get(i).toString()));
            }
         }
         else if (rawJson instanceof JSONObject) {

            final JSONObject jsonObj = (JSONObject) rawJson;
            final JSONArray attributes = jsonObj.names();

            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONObject();

            for (int i = 0; i < attributes.length(); i++) {
               g3mJSONBaseObject.getObject().putObject(attributes.getString(i),
                        parse(jsonObj.get(attributes.getString(i)).toString()));
            }
         }
         else if (rawJson instanceof String) {
            //g3mJSONBaseObject = new org.glob3.mobile.generated.JSONString((String) rawJson);

            //TODO Hack to return the full string if it contain ":"
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONString(string);
         }
         else if (rawJson instanceof Boolean) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONBoolean((Boolean) rawJson);
         }
         else if (rawJson instanceof Long) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Integer) rawJson);
         }
         else if (rawJson instanceof Double) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Double) rawJson);
         }
         else if (rawJson instanceof Integer) {
            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Integer) rawJson);
         }
      }
      catch (final JSONException e) {
         e.printStackTrace();
      }

      return g3mJSONBaseObject;
   }


   @Override
   public JSONBaseObject parse(final IByteBuffer buffer) {
      return parse(buffer.getAsString());
   }

}
