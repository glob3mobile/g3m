package org.glob3.mobile.generated; 
//
//  OrTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

//
//  OrTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//




public class OrTileLODTester extends TileLODTester
{
  private TileLODTester _left;
  private TileLODTester _right;

  public OrTileLODTester(TileLODTester left, TileLODTester right)
  {
     _left = left;
     _right = right;
  
  }

  public void dispose()
  {
    if (_left != null)
       _left.dispose();
    if (_right != null)
       _right.dispose();
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
  
    if (_left.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS))
    {
      return true;
    }
  
    return _right.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    _left.onTileHasChangedMesh(tile);
    _right.onTileHasChangedMesh(tile);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _left.onLayerTilesRenderParametersChanged(ltrp);
    _right.onLayerTilesRenderParametersChanged(ltrp);
  }

  public final void renderStarted()
  {
    _left.renderStarted();
    _right.renderStarted();
  }

}