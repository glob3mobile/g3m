//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3M
//
//  Created by José Miguel S N on 06/09/12.
//

#ifndef G3M_FloatBufferBuilderFromGeodetic
#define G3M_FloatBufferBuilderFromGeodetic

#include "FloatBufferBuilder.hpp"
#include "CenterStrategy.hpp"

class Planet;
class Geodetic2D;
class Geodetic3D;
class Angle;


class FloatBufferBuilderFromGeodetic : public FloatBufferBuilder {
private:
  FloatBufferBuilderFromGeodetic(const FloatBufferBuilderFromGeodetic& that);

  const CenterStrategy _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(const Vector3D& center);

  const Planet* _planet;

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Vector3D& center);

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Geodetic2D& center);

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Geodetic3D& center);

public:

  static FloatBufferBuilderFromGeodetic* builderWithoutCenter(const Planet* planet);

  static FloatBufferBuilderFromGeodetic* builderWithFirstVertexAsCenter(const Planet* planet);

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Vector3D& center);

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Geodetic2D& center);

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Geodetic3D& center);

  void add(const Angle& latitude,
           const Angle& longitude,
           const double height);

  void add(const Geodetic3D& position);

  void add(const Geodetic2D& position);

  void add(const Geodetic2D& position,
           const double height);

  Vector3D getCenter();
  
};

#endif
