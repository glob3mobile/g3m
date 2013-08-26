//
//  GPUVariable.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

#include "GPUVariable.hpp"
#include "ILogger.hpp"

//const int UNRECOGNIZED = -1;
//const int FLAT_COLOR = 1;
//const int MODELVIEW = 2;
//const int TEXTURE_EXTENT = 3;
//const int VIEWPORT_EXTENT = 4;
//const int TRANSLATION_TEXTURE_COORDS = 5;
//const int SCALE_TEXTURE_COORDS = 6;
//const int POINT_SIZE = 7;
//
//const int POSITION = 8;
//const int TEXTURE_COORDS = 9;
//const int COLOR = 10;
//
////TODO: DELETE
//const int EnableColorPerVertex = 11;
//const int EnableTexture = 12;
//const int EnableFlatColor = 13;
//const int FlatColorIntensity = 14;
//const int ColorPerVertexIntensity = 15;
//
//const int GROUP_NOGROUP = -1;
//const int GROUP_COLOR = 1;

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

  return UNRECOGNIZED_ATTRIBUTE;
}
/*
void createMetadata() {
  _group = GROUP_NOGROUP;
  _priority = -1;
  _key = getKeyForName(_name, _variableType);

  if (_key == UNRECOGNIZED) {
    ILogger::instance()->logError("Unrecognized GPU VARAIBLE %s\n", _name.c_str());
  }

  if (_variableType == UNIFORM) {
    if (_key == FLAT_COLOR) {
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_EXTENT) {
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }
//
//    if (true) { //DELETE
//      if (_key == EnableColorPerVertex) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == EnableTexture) {
//        _group = GROUP_COLOR;
//      }
//
//      if ( _key == EnableFlatColor) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == FlatColorIntensity) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == ColorPerVertexIntensity) {
//        _group = GROUP_COLOR;
//      }
//    }
  }

  if (_variableType == ATTRIBUTE) {

    if (_key == COLOR) {
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }
  }


}
*/