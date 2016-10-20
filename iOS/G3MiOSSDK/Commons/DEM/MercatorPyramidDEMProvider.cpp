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
#include "DEMPyramidNode.hpp"


MercatorPyramidDEMProvider::MercatorPyramidDEMProvider(const double deltaHeight) :
PyramidDEMProvider(deltaHeight, 1)
{
}

MercatorPyramidDEMProvider::~MercatorPyramidDEMProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}


DEMPyramidNode* MercatorPyramidDEMProvider::createNode(const DEMPyramidNode* parent,
                                                       const size_t childID) {
  if (parent == NULL) {
    // creating root node
    return new DEMPyramidNode(NULL, // parent
                              childID,
                              Sector::FULL_SPHERE,
                              0,    // z
                              0,    // x
                              0     // y
                              );
  }

  const int nextZ = parent->_z + 1;
  const int x2    = parent->_x * 2;
  const int y2    = parent->_y * 2;

  const Geodetic2D lower = parent->_sector._lower;
  const Geodetic2D upper = parent->_sector._upper;
  const Angle splitLongitude = Angle::midAngle(lower._longitude, upper._longitude);
  const Angle splitLatitude  = MercatorUtils::calculateSplitLatitude(lower._latitude, upper._latitude);

  if (childID == 0) {
    const Sector s0(Geodetic2D(lower._latitude, lower._longitude),
                    Geodetic2D(  splitLatitude, splitLongitude  ));

    return new DEMPyramidNode(parent,
                              childID,
                              s0,
                              nextZ,
                              x2,
                              y2 + 1);
  }
  else if (childID == 1) {
    const Sector s1(Geodetic2D(lower._latitude, splitLongitude),
                    Geodetic2D(splitLatitude,   upper._longitude));
    return new DEMPyramidNode(parent,
                              childID,
                              s1,
                              nextZ,
                              x2 + 1,
                              y2 + 1);
  }
  else if (childID == 2) {
    const Sector s2(Geodetic2D(splitLatitude,   lower._longitude),
                    Geodetic2D(upper._latitude, splitLongitude));
    return new DEMPyramidNode(parent,
                              childID,
                              s2,
                              nextZ,
                              x2,
                              y2);
  }
  else if (childID == 3) {
    const Sector s3(Geodetic2D(splitLatitude,   splitLongitude),
                    Geodetic2D(upper._latitude, upper._longitude));
    return new DEMPyramidNode(parent,
                              childID,
                              s3,
                              nextZ,
                              x2 + 1,
                              y2);
  }
  else {
    THROW_EXCEPTION("Man, isn't it a QuadTree?");
  }
}
