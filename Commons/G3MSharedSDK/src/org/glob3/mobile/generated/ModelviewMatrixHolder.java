package org.glob3.mobile.generated; 
/////////////////////

public class ModelviewMatrixHolder extends Disposable
{
  private final Matrix44D[] _matrix;
  private final Matrix44DHolder[] _matrixHolders;
  private int _nMatrix;
  private Matrix44D _modelview;
  public ModelviewMatrixHolder(Matrix44DHolder[] matrixHolders, int nMatrix)
  {
     _matrixHolders = matrixHolders;
     _nMatrix = nMatrix;
     _modelview = null;
    _matrix = new Matrix44D[nMatrix];
    for (int i = 0; i < _nMatrix; i++)
    {
      _matrix[i] = matrixHolders[i].getMatrix();
      if (_matrix[i] == null)
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

    JAVA_POST_DISPOSE
  }

  public final Matrix44D getModelview()
  {

    if (_modelview != null)
    {
      for (int i = 0; i < _nMatrix; i++)
      {
        final Matrix44D m = _matrixHolders[i].getMatrix();
        if (m == null)
        {
          ILogger.instance().logError("Modelview multiplication failure");
        }

        if (_matrix[i] != m)
        {

          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview._release(); //NEW MODELVIEW NEEDED
          _modelview = null;

          for (int j = 0; j < _nMatrix; j++)
          {
            _matrix[j] = _matrixHolders[j].getMatrix();
          }
          break;
        }
      }
    }


    if (_modelview == null)
    {
      _modelview = new Matrix44D(_matrix[0]);
      for (int i = 1; i < _nMatrix; i++)
      {
        final Matrix44D m2 = _matrix[i];
        Matrix44D m3 = _modelview.createMultiplication(m2);
        _modelview._release();
        _modelview = m3;
      }
    }
    return _modelview;
  }

}