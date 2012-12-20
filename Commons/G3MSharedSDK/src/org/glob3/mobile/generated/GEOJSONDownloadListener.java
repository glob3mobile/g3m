

package org.glob3.mobile.generated;

//
//  SceneParserDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  SceneParserDownloadListener.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
public class GEOJSONDownloadListener
         implements
            IBufferDownloadListener {

   private static final String FEATURES     = "features";
   private static final String GEOMETRY     = "geometry";
   private static final String TYPE         = "type";
   private static final String COORDINATES  = "coordinates";
   private static final String PROPERTIES   = "properties";
   private static final String DENOMINATION = "DENOMINACI";
   private static final String CLASE        = "CLASE";
   private static final String URLWEB       = "URL";


   private final MarksRenderer _marksRenderer;
   private final String        _icon;
   private final double        _minDistance;


   public GEOJSONDownloadListener(final MarksRenderer marksRenderer,
                                  final String icon,
                                  final double minDistance) {
      _marksRenderer = marksRenderer;
      _icon = icon;
      _minDistance = minDistance;
   }


   @Override
   public final void onDownload(final URL url,
                                final IByteBuffer buffer) {
      final String String = buffer.getAsString();
      final JSONBaseObject json = IJSONParser.instance().parse(String);
      ILogger.instance().logInfo(url.getPath());
      parseGEOJSON(json.asObject());
      IJSONParser.instance().deleteJSONData(json);

   }


   @Override
   public final void onError(final URL url) {
      ILogger.instance().logError("The requested geojson file could not be found!");
   }


   @Override
   public final void onCancel(final URL url) {
   }


   @Override
   public final void onCanceledDownload(final URL url,
                                        final IByteBuffer data) {
   }


   public void dispose() {
   }


   private void parseGEOJSON(final JSONObject geojson) {
      final JSONArray jsonFeatures = geojson.get(FEATURES).asArray();
      for (int i = 0; i < jsonFeatures.size(); i++) {
         final JSONObject jsonFeature = jsonFeatures.getAsObject(i);
         final JSONObject jsonGeometry = jsonFeature.getAsObject(GEOMETRY);
         final String jsonType = jsonGeometry.getAsString(TYPE).value();
         if (jsonType.equals("Point")) {
            parsePointObject(jsonFeature);
         }
      }
   }


   private void parsePointObject(final JSONObject point) {
      final JSONObject jsonProperties = point.getAsObject(PROPERTIES);
      final JSONObject jsonGeometry = point.getAsObject(GEOMETRY);
      final JSONArray jsonCoordinates = jsonGeometry.getAsArray(COORDINATES);

      final Angle latitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(1).doubleValue());
      final Angle longitude = Angle.fromDegrees(jsonCoordinates.getAsNumber(0).doubleValue());

      final JSONBaseObject denominaci = jsonProperties.get(DENOMINATION);
      final JSONBaseObject clase = jsonProperties.get(CLASE);

      Mark mark;

      if ((denominaci != null) && (clase != null)) {

         final IStringBuilder name = IStringBuilder.newStringBuilder();
         name.addString(IStringUtils.instance().capitalize(clase.asString().value()));
         name.addString(" ");
         name.addString(denominaci.asString().value());

         URL url;
         if ((jsonProperties.getAsString(URLWEB) != null) || (jsonProperties.getAsString(URLWEB).value().length() == 0)) {
            url = new URL(jsonProperties.getAsString(URLWEB).value(), false);
         }
         else {
            url = new URL("http://sig.caceres.es/SerWeb/fichatoponimo.asp?mslink=0", false);
         }

         if (_icon.length() > 0) {
            mark = new Mark(name.getString(), new URL(_icon, false), new Geodetic3D(latitude, longitude, 0), url, _minDistance,
                     null);
         }
         else {
            mark = new Mark(name.getString(), new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                     new Geodetic3D(latitude, longitude, 0), url, _minDistance, null);
         }
      }
      else {
         mark = new Mark("Unknown POI", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), new Geodetic3D(
                  latitude, longitude, 0), null, _minDistance, null);
      }
      _marksRenderer.addMark(mark);
   }

}
