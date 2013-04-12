package org.glob3.mobile.generated; 
//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

//
//  GPUProgramState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//



//#include "G3MError.hpp"
//#include "G3MError.hpp"

public class GPUProgramState
{

  private java.util.HashMap<String, GPUUniformValue> _uniformValues = new java.util.HashMap<String, GPUUniformValue>();

  private final GPUProgramState _parentState;

  private void setValueToUniform(String name, GPUUniformValue v)
  {
    java.util.HashMap<String, GPUUniformValue> iterator it = _uniformValues.indexOf(name);
    if (it != _uniformValues.end())
    {
      it.second = null;
    }
    _uniformValues.put(name, v);
  }


  public GPUProgramState(GPUProgramState parentState)
  {
     _parentState = parentState;
  }

  public void dispose()
  {
    for(java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      it.second = null;
    }
  }


  public final void setValueToUniform(String name, boolean b)
  {
    setValueToUniform(name, new GPUUniformValueBool(b));
  }

  public final void setValueToUniform(String name, float f)
  {
    setValueToUniform(name, new GPUUniformValueFloat(f));
  }

  public final void setValueToUniform(String name, Vector2D v)
  {
    setValueToUniform(name, new GPUUniformValueVec2Float(v._x, v._y));
  }

  public final void setValueToUniform(String name, double x, double y, double z, double w)
  {
    setValueToUniform(name, new GPUUniformValueVec4Float(x,y,z,w));
  }

  public final void setValueToUniform(String name, MutableMatrix44D m)
  {
    setValueToUniform(name, new GPUUniformValueMatrix4Float(m));
  }

  public final void multiplyValueOfUniform(String name, MutableMatrix44D m)
  {
    setValueToUniform(name, new GPUUniformValueMatrix4Float(m));
  }

  public final void applyChanges(GL gl, GPUProgram prog)
  {
  
    if (_parentState != null)
    {
      _parentState.applyChanges(gl, prog);
    }
  
    for(java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
  
      String name = it.first;
      GPUUniformValue v = it.second;
  
      final int type = v.getType();
      if (type == GLType.glBool())
      {
        GPUUniformBool u = prog.getGPUUniformBool(name);
        if (u == null)
        {
          throw new G3MError("UNIFORM NOT FOUND");
        }
        else
        {
          u.set(v);
        }
        continue;
      }
      if (type == GLType.glVec2Float())
      {
        GPUUniformVec2Float u = prog.getGPUUniformVec2Float(name);
        if (u == null)
        {
          throw new G3MError("UNIFORM NOT FOUND");
        }
        else
        {
          u.set(v);
        }
        continue;
      }
      if (type == GLType.glVec4Float())
      {
        GPUUniformVec4Float u = prog.getGPUUniformVec4Float(name);
        if (u == null)
        {
          throw new G3MError("UNIFORM NOT FOUND");
        }
        else
        {
          u.set(v);
        }
        continue;
      }
      if (type == GLType.glFloat())
      {
        GPUUniformFloat u = prog.getGPUUniformFloat(name);
        if (u == null)
        {
          throw new G3MError("UNIFORM NOT FOUND");
        }
        else
        {
          u.set(v);
        }
        continue;
      }
      if (type == GLType.glMatrix4Float())
      {
        GPUUniformMatrix4Float u = prog.getGPUUniformMatrix4Float(name);
        if (u == null)
        {
          throw new G3MError("UNIFORM NOT FOUND");
        }
        else
        {
          u.set(v);
        }
        continue;
      }
    }
  
    prog.applyChanges(gl); //Applying changes on GPU
  }

  public final GPUUniformValue getUniformValue(String name)
  {
    java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.indexOf(name);
    if (it != _uniformValues.end())
    {
      return it.second;
    }
    else
    {
      return null;
    }
  }
}