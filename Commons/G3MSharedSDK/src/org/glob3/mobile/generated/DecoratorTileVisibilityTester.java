package org.glob3.mobile.generated; 
//
//  DecoratorTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  DecoratorTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//



public abstract class DecoratorTileVisibilityTester extends TileVisibilityTester
{
  protected TileVisibilityTester _tileVisibilityTester;

  protected DecoratorTileVisibilityTester(TileVisibilityTester tileVisibilityTester)
  {
     _tileVisibilityTester = tileVisibilityTester;
    if (_tileVisibilityTester == null)
    {
      throw new RuntimeException("NULL NOT ALLOWED");
    }
  }


  public void dispose()
  {
    if (_tileVisibilityTester != null)
       _tileVisibilityTester.dispose();
    super.dispose();
  }

  public void onTileHasChangedMesh(Tile tile)
  {
    _tileVisibilityTester.onTileHasChangedMesh(tile);
  }

  public void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _tileVisibilityTester.onLayerTilesRenderParametersChanged(ltrp);
  }

}