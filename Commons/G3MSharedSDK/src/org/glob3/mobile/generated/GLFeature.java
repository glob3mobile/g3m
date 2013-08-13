package org.glob3.mobile.generated; 
//
//  GLFeature.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLFeature.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//class Camera;

public abstract class GLFeature extends RCObject
{

  public GLFeature(GLFeatureGroupName group)
  //, _globalState(NULL)
  {
     _group = group;
  }

//  virtual ~GLFeature() {
//    delete _globalState;
//    JAVA_POST_DISPOSE
//  }

//  void applyGLGlobalState(GL* gl) const{
//    if (_globalState != NULL) {
//      _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
//    }
//  }
  public final GPUVariableValueSet getGPUVariableValueSet()
  {
    return _values;
  }

  public final GLFeatureGroupName getGroup()
  {
    return _group;
  }

  public abstract void applyOnGlobalGLState(GLGlobalState state);

  protected final GLFeatureGroupName _group;
  protected GPUVariableValueSet _values = new GPUVariableValueSet();
//  GLGlobalState* _globalState;

}