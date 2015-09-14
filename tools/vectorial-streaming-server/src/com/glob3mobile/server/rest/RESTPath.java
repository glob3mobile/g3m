

package com.glob3mobile.server.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class RESTPath {


   private static final RESTPath EMPTY = new RESTPath(Collections.<String> emptyList());


   public static RESTPath create(final HttpServletRequest request) {
      final String path = request.getPathInfo();

      if ((path == null) || path.isEmpty()) {
         return EMPTY;
      }

      final String[] parts = path.split("/");

      final List<String> result = new ArrayList<String>(parts.length);

      for (final String part : parts) {
         final String trimmedPart = part.trim();
         if (!trimmedPart.isEmpty()) {
            result.add(trimmedPart);
         }
      }

      return new RESTPath(Collections.unmodifiableList(result));
   }


   private final List<String> _path;


   private RESTPath(final List<String> path) {
      _path = path;
   }


   public boolean isEmpty() {
      return _path.isEmpty();
   }


   public String getLast() {
      return _path.get(_path.size() - 1);
   }


   public RESTPath removeFirst() {
      return new RESTPath(_path.subList(1, _path.size()));
   }


   public RESTPath removeLast() {
      return new RESTPath(_path.subList(0, _path.size() - 1));
   }


   @Override
   public String toString() {
      return _path.toString();
   }


   public int size() {
      return _path.size();
   }


   public String getFirst() {
      return _path.get(0);
   }


   public String get(final int i) {
      return _path.get(i);
   }


}
