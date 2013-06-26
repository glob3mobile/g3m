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

//  void setProgramState(GL* gl, GPUProgramManager& progManager) const;

  private final GLState _parentGLState;


  //void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{
  ////  applyGlobalStateOnGPU(gl);
  //  setProgramState(gl, progManager);
  //}
  
  private void linkAndApplyToGPUProgram(GL gl, GPUProgram prog)
  {
    if (_parentGLState != null)
    {
      _parentGLState.linkAndApplyToGPUProgram(gl, prog);
    }
  
    _programState.linkToProgram(prog);
    _programState.applyValuesToLinkedProgram();
  
    _globalState.applyChanges(gl, _currentGPUGlobalState);
  }

  private GLState(GLState state)
  {
     _programState = new GPUProgramState();
     _globalState = new GLGlobalState();
     _owner = true;
     _parentGLState = null;
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
  
    GPUProgram prog = _programState.getLinkedProgram();
    if (prog != null)
    {
      final GLState parent = _parentGLState;
      while (parent != null)
      {
        if (prog != parent._programState.getLinkedProgram())
        {
          prog = null;
          break;
        }
        parent = parent._parentGLState;
      }
    }
  
    if (prog == null)
    {
      prog = progManager.getProgram(gl, this);
    }
  
    if (prog != null)
    {
      if (prog != _currentGPUProgram)
      {
        if (_currentGPUProgram != null)
        {
          _currentGPUProgram.onUnused(gl);
        }
        _currentGPUProgram = prog;
        gl.useProgram(prog);
      }
  
      linkAndApplyToGPUProgram(gl, prog);
      prog.applyChanges(gl);
  
      //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
    }
    else
    {
      ILogger.instance().logError("No GPUProgram found.");
    }
  
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