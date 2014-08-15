

package com.glob3mobile.pointcloud.octree;


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


   public static void main(final String[] args) {
      final byte[] left = new byte[] { 1, 2, 3, 1 };
      final byte[] right = new byte[] { 1, 22, 3 };
      //System.out.println(isGreaterThan(left, right));
      System.out.println(hasSamePrefix(left, right));
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

}
