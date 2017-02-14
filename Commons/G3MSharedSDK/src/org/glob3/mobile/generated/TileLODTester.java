package org.glob3.mobile.generated;
//
//  TileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

//
//  TileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//


//class G3MRenderContext;
//class PlanetRenderContext;
//class Tile;
//class LayerTilesRenderParameters;


public abstract class TileLODTester
{

  public void dispose()
  {
  }

  public abstract boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

  public abstract void onTileHasChangedMesh(Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

  public abstract void renderStarted();

}
