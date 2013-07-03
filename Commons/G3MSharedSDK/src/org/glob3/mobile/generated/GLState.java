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

  /////////////////////////////////////////////////
  //CURRENT GL STATUS
  private static GLGlobalState _currentGPUGlobalState = new GLGlobalState();
  private static GPUProgram _currentGPUProgram = null;
  /////////////////////////////////////////////////

  private GPUProgramState _programState;
  private GLGlobalState _globalState;
  private final boolean _owner;

  private int _uniformsCode;
  private int _attributesCode;
  private boolean _totalGPUProgramStateChanged;
  private GPUProgram _lastGPUProgramUsed;

  private Matrix44D _modelview;
  private Matrix44D _accumulatedModelview;
  private Matrix44D _lastParentModelview;
  private boolean _multiplyModelview;

//  class ParentModelviewListener: public Matrix44DListener{
//    const GLState* _state;
//  public:
//    ParentModelviewListener(GLState* state):_state(state){}
//    void onMatrixBeingDeleted(const Matrix44D* m){
//      //ILogger::instance()->logError("BORRADO");
//      _state->_lastParentModelview = NULL;
//    }
//  };
//  mutable ParentModelviewListener _parentMatrixListener;

  private GLState _parentGLState;

  private void applyStates(GL gl, GPUProgram prog)
  {
    if (_parentGLState != null)
    {
      _parentGLState.applyStates(gl, prog);
    }
  
    //  _programState->linkToProgram(prog);
    //  _programState->applyValuesToLinkedProgram();
    _programState.applyValuesToProgram(prog);
  
    _globalState.applyChanges(gl, _currentGPUGlobalState);
  }

//  explicit GLState(const GLState& state):
//  _programState(new GPUProgramState()),
//  _globalState(new GLGlobalState()),
//  _owner(true),
//  _parentGLState(NULL),
//  _uniformsCode(0),
//  _attributesCode(0),
//  _totalGPUProgramStateChanged(true),
//  _modelview(new Matrix44D(*state._modelview)),
//  _accumulatedModelview(new Matrix44D(*state._accumulatedModelview)),
//  _multiplyModelview(state._multiplyModelview),
//  _lastParentModelview(new Matrix44D(*state._lastParentModelview))
//  {
//    
//  }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLState(GLState state);


  public GLState()
  {
     _programState = new GPUProgramState();
     _globalState = new GLGlobalState();
     _owner = true;
     _parentGLState = null;
     _uniformsCode = 0;
     _attributesCode = 0;
     _totalGPUProgramStateChanged = true;
     _modelview = null;
     _accumulatedModelview = null;
     _multiplyModelview = false;
     _lastParentModelview = null;
  }

  //For debugging purposes only
  public GLState(GLGlobalState globalState, GPUProgramState programState)
//    _parentMatrixListener(this),
  {
     _programState = programState;
     _globalState = globalState;
     _owner = false;
     _parentGLState = null;
     _uniformsCode = 0;
     _attributesCode = 0;
     _totalGPUProgramStateChanged = true;
     _modelview = null;
     _accumulatedModelview = null;
     _multiplyModelview = false;
     _lastParentModelview = null;
  }

  public void dispose()
  {
    if (_lastParentModelview != null)
    {
      //      _lastParentModelview->removeListener(&_parentMatrixListener);
      _lastParentModelview._release();
    }
    if (_modelview != null)
    {
      _modelview._release();
    }
    if (_accumulatedModelview != null)
    {
      _accumulatedModelview._release();
    }
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
    if (p != null)
    {
      //UNIFORMS AND ATTRIBUTES CODES
      int __ASK_JM;
      final int newUniformsCode = p.getUniformsCode() | _programState.getUniformsCode();
      final int newAttributesCode = p.getAttributesCode() | _programState.getAttributesCode();
  
      _totalGPUProgramStateChanged = ((newAttributesCode != _attributesCode) || (newUniformsCode != _uniformsCode));
      _uniformsCode = newUniformsCode;
      _attributesCode = newAttributesCode;
  
      //MODELVIEW
      if (_multiplyModelview)
      {
        if (_modelview != null)
        {
          final Matrix44D parentsM = p.getAccumulatedModelView();
          if (parentsM == null)
          {
            ILogger.instance().logError("CAN'T MODIFY PARENTS MODELVIEW");
          }
          else
          {
  
            if (_lastParentModelview != parentsM)
            {
  
              if (_accumulatedModelview != null)
              {
                _accumulatedModelview._release();
              }
              _accumulatedModelview = parentsM.createMultiplication(_modelview);
  
              if (_lastParentModelview != null)
              {
  //              _lastParentModelview->removeListener(&_parentMatrixListener);
                _lastParentModelview._release();
              }
  
              _lastParentModelview = parentsM;
              _lastParentModelview._retain();
  
  //            _lastParentModelview->addListener(&_parentMatrixListener);
            }
  //          else{
  //            ILogger::instance()->logInfo("REUSING MODELVIEW");
  //          }
  
          }
        }
      }
    }
  }

  public final int getUniformsCode()
  {
    if (_uniformsCode == 0)
    {
      return _programState.getUniformsCode();
    }
    return _uniformsCode;
  }

  public final int getAttributesCode()
  {
    if (_attributesCode == 0)
    {
      return _programState.getAttributesCode();
    }
    return _attributesCode;
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
  
    if (_lastGPUProgramUsed == null || _totalGPUProgramStateChanged)
    {
      //ILogger::instance()->logInfo("Total State for GPUProgram has changed since last apply");
      _lastGPUProgramUsed = progManager.getProgram(gl, getUniformsCode(), getAttributesCode());
    }
  
    if (_lastGPUProgramUsed != null)
    {
      if (_lastGPUProgramUsed != _currentGPUProgram)
      {
        if (_currentGPUProgram != null)
        {
          _currentGPUProgram.onUnused(gl);
        }
        _currentGPUProgram = _lastGPUProgramUsed;
        gl.useProgram(_lastGPUProgramUsed);
      }
  
      applyStates(gl, _lastGPUProgramUsed);
  
      //APPLY TO GPU STATE MODELVIEW
      final Matrix44D modelview = getAccumulatedModelView();
      if (modelview != null)
      {
        GPUUniformValueMatrix4Float valueModelview = new GPUUniformValueMatrix4Float(modelview);
        _lastGPUProgramUsed.getGPUUniform(GPUUniformKey.MODELVIEW.getValue()).set(valueModelview);
      }
  
      _lastGPUProgramUsed.applyChanges(gl);
  
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

  public final void setModelView(Matrix44D modelview, boolean multiply)
  {
  
    _multiplyModelview = multiply;
  
    if (_modelview == null || _modelview != modelview)
    {
  //    delete _modelview;
  //    _modelview = new Matrix44D(modelview);
  
      if (_modelview != null)
      {
        _modelview._release();
      }
  
      _modelview = modelview;
      _modelview._retain();
  
  
      //Forcing matrix multiplication next time even when parent's modelview is the same
      if (_lastParentModelview != null)
      {
  //      _lastParentModelview->removeListener(&_parentMatrixListener);
        _lastParentModelview._release();
      }
  
      _lastParentModelview = null;
    }
    //  else{
    //    ILogger::instance()->logInfo("Same modelview set.");
    //  }
  }
  public final Matrix44D getAccumulatedModelView()
  {
  
    if (!_multiplyModelview && _modelview != null)
    {
      return _modelview;
    }
  
    if (_accumulatedModelview != null)
    {
      return _accumulatedModelview;
    }
    else
    {
      if (_parentGLState != null)
      {
        return _parentGLState.getAccumulatedModelView();
      }
      else
      {
        return null;
      }
    }
  }
}