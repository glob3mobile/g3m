//
//  DirectMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "DirectMesh.hpp"
#include "GL.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "CompositeMesh.hpp"
#include "Sphere.hpp"

DirectMesh::DirectMesh(const int primitive,
                       bool owner,
                       const Vector3D& center,
                       IFloatBuffer* vertices,
                       float lineWidth,
                       float pointSize,
                       Color* flatColor,
                       IFloatBuffer* colors,
                       const float colorsIntensity,
                       bool depthTest,
                       IFloatBuffer* normals) :
AbstractMesh(primitive,
             owner,
             center,
             vertices,
             lineWidth,
             pointSize,
             flatColor,
             colors,
             colorsIntensity,
             depthTest,
             normals)
{
}

void DirectMesh::rawRender(const G3MRenderContext* rc) const{
  GL* gl = rc->getGL();
  
  const int verticesCount = getVertexCount();
  gl->drawArrays(_primitive, 0, verticesCount, _glState, *rc->getGPUProgramManager());
}

Mesh* DirectMesh::createNormalsMesh() const{

  DirectMesh* verticesMesh = new DirectMesh(GLPrimitive::points(),
                                            false,
                                            _center,
                                            _vertices,
                                            1.0,
                                            3.0,
                                            new Color(Color::red()));

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  BoundingVolume* volume = getBoundingVolume();
  Sphere* sphere = volume->createSphere();
  double normalsSize = sphere->getRadius() / 100.0;
  delete sphere;

  const int size = _vertices->size();
  for (int i = 0; i < size; i+=3) {

    Vector3D v(_vertices->get(i), _vertices->get(i+1), _vertices->get(i+2));
    Vector3D n(_normals->get(i), _normals->get(i+1), _normals->get(i+2));

    Vector3D v_n = v.add(n.normalized().times(normalsSize));

    fbb->add(v);
    fbb->add(v_n);
  }

  DirectMesh* normalsMesh = new DirectMesh(GLPrimitive::lines(),
                                           true,
                                           _center,
                                           fbb->create(),
                                           2.0,
                                           1.0,
                                           new Color(Color::blue()));

  delete fbb;

  CompositeMesh* compositeMesh = new CompositeMesh();
  compositeMesh->addMesh(verticesMesh);
  compositeMesh->addMesh(normalsMesh);

  return compositeMesh;

}

