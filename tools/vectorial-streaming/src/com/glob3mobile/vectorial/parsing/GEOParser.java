

package com.glob3mobile.vectorial.parsing;

import java.io.File;
import java.io.IOException;

import com.glob3mobile.geo.Sector;


public interface GEOParser {


   public static class Statistics {
      public final long   _featuresCount;
      public final Sector _boundingSector;


      protected Statistics(final long featuresCount,
                           final Sector boundingSector) {
         _featuresCount = featuresCount;
         _boundingSector = boundingSector;
      }

   }


   long countFeatures(File file,
                      boolean showProgress) throws IOException, GEOParseException;


   <E extends Exception> void parse(File file,
                                    GEOFeatureHandler<E> handler) throws IOException, GEOParseException, E;


}
