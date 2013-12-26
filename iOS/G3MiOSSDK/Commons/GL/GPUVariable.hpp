//
//  GPUVariable.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

#ifndef __G3MiOSSDK__GPUVariable__
#define __G3MiOSSDK__GPUVariable__

#include <string>

enum GPUVariableType{
  ATTRIBUTE = 1,
  UNIFORM = 2
};

enum GPUUniformKey{
  UNRECOGNIZED_UNIFORM = -1,
  FLAT_COLOR = 0,
  MODELVIEW = 1,
  TEXTURE_EXTENT = 2,
  VIEWPORT_EXTENT = 3,
  TRANSLATION_TEXTURE_COORDS = 4,
  SCALE_TEXTURE_COORDS = 5,
  POINT_SIZE= 6,
  AMBIENT_LIGHT_COLOR = 7,
  DIFFUSE_LIGHT_DIRECTION = 8,
  DIFFUSE_LIGHT_COLOR = 9,
  PROJECTION = 10,
  CAMERA_MODEL = 11,
  MODEL = 12,
  POINT_LIGHT_POSITION= 13,
  POINT_LIGHT_COLOR= 14,
  BILLBOARD_POSITION = 15,
  ROTATION_CENTER_TEXTURE_COORDS = 16,
  ROTATION_ANGLE_TEXTURE_COORDS = 17,
  SAMPLER = 18,
  SAMPLER2 = 19,
  SAMPLER3 = 20
};

enum GPUAttributeKey{
  UNRECOGNIZED_ATTRIBUTE = -1,
  POSITION = 0,
  TEXTURE_COORDS = 1,
  COLOR = 2,
  NORMAL = 3,
  TEXTURE_COORDS_2 = 4,
  TEXTURE_COORDS_3 = 5
};

class GPUVariable {
public:

  static GPUUniformKey getUniformKey(const std::string& name);
  static GPUAttributeKey getAttributeKey(const std::string& name);

  static int getUniformCode(GPUUniformKey u);
  static int getAttributeCode(GPUAttributeKey a);

  static int getUniformCode(int u);
  static int getAttributeCode(int a);

  static bool hasUniform(int code, int u);
  static bool hasAttribute(int code, int a);

  static bool hasUniform(int code, GPUUniformKey u);
  static bool hasAttribute(int code, GPUAttributeKey a);

  virtual ~GPUVariable() {}

  const GPUVariableType _variableType;
  const std::string     _name;

  GPUVariable(const std::string& name,
              GPUVariableType type) :
  _name(name),
  _variableType(type)
  {
  }


};

#endif
