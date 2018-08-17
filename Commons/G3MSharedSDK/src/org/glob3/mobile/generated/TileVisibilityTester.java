package org.glob3.mobile.generated;//
//  TileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  TileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//


//class G3MRenderContext;
//class PlanetRenderContext;
//class Tile;
//class LayerTilesRenderParameters;

public abstract class TileVisibilityTester
{

  public TileVisibilityTester()
  {
  }

  public void dispose()
  {
  }

  public abstract boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

  public abstract void onTileHasChangedMesh(Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

  public abstract void renderStarted();

}
