package org.glob3.mobile.generated; 
public class RenderedSectorCameraConstrainer implements ICameraConstrainer
{
  private final double _maxHeight;
  private final PlanetRenderer _planetRenderer;


  public RenderedSectorCameraConstrainer(PlanetRenderer planetRenderer, double maxHeight)
  {
     _planetRenderer = planetRenderer;
     _maxHeight = maxHeight;
  }

  public void dispose()
  {
  }

  public boolean onCameraChange(Planet planet, Camera previousCamera, Camera nextCamera)
  {
  
    final Sector sector = _planetRenderer.getRenderedSector();
  
    final Geodetic3D position = nextCamera.getGeodeticPosition();
    final double height = position._height;
  
    final Geodetic3D center = nextCamera.getGeodeticCenterOfView();
  
    final boolean invalidHeight = (height > _maxHeight);
    final boolean invalidPosition = sector == null? false : !sector.contains(center._latitude, center._longitude);
  
    if (invalidHeight && !invalidPosition)
    {
      Geodetic3D newPos = new Geodetic3D(position._latitude, position._longitude, _maxHeight);
      nextCamera.setGeodeticPosition(newPos);
      return true;
    }
  
    if (invalidPosition)
    {
  
      boolean previousCameraWasValid = previousCamera.getHeight() < _maxHeight;
      if (previousCameraWasValid && sector != null)
      {
        final Geodetic3D position = previousCamera.getGeodeticCenterOfView();
        previousCameraWasValid = sector.contains(position._latitude, position._longitude);
      }
  
      if (previousCameraWasValid)
      {
        nextCamera.copyFrom(previousCamera);
        return true;
      }
      return false;
    }
  
    return true;
  
  }

}