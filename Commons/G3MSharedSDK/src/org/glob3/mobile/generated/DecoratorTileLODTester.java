package org.glob3.mobile.generated;//
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
	  THROW_EXCEPTION("NULL NOT ALLOWED");
	}
  }

  public void dispose()
  {
	if (_tileLODTester != null)
		_tileLODTester.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void onTileHasChangedMesh(const Tile* tile) const
  public void onTileHasChangedMesh(Tile tile)
  {
	_tileLODTester.onTileHasChangedMesh(tile);
  }

  public void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
	_tileLODTester.onLayerTilesRenderParametersChanged(ltrp);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void renderStarted() const
  public void renderStarted()
  {
	_tileLODTester.renderStarted();
  }

}
