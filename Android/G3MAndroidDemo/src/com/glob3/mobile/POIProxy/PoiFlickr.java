

package com.glob3.mobile.POIProxy;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public class PoiFlickr
         extends
            Poi {

   private String title;
   private String urlM;
   private String urlT;
   private String urlL;
   private String urlS;
   private String urlZ;


   public PoiFlickr(final String _service,
                    final JSONArray coordinates,
                    final JSONObject properties) {
      super(_service, coordinates);
      parseProperties(properties);
   }


   @Override
   public void parseProperties(final JSONObject jsonProperties) {


      title = jsonProperties.getAsString("title").value();
      urlM = jsonProperties.getAsString("url_m").value();
      urlT = jsonProperties.getAsString("url_t").value();
      urlL = jsonProperties.getAsString("url_l").value();
      urlS = jsonProperties.getAsString("url_s").value();
      urlZ = jsonProperties.getAsString("url_z").value();

      setName(title);
   }


   public String getTitle() {
      return title;
   }


   public void setTitle(final String _title) {
      this.title = _title;
   }


   public String getUrlM() {
      return urlM;
   }


   public void setUrlM(final String _urlM) {
      this.urlM = _urlM;
   }


   public String getUrlT() {
      return urlT;
   }


   public void setUrlT(final String _urlT) {
      this.urlT = _urlT;
   }


   public String getUrlL() {
      return urlL;
   }


   public void setUrlL(final String _urlL) {
      this.urlL = _urlL;
   }


   public String getUrlS() {
      return urlS;
   }


   public void setUrlS(final String _urlS) {
      this.urlS = _urlS;
   }


   public String getUrlZ() {
      return urlZ;
   }


   public void setUrlZ(final String _urlZ) {
      this.urlZ = _urlZ;
   }
}
