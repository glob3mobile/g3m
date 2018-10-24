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

  private static java.util.ArrayList<GEOFeature> copy(java.util.ArrayList<GEOFeature> features)
  {
    java.util.ArrayList<GEOFeature> result = new java.util.ArrayList<GEOFeature>();
    final int size = features.size();
    for (int i = 0; i < size; i++)
    {
      GEOFeature feature = features.get(i);
      result.add((feature == null) ? null : feature.deepCopy());
    }
    return result;
  }

  protected final Sector calculateSector()
  {
    final int featuresCount = _features.size();
    if (featuresCount == 0)
    {
      return null;
    }
  
    final Sector sector0 = _features.get(0).getSector();
  
    double minLatRad = sector0._lower._latitude._radians;
    double maxLatRad = sector0._upper._latitude._radians;
  
    double minLonRad = sector0._lower._longitude._radians;
    double maxLonRad = sector0._upper._longitude._radians;
  
    for (int i = 1; i < featuresCount; i++)
    {
      final Sector sector = _features.get(i).getSector();
  
      final double lowerLatRad = sector._lower._latitude._radians;
      if (lowerLatRad < minLatRad)
      {
        minLatRad = lowerLatRad;
      }
      final double upperLatRad = sector._upper._latitude._radians;
      if (upperLatRad > maxLatRad)
      {
        maxLatRad = upperLatRad;
      }
  
      final double lowerLonRad = sector._lower._longitude._radians;
      if (lowerLonRad < minLonRad)
      {
        minLonRad = lowerLonRad;
      }
      final double upperLonRad = sector._upper._longitude._radians;
      if (upperLonRad > maxLonRad)
      {
        maxLonRad = upperLonRad;
      }
    }
  
    final double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
    final double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;
  
    final double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
    final double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;
  
    return Sector.newFromRadians(lowerLatRadians, lowerLonRadians, upperLatRadians, upperLonRadians);
  }

  public GEOFeatureCollection(java.util.ArrayList<GEOFeature> features)
  {
     _features = features;
  }

  public void dispose()
  {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      if (feature != null)
         feature.dispose();
    }
  
    super.dispose();
  
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      feature.symbolize(rc, symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
    }
  }

  public final GEOFeature get(int i)
  {
    return _features.get(i);
  }

  public final int size()
  {
    return _features.size();
  }

  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      if (feature != null)
      {
        feature.rasterize(symbolizer, canvas, projection, tileLevel);
      }
    }
  }

  public final long getCoordinatesCount()
  {
    long result = 0;
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      result += feature.getCoordinatesCount();
    }
    return result;
  }

  public final GEOFeatureCollection deepCopy()
  {
    return new GEOFeatureCollection(copy(_features));
  }

  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    long result = 0;
    final int featuresCount = _features.size();
    for (int i = 0; i < featuresCount; i++)
    {
      GEOFeature feature = _features.get(i);
      if (feature != null)
      {
        result += feature.createFeatureMarks(vectorSet, node);
      }
    }
    return result;
  }

}
