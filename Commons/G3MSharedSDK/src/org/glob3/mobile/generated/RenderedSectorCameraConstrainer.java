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
    //  const Sector* sector = _planetRenderer->getRenderedSector();
    //
    //  const Geodetic3D position = nextCamera->getGeodeticPosition();
    //  const Geodetic3D center   = nextCamera->getGeodeticCenterOfView();
    //
    //  const bool validHeight   = (position._height <= _maxHeight);
    //  const bool validPosition = ((sector == NULL)
    //                              ? true
    //                              : sector->contains(center._latitude, center._longitude));
    //
    //  if (!validHeight && validPosition) {
    //    Geodetic3D newPos(position._latitude, position._longitude, _maxHeight);
    //    nextCamera->setGeodeticPosition(newPos);
    //    return true;
    //  }
    //
    //  if (!validPosition) {
    ///#warning check with JM
    //    //    bool previousCameraWasValid = previousCamera->getHeight() < _maxHeight;
    //    bool previousCameraWasValid = previousCamera->getGeodeticPosition()._height <= _maxHeight;
    //    if (previousCameraWasValid && sector != NULL) {
    //      const Geodetic3D centerPosition = previousCamera->getGeodeticCenterOfView();
    //      previousCameraWasValid = sector->contains(centerPosition._latitude, centerPosition._longitude);
    //    }
    //
    //    if (previousCameraWasValid) {
    //      nextCamera->copyFrom(*previousCamera);
    //      return true;
    //    }
    //    return false;
    //  }
    //
    //  return true;
  
    final Sector sector = _planetRenderer.getRenderedSector();
    final Geodetic3D position = nextCamera.getGeodeticPosition();
    final boolean isValidHeight = (position._height <= _maxHeight);
  
    if (sector == null)
    {
      if (!isValidHeight)
      {
        nextCamera.setGeodeticPosition(new Geodetic3D(position._latitude, position._longitude, _maxHeight));
      }
    }
    else
    {
      final Geodetic3D center = nextCamera.getGeodeticCenterOfView();
      final boolean isValidPosition = sector.contains(center._latitude, center._longitude);
  
      if (isValidPosition)
      {
        if (!isValidHeight)
        {
          nextCamera.setGeodeticPosition(new Geodetic3D(position._latitude, position._longitude, _maxHeight));
        }
      }
      else
      {
        nextCamera.copyFrom(previousCamera);
      }
    }
  
    return true;
  }

}