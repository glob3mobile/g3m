package org.glob3.mobile.generated; 
//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

//
//  GLFeatureGroup.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//





//class GLFeature;

public class GLFeatureSet
{
   protected static final int MAX_CONCURRENT_FEATURES_PER_GROUP = 20;
  protected final GLFeature[] _features = new GLFeature[MAX_CONCURRENT_FEATURES_PER_GROUP];
  protected int _nFeatures;


  public GLFeatureSet()
  {
     _nFeatures = 0;
    for (int i = 0; i < MAX_CONCURRENT_FEATURES_PER_GROUP; i++)
    {
      _features[i] = null;
    }
  }

  public void dispose()
  {
    for (int i = 0; i < _nFeatures; i++)
    {
      _features[i]._release();
    }
  }

  public final GLFeature get(int i)
  {
    if (_nFeatures < i)
    {
      return null;
    }
    return _features[i];
  }

  public final void add(GLFeature f)
  {
    _features[_nFeatures++] = f;
    f._retain();
  
    if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP)
    {
      ILogger.instance().logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
    }
  }

  public final void add(GLFeatureSet fs)
  {
    final int size = fs.size();
    for (int i = 0; i < size; i++)
    {
      add(fs.get(i));
    }
  }

  public final int size()
  {
    return _nFeatures;
  }

}