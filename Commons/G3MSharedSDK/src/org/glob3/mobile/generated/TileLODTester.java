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


//class Tile;
//class G3MRenderContext;
//class LayerTilesRenderParameters;
//class TilesRenderParameters;
//class ITimer;

public abstract class TileLODTester
{
  private static int ID_COUNTER = 0;

  protected final int _id;


  public TileLODTester()
  {
     _id = ID_COUNTER++;
  }

  public void dispose()
  {
  }

  public abstract boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS);

  public abstract boolean isVisible(Tile tile, G3MRenderContext rc);

  public abstract void onTileHasChangedMesh(Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

}