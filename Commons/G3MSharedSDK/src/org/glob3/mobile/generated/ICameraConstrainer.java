package org.glob3.mobile.generated;//
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

public abstract class ICameraConstrainer
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

  //Returns false if it could not create a valid nextCamera
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean onCameraChange(const Planet* planet, const Camera* previousCamera, Camera* nextCamera) const = 0;
  public abstract boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera);
}
