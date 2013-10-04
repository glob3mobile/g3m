//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SubviewElevationData.hpp"

#include "IStringBuilder.hpp"
#include "IFloatBuffer.hpp"
#include "IFactory.hpp"
#include "Vector3D.hpp"
#include "Vector2I.hpp"

SubviewElevationData::SubviewElevationData(const ElevationData* elevationData,
                                           const Sector& sector,
                                           const Vector2I& extent) :
ElevationData(sector, extent),
_buffer( new float[_width * _height] )
{
  if ((elevationData == NULL) ||
      (elevationData->getExtentWidth() < 1) ||
      (elevationData->getExtentHeight() < 1)) {
    ILogger::instance()->logError("SubviewElevationData can't subview given elevation data.");
    _buffer = NULL;
    return;
  }

  _hasNoData = false;
}

SubviewElevationData::~SubviewElevationData() {
  delete [] _buffer;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

double SubviewElevationData::getElevationAt(int x, int y) const {

  const int index = ((_height-1-y) * _width) + x;

//  if ( (index < 0) || (index >= _buffer->size()) ) {
//    printf("break point on me\n");
//    return IMathUtils::instance()->NanD();
//  }

  return _buffer[index];
}

const std::string SubviewElevationData::description(bool detailed) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(SubviewElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row) );
        isb->addString(",");
      }
      isb->addString("\n");
    }
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;

}

Vector3D SubviewElevationData::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();

  double minHeight = mu->maxDouble();
  double maxHeight = mu->minDouble();
  double sumHeight = 0.0;

  for (int x = 0; x < _width; x++) {
    for (int y = 0; y < _height; y++) {
      const double height = getElevationAt(x, y);
      if ( !ISNAN(height) ) {
        if (height < minHeight) {
          minHeight = height;
        }
        if (height > maxHeight) {
          maxHeight = height;
        }
        sumHeight += height;
      }
    }
  }

  if (minHeight == mu->maxDouble()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minDouble()) {
    maxHeight = 0;
  }

  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_width * _height));
}
