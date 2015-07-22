

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.LayerBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MercatorTiledLayer;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;


public class SimpleRasterLayerBuilder
         extends
            LayerBuilder {

   public static LayerSet createLayerset() {
      final LayerSet layerSet = new LayerSet();

      final MapQuestLayer mqOSM = MapQuestLayer.newOSM(TimeInterval.fromDays(30));
      mqOSM.setEnable(false);
      mqOSM.setTitle("MapQuest OSM");
      layerSet.addLayer(mqOSM);

      final MapQuestLayer mqlAerial = MapQuestLayer.newOpenAerial(TimeInterval.fromDays(30));
      mqlAerial.setTitle("MapQuest Aerial");
      mqlAerial.setEnable(false);
      layerSet.addLayer(mqlAerial);

      final MapBoxLayer mboxAerialLayer = new MapBoxLayer("examples.map-m0t0lrpu", TimeInterval.fromDays(30), true, 2);
      mboxAerialLayer.setTitle("Map Box Aerial");
      mboxAerialLayer.setEnable(false);
      layerSet.addLayer(mboxAerialLayer);

      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 2);
      mboxTerrainLayer.setTitle("Map Box Terrain");
      mboxTerrainLayer.setEnable(false);
      layerSet.addLayer(mboxTerrainLayer);

      final MapBoxLayer mboxOSMLayer = new MapBoxLayer("examples.map-cnkhv76j", TimeInterval.fromDays(30), true, 2);
      mboxOSMLayer.setTitle("Map Box OSM");
      mboxOSMLayer.setEnable(true);
      layerSet.addLayer(mboxOSMLayer);

      final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
               WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0,
                        18), TimeInterval.fromDays(30), true);
      blueMarble.setTitle("WMS Nasa Blue Marble");
      blueMarble.setEnable(false);
      layerSet.addLayer(blueMarble);


      final OSMLayer osmLayer = new OSMLayer(TimeInterval.fromDays(30));
      osmLayer.setTitle("Open Street Map");
      osmLayer.setEnable(false);
      layerSet.addLayer(osmLayer);

      final BingMapsLayer bingMapsAerialLayer = new BingMapsLayer(BingMapType.Aerial(),
               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", TimeInterval.fromDays(30));
      bingMapsAerialLayer.setTitle("Bing Aerial");
      bingMapsAerialLayer.setEnable(false);
      layerSet.addLayer(bingMapsAerialLayer);

      final BingMapsLayer bingMapsAerialWithLabels = new BingMapsLayer(BingMapType.AerialWithLabels(),
               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", TimeInterval.fromDays(30));
      bingMapsAerialWithLabels.setTitle("Bing Aerial With Labels");
      bingMapsAerialWithLabels.setEnable(false);
      layerSet.addLayer(bingMapsAerialWithLabels);

      final BingMapsLayer bingMapsCollinsBart = new BingMapsLayer(BingMapType.CollinsBart(),
               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", TimeInterval.fromDays(30));
      bingMapsCollinsBart.setTitle("MapQuest OSM");
      bingMapsCollinsBart.setEnable(false);
      layerSet.addLayer(bingMapsCollinsBart);

      final ArrayList<String> subdomains = new ArrayList<String>();
      subdomains.add("0.");
      subdomains.add("1.");
      subdomains.add("2.");
      subdomains.add("3.");

      final MercatorTiledLayer meteoritesLayer = new MercatorTiledLayer( //
               "http://", // protocol
               "tiles.cartocdn.com/osm2/tiles/meteoritessize", // domain
               subdomains, // subdomains
               "png", // imageFormat
               TimeInterval.fromDays(90), // timeToCache
               true, // readExpired
               2, // initialLevel
               17, // maxLevel
               true // isTransparent
      );
      meteoritesLayer.setTitle("CartoDB Meteorites");
      meteoritesLayer.setEnable(false);
      layerSet.addLayer(meteoritesLayer);

      final URLTemplateLayer arcGISOverlayLayerTest = URLTemplateLayer.newMercator(
               "http://www.fairfaxcounty.gov/gis/rest/services/DMV/DMV/MapServer/export?dpi=96&transparent=true&format=png8&bbox={west}%2C{south}%2C{east}%2C{north}&bboxSR=3857&imageSR=3857&size={width}%2C{height}&f=image",
               Sector.fullSphere(), true, 2, 18, TimeInterval.fromDays(30), true, 1, new LevelTileCondition(12, 18));
      arcGISOverlayLayerTest.setTitle("ESRI ArcGis Online");
      arcGISOverlayLayerTest.setEnable(false);
      layerSet.addLayer(arcGISOverlayLayerTest);


      return layerSet;
   }
}
