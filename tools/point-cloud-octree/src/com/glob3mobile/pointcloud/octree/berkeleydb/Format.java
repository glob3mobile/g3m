

package com.glob3mobile.pointcloud.octree.berkeleydb;


enum Format {
   LatLonHeight((byte) 1);


   final byte _formatID;


   Format(final byte formatID) {
      _formatID = formatID;
   }


   static Format getFromID(final byte formatID) {
      for (final Format each : Format.values()) {
         if (each._formatID == formatID) {
            return each;
         }
      }
      throw new RuntimeException("Invalid FormatID=" + formatID);
   }


}
