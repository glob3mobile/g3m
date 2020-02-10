//
//  MeshShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#ifndef __G3M__MeshShape__
#define __G3M__MeshShape__

#include "AbstractMeshShape.hpp"

class MeshShape : public AbstractMeshShape {
protected:
  Mesh* createMesh(const G3MRenderContext* rc) {
    return NULL;
  }

public:
  MeshShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            Mesh* mesh) :
  AbstractMeshShape(position, altitudeMode, mesh)
  {
  }

  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Vector3D& origin,
                                             const Vector3D& direction) const;

};

#endif

