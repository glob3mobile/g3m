

package com.glob3mobile.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class StringUtils {

   private static final int           KILO              = 1024;
   private static final long          MEGA              = KILO * KILO;
   private static final long          GIGA              = KILO * MEGA;
   private static final long          TERA              = KILO * GIGA;

   private static final DecimalFormat SPACE_FORMAT_BYTE = new DecimalFormat("##0.###");
   private static final DecimalFormat SPACE_FORMAT      = new DecimalFormat("##0.#");


   public static String getSpaceMessage(final double value) {
      if (Double.isNaN(value)) {
         return "NaN";
      }

      if (value < 16) {
         return SPACE_FORMAT_BYTE.format(value) + "B";
      }

      if (value < (KILO * 0.8)) {
         return SPACE_FORMAT.format(value) + "B";
      }

      if (value < (MEGA * 0.8)) {
         final double kilos = value / KILO;
         return SPACE_FORMAT.format(kilos) + "kB";
      }

      if (value < (GIGA * 0.8)) {
         final double megas = value / MEGA;
         return SPACE_FORMAT.format(megas) + "MB";
      }

      if (value < (TERA * 0.8)) {
         final double gigas = value / GIGA;
         return SPACE_FORMAT.format(gigas) + "GB";
      }

      final double teras = value / TERA;
      return SPACE_FORMAT.format(teras) + "TB";
   }


   public static String getTimeMessage(final long ms) {
      return getTimeMessage(ms, true);
   }


   public static String getTimeMessage(final long ms,
                                       final boolean rounded) {
      if (ms < 1000) {
         return ms + "ms";
      }

      if (ms < 60000) {
         final double seconds = ms / 1000d;
         return (rounded ? Long.toString(Math.round(seconds)) : Double.toString(seconds)) + "s";
      }

      long minutes = ms / 60000;
      if (minutes < 60) {
         final double seconds = (ms - (minutes * 60000d)) / 1000d;
         if (seconds <= 0) {
            return minutes + "m";
         }
         return minutes + "m " + (rounded ? Long.toString(Math.round(seconds)) : Double.toString(seconds)) + "s";
      }

      final long hours = minutes / 60;
      final double seconds = (ms - (minutes * 60000d)) / 1000d;
      minutes -= hours * 60;
      if (seconds <= 0) {
         return hours + "h " + minutes + "m";
      }
      return hours + "h " + minutes + "m " + (rounded ? Long.toString(Math.round(seconds)) : Double.toString(seconds)) + "s";
   }

   private static final Map<String, String> _largeStrings = new HashMap<String, String>();


   static String getSubstringOfStringRepeating(final String repeated,
                                               final int count) {
      synchronized (_largeStrings) {
         String large = _largeStrings.get(repeated);
         if (large == null) {
            large = createStringRepeating(repeated, 16 * KILO);
            _largeStrings.put(repeated, large);
         }
         return large.substring(0, Math.min(count, large.length() - 1));
      }
   }


   private static String createStringRepeating(final String repeated,
                                               final int count) {
      final StringBuilder builder = new StringBuilder(count * repeated.length());
      for (int i = 0; i < count; i++) {
         builder.append(repeated);
      }
      return builder.toString();
   }

}
