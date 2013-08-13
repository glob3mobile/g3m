package org.glob3.mobile.generated; 
public class Matrix44DHolder
{
  private Matrix44D _matrix;
  public Matrix44DHolder(Matrix44D matrix)
  {
     _matrix = matrix;
    if (matrix == null)
    {
      ILogger.instance().logError("Setting NULL in Matrix44D Holder");
    }
    _matrix._retain();
  }

  public void dispose()
  {
    _matrix._release();
  super.dispose();

  }

  public final void setMatrix(Matrix44D matrix)
  {
    if (matrix == null)
    {
      ILogger.instance().logError("Setting NULL in Matrix44D Holder");
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