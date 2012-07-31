package org.glob3.mobile.generated; 
public class TilesStatistics
{
  private int _counter;
  private int _minLevel;
  private int _maxLevel;
  private int _splitsCountInFrame;

  public TilesStatistics(TileParameters parameters)
  {
	  _counter = 0;
	  _minLevel = parameters._maxLevel + 1;
	  _maxLevel = parameters._topLevel - 1;
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

  public final void computeTileRender(Tile tile)
  {
	_counter++;

	int level = tile.getLevel();
	if (level < _minLevel)
	{
	  _minLevel = level;
	}
	if (level > _maxLevel)
	{
	  _maxLevel = level;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void log(const ILogger* logger) const
  public final void log(ILogger logger)
  {
	logger.logInfo("Rendered %d tiles. Levels: %d-%d", _counter, _minLevel, _maxLevel);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean equalsTo(const TilesStatistics& that) const
  public final boolean equalsTo(TilesStatistics that)
  {
	if (_counter != that._counter)
	{
	  return false;
	}
	if (_minLevel != that._minLevel)
	{
	  return false;
	}
	if (_maxLevel != that._maxLevel)
	{
	  return false;
	}

	return true;
  }

}