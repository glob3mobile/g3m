

package org.glob3.mobile.generated;

//
//  TMSLayer.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

//
//  TMSLayer.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//


public class TMSLayer
         extends
            Layer {

   private final URL     _mapServerURL;

   private final String  _mapLayer;

   private final Sector  _sector;

   private final String  _format;
   private final String  _srs;
   private final boolean _isTransparent;


   public TMSLayer(final String mapLayer,
                   final URL mapServerURL,
                   final Sector sector,
                   final String format,
                   final String srs,
                   final boolean isTransparent,
                   final LayerCondition condition,
                   final TimeInterval timeToCache,
                   final boolean readExpired) {
      this(mapLayer, mapServerURL, sector, format, srs, isTransparent, condition, timeToCache, readExpired, null);
   }


   public TMSLayer(final String mapLayer,
                   final URL mapServerURL,
                   final Sector sector,
                   final String format,
                   final String srs,
                   final boolean isTransparent,
                   final LayerCondition condition,
                   final TimeInterval timeToCache,
                   final boolean readExpired,
                   final LayerTilesRenderParameters parameters) {
      super(condition, mapLayer, timeToCache, readExpired,
            (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(sector) : parameters);
      _mapServerURL = mapServerURL;
      _mapLayer = mapLayer;
      _sector = new Sector(sector);
      _format = format;
      _srs = srs;
      _isTransparent = isTransparent;
   }


   @Override
   public final java.util.ArrayList<Petition> createTileMapPetitions(final G3MRenderContext rc,
                                                                     final Tile tile) {

      final java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();

      final Sector tileSector = tile._sector;
      if (!_sector.touchesWith(tileSector)) {
         return petitions;
      }

      final IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString(_mapServerURL.getPath());
      isb.addString(_mapLayer);
      isb.addString("/");
      isb.addInt(tile._level);
      isb.addString("/");
      isb.addInt(tile._column);
      isb.addString("/");
      isb.addInt(tile._row);
      isb.addString(".");
      isb.addString(IStringUtils.instance().replaceSubstring(_format, "image/", ""));

      ILogger.instance().logInfo(isb.getString());

      final Petition petition = new Petition(tileSector, new URL(isb.getString(), false), getTimeToCache(), getReadExpired(),
               _isTransparent);
      petitions.add(petition);

      return petitions;

   }


   @Override
   public final URL getFeatureInfoURL(final Geodetic2D g,
                                      final Sector sector) {
      return URL.nullURL();

   }


   @Override
   public final String description() {
      return "[TMSLayer]";
   }


   @Override
   protected String getLayerType() {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   protected boolean rawIsEquals(final Layer that) {
      // TODO Auto-generated method stub
      return false;
   }


   @Override
   public Layer copy() {
      // TODO Auto-generated method stub
      return null;
   }

}
