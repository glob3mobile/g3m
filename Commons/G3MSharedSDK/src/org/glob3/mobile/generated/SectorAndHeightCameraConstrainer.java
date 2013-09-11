package org.glob3.mobile.generated; 
//
//  SectorAndHeightCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//

//
//  SectorAndHeightCameraConstrainer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//




public class SectorAndHeightCameraConstrainer implements ICameraConstrainer
{

  private final Sector _sector ;
  private final double _height;


  public SectorAndHeightCameraConstrainer(Sector sector, double height)
  {
     _sector = new Sector(sector);
     _height = height;
  }

  public void dispose()
  {
  }

  public void onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    Geodetic3D g = nextCamera.getGeodeticPosition();
    double h = nextCamera.getHeight();
  
    if (h > _height || !_sector.contains(g))
    {
      nextCamera.copyFrom(previousCamera);
    }
  
  }

}