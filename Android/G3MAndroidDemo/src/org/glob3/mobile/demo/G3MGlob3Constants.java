

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;


public class G3MGlob3Constants {

   public G3MGlob3Constants() {
      // TODO Auto-generated constructor stub
   }

   public static WMSLayer BING_LAYER = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                              WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png", "EPSG:4326", "",
                                              false, null);


   public static WMSLayer OSM_LAYER  = new WMSLayer( //
                                              "osm_auto:all", //
                                              new URL("http://129.206.228.72/cached/osm", false), //
                                              WMSServerVersion.WMS_1_1_0, //
                                              Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                                              "image/jpeg", //
                                              "EPSG:4326", //
                                              "", //
                                              false, //
                                              null);
}
