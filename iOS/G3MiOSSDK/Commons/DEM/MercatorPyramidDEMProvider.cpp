//
//  MercatorPyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "MercatorPyramidDEMProvider.hpp"


#include "Sector.hpp"
#include "ErrorHandling.hpp"
#include "MercatorUtils.hpp"
#include "PyramidNode.hpp"


MercatorPyramidDEMProvider::MercatorPyramidDEMProvider(const double deltaHeight,
                                                       const Vector2S& tileExtent) :
PyramidDEMProvider(deltaHeight, 1, tileExtent)
{
}

MercatorPyramidDEMProvider::~MercatorPyramidDEMProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}


PyramidNode* MercatorPyramidDEMProvider::createNode(const PyramidNode* parent,
                                                    const size_t childID) {
  if (parent == NULL) {
    // creating root node
    return new PyramidNode(NULL,       // parent
                           childID,
                           Sector::FULL_SPHERE,
                           0, 0, 0,    // z, x, y
                           this);
  }

  const int nextZ = parent->_z + 1;
  const int x2    = parent->_x * 2;
  const int y2    = parent->_y * 2;

  const Geodetic2D lower = parent->_sector._lower;
  const Geodetic2D upper = parent->_sector._upper;
  const Angle splitLongitude = Angle::midAngle(lower._longitude, upper._longitude);
  const Angle splitLatitude  = MercatorUtils::calculateSplitLatitude(lower._latitude, upper._latitude);

  if (childID == 0) {
    const Sector sector(Geodetic2D(lower._latitude, lower._longitude),
                        Geodetic2D(  splitLatitude,   splitLongitude));
    return new PyramidNode(parent, childID, sector,
                           nextZ, x2, y2 + 1,
                           this);
  }
  else if (childID == 1) {
    const Sector sector(Geodetic2D(lower._latitude,   splitLongitude),
                        Geodetic2D(  splitLatitude, upper._longitude));
    return new PyramidNode(parent, childID, sector,
                           nextZ, x2 + 1, y2 + 1,
                           this);
  }
  else if (childID == 2) {
    const Sector sector(Geodetic2D(  splitLatitude, lower._longitude),
                        Geodetic2D(upper._latitude,   splitLongitude));
    return new PyramidNode(parent, childID, sector,
                           nextZ, x2, y2,
                           this);
  }
  else if (childID == 3) {
    const Sector sector(Geodetic2D(  splitLatitude,   splitLongitude),
                        Geodetic2D(upper._latitude, upper._longitude));
    return new PyramidNode(parent, childID, sector,
                           nextZ, x2 + 1, y2,
                           this);
  }
  else {
    THROW_EXCEPTION("Man, isn't it a QuadTree?");
  }
}
