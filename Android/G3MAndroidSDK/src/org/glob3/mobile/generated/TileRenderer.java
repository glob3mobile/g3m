package org.glob3.mobile.generated; 
public class TileRenderer extends Renderer
{
  private final TileTessellator _tessellator;
  private TileTexturizer _texturizer;
  private final TileParameters _parameters;
  private final boolean _showStatistics;
  private boolean _topTilesJustCreated;

  private java.util.ArrayList<Tile> _topLevelTiles = new java.util.ArrayList<Tile>();

  private ITimer _frameTimer; // timer started at the start of each frame rendering
  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

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


  private static class DistanceToCenterTileComparison
  {
	private final Camera _camera;
	private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

	public DistanceToCenterTileComparison(Camera camera, Planet planet)
	{
		_camera = camera;
		_planet = planet;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: inline boolean operator ()(const Tile *t1, const Tile *t2) const
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
	boolean operator ()(Tile t1, Tile t2)
	{
	  final Vector3D cameraPos = _camera.getPosition();

	  final Vector3D center1 = _planet.toVector3D(t1.getSector().getCenter());
	  final Vector3D center2 = _planet.toVector3D(t2.getSector().getCenter());

	  final double dist1 = center1.sub(cameraPos).squaredLength();
	  final double dist2 = center2.sub(cameraPos).squaredLength();
	  return (dist1 < dist2);
	}
  }


  //bool isTile1ClosestToCameraThanTile2(Tile *t1, Tile *t2) const;


  public TileRenderer(TileTessellator tessellator, TileTexturizer texturizer, TileParameters parameters, boolean showStatistics)
  {
	  _tessellator = tessellator;
	  _texturizer = texturizer;
	  _parameters = parameters;
	  _showStatistics = showStatistics;
	  _lastStatistics = new TilesStatistics(parameters);
	  _topTilesJustCreated = false;
	  _frameTimer = null;
	  _lastSplitTimer = null;

  }

  public void dispose()
  {
	clearTopLevelTiles();
  
  }

  public final void initialize(InitializationContext ic)
  {
	clearTopLevelTiles();
	createTopLevelTiles(ic);
  
	_frameTimer = ic.getFactory().createTimer();
	_lastSplitTimer = ic.getFactory().createTimer();
  }

  public final int render(RenderContext rc)
  {
	_frameTimer.start();
  
	TilesStatistics statistics = new TilesStatistics(_parameters);
  
	final int topLevelTilesSize = _topLevelTiles.size();
  
	if (_topTilesJustCreated)
	{
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
  
	final DistanceToCenterTileComparison predicate = new DistanceToCenterTileComparison(rc.getCamera(), rc.getPlanet());
  
	java.util.ArrayList<Tile> toVisit = new java.util.ArrayList<Tile>(_topLevelTiles);
	while (toVisit.size() > 0)
	{
	  java.util.ArrayList<Tile> toVisitInNextIteration = new java.util.ArrayList<Tile>();
  
	  std.sort(toVisit.iterator(), toVisit.end(), predicate);
  
	  final int toVisitSize = toVisit.size();
	  for (int i = 0; i < toVisitSize; i++)
	  {
		Tile tile = toVisit.get(i);
		tile.render(rc, _tessellator, _texturizer, _parameters, statistics, toVisitInNextIteration, _frameTimer, _lastSplitTimer);
	  }
  
	  toVisit = toVisitInNextIteration;
	  toVisitInNextIteration.clear();
	}
  
  
	if (_showStatistics)
	{
	  if (!_lastStatistics.equalsTo(statistics))
	  {
		_lastStatistics = statistics;
		statistics.log(rc.getLogger());
	  }
	}
  
	return MAX_TIME_TO_RENDER;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(int width, int height)
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
	  if (!_texturizer.isReadyToRender(rc))
	  {
		return false;
	  }
	}
  
	return true;
  }

}