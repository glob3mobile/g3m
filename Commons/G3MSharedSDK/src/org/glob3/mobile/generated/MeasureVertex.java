package org.glob3.mobile.generated;
public class MeasureVertex
{
  private Vector3D _cartesian;

  public final Geodetic3D _geodetic ;
  public final float _verticalExaggeration;
  public final double _deltaHeight;

  public MeasureVertex(Geodetic3D geodetic, float verticalExaggeration, double deltaHeight)
  {
     _geodetic = new Geodetic3D(geodetic);
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
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

}