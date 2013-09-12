package org.glob3.mobile.generated; 
public class RenderedSectorCameraConstrainer implements ICameraConstrainer
{
  private final double _maxHeight;
  private final double _margin;
  private final PlanetRenderer _planetRenderer;


  public RenderedSectorCameraConstrainer(PlanetRenderer planetRenderer, double maxHeight, double margin)
  {
     _planetRenderer = planetRenderer;
     _maxHeight = maxHeight;
     _margin = margin;
  }

  public void dispose()
  {
  }

  public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    Sector sector = _planetRenderer.getRenderedSector();
  //  if (!nextCamera->isCenterOfViewWithin(sector, _maxHeight)){
  //    if (previousCamera->isCenterOfViewWithin(sector, _maxHeight)){
  //      nextCamera->copyFrom(*previousCamera);
  //      return true;
  //    }
  //  } else{
  //    return false;
  //  }
  
    final Geodetic3D position = nextCamera.getGeodeticPosition();
    final double height = position._height;
  
    final Geodetic3D center = nextCamera.getGeodeticCenterOfView();
  
    final boolean invalidHeight = (height > _maxHeight);
    final boolean invalidPosition = !sector.contains(center._latitude, center._longitude);
  
    if (invalidHeight && !invalidPosition)
    {
      Geodetic3D newPos = new Geodetic3D(position._latitude, position._longitude, _maxHeight);
      nextCamera.setGeodeticPosition(newPos);
      return true;
    }
    else
    {
      if (invalidPosition)
      {
        if (previousCamera.isCenterOfViewWithin(sector, _maxHeight))
        {
          nextCamera.copyFrom(previousCamera);
          return true;
        }
        else
        {
          return false;
        }
      }
      return true;
    }
  
  }

}