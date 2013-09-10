

package org.glob3.mobile.utils;

import org.glob3.mobile.generated.Color;


public class ColorUtils {


   public static double normalize(final double value,
                                  final double max,
                                  final double min,
                                  final double new_max,
                                  final double new_min) {
      return (((value - min) / (max - min)) * (new_max - new_min)) + new_min;
   }


   public static Color fromRGBAInt(final int r,
                                   final int g,
                                   final int b,
                                   final float A) {
      return Color.fromRGBA((float) normalize(r, 255, 0, 1, 0), (float) normalize(g, 255, 0, 1, 0),
               (float) normalize(b, 255, 0, 1, 0), A);
   }


   public static int toAndroidColor(final Color c) {
      return android.graphics.Color.argb(Math.round(c.getAlpha() * 255), Math.round(c.getRed() * 255),
               Math.round(c.getGreen() * 255), Math.round(c.getBlue() * 255));
   }


}
