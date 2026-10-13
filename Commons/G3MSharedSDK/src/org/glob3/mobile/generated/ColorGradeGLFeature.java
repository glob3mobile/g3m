package org.glob3.mobile.generated;
// Tile color = (colorMatrix * (r, g, b, 1)).rgb, clamped; alpha is untouched.
// The fourth column of the homogeneous matrix is the color offset.
public class ColorGradeGLFeature extends GLFeature
{
  private Matrix44DHolder _colorMatrixHolder;

  public void dispose()
  {
    _colorMatrixHolder._release();

    super.dispose();
  }

  public ColorGradeGLFeature(Matrix44D colorMatrix)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_COLOR_GRADE);
     _colorMatrixHolder = new Matrix44DHolder(colorMatrix);
    _values.addUniformValue(GPUUniformKey.COLOR_MATRIX, new GPUUniformValueMatrix4(_colorMatrixHolder), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void changeColorMatrix(Matrix44D colorMatrix)
  {
    _colorMatrixHolder.setMatrix(colorMatrix);
  }
}