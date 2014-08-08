

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.util.Arrays;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.Utils;


public class MercatorTile {


   private static final Byte[] ROOT_ID = {};


   public static MercatorTile deepestEnclosingTile(final Sector targetSector) {
      return deepestEnclosingTile(root(), targetSector);
   }


   private static MercatorTile deepestEnclosingTile(final MercatorTile candidate,
                                                    final Sector targetSector) {
      final MercatorTile[] children = candidate.createChildren();
      for (final MercatorTile child : children) {
         if (child._sector.fullContains(targetSector)) {
            return deepestEnclosingTile(child, targetSector);
         }
      }
      return candidate;
   }


   private static double _upperLimitInDegrees = 85.0511287798;
   private static double _lowerLimitInDegrees = -85.0511287798;


   private static double getMercatorV(final Angle latitude) {
      if (latitude._degrees >= _upperLimitInDegrees) {
         return 0;
      }
      if (latitude._degrees <= _lowerLimitInDegrees) {
         return 1;
      }

      final double pi4 = Math.PI * 4;

      final double latSin = Math.sin(latitude._radians);
      return 1.0 - ((Math.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
   }


   private static Angle toLatitude(final double v) {
      final double exp = Math.exp(-2 * Math.PI * (1.0 - v - 0.5));
      final double atan = Math.atan(exp);
      return Angle.fromRadians((Math.PI / 2) - (2 * atan));
   }


   private static Angle calculateSplitLatitude(final Angle lowerLatitude,
                                               final Angle upperLatitude) {
      final double middleV = (getMercatorV(lowerLatitude) + getMercatorV(upperLatitude)) / 2;

      return toLatitude(middleV);
   }


   private static MercatorTile root() {
      return new MercatorTile(ROOT_ID, Sector.FULL_SPHERE);
   }


   private final Byte[] _id;
   private final Sector _sector;


   private MercatorTile[] createChildren() {
      final Geodetic2D lower = _sector._lower;
      final Geodetic2D upper = _sector._upper;

      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
      final Angle splitLatitude = calculateSplitLatitude(lower._latitude, upper._latitude);

      final Sector s0 = new Sector( //
               new Geodetic2D(splitLatitude, lower._longitude), //
               new Geodetic2D(upper._latitude, splitLongitude));

      final Sector s1 = new Sector( //
               new Geodetic2D(splitLatitude, splitLongitude), //
               new Geodetic2D(upper._latitude, upper._longitude));

      final Sector s2 = new Sector( //
               new Geodetic2D(lower._latitude, lower._longitude), //
               new Geodetic2D(splitLatitude, splitLongitude));

      final Sector s3 = new Sector( //
               new Geodetic2D(lower._latitude, splitLongitude), //
               new Geodetic2D(splitLatitude, upper._longitude));


      final MercatorTile child0 = createChild((byte) 0, s0);
      final MercatorTile child1 = createChild((byte) 1, s1);
      final MercatorTile child2 = createChild((byte) 2, s2);
      final MercatorTile child3 = createChild((byte) 3, s3);
      return new MercatorTile[] { child0, child1, child2, child3 };
   }


   private MercatorTile createChild(final byte index,
                                    final Sector sector) {
      final int length = getLevel();
      final Byte[] childId = new Byte[length + 1];
      System.arraycopy(_id, 0, childId, 0, length);
      childId[length] = index;
      return new MercatorTile(childId, sector);
   }


   private MercatorTile(final Byte[] id,
                        final Sector sector) {
      _id = id;
      _sector = sector;
   }


   public int getLevel() {
      return _id.length;
   }


   public Byte[] getID() {
      return Arrays.copyOf(_id, _id.length);
   }


   public String getIDString() {
      final StringBuilder builder = new StringBuilder();
      for (final byte each : _id) {
         builder.append(each);
      }
      return builder.toString();
   }


   public Sector getSector() {
      return _sector;
   }


   @Override
   public String toString() {
      return "MercatorTile [id=" + getIDString() + ", sector=" + Utils.toString(_sector) + ", level=" + getLevel() + "]";
   }


}
