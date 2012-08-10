package org.glob3.mobile.generated; 
//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTexturizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;

//class TileKey {
//public:
//  const int _level;
//  const int _row;
//  const int _column;
//  
//  TileKey(const int level,
//          const int row,
//          const int column) :
//  _level(level),
//  _row(row),
//  _column(column)
//  {
//    
//  }
//  
//  TileKey(const TileKey& that):
//  _level(that._level),
//  _row(that._row),
//  _column(that._column)
//  {
//    
//  }
//  
//};

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TilesStatistics;

public class Tile
{
  private final Sector _sector ;
  private final int _level;
  private final int _row;
  private final int _column;

  private Mesh _tessellatorMesh;
  private Mesh _debugMesh;
  private Mesh _texturizerMesh;

  private Tile _parent;
  private boolean _textureSolved;
  private java.util.ArrayList<Tile> _subtiles;

  private ITimer _texturizerTimer;

  private boolean _justCreatedSubtiles;


  private Mesh getTessellatorMesh(RenderContext rc, TileTessellator tessellator)
  {
	if (_tessellatorMesh == null)
	{
	  _tessellatorMesh = tessellator.createMesh(rc, this);
	}
	return _tessellatorMesh;
  }

  private Mesh getDebugMesh(RenderContext rc, TileTessellator tessellator)
  {
	if (_debugMesh == null)
	{
	  _debugMesh = tessellator.createDebugMesh(rc, this);
	}
	return _debugMesh;
  }

  private boolean isVisible(RenderContext rc, TileTessellator tessellator)
  {
  
	// test if sector is back oriented with respect to the camera
	if (_sector.isBackOriented(rc))
	{
		return false;
	  }
  
	return getTessellatorMesh(rc, tessellator).getExtent().touches(rc.getNextCamera().getFrustumInModelCoordinates());
	//return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->_halfFrustumInModelCoordinates);
  }

  private boolean meetsRenderCriteria(RenderContext rc, TileTessellator tessellator, TileTexturizer texturizer, TileParameters parameters, ITimer lastSplitTimer, TilesStatistics statistics)
  {
	if (_level >= parameters._maxLevel)
	{
	  return true;
	}
  
  //  if (timer != NULL) {
  //    if ( timer->elapsedTime().milliseconds() > 50 ) {
  //      return true;
  //    }
  //  }
  
	if (texturizer != null)
	{
	  if (texturizer.tileMeetsRenderCriteria(this))
	  {
		return true;
	  }
	}
  
  
  //  int projectedSize = getTessellatorMesh(rc, tessellator)->getExtent()->squaredProjectedArea(rc);
  //  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
  //    return true;
  //  }
	final Vector2D extent = getTessellatorMesh(rc, tessellator).getExtent().projectedExtent(rc);
	//const double t = extent.maxAxis() * 2;
	final double t = (extent.x() + extent.y());
	if (t <= ((parameters._tileTextureWidth + parameters._tileTextureHeight) * 1.75))
	{
	  return true;
	}
  
  
	int __TODO_tune_render_budget;
	if (_subtiles == null) // the tile needs to create the subtiles
	{
	  if (statistics.getSplitsCountInFrame() > 1)
	  {
		// there are not more splitsCount-budget to spend
		return true;
	  }
  
	  if (lastSplitTimer.elapsedTime().milliseconds() < 50)
	  {
		// there are not more time-budget to spend
		return true;
	  }
	}
  
	return false;
  }

  private java.util.ArrayList<Tile> createSubTiles()
  {
	final Geodetic2D lower = _sector.lower();
	final Geodetic2D upper = _sector.upper();
  
	final Angle midLat = Angle.midAngle(lower.latitude(), upper.latitude());
	final Angle midLon = Angle.midAngle(lower.longitude(), upper.longitude());
  
	final int nextLevel = _level + 1;
  
	java.util.ArrayList<Tile> subTiles = new java.util.ArrayList<Tile>();
	subTiles.add(createSubTile(lower.latitude(), lower.longitude(), midLat, midLon, nextLevel, 2 * _row, 2 * _column));
  
	subTiles.add(createSubTile(lower.latitude(), midLon, midLat, upper.longitude(), nextLevel, 2 * _row, 2 * _column + 1));
  
	subTiles.add(createSubTile(midLat, lower.longitude(), upper.latitude(), midLon, nextLevel, 2 * _row + 1, 2 * _column));
  
	subTiles.add(createSubTile(midLat, midLon, upper.latitude(), upper.longitude(), nextLevel, 2 * _row + 1, 2 * _column + 1));
  
	return subTiles;
  }

  private void rawRender(RenderContext rc, TileTessellator tessellator, TileTexturizer texturizer, ITimer lastTexturizerTimer)
  {
  
	Mesh tessellatorMesh = getTessellatorMesh(rc, tessellator);
  
	if (tessellatorMesh != null)
	{
	  if (texturizer == null)
	  {
		tessellatorMesh.render(rc);
	  }
	  else
	  {
  
		final boolean needsToCallTexturizer = (!isTextureSolved() || (_texturizerMesh == null));
  
		if (needsToCallTexturizer)
		{
		  int __TODO_tune_render_budget;
  
		  boolean callTexturizer = ((_texturizerTimer == null) || (_texturizerTimer.elapsedTime().milliseconds() > 125 && lastTexturizerTimer.elapsedTime().milliseconds() > 50));
  
		  if (callTexturizer)
		  {
			_texturizerMesh = texturizer.texturize(rc, this, tessellator, tessellatorMesh, _texturizerMesh);
			lastTexturizerTimer.start();
  
			if (_texturizerTimer == null)
			{
			  _texturizerTimer = rc.getFactory().createTimer();
			}
			else
			{
			  _texturizerTimer.start();
			}
		  }
  
		}
  
		if ((_texturizerTimer != null) && isTextureSolved())
		{
		  if (_texturizerTimer != null)
			  _texturizerTimer.dispose();
		  _texturizerTimer = null;
		}
  
		if (_texturizerMesh != null)
		{
		  _texturizerMesh.render(rc);
		}
		else
		{
		  tessellatorMesh.render(rc);
		}
	  }
	}
  
  }

  private void cleanTexturizerMesh()
  {
	int __DIEGO_AT_WORK;
  
  
  //  if (_texturizerMesh != NULL) {
  //    delete _texturizerMesh;
  //    _texturizerMesh = NULL;
  //  }
  //
  //  setTextureSolved(false);
  //
  //  if (_texturizerTimer != NULL) {
  //    delete _texturizerTimer;
  //    _texturizerTimer = NULL;
  //  }
  }

  private void debugRender(RenderContext rc, TileTessellator tessellator)
  {
	Mesh debugMesh = getDebugMesh(rc, tessellator);
	if (debugMesh != null)
	{
	  debugMesh.render(rc);
	}
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column)
  {
	return new Tile(this, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), level, row, column);
  }


  private java.util.ArrayList<Tile> getSubTiles()
  {
	if (_subtiles == null)
	{
	  _subtiles = createSubTiles();
	  _justCreatedSubtiles = true;
	}
	return _subtiles;
  }

  private void prune(TileTexturizer texturizer)
  {
	if (_subtiles != null)
	{
  
	  final int subtilesSize = _subtiles.size();
	  for (int i = 0; i < subtilesSize; i++)
	  {
		Tile subtile = _subtiles.get(i);
  
		subtile.prune(texturizer);
		if (texturizer != null)
		{
		  texturizer.tileToBeDeleted(subtile);
		}
		if (subtile != null)
			subtile.dispose();
	  }
  
	  _subtiles = null;
	  _subtiles = null;
  
	}
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Tile(Tile that);

  public Tile(Tile parent, Sector sector, int level, int row, int column)
  {
	  _parent = parent;
	  _sector = new Sector(sector);
	  _level = level;
	  _row = row;
	  _column = column;
	  _tessellatorMesh = null;
	  _debugMesh = null;
	  _texturizerMesh = null;
	  _textureSolved = false;
	  _subtiles = null;
	  _justCreatedSubtiles = false;
	  _texturizerTimer = null;
  }

  public void dispose()
  {
	prune(null);
  
	if (_texturizerTimer != null)
	{
	  if (_texturizerTimer != null)
		  _texturizerTimer.dispose();
	}
  
	if (_debugMesh != null)
	{
	  if (_debugMesh != null)
		  _debugMesh.dispose();
	}
  
	if (_tessellatorMesh != null)
	{
	  if (_tessellatorMesh != null)
		  _tessellatorMesh.dispose();
	}
  
	if (_texturizerMesh != null)
	{
	  if (_texturizerMesh != null)
		  _texturizerMesh.dispose();
	}
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getLevel() const
  public final int getLevel()
  {
	return _level;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getRow() const
  public final int getRow()
  {
	return _row;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getColumn() const
  public final int getColumn()
  {
	return _column;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* getTexturizerMesh() const
  public final Mesh getTexturizerMesh()
  {
	return _texturizerMesh;
  }

  public final void setTextureSolved(boolean textureSolved)
  {
	_textureSolved = textureSolved;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTextureSolved() const
  public final boolean isTextureSolved()
  {
	return _textureSolved;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Tile* getParent() const
  public final Tile getParent()
  {
	return _parent;
  }

  public final void render(RenderContext rc, TileTessellator tessellator, TileTexturizer texturizer, TileParameters parameters, TilesStatistics statistics, java.util.LinkedList<Tile> toVisitInNextIteration, ITimer lastSplitTimer, ITimer lastTexturizerTimer)
  {
	statistics.computeTileProcessed(this);
  
	if (isVisible(rc, tessellator))
	{
	  statistics.computeVisibleTile(this);
  
	  if (meetsRenderCriteria(rc, tessellator, texturizer, parameters, lastSplitTimer, statistics))
	  {
		rawRender(rc, tessellator, texturizer, lastTexturizerTimer);
		if (parameters._renderDebug)
		{
		  debugRender(rc, tessellator);
		}
  
		statistics.computeTileRendered(this);
  
		prune(texturizer);
	  }
	  else
	  {
		cleanTexturizerMesh();
  
		java.util.ArrayList<Tile> subTiles = getSubTiles();
		if (_justCreatedSubtiles)
		{
		  lastSplitTimer.start();
		  statistics.computeSplit();
		  _justCreatedSubtiles = false;
		}
  
		final int subTilesSize = subTiles.size();
		for (int i = 0; i < subTilesSize; i++)
		{
		  Tile subTile = subTiles.get(i);
		  // subTile->render(rc, tessellator, texturizer, parameters, statistics, toVisitInNextIteration, timer);
		  toVisitInNextIteration.addLast(subTile);
		}
	  }
	}
	else
	{
	  prune(texturizer);
	}
  }

}