//
//  PipesModel.cpp
//  EIFER App
//
//  Created by Jose Miguel SN on 23/09/2017.
//
//

#include <stdio.h>

#include "PipesModel.hpp"

#import <UIKit/UIKit.h>

std::vector<Cylinder::CylinderMeshInfo> PipesModel::addMeshes(const std::string& fileName, const Planet* p, MeshRenderer* mr, const ElevationData* ed, double heightOffset){
    
  //Línea añadida por mí
  std::vector<Cylinder::CylinderMeshInfo> cylinderInfo;
  
  NSString* s = [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithUTF8String:fileName.c_str()] ofType:@"csv"] encoding:NSASCIIStringEncoding error:nil ];
  
  int nPipes = 0;
  
  NSArray* lines = [s componentsSeparatedByString:@"\n"];
  for (size_t i = 0; i < lines.count; i++){
    NSArray* numbers = [lines[i] componentsSeparatedByString:@" "];
    
    if (numbers.count == 6){
      
      double lon = ((NSString*)numbers[0]).doubleValue;
      double lat = ((NSString*)numbers[1]).doubleValue;
      double h = ((NSString*)numbers[2]).doubleValue;
      
      double lon2 = ((NSString*)numbers[3]).doubleValue;
      double lat2 = ((NSString*)numbers[4]).doubleValue;
      double h2 = ((NSString*)numbers[5]).doubleValue;
      
      double o1 = ed == NULL? 0.0 : ed->getElevationAt(Geodetic2D::fromDegrees(lat, lon));
      double o2 = ed == NULL? 0.0 : ed->getElevationAt(Geodetic2D::fromDegrees(lat2, lon2));
      
      Geodetic3D g = Geodetic3D::fromDegrees(lat, lon, h + o1 + heightOffset);
      Geodetic3D g2 = Geodetic3D::fromDegrees(lat2, lon2, h2 + o2 + heightOffset);

      //Tubes
      Cylinder c(p->toCartesian(g), p->toCartesian(g2), 0.5);
      
        
      mr->addMesh(c.createMesh(Color::fromRGBA255(255, 0, 0, 32), 5, p));
//      mr->addMesh(c.createMesh(Color::fromRGBA255(255, 0, 0, 255), 5, p));
      cylinderInfo.push_back(Cylinder::CylinderMeshInfo(c._info));
      
      nPipes++;
      
      //Junctions
//      Cylinder c2(p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height-0.5)),
//                  p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height+0.7)), 0.7);
//      mr->addMesh(c2.createMesh(Color::blue(), 5));
//      
//      Cylinder c3(p->toCartesian(Geodetic3D(g2._latitude, g2._longitude, g2._height-0.5)),
//                  p->toCartesian(Geodetic3D(g2._latitude, g2._longitude, g2._height+0.7)), 0.7);
//      mr->addMesh(c3.createMesh(Color::blue(), 5));
      
    }
    
  }
  
  printf("%d pipes created.\n", nPipes);
  return cylinderInfo;
}
