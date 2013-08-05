

package com.glob3.mobile.POIProxy;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public class PoiInstagram
         extends
            Poi {

   private String username;
   private String profile_picture;
   private String id;
   private String link;
   private String url;


   public PoiInstagram(final String _service,
                       final JSONArray coordinates,
                       final JSONObject properties) {
      super(_service, coordinates);
      parseProperties(properties);
   }


   @Override
   public void parseProperties(final JSONObject jsonProperties) {
      username = jsonProperties.getAsString("username").value();
      profile_picture = jsonProperties.getAsString("profile_picture").value();
      id = jsonProperties.getAsString("id").value();
      link = jsonProperties.getAsString("link").value();
      url = jsonProperties.getAsString("url").value();
      setName(id);
   }


   public String getUsername() {
      return username;
   }


   public void setUsername(final String _username) {
      this.username = _username;
   }


   public String getProfilePicture() {
      return profile_picture;
   }


   public void setProfilePicture(final String _profile_picture) {
      this.profile_picture = _profile_picture;
   }


   public String getId() {
      return id;
   }


   public void setId(final String _id) {
      this.id = _id;
   }


   public String getLink() {
      return link;
   }


   public void setLink(final String _link) {
      this.link = _link;
   }


   public String getUrl() {
      return url;
   }


   public void setUrl(final String _url) {
      this.url = _url;
   }

}
