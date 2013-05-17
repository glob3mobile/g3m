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
#include "GLGlobalState.hpp"

class GLClient{
protected:
  const GLClient* _parent;
  
public:
  
  GLClient():_parent(NULL){}
  
  virtual ~GLClient(){}
  
  //Idle if this is not a drawable client
  virtual void getGLGlobalStateAndGPUProgramState(GLGlobalState** GLGlobalState, GPUProgramState** progState){
    (*GLGlobalState) = NULL;
    (*progState) = NULL;
  }
  
  virtual void notifyGLClientChildrenParentHasChanged(){
    //TIPICALLY: _mesh->actualizeGLGlobalState(this);
  } //Idle if this is a drawable client
  
  //Implemented if the node modifies the GLGlobalState
  virtual void modifyGLGlobalState(GLGlobalState& GLGlobalState) const{}
  //Implemented if the node modifies the GPUProgramState
  virtual void modifyGPUProgramState(GPUProgramState& progState) const{}
  
  /**
   Invoked by parent to change my GLGlobalState and GPUProgramState
   */
  void actualizeGLGlobalState(const GLClient* parent){
    _parent = parent;
    notifyGLClientChildrenParentHasChanged();
    
    GLGlobalState* GLGlobalState;
    GPUProgramState* programState;
    getGLGlobalStateAndGPUProgramState(&GLGlobalState, &programState);
    if (GLGlobalState != NULL && programState != NULL){
      //We are a drawable client
      modifyGLGlobalStatesHierarchy(*GLGlobalState, *programState);
    }
    
  }
  
  /**
   Modifying current children's GLGlobalState and GPUProgramState
   */
  void modifyGLGlobalStatesHierarchy(GLGlobalState& GLGlobalState, GPUProgramState& programState) const{
    if (_parent != NULL){
      _parent->modifyGLGlobalStatesHierarchy(GLGlobalState, programState);
    }
    modifyGLGlobalState(GLGlobalState);
    modifyGPUProgramState(programState);
  }
};

#endif
