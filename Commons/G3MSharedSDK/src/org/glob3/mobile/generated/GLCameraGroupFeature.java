package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

//enum GLCameraGroupFeatureType{
//  F_PROJECTION, F_CAMERA_MODEL, F_MODEL_TRANSFORM
//};
public class GLCameraGroupFeature extends GLFeature
{

  private Matrix44DHolder _matrixHolder = null;
  public GLCameraGroupFeature(Matrix44D matrix, GLCameraGroupFeatureType type)
  {
    super(GLFeatureGroupName.CAMERA_GROUP);
    _matrixHolder = new Matrix44DHolder(matrix);
    _type = type;
  }

  public void dispose()
  {
    _matrixHolder._release();

    super.dispose();
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
}