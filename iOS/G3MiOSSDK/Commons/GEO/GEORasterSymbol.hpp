//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEORasterSymbol__
#define __G3MiOSSDK__GEORasterSymbol__

#include "GEOSymbol.hpp"
#include <vector>

#include "Sector.hpp"
#include "Vector2F.hpp"
class GEORasterProjection;
class ICanvas;

class GEORasterSymbol : public GEOSymbol {
protected:
  const Sector* _sector;

  static std::vector<Geodetic2D*>* copy(const std::vector<Geodetic2D*>* coordinates);
  static std::vector<std::vector<Geodetic2D*>*>* copy(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

  static Sector* calculateSector(const std::vector<Geodetic2D*>* coordinates);
  static Sector* calculateSector(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

protected:
  GEORasterSymbol(const Sector* sector) :
  _sector(sector)
  {
  }

  void rasterLine(const std::vector<Geodetic2D*>* coordinates,
                  ICanvas*                        canvas,
                  const GEORasterProjection*      projection) const;

public:
  virtual ~GEORasterSymbol();

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizationContext& sc) const;

  const Sector* getSector() const {
    return _sector;
  }

  virtual GEORasterSymbol* createSymbol() const = 0;


  virtual void rasterize(ICanvas*                   canvas,
                         const GEORasterProjection* projection) const = 0;
};

#endif
