

package com.glob3mobile.geo;

import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


public class GEOGeodetic {
   public final double _latitude;
   public final double _longitude;


   public GEOGeodetic(final double latitude,
                      final double longitude) {
      _latitude = latitude;
      _longitude = longitude;
   }


   GEOGeodetic(final CoordinateReferenceSystem crs,
               final DirectPosition directPosition) {
      try {
         final CoordinateReferenceSystem wsg84 = CRS.decode("EPSG:4326");

         final boolean lenient = true;
         final MathTransform transform = CRS.findMathTransform(crs, wsg84, lenient);

         final DirectPosition reprojectedDirectPosition = transform.transform(directPosition, null);
         _latitude = reprojectedDirectPosition.getOrdinate(0);
         _longitude = reprojectedDirectPosition.getOrdinate(1);
      }
      catch (final FactoryException | MismatchedDimensionException | TransformException e) {
         throw new RuntimeException(e);
      }

      //      _latitude = directPosition.getOrdinate(1);
      //      _longitude = directPosition.getOrdinate(0);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(_latitude);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(_longitude);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
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
      final GEOGeodetic other = (GEOGeodetic) obj;
      if (Double.doubleToLongBits(_latitude) != Double.doubleToLongBits(other._latitude)) {
         return false;
      }
      if (Double.doubleToLongBits(_longitude) != Double.doubleToLongBits(other._longitude)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("[lat=");
      builder.append(_latitude);
      builder.append(", lon=");
      builder.append(_longitude);
      builder.append("]");
      return builder.toString();
   }


   public GEOGeodetic sub(final GEOGeodetic that) {
      return new GEOGeodetic(_latitude - that._latitude, _longitude - that._longitude);
   }
}
