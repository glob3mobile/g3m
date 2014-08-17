

package com.glob3mobile.pointcloud.octree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GMath;


public class Utils {

   private Utils() {
   }


   public static int compare(final byte[] left,
                             final byte[] right) {
      final int leftLength = left.length;
      final int rightLength = right.length;
      final int compareLenght = Math.min(leftLength, rightLength);
      for (int i = 0; i < compareLenght; i++) {
         final int leftValue = (left[i] & 0xff);
         final int rightValue = (right[i] & 0xff);
         if (leftValue != rightValue) {
            return leftValue - rightValue;
         }
      }
      return leftLength - rightLength;
   }


   public static boolean isGreaterThan(final byte[] left,
                                       final byte[] right) {
      return compare(left, right) > 0;
   }


   public static boolean isEquals(final byte[] left,
                                  final byte[] right) {
      return compare(left, right) == 0;
   }


   public static boolean hasSamePrefix(final byte[] left,
                                       final byte[] right) {
      final int len = Math.min(left.length, right.length);
      for (int i = 0; i < len; i++) {
         if (left[i] != right[i]) {
            return false;
         }
      }
      return true;
   }


   public static String toIDString(final byte[] id) {
      final StringBuilder builder = new StringBuilder();
      for (final byte each : id) {
         builder.append(each);
      }
      return builder.toString();
   }


   public static byte[] toBinaryID(final String id) {
      final int length = id.length();
      final byte[] result = new byte[length];
      for (int i = 0; i < length; i++) {
         result[i] = Byte.parseByte(Character.toString(id.charAt(i)));
      }
      return result;
   }


   public static List<byte[]> getPathFromRoot(final byte[] id) {
      final int length = id.length;
      final List<byte[]> result = new ArrayList<byte[]>(length + 1);
      int parentIDLenght = 0;
      while (parentIDLenght <= length) {
         final byte[] parentID = Arrays.copyOf(id, parentIDLenght);
         result.add(parentID);
         parentIDLenght++;
      }
      return result;
   }


   static final GColorF[] RAMP = new GColorF[] { GColorF.CYAN, GColorF.GREEN, GColorF.YELLOW, GColorF.RED };


   public static GColorF interpolateColorFromRamp(final GColorF colorFrom,
                                                  final GColorF[] ramp,
                                                  final float alpha) {
      final float rampStep = 1f / ramp.length;

      final int toI;
      if (GMath.closeTo(alpha, 1)) {
         toI = ramp.length - 1;
      }
      else {
         toI = (int) (alpha / rampStep);
      }

      final GColorF from;
      if (toI == 0) {
         from = colorFrom;
      }
      else {
         from = ramp[toI - 1];
      }

      final float colorAlpha = (alpha % rampStep) / rampStep;
      return from.mixedWidth(ramp[toI], colorAlpha);
   }


   public static Color toAWTColor(final GColorF color) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), 1);
   }


   public static byte[] removeTrailing(final byte[] id) {
      final int length = id.length;
      if (length == 0) {
         return null;
      }
      return Arrays.copyOf(id, length - 1);
   }


}
