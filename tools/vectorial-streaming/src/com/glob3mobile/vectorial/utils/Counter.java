

package com.glob3mobile.vectorial.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Counter {


   private final String            _name;
   private final Map<String, Long> _map = new HashMap<>();


   public Counter(final String name) {
      _name = name;
   }


   public void count(final String attribute) {
      final Long count = _map.get(attribute);
      if (count == null) {
         _map.put(attribute, 1L);
      }
      else {
         _map.put(attribute, count + 1);
      }
   }


   public void show() {
      final List<Map.Entry<String, Long>> entries = new ArrayList<>(_map.entrySet());
      Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
         @Override
         public int compare(final Entry<String, Long> o1,
                            final Entry<String, Long> o2) {
            return Long.compare(o2.getValue(), o1.getValue());
         }
      });

      System.out.println("---------------------------------------------------------");
      System.out.println(" " + _name + ":");
      for (final Map.Entry<String, Long> entry : entries) {
         System.out.println("   " + entry.getKey() + ": " + entry.getValue());
      }
      System.out.println("---------------------------------------------------------");
   }


}
