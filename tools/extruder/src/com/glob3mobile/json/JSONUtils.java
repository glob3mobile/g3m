

package com.glob3mobile.json;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


public class JSONUtils {
   private JSONUtils() {
   }


   @SuppressWarnings("unchecked")
   public static String toJSON(final Object value,
                               final int floatPrecision) {
      if (value == null) {
         return "null";
      }
      else if (value instanceof String) {
         return toJSON((String) value);
      }
      else if (value instanceof Number) {
         return toJSON((Number) value, floatPrecision);
      }
      else if (value instanceof Boolean) {
         return toJSON((Boolean) value);
      }
      else if (value instanceof List) {
         return toJSON((List<?>) value, floatPrecision);
      }
      else if (value instanceof Object[]) {
         return toJSON((Object[]) value, floatPrecision);
      }
      else if (value instanceof Map) {
         return toJSON((Map<String, ?>) value, floatPrecision);
      }
      else {
         throw new RuntimeException("Unsupported type: " + value.getClass());
      }
   }


   public static String toJSON(final Number value,
                               final int floatPrecision) {
      if (value instanceof Double) {
         return toJSON(value.doubleValue(), floatPrecision);
      }
      else if (value instanceof Float) {
         return toJSON(value.floatValue(), floatPrecision);
      }
      else {
         return value.toString();
      }
   }


   private static final DecimalFormat DF = new DecimalFormat("0");


   public static String toJSON(final float value,
                               final int floatPrecision) {
      synchronized (DF) {
         DF.setMaximumFractionDigits(floatPrecision);
         return DF.format(value);
      }
   }


   public static String toJSON(final double value,
                               final int floatPrecision) {
      synchronized (DF) {
         DF.setMaximumFractionDigits(floatPrecision);
         return DF.format(value);
      }
   }


   public static String toJSON(final Boolean value) {
      if (value == null) {
         return "null";
      }
      return value.booleanValue() ? "true" : "false";
   }


   public static String toJSON(final boolean value) {
      return value ? "true" : "false";
   }


   private static String quote(final String value) {
      if (value == null) {
         return "null";
      }
      if (value.isEmpty()) {
         return "\"\"";
      }

      char c = 0;
      int i;
      final int len = value.length();
      final StringBuilder sb = new StringBuilder(len + 4);
      String t;

      sb.append('"');
      for (i = 0; i < len; i += 1) {
         c = value.charAt(i);
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


   public static String toJSON(final String value) {
      return quote(value);
   }


   public static String toJSON(final Map<String, ?> value,
                               final int floatPrecision) {
      if (value == null) {
         return "null";
      }
      final StringBuilder sb = new StringBuilder();

      sb.append('{');
      boolean first = true;
      for (final Map.Entry<String, ?> entry : value.entrySet()) {
         if (first) {
            first = false;
         }
         else {
            sb.append(',');
         }
         sb.append(toJSON(entry.getKey()));
         sb.append(':');
         sb.append(toJSON(entry.getValue(), floatPrecision));
      }
      sb.append('}');

      return sb.toString();
   }


   public static String toJSON(final List<?> value,
                               final int floatPrecision) {
      if (value == null) {
         return "null";
      }
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      for (int i = 0; i < value.size(); i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(value.get(i), floatPrecision));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final Object[] value,
                               final int floatPrecision) {
      if (value == null) {
         return "null";
      }
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      final int length = value.length;
      for (int i = 0; i < length; i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(value[i], floatPrecision));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final String name,
                               final Object value,
                               final int floatPrecision) {
      return "{" + toJSON(name) + ":" + toJSON(value, floatPrecision) + "}";
   }

}
