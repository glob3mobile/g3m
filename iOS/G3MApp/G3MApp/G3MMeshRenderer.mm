//
//  G3MMeshRenderer.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 25/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MMeshRenderer.hpp"

#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/Planet.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromGeodetic.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromColor.hpp>
#import <G3MiOSSDK/DirectMesh.hpp>
#import <G3MiOSSDK/GL.hpp>


MeshRenderer* G3MMeshRenderer::createMeshRenderer(const Planet* planet) {
  MeshRenderer* mr = new MeshRenderer();

  FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);

  FloatBufferBuilderFromColor colors;

  const Angle centerLat = Angle::fromDegreesMinutesSeconds(38, 53, 42);
  const Angle centerLon = Angle::fromDegreesMinutesSeconds(-77, 02, 11);

  const Angle deltaLat = Angle::fromDegrees(1).div(16);
  const Angle deltaLon = Angle::fromDegrees(1).div(16);

  const int steps = 128;
  const int halfSteps = steps/2;
  for (int i = -halfSteps; i < halfSteps; i++) {
    Angle lat = centerLat.add( deltaLat.times(i) );
    for (int j = -halfSteps; j < halfSteps; j++) {
      Angle lon = centerLon.add( deltaLon.times(j) );

      vertices.add( Geodetic3D(lat, lon, 100000) );

      const float red   = (float) (i + halfSteps + 1) / steps;
      const float green = (float) (j + halfSteps + 1) / steps;
      colors.add(Color::fromRGBA(red, green, 0, 1));
    }
  }

  const float lineWidth = 1;
  const float pointSize = 2;
  Color* flatColor = NULL;
  DirectMesh* pointMesh = new DirectMesh(GLPrimitive::points(),
                                         true,
                                         vertices.getCenter(),
                                         vertices.create(),
                                         lineWidth,
                                         pointSize,
                                         flatColor,
                                         colors.create());


  mr->addMesh(pointMesh);

  return mr;
}