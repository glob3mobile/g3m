

package com.glob3mobile.vectorial.parsing;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiPredicate;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.GEOGeometry;
import com.glob3mobile.vectorial.GEOPoint;


public class GEOStatisticsGatherer
   implements
      GEOFeatureHandler<RuntimeException> {


   public static class Statistics {
      public final long       _featuresCount;
      public final Geodetic2D _averagePosition;
      public final Sector     _boundingSector;


      private Statistics(final long featuresCount,
                         final Geodetic2D averagePosition,
                         final Sector boundingSector) {
         _featuresCount = featuresCount;
         _averagePosition = averagePosition;
         _boundingSector = boundingSector;
      }
   }


   public static GEOStatisticsGatherer.Statistics getStatistics(final GEOParser parser,
                                                                final File file,
                                                                final BiPredicate<Map<String, Object>, Geodetic2D> filter)
                                                                                                                          throws IOException,
                                                                                                                          GEOParseException {

      final long featuresCount = parser.countFeatures(file, true);
      System.out.println();
      final GEOStatisticsGatherer gatherer = new GEOStatisticsGatherer(file.getName(), featuresCount, filter);
      parser.parse(file, gatherer);
      return gatherer.getStatistics();
   }


   private final String                                       _name;
   private final long                                         _featuresCount;
   private final BiPredicate<Map<String, Object>, Geodetic2D> _filter;

   private Progress                                           _progress;

   private double                                             _sumLatRadians;
   private double                                             _sumLonRadians;
   private double                                             _minLatRadians;
   private double                                             _maxLatRadians;
   private double                                             _minLonRadians;
   private double                                             _maxLonRadians;


   private GEOStatisticsGatherer(final String name,
                                 final long featuresCount,
                                 final BiPredicate<Map<String, Object>, Geodetic2D> filter) {
      _name = name;
      _featuresCount = featuresCount;
      _filter = filter;
   }


   private GEOStatisticsGatherer.Statistics getStatistics() {
      final Geodetic2D averagePosition = Geodetic2D.fromRadians(_sumLatRadians / _featuresCount, _sumLonRadians / _featuresCount);
      final Sector boundingSector = Sector.fromRadians(_minLatRadians, _minLonRadians, _maxLatRadians, _maxLonRadians);
      return new GEOStatisticsGatherer.Statistics(_featuresCount, averagePosition, boundingSector);
   }


   @Override
   public void onStart() {
      _progress = new Progress(_featuresCount) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println(_name + ": Gathering Statistics: "
                               + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };

      _sumLatRadians = 0;
      _sumLonRadians = 0;
      _minLatRadians = Double.POSITIVE_INFINITY;
      _maxLatRadians = Double.NEGATIVE_INFINITY;
      _minLonRadians = Double.POSITIVE_INFINITY;
      _maxLonRadians = Double.NEGATIVE_INFINITY;
   }


   @Override
   public void onFinish() {
      _progress.finish();
      _progress = null;
   }


   @Override
   public void onFinishWithException() {
      _progress.finish();
      _progress = null;
   }


   @Override
   public void onError(final Map<String, Object> properties,
                       final GEOGeometry geometry) {
   }


   @Override
   public void onFeature(final Map<String, Object> properties,
                         final GEOGeometry geometry) {

      final Geodetic2D position = ((GEOPoint) geometry)._position;

      if ((_filter == null) || _filter.test(properties, position)) {
         final double lat = position._latitude._radians;
         final double lon = position._longitude._radians;

         _sumLatRadians += lat;
         _sumLonRadians += lon;

         _minLatRadians = Math.min(_minLatRadians, lat);
         _maxLatRadians = Math.max(_maxLatRadians, lat);
         _minLonRadians = Math.min(_minLonRadians, lon);
         _maxLonRadians = Math.max(_maxLonRadians, lon);
      }

      _progress.stepDone();
   }


   public static void main(final String[] args) throws IOException, GEOParseException {
      System.out.println("GEOStatisticsGatherer 0.1");
      System.out.println("-------------------------\n");

      final GEOParser parser = GEONamesParser.INSTANCE;
      final String featuresFileName = "test-files/geonames/ES.txt";
      // final String featuresFileName = "test-files/geonames/MX.txt";
      // final String featuresFileName = "test-files/geonames/US.txt";
      // final String featuresFileName = "test-files/geonames/cities1000.txt";
      // final String featuresFileName = "test-files/geonames/AR.txt";
      // final String featuresFileName = "test-files/geonames/NO.txt";
      // final String featuresFileName = "test-files/geonames/allCountries.txt";

      final File file = new File(featuresFileName);

      final GEOStatisticsGatherer.Statistics statistics = GEOStatisticsGatherer.getStatistics(parser, file, null);

      System.out.println("   Features Count: " + statistics._featuresCount);
      System.out.println(" Average Position: " + statistics._averagePosition);
      System.out.println("  Bounding Sector: " + statistics._boundingSector);

      System.out.println("\n- done!");
   }


}
