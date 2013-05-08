//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEOGeometry.hpp"

#include "Mesh.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "GEOSymbolizer.hpp"
#include "GEOSymbol.hpp"
#include "GEOFeature.hpp"

#include "GPUProgramState.hpp"

GEOGeometry::~GEOGeometry() {
  if (_meshes != NULL) {
    const int meshesCount = _meshes->size();
    for (int i = 0; i < meshesCount; i++) {
      delete _meshes->at(0);
    }
    delete _meshes;
  }
}

std::vector<Mesh*>* GEOGeometry::createMeshes(const G3MRenderContext* rc,
                                              const GEOSymbolizer* symbolizer) {

  std::vector<GEOSymbol*>* symbols = createSymbols(rc, symbolizer);
  if (symbols == NULL) {
    return NULL;
  }

  std::vector<Mesh*>* meshes = new std::vector<Mesh*>();

  const int symbolsSize = symbols->size();
  for (int i = 0; i < symbolsSize; i++) {
    GEOSymbol* symbol = symbols->at(i);

    Mesh* mesh = symbol->createMesh(rc);
    if (mesh != NULL) {
      meshes->push_back(mesh);
    }
    delete symbol;
  }
  delete symbols;

  return meshes;
}


std::vector<Mesh*>* GEOGeometry::getMeshes(const G3MRenderContext* rc,
                                           const GEOSymbolizer* symbolizer) {
  if (_meshes == NULL) {
    _meshes = createMeshes(rc, symbolizer);
  }
  return _meshes;
}

void GEOGeometry::render(const G3MRenderContext* rc,
                         const GLState& parentState, const GPUProgramState* parentProgramState,
                         const GEOSymbolizer* symbolizer) {
  
  
  GPUProgramState progState(parentProgramState);
  progState.setUniformValue("EnableTexture", false);
  progState.setAttributeEnabled("TextureCoord", false);
  progState.setUniformValue("BillBoard", false);
  
  GLState state(parentState);
  state.disableDepthTest();
//  Mesh* mesh = getMesh(rc, symbolizer);

  std::vector<Mesh*>* meshes = getMeshes(rc, symbolizer);

  if (meshes != NULL) {

    const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();

    const int meshesCount = _meshes->size();
    for (int i = 0; i < meshesCount; i++) {
      Mesh* mesh = meshes->at(0);
      if (mesh != NULL) {
        const Extent* extent = mesh->getExtent();

        if ( extent->touches(frustum) ) {
          
          mesh->render(rc, state, &progState);
        }
      }
    }
  }
}

void GEOGeometry::setFeature(GEOFeature* feature) {
  if (_feature != feature) {
    delete _feature;
    _feature = feature;
  }
}
