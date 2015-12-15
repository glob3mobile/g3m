package org.glob3.mobile.generated; 
//
//  MaxLevelTileLoDTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

//
//  MaxLevelTileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//



public class MaxLevelTileLoDTester extends TileLoDTester
{

  protected final boolean _meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {

    if (tile._level >= _maxLevel)
    {
      return true;
    }

    if (tile._sector.touchesPoles())
    {
      if (tile._level >= _maxLevelForPoles)
      {
        return true;
      }
    }

    return false;
  }

  protected final boolean _isVisible(int testerLevel, Tile tile, G3MRenderContext rc)
  {
    return true;
  }

  protected int _maxLevelForPoles;
  protected int _maxLevel;


  public MaxLevelTileLoDTester(int maxLevel, int maxLevelForPoles, TileLoDTester nextTesterRightLoD, TileLoDTester nextTesterWrongLoD, TileLoDTester nextTesterVisible, TileLoDTester nextTesterNotVisible)
  {
     super(nextTesterRightLoD, nextTesterWrongLoD, nextTesterVisible, nextTesterNotVisible);
     _maxLevelForPoles = maxLevelForPoles;
     _maxLevel = maxLevel;
  }


  public void dispose()
  {
  }

}