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
  private final double _minHeight;
  private final double _maxHeight;
  private final double _minHeightPlanetRadiiFactor;
  private final double _maxHeightPlanetRadiiFactor;

  private SimpleCameraConstrainer(double minHeight, double maxHeight, double minHeightPlanetRadiiFactor, double maxHeightPlanetRadiiFactor)
  {
     _minHeight = minHeight;
     _maxHeight = maxHeight;
     _minHeightPlanetRadiiFactor = minHeightPlanetRadiiFactor;
     _maxHeightPlanetRadiiFactor = maxHeightPlanetRadiiFactor;
  }


  public static SimpleCameraConstrainer create(double minHeight, double maxHeight, double minHeightPlanetRadiiFactor, double maxHeightPlanetRadiiFactor)
  {
    return new SimpleCameraConstrainer(minHeight, maxHeight, minHeightPlanetRadiiFactor, maxHeightPlanetRadiiFactor);
  }

  public static SimpleCameraConstrainer createDefault()
  {
    final double minHeight = 10;
    final double minHeightPlanetRadiiFactor = Double.NaN;
  
    final double maxHeight = Double.NaN;
    final double maxHeightPlanetRadiiFactor = 9;
  
    return create(minHeight, maxHeight, minHeightPlanetRadiiFactor, maxHeightPlanetRadiiFactor);
  }

  public static SimpleCameraConstrainer createFixed(double minHeight, double maxHeight)
  {
    final double minHeightPlanetRadiiFactor = Double.NaN;
    final double maxHeightPlanetRadiiFactor = Double.NaN;
  
    return create(minHeight, maxHeight, minHeightPlanetRadiiFactor, maxHeightPlanetRadiiFactor);
  }

  public static SimpleCameraConstrainer createPlanetRadiiFactor(double minHeightPlanetRadiiFactor, double maxHeightPlanetRadiiFactor)
  {
    final double minHeight = Double.NaN;
    final double maxHeight = Double.NaN;
  
    return create(minHeight, maxHeight, minHeightPlanetRadiiFactor, maxHeightPlanetRadiiFactor);
  }


  public void dispose()
  {
  }

  public final boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
   final double radii = planet.getRadii().maxAxis();
   final double minHeight = (_minHeight != _minHeight) ? (radii * _minHeightPlanetRadiiFactor) : _minHeight;
   final double maxHeight = (_maxHeight != _maxHeight) ? (radii * _maxHeightPlanetRadiiFactor) : _maxHeight;
 
   final Geodetic3D cameraPosition = nextCamera.getGeodeticPosition();
 
   if (cameraPosition._height < minHeight)
   {
     nextCamera.setGeodeticPosition(cameraPosition._latitude, cameraPosition._longitude, minHeight);
   }
   else if (cameraPosition._height > maxHeight)
   {
     nextCamera.setGeodeticPosition(cameraPosition._latitude, cameraPosition._longitude, maxHeight);
   }
 
   return true;
 }

}
