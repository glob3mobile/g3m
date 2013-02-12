package org.glob3.mobile.generated; 
//
//  ICameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

//
//  ICameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//


//class Planet;
//class Camera;

public interface ICameraConstrainer
{
  public void dispose();

  void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera);
}