package org.glob3.mobile.generated;
//
//  GLFeature.cpp
//  G3M
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLFeature.hpp
//  G3M
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//





//class Camera;
//class Color;


public abstract class GLFeature extends RCObject
{
  public void dispose()
  {
    if (_values != null)
       _values.dispose();
    super.dispose();
  }

  protected GPUVariableValueSet _values;

  public final GLFeatureGroupName _group;
  public final GLFeatureID _id;

  public GLFeature(GLFeatureGroupName group, GLFeatureID id)
  {
     _group = group;
     _id = id;
    _values = new GPUVariableValueSet();
  }

  public final GPUVariableValueSet getGPUVariableValueSet()
  {
    return _values;
  }

  public abstract void applyOnGlobalGLState(GLGlobalState state);

}