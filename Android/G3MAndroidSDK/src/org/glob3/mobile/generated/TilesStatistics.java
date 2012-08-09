package org.glob3.mobile.generated; 
public class TilesStatistics
{
  private int _tilesProcessed;
  private java.util.HashMap<Integer, Integer> _tilesProcessedByLevel = new java.util.HashMap<Integer, Integer>();

  private int _tilesVisible;
  private java.util.HashMap<Integer, Integer> _tilesVisibleByLevel = new java.util.HashMap<Integer, Integer>();

  private int _tilesRendered;
  private java.util.HashMap<Integer, Integer> _tilesRenderedByLevel = new java.util.HashMap<Integer, Integer>();

  private int _splitsCountInFrame;


  public TilesStatistics()
  {
	  _tilesProcessed = 0;
	  _tilesVisible = 0;
	  _tilesRendered = 0;
	  _splitsCountInFrame = 0;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getSplitsCountInFrame() const
  public final int getSplitsCountInFrame()
  {
	return _splitsCountInFrame;
  }

  public final void computeSplit()
  {
	_splitsCountInFrame++;
  }

  public final void computeTileProcessed(Tile tile)
  {
	_tilesProcessed++;

	final int level = tile.getLevel();
	_tilesProcessedByLevel.put(level, _tilesProcessedByLevel.get(level) + 1);
  }

  public final void computeVisibleTile(Tile tile)
  {
	_tilesVisible++;

	final int level = tile.getLevel();
	_tilesVisibleByLevel.put(level, _tilesVisibleByLevel.get(level) + 1);
  }

  public final void computeTileRendered(Tile tile)
  {
	_tilesRendered++;

	final int level = tile.getLevel();
	_tilesRenderedByLevel.put(level, _tilesRenderedByLevel.get(level) + 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean equalsTo(const TilesStatistics& that) const
  public final boolean equalsTo(TilesStatistics that)
  {
	if (_tilesProcessed != that._tilesProcessed)
	{
	  return false;
	}
	if (_tilesRendered != that._tilesRendered)
	{
	  return false;
	}
	if (_tilesRenderedByLevel != that._tilesRenderedByLevel)
	{
	  return false;
	}
	if (_tilesProcessedByLevel != that._tilesProcessedByLevel)
	{
	  return false;
	}
	return true;
  }

  public static String asLogString(java.util.HashMap<Integer, Integer> map)
  {
	std.ostringstream buffer = new std.ostringstream();

	boolean first = true;
	for(java.util.Iterator<Integer, Integer> i = map.iterator(); i.hasNext();)
	{
	  final int level = i.next().getKey();
	  final int counter = i.next().getValue();

	  if (first)
	  {
		first = false;
	  }
	  else
	  {
		buffer << ",";
	  }
	  buffer << "L" << level << ":" << counter;
	}

	return buffer.str();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void log(const ILogger* logger) const
  public final void log(ILogger logger)
  {
	logger.logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).", _tilesProcessed, asLogString(_tilesProcessedByLevel), _tilesVisible, asLogString(_tilesVisibleByLevel), _tilesRendered, asLogString(_tilesRenderedByLevel));
  }

}