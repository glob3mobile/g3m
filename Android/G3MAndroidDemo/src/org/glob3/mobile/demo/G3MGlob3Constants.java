

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;


public class G3MGlob3Constants {

   public G3MGlob3Constants() {
      // TODO Auto-generated constructor stub
   }

   public static WMSLayer         BING_LAYER                 = new WMSLayer(
                                                                      "ve",
                                                                      new URL(
                                                                               "http://worldwind27.arc.nasa.gov/wms/virtualearth?",
                                                                               false), WMSServerVersion.WMS_1_1_0,
                                                                      Sector.fullSphere(), "image/png", "EPSG:4326", "", false,
                                                                      null, TimeInterval.fromDays(30), true);


   public static WMSLayer         OSM_LAYER                  = new WMSLayer( //
                                                                      "osm_auto:all", //
                                                                      new URL("http://129.206.228.72/cached/osm", false), //
                                                                      WMSServerVersion.WMS_1_1_0, //
                                                                      Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                                                                      "image/jpeg", //
                                                                      "EPSG:4326", //
                                                                      "", //
                                                                      false, //
                                                                      null, //
                                                                      TimeInterval.fromDays(30), true);


   public static final Geodetic2D FRANKFURT_POSITION         = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutesSeconds(50, 6, 42), //
                                                                      Angle.fromDegreesMinutesSeconds(8, 41, 9));

   public static final Geodetic2D FRANKFURT_AIRPORT_POSITION = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutesSeconds(50, 2, 42), //
                                                                      Angle.fromDegreesMinutesSeconds(8, 34, 14));

   public static final Geodetic2D SAN_FRANCISCO_POSITION     = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutesSeconds(37, 47, 00), //
                                                                      Angle.fromDegreesMinutesSeconds(-122, 25, 00));

   public static final Geodetic2D HAMBURG_POSITION           = new Geodetic2D(//
                                                                      Angle.fromDegreesMinutesSeconds(53, 33, 55), //
                                                                      Angle.fromDegreesMinutesSeconds(10, 00, 5));

   public static final Geodetic2D LONDON_POSITION            = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutesSeconds(51, 30, 26), //
                                                                      Angle.fromDegreesMinutesSeconds(0, -7, 39));

   public static final Geodetic2D REYKJAVIK_POSITION         = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutes(64, 8), //
                                                                      Angle.fromDegreesMinutes(-21, 56));

   public static final Geodetic2D LOS_ANGELES_POSITION       = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutes(34, 3), //
                                                                      Angle.fromDegreesMinutes(-118, 15));

   public static final Geodetic2D EAST_AUSTRALIA_POSITION    = new Geodetic2D( //
                                                                      Angle.fromDegreesMinutes(-27, 43), //
                                                                      Angle.fromDegreesMinutes(153, 15));
}
