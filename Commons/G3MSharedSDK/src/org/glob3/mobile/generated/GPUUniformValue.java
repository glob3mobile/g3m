package org.glob3.mobile.generated; 
//
//  GPUUniform.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//


//#include "G3MError.hpp"

public abstract class GPUUniformValue
{
  private final int _type;
  public GPUUniformValue(int type)
  {
     _type = type;
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
}