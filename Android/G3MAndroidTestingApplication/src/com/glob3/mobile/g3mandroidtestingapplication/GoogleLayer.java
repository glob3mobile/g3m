

package com.glob3.mobile.g3mandroidtestingapplication;

import java.util.ArrayList;

import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerCondition;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;


/**
 * http://mt1.googleapis.com/vt?lyrs=t@131,r@241000000&src=apiv3&hl=en-US&x=489&y=390&z=10&scale=2&style=37%7Csmartmaps
 * 
 * @author vidalete
 * 
 */
public class GoogleLayer
         extends
            Layer {

   private final String _url;
   private final int    _initialLevel;


   @Override
   protected String getLayerType() {
      return "GoogleOadl";
   }


   @Override
   protected boolean rawIsEquals(final Layer that) {
      final GoogleLayer t = (GoogleLayer) that;

      if (_initialLevel != t._initialLevel) {
         return false;
      }

      return true;
   }


   public GoogleLayer(final String url,
                      final TimeInterval timeToCache,
                      final boolean readExpired,
                      final int initialLevel) {
      this(url, timeToCache, readExpired, initialLevel, null);
   }


   public GoogleLayer(final String url,
                      final TimeInterval timeToCache,
                      final boolean readExpired) {
      this(url, timeToCache, readExpired, 2, null);
   }


   public GoogleLayer(final String url,
                      final TimeInterval timeToCache) {
      this(url, timeToCache, true, 2, null);
   }


   public GoogleLayer(final String url,
                      final TimeInterval timeToCache,
                      final boolean readExpired,
                      final int initialLevel,
                      final LayerCondition condition) {
      super(condition, "GoogleMaps", timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1,
               initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
      _initialLevel = initialLevel;
      _url = url;

   }


   @Override
   public ArrayList<Petition> createTileMapPetitions(final G3MRenderContext rc,
                                                     final Tile tile) {
      final IMathUtils mu = IMathUtils.instance();

      final java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();

      final Sector tileSector = tile._sector;

      final IStringBuilder isb = IStringBuilder.newStringBuilder();

      // http://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=600x300&key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc&sensor=false

      /*
       http: //maps.googleapis.com/maps/api/staticmap
       ?center=New+York,NY
       &zoom=13
       &size=600x300
       &key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc
       &sensor=false
       */


      //&x=489&y=390&z=10&scale=2&style=37%7Csmartmaps

      isb.addString(_url);
      final int level = tile._level;
      final int column = tile._column;
      final int numRows = (int) mu.pow(2.0, level);
      final int row = numRows - tile._row - 1;

      isb.addString("&x=");
      isb.addInt(column);
      isb.addString("&y=");
      isb.addInt(row);
      isb.addString("&z=");
      isb.addInt(level);


      final String path = isb.getString();

      if (isb != null) {
         isb.dispose();
      }

      petitions.add(new Petition(tileSector, new URL(path, false), getTimeToCache(), getReadExpired(), true));

      return petitions;
   }


   @Override
   public URL getFeatureInfoURL(final Geodetic2D position,
                                final Sector sector) {
      // TODO Auto-generated method stub
      return new URL();
   }


   @Override
   public String description() {
      return "[GoogleOadlLayer]";

   }


   @Override
   public Layer copy() {
      return new GoogleLayer(_url, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, _initialLevel,
               (_condition == null) ? null : _condition.copy());

   }

}
