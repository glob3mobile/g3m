//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include <iostream>

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramState.hpp"
#include "GPUProgramManager.hpp"

class GLState{

  /////////////////////////////////////////////////
  //CURRENT GL STATUS
  static GLGlobalState _currentGPUGlobalState;
  static GPUProgram*   _currentGPUProgram;
  /////////////////////////////////////////////////
  
  GPUProgramState* _programState;
  GLGlobalState*   _globalState;
  const bool _owner;

  mutable int _uniformsCode;
  mutable int _attributesCode;
  mutable bool _totalGPUProgramStateChanged;
  mutable GPUProgram* _lastGPUProgramUsed;

  Matrix44D* _modelview;
  mutable Matrix44D* _accumulatedModelview;
  mutable Matrix44D* _lastParentsModelview;
  bool _modelviewModifiesParents;
  
#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif
  
  void applyStates(GL* gl, GPUProgram* prog) const;
  
  explicit GLState(const GLState& state):
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(new Matrix44D(*state._modelview)),
  _accumulatedModelview(new Matrix44D(*state._accumulatedModelview)),
  _modelviewModifiesParents(state._modelviewModifiesParents){}
  
public:
  
  GLState():
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(NULL),
  _accumulatedModelview(NULL),
  _modelviewModifiesParents(false){}
  
  //For debugging purposes only
  GLState(GLGlobalState*   globalState,
          GPUProgramState* programState):
  _programState(programState),
  _globalState(globalState),
  _owner(false),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(NULL),
  _accumulatedModelview(NULL),
  _modelviewModifiesParents(false){}
  
  ~GLState(){
    if (_owner){
      delete _programState;
      delete _globalState;
    }
  }
  
  const GLState* getParent() const{
    return _parentGLState;
  }
  
  void setParent(const GLState* p) const{
    _parentGLState = p;
    if (p != NULL){
      //UNIFORMS AND ATTRIBUTES CODES
      int newUniformsCode = p->getUniformsCode() | _programState->getUniformsCode();
      int newAttributesCode = p->getAttributesCode() | _programState->getAttributesCode();

      _totalGPUProgramStateChanged = (newAttributesCode != _attributesCode) || (newUniformsCode != _uniformsCode);
      _uniformsCode = newUniformsCode;
      _attributesCode = newAttributesCode;

      //MODELVIEW
      if (_modelviewModifiesParents){
        Matrix44D* parentsM = p->getAccumulatedModelView();
        if (parentsM == NULL){
          ILogger::instance()->logError("CAN'T MODIFIE PARENTS MODELVIEW");
        }

        if (_lastParentsModelview != parentsM && _modelview != NULL){
          delete _accumulatedModelview;
          _accumulatedModelview = parentsM->multiply(*_modelview);
        }
        
      }


      
    }
  }

  int getUniformsCode() const{
    if (_uniformsCode == 0){
      return _programState->getUniformsCode();
    }
    return _uniformsCode;
  }

  int getAttributesCode() const{
    if (_attributesCode == 0){
      return _programState->getAttributesCode();
    }
    return _attributesCode;
  }

  void applyGlobalStateOnGPU(GL* gl) const;
  
  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;
  
  GPUProgramState* getGPUProgramState() const{
    return _programState;
  }
  
  GLGlobalState* getGLGlobalState() const{
    return _globalState;
  }
  
  static void textureHasBeenDeleted(const IGLTextureId* textureId){
    if (_currentGPUGlobalState.getBoundTexture() == textureId){
      _currentGPUGlobalState.bindTexture(NULL);
    }
  }
  
  static GLGlobalState* createCopyOfCurrentGLGlobalState(){
    return _currentGPUGlobalState.createCopy();
  }

  void setModelView(const Matrix44D& modelview, bool modifiesParents);
  Matrix44D* getAccumulatedModelView() const;
};

#endif /* defined(__G3MiOSSDK__GLState__) */
