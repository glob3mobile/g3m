package org.glob3.mobile.generated; 
//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEO2DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//




public class GEO2DPointGeometry extends GEOGeometry2D
{
  private final Geodetic2D _position ;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }


  public GEO2DPointGeometry(Geodetic2D position)
  {
     _position = new Geodetic2D(position);
  }

  public final Geodetic2D getPosition()
  {
    return _position;
  }

  public final long getCoordinatesCount()
  {
    return 1;
  }

  public final GEO2DPointGeometry deepCopy()
  {
    return new GEO2DPointGeometry(_position);
  }

  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    return vectorSet.createFeatureMark(node, this);
  }

}