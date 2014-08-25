package org.glob3.mobile.generated; 
//
//  TileRenderingListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/21/14.
//
//


//class Tile;

public abstract class TileRenderingListener
{
  public void dispose()
  {
  }

  public abstract void changedTilesRendering(java.util.ArrayList<Tile> tilesStartedRendering, java.util.ArrayList<String> tilesStoppedRendering);

}