package org.glob3.mobile.generated; 
//
//  DecoratorTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/26/16.
//
//

//
//  DecoratorTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/26/16.
//
//




public abstract class DecoratorTileLODTester extends TileLODTester
{
  protected TileLODTester _tileLODTester;

  protected DecoratorTileLODTester(TileLODTester tileLODTester)
  {
     _tileLODTester = tileLODTester;
    if (_tileLODTester == null)
    {
      throw new RuntimeException("NULL NOT ALLOWED");
    }
  }

  public void dispose()
  {
    if (_tileLODTester != null)
       _tileLODTester.dispose();
    super.dispose();
  }

  public void onTileHasChangedMesh(Tile tile)
  {
    _tileLODTester.onTileHasChangedMesh(tile);
  }

  public void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _tileLODTester.onLayerTilesRenderParametersChanged(ltrp);
  }

}