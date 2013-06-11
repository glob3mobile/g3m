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
  
    GPUProgram prog = progManager.getProgram(this);
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
    }
    else
    {
      ILogger.instance().logError("No GPUProgram found.");
    }
  
    linkAndApplyToGPUProgram(prog);
    prog.applyChanges(gl);
  
    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  }

  private GLState _parentGLState;

  private void linkAndApplyToGPUProgram(GPUProgram prog)
  {
  
    if (_parentGLState != null)
    {
      _parentGLState.linkAndApplyToGPUProgram(prog);
    }
  
    _programState.linkToProgram(prog);
    _programState.applyValuesToLinkedProgram();
  }


  public GLState()
  {
     _programState = new GPUProgramState();
     _globalState = new GLGlobalState();
     _owner = true;
     _parentGLState = null;
  }

  //For debugging purposes only
  public GLState(GLGlobalState globalState, GPUProgramState programState)
  {
     _programState = programState;
     _globalState = globalState;
     _owner = false;
     _parentGLState = null;
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

  public final GLState getParent()
  {
    return _parentGLState;
  }

  public final void setParent(GLState p)
  {
    _parentGLState = p;
  }

  public final void applyGlobalStateOnGPU(GL gl)
  {
  
    if (_parentGLState != null)
    {
      _parentGLState.applyGlobalStateOnGPU(gl);
    }
  
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