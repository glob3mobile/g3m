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

class GEOGeometry : public GEOObject {
private:
  Mesh* _mesh;

protected:
  virtual Mesh* getMesh(const G3MRenderContext* rc);

  virtual Mesh* createMesh(const G3MRenderContext* rc) = 0;

  Mesh* create2DBoundaryMesh(std::vector<Geodetic2D*>* coordinates,
                             Color* color,
                             float lineWidth,
                             const G3MRenderContext* rc);

public:
  GEOGeometry() :
  _mesh(NULL)
  {

  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState);

  ~GEOGeometry();

};

#endif
