package org.glob3.mobile.generated;
//
//  ICameraConstrainer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

//
//  ICameraConstrainer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//


//class Planet;
//class Camera;


public interface ICameraConstrainer
{
  void dispose();

  //Returns false if it could not create a valid nextCamera
  boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera);
}