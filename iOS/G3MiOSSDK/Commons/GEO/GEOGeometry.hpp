//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEOGeometry__
#define __G3MiOSSDK__GEOGeometry__

#include "GEOObject.hpp"

#include <vector>
class Geodetic2D;
class Mesh;
class Color;
class GEOSymbol;
class GEOFeature;
class GPUProgramState;

class GEOGeometry : public GEOObject {
private:
  std::vector<Mesh*>* _meshes;

  std::vector<Mesh*>* createMeshes(const G3MRenderContext* rc,
                                   const GEOSymbolizer* symbolizer);

  GEOFeature* _feature;

protected:
  virtual std::vector<Mesh*>* getMeshes(const G3MRenderContext* rc,
                                        const GEOSymbolizer* symbolizer);

  virtual std::vector<GEOSymbol*>* createSymbols(const G3MRenderContext* rc,
                                                 const GEOSymbolizer* symbolizer) = 0;

//  Mesh* create2DBoundaryMesh(std::vector<Geodetic2D*>* coordinates,
//                             Color* color,
//                             float lineWidth,
//                             const G3MRenderContext* rc);

public:
  GEOGeometry() :
  _meshes(NULL),
  _feature(NULL)
  {

  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState, const GPUProgramState* parentProgramState,
              const GEOSymbolizer* symbolizer);

  ~GEOGeometry();

  void setFeature(GEOFeature* feature);

  const GEOFeature* getFeature() const {
    return _feature;
  }

};

#endif
