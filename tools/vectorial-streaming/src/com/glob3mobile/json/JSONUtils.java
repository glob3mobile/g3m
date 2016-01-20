

package com.glob3mobile.json;


import java.util.List;
import java.util.Map;


public class JSONUtils {

   private JSONUtils() {
   }


   @SuppressWarnings("unchecked")
   public static String toJSON(final Object value) {
      if (value == null) {
         return "null";
      }
      else if (value instanceof String) {
         return toJSON((String) value);
      }
      else if (value instanceof Number) {
         return toJSON((Number) value);
      }
      else if (value instanceof Boolean) {
         return toJSON((Boolean) value);
      }
      else if (value instanceof List) {
         return toJSON((List<?>) value);
      }
      else if (value instanceof Object[]) {
         return toJSON((Object[]) value);
      }
      else if (value instanceof Map) {
         return toJSON((Map<String, ?>) value);
      }
      else if (value instanceof JSONConvertible) {
         return toJSON((JSONConvertible) value);
      }
      //      else if (value instanceof ObjectId) {
      //         return toJSON((ObjectId) value);
      //      }
      else {
         throw new RuntimeException("Unsupported type: " + value.getClass());
      }
   }


   public static String toJSON(final JSONConvertible jsonConvertible) {
      return toJSON(jsonConvertible.toJSONObject());
   }


   //   public static String toJSON(final ObjectId id) {
   //      return toJSON(id.toHexString());
   //   }


   public static String toJSON(final Number number) {
      return number.toString();
   }


   public static String toJSON(final Boolean bool) {
      return bool.booleanValue() ? "true" : "false";
   }


   private static String quote(final String string) {
      if ((string == null) || (string.length() == 0)) {
         return "\"\"";
      }

      char c = 0;
      int i;
      final int len = string.length();
      final StringBuilder sb = new StringBuilder(len + 4);
      String t;

      sb.append('"');
      for (i = 0; i < len; i += 1) {
         c = string.charAt(i);
         switch (c) {
            case '\\':
            case '"':
               sb.append('\\');
               sb.append(c);
               break;
            case '/':
               //                if (b == '<') {
               sb.append('\\');
               //                }
               sb.append(c);
               break;
            case '\b':
               sb.append("\\b");
               break;
            case '\t':
               sb.append("\\t");
               break;
            case '\n':
               sb.append("\\n");
               break;
            case '\f':
               sb.append("\\f");
               break;
            case '\r':
               sb.append("\\r");
               break;
            default:
               if (c < ' ') {
                  t = "000" + Integer.toHexString(c);
                  sb.append("\\u" + t.substring(t.length() - 4));
               }
               else {
                  sb.append(c);
               }
         }
      }
      sb.append('"');
      return sb.toString();
   }


   public static String toJSON(final String string) {
      // return "\"" + string.replace("\"", "\\\"") + "\"";
      return quote(string);
   }


   public static String toJSON(final Map<String, ?> map) {
      final StringBuilder sb = new StringBuilder();

      sb.append('{');
      boolean first = true;
      for (final Map.Entry<String, ?> entry : map.entrySet()) {
         if (first) {
            first = false;
         }
         else {
            sb.append(',');
         }
         sb.append(toJSON(entry.getKey()));
         sb.append(':');
         sb.append(toJSON(entry.getValue()));
      }
      sb.append('}');

      return sb.toString();
   }


   public static String toJSON(final List<?> list) {
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      for (int i = 0; i < list.size(); i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(list.get(i)));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final Object[] array) {
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      final int length = array.length;
      for (int i = 0; i < length; i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(array[i]));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final String name,
                               final Object value) {
      return "{" + toJSON(name) + ":" + toJSON(value) + "}";
   }


}
