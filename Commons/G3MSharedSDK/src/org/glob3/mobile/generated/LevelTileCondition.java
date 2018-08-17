package org.glob3.mobile.generated;import java.util.*;

//
//  LevelTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  LevelTileCondition.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//



public class LevelTileCondition extends LayerCondition
{
  private final int _minLevel;
  private final int _maxLevel;

  public LevelTileCondition(int minLevel, int maxLevel)
  {
	  _minLevel = minLevel;
	  _maxLevel = maxLevel;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

//  bool isAvailable(const G3MRenderContext* rc,
//                   const Tile* tile) const;
//  
//  bool isAvailable(const G3MEventContext* ec,
//                   const Tile* tile) const;


  //bool LevelTileCondition::isAvailable(const G3MRenderContext* rc,
  //                                     const Tile* tile) const {
  //  const int level = tile->_level;
  //  return ((level >= _minLevel) &&
  //          (level <= _maxLevel));
  //}
  //
  //bool LevelTileCondition::isAvailable(const G3MEventContext* ec,
  //                                     const Tile* tile) const {
  //  const int level = tile->_level;
  //  return ((level >= _minLevel) &&
  //          (level <= _maxLevel));
  //}
  
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const Tile* tile) const
  public final boolean isAvailable(Tile tile)
  {
	final int level = tile._level;
	return ((level >= _minLevel) && (level <= _maxLevel));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LayerCondition* copy() const
  public final LayerCondition copy()
  {
	return new LevelTileCondition(_minLevel, _maxLevel);
  }

}
