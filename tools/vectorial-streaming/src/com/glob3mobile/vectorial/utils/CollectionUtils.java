

package com.glob3mobile.vectorial.utils;

import java.util.Collections;
import java.util.List;


public class CollectionUtils {
   private CollectionUtils() {
   }


   public static <T> List<T> pruneTrailingNulls(final List<T> list) {
      for (int i = list.size() - 1; i >= 0; i--) {
         final T e = list.get(i);
         if (e != null) {
            return list.subList(0, i + 1);
         }
      }
      return Collections.emptyList();
   }


   //   public static void main(final String[] args) {
   //      final List<String> list = Collections.emptyList();
   //      // final List<String> list = Arrays.asList("A", "B", "C");
   //      // final List<String> list = Arrays.asList("A", "B", "C", null, "E", null, null);
   //      // final List<String> list = Arrays.asList(null, null, null);
   //
   //      System.out.println(pruneTrailingNulls(list));
   //   }


}
