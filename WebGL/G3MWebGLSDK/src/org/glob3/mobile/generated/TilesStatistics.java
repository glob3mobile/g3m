package org.glob3.mobile.generated; 
public class TilesStatistics
{
  private int _tilesProcessed;
  //std::map<int, int> _tilesProcessedByLevel;

  private int _tilesVisible;
  //std::map<int, int> _tilesVisibleByLevel;

  private int _tilesRendered;
  //std::map<int, int> _tilesRenderedByLevel;

  private static final int _maxLOD = 30;

  private int[] _tilesProcessedByLevel = new int[_maxLOD];
  private int[] _tilesVisibleByLevel = new int[_maxLOD];
  private int[] _tilesRenderedByLevel = new int[_maxLOD];

  private int _splitsCountInFrame;
  private int _buildersStartsInFrame;


  public TilesStatistics()
  {
	  _tilesProcessed = 0;
	  _tilesVisible = 0;
	  _tilesRendered = 0;
	  _splitsCountInFrame = 0;
	  _buildersStartsInFrame = 0;
	for(int i = 0; i < _maxLOD; i++)
	{
	  _tilesProcessedByLevel[i] = _tilesVisibleByLevel[i] = _tilesRenderedByLevel[i] = 0;
	}
  }

  public void dispose()
  {
//    if (_buildersStartsInFrame > 0) {
//      printf("buildersStartsInFrame=%d\n", _buildersStartsInFrame);
//    }
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getSplitsCountInFrame() const
  public final int getSplitsCountInFrame()
  {
	return _splitsCountInFrame;
  }

  public final void computeSplitInFrame()
  {
	_splitsCountInFrame++;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getBuildersStartsInFrame() const
  public final int getBuildersStartsInFrame()
  {
	return _buildersStartsInFrame;
  }

  public final void computeBuilderStartInFrame()
  {
	_buildersStartsInFrame++;
  }

  public final void computeTileProcessed(Tile tile)
  {
	_tilesProcessed++;

	final int level = tile.getLevel();
	_tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;
  }

  public final void computeVisibleTile(Tile tile)
  {
	_tilesVisible++;

	final int level = tile.getLevel();
	_tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
  }

  public final void computeTileRendered(Tile tile)
  {
	_tilesRendered++;

	final int level = tile.getLevel();
	_tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;
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

//  static std::string asLogString(std::map<int, int> map) {
//    
//    bool first = true;
///#ifdef C_CODE
//    
//    IStringBuilder *isb = IStringBuilder::newStringBuilder();
//    for(std::map<int, int>::const_iterator i = map.begin();
//        i != map.end();
//        ++i ) {
//      const int level   = i->first;
//      const int counter = i->second;
//      
//      if (first) {
//        first = false;
//      }
//      else {
//        isb->add(",");
//      }
//      isb->add("L")->add(level)->add(":")->add(counter);
//    }
//    
//    std::string s = isb->getString();
//    delete isb;
//    return s;  
///#endif
///#ifdef JAVA_CODE
//    String res = "";
//    for (java.util.Map.Entry<Integer, Integer> i: map.entrySet()){
//		  final int level = i.getKey();
//		  final int counter = i.getValue();
//		  if (first){
//        first = false;
//		  }else{
//        res += ",";
//		  }
//		  res += "L" + level + ":" + counter;
//    }
//    return res;
///#endif
//  }

  public static String asLogString(int[] m, int nMax)
  {

	boolean first = true;
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	for(int i = 0; i < nMax; i++)
	{
	  final int level = i;
	  final int counter = m[i];
	  if (counter != 0)
	  {
		if (first)
		{
		  first = false;
		}
		else
		{
		  isb.add(",");
		}
		isb.add("L").add(level).add(":").add(counter);
	  }
	}

	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void log(const ILogger* logger) const
  public final void log(ILogger logger)
  {
	logger.logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).", _tilesProcessed, asLogString(_tilesProcessedByLevel, _maxLOD), _tilesVisible, asLogString(_tilesVisibleByLevel, _maxLOD), _tilesRendered, asLogString(_tilesRenderedByLevel, _maxLOD));
  }

}