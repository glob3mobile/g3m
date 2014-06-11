package org.glob3.mobile.generated; 
public class Matrix44DMultiplicationHolder extends Matrix44DProvider
{
  private final Matrix44D[] _matrices;
  private final Matrix44DProvider[] _providers;
  private final int _matricesSize;
  private Matrix44D _modelview;

  private void pullMatrixes()
  {
    for (int j = 0; j < _matricesSize; j++)
    {
      final Matrix44D newMatrix = _providers[j].getMatrix();
  
      if (newMatrix != _matrices[j])
      {
        if (_matrices[j] != null)
        {
          _matrices[j]._release();
        }
  
        _matrices[j] = newMatrix;
        _matrices[j]._retain();
      }
    }
  }

  public void dispose()
  {
    for (int j = 0; j < _matricesSize; j++)
    {
      if (_matrices[j] != null)
      {
        _matrices[j]._release();
      }
      _providers[j]._release();
    }
  
  
    if (_modelview != null)
    {
      _modelview._release();
    }
  
    super.dispose();
  }

  public Matrix44DMultiplicationHolder(Matrix44DProvider[] providers, int matricesSize)
  {
     _matricesSize = matricesSize;
     _modelview = null;
    _matrices  = new Matrix44D[_matricesSize];
    _providers = new Matrix44DProvider[_matricesSize];
    for (int i = 0; i < _matricesSize; i++)
    {
      _matrices[i] = null;
      _providers[i] = providers[i];
      _providers[i]._retain();
    }
  
    pullMatrixes();
  }

  public final Matrix44D getMatrix()
  {
    if (_modelview != null)
    {
      for (int i = 0; i < _matricesSize; i++)
      {
        final Matrix44D m = _providers[i].getMatrix();
        if (m == null)
        {
          ILogger.instance().logError("Modelview multiplication failure");
        }
  
        if (_matrices[i] != m)
        {
          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview._release(); //NEW MODELVIEW NEEDED
          _modelview = null;
  
          pullMatrixes();
          break;
        }
      }
    }
  
    if (_modelview == null)
    {
      _modelview = new Matrix44D(_matrices[0]);
      for (int i = 1; i < _matricesSize; i++)
      {
        final Matrix44D m2 = _matrices[i];
        Matrix44D m3 = _modelview.createMultiplication(m2);
        _modelview._release();
        _modelview = m3;
      }
    }
  
    return _modelview;
  }

}