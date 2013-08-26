package org.glob3.mobile.generated; 
/////////////////////


public class GPUUniformValueMatrix4 extends GPUUniformValue
{
  private final boolean _ownsProvider;
  protected Matrix44DProvider _provider = null;
  protected  Matrix44D _lastModelSet;





  public GPUUniformValueMatrix4(Matrix44D m)
  {
     super(GLType.glMatrix4Float());
     _provider = new Matrix44DHolder(m);
     _lastModelSet = null;
     _ownsProvider = true;
  }

  public void dispose()
  {
    if (_ownsProvider)
    {
      _provider._release();
    }
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    _lastModelSet = _provider.getMatrix();

    gl.uniformMatrix4fv(id, false, _provider.getMatrix());
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    if (_lastModelSet == ((GPUUniformValueMatrix4)v)._provider.getMatrix())
    {
      return true;
    }

    return false;
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

  //  const Matrix44D* getMatrix() const { return _m;}
}