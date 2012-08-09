package org.glob3.mobile.generated; 
public class TileRenderer extends Renderer
{
  private final TileTessellator _tessellator;
  private TileTexturizer _texturizer;
  private final TileParameters _parameters;
  private final boolean _showStatistics;
  private boolean _topTilesJustCreated;

  private java.util.ArrayList<Tile> _topLevelTiles = new java.util.ArrayList<Tile>();

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles
  private ITimer _lastTexturizerTimer; // timer to start every time the texturizer is called

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
  
		Tile tile = new Tile(null, sector, _parameters._topLevel, row, col);
		_topLevelTiles.add(tile);
	  }
	}
  
	ic.getLogger().logInfo("Created %i top level tiles", _topLevelTiles.size());
  
	_topTilesJustCreated = true;
  }

  private TilesStatistics _lastStatistics = new TilesStatistics();



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


  public TileRenderer(TileTessellator tessellator, TileTexturizer texturizer, TileParameters parameters, boolean showStatistics)
  {
	  _tessellator = tessellator;
	  _texturizer = texturizer;
	  _parameters = parameters;
	  _showStatistics = showStatistics;
	  _lastStatistics = new TilesStatistics();
	  _topTilesJustCreated = false;
	  _lastSplitTimer = null;
	  _lastTexturizerTimer = null;

  }

  public void dispose()
  {
	clearTopLevelTiles();
  
  }

  public final void initialize(InitializationContext ic)
  {
	clearTopLevelTiles();
	createTopLevelTiles(ic);
  
	_lastSplitTimer = ic.getFactory().createTimer();
	_lastTexturizerTimer = ic.getFactory().createTimer();
  
	_texturizer.initialize(ic);
  }

  public final int render(RenderContext rc)
  {
	TilesStatistics statistics = new TilesStatistics();
  
	final int topLevelTilesSize = _topLevelTiles.size();
  
  
	DistanceToCenterTileComparison predicate = new DistanceToCenterTileComparison(rc.getCamera(), rc.getPlanet());
  
	if (_topTilesJustCreated)
	{
  
  	java.util.Collections.sort(_topLevelTiles, predicate);
  
	  if (_texturizer != null)
	  {
		for (int i = 0; i < topLevelTilesSize; i++)
		{
		  Tile tile = _topLevelTiles.get(i);
		  _texturizer.justCreatedTopTile(tile);
		}
	  }
	  _topTilesJustCreated = false;
	}
  
  //  std::vector<Tile*> toVisit(_topLevelTiles);
	java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
  
	for (int i = 0; i < _topLevelTiles.size(); i++)
	{
	  toVisit.addLast(_topLevelTiles.get(i));
	}
  
	while (toVisit.size() > 0)
	{
	  java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();
  
  	  java.util.Collections.sort(toVisit, predicate);
  
	  for (java.util.Iterator<Tile > i = toVisit.iterator(); i.hasNext();)
	  {
		Tile tile = i.next();
		tile.render(rc, _tessellator, _texturizer, _parameters, statistics, toVisitInNextIteration, _lastSplitTimer, _lastTexturizerTimer);
  
	  }
  
	  toVisit = toVisitInNextIteration;
  //    toVisitInNextIteration.clear();
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
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	if (_tessellator != null)
	{
	  if (!_tessellator.isReadyToRender(rc))
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

}