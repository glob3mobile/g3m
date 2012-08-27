package org.glob3.mobile.generated; 
//
//  CameraConstraints.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 09/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




public abstract class ICameraConstrainer
{
  //NO DESTRUCTOR FOR INTERFACE
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean acceptsCamera(const Camera* camera, const Planet *planet) const = 0;
  public abstract boolean acceptsCamera(Camera camera, Planet planet);
}