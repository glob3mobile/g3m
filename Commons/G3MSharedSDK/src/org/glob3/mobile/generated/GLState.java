package org.glob3.mobile.generated; 
//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





public class GLState extends RCObject
{
  private GLFeatureSet _features;
  private GLFeatureSet _accumulatedFeatures;

  private int _timestamp;
  private int _parentsTimestamp;

  private GPUVariableValueSet _valuesSet;
  private GLGlobalState _globalState;

  private GPUProgram _linkedProgram;

  private GLState _parentGLState;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLState(GLState state);

  private void hasChangedStructure()
  {
    _timestamp++;
    if (_valuesSet != null)
       _valuesSet.dispose();
    _valuesSet = null;
    if (_globalState != null)
       _globalState.dispose();
    _globalState = null;
  
    if (_linkedProgram != null)
    {
      _linkedProgram.removeReference();
      _linkedProgram = null;
    }
  
    if (_accumulatedFeatures != null)
       _accumulatedFeatures.dispose();
    _accumulatedFeatures = null;
  }

  public void dispose()
  {
    if (_features != null)
       _features.dispose();
    if (_accumulatedFeatures != null)
       _accumulatedFeatures.dispose();
  
    if (_valuesSet != null)
       _valuesSet.dispose();
    if (_globalState != null)
       _globalState.dispose();
  
    if (_parentGLState != null)
    {
      _parentGLState._release();
    }
  
    if (_linkedProgram != null)
    {
      _linkedProgram.removeReference();
    }
  }


  public GLState()
  {
     _parentGLState = null;
     _linkedProgram = null;
     _parentsTimestamp = -1;
     _timestamp = 0;
     _valuesSet = null;
     _globalState = null;
     _accumulatedFeatures = null;
    _features = new GLFeatureSet();
  }

  public final int getTimestamp()
  {
     return _timestamp;
  }

  public final GLFeatureSet getAccumulatedFeatures()
  {
    if (_accumulatedFeatures == null)
    {
  
      _accumulatedFeatures = new GLFeatureSet();
  
      if (_parentGLState != null)
      {
        GLFeatureSet parents = _parentGLState.getAccumulatedFeatures();
        if (parents != null)
        {
          _accumulatedFeatures.add(parents);
        }
      }
      _accumulatedFeatures.add(_features);
  
    }
    return _accumulatedFeatures;
  }
//  GLFeatureSet* createAccumulatedFeatures() const;


  public final void setParent(GLState parent)
  {
  
    if (parent == null)
    {
      if (parent != _parentGLState)
      {
        _parentGLState = null;
        _parentsTimestamp = -1;
        hasChangedStructure();
      }
    }
    else
    {
      final int parentsTimestamp = parent.getTimestamp();
      if ((parent != _parentGLState) || (_parentsTimestamp != parentsTimestamp))
      {
  
        if (_parentGLState != parent)
        {
          if (_parentGLState != null)
          {
            _parentGLState._release();
          }
          _parentGLState = parent;
          _parentGLState._retain();
        }
  
        _parentsTimestamp = parentsTimestamp;
        hasChangedStructure();
      }
    }
  }

  public final void applyOnGPU(GL gl, GPUProgramManager progManager)
  {
  
  
    if (_valuesSet == null && _globalState == null)
    {
  
      _valuesSet = new GPUVariableValueSet();
      _globalState = new GLGlobalState();
  
      GLFeatureSet accumulatedFeatures = getAccumulatedFeatures();
  
      GLFeatureGroup.applyToAllGroups(accumulatedFeatures, _valuesSet, _globalState);
  
      final int uniformsCode = _valuesSet.getUniformsCode();
      final int attributesCode = _valuesSet.getAttributesCode();
  
      _linkedProgram = progManager.getProgram(gl, uniformsCode, attributesCode); //GET RETAINED REFERENCE
    }
  
    if (_valuesSet == null || _globalState == null)
    {
      ILogger.instance().logError("GLState logic error.");
      return;
    }
  
    if (_linkedProgram != null)
    {
      gl.useProgram(_linkedProgram);
  
      _valuesSet.applyValuesToProgram(_linkedProgram);
      _globalState.applyChanges(gl, gl.getCurrentGLGlobalState());
  
      _linkedProgram.applyChanges(gl);
  
      //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
    }
    else
    {
      ILogger.instance().logError("No GPUProgram found.");
    }
  
  }

  public final void addGLFeature(GLFeature f, boolean mustRetain)
  {
    _features.add(f);
  
    if (!mustRetain)
    {
      f._release();
    }
  
    hasChangedStructure();
  }

  public final void clearGLFeatureGroup(GLFeatureGroupName g)
  {
    _features.clearFeatures(g);
    hasChangedStructure();
  }

  public final void clearAllGLFeatures()
  {
    _features.clearFeatures();
    hasChangedStructure();
  }

  public final int getNumberOfGLFeatures()
  {
    return _features.size();
  }

  public final GLFeature getGLFeature(GLFeatureID id)
  {
    final int size = _features.size();
    for (int i = 0; i < size; i++)
    {
      GLFeature f = _features.get(i);
      if (f._id == id)
      {
        return f;
      }
    }
  
    return null;
  }

  public final GLFeatureSet getGLFeatures(GLFeatureID id)
  {
    GLFeatureSet features = new GLFeatureSet();
    final int size = _features.size();
    for (int i = 0; i < size; i++)
    {
      GLFeature f = _features.get(i);
      if (f._id == id)
      {
        features.add(f);
      }
    }
    return features;
  }
}