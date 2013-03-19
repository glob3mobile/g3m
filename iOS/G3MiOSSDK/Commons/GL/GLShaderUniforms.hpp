//
//  GLShaderUniforms.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/03/13.
//
//

#ifndef G3MiOSSDK_GLShaderUniforms_h
#define G3MiOSSDK_GLShaderUniforms_h

#include "IGLUniformID.hpp"

class UniformsStruct {
public:
  
  IGLUniformID* Projection;
  IGLUniformID* Modelview;
  IGLUniformID* Sampler;
  IGLUniformID* EnableTexture;
  IGLUniformID* FlatColor;
  IGLUniformID* TranslationTexCoord;
  IGLUniformID* ScaleTexCoord;
  IGLUniformID* PointSize;
  
  //FOR BILLBOARDING
  IGLUniformID* BillBoard;
  IGLUniformID* ViewPortExtent;
  IGLUniformID* TextureExtent;
  
  
  //FOR COLOR MIXING
  IGLUniformID* FlatColorIntensity;
  IGLUniformID* EnableColorPerVertex;
  IGLUniformID* EnableFlatColor;
  IGLUniformID* ColorPerVertexIntensity;
  
  UniformsStruct() {
    Projection = NULL;
    Modelview = NULL;
    Sampler = NULL;
    EnableTexture = NULL;
    FlatColor = NULL;
    TranslationTexCoord = NULL;
    ScaleTexCoord = NULL;
    PointSize = NULL;
    
    //FOR BILLBOARDING
    BillBoard = NULL;
    ViewPortExtent = NULL;
    TextureExtent = NULL;
    
    //FOR COLOR MIXING
    FlatColorIntensity = NULL;
    EnableColorPerVertex = NULL;
    EnableFlatColor = NULL;
    ColorPerVertexIntensity = NULL;
  }
  
  void deleteUniformsIDs(){
    delete Projection;
    delete Modelview;
    delete Sampler;
    delete EnableTexture;
    delete FlatColor;
    delete TranslationTexCoord;
    delete ScaleTexCoord;
    delete PointSize;
    
    //FOR BILLBOARDING
    delete BillBoard;
    delete ViewPortExtent;
    delete TextureExtent;
    
    //FOR COLOR MIXING
    delete FlatColorIntensity;
    delete EnableColorPerVertex;
    delete EnableFlatColor;
    delete ColorPerVertexIntensity;
  }
  
  ~UniformsStruct(){
    deleteUniformsIDs();
  }
};


#endif
