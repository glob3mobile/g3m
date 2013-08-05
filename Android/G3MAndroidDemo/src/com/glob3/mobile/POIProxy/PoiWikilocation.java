

package com.glob3.mobile.POIProxy;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public class PoiWikilocation
         extends
            Poi {

   private String title;
   private String url;
   private String distance;


   public PoiWikilocation(final String _service,
                          final JSONArray coordinates,
                          final JSONObject properties) {
      super(_service, coordinates);
      parseProperties(properties);
   }


   @Override
   public void parseProperties(final JSONObject jsonProperties) {
      title = jsonProperties.getAsString("title").value();
      url = jsonProperties.getAsString("url").value();
      distance = jsonProperties.getAsString("distance").value();

      setName(title);
   }


   public String getTitle() {
      return title;
   }


   public void setTitle(final String _title) {
      this.title = _title;
   }


   public String getUrl() {
      return url;
   }


   public void setUrl(final String _url) {
      this.url = _url;
   }


   public String getDistance() {
      return distance;
   }


   public void setDistance(final String _distance) {
      this.distance = _distance;
   }
}
