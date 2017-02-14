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

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    if (_left.meetsRenderCriteria(rc, prc, tile))
    {
      return true;
    }
  
    return _right.meetsRenderCriteria(rc, prc, tile);
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
