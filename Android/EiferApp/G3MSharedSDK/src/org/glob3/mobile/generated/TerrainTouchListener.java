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
//class Vector2F;

public interface TerrainTouchListener
{
  void dispose();

  boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile);

}