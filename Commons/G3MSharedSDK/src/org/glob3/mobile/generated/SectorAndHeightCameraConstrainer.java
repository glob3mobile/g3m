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

  public void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    final Geodetic3D position = nextCamera.getGeodeticPosition();
  //  const double height = nextCamera->getHeight();
    final double height = position._height;
  
    final boolean invalidHeight = (height > _maxHeight);
    final boolean invalidPosition = !_sector.contains(position._latitude, position._longitude);
  
    if (invalidHeight || invalidPosition)
    {
      nextCamera.copyFrom(previousCamera);
  
  //    const double newHeight = invalidHeight ? _maxHeight : height;
  //    if (invalidPosition) {
  //      nextCamera->setGeodeticPosition(_sector.clamp(g2), newHeight);
  //    }
  //    else {
  //      nextCamera->setGeodeticPosition(g2, newHeight);
  //    }
    }
  
  }

}