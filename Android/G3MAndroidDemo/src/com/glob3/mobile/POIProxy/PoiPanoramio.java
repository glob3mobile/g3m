

package com.glob3.mobile.POIProxy;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public class PoiPanoramio
         extends
            Poi {

   private String photoTitle;
   private String photoUrl;
   private String photoFileUrl;


   public PoiPanoramio(final String _service,
                       final JSONArray coordinates,
                       final JSONObject properties) {
      super(_service, coordinates);
      parseProperties(properties);
   }


   @Override
   public void parseProperties(final JSONObject jsonProperties) {
      photoTitle = jsonProperties.getAsString("photo_title").value();
      photoUrl = jsonProperties.getAsString("photo_url").value();
      photoFileUrl = jsonProperties.getAsString("photo_file_url").value();

      setName(photoTitle);
   }


   public String getPhotoTitle() {
      return photoTitle;
   }


   public void setPhotoTitle(final String _photoTitle) {
      this.photoTitle = _photoTitle;
   }


   public String getPhotoUrl() {
      return photoUrl;
   }


   public void setPhotoUrl(final String _photoUrl) {
      this.photoUrl = _photoUrl;
   }


   public String getPhotoFileUrl() {
      return photoFileUrl;
   }


   public void setPhotoFileUrl(final String _photoFileUrl) {
      this.photoFileUrl = _photoFileUrl;
   }
}
