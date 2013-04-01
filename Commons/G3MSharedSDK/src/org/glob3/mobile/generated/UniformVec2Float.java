package org.glob3.mobile.generated; 
public class UniformVec2Float extends Uniform
{
  private double _x;
  private double _y;
  public UniformVec2Float(String name, IGLUniformID id)
  {
     super(name,id);
  }
  public final void set(GL gl, Vector2D v)
  {
    double x = v.x();
    double y = v.y();
    if (x != _x || y != _y)
    {
      gl.uniform2f(_id, (float)x, (float)y);
      _x = x;
      _y = y;




      IUniform<UniformTypeBool > boolUniform = new IUniform<UniformTypeBool >("",0);
    }
  }
}