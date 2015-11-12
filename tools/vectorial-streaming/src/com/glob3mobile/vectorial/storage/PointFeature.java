

package com.glob3mobile.vectorial.storage;

import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;


public class PointFeature {

   public final Map<String, Object> _properties;
   public final Geodetic2D          _position;


   public PointFeature(final Map<String, Object> properties,
                       final Geodetic2D position) {
      _properties = properties;
      _position = position;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((_position == null) ? 0 : _position.hashCode());
      result = (prime * result) + ((_properties == null) ? 0 : _properties.hashCode());
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
      final PointFeature other = (PointFeature) obj;
      if (_position == null) {
         if (other._position != null) {
            return false;
         }
      }
      else if (!_position.equals(other._position)) {
         return false;
      }
      if (_properties == null) {
         if (other._properties != null) {
            return false;
         }
      }
      else if (!_properties.equals(other._properties)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      return "PointFeature position: " + _position + ", properties=" + _properties + "]";
   }


}
