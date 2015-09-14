

package com.glob3mobile.vectorial.storage;

import java.util.Arrays;

import com.glob3mobile.geo.Angle;
import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class QuadKey {


   //   public static final QuadKey ROOT_QUADKEY_MERCATOR = new QuadKey(new byte[0], Sector.FULL_SPHERE, true);
   //   public static final QuadKey ROOT_QUADKEY_WGS84    = new QuadKey(new byte[0], Sector.FULL_SPHERE, false);

   private static final byte B0 = (byte) 0;
   private static final byte B1 = (byte) 1;
   private static final byte B2 = (byte) 2;
   private static final byte B3 = (byte) 3;


   public static QuadKey deepestEnclosingNodeKey(final QuadKey candidate,
                                                 final Sector targetSector) {
      final QuadKey[] children = candidate.createChildren();
      for (final QuadKey child : children) {
         if (child._sector.fullContains(targetSector)) {
            return deepestEnclosingNodeKey(child, targetSector);
         }
      }
      return candidate;
   }


   public static QuadKey deepestEnclosingNodeKey(final QuadKey candidate,
                                                 final Geodetic2D position,
                                                 final int maxLevel) {
      if (candidate.getLevel() < maxLevel) {
         final QuadKey[] children = candidate.createChildren();
         for (final QuadKey child : children) {
            if (child._sector.contains(position)) {
               return deepestEnclosingNodeKey(child, position, maxLevel);
            }
         }
      }
      return candidate;
   }


   public final byte[] _id;
   public final Sector _sector;


   public QuadKey(final byte[] id,
                  final Sector sector) {
      _id = id;
      _sector = sector;

   }


   public QuadKey[] createChildren() {
      if (isAlmostSquared()) {
         return new QuadKey[] { createChild(B0), createChild(B1), createChild(B2), createChild(B3) };
      }
      return new QuadKey[] { createChild(B0), createChild(B1) };
   }


   private Sector createSectorForChild(final Sector sector,
                                       final byte index) {
      final Geodetic2D lower = sector._lower;
      final Geodetic2D upper = sector._upper;


      if (isAlmostSquared()) {
         final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
         final Angle splitLatitude = Angle.midAngle(lower._latitude, upper._latitude);

         switch (index) {
            case 0:
               return new Sector( //
                        splitLatitude, lower._longitude, //
                        upper._latitude, splitLongitude);
            case 1:
               return new Sector( //
                        splitLatitude, splitLongitude, //
                        upper._latitude, upper._longitude);
            case 2:
               return new Sector( //
                        lower._latitude, lower._longitude, //
                        splitLatitude, splitLongitude);
            case 3:
               return new Sector( //
                        lower._latitude, splitLongitude, //
                        splitLatitude, upper._longitude);
            default:
               throw new RuntimeException("Invalid index=" + index);
         }
      }

      if (sector._deltaLongitude._radians > sector._deltaLatitude._radians) {
         final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
         switch (index) {
            case 0:
               return new Sector( //
                        lower._latitude, lower._longitude, //
                        upper._latitude, splitLongitude);
            case 1:
               return new Sector( //
                        lower._latitude, splitLongitude, //
                        upper._latitude, upper._longitude);
            default:
               throw new RuntimeException("Invalid index=" + index);
         }
      }

      final Angle splitLatitude = Angle.midAngle(lower._latitude, upper._latitude);
      switch (index) {
         case 0:
            return new Sector( //
                     lower._latitude, lower._longitude, //
                     splitLatitude, upper._longitude);
         case 1:
            return new Sector( //
                     splitLatitude, lower._longitude, //
                     upper._latitude, upper._longitude);
         default:
            throw new RuntimeException("Invalid index=" + index);
      }

   }


   private boolean isAlmostSquared() {
      final double ratio = _sector._deltaLatitude._radians / _sector._deltaLongitude._radians;
      return (ratio > 0.75) && (ratio < 1.25);
   }


   private QuadKey createChild(final byte index) {
      final int length = _id.length;
      final byte[] childId = new byte[length + 1];
      System.arraycopy(_id, 0, childId, 0, length);
      childId[length] = index;
      return new QuadKey(childId, createSectorForChild(_sector, index));
   }


   public int getLevel() {
      return _id.length;
   }


   @Override
   public String toString() {
      return "[QuadKey id=" + QuadKeyUtils.toIDString(_id) + ", sector=" + _sector + "]";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + Arrays.hashCode(_id);
      result = (prime * result) + ((_sector == null) ? 0 : _sector.hashCode());
      return result;
   }


   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final QuadKey other = (QuadKey) obj;
      if (!Arrays.equals(_id, other._id)) {
         return false;
      }
      if (_sector == null) {
         if (other._sector != null) {
            return false;
         }
      }
      else if (!_sector.equals(other._sector)) {
         return false;
      }
      return true;
   }


   public static Sector sectorFor(final QuadKey root,
                                  final byte[] id) {
      return quadKeyFor(root, id)._sector;
   }


   private static QuadKey quadKeyFor(final QuadKey root,
                                     final byte[] id) {
      QuadKey current = root;
      for (final byte b : id) {
         current = current.createChild(b);
      }
      return current;
   }


}
