package org.glob3.mobile.generated;import java.util.*;

//
//  SurfaceElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

//
//  SurfaceElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Angle;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;



public abstract class SurfaceElevationListener
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

  public abstract void elevationChanged(Geodetic2D position, double rawElevation, double verticalExaggeration); //Without considering vertical exaggeration

  public abstract void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration); //Without considering vertical exaggeration
}
