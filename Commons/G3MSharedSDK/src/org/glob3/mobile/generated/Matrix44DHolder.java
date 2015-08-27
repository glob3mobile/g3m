package org.glob3.mobile.generated; 
public class Matrix44DHolder extends Matrix44DProvider
{
  private Matrix44D _matrix;

  public void dispose()
  {
    _matrix._release();

    super.dispose();
  }

  public Matrix44DHolder(Matrix44D matrix)
  {
     _matrix = matrix;
    if (matrix == null)
    {
      throw new RuntimeException("Setting NULL in Matrix44D Holder");
    }
    _matrix._retain();
  }

  public final void setMatrix(Matrix44D matrix)
  {
    if (matrix == null)
    {
      throw new RuntimeException("Setting NULL in Matrix44D Holder");
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

  public final Matrix44D getMatrix()
  {
    return _matrix;
  }
}