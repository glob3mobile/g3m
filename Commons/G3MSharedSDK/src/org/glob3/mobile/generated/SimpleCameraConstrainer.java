package org.glob3.mobile.generated;
//
//  SimpleCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

//
//  SimpleCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//




public class SimpleCameraConstrainer implements ICameraConstrainer
{

  public SimpleCameraConstrainer()
  {
  }


  public void dispose()
  {
  }

  public final boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
    final double radii = planet.getRadii().maxAxis();
    final double maxHeight = radii *9;
    final double minHeight = 10;
  
    final double height = nextCamera.getGeodeticPosition()._height;
  
    if ((height < minHeight) || (height > maxHeight))
    {
      nextCamera.copyFrom(previousCamera, true);
    }
  
    return true;
  }

}
