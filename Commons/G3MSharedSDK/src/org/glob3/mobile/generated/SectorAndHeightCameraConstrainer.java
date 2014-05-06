package org.glob3.mobile.generated; 
//
//  SectorAndHeightCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//

//
//  SectorAndHeightCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//



//class PlanetRenderer;

public class SectorAndHeightCameraConstrainer implements ICameraConstrainer
{
  private final Sector _sector ;
  private final double _maxHeight;


  public SectorAndHeightCameraConstrainer(Sector sector, double maxHeight)
  {
     _sector = new Sector(sector);
     _maxHeight = maxHeight;
  }

  public void dispose()
  {
  }

  public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    final Geodetic3D position = nextCamera.getGeodeticPosition();
    final double height = position._height;
  
    final Geodetic3D center = nextCamera.getGeodeticCenterOfView();
  
    final boolean invalidHeight = (height > _maxHeight);
    final boolean invalidPosition = !_sector.contains(center._latitude, center._longitude);
  
    if (invalidHeight || invalidPosition)
    {
      nextCamera.copyFrom(previousCamera);
    }
  
    return true;
  }

}