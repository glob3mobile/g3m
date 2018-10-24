//
//  GEO3DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

#ifndef GEO3DPointGeometry_hpp
#define GEO3DPointGeometry_hpp

#include "GEO3DGeometry.hpp"

#include "Geodetic3D.hpp"


class GEO3DPointGeometry : public GEO3DGeometry {
private:
  const Geodetic3D _position;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

  const Sector* calculateSector() const;

public:

  GEO3DPointGeometry(const Geodetic3D& position) :
  _position(position)
  {
  }

  const Geodetic3D getPosition() const {
    return _position;
  }

  long long getCoordinatesCount() const {
    return 1;
  }

  GEO3DPointGeometry* deepCopy() const;

  long long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                               const VectorStreamingRenderer::Node*      node) const;

};

#endif
