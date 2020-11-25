package org.glob3.mobile.generated;
//
//  GEO3DPointGeometry.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

//
//  GEO3DPointGeometry.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//





public class GEO3DPointGeometry extends GEO3DGeometry
{
  private final Geodetic3D _position ;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }


  public GEO3DPointGeometry(Geodetic3D position)
  {
     _position = new Geodetic3D(position);
  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final int symbolize(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    return vectorSet.symbolizeGeometry(node, this);
  }

}