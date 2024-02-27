//
//  GPUVariable.cpp
//  G3M
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

#include "GPUVariable.hpp"
#include "ILogger.hpp"

bool GPUVariable::hasUniform(int code, GPUUniformKey u) {
  if (u == UNRECOGNIZED_UNIFORM) {
    return false;
  }
#ifdef C_CODE
  const int index = u;
#endif
#ifdef JAVA_CODE
  final int index = u.getValue();
#endif
  return hasUniform(code, index);
}

bool GPUVariable::hasAttribute(int code, GPUAttributeKey a) {
  if (a == UNRECOGNIZED_ATTRIBUTE) {
    return false;
  }
#ifdef C_CODE
  const int index = a;
#endif
#ifdef JAVA_CODE
  final int index = a.getValue();
#endif
  return hasAttribute(code, index);
}

bool GPUVariable::hasUniform(int code, int u) {
  return ((code >> u) & 0x00000001) != 0;
}

bool GPUVariable::hasAttribute(int code, int a) {
  return ((code >> a) & 0x00000001) != 0;
}

int GPUVariable::getUniformCode(int u) {
  return 0x00000001 << u;
}
int GPUVariable::getAttributeCode(int a) {
  return 0x00000001 << a;
}

int GPUVariable::getUniformCode(GPUUniformKey u) {
  if (u == UNRECOGNIZED_UNIFORM) {
    return 0;
  }
#ifdef C_CODE
  const int index = u;
#endif
#ifdef JAVA_CODE
  final int index = u.getValue();
#endif
  return getUniformCode(index);
}

int GPUVariable::getAttributeCode(GPUAttributeKey a) {
  if (a == UNRECOGNIZED_ATTRIBUTE) {
    return 0;
  }
#ifdef C_CODE
  const int index = a;
#endif
#ifdef JAVA_CODE
  final int index = a.getValue();
#endif
  return getUniformCode(index);
}

GPUUniformKey GPUVariable::getUniformKey(const std::string& name) {
  if (name == "uFlatColor") {
    return FLAT_COLOR;
  }
  else if (name == "uModelview") {
    return MODELVIEW;
  }
  else if (name == "uTextureExtent") {
    return TEXTURE_EXTENT;
  }
  else if (name == "uViewPortExtent") {
    return VIEWPORT_EXTENT;
  }
  else if (name == "uTranslationTexCoord") {
    return TRANSLATION_TEXTURE_COORDS;
  }
  else if (name == "uScaleTexCoord") {
    return SCALE_TEXTURE_COORDS;
  }
  else if (name == "uPointSize") {
    return POINT_SIZE;
  }
  else if (name == "uAmbientLightColor") {
    return AMBIENT_LIGHT_COLOR;
  }
  else if (name == "uDiffuseLightDirection") {
    return DIFFUSE_LIGHT_DIRECTION;
  }
  else if (name == "uDiffuseLightColor") {
    return DIFFUSE_LIGHT_COLOR;
  }
  else if (name == "uProjection") {
    return PROJECTION;
  }
  else if (name == "uCameraModel") {
    return CAMERA_MODEL;
  }
  else if (name == "uModel") {
    return MODEL;
  }
  else if (name == "uBillboardPosition") {
    return BILLBOARD_POSITION;
  }
  else if (name == "uRotationCenterTexCoord") {
    return ROTATION_CENTER_TEXTURE_COORDS;
  }
  else if (name == "uRotationAngleTexCoord") {
    return ROTATION_ANGLE_TEXTURE_COORDS;
  }
  else if (name == "Sampler") {
    return SAMPLER;
  }
  else if (name == "Sampler2") {
    return SAMPLER2;
  }
  else if (name == "Sampler3") {
    return SAMPLER3;
  }
  else if (name == "uTranslation2D") {
    return TRANSLATION_2D;
  }
  else if (name == "uBillboardAnchor") {
    return BILLBOARD_ANCHOR;
  }
  else if (name == "uCameraPosition") {
    return CAMERA_POSITION;
  }
  else {
    return UNRECOGNIZED_UNIFORM;
  }
}

GPUAttributeKey GPUVariable::getAttributeKey(const std::string& name) {
  if (name == "aPosition") {
    return POSITION;
  }
  else if (name == "aColor") {
    return COLOR;
  }
  else if (name == "aTextureCoord") {
    return TEXTURE_COORDS;
  }
  else if (name == "aTextureCoord2") {
    return TEXTURE_COORDS_2;
  }
  else if (name == "aTextureCoord3") {
    return TEXTURE_COORDS_3;
  }
  else if (name == "aNormal") {
    return NORMAL;
  }
  else if (name == "aPosition2D") {
    return POSITION_2D;
  }
  else {
    return UNRECOGNIZED_ATTRIBUTE;
  }
}
