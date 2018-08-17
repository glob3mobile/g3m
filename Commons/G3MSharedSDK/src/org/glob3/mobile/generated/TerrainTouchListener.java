package org.glob3.mobile.generated;//
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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2F;

public abstract class TerrainTouchListener
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif

  public abstract boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile);

}
