package org.glob3.mobile.generated;
public class FixedFrustumPolicy extends FrustumPolicy
{
  private double _znear;
  private double _zfar;
  public FixedFrustumPolicy(double znear, double zfar)
  {
     _znear = znear;
     _zfar = zfar;
  }

  public final void setRange(double znear, double zfar)
  {
    _znear = znear;
    _zfar = zfar;
  }

  public final Vector2D calculateFrustumZNearAndZFar(Camera camera)
  {
    return new Vector2D(_znear, _zfar);
  }

  public final FrustumPolicy copy()
  {
    new FixedFrustumPolicy(_znear, _zfar);
  }
}
