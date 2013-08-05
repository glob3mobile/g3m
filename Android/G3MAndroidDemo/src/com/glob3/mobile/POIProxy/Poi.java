

package com.glob3.mobile.POIProxy;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public abstract class Poi {

   private String name;
   private String service;
   private double latitude;
   private double longitude;


   public Poi(final String _service,
              final double _latitude,
              final double _longitude) {
      this.service = _service;
      this.latitude = _latitude;
      this.longitude = _longitude;
   }


   public Poi(final String _service,
              final JSONArray coordinates) {
      this.service = _service;
      this.latitude = coordinates.getAsNumber(0, latitude);
      this.longitude = coordinates.getAsNumber(1, latitude);

      //      this.latitude = ((Object) coordinates).               getElement(0).getNumber().getDoubleValue();
      //      this.longitude = ((Object) coordinates).getElement(1).getNumber().getDoubleValue();
   }


   public String getService() {
      return service;
   }


   public void setService(final String _service) {
      this.service = _service;
   }


   public double getLatitude() {
      return latitude;
   }


   public void setLatitude(final double _latitude) {
      this.latitude = _latitude;
   }


   public double getLongitude() {
      return longitude;
   }


   public void setLongitude(final double _longitude) {
      this.longitude = _longitude;
   }


   public String getName() {
      return name;
   }


   public void setName(final String _name) {
      this.name = _name;
   }


   public abstract void parseProperties(final JSONObject jsonProp);
}
