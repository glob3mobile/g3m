package org.glob3.mobile.generated;import java.util.*;

public class Matrix44DHolder extends Matrix44DProvider
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Matrix44D _matrix;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Matrix44D _matrix = new internal();
//#endif

  public void dispose()
  {
	_matrix._release();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public Matrix44DHolder(Matrix44D matrix)
  {
	  _matrix = matrix;
	if (matrix == null)
	{
	  THROW_EXCEPTION("Setting NULL in Matrix44D Holder");
	}
	_matrix._retain();
  }

  public final void setMatrix(Matrix44D matrix)
  {
	if (matrix == null)
	{
	  THROW_EXCEPTION("Setting NULL in Matrix44D Holder");
	}

	if (matrix != _matrix)
	{
	  if (_matrix != null)
	  {
		_matrix._release();
	  }
	  _matrix = matrix;
	  _matrix._retain();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Matrix44D* getMatrix() const
  public final Matrix44D getMatrix()
  {
	return _matrix;
  }
}
