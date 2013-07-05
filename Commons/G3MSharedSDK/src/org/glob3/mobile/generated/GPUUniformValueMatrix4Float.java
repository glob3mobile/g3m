package org.glob3.mobile.generated; 
public class GPUUniformValueMatrix4Float extends GPUUniformValue
{

  private GPUUniformValueMatrix4Float(GPUUniformValueMatrix4Float that)
  {
     super(GLType.glMatrix4Float());
     _m = that._m;
    that._m._retain();
  }

  public final Matrix44D _const _m;

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
    GPUUniformValueMatrix4Float v2 = (GPUUniformValueMatrix4Float)v;
    if (_m == v2._m)
    {
      return true;
    }

    if (_m.isEqualsTo(*v2._m))
    {
      return true;
    }
    return false;

//    return _m->isEqualsTo(*v2->_m);
  }

//  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const;

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