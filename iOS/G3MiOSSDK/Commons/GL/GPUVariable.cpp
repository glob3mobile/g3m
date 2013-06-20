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

const int GPUVariable::POSITION = 8;
const int GPUVariable::TEXTURE_COORDS = 9;
const int GPUVariable::COLOR = 10;

//TODO: DELETE
const int GPUVariable::EnableColorPerVertex = 11;
const int GPUVariable::EnableTexture = 12;
const int GPUVariable::EnableFlatColor = 13;
const int GPUVariable::FlatColorIntensity = 14;
const int GPUVariable::ColorPerVertexIntensity = 15;

const int GPUVariable::GROUP_NOGROUP = -1;
const int GPUVariable::GROUP_COLOR = 1;

int GPUVariable::getKeyForName(const std::string& name, int variableType){
  
  if (variableType == UNIFORM){
    if (name.compare("uFlatColor") == 0){
      return FLAT_COLOR;
    }
    
    if (name.compare("uModelview") == 0){
      return MODELVIEW;
    }
    
    if (name.compare("uTextureExtent") == 0){
      return TEXTURE_EXTENT;
    }
    
    if (name.compare("uViewPortExtent") == 0){
      return  VIEWPORT_EXTENT;
    }
    
    if (name.compare("uTranslationTexCoord") == 0){
      return  TRANSLATION_TEXTURE_COORDS;
    }
    
    if (name.compare("uScaleTexCoord") == 0){
      return  SCALE_TEXTURE_COORDS;
    }
    
    
    if (true){ //DELETE
      if (name.compare("EnableColorPerVertex") == 0){
        return  EnableColorPerVertex;
      }
      
      if (name.compare("EnableTexture") == 0){
        return  EnableTexture;
      }
      
      if (name.compare("EnableFlatColor") == 0){
        return  EnableFlatColor;
      }
      
      if (name.compare("FlatColorIntensity") == 0){
        return  FlatColorIntensity;
      }
      
      if (name.compare("ColorPerVertexIntensity") == 0){
        return  ColorPerVertexIntensity;
      }
      
      if (name.compare("uPointSize") == 0){
        return  POINT_SIZE;
      }
    }
  }
  
  if (variableType == ATTRIBUTE){
    if (name.compare("aPosition") == 0){
      return POSITION;
    }
    
    if (name.compare("aColor") == 0){
      return  COLOR;
    }
    
    if (name.compare("aTextureCoord") == 0){
      return  TEXTURE_COORDS;
    }
  }
  
  return UNRECOGNIZED;
}

void GPUVariable::createMetadata(){
  _group = GROUP_NOGROUP;
  _priority = -1;
  _key = getKeyForName(_name, _variableType);
  
  if (_key == UNRECOGNIZED){
    ILogger::instance()->logError("Unrecognized GPU VARAIBLE %s\n", _name.c_str());
  }
  
  if (_variableType == UNIFORM){
    if (_key == FLAT_COLOR){
      _group = GROUP_COLOR;
    }
    
    if (_key == TEXTURE_EXTENT){
      _group = GROUP_COLOR;
    }
    
    if (_key == TRANSLATION_TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }
    
    if (_key == TRANSLATION_TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }

    if (true){ //DELETE
      if (_key == EnableColorPerVertex){
        _group = GROUP_COLOR;
      }
      
      if (_key == EnableTexture){
        _group = GROUP_COLOR;
      }
      
      if ( _key == EnableFlatColor){
        _group = GROUP_COLOR;
      }
      
      if (_key == FlatColorIntensity){
        _group = GROUP_COLOR;
      }
      
      if (_key == ColorPerVertexIntensity){
        _group = GROUP_COLOR;
      }
    }
  }
  
  if (_variableType == ATTRIBUTE){

    if (_key == COLOR){
      _group = GROUP_COLOR;
    }
    
    if (_key == TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }
  }
  
  
}