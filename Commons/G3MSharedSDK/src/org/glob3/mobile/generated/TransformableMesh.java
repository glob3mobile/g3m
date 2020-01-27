package org.glob3.mobile.generated;
//
//  TransformableMesh.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  TransformableMesh.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class MutableMatrix44D;
//class ModelTransformGLFeature;


public abstract class TransformableMesh extends Mesh
{
  private MutableMatrix44D _transformMatrix;
  private ModelTransformGLFeature _transformGLFeature;
  private MutableMatrix44D _userTransformMatrix;
  private MutableMatrix44D getTransformMatrix()
  {
    if (_transformMatrix == null)
    {
      _transformMatrix = createTransformMatrix();
    }
    return _transformMatrix;
  }
  private MutableMatrix44D createTransformMatrix()
  {
    if (_center.isNan() || _center.isZero())
    {
      return new MutableMatrix44D(_userTransformMatrix);
    }
  
    final MutableMatrix44D centerM = MutableMatrix44D.createTranslationMatrix(_center);
    if (_userTransformMatrix == null)
    {
      return new MutableMatrix44D(centerM);
    }
  
    MutableMatrix44D result = new MutableMatrix44D();
    result.copyValueOfMultiplication(centerM, _userTransformMatrix);
  
    return result;
  }

  private GLState _glState;

  protected final Vector3D _center ;

  protected TransformableMesh(Vector3D center)
  {
     _center = center;
     _glState = null;
     _transformMatrix = null;
     _userTransformMatrix = MutableMatrix44D.newIdentity();
    _transformGLFeature = new ModelTransformGLFeature(getTransformMatrix().asMatrix44D());
  }

  protected abstract void userTransformMatrixChanged();

  protected final GLState getGLState()
  {
    if (_glState == null)
    {
      _glState = new GLState();
      initializeGLState(_glState);
    }
    return _glState;
  }
  protected void initializeGLState(GLState glState)
  {
    glState.addGLFeature(_transformGLFeature, true);
  }

  protected final boolean hasUserTransform()
  {
    return !_userTransformMatrix.isIdentity();
  }

  public final void setUserTransformMatrix(MutableMatrix44D userTransformMatrix)
  {
    if (userTransformMatrix == null)
    {
      throw new RuntimeException("userTransformMatrix is NULL");
    }
  
    if (userTransformMatrix != _userTransformMatrix)
    {
      if (_userTransformMatrix != null)
         _userTransformMatrix.dispose();
      _userTransformMatrix = userTransformMatrix;
    }
  
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    _transformMatrix = null;
  
    _transformGLFeature.setMatrix(getTransformMatrix().asMatrix44D());
  }

  public void dispose()
  {
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    if (_userTransformMatrix != null)
       _userTransformMatrix.dispose();
  
    _transformGLFeature._release();
  
    if (_glState != null)
    {
      _glState._release();
    }
  
    super.dispose();
  }

}
