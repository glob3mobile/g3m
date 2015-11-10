

package com.glob3mobile.vectorial.utils;

import java.io.IOException;

import org.mapdb.BTreeMap;

import com.glob3mobile.geo.Sector;


public class MapDBUtils {


   private MapDBUtils() {
   }


   public static Sector readSector(final BTreeMap<String, Object> metadata,
                                   final String name) throws IOException {

      final double minLatitudeInRadians = readDouble(metadata, name + "_minLatitudeInRadians");
      final double minLongitudeInRadians = readDouble(metadata, name + "_minLongitudeInRadians");
      final double maxLatitudeInRadians = readDouble(metadata, name + "_maxLatitudeInRadians");
      final double maxLongitudeInRadians = readDouble(metadata, name + "_maxLongitudeInRadians");
      return Sector.fromRadians(//
               minLatitudeInRadians, minLongitudeInRadians, //
               maxLatitudeInRadians, maxLongitudeInRadians);
   }


   public static void saveSector(final BTreeMap<String, Object> metadata,
                                 final String name,
                                 final Sector sector) {
      saveDouble(metadata, name + "_minLatitudeInRadians", sector._lower._latitude._radians);
      saveDouble(metadata, name + "_minLongitudeInRadians", sector._lower._longitude._radians);
      saveDouble(metadata, name + "_maxLatitudeInRadians", sector._upper._latitude._radians);
      saveDouble(metadata, name + "_maxLongitudeInRadians", sector._upper._longitude._radians);
   }


   public static void saveDouble(final BTreeMap<String, Object> metadata,
                                 final String name,
                                 final double value) {
      metadata.put(name, Double.valueOf(value));
   }


   public static double readDouble(final BTreeMap<String, Object> metadata,
                                   final String name) throws IOException {
      final Object value = metadata.get(name);
      if (value == null) {
         throw new IOException("Mandatory double \"" + name + "\" is not present");
      }
      if (!(value instanceof Double)) {
         throw new IOException("Mandatory double \"" + name + "\" has wrong value (" + value.getClass() + ")");
      }
      return ((Double) value).doubleValue();
   }


   public static int readInt(final BTreeMap<String, Object> metadata,
                             final String name) throws IOException {
      final Object value = metadata.get(name);
      if (value == null) {
         throw new IOException("Mandatory integer \"" + name + "\" is not present");
      }
      if (!(value instanceof Integer)) {
         throw new IOException("Mandatory integer \"" + name + "\" has wrong value (" + value.getClass() + ")");
      }
      return ((Integer) value).intValue();
   }


   public static void saveInt(final BTreeMap<String, Object> metadata,
                              final String name,
                              final int value) {
      metadata.put(name, Integer.valueOf(value));
   }


}
