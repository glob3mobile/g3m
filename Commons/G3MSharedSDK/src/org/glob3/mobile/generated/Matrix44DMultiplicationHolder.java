package org.glob3.mobile.generated; 
public class Matrix44DMultiplicationHolder extends Matrix44DProvider
{
  private final Matrix44D[] _lastMatrixes;
  private final Matrix44DProvider[] _providers;
  private int _nMatrix;
  private Matrix44D _modelview;
  public Matrix44DMultiplicationHolder(Matrix44DProvider[] providers, int nMatrix)
  {
     _providers = providers;
     _nMatrix = nMatrix;
     _modelview = null;
    _lastMatrixes = new Matrix44D[nMatrix];
    for (int i = 0; i < _nMatrix; i++)
    {
      _lastMatrixes[i] = _providers[i].getMatrix();
      _providers[i]._retain();
      if (_lastMatrixes[i] == null)
      {
        ILogger.instance().logError("Modelview multiplication failure");
      }
    }
  }

  public void dispose()
  {
    if (_modelview != null)
    {
      _modelview._release();
    }
    for (int i = 0; i < _nMatrix; i++)
    {
      _providers[i]._release();
    }
  }

  public final Matrix44D getMatrix()
  {

    if (_modelview != null)
    {
      for (int i = 0; i < _nMatrix; i++)
      {
        final Matrix44D m = _providers[i].getMatrix();
        if (m == null)
        {
          ILogger.instance().logError("Modelview multiplication failure");
        }

        if (_lastMatrixes[i] != m)
        {

          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview._release(); //NEW MODELVIEW NEEDED
          _modelview = null;

          for (int j = 0; j < _nMatrix; j++)
          {
            _lastMatrixes[j] = _providers[j].getMatrix();
          }
          break;
        }
      }
    }


    if (_modelview == null)
    {
      _modelview = new Matrix44D(_lastMatrixes[0]);
      for (int i = 1; i < _nMatrix; i++)
      {
        final Matrix44D m2 = _lastMatrixes[i];
        Matrix44D m3 = _modelview.createMultiplication(m2);
        _modelview._release();
        _modelview = m3;
      }
    }
    return _modelview;
  }


}