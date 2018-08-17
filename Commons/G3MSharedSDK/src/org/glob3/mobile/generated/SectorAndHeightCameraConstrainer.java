package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PlanetRenderer;

public class SectorAndHeightCameraConstrainer implements ICameraConstrainer
{
  private final Sector _sector = new Sector();
  private final double _maxHeight;


  public SectorAndHeightCameraConstrainer(Sector sector, double maxHeight)
  {
	  _sector = new Sector(sector);
	  _maxHeight = maxHeight;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean onCameraChange(const Planet* planet, const Camera* previousCamera, Camera* nextCamera) const
  public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
	final Geodetic3D position = nextCamera.getGeodeticPosition();
	final double height = position._height;
  
	final Geodetic3D center = nextCamera.getGeodeticCenterOfView();
  
	final boolean invalidHeight = (height > _maxHeight);
	final boolean invalidPosition = !_sector.contains(center._latitude, center._longitude);
  
	if (invalidHeight || invalidPosition)
	{
	  nextCamera.copyFrom(previousCamera, true);
	}
  
	return true;
  }

}
