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
    if (_planetRenderer != null)
    {
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
    }
  
    return true;
  }

}