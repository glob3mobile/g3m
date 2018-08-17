package org.glob3.mobile.generated;import java.util.*;

//
//  SectorTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/4/13.
//
//

//
//  SectorTileCondition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/4/13.
//
//



public class SectorTileCondition extends LayerCondition
{
  private final Sector _sector = new Sector();

  public SectorTileCondition(Sector sector)
  {
	  _sector = new Sector(sector);
  }

//  bool isAvailable(const G3MRenderContext* rc,
//                   const Tile* tile) const;
//
//  bool isAvailable(const G3MEventContext* ec,
//                   const Tile* tile) const;


  //bool SectorTileCondition::isAvailable(const G3MRenderContext* rc,
  //                                      const Tile* tile) const {
  //  return _sector.touchesWith(tile->_sector);
  //}
  //
  //bool SectorTileCondition::isAvailable(const G3MEventContext* ec,
  //                                      const Tile* tile) const {
  //  return _sector.touchesWith(tile->_sector);
  //}
  
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const Tile* tile) const
  public final boolean isAvailable(Tile tile)
  {
	return _sector.touchesWith(tile._sector);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SectorTileCondition* copy() const
  public final SectorTileCondition copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new SectorTileCondition(_sector);
	return new SectorTileCondition(new Sector(_sector));
  }

}
