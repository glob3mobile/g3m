

package com.glob3mobile.pointcloud.octree.berkeleydb;


enum Format {
   LatLonHeight((byte) 1);

   final byte _formatID;


   Format(final byte formatID) {
      _formatID = formatID;
   }


   //      private int sizeOf(final float[] values) {
   //         return values.length * 4;
   //      }


   //   int sizeOf(final double[] values) {
   //      return values.length * 8;
   //   }


   static Format getFromID(final byte formatID) {
      for (final Format each : Format.values()) {
         if (each._formatID == formatID) {
            return each;
         }
      }
      throw new RuntimeException("Invalid FormatID=" + formatID);
   }


}
