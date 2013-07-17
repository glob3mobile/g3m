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

  private int _timeStamp;
  private int _parentsTimeStamp;

  private GPUVariableValueSet _valuesSet;
  private GLGlobalState _globalState;

  private GPUProgram _lastGPUProgramUsed;

  private GLState _parentGLState;

  private void applyStates(GL gl, GPUProgram prog)
  {
    if (_parentGLState != null)
    {
      _parentGLState.applyStates(gl, prog);
    }
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
    _lastGPUProgramUsed = null;

    for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
    {
      if (_accumulatedGroups[i] != null)
         _accumulatedGroups[i].dispose();
      _accumulatedGroups[i] = null;
    }
  }


  public GLState()
  {
     _parentGLState = null;
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

      _accumulatedGroups[i] = GLFeatureGroup.createGroup(GLFeatureGroup.getGroupName(i));
      if (_parentGLState != null)
      {
        GLFeatureGroup pg = _parentGLState.getAccumulatedGroup(i);
        if (pg != null)
        {
          _accumulatedGroups[i].add(pg);
        }
      }
      if (_featuresGroups[i] != null)
      {
        _accumulatedGroups[i].add(_featuresGroups[i]);
      }
    }
    return _accumulatedGroups[i];
  }

  public void dispose()
  {
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
  
    if (parent != _parentGLState || parent == null || _parentsTimeStamp != parent.getTimeStamp())
    {
  
      _parentGLState = parent;
      if (_parentGLState != null)
      {
        _parentsTimeStamp = parent.getTimeStamp();
      }
      else
      {
        _parentsTimeStamp = 0;
      }
  
      hasChangedStructure();
  
    }
    else
    {
      //ILogger::instance()->logInfo("Reusing GLState Parent");
    }
  
  }

  public final void applyGlobalStateOnGPU(GL gl)
  {
  
    if (_parentGLState != null)
    {
      _parentGLState.applyGlobalStateOnGPU(gl);
    }
  }

  public final void applyOnGPU(GL gl, GPUProgramManager progManager)
  {
  
    if (_valuesSet == null)
    {
      _valuesSet = new GPUVariableValueSet();
      for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
      {
  
        GLFeatureGroup group = getAccumulatedGroup(i);
        if (group != null)
        {
          group.addToGPUVariableSet(_valuesSet);
        }
      }
  
  
      int uniformsCode = _valuesSet.getUniformsCode();
      int attributesCode = _valuesSet.getAttributesCode();
  
      _lastGPUProgramUsed = progManager.getProgram(gl, uniformsCode, attributesCode);
    }
  
  
    if (_globalState == null)
    {
      _globalState = new GLGlobalState();
      for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
      {
  
        GLFeatureGroup group = getAccumulatedGroup(i);
        if (group != null)
        {
          group.applyOnGlobalGLState(_globalState);
        }
      }
    }
  
    if (_lastGPUProgramUsed != null)
    {
      gl.useProgram(_lastGPUProgramUsed);
      applyStates(gl, _lastGPUProgramUsed);
  
      _valuesSet.applyValuesToProgram(_lastGPUProgramUsed);
      _globalState.applyChanges(gl, gl.getCurrentGLGlobalState());
  
      _lastGPUProgramUsed.applyChanges(gl);
  
      //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
    }
    else
    {
      ILogger.instance().logError("No GPUProgram found.");
    }
  
  }

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
  
    hasChangedStructure();
  }
}