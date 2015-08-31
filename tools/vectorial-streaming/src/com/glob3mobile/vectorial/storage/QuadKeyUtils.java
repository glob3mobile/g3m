

package com.glob3mobile.vectorial.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuadKeyUtils {


   private QuadKeyUtils() {
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


   public static byte[] removeTrailing(final byte[] id) {
      final int length = id.length;
      if (length == 0) {
         return null;
      }
      return Arrays.copyOf(id, length - 1);
   }


   public static byte[] append(final byte[] id,
                               final byte b) {
      final byte[] newID = Arrays.copyOf(id, id.length + 1);
      newID[id.length] = b;
      return newID;
   }


   public static List<byte[]> ancestors(final byte[] key) {
      final List<byte[]> result = new ArrayList<>(key.length);
      byte[] currentKey = Arrays.copyOf(key, key.length);
      while (currentKey.length >= 1) {
         final byte[] parentKey = removeTrailing(currentKey);
         result.add(parentKey);
         currentKey = parentKey;
      }
      return result;
   }


   //   public static void main(final String[] args) {
   //      final List<byte[]> ancestors = ancestors(new byte[] { 0, 1, 2, 3, 4, 5, 6 });
   //      for (final byte[] ancestor : ancestors) {
   //         System.out.println(Arrays.toString(ancestor));
   //      }
   //   }


}
