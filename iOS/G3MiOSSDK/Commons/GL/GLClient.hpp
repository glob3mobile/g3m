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
  const GLClient* _parent;
  
public:
  
  GLClient():_parent(NULL){}
  
  virtual ~GLClient(){}
  
  //Idle if this is not a drawable client
  virtual void getGLStateAndGPUProgramState(GLState** glState, GPUProgramState** progState){
    (*glState) = NULL;
    (*progState) = NULL;
  }
  
  virtual void notifyGLClientChildrenParentHasChanged(){
    //TIPICALLY: _mesh->actualizeGLState(this);
  } //Idle if this is a drawable client
  
  //Implemented if the node modifies the GLState
  virtual void modifyGLState(GLState& glState) const{}
  //Implemented if the node modifies the GPUProgramState
  virtual void modifyGPUProgramState(GPUProgramState& progState) const{}
  
  /**
   Invoked by parent to change my GLState and GPUProgramState
   */
  void actualizeGLState(const GLClient* parent){
    _parent = parent;
    notifyGLClientChildrenParentHasChanged();
    
    GLState* glState;
    GPUProgramState* programState;
    getGLStateAndGPUProgramState(&glState, &programState);
    if (glState != NULL && programState != NULL){
      //We are a drawable client
      modifyGLStatesHierarchy(*glState, *programState);
    }
    
  }
  
  /**
   Modifying current children's GLState and GPUProgramState
   */
  void modifyGLStatesHierarchy(GLState& glState, GPUProgramState& programState) const{
    if (_parent != NULL){
      _parent->modifyGLStatesHierarchy(glState, programState);
    }
    modifyGLState(glState);
    modifyGPUProgramState(programState);
  }
};

#endif
