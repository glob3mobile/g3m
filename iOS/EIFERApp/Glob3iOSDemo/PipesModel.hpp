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
  static std::vector<Cylinder::CylinderMeshInfo> cylinderInfo;
  static std::vector<Cylinder *> cylinders;
    
  
  static void addMeshes(const std::string& fileName, const Planet* p, MeshRenderer* mr, const ElevationData* ed, double heightOffset = 0.0);
    
  static void reset();
    
  //static void insertNewCylinder(Geodetic3D &start,Geodetic3D &end, const Planet *p, MeshRenderer *mr, const ElevationData *ed, double heightOffset = 0.0);
    
  static void insertNewCylinder(Geodetic3D &start,Geodetic3D &end, const Planet *p, MeshRenderer *mr, const ElevationData *ed, double heightOffset,
                                int theId, std::string iMat, std::string eMat,double iDiam, double eDiam, std::string pClass, std::string pType, bool isT, bool isC);
  
};


#endif /* PipesModel_hpp */
