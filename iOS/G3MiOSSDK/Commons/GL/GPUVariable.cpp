//
//  GPUVariable.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

#include "GPUVariable.hpp"
#include "ILogger.hpp"

const int GPUVariable::UNRECOGNIZED = -1;
const int GPUVariable::FLAT_COLOR = 1;
const int GPUVariable::MODELVIEW = 2;
const int GPUVariable::TEXTURE_EXTENT = 3;
const int GPUVariable::VIEWPORT_EXTENT = 4;
const int GPUVariable::TRANSLATION_TEXTURE_COORDS = 5;
const int GPUVariable::SCALE_TEXTURE_COORDS = 6;
const int GPUVariable::POINT_SIZE = 7;

const int GPUVariable::GROUP_NOGROUP = -1;
const int GPUVariable::GROUP_COLOR = 1;

void GPUVariable::createMetadata(){
  _group = GROUP_NOGROUP;
  _priority = -1;
  _key = UNRECOGNIZED;
  
  if (_variableType == UNIFORM){
    if (_name.compare("uFlatColor") == 0){
      _key = FLAT_COLOR;
      _group = GROUP_COLOR;
    }
    
    if (_name.compare("uModelview") == 0){
      _key = MODELVIEW;
    }
    
    if (_name.compare("uTextureExtent") == 0){
      _key = TEXTURE_EXTENT;
      _group = GROUP_COLOR;
    }
    
    if (_name.compare("uViewPortExtent") == 0){
      _key = VIEWPORT_EXTENT;
    }
    
    if (_name.compare("uTranslationTexCoord") == 0){
      _key = TRANSLATION_TEXTURE_COORDS;
      _group = GROUP_COLOR;
    }
    
    if (_name.compare("uScaleTexCoord") == 0){
      _key = SCALE_TEXTURE_COORDS;
      _group = GROUP_COLOR;
    }
    
    if (_name.compare("uPointSize") == 0){
      _key = POINT_SIZE;
    }
    
    if (_key == UNRECOGNIZED){
      ILogger::instance()->logError("Unrecognized GPU uniform %s\n", _name.c_str());
    }
  }
  
  if (_variableType == ATTRIBUTE){
    if (_name.compare("aPosition") == 0){
      _key = FLAT_COLOR;
    }
    
    if (_name.compare("aColor") == 0){
      _key = MODELVIEW;
      _group = GROUP_COLOR;
    }
    
    if (_name.compare("aTextureCoord") == 0){
      _key = TEXTURE_EXTENT;
      _group = GROUP_COLOR;
    }
    
    if (_key == UNRECOGNIZED){
      ILogger::instance()->logError("Unrecognized GPU attribute %s\n", _name.c_str());
    }
  }
  
  
}