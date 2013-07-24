package org.glob3.mobile.generated; 
/////////////////////

public class GPUUniformValueModelview extends GPUUniformValue
{
  protected ModelviewMatrixHolder _holder = null;
  public GPUUniformValueModelview(Matrix44DHolder[] matrixHolders, int nMatrix)
  {
    super(GLType.glMatrix4Float());
    _holder = new ModelviewMatrixHolder(matrixHolders, nMatrix);
  }
  public void dispose()
  {
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniformMatrix4fv(id, false, _holder.getModelview());
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    return (_holder.getModelview() == ((GPUUniformValueModelview)v)._holder.getModelview());
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