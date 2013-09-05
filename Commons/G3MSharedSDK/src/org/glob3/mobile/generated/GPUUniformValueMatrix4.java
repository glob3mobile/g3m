package org.glob3.mobile.generated; 
/////////////////////


public class GPUUniformValueMatrix4 extends GPUUniformValue
{
//  const bool _ownsProvider;
  protected Matrix44DProvider _provider = null;
  protected  Matrix44D _lastModelSet;

  public GPUUniformValueMatrix4(Matrix44DProvider[] providers, int nMatrix)
//  _ownsProvider(true)
  {
     super(GLType.glMatrix4Float());
     _provider = new Matrix44DMultiplicationHolder(providers, nMatrix);
     _lastModelSet = null;
  }

  public GPUUniformValueMatrix4(Matrix44DProvider provider)
//  _ownsProvider(ownsProvider)
  {
     super(GLType.glMatrix4Float());
     _provider = provider;
     _lastModelSet = null;
    _provider._retain();
  }

  public GPUUniformValueMatrix4(Matrix44D m)
//  _ownsProvider(true)
  {
     super(GLType.glMatrix4Float());
     _provider = new Matrix44DHolder(m);
     _lastModelSet = null;
  }

  public void dispose()
  {
//    if (_ownsProvider){
      _provider._release();
//    }
    if (_lastModelSet != null)
    {
      _lastModelSet._release();
    }
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {

    if (_lastModelSet != null)
    {
      _lastModelSet._release();
    }

    _lastModelSet = _provider.getMatrix();

    _lastModelSet._retain();

    gl.uniformMatrix4fv(id, false, _lastModelSet);
  }

  public final boolean isEquals(GPUUniformValue v)
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