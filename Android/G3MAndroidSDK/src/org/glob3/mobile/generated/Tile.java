package org.glob3.mobile.generated; 
public class Tile
{
  private TileTexturizer _texturizer;

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
  private boolean _texturizerDirty;

  private Mesh getTessellatorMesh(RenderContext rc, TileRenderContext trc)
  {
	if (_tessellatorMesh == null)
	{
	  _tessellatorMesh = trc.getTessellator().createMesh(rc, this);
	}
	return _tessellatorMesh;
  }

  private Mesh getDebugMesh(RenderContext rc, TileRenderContext trc)
  {
	if (_debugMesh == null)
	{
	  _debugMesh = trc.getTessellator().createDebugMesh(rc, this);
	}
	return _debugMesh;
  }

  private boolean isVisible(RenderContext rc, TileRenderContext trc)
  {
	// test if sector is back oriented with respect to the camera
	//  if (_sector.isBackOriented(rc)) {
	//    return false;
	//  }
  
	Extent extent = getTessellatorMesh(rc, trc).getExtent();
	if (extent == null)
	{
	  return false;
	}
	return extent.touches(rc.getNextCamera().getFrustumInModelCoordinates());
  }

  private boolean meetsRenderCriteria(RenderContext rc, TileRenderContext trc)
  {
	final TilesRenderParameters parameters = trc.getParameters();
  
	if (_level >= parameters._maxLevel)
	{
	  return true;
	}
  
	//  if (timer != NULL) {
	//    if ( timer->elapsedTime().milliseconds() > 50 ) {
	//      return true;
	//    }
	//  }
  
	TileTexturizer texturizer = trc.getTexturizer();
	if (texturizer != null)
	{
	  if (texturizer.tileMeetsRenderCriteria(this))
	  {
		return true;
	  }
	}
  
	Extent extent = getTessellatorMesh(rc, trc).getExtent();
	if (extent == null)
	{
	  return true;
	}
	//  const double projectedSize = extent->squaredProjectedArea(rc);
	//  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
	//    return true;
	//  }
	final Vector2D ex = extent.projectedExtent(rc);
	//const double t = extent.maxAxis() * 2;
	final double t = (ex.x() + ex.y());
	if (t <= ((parameters._tileTextureWidth + parameters._tileTextureHeight) * 1.75))
	{
	  return true;
	}
  
  
	int __TODO_tune_render_budget;
	if (trc.getParameters()._useTilesSplitBudget)
	{
	  if (_subtiles == null) // the tile needs to create the subtiles
	  {
		if (trc.getStatistics().getSplitsCountInFrame() > 1)
		{
		  // there are not more splitsCount-budget to spend
		  return true;
		}
  
		if (trc.getLastSplitTimer().elapsedTime().milliseconds() < 25)
		{
		  // there are not more time-budget to spend
		  return true;
		}
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

  private void rawRender(RenderContext rc, TileRenderContext trc)
  {
  
	Mesh tessellatorMesh = getTessellatorMesh(rc, trc);
  
	TileTexturizer texturizer = trc.getTexturizer();
  
	if (tessellatorMesh != null)
	{
	  if (texturizer == null)
	  {
		tessellatorMesh.render(rc);
	  }
	  else
	  {
  
		final boolean needsToCallTexturizer = (!isTextureSolved() || (_texturizerMesh == null)) && isTexturizerDirty();
  
		if (needsToCallTexturizer)
		{
		  int __TODO_tune_render_budget;
  
		  //                               (_texturizerTimer->elapsedTime().milliseconds() > 125 &&
		  //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
		  //                               (_texturizerTimer->elapsedTime().milliseconds() > 125 &&
		  //                                lastTexturizerTimer->elapsedTime().milliseconds() > 50));
		  //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
		  //                                     (_texturizerTimer->elapsedTime().milliseconds() > 100 &&
		  //                                      lastTexturizerTimer->elapsedTime().milliseconds() > 10));
		  //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
		  //                                     (_texturizerTimer->elapsedTime().milliseconds() > 100));
  //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
  //                                     (_texturizerTimer->elapsedTime().milliseconds() > 50)) && isTexturizerDirty();
  
				  final boolean callTexturizer = true;
  //        const bool callTexturizer = (trc->getLastTexturizerTimer()->elapsedTime().milliseconds() > 10);
  //        const bool callTexturizer = (trc->getLastTexturizerTimer()->elapsedTime().milliseconds() > 10);
  
		  if (callTexturizer)
		  {
			_texturizerMesh = texturizer.texturize(rc, trc, this, tessellatorMesh, _texturizerMesh);
			//trc->getLastTexturizerTimer()->start();
  
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

  private void debugRender(RenderContext rc, TileRenderContext trc)
  {
	Mesh debugMesh = getDebugMesh(rc, trc);
	if (debugMesh != null)
	{
	  debugMesh.render(rc);
	}
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column)
  {
	return new Tile(_texturizer, this, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), level, row, column);
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

  private void prune(TileRenderContext trc)
  {
	if (_subtiles != null)
	{
  
  //    printf("= pruned tile %s\n", getKey().description().c_str());
  
	  TileTexturizer texturizer = (trc == null) ? null : trc.getTexturizer();
  
	  final int subtilesSize = _subtiles.size();
	  for (int i = 0; i < subtilesSize; i++)
	  {
		Tile subtile = _subtiles.get(i);
  
		subtile.setIsVisible(false);
  
		subtile.prune(trc);
		if (texturizer != null)
		{
		  texturizer.tileToBeDeleted(subtile, subtile._texturizerMesh);
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

  private void ancestorTexturedSolvedChanged(Tile ancestor, boolean textureSolved)
  {
	if (textureSolved && isTextureSolved())
	{
	  return;
	}
  
	if (_texturizer != null)
	{
	  _texturizer.ancestorTexturedSolvedChanged(this, ancestor, textureSolved);
	}
  
	if (_subtiles != null)
	{
	  final int subtilesSize = _subtiles.size();
	  for (int i = 0; i < subtilesSize; i++)
	  {
		Tile subtile = _subtiles.get(i);
		subtile.ancestorTexturedSolvedChanged(ancestor, textureSolved);
	  }
	}
  }

  private boolean _isVisible;
  private void setIsVisible(boolean isVisible)
  {
  
  
	if (_isVisible != isVisible)
	{
	  _isVisible = isVisible;
  
	  if (_isVisible)
	  {
		//      visibleCounter++;
		//      printf("**** Tile %s becomed Visible (visibles=%ld)\n",
		//             getKey().description().c_str(),
		//             visibleCounter);
	  }
	  else
	  {
		//      visibleCounter--;
		//      printf("**** Tile %s becomed INVisible (visibles=%ld)\n",
		//             getKey().description().c_str(),
		//             visibleCounter);
		deleteTexturizerMesh();
	  }
	}
  }

  private void deleteTexturizerMesh()
  {
	if ((_level > 0) && (_texturizerMesh != null))
	{
	  _texturizer.tileMeshToBeDeleted(this, _texturizerMesh);
  
	  if (_texturizerMesh != null)
		  _texturizerMesh.dispose();
	  _texturizerMesh = null;
  
	  _texturizerData = null;
  
	  setTexturizerDirty(true);
	  setTextureSolved(false);
	}
  }

  private ITexturizerData _texturizerData;

  public Tile(TileTexturizer texturizer, Tile parent, Sector sector, int level, int row, int column)
  {
	  _texturizer = texturizer;
	  _parent = parent;
	  _sector = new Sector(sector);
	  _level = level;
	  _row = row;
	  _column = column;
	  _tessellatorMesh = null;
	  _debugMesh = null;
	  _texturizerMesh = null;
	  _textureSolved = false;
	  _texturizerDirty = true;
	  _subtiles = null;
	  _justCreatedSubtiles = false;
	  _texturizerTimer = null;
	  _isVisible = false;
	  _texturizerData = null;
  }


  //static long visibleCounter = 0;
  
  public void dispose()
  {
	if (_isVisible)
	{
	  deleteTexturizerMesh();
	}
  
	prune(null);
  
	//  if (_isVisible) {
	//    visibleCounter--;
	//    printf("**** Tile %s is DESTROYED (visibles=%ld)\n",
	//           getKey().description().c_str(),
	//           visibleCounter);
	//  }
  
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
  
	if (_texturizerData != null)
	{
	  _texturizerData = null;
	}
  
	if (_texturizerMesh != null)
	{
	  if (_texturizerMesh != null)
		  _texturizerMesh.dispose();
	}
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getSector() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Tile* getParent() const
  public final Tile getParent()
  {
	return _parent;
  }

  public final void render(RenderContext rc, TileRenderContext trc, java.util.LinkedList<Tile> toVisitInNextIteration)
  {
	TilesStatistics statistics = trc.getStatistics();
  
	statistics.computeTileProcessed(this);
	if (isVisible(rc, trc))
	{
	  setIsVisible(true);
  
	  statistics.computeVisibleTile(this);
  
	  if ((toVisitInNextIteration == null) || meetsRenderCriteria(rc, trc))
	  {
		rawRender(rc, trc);
		if (trc.getParameters()._renderDebug)
		{
		  debugRender(rc, trc);
		}
  
		statistics.computeTileRendered(this);
  
		prune(trc);
	  }
	  else
	  {
		java.util.ArrayList<Tile> subTiles = getSubTiles();
		if (_justCreatedSubtiles)
		{
		  trc.getLastSplitTimer().start();
		  statistics.computeSplitInFrame();
		  _justCreatedSubtiles = false;
		}
  
		final int subTilesSize = subTiles.size();
		for (int i = 0; i < subTilesSize; i++)
		{
		  Tile subTile = subTiles.get(i);
		  toVisitInNextIteration.addLast(subTile);
		}
	  }
	}
	else
	{
	  setIsVisible(false);
  
	  prune(trc);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileKey getKey() const
  public final TileKey getKey()
  {
	return new TileKey(_level, _row, _column);
  }

  public final void setTextureSolved(boolean textureSolved)
  {
	if (textureSolved != _textureSolved)
	{
	  _textureSolved = textureSolved;
  
	  if (_subtiles != null)
	  {
		final int subtilesSize = _subtiles.size();
		for (int i = 0; i < subtilesSize; i++)
		{
		  Tile subtile = _subtiles.get(i);
		  subtile.ancestorTexturedSolvedChanged(this, _textureSolved);
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTextureSolved() const
  public final boolean isTextureSolved()
  {
	return _textureSolved;
  }

  public final void setTexturizerDirty(boolean texturizerDirty)
  {
	_texturizerDirty = texturizerDirty;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTexturizerDirty() const
  public final boolean isTexturizerDirty()
  {
	return _texturizerDirty;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D intersection(const Vector3D& origin, const Vector3D& ray, const Planet* planet) const
  public final Geodetic3D intersection(Vector3D origin, Vector3D ray, Planet planet)
  {
	  //As our tiles are still flat our onPlanet vector is calculated directly from Planet
	java.util.ArrayList<Double> ts = planet.intersections(origin, ray);
  
	if (ts.size() > 0)
	{
	  final Vector3D onPlanet = origin.add(ray.times(ts.get(0)));
	  final Geodetic3D g = planet.toGeodetic3D(onPlanet);
  
	  if (_sector.contains(g))
	  {
		//If this tile is not a leaf
		if (_subtiles != null)
		{
		  for (int i = 0; i < _subtiles.size(); i++)
		  {
			final Geodetic3D g3d = _subtiles.get(i).intersection(origin, ray, planet);
			if (!g3d.isNan())
			{
			  return g3d;
			}
		  }
		}
		else
		{
		  //printf("TOUCH TILE %d\n", _level);
		  _texturizer.onTerrainTouchEvent(g, this);
		  return g;
		}
	  }
	}
  
	return Geodetic3D.nan();
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasTexturizerData() const
  public final boolean hasTexturizerData()
  {
	return (_texturizerData != null);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ITexturizerData* getTexturizerData() const
  public final ITexturizerData getTexturizerData()
  {
	return _texturizerData;
  }

  public final void setTexturizerData(ITexturizerData texturizerData)
  {
	_texturizerData = texturizerData;
  }

}