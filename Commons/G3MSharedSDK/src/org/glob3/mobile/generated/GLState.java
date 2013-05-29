package org.glob3.mobile.generated; 
//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//




public class GLState
{

  private static GLGlobalState _currentGPUGlobalState = new GLGlobalState();
  private static GPUProgram _currentGPUProgram = null;

  private GPUProgramState _programState;
  private GLGlobalState _globalState;
  private final boolean _owner;

  private void setProgramState(GL gl, GPUProgramManager progManager)
  {
    GPUProgram prog = null;
    if (!_programState.isLinkedToProgram())
    {
      prog = progManager.getProgram(_programState);
      _programState.linkToProgram(prog);
    }
    else
    {
      prog = _programState.getLinkedProgram();
    }
  
    if (prog != null)
    {
      if (prog != _currentGPUProgram)
      {
        if (_currentGPUProgram != null)
        {
          _currentGPUProgram.onUnused();
        }
        _currentGPUProgram = prog;
        gl.useProgram(prog);
      }
  
      _programState.applyChanges(gl);
    }
    else
    {
      ILogger.instance().logError("No available GPUProgram for this state.");
    }
  }


  public GLState()
  {
     _programState = new GPUProgramState();
     _globalState = new GLGlobalState();
     _owner = true;
  }

  //For debugging purposes only
  public GLState(GLGlobalState globalState, GPUProgramState programState)
  {
     _programState = programState;
     _globalState = globalState;
     _owner = false;
  }

  public void dispose()
  {
    if (_owner)
    {
      if (_programState != null)
         _programState.dispose();
      if (_globalState != null)
         _globalState.dispose();
    }
  }

  public final void applyGlobalStateOnGPU(GL gl)
  {
    _globalState.applyChanges(gl, _currentGPUGlobalState);
  }

  public final void applyOnGPU(GL gl, GPUProgramManager progManager)
  {
  
    applyGlobalStateOnGPU(gl);
    setProgramState(gl, progManager);
  
  }

  public final GPUProgramState getGPUProgramState()
  {
    return _programState;
  }

  public final GLGlobalState getGLGlobalState()
  {
    return _globalState;
  }

  public static void textureHasBeenDeleted(IGLTextureId textureId)
  {
    if (_currentGPUGlobalState.getBoundTexture() == textureId)
    {
      _currentGPUGlobalState.bindTexture(null);
    }
  }

  public static GLGlobalState createCopyOfCurrentGLGlobalState()
  {
    return _currentGPUGlobalState.createCopy();
  }

//  static const GLGlobalState* getCurrentGLGlobalState() {
//    return &_currentGPUGlobalState;
//  }

//  static const GPUProgram* getGPUProgram() {
//    return _currentGPUProgram;
//  }
}