//
//  TerrainTouchListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/4/13.
//
//

#ifndef __G3MiOSSDK__TerrainTouchListener__
#define __G3MiOSSDK__TerrainTouchListener__

class G3MEventContext;
class Camera;
class Geodetic3D;
class Tile;
class Vector2F;

class TerrainTouchListener {
public:
#ifdef C_CODE
  virtual ~TerrainTouchListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual bool onTerrainTouch(const G3MEventContext* ec,
                              const Vector2F&        pixel,
                              const Camera*          camera,
                              const Geodetic3D&      position,
                              const Tile*            tile) = 0;

};

#endif
