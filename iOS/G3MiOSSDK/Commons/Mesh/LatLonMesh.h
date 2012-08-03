//
//  LatLonMesh.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_LatLonMesh_h
#define G3MiOSSDK_LatLonMesh_h

#include "IndexedMesh.hpp"


class LatLonMesh: public Mesh {
  
private:
  IndexedMesh *mesh;
  
public:
  LatLonMesh(const InitializationContext *ic,
             bool owner,
             const GLPrimitive primitive,
             CenterStrategy strategy,
             Vector3D center,
             const int numVertices,
             float* vertices,
             const int* indexes,
             const int numIndex, 
             const Color* flatColor = NULL,
             const float * colors = NULL,
             const float colorsIntensity = 0.0,
             const float* normals = NULL); 
  
  int getVertexCount() const { return mesh->getVertexCount(); }
  
  const Vector3D getVertex(int i) const { return mesh->getVertex(i); }
  
  void render(const RenderContext* rc) const { mesh->render(rc); }
  
  Extent *getExtent() const { return mesh->getExtent(); }
  
  ~LatLonMesh() { delete mesh; }

};


#endif
