//
//  SurfaceElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

#ifndef __G3MiOSSDK__SurfaceElevationProvider__
#define __G3MiOSSDK__SurfaceElevationProvider__

#include "GenericQuadTree.hpp"

#include "ElevationData.hpp"

class Angle;
class Geodetic2D;



class SurfaceElevationListener {
public:

#ifdef C_CODE
  virtual ~SurfaceElevationListener() {}
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void elevationChanged(const Geodetic2D& position,
                                double rawElevation,            //Without considering vertical exaggeration
                                double verticalExaggeration) = 0;
  
  virtual void elevationChanged(const Sector& position,
                                const ElevationData* rawElevationData, //Without considering vertical exaggeration
                                double verticalExaggeration) = 0;
};


class SurfaceElevationProvider_Visitor: public GenericQuadTreeVisitor{
  const ElevationData* _elevationData;
  const double _verticalExaggeration;

public:
  SurfaceElevationProvider_Visitor(const ElevationData* ed,
                                   double verticalExaggeration);

  bool visitElement(const Sector& sector,
                    const void*   element) const;

  bool visitElement(const Geodetic2D& geodetic,
                    const void*   element) const;

  void endVisit(bool aborted) const{}
};

//Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
class SurfaceElevationProvider_Tree: public GenericQuadTree{
public:
  void notifyListeners(const ElevationData* ed, double verticalExaggeration) const;
};

class SurfaceElevationProvider {

private:
  


protected:


public:
#ifdef C_CODE
  virtual ~SurfaceElevationProvider() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void addListener(const Angle& latitude,
                           const Angle& longitude,
                           SurfaceElevationListener* listener) = 0;

  virtual void addListener(const Geodetic2D& position,
                           SurfaceElevationListener* listener) = 0;

  virtual bool removeListener(SurfaceElevationListener* listener) = 0;
  
};

#endif
