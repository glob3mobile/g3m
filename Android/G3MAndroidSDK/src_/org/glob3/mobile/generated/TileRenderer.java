package org.glob3.mobile.generated; 
//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TileRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;


public class TileRenderer extends Renderer
{
  private final TileTessellator _tessellator;
  private java.util.ArrayList<Tile> _topLevelTiles = new java.util.ArrayList<Tile>();

  private void clearTopLevelTiles()
  {
	for (int i = 0; i < _topLevelTiles.size(); i++)
	{
	  Tile tile = _topLevelTiles.get(i);
	  if (tile != null)
		  tile.dispose();
	}
  
	_topLevelTiles.clear();
  }
  private void createTopLevelTiles(InitializationContext ic)
  {
	int __diego_at_work;
  
	final Sector topSector = new Sector(new Geodetic2D(Angle.fromDegrees(-90), Angle.fromDegrees(-180)), new Geodetic2D(Angle.fromDegrees(90), Angle.fromDegrees(180)));
	final int K = 4;
	final int splitsByLatitude = 2 * K;
	final int splitsByLongitude = 4 * K;
	final int topLevel = 0;
  
  
	final Angle fromLatitude = topSector.lower().latitude();
	final Angle fromLongitude = topSector.lower().longitude();
  
	final Angle deltaLan = topSector.getDeltaLatitude();
	final Angle deltaLon = topSector.getDeltaLongitude();
  
	final Angle tileHeight = deltaLan.div(splitsByLatitude);
	final Angle tileWidth = deltaLon.div(splitsByLongitude);
  
	for (int row = 0; row < splitsByLatitude; row++)
	{
	  final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
	  final Angle tileLatTo = tileLatFrom.add(tileHeight);
  
	  for (int col = 0; col < splitsByLongitude; col++)
	  {
		final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
		final Angle tileLonTo = tileLonFrom.add(tileWidth);
  
		final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
		final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
		final Sector sector = new Sector(tileLower, tileUpper);
  
		Tile tile = new Tile(sector, topLevel, row, col);
		_topLevelTiles.add(tile);
	  }
	}
  
	ic.getLogger().logInfo("Created %i top level tiles", _topLevelTiles.size());
  }

  public TileRenderer(TileTessellator tessellator)
  {
	  _tessellator = tessellator;

  }

  public void dispose()
  {
	clearTopLevelTiles();
  
	if (_tessellator != null)
		_tessellator.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
	clearTopLevelTiles();
	createTopLevelTiles(ic);
  }

  public final int render(RenderContext rc)
  {
  
   // std::vector<Tile*> visibleTiles = getVisibleTiles(rc);
  
	for (int i = 0; i < _topLevelTiles.size(); i++)
	{
	  Tile tile = _topLevelTiles.get(i);
	  tile.render(rc, _tessellator);
	}
  
	return DefineConstants.MAX_TIME_TO_RENDER;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(int width, int height)
  {

  }

}