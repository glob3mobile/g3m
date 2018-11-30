//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian3D
#define G3MiOSSDK_FloatBufferBuilderFromCartesian3D

#include "FloatBufferBuilder.hpp"

#include "CenterStrategy.hpp"


class FloatBufferBuilderFromCartesian3D : public FloatBufferBuilder {

private:
  const CenterStrategy _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(double x, double y, double z);

  FloatBufferBuilderFromCartesian3D(CenterStrategy centerStrategy,
                                    const Vector3D& center);

public:

  static FloatBufferBuilderFromCartesian3D* builderWithoutCenter();

  static FloatBufferBuilderFromCartesian3D* builderWithFirstVertexAsCenter();

  static FloatBufferBuilderFromCartesian3D* builderWithGivenCenter(const Vector3D& center);

  void add(const Vector3D& vector);

  void add(double x, double y, double z);

  void add(float x, float y, float z);

  Vector3D getCenter();
  
};

#endif
