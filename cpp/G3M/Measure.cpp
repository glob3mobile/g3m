//
//  Measure.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

#include "Measure.hpp"

#include "Geodetic3D.hpp"
#include "ErrorHandling.hpp"

#include "ShapesRenderer.hpp"
#include "MeshRenderer.hpp"
#include "MarksRenderer.hpp"
#include "EllipsoidShape.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Mark.hpp"


Measure::Measure(const double vertexSphereRadius,
                 const Color& vertexColor,
                 const Color& vertexSelectedColor,
                 const float segmentLineWidth,
                 const Color& segmentColor,
                 const Geodetic3D* firstVertex,
                 ShapesRenderer* shapesRenderer,
                 MeshRenderer* meshRenderer,
                 MarksRenderer* marksRenderer,
                 const Planet* planet) :
_vertexSphereRadius(vertexSphereRadius),
_vertexColor(vertexColor),
_vertexSelectedColor(vertexSelectedColor),
_segmentLineWidth(segmentLineWidth),
_segmentColor(segmentColor),
_shapesRenderer(shapesRenderer),
_meshRenderer(meshRenderer),
_marksRenderer(marksRenderer),
_planet(planet)
{
  _vertices.push_back( firstVertex );
}

Measure::~Measure() {
  for (size_t i = 0; i < _vertices.size(); i++) {
    const Geodetic3D* vertex = _vertices[i];
    delete vertex;
  }
}

void Measure::reset() {
  _shapesRenderer->removeAllShapes();
  _meshRenderer->clearMeshes();
  _marksRenderer->removeAllMarks();

  const size_t verticesCount = _vertices.size();

  // create vertices spheres
  for (size_t i = 0; i < verticesCount; i++) {
    const Geodetic3D* geodetic = _vertices[i];

    Shape* vertexSphere = new EllipsoidShape(new Geodetic3D(*geodetic),
                                             AltitudeMode::ABSOLUTE,
                                             Vector3D(_vertexSphereRadius,
                                                      _vertexSphereRadius,
                                                      _vertexSphereRadius),
                                             (short) 16,  // resolution
                                             0,           // float borderWidth
                                             false,       // bool texturedInside
                                             false,       // bool mercator
                                             _vertexColor // const Color& surfaceColor
                                             );


    _shapesRenderer->addShape( vertexSphere );
  }


  if (verticesCount > 1) {
    {
      // create edges lines
      FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

      for (size_t i = 0; i < verticesCount; i++) {
        const Geodetic3D* geodetic = _vertices[i];
        fbb->add(*geodetic);
      }

      Mesh* edgesLines = new DirectMesh(GLPrimitive::lineStrip(),
                                        true,
                                        fbb->getCenter(),
                                        fbb->create(),
                                        _segmentLineWidth,
                                        1.0f,  // float pointSize
                                        new Color(_segmentColor),
                                        NULL,  // const IFloatBuffer* colors
                                        false  // depthTest
                                        );

      _meshRenderer->addMesh(edgesLines);
    }

    {
      // create edges distance labels
      const Geodetic3D* previousGeodetic  = _vertices[0];
      const Vector3D*   previousCartesian = new Vector3D(_planet->toCartesian(*previousGeodetic));
      for (size_t i = 1; i < verticesCount; i++) {
        const Geodetic3D* currentGeodetic  = _vertices[i];
        const Vector3D*   currentCartesian = new Vector3D(_planet->toCartesian(*currentGeodetic));

        Geodetic3D middle = Geodetic3D::linearInterpolation(*previousGeodetic, *currentGeodetic, 0.5);
        Mark* distanceLabel = new Mark( IStringUtils::instance()->toString( (float) previousCartesian->distanceTo(*currentCartesian) ) + "m",
                                       middle,
                                       ABSOLUTE);

        _marksRenderer->addMark(distanceLabel);

//        delete previousGeodetic;
        previousGeodetic = currentGeodetic;

        delete previousCartesian;
        previousCartesian = currentCartesian;
      }

//      delete previousGeodetic;
      delete previousCartesian;

    }

#warning TODO:   vertices angle labels

  }

}

const size_t Measure::getVexticesCount() const {
  return _vertices.size();
}

void Measure::addVertex(const Geodetic3D* vertex) {
  _vertices.push_back( vertex );

  reset();
}

void Measure::setVertex(const size_t i,
                        const Geodetic3D* vertex) {
  const Geodetic3D* current = _vertices[i];
  if (vertex != current) {
    delete current;
    _vertices[i] = vertex;

    reset();
  }
}

bool Measure::removeVertex(const size_t i) {
  if (_vertices.size() == 1) {
    return false;
  }

#ifdef C_CODE
  _vertices.erase(_vertices.begin() + i);
#endif
#ifdef JAVA_CODE
  _vertices.remove(i);
#endif

  reset();

  return true;
}
