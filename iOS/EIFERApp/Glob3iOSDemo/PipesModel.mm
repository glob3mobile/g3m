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

void PipesModel::addMeshes(const Planet* p, MeshRenderer* mr, const ElevationData* ed){
  
  NSString* s = [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"pipesCoords" ofType:@"csv"] encoding:NSASCIIStringEncoding error:nil ];
  
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
      
      Geodetic3D g = Geodetic3D::fromDegrees(lat, lon, h + ed->getElevationAt(Geodetic2D::fromDegrees(lat, lon)));
      Geodetic3D g2 = Geodetic3D::fromDegrees(lat2, lon2, h2 + + ed->getElevationAt(Geodetic2D::fromDegrees(lat2, lon2)));

      Cylinder c(p->toCartesian(g), p->toCartesian(g2), 0.5);
      
      mr->addMesh(c.createMesh(Color::red(), 15));
      
      //Cylinder c2(p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height-0.5)), p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height+0.5)), 0.5);
      
      mr->addMesh(c.createMesh(Color::red(), 10));
      //mr->addMesh(c2.createMesh(Color::blue(), 15));
      
    }
    
  }
}
