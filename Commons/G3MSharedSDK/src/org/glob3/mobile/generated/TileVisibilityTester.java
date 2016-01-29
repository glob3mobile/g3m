package org.glob3.mobile.generated; 
//
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


//class Tile;
//class G3MRenderContext;
//class LayerTilesRenderParameters;
//class Frustum;

public abstract class TileVisibilityTester
{

  public TileVisibilityTester()
  {
  }

  public void dispose()
  {
  }

  public abstract boolean isVisible(Tile tile, G3MRenderContext rc, long nowInMS, Frustum frustumInModelCoordinates);

  public abstract void onTileHasChangedMesh(Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

  public abstract void renderStarted();

}