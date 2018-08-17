package org.glob3.mobile.generated;import java.util.*;

//
//  Geodetic3DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//

//
//  Geodetic3DProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PlanetRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;

public abstract class InitialCameraPositionProvider
{

  public void dispose()
  {
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic3D getCameraPosition(const Planet* planet, const PlanetRenderer* planetRenderer) const = 0;
  public abstract Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer);
}
