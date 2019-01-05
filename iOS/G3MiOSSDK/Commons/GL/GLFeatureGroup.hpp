//
//  GLFeatureGroup.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#ifndef __G3MiOSSDK__GLFeatureGroup__
#define __G3MiOSSDK__GLFeatureGroup__

class GLFeature;
class GPUVariableValueSet;
class GLGlobalState;


enum GLFeatureGroupName {
  UNRECOGNIZED_GROUP = -1,
  NO_GROUP = 0,
  CAMERA_GROUP = 1,
  COLOR_GROUP = 2,
  LIGHTING_GROUP = 3
};


class GLFeatureSet {
protected:
#define MAX_CONCURRENT_FEATURES_PER_GROUP 20
#ifdef C_CODE
  GLFeature* _features[MAX_CONCURRENT_FEATURES_PER_GROUP];
#endif
#ifdef JAVA_CODE
  protected final GLFeature[] _features = new GLFeature[MAX_CONCURRENT_FEATURES_PER_GROUP];
#endif
  int _nFeatures;

  GLFeatureSet(const GLFeatureSet& that);
  GLFeatureSet& operator=(const GLFeatureSet& that);

public:

  GLFeatureSet();

  virtual ~GLFeatureSet();

  GLFeature* get(int i) const;

  void add(GLFeature* f);

  void add(const GLFeatureSet* fs);

  int size() const {
    return _nFeatures;
  }

  void clearFeatures(GLFeatureGroupName g);
  void clearFeatures();

};



#define N_GLFEATURES_GROUPS 4

class GLFeatureGroup {

  static GLFeatureGroup** _groups;

public:

  virtual ~GLFeatureGroup() {}

  static GLFeatureGroup* createGroup(GLFeatureGroupName name);
  static GLFeatureGroupName getGroupName(int i);

  static void applyToAllGroups(const GLFeatureSet& features,
                               GPUVariableValueSet& vs,
                               GLGlobalState& state);

  virtual void apply(const GLFeatureSet& features,
                     GPUVariableValueSet& vs,
                     GLGlobalState& state) = 0;
};

class GLFeatureNoGroup: public GLFeatureGroup {
public:
  void apply(const GLFeatureSet& features,
             GPUVariableValueSet& vs,
             GLGlobalState& state);
};

class GLFeatureCameraGroup: public GLFeatureGroup{
public:
  void apply(const GLFeatureSet& features,
             GPUVariableValueSet& vs,
             GLGlobalState& state);
};


class GLFeatureColorGroup: public GLFeatureGroup{
public:
  void apply(const GLFeatureSet& features,
             GPUVariableValueSet& vs,
             GLGlobalState& state);
};

class GLFeatureLightingGroup: public GLFeatureGroup{
public:
  void apply(const GLFeatureSet& features,
             GPUVariableValueSet& vs,
             GLGlobalState& state);
};

#endif
