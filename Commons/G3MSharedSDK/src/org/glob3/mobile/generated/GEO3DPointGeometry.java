package org.glob3.mobile.generated;
//
//  GEO3DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

//
//  GEO3DPointGeometry.hpp
//  G3MiOSSDK
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

  public final long getCoordinatesCount()
  {
    return 1;
  }

  public final GEO3DPointGeometry deepCopy()
  {
    return new GEO3DPointGeometry(_position);
  }

  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    return vectorSet.createFeatureMark(node, this);
  }

}
