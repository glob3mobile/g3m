package org.glob3.mobile.generated; 
//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  GPUUniform.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//





//class GPUUniform;

public abstract class GPUUniformValue extends RCObject
{
  private final int _type;

  public void dispose()
  {
    super.dispose();
  }

  public GPUUniformValue(int type)
  {
     _type = type;
  }



  public final int getType()
  {
     return _type;
  }
  public abstract void setUniform(GL gl, IGLUniformID id);
  public abstract boolean isEquals(GPUUniformValue v);

  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

}