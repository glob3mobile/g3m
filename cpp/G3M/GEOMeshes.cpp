//
//  GEOMeshes.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/6/18.
//

#include "GEOMeshes.hpp"

#include "Mesh.hpp"


GEOMeshes::GEOMeshes(const std::vector<Mesh*>& meshes) :
_meshes(meshes)
{

}

GEOMeshes::~GEOMeshes() {
  for (size_t i = 0; i < _meshes.size(); i++) {
    delete _meshes[i];
  }
}

void GEOMeshes::rasterize(const GEORasterSymbolizer* symbolizer,
                          ICanvas* canvas,
                          const GEORasterProjection* projection,
                          int tileLevel) const {
// do nothing
}

void GEOMeshes::symbolize(const G3MRenderContext* rc,
                          const GEOSymbolizer*    symbolizer,
                          MeshRenderer*           meshRenderer,
                          ShapesRenderer*         shapesRenderer,
                          MarksRenderer*          marksRenderer,
                          GEOVectorLayer*         geoVectorLayer) const {
// do nothing
}

int GEOMeshes::symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                         const VectorStreamingRenderer::Node*      node) const {
  const int result = vectorSet->symbolizeMeshes(node, _meshes);
  _meshes.clear(); // moved meshes ownership to vectorSet
  return result;
}
