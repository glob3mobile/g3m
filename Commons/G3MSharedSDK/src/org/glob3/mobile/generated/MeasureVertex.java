package org.glob3.mobile.generated;
public class MeasureVertex
{
  private Vector3D _cartesian;
  private final Geodetic3D _geodetic ;


  public MeasureVertex(Geodetic3D geodetic)
  {
     _geodetic = new Geodetic3D(geodetic);
     _cartesian = null;

  }

  public final Vector3D getCartesian(Planet planet)
  {
    if (_cartesian == null)
    {
      _cartesian = new Vector3D(planet.toCartesian(_geodetic));
    }
    return _cartesian;
  }

  public void dispose()
  {
    if (_cartesian != null)
       _cartesian.dispose();
  }

  public final Geodetic3D getScaledGeodetic(float verticalExaggeration, double deltaHeight)
  {
    final double scaledHeight = (_geodetic._height + deltaHeight) * verticalExaggeration;

    return new Geodetic3D(_geodetic._latitude, _geodetic._longitude, scaledHeight);
  }

  public final Geodetic3D getGeodetic()
  {
    return _geodetic;
  }

}