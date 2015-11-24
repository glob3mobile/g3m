

package com.glob3mobile.vectorial.storage;

import com.glob3mobile.geo.Geodetic2D;


public class PointFeatureCluster {
   public final Geodetic2D _position;
   public final long       _size;


   public PointFeatureCluster(final Geodetic2D position,
                              final long size) {
      _position = position;
      _size = size;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((_position == null) ? 0 : _position.hashCode());
      result = (prime * result) + (int) (_size ^ (_size >>> 32));
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
      final PointFeatureCluster other = (PointFeatureCluster) obj;
      if (_position == null) {
         if (other._position != null) {
            return false;
         }
      }
      else if (!_position.equals(other._position)) {
         return false;
      }
      if (_size != other._size) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      return "[PointFeatureCluster position=" + _position + ", size=" + _size + "]";
   }


}
