

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
               g3mJSONBaseObject.getArray().appendElement(makeJSONElement(jsonArray.get(i)));
            }
         }
         else if (rawJson instanceof JSONObject) {

            final JSONObject jsonObj = (JSONObject) rawJson;
            final JSONArray attributes = jsonObj.names();

            g3mJSONBaseObject = new org.glob3.mobile.generated.JSONObject();

            for (int i = 0; i < attributes.length(); i++) {
               g3mJSONBaseObject.getObject().putObject(attributes.getString(i),
                        makeJSONElement(jsonObj.get(attributes.getString(i))));
            }
         }
      }
      catch (final JSONException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return g3mJSONBaseObject;

   }


   @Override
   public JSONBaseObject parse(final IByteBuffer buffer) {
      return parse(buffer.getAsString());
   }


   private JSONBaseObject makeJSONElement(final Object jsonObject) throws JSONException {

      org.glob3.mobile.generated.JSONBaseObject g3mJSONBaseObject = new JSONBaseObject();

      if (jsonObject instanceof JSONArray) {

         final JSONArray jsonArray = (JSONArray) jsonObject;
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONArray();

         for (int i = 0; i < jsonArray.length(); i++) {
            g3mJSONBaseObject.getArray().appendElement(makeJSONElement(jsonArray.get(i)));
         }
      }
      else if (jsonObject instanceof JSONObject) {

         final JSONObject jsonObj = (JSONObject) jsonObject;
         final JSONArray attributes = jsonObj.names();

         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONObject();

         for (int i = 0; i < attributes.length(); i++) {
            g3mJSONBaseObject.getObject().putObject(attributes.getString(i),
                     makeJSONElement(jsonObj.get(attributes.getString(i))));
         }
      }
      else if (jsonObject instanceof String) {
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONString((String) jsonObject);
      }
      else if (jsonObject instanceof Boolean) {
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONBoolean((Boolean) jsonObject);
      }
      else if (jsonObject instanceof Long) {
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Integer) jsonObject);
      }
      else if (jsonObject instanceof Double) {
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Double) jsonObject);
      }
      else if (jsonObject instanceof Integer) {
         g3mJSONBaseObject = new org.glob3.mobile.generated.JSONNumber((Integer) jsonObject);
      }

      return g3mJSONBaseObject;
   }

}
