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

  public void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    final double radii = planet.getRadii().maxAxis();
  
    final Geodetic3D cameraPosition3D = planet.toGeodetic3D(nextCamera.getCartesianPosition());
    final double cameraHeight = cameraPosition3D.height();
  
    if (cameraHeight > radii *9)
    {
      nextCamera.resetPosition();
      nextCamera.setPosition(planet.toGeodetic3D(previousCamera.getCartesianPosition()));
    }
  }

}