//
//  GEOMeshes.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/6/18.
//

#ifndef GEOMeshes_hpp
#define GEOMeshes_hpp

#include "GEOObject.hpp"

#include <vector>
class Mesh;


class GEOMeshes : public GEOObject {
private:
  mutable std::vector<Mesh*> _meshes;

public:
  GEOMeshes(const std::vector<Mesh*>& meshes);

  ~GEOMeshes();

  void rasterize(const GEORasterSymbolizer* symbolizer,
                 ICanvas* canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOVectorLayer*         geoVectorLayer) const;

  int symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                const VectorStreamingRenderer::Node*      node) const;

  GEOMeshes* deepCopy() const;

  long long getCoordinatesCount() const {
    return 0;
  }

};

#endif
