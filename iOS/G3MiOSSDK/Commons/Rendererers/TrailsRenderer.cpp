//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

#include "Mesh.hpp"
#include "DirectMesh.hpp"
#include "Planet.hpp"
#include "GLConstants.hpp"
#include "GLState.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "Camera.hpp"

Trail::~Trail() {
  delete _mesh;
}

Mesh* Trail::createMesh(const Planet* planet) {
  const int positionsSize = _positions.size();

  if (positionsSize < 2) {
    return NULL;
  }


  std::vector<double> anglesInDegrees = std::vector<double>();
  for (int i = 1; i < positionsSize; i++) {
    const Geodetic3D* current  = _positions[i];
    const Geodetic3D* previous = _positions[i - 1];

    const Angle angle = Geodetic2D::bearing(previous->latitude(), previous->longitude(),
                                            current->latitude(), current->longitude());

    const double angleInDegrees = angle.degrees();
    if (i == 1) {
      anglesInDegrees.push_back(angleInDegrees);
      anglesInDegrees.push_back(angleInDegrees);
    }
    else {
      anglesInDegrees.push_back(angleInDegrees);
      const double avr = (angleInDegrees + anglesInDegrees[i - 1]) / 2.0;
      anglesInDegrees[i - 1] = avr;
    }
  }


  float centerX = 0;
  float centerY = 0;
  float centerZ = 0;
  const Vector3D offsetP(_ribbonWidth/2, 0, 0);
  const Vector3D offsetN(-_ribbonWidth/2, 0, 0);

  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(positionsSize * 3 * 2);

  const Vector3D rotationAxis = Vector3D::downZ();
  for (int i = 0; i < positionsSize; i++) {
    const Geodetic3D* position = _positions[i];

    const MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(anglesInDegrees[i]),
                                                                                   rotationAxis);
    const MutableMatrix44D geoMatrix = planet->createGeodeticTransformMatrix(*position);
    const MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);

    const int i6 = i * 6;
    const Vector3D offsetNTransformed = offsetN.transformedBy(matrix, 1);
    if (i == 0) {
      centerX = (float) offsetNTransformed._x;
      centerY = (float) offsetNTransformed._y;
      centerZ = (float) offsetNTransformed._z;
    }
    vertices->rawPut(i6    , (float) offsetNTransformed._x - centerX);
    vertices->rawPut(i6 + 1, (float) offsetNTransformed._y - centerY);
    vertices->rawPut(i6 + 2, (float) offsetNTransformed._z - centerZ);


    const Vector3D offsetPTransformed = offsetP.transformedBy(matrix, 1);
    vertices->rawPut(i6 + 3, (float) offsetPTransformed._x - centerX);
    vertices->rawPut(i6 + 4, (float) offsetPTransformed._y - centerY);
    vertices->rawPut(i6 + 5, (float) offsetPTransformed._z - centerZ);
  }

  const Vector3D center(centerX, centerY, centerZ);
  Mesh* surfaceMesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                     true,
                                     center,
                                     vertices,
                                     1,
                                     1,
                                     new Color(_color));

  // Debug unions
  //  Mesh* edgesMesh = new DirectMesh(GLPrimitive::lines(),
  //                                   false,
  //                                   center,
  //                                   vertices,
  //                                   2,
  //                                   1,
  //                                   Color::newFromRGBA(1, 1, 1, 0.7f));
  //
  //  CompositeMesh* cm = new CompositeMesh();
  //
  //  cm->addMesh(surfaceMesh);
  //  cm->addMesh(edgesMesh);
  //
  //  return cm;

  return surfaceMesh;


  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
  //                                          planet,
  //                                          Geodetic3D::fromDegrees(0, 0, 0));
  //
  //  const int positionsSize = _positions.size();
  //  for (int i = 0; i < positionsSize; i++) {
  //#ifdef C_CODE
  //    vertices.add( *(_positions[i]) );
  //#endif
  //#ifdef JAVA_CODE
  //	  vertices.add( _positions.get(i) );
  //#endif
  //  }
  //
  //  return new DirectMesh(GLPrimitive::lineStrip(),
  //                        true,
  //                        vertices.getCenter(),
  //                        vertices.create(),
  //                        _lineWidth,
  //                        1,
  //                        new Color(_color));
}

Mesh* Trail::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;
    _mesh = createMesh(planet);
    _positionsDirty = false;
  }
  return _mesh;
}

void Trail::render(const G3MRenderContext* rc,
                   const GLState& parentState, const GPUProgramState* parentProgramState) {
  if (_visible) {
    Mesh* mesh = getMesh(rc->getPlanet());
    if (mesh != NULL) {
      mesh->render(rc, parentState, parentProgramState);
    }
  }
}

TrailsRenderer::~TrailsRenderer() {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    delete trail;
  }
  _trails.clear();
}

void TrailsRenderer::addTrail(Trail* trail) {
  _trails.push_back(trail);
}

void TrailsRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  
  rc->getCurrentCamera()->applyOnGPUProgramState(_programState);// Setting projection and modelview
  
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    trail->render(rc, parentState, &_programState);
  }
}

void TrailsRenderer::initialize(const G3MContext* context) {
  _programState.setUniformValue("BillBoard", false);
  _programState.setUniformValue("EnableTexture", false);
  _programState.setUniformValue("ScaleTexCoord", Vector2D(1.0, 1.0));
  _programState.setUniformValue("TextureExtent", Vector2D(0.0, 0.0));
  _programState.setUniformValue("TranslationTexCoord", Vector2D(0.0, 0.0));
  _programState.setUniformValue("ViewPortExtent", Vector2D(0.0, 0.0));
}
