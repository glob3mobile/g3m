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



///#include "GPUProgramState.hpp"


public class GLState
{

  private GLFeatureGroup[] _featuresGroups = new GLFeatureGroup[DefineConstants.N_GLFEATURES_GROUPS]; //1 set for group of features
  private GLFeatureGroup[] _accumulatedGroups = new GLFeatureGroup[DefineConstants.N_GLFEATURES_GROUPS]; //1 set for group of features

//  GLFeatureNoGroup _featureNoGroup;
//  GLFeatureCameraGroup _featureCameraGroup;
//  GLFeatureColorGroup _featureColorGroup;

  private int _timeStamp;
  private int _parentsTimeStamp;

  private GPUVariableValueSet _valuesSet;
  private GLGlobalState _globalState;


//  GPUProgramState* _programState;
//  GLGlobalState*   _globalState;
//  const bool _owner;

  private int _uniformsCode;
  private int _attributesCode;
  private boolean _totalGPUProgramStateChanged;
  private GPUProgram _lastGPUProgramUsed;

///#ifdef C_CODE
//  const Matrix44D* _modelview;
//  mutable const Matrix44D* _accumulatedModelview;
//  mutable const Matrix44D* _lastParentModelview;
///#else
//  Matrix44D* _modelview;
//  mutable Matrix44D* _accumulatedModelview;
//  mutable Matrix44D* _lastParentModelview;
///#endif
//  bool _multiplyModelview;
//  mutable GPUUniformValueMatrix4Float* _modelviewUniformValue;

  private GLState _parentGLState;

  private void applyStates(GL gl, GPUProgram prog)
  {
    if (_parentGLState != null)
    {
      _parentGLState.applyStates(gl, prog);
    }
  
    //  _programState->linkToProgram(prog);
    //  _programState->applyValuesToLinkedProgram();
    //  _programState->applyValuesToProgram(prog);
    //
    //  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLState(GLState state);

  private void hasChangedStructure()
  {
    _timeStamp++;
    if (_valuesSet != null)
       _valuesSet.dispose();
    _valuesSet = null;
    if (_globalState != null)
       _globalState.dispose();
    _globalState = null;
  }


  public GLState()
//  _programState(new GPUProgramState()),
//  _globalState(new GLGlobalState()),
//  _owner(true),
//  _modelview(NULL),

  //,
//  _accumulatedModelview(NULL),
//  _multiplyModelview(false),
//  _lastParentModelview(NULL),
//  _modelviewUniformValue(NULL)
  {
     _parentGLState = null;
     _uniformsCode = 0;
     _attributesCode = 0;
     _totalGPUProgramStateChanged = true;
     _lastGPUProgramUsed = null;
     _parentsTimeStamp = 0;
     _timeStamp = 0;
     _valuesSet = null;
     _globalState = null;

    for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
    {
      _featuresGroups[i] = null;
      _accumulatedGroups[i] = null;
    }

  }

  public final int getTimeStamp()
  {
     return _timeStamp;
  }

  public final GLFeatureGroup getAccumulatedGroup(int i)
  {
    if (_accumulatedGroups[i] == null)
    {
      return _featuresGroups[i];
    }
    return _accumulatedGroups[i];
  }
  /*
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
   _multiplyModelview(false),
   //    _parentMatrixListener(this),
   _lastParentModelview(NULL),
   _modelviewUniformValue(NULL){}
   */

  //GLGlobalState GLState::_currentGPUGlobalState;
  //GPUProgram* GLState::_currentGPUProgram = NULL;
  
  public void dispose()
  {
    //  if (_lastParentModelview != NULL){
    //    //      _lastParentModelview->removeListener(&_parentMatrixListener);
    //    _lastParentModelview->_release();
    //  }
    //  if (_modelview != NULL){
    //    _modelview->_release();
    //  }
    //  if (_accumulatedModelview != NULL){
    //    _accumulatedModelview->_release();
    //  }
    //
    //  if (_modelviewUniformValue != NULL){
    //    _modelviewUniformValue->_release();
    //  }
  
    //  if (_owner){
    //    delete _programState;
    //    delete _globalState;
    //  }
  
    for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
    {
      if (_featuresGroups[i] != null)
         _featuresGroups[i].dispose();
      if (_accumulatedGroups[i] != null)
         _accumulatedGroups[i].dispose();
    }
  
    if (_valuesSet != null)
       _valuesSet.dispose();
    if (_globalState != null)
       _globalState.dispose();
  }

  public final GLState getParent()
  {
    return _parentGLState;
  }

  public final void setParent(GLState parent)
  {
  
    if (parent != _parentGLState || _parentsTimeStamp != parent.getTimeStamp())
    {
  
      _parentGLState = parent;
      _parentsTimeStamp = parent.getTimeStamp();
  
      for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
      {
        if (_accumulatedGroups[i] != null)
           _accumulatedGroups[i].dispose();
        _accumulatedGroups[i] = GLFeatureGroup.createGroup(GLFeatureGroupName.forValue(i));
        GLFeatureSet pfs = parent.getAccumulatedGroup(i);
        if (pfs != null) //PARENT'S FEATURES
        {
          _accumulatedGroups[i].add(pfs);
        }
        if (_featuresGroups[i] != null) //THIS'S FEATURES
        {
          _accumulatedGroups[i].add(_featuresGroups[i]);
        }
      }
  
      hasChangedStructure();
  
    }
    else
    {
      //ILogger::instance()->logInfo("Reusing GLState Parent");
    }
  
    //  if (parent != NULL){
    //    //UNIFORMS AND ATTRIBUTES CODES
    ////    const int newUniformsCode   = parent->getUniformsCode()   | _programState->getUniformsCode();
    //////    const int newAttributesCode = parent->getAttributesCode() | _programState->getAttributesCode();
    ////
    ////    _totalGPUProgramStateChanged = ((newAttributesCode != _attributesCode) ||
    ////                                    (newUniformsCode   != _uniformsCode));
    ////    _uniformsCode   = newUniformsCode;
    ////    _attributesCode = newAttributesCode;
    //
    ////    //MODELVIEW
    ////    if (_multiplyModelview){
    ////      if (_modelview != NULL){
    ////        const Matrix44D* parentsM = parent->getAccumulatedModelView();
    ////        if (parentsM == NULL){
    ////          ILogger::instance()->logError("CAN'T MODIFY PARENTS MODELVIEW");
    ////        } else{
    ////
    ////          if (_lastParentModelview != parentsM){
    ////
    ////            if (_accumulatedModelview != NULL){
    ////              _accumulatedModelview->_release();
    ////            }
    ////            _accumulatedModelview = parentsM->createMultiplication(*_modelview);
    ////
    ////            if (_lastParentModelview != NULL){
    ////              //              _lastParentModelview->removeListener(&_parentMatrixListener);
    ////              _lastParentModelview->_release();
    ////            }
    ////
    ////            _lastParentModelview = parentsM;
    ////            _lastParentModelview->_retain();
    ////
    ////            //            if (_modelviewUniformValue != NULL){
    ////            //              _modelviewUniformValue->_release();
    ////            //              _modelviewUniformValue = NULL;
    ////            //            }
    ////
    ////            //            _lastParentModelview->addListener(&_parentMatrixListener);
    ////          }
    ////          //          else{
    ////          //            ILogger::instance()->logInfo("REUSING MODELVIEW");
    ////          //          }
    ////
    ////        }
    ////      }
    ////    }
    //  }
  }

//  int getUniformsCode() const{
//    if (_parentGLState == NULL){
//      return _programState->getUniformsCode();
//    }
//    return _uniformsCode;
//  }
//
//  int getAttributesCode() const{
//    if (_parentGLState == NULL){
//      return _programState->getAttributesCode();
//    }
//    return _attributesCode;
//  }

  public final void applyGlobalStateOnGPU(GL gl)
  {
  
    if (_parentGLState != null)
    {
      _parentGLState.applyGlobalStateOnGPU(gl);
    }
  
    //  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
  }

  public final void applyOnGPU(GL gl, GPUProgramManager progManager)
  {
  
  
    //  int uniformsCode = getUniformsCode();
    //  int attributesCode = getAttributesCode();
  
    /////////////////////////// FEATURES
    if (_valuesSet == null)
    {
      _valuesSet = new GPUVariableValueSet();
      for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
      {
  
        GLFeatureGroup group = _accumulatedGroups[i];
        if (group != null)
        {
  //        GPUVariableValueSet* variables = group->createGPUVariableSet();
  //        if (variables != NULL){
  //          _valuesSet->combineWith(variables);
  //          delete variables;
  //        }
          group.addToGPUVariableSet(_valuesSet);
        }
      }
  
  
      int uniformsCode = _valuesSet.getUniformsCode();
      int attributesCode = _valuesSet.getAttributesCode();
      if (uniformsCode != _uniformsCode || attributesCode != _attributesCode)
      {
        _uniformsCode = uniformsCode;
        _attributesCode = attributesCode;
        _totalGPUProgramStateChanged = true;
      }
    }
  
  
    if (_globalState == null)
    {
      _globalState = new GLGlobalState();
      for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
      {
  
        GLFeatureGroup group = _accumulatedGroups[i];
        if (group != null)
        {
          //      group->applyGlobalGLState(gl);
          group.applyOnGlobalGLState(_globalState);
        }
      }
    }
  
    //  const GLFeatureGroup* _groups[] = {&_featureNoGroup, &_featureCameraGroup, &_featureColorGroup};
    //  for (int i = 0; i < 3; i++){
    //
    //    GLFeatureSet* fs = createAccumulatedGLFeaturesForGroup(GLFeatureGroup::getGroupName(i));
    //    if (fs != NULL){
    //
    //      GLFeatureGroup* group = GLFeatureGroup::getGroup(i);
    //      GPUVariableValueSet* variables = group->applyAndCreateGPUVariableSet(gl, fs);
    //      delete fs;
    //      if (variables != NULL){
    //        values.combineWith(variables);
    //        delete variables;
    //      }
    //    }
    //  }
  
  
    /////////////////////////// FEATURES
  
  
  
    if (_lastGPUProgramUsed == null || _totalGPUProgramStateChanged)
    {
      //ILogger::instance()->logInfo("Total State for GPUProgram has changed since last apply");
      _lastGPUProgramUsed = progManager.getProgram(gl, _uniformsCode, _attributesCode);
      _totalGPUProgramStateChanged = false;
    }
  
    if (_lastGPUProgramUsed != null)
    {
      gl.useProgram(_lastGPUProgramUsed);
      applyStates(gl, _lastGPUProgramUsed);
  
      //APPLY TO GPU STATE MODELVIEW
      //    const Matrix44D* modelview = getAccumulatedModelView();
      //    GPUUniformValueMatrix4Float* modelviewValue = getModelviewUniformValue();
      //    if (modelviewValue != NULL){
      //      //      GPUUniformValueMatrix4Float valueModelview(*modelview);
      ///#ifdef C_CODE
      //      _lastGPUProgramUsed->getGPUUniform(MODELVIEW)->set(modelviewValue);
      ///#endif
      ///#ifdef JAVA_CODE
      //      _lastGPUProgramUsed.getGPUUniform(GPUUniformKey.MODELVIEW.getValue()).set(modelviewValue);
      ///#endif
      //    }
  
  
      /////////////////////////// FEATURES
      _valuesSet.applyValuesToProgram(_lastGPUProgramUsed);
      _globalState.applyChanges(gl, gl.getCurrentGLGlobalState());
  
  //    delete _valuesSet;
  //    _valuesSet = NULL;
      /////////////////////////// FEATURES
  
  
      _lastGPUProgramUsed.applyChanges(gl);
  
      //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
    }
    else
    {
      ILogger.instance().logError("No GPUProgram found.");
    }
  
  
  
  
  }

//  GPUProgramState* getGPUProgramState() const{
//    return _programState;
//  }
//
//  GLGlobalState* getGLGlobalState() const{
//    return _globalState;
//  }

  //  static void textureHasBeenDeleted(const IGLTextureId* textureId){
  //    if (_currentGPUGlobalState.getBoundTexture() == textureId){
  //      _currentGPUGlobalState.bindTexture(NULL);
  //    }
  //  }
  //
  //  static GLGlobalState* createCopyOfCurrentGLGlobalState(){
  //    return _currentGPUGlobalState.createCopy();
  //  }

//  void setModelView(const Matrix44D* modelview, bool modifiesParents);
//  const Matrix44D* getAccumulatedModelView() const;

//  GPUUniformValueMatrix4Float* getModelviewUniformValue() const;


  public final void addGLFeature(GLFeature f, boolean mustRetain)
  {
    GLFeatureGroupName g = f.getGroup();
    final int index = g.getValue();

    if (_featuresGroups[index] == null)
    {
      _featuresGroups[index] = GLFeatureGroup.createGroup(g);
    }

    _featuresGroups[index].add(f);
    if (!mustRetain)
    {
      f._release();
    }

    hasChangedStructure();
  }

//  void addGLFeatureAndRelease(const GLFeature* f){
//    addGLFeature(f);
//    f->_release();
//  }

//  GLFeatureSet* createAccumulatedGLFeaturesForGroup(GLFeatureGroupName g) const;


  //void GLState::setModelView(const Matrix44D* modelview, bool multiply){
  //
  //  _multiplyModelview = multiply;
  //
  //  if (_modelview == NULL || _modelview != modelview){
  //    //    delete _modelview;
  //    //    _modelview = new Matrix44D(modelview);
  //
  //    if (_modelview != NULL){
  //      _modelview->_release();
  //    }
  //
  //    _modelview = modelview;
  //    _modelview->_retain();
  //
  //
  //    //Forcing matrix multiplication next time even when parent's modelview is the same
  //    if (_lastParentModelview != NULL){
  //      //      _lastParentModelview->removeListener(&_parentMatrixListener);
  //      _lastParentModelview->_release();
  //    }
  //
  //    _lastParentModelview = NULL;
  //
  //    if (_modelviewUniformValue != NULL){
  //      _modelviewUniformValue->_release();
  //      _modelviewUniformValue = NULL;
  //    }
  //  }
  //  //  else{
  //  //    ILogger::instance()->logInfo("Same modelview set.");
  //  //  }
  //}
  
  //const Matrix44D* GLState::getAccumulatedModelView() const{
  //
  //  if (!_multiplyModelview && _modelview != NULL){
  //    return _modelview;
  //  }
  //
  //  if (_accumulatedModelview != NULL){
  //    return _accumulatedModelview;
  //  }
  //  if (_parentGLState != NULL){
  //    return _parentGLState->getAccumulatedModelView();
  //  }
  //  return NULL;
  //
  //};
  
  //GPUUniformValueMatrix4Float* GLState::getModelviewUniformValue() const{
  //
  //  const Matrix44D* mv = getAccumulatedModelView();
  //
  //  if (_modelviewUniformValue == NULL){
  //    if (mv == NULL){
  //      return NULL;
  //    }
  //    _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
  //  } else{
  //    if (mv != _modelviewUniformValue->getMatrix()){
  //      _modelviewUniformValue->_release();
  //      _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
  //    }
  //  }
  //  return _modelviewUniformValue;
  //
  //}
  
  //GLFeatureSet* GLState::createAccumulatedGLFeaturesForGroup(GLFeatureGroupName g) const{
  ////TODO: WHY THIS DOES NOT WORK???? -> SOLVED: NOT TOP-BOTTOM
  ////  GLFeatureSet* fs = NULL;
  ////  const GLState* state = this;
  ////  const int index = g;
  ////  while (state != NULL) {
  ////    const GLFeatureSet* const thisFS = state->_featuresSets[index];
  ////    if (thisFS != NULL){
  ////      if (fs == NULL){
  ////        fs = new GLFeatureSet();
  ////      }
  ////      fs->add(thisFS);
  ////    }
  ////    state = state->getParent();
  ////  }
  ////  return fs;
  //
  //
  //
  //  GLFeatureSet* pfs = NULL;
  //  if (_parentGLState != NULL){
  //    pfs = _parentGLState->createAccumulatedGLFeaturesForGroup(g);
  //  }
  //
  //  const int index = g;
  //  const GLFeatureSet* const thisFS = _featuresSets[index];
  //  if (thisFS == NULL){
  //    return pfs;
  //  } else{
  //    GLFeatureSet* fs = new GLFeatureSet();
  //    if (pfs != NULL){
  //      fs->add(pfs);
  //      delete pfs;
  //    }
  //    fs->add(thisFS);
  //    return fs;
  //  }
  //
  //}
  
  public final void clearGLFeatureGroup(GLFeatureGroupName g)
  {
  
  
    final int index = g.getValue();
  
    GLFeatureGroup group = _featuresGroups[index];
  
  
    if (group != null)
    {
      if (group != null)
         group.dispose();
      _featuresGroups[index] = null;
    }
  
    GLFeatureGroup aGroup = _accumulatedGroups[index];
    if (aGroup != null)
    {
      if (aGroup != null)
         aGroup.dispose();
      _accumulatedGroups[index] = null;
    }
  
    hasChangedStructure();
  }
}