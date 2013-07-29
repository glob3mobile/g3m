package org.glob3.mobile.generated; 
public class GPUUniformValueMatrix4Float extends GPUUniformValue
{

  private GPUUniformValueMatrix4Float(GPUUniformValueMatrix4Float that)
  {
     super(GLType.glMatrix4Float());
     _m = that._m;
    that._m._retain();
  }

  public final Matrix44D _m;

  public GPUUniformValueMatrix4Float(Matrix44D m)
  {
     super(GLType.glMatrix4Float());
     _m = m;
    m._retain();
  }

  public void dispose()
  {
    _m._release();
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniformMatrix4fv(id, false, _m);
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    final Matrix44D m = ((GPUUniformValueMatrix4Float)v)._m;
    if (_m == m)
    {
      return true;
    }

    return _m.isEqualsTo(m);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Matrix44D.");
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final Matrix44D getMatrix()
  {
     return _m;
  }
}