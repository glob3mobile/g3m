//
//  AbstractMeshShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#ifndef __G3MiOSSDK__AbstractMeshShape__
#define __G3MiOSSDK__AbstractMeshShape__

#include "Shape.hpp"
#include "Sphere.hpp"

class Mesh;

class AbstractMeshShape : public Shape {
private:
  Mesh* _mesh;
  Sphere* boundingSphere = NULL;

  BoundingVolume* getBoundingSphere(const G3MRenderContext* rc);

protected:
  virtual Mesh* createMesh(const G3MRenderContext* rc) = 0;
  bool touchesFrustum(const G3MRenderContext* rc);

  Mesh* getMesh(const G3MRenderContext* rc);

  void cleanMesh();

  AbstractMeshShape(Geodetic3D* position, AltitudeMode altitudeMode) :
  Shape(position, altitudeMode),
  _mesh(NULL) {

  }

  AbstractMeshShape(Geodetic3D* position, AltitudeMode altitudeMode,
                    Mesh* mesh) :
  Shape(position, altitudeMode),
  _mesh(mesh) {

  }

public:

  bool isReadyToRender(const G3MRenderContext* rc);

  void rawRender(const G3MRenderContext* rc,
                 GLState* parentState,
                 bool renderNotReadyShapes);

  virtual ~AbstractMeshShape();

  bool isTransparent(const G3MRenderContext* rc);
};

#endif
