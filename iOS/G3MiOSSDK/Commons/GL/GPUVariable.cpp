//
//  GPUVariable.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

#include "GPUVariable.hpp"
#include "ILogger.hpp"

bool GPUVariable::codeContainsUniform(int code, GPUUniformKey u) {
  if (u == UNRECOGNIZED_UNIFORM) {
    return false;
  }
#ifdef C_CODE
  const int index = u;
#endif
#ifdef JAVA_CODE
  final int index = u.getValue();
#endif
  return codeContainsUniform(code, index);
}

bool GPUVariable::codeContainsAttribute(int code, GPUAttributeKey a) {
  if (a == UNRECOGNIZED_ATTRIBUTE) {
    return false;
  }
#ifdef C_CODE
  const int index = a;
#endif
#ifdef JAVA_CODE
  final int index = a.getValue();
#endif
  return codeContainsAttribute(code, index);
}

bool GPUVariable::codeContainsUniform(int code, int u) {
  return ((code >> u) & 0x00000001) != 0;
}

bool GPUVariable::codeContainsAttribute(int code, int a) {
  return ((code >> a) & 0x00000001) != 0;
}

int GPUVariable::getUniformCode(int u) {
  return 0x00000001  << u;
}
int GPUVariable::getAttributeCode(int a) {
  return 0x00000001  << a;
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

  if (name.compare("uFlatColor") == 0) {
    return FLAT_COLOR;
  }

  if (name.compare("uModelview") == 0) {
    return MODELVIEW;
  }

  if (name.compare("uTextureExtent") == 0) {
    return TEXTURE_EXTENT;
  }

  if (name.compare("uViewPortExtent") == 0) {
    return  VIEWPORT_EXTENT;
  }

  if (name.compare("uTranslationTexCoord") == 0) {
    return  TRANSLATION_TEXTURE_COORDS;
  }

  if (name.compare("uScaleTexCoord") == 0) {
    return  SCALE_TEXTURE_COORDS;
  }

  if (name.compare("uPointSize") == 0) {
    return  POINT_SIZE;
  }

  if (name.compare("uAmbientLight") == 0) {
    return AMBIENT_LIGHT;
  }

  if (name.compare("uLightDirection") == 0) {
    return LIGHT_DIRECTION;
  }

  if (name.compare("uLightColor") == 0) {
    return LIGHT_COLOR;
  }

  if (name.compare("uProjection") == 0) {
    return PROJECTION;
  }

  if (name.compare("uCameraModel") == 0) {
    return CAMERA_MODEL;
  }

  if (name.compare("uModel") == 0) {
    return MODEL;
  }

  if (name.compare("uBillboardPosition") == 0) {
    return  BILLBOARD_POSITION;
  }

  return UNRECOGNIZED_UNIFORM;
}

GPUAttributeKey GPUVariable::getAttributeKey(const std::string& name) {

  if (name.compare("aPosition") == 0) {
    return POSITION;
  }

  if (name.compare("aColor") == 0) {
    return  COLOR;
  }

  if (name.compare("aTextureCoord") == 0) {
    return  TEXTURE_COORDS;
  }

  if (name.compare("aNormal") == 0) {
    return  NORMAL;
  }

  return UNRECOGNIZED_ATTRIBUTE;
}
