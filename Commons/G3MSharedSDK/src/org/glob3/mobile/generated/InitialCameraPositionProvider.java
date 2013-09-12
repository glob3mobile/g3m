package org.glob3.mobile.generated; 
//
//  Geodetic3DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//

//
//  Geodetic3DProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//




//class PlanetRenderer;
//class Planet;
//class Vector2I;

public abstract class InitialCameraPositionProvider
{

  public void dispose()
  {
  }
  public abstract Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer, Vector2I viewportExtent);
}