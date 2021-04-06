//
//  MeasureHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//

#ifndef MeasureHandler_hpp
#define MeasureHandler_hpp

#include <string>

class Measure;
class Geodetic3D;
class Vector3D;
class Angle;


class MeasureHandler {
public:

  virtual ~MeasureHandler() {

  }

  virtual void onVertexDeselection(Measure* measure) = 0;

  virtual void onVertexSelection(Measure* measure,
                                 const Geodetic3D& geodetic,
                                 const Vector3D& cartesian,
                                 int selectedIndex) = 0;

  virtual std::string getAngleLabel(Measure* measure,
                                    size_t vertexIndex,
                                    const Angle& angle) = 0;

  virtual std::string getDistanceLabel(Measure* measure,
                                       size_t vertexIndexFrom,
                                       size_t vertexIndexTo,
                                       const double distanceInMeters) = 0;

};

#endif
