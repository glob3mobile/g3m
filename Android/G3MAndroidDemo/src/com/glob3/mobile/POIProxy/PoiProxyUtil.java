

package com.glob3.mobile.POIProxy;

import java.util.ArrayList;

import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.specific.JSONParser_Android;


public class PoiProxyUtil {

   public static String PANORAMIO    = "panoramio";
   public static String WIKILOCATION = "wikilocation";
   public static String FLICKR       = "flickr";
   public static String INSTAGRAM    = "instagram";


   // public static String makeUriForRequestByExtent(final String service,
   // final double minLat,
   // final double minLon,
   // final double maxLat,
   // final double maxLon) {
   // return "poiproxy.mapps.es/browseByExtent?service=" + service + "&minX="
   // + minLat + "&minY=" + minLon + "&maxX=" + maxLat
   // + "&maxY=" + maxLon;
   // }


   public static String makeUriForRequestByLocation(final String service,
                                                    final Geodetic2D position,
                                                    final double radius) {
      return "http://poiproxy.mapps.es/browseByLonLat" + //
             "?service=" + service + //
             "&lon=" + position._longitude._degrees + //
             "&lat=" + position._latitude._degrees + //
             "&dist=" + radius;
   }


   // public static String getJsonPOIs(final String url) {
   // String response = "{\"response\" : \"SERVER ERROR\"}";
   // try {
   // response = HttpConnectionClient.getAsString(url);
   // }
   // catch (final Exception e) {
   // Log.e("PoiProxyUtil", "Error getJsonPOIs", e);
   // }
   //
   // return response;
   // }

   // PoiProxyUtil.getPOIs(params[0], params[1]);

   /*
    * new GetPoiTask().execute(PoiProxyUtil.makeUriForRequestByLocation(
    * "wikilocation", cameraPos.GetLatitude(),
    * cameraPos.GetLongitude(), cameraPos.GetHeight()), "wikilocation");
    * new
    * GetPoiTask().execute(PoiProxyUtil.makeUriForRequestByLocation("instagram"
    * , cameraPos.GetLatitude(),
    * cameraPos.GetLongitude(), cameraPos.GetHeight()), "instagram");
    * new
    * GetPoiTask().execute(PoiProxyUtil.makeUriForRequestByLocation("panoramio"
    * , cameraPos.GetLatitude(),
    * cameraPos.GetLongitude(), cameraPos.GetHeight()), "panoramio");
    * new GetPoiTask().execute(
    * PoiProxyUtil.makeUriForRequestByLocation("flickr",
    * cameraPos.GetLatitude(), cameraPos.GetLongitude(),
    * cameraPos.GetHeight()), "flickr");
    */


   public static ArrayList<Poi> getPOIs(final String json,
                                        final String service) {

      final IJSONParser parser = new JSONParser_Android();

      final JSONObject jsonObj = parser.parse(json).asObject();

      final JSONArray features = jsonObj.getAsArray("features");

      final ArrayList<Poi> pois = new ArrayList<Poi>();

      for (int i = 0; i < features.size(); i++) {
         // if (PANORAMIO.equals(service)) {
         // pois.add(new PoiPanoramio(service,
         // features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates"),
         // features.getJSONObject(i).getJSONObject("properties")));
         // }
         // else if (FLICKR.equals(service)) {
         // pois.add(new PoiFlickr(service,
         // features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates"),
         // features.getJSONObject(i).getJSONObject("properties")));
         // }
         // else if (INSTAGRAM.equals(service)) {
         // pois.add(new PoiInstagram(service,
         // features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates"),
         // features.getJSONObject(i).getJSONObject("properties")));
         // }
         // else
         if (WIKILOCATION.equals(service)) {
            final JSONArray coordinates = features.getAsObject(i).getAsObject("geometry").getAsArray("coordinates");
            final JSONObject properties = features.getAsObject(i).getAsObject("properties");
            pois.add(new PoiWikilocation(service, coordinates, properties));
         }
      }

      return pois;
   }

}
