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
  //NO DESTRUCTOR FOR INTERFACE

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void onCameraChange(const Planet* planet, const Camera* previousCamera, Camera* nextCamera) const = 0;
  void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera);
}