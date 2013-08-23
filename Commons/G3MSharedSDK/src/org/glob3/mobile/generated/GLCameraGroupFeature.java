package org.glob3.mobile.generated; 
public class GLCameraGroupFeature extends GLFeature
{
  protected final GLCameraGroupFeatureType _type;
  private Matrix44DHolder _matrixHolder = null;
  public GLCameraGroupFeature(Matrix44D matrix, GLCameraGroupFeatureType type)
  {
    super(GLFeatureGroupName.CAMERA_GROUP);
    _matrixHolder = new Matrix44DHolder(matrix);
    _type = type;
  }
  public void dispose()
  {
<<<<<<< HEAD
    _matrixHolder._release();
=======
  super.dispose();

>>>>>>> glstate-rc
  }
  public final Matrix44D getMatrix()
  {
     return _matrixHolder.getMatrix();
  }
  public final void setMatrix(Matrix44D matrix)
  {
     _matrixHolder.setMatrix(matrix);
  }
  public final Matrix44DHolder getMatrixHolder()
  {
     return _matrixHolder;
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final GLCameraGroupFeatureType getType()
  {
     return _type;
  }
}