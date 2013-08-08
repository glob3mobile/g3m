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

  public void dispose()
  {

  }

  public void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    final double radii = planet.getRadii().maxAxis();
    final double maxHeight = radii *9;
    final double minHeight = 10;
  
    final Geodetic3D cameraPosition = nextCamera.getGeodeticPosition();
    final double cameraHeight = cameraPosition._height;
  
    if (cameraHeight > maxHeight)
    {
      nextCamera.copyFrom(previousCamera);
      /*nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                      cameraPosition._longitude,
                                      maxHeight);*/
    }
    else if (cameraHeight < minHeight)
    {
      nextCamera.copyFrom(previousCamera);
      /*nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                      cameraPosition._longitude,
                                      minHeight);*/
    }
  
  }

}