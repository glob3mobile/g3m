package org.glob3.mobile.generated;//
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
	  THROW_EXCEPTION("NULL NOT ALLOWED");
	}
  }


  public void dispose()
  {
	if (_tileVisibilityTester != null)
		_tileVisibilityTester.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void onTileHasChangedMesh(const Tile* tile) const
  public void onTileHasChangedMesh(Tile tile)
  {
	_tileVisibilityTester.onTileHasChangedMesh(tile);
  }

  public void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
	_tileVisibilityTester.onLayerTilesRenderParametersChanged(ltrp);
  }

}
