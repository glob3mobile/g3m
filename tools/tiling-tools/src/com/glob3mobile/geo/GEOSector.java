

package com.glob3mobile.geo;

import java.awt.geom.Point2D;

import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class GEOSector {

   private static final GEOSector FULL_SPHERE = new GEOSector(new GEOGeodetic(-90, -180), new GEOGeodetic(90, 180));


   public static GEOSector fullSphere() {
      return FULL_SPHERE;
   }

   public final GEOGeodetic _lower;
   public final GEOGeodetic _upper;
   public final GEOGeodetic _delta;
   public final GEOGeodetic _center;


   public GEOSector(final GEOGeodetic lower,
                    final GEOGeodetic upper) {
      _lower = lower;
      _upper = upper;
      _delta = _upper.sub(_lower);
      _center = new GEOGeodetic( //
               (_lower._latitude + _upper._latitude) / 2, //
               (_lower._longitude + _upper._longitude) / 2);
   }


   public GEOSector(final Envelope envelope) {
      final CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
      _lower = new GEOGeodetic(crs, envelope.getLowerCorner());
      _upper = new GEOGeodetic(crs, envelope.getUpperCorner());
      _delta = _upper.sub(_lower);
      _center = new GEOGeodetic( //
               (_lower._latitude + _upper._latitude) / 2, //
               (_lower._longitude + _upper._longitude) / 2);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + _lower.hashCode();
      result = (prime * result) + _upper.hashCode();
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
      final GEOSector other = (GEOSector) obj;
      if (_lower == null) {
         if (other._lower != null) {
            return false;
         }
      }
      else if (!_lower.equals(other._lower)) {
         return false;
      }
      if (_upper == null) {
         if (other._upper != null) {
            return false;
         }
      }
      else if (!_upper.equals(other._upper)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[lower=");
      builder.append(_lower);
      builder.append(", upper=");
      builder.append(_upper);
      //builder.append(", delta=");
      //builder.append(_delta);
      builder.append("]");
      return builder.toString();
   }


   public final boolean touchesWith(final GEOSector that) {
      if ((_upper._latitude < that._lower._latitude) || (_lower._latitude > that._upper._latitude)) {
         return false;
      }
      if ((_upper._longitude < that._lower._longitude) || (_lower._longitude > that._upper._longitude)) {
         return false;
      }

      // Overlapping on all axes means Sectors are intersecting
      return true;
   }


   public Point2D getUVCoordinates(final GEOGeodetic position) {
      return getUVCoordinates(position._latitude, position._longitude);
   }


   public Point2D getUVCoordinates(final double latitude,
                                   final double longitude) {
      return new Point2D.Double( //
               (longitude - _lower._longitude) / _delta._longitude, //
               (_upper._latitude - latitude) / _delta._latitude);
   }


}
