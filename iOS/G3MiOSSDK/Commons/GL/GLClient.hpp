//
//  GLClient.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/05/13.
//
//

#ifndef G3MiOSSDK_GLClient_hpp
#define G3MiOSSDK_GLClient_hpp

#include "GPUProgramState.hpp"
#include "GLState.hpp"

class GLClientDrawable;
class GLClientNotDrawable;

class GLClient{
protected:
  const GLClientNotDrawable* _parent;
  
public:
  
  GLClient():_parent(NULL){}
  
  virtual ~GLClient(){}
  
  /**
   Invoked by parent to change my GLState and GPUProgramState
   */
  virtual void actualizeGLState(const GLClientNotDrawable* parent){}
};


class GLClientNotDrawable: public GLClient{
public:
  
  GLClientNotDrawable(){}
  
  virtual ~GLClientNotDrawable(){}
  
  //Implement on not drawable nodes
  virtual void notifyGLClientChildrenParentHasChanged() = 0;
  virtual void modifyGLState(GLState* glState) const = 0;
  virtual void modifyGPUProgramState(GPUProgramState* progState) const = 0;
  
  /**
   Invoked by parent to change my GLState and GPUProgramState
   */
  void actualizeGLState(const GLClientNotDrawable* parent){
    _parent = parent;
    notifyGLClientChildrenParentHasChanged();
  }
  
  /**
   Modifying current children's GLState and GPUProgramState
   */
  void modifyGLStateHierarchy(GLState* glState, GPUProgramState* programState) const{
    if (_parent != NULL){
      _parent->modifyGLStateHierarchy(glState, programState);
    }
    modifyGLState(glState);
    modifyGPUProgramState(programState);
  }
  
};


class GLClientDrawable: public GLClient{
public:
  
  GLClientDrawable(){}
  
  virtual ~GLClientDrawable(){}
  
  //Implement on drawable nodes
  virtual GLState* getGLState() = 0;
  virtual GPUProgramState* getGPUProgramState() = 0;
  
  /**
   Invoked by parent to change my GLState and GPUProgramState
   */
  void actualizeGLState(const GLClientNotDrawable* parent){
    _parent = parent;
    
    GLState* glState = getGLState();
    GPUProgramState* programState = getGPUProgramState();
    
    if (glState == NULL || programState == NULL){
      ILogger::instance()->logError("Cannot get a proper GLState");
    } else{
      //Modifying states through hierarchy of nodes
      parent->modifyGLStateHierarchy(glState, programState);
    }
    
  }
  
};


#endif
