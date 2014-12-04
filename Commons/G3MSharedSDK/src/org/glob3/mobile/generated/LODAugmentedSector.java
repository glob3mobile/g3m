package org.glob3.mobile.generated; 
//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  PlanetRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Tile;
//class TileTessellator;
//class LayerSet;
//class VisibleSectorListenerEntry;
//class VisibleSectorListener;
//class ElevationDataProvider;
//class LayerTilesRenderParameters;
//class TerrainTouchListener;
//class ChangedInfoListener;
//class TileRenderingListener;




//class EllipsoidShape;


public class LODAugmentedSector
{
  public Sector _sector;
  public double _lodFactor;

  public LODAugmentedSector(Sector sector, double factor)
  {
     _sector = new Sector(sector);
     _lodFactor = factor;
  }

  //CANT DO THIS
  //    ~LODAugmentedSector(){
  //      delete _sector;
  //    }
}