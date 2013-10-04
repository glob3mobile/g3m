//
//  TerrainTouchListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/4/13.
//
//

#ifndef __G3MiOSSDK__TerrainTouchListener__
#define __G3MiOSSDK__TerrainTouchListener__

class TerrainTouchListener {
public:
#ifdef C_CODE
  virtual ~TerrainTouchListener() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif
};

#endif
