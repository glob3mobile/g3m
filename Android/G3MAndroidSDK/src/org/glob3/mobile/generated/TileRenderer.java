package org.glob3.mobile.generated; 
public class TileRenderer extends Renderer
{
  private final TileTessellator _tessellator;
  private TileTexturizer _texturizer;
  private final TilesRenderParameters _parameters;
  private final boolean _showStatistics;
  private boolean _topTilesJustCreated;

  private Camera _lastCamera;

  private java.util.ArrayList<Tile> _topLevelTiles = new java.util.ArrayList<Tile>();

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles
//  ITimer* _lastTexturizerTimer; // timer to start every time the texturizer is called

  private void clearTopLevelTiles()
  {
	for (int i = 0; i < _topLevelTiles.size(); i++)
	{
	  Tile tile = _topLevelTiles.get(i);
	}
  
	_topLevelTiles.clear();
  }
  private void createTopLevelTiles(InitializationContext ic)
  {
	final Angle fromLatitude = _parameters._topSector.lower().latitude();
	final Angle fromLongitude = _parameters._topSector.lower().longitude();
  
	final Angle deltaLan = _parameters._topSector.getDeltaLatitude();
	final Angle deltaLon = _parameters._topSector.getDeltaLongitude();
  
	final Angle tileHeight = deltaLan.div(_parameters._splitsByLatitude);
	final Angle tileWidth = deltaLon.div(_parameters._splitsByLongitude);
  
	for (int row = 0; row < _parameters._splitsByLatitude; row++)
	{
	  final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
	  final Angle tileLatTo = tileLatFrom.add(tileHeight);
  
	  for (int col = 0; col < _parameters._splitsByLongitude; col++)
	  {
		final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
		final Angle tileLonTo = tileLonFrom.add(tileWidth);
  
		final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
		final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
		final Sector sector = new Sector(tileLower, tileUpper);
  
		Tile tile = new Tile(_texturizer, null, sector, _parameters._topLevel, row, col);
		_topLevelTiles.add(tile);
	  }
	}
  
	ic.getLogger().logInfo("Created %d top level tiles", _topLevelTiles.size());
  
	_topTilesJustCreated = true;
  }

  private TilesStatistics _lastStatistics = new TilesStatistics();

  private boolean _firstRender;


	private static class DistanceToCenterTileComparison implements java.util.Comparator<Tile> {
		private final Camera _camera;
		private Planet _planet;
		
		public DistanceToCenterTileComparison(Camera camera, Planet planet) {
			_camera = camera;
			_planet = planet;
		}
	  
		public void initialize() {}
		
		public int compare(Tile t1, Tile t2) {
			final Vector3D cameraPos = _camera.getPosition();
			
			final Vector3D center1 = _planet.toVector3D(t1.getSector().getCenter());
			final Vector3D center2 = _planet.toVector3D(t2.getSector().getCenter());
			
			final double dist1 = center1.sub(cameraPos).squaredLength();
			final double dist2 = center2.sub(cameraPos).squaredLength();
			
			if (dist1 < dist2) {
				return -1;
			}
			else if (dist1 > dist2) {
				return 1;
			}
			return 0;
		}
	}


  //bool isTile1ClosestToCameraThanTile2(Tile *t1, Tile *t2) const;


  public TileRenderer(TileTessellator tessellator, TileTexturizer texturizer, TilesRenderParameters parameters, boolean showStatistics)
//  _lastTexturizerTimer(NULL),
  {
	  _tessellator = tessellator;
	  _texturizer = texturizer;
	  _parameters = parameters;
	  _showStatistics = showStatistics;
	  _lastStatistics = new TilesStatistics();
	  _topTilesJustCreated = false;
	  _lastSplitTimer = null;
	  _lastCamera = null;
	  _firstRender = false;

  }

  public void dispose()
  {
	clearTopLevelTiles();
  
  }

  public final void initialize(InitializationContext ic)
  {
	clearTopLevelTiles();
	createTopLevelTiles(ic);
  
	if (_lastSplitTimer != null)
	{
	  if (_lastSplitTimer != null)
		  _lastSplitTimer.dispose();
	}
	_lastSplitTimer = ic.getFactory().createTimer();
  
  //  if (_lastTexturizerTimer != NULL) {
  //    delete _lastTexturizerTimer;
  //  }
  //  _lastTexturizerTimer = ic->getFactory()->createTimer();
  
	_texturizer.initialize(ic, _parameters);
  
  }

  public final int render(RenderContext rc)
  {
	TilesStatistics statistics = new TilesStatistics();
	//Saving camera for Long Press Event
	_lastCamera = rc.getCurrentCamera();
  
	TileRenderContext trc = new TileRenderContext(_tessellator, _texturizer, _parameters, statistics, _lastSplitTimer, _firstRender); // if first render, force full render
  //                        _lastTexturizerTimer,
  
	if (_firstRender)
	{
	  // force one render of the topLevel tiles to make the (toplevel) textures loaded as they
	  // will be used as last-change fallback texture for any tile.
	  _firstRender = false;
  
	  for (int i = 0; i < _topLevelTiles.size(); i++)
	  {
		Tile tile = _topLevelTiles.get(i);
		tile.render(rc, trc, null);
	  }
	}
	else
	{
	  java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
	  for (int i = 0; i < _topLevelTiles.size(); i++)
	  {
		toVisit.addLast(_topLevelTiles.get(i));
	  }
  
  //    DistanceToCenterTileComparison predicate = DistanceToCenterTileComparison(rc->getNextCamera(),
  //                                                                              rc->getPlanet());
  
	  while (toVisit.size() > 0)
	  {
		java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();
  
		//    std::sort(toVisit.begin(),
		//              toVisit.end(),
		//              predicate);
  
  //      predicate.initialize();
  //      toVisit.sort(predicate);
  
		for (java.util.Iterator<Tile> iter = toVisit.iterator(); iter.hasNext();)
		{
		  Tile tile = iter.next();
  
		  tile.render(rc, trc, toVisitInNextIteration);
		}
  
		toVisit = toVisitInNextIteration;
		//    toVisitInNextIteration.clear();
	  }
	}
  
	if (_showStatistics)
	{
	  if (!_lastStatistics.equalsTo(statistics))
	  {
		_lastStatistics = statistics;
		statistics.log(rc.getLogger());
	  }
	}
	return Renderer.maxTimeToRender;
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
  
	if (touchEvent.getType() == TouchEventType.LongPress)
	{
  
	  if (_lastCamera != null)
	  {
		Vector2D pixel = touchEvent.getTouch(0).getPos();
		Vector3D ray = _lastCamera.pixel2Ray(pixel);
		Vector3D origin = _lastCamera.getPosition();
  
		for(int i = 0; i < _topLevelTiles.size(); i++)
		{
  
		  Geodetic3D g = _topLevelTiles.get(i).intersection(origin, ray, ec.getPlanet());
		  if (!g.isNan())
		  {
			System.out.printf("G: %f, %f, %f\n", g.latitude().degrees(), g.longitude().degrees(), g.height());
		  }
		}
  
	  }
  
	  return true;
	}
  
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	if (_topTilesJustCreated)
	{
  
	  if (_texturizer != null)
	  {
		final int topLevelTilesSize = _topLevelTiles.size();
		for (int i = 0; i < topLevelTilesSize; i++)
		{
		  Tile tile = _topLevelTiles.get(i);
		  _texturizer.justCreatedTopTile(rc, tile);
		}
	  }
	  _topTilesJustCreated = false;
	}
  
  
	if (_tessellator != null)
	{
	  if (!_tessellator.isReady(rc))
	  {
		return false;
	  }
	}
  
	if (_texturizer != null)
	{
	  if (!_texturizer.isReady(rc))
	  {
		return false;
	  }
	}
  
	return true;
  }


  public final void start()
  {
	_firstRender = true;
  }

  public final void stop()
  {
	_firstRender = false;
  }

}