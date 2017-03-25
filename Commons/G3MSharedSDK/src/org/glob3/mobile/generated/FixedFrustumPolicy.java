package org.glob3.mobile.generated;
public class FixedFrustumPolicy extends FrustumPolicy
{
  private double _zNear;
  private double _zFar;

  public FixedFrustumPolicy(double zNear, double zFar)
  {
     _zNear = zNear;
     _zFar = zFar;
  }

  public final void setRange(double zNear, double zFar)
  {
    _zNear = zNear;
    _zFar = zFar;
  }

  public final Vector2D calculateFrustumZNearAndZFar(Camera camera)
  {
    return new Vector2D(_zNear, _zFar);
  }

  public final FrustumPolicy copy()
  {
    return new FixedFrustumPolicy(_zNear, _zFar);
  }
}
