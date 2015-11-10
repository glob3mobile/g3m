

package com.glob3mobile.vectorial.utils;

import com.glob3mobile.geo.Sector;


public class SectorUtils {


   private SectorUtils() {
   }


   private static Sector createSector(final double minLatitudeInDegrees,
                                      final double minLongitudeInDegrees,
                                      final double maxLatitudeInDegrees,
                                      final double maxLongitudeInDegrees) {
      return Sector.fromDegrees( //
               Math.max(-180, minLatitudeInDegrees), //
               Math.max(-360, minLongitudeInDegrees), //
               Math.min(180, maxLatitudeInDegrees), //
               Math.min(360, maxLongitudeInDegrees));
   }


   public static Sector getRootSector(final Sector sector) {
      final double deltaLat = sector._deltaLatitude._degrees;
      final double deltaLon = sector._deltaLongitude._degrees;

      if ((deltaLat > 160) || (deltaLon > 320)) {
         return Sector.FULL_SPHERE;
      }

      final double lowerLat = sector._lower._latitude._degrees;
      final double lowerLon = sector._lower._longitude._degrees;
      final double upperLat = sector._upper._latitude._degrees;
      final double upperLon = sector._upper._longitude._degrees;


      if (deltaLat > deltaLon) {
         final double offsetLon = (deltaLat - deltaLon) / 2;
         return createSector( //
                  lowerLat, lowerLon - offsetLon, //
                  upperLat, upperLon + offsetLon);
      }

      final double offsetLat = (deltaLon - deltaLat) / 2;
      return createSector( //
               lowerLat - offsetLat, lowerLon, //
               upperLat + offsetLat, upperLon);
   }


   private static void test(final Sector sector) {
      final Sector rootSector = getRootSector(sector);
      final float ratio = (float) (rootSector._deltaLatitude._radians / rootSector._deltaLongitude._radians);
      System.out.println(sector + //
                         "\n\t" + rootSector + //
                         "\n\tdeltaLatitude=" + rootSector._deltaLatitude + //
                         "\n\tdeltaLongitude=" + rootSector._deltaLongitude + //
                         "\n\tratio=" + ratio + //
                         "\n");
   }


   public static void main(final String[] args) {

      final Sector allCountriesSector = Sector.fromDegrees(-90.0, -179.98359999999997, 90.0, 180.0);
      test(allCountriesSector);

      final Sector usSector = Sector.fromDegrees(16.86667, -179.46806, 71.388, 179.98333);
      test(usSector);

      final Sector noSector = Sector.fromDegrees(56.14999999999999, -12.999999999999998, 80.1731, 36.69273);
      test(noSector);

      final Sector mxSector = Sector.fromDegrees(14.535879999999999, -119.92167, 45.0, -85.74999999999999);
      test(mxSector);

      final Sector arSector = Sector.fromDegrees(-58.11667, -73.5, -21.78333, -53.55);
      test(arSector);

   }


}
