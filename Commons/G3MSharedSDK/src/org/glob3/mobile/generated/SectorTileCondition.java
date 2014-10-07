package org.glob3.mobile.generated; 
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
  private final Sector _sector ;

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
  
  public final boolean isAvailable(Tile tile)
  {
    return _sector.touchesWith(tile._sector);
  }

  public final SectorTileCondition copy()
  {
    return new SectorTileCondition(_sector);
  }

}