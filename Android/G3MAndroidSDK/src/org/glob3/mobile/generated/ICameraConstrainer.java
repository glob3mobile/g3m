package org.glob3.mobile.generated; 
//
//  CameraConstraints.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 09/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




public interface ICameraConstrainer
{
  //NO DESTRUCTOR FOR INTERFACE

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void onCameraChange(const Planet *planet, const Camera* previousCamera, Camera* nextCamera) const = 0;
  void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera);
}