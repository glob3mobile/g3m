

package com.glob3mobile.utils;

import java.util.ArrayList;
import java.util.List;


public class CollectionsUtils {
   private CollectionsUtils() {
   }


   public static <S, R> List<R> map(final List<S> source,
                                    final Function<S, R> function) {
      if (source == null) {
         return null;
      }
      final List<R> result = new ArrayList<>(source.size());
      for (final S s : source) {
         result.add(function.evaluate(s));
      }
      return result;
   }

}
