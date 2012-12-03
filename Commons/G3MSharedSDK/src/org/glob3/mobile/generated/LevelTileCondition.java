package org.glob3.mobile.generated; 
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
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const G3MRenderContext* rc, const Tile* tile) const
  public final boolean isAvailable(G3MRenderContext rc, Tile tile)
  {
	final int level = tile.getLevel();
	return ((level >= _minLevel) && (level <= _maxLevel));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const G3MEventContext* ec, const Tile* tile) const
  public final boolean isAvailable(G3MEventContext ec, Tile tile)
  {
	final int level = tile.getLevel();
	return ((level >= _minLevel) && (level <= _maxLevel));
  }

}