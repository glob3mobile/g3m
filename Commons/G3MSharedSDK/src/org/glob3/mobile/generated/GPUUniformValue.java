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
  public abstract GPUUniformValue deepCopy();

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
      ILogger.instance().logError("Uniform unlinked");
    }
    else
    {
      _uniform.set((GPUUniformValue)this);
  //    _uniform->applyChanges(gl);
  
      //    setUniform(gl, _uniform->getID());
    }
  }
}