package org.glob3.mobile.generated; 
//
//  TerrainTouchListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/4/13.
//
//

//
//  TerrainTouchListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/4/13.
//
//


//class G3MEventContext;
//class Camera;
//class Geodetic3D;
//class Tile;

public interface TerrainTouchListener
{
  public void dispose();

  boolean onTerrainTouch(G3MEventContext ec, Camera camera, Geodetic3D position, Tile tile);

}