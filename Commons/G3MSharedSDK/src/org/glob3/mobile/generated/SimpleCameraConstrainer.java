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
  //  _previousCameraTimestamp(0),
  //  _nextCameraTimestamp(0)
  {

  }


  public void dispose()
  {
  }

  public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    //  long long previousCameraTimestamp = previousCamera->getTimestamp();
    //  long long nextCameraTimestamp = nextCamera->getTimestamp();
    //  if (previousCameraTimestamp != _previousCameraTimestamp || nextCameraTimestamp != _nextCameraTimestamp) {
    //    _previousCameraTimestamp = previousCameraTimestamp;
    //    _nextCameraTimestamp = nextCameraTimestamp;
    //    ILogger::instance()->logInfo("Cameras Timestamp: Previous=%lld; Next=%lld\n",
    //                                 _previousCameraTimestamp, _nextCameraTimestamp);
    //  }
  
    final double radii = planet.getRadii().maxAxis();
    final double maxHeight = radii *9;
    final double minHeight = 10;
  
    final double height = nextCamera.getGeodeticPosition()._height;
  
    if ((height < minHeight) || (height > maxHeight))
    {
      nextCamera.copyFrom(previousCamera);
    }
  
    return true;
  }

  //private:
  //  mutable long long _previousCameraTimestamp;
  //  mutable long long _nextCameraTimestamp;
}