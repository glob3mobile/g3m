package org.glob3.mobile.generated;import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////////


public class GLCameraGroupFeature extends GLFeature
{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private Matrix44DHolder _matrixHolder;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Matrix44DHolder _matrixHolder = null;
//#endif

  public void dispose()
  {
	_matrixHolder._release();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public GLCameraGroupFeature(Matrix44D matrix, GLFeatureID id)
  {
	  super(GLFeatureGroupName.CAMERA_GROUP, id);
	  _matrixHolder = new Matrix44DHolder(matrix);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Matrix44D* getMatrix() const
  public final Matrix44D getMatrix()
  {
	return _matrixHolder.getMatrix();
  }

  public final void setMatrix(Matrix44D matrix)
  {
	_matrixHolder.setMatrix(matrix);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Matrix44DHolder* getMatrixHolder() const
  public final Matrix44DHolder getMatrixHolder()
  {
	return _matrixHolder;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}
