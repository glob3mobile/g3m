package org.glob3.mobile.generated; 
//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  GPUUniform.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//



//class GPUUniform;

public abstract class GPUUniformValue
{
  private final int _type;

  private GPUUniform _uniform;

  public GPUUniformValue(int type)
  {
     _type = type;
     _uniform = null;
  }
  public final int getType()
  {
     return _type;
  }
  public void dispose()
  {
  }
  public abstract void setUniform(GL gl, IGLUniformID id);
  public abstract boolean isEqualsTo(GPUUniformValue v);

  public final GPUUniform getLinkedUniform()
  {
     return _uniform;
  }

  public abstract String description();

  public final void linkToGPUUniform(GPUUniform u)
  {
    _uniform = u;
  }

  public final void unLinkToGPUUniform()
  {
    _uniform = null;
  }

  ////////////////////////////////////////////////////////////////////////
  
  
  public final void setValueToLinkedUniform()
  {
    if (_uniform == null)
    {
      ILogger.instance().logError("Uniform value unlinked");
    }
    else
    {
      _uniform.set((GPUUniformValue)this);
    }
  }

  public GPUUniformValue copyOrCreate(GPUUniformValue value)
  {
     return value;
  }

  public final boolean linkToGPUProgram(GPUProgram prog, int key)
  {
    GPUUniform u = prog.getGPUUniform(key);
    if (u == null)
    {
      ILogger.instance().logError("UNIFORM WITH KEY %d NOT FOUND", key);
      return false;
    }
    else
    {
      _uniform = u;
      return true;
    }
  }
}