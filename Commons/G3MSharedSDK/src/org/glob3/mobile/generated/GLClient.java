package org.glob3.mobile.generated; 
//
//  GLClient.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/05/13.
//
//



public class GLClient
{
  protected final GLClient _parent;


  public GLClient()
  {
     _parent = null;
  }

  public void dispose()
  {
  }

  //Idle if this is not a drawable client
  public void getGLGlobalStateAndGPUProgramState(GLGlobalState[]GLGlobalState, GPUProgramState[]progState)
  {
    (*GLGlobalState) = null;
    (*progState) = null;
  }

  public void notifyGLClientChildrenParentHasChanged()
  {
    //TIPICALLY: _mesh->actualizeGLGlobalState(this);
  } //Idle if this is a drawable client

  //Implemented if the node modifies the GLGlobalState
  public void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  }
  //Implemented if the node modifies the GPUProgramState
  public void modifyGPUProgramState(GPUProgramState progState)
  {
  }

  /**
   Invoked by parent to change my GLGlobalState and GPUProgramState
   */
  public final void actualizeGLGlobalState(GLClient parent)
  {
    _parent = parent;
    notifyGLClientChildrenParentHasChanged();

    GLGlobalState GLGlobalState;
    GPUProgramState programState;
    getGLGlobalStateAndGPUProgramState(GLGlobalState, programState);
    if (GLGlobalState != null && programState != null)
    {
      //We are a drawable client
      modifyGLGlobalStatesHierarchy(GLGlobalState, programState);
    }

  }

  /**
   Modifying current children's GLGlobalState and GPUProgramState
   */
  public final void modifyGLGlobalStatesHierarchy(GLGlobalState GLGlobalState, GPUProgramState programState)
  {
    if (_parent != null)
    {
      _parent.modifyGLGlobalStatesHierarchy(GLGlobalState, programState);
    }
    modifyGLGlobalState(GLGlobalState);
    modifyGPUProgramState(programState);
  }
}