package org.glob3.mobile.generated;import java.util.*;

//
//  MaxLevelTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

//
//  MaxLevelTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//




public class MaxLevelTileLODTester extends TileLODTester
{
  private int _maxLevel;
  private int _maxLevelForPoles;



  public MaxLevelTileLODTester()
  {
	  _maxLevel = -1;
	  _maxLevelForPoles = -1;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean meetsRenderCriteria(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
	if (_maxLevel < 0)
	{
	  return true;
	}
  
	if (tile._level >= _maxLevel)
	{
	  return true;
	}
  
	if ((tile._level >= _maxLevelForPoles) && (tile._sector.touchesPoles()))
	{
	  return true;
	}
  
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTileHasChangedMesh(const Tile* tile) const
  public final void onTileHasChangedMesh(Tile tile)
  {

  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
	if (ltrp == null)
	{
	  _maxLevel = -1;
	  _maxLevelForPoles = -1;
	}
	else
	{
	  _maxLevel = ltrp._maxLevel;
	  _maxLevelForPoles = ltrp._maxLevelForPoles;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void renderStarted() const
  public final void renderStarted()
  {

  }

}
