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

public abstract class GPUUniformValue extends RCObject
{
  private final int _type;

  //  mutable GPUUniform* _uniform;

  public GPUUniformValue(int type)
  //, _uniform(NULL)
  {
     _type = type;
  }

  public void dispose()
  {
//    ILogger::instance()->logInfo("Deleting Uniform Value");
  super.dispose();

  }


  public final int getType()
  {
     return _type;
  }
  public abstract void setUniform(GL gl, IGLUniformID id);
  public abstract boolean isEqualsTo(GPUUniformValue v);

  //  GPUUniform* getLinkedUniform() const { return _uniform;}

  public abstract String description();

  //  void linkToGPUUniform(GPUUniform* u) const{
  //    _uniform = u;
  //  }
  //
  //  void unLinkToGPUUniform() {
  //    _uniform = NULL;
  //  }

  //  void setValueToLinkedUniform() const;

  //  virtual GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    return value;
  //  }

  //  virtual GPUUniformValue* copyOrCreate(GPUUniformValue* value) const = 0;

  //  bool linkToGPUProgram(const GPUProgram* prog, int key) const{
  //    GPUUniform* u = prog->getGPUUniform(key);
  //    if (u == NULL) {
  //      ILogger::instance()->logError("UNIFORM WITH KEY %d NOT FOUND", key);
  //      return false;
  //    } else{
  //      _uniform = u;
  //      return true;
  //    }
  //  }
}