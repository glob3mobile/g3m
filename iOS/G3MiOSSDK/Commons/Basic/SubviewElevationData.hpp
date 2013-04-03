//
//  SubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__SubviewElevationData__
#define __G3MiOSSDK__SubviewElevationData__

#include "ElevationData.hpp"
class IFloatBuffer;

class SubviewElevationData : public ElevationData {
private:
  const ElevationData* _elevationData;
  const bool           _ownsElevationData;
  const IFloatBuffer*  _buffer;

  IFloatBuffer* createDecimatedBuffer() const;
  IFloatBuffer* createInterpolatedBuffer() const;

  double getElevationBoxAt(double x0, double y0,
                           double x1, double y1) const;

  const Vector2D getParentXYAt(const Geodetic2D& position) const;

public:
  SubviewElevationData(const ElevationData *elevationData,
                       bool ownsElevationData,
                       const Sector& sector,
                       const Vector2I& resolution,
                       double noDataValue,
                       bool useDecimation);

  ~SubviewElevationData();

  double getElevationAt(int x, int y,
                        int *type) const;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude,
                        int *type) const;

  const std::string description(bool detailed) const;

  Vector3D getMinMaxAverageHeights() const;

};

#endif
