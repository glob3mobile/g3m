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
//class GPUProgramState;
//class GLGlobalState;
//class GPUProgramState;
//class GEOSymbolizer;

public class GEOFeatureCollection extends GEOObject
{
  private java.util.ArrayList<GEOFeature> _features = new java.util.ArrayList<GEOFeature>();

  public GEOFeatureCollection(java.util.ArrayList<GEOFeature> features)
  {
     _features = features;

  }

//  void addFeature(GEOFeature* feature);


  //void GEOFeatureCollection::addFeature(GEOFeature* feature) {
  //  _features.push_back(feature);
  //}
  
  //void GEOFeatureCollection::render(const G3MRenderContext* rc,
  //                                  const GLGlobalState& parentState, const GPUProgramState* parentProgramState,
  //                                  const GEOSymbolizer* symbolizer) {
  //  const int featuresCount = _features.size();
  //  for (int i = 0; i < featuresCount; i++) {
  //    GEOFeature* feature = _features[i];
  //    feature->render(rc, parentState, parentProgramState, symbolizer);
  //  }
  //}
  
  public void dispose()
  {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      if (feature != null)
         feature.dispose();
    }
  }

//  void render(const G3MRenderContext* rc,
//              const GLGlobalState& parentState, const GPUProgramState* parentProgramState,
//              const GEOSymbolizer* symbolizer);

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      feature.symbolize(rc, sc);
    }
  }

}