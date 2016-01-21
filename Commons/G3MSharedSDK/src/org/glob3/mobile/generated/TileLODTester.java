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


public abstract class TileLODTester
{


  public TileLODTester()
  {
  }

  public void dispose()
  {
  }

  public abstract boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc);

  public abstract boolean isVisible(int testerLevel, Tile tile, G3MRenderContext rc);

  public abstract void onTileHasChangedMesh(int testerLevel, Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

}