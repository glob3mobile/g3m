//
//  PipesModel.hpp
//  EIFER App
//
//  Created by Jose Miguel SN on 23/09/2017.
//
//

#ifndef PipesModel_hpp
#define PipesModel_hpp



#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/Cylinder.hpp>
#import <G3MiOSSDK/Planet.hpp>
#import <G3MiOSSDK/ElevationData.hpp>

class PipesModel{
public:
  static double coord;
  
  static void addMeshes(const Planet* p, MeshRenderer* mr, const ElevationData* ed);
  
};




#endif /* PipesModel_hpp */
