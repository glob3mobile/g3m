package org.glob3.mobile.generated; 
public class Matrix44DMultiplicationHolder extends Matrix44DProvider
{
  private final Matrix44D[] _lastMatrixes;
  private final Matrix44DProvider[] _providers;
  private int _nMatrix;
  private Matrix44D _modelview;
<<<<<<< HEAD:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/Matrix44DMultiplicationHolder.java
  public Matrix44DMultiplicationHolder(Matrix44DProvider[] providers, int nMatrix)
=======

  private void pullMatrixes()
  {
    for (int j = 0; j < _nMatrix; j++)
    {
      final Matrix44D newMatrix = _matrixHolders[j].getMatrix();

      if (newMatrix != _matrix[j])
      {
        if (_matrix[j] != null)
        {
          _matrix[j]._release();
        }

        _matrix[j] = newMatrix;
        _matrix[j]._retain();
      }
    }
  }

  public ModelviewMatrixHolder(Matrix44DHolder[] matrixHolders, int nMatrix)
>>>>>>> glstate-rc:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/ModelviewMatrixHolder.java
  {
     _providers = providers;
     _nMatrix = nMatrix;
     _modelview = null;
<<<<<<< HEAD:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/Matrix44DMultiplicationHolder.java
    _lastMatrixes = new Matrix44D[nMatrix];
    for (int i = 0; i < _nMatrix; i++)
    {
      _lastMatrixes[i] = _providers[i].getMatrix();
      _providers[i]._retain();
      if (_lastMatrixes[i] == null)
      {
        ILogger.instance().logError("Modelview multiplication failure");
      }
=======
    _matrix = new Matrix44D[nMatrix];

    for (int i = 0; i < _nMatrix; i++)
    {
      _matrix[i] = null;
>>>>>>> glstate-rc:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/ModelviewMatrixHolder.java
    }

    pullMatrixes();
  }



  public void dispose()
  {

    for (int j = 0; j < _nMatrix; j++)
    {
      if (_matrix[j] != null)
      {
        _matrix[j]._release();
      }
    }

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

<<<<<<< HEAD:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/Matrix44DMultiplicationHolder.java
          for (int j = 0; j < _nMatrix; j++)
          {
            _lastMatrixes[j] = _providers[j].getMatrix();
          }
=======
          pullMatrixes();
>>>>>>> glstate-rc:Commons/G3MSharedSDK/src/org/glob3/mobile/generated/ModelviewMatrixHolder.java
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