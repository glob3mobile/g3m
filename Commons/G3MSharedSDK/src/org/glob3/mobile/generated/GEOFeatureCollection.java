package org.glob3.mobile.generated; 
//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeatureCollection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//class GEOFeature;

public class GEOFeatureCollection extends GEOObject {
  private java.util.ArrayList<GEOFeature> _features = new java.util.ArrayList<GEOFeature>();

  public final void addFeature(GEOFeature feature) {
    _features.add(feature);
  }

  public void dispose() {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++) {
      GEOFeature feature = _features.get(i);
      if (feature != null)
         feature.dispose();
    }
  }

  public final void render(G3MRenderContext rc, GLState parentState) {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++) {
      GEOFeature feature = _features.get(i);
      feature.render(rc, parentState);
    }
  }

}