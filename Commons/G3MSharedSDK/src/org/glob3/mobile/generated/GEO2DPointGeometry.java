package org.glob3.mobile.generated;
//
//  GEO2DPointGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEO2DPointGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//





public class GEO2DPointGeometry extends GEO2DGeometry
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

  public final int symbolize(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    return vectorSet.symbolizeGeometry(node, this);
  }

}
