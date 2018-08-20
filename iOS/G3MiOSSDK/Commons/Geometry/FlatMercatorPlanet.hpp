//
//  FlatMercatorPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#ifndef G3MiOSSDK_FlatMercatorPlanet
#define G3MiOSSDK_FlatMercatorPlanet

#include "MutableVector3D.hpp"
#include "Geodetic3D.hpp"
#include "Planet.hpp"
#include "Vector2D.hpp"
#include "Sector.hpp"


class FlatMercatorPlanet: public Planet {
private:
    const Vector2D _size;
    const Vector3D _radii;
    
    mutable MutableVector3D _origin;
    mutable MutableVector3D _initialPoint;
    mutable MutableVector3D _lastFinalPoint;
    mutable bool            _validSingleDrag;
    mutable MutableVector3D _lastDirection;
    
    mutable MutableVector3D _centerRay;
    mutable MutableVector3D _initialPoint0;
    mutable MutableVector3D _initialPoint1;
    mutable double          _distanceBetweenInitialPoints;
    mutable MutableVector3D _centerPoint;
    
    
    //https://wiki.openstreetmap.org/wiki/Mercator
#define M_PI        3.14159265358979323846264338327950288
#define DEG2RAD(a)   ((a) / (180 / M_PI))
#define RAD2DEG(a)   ((a) * (180 / M_PI))
#define EARTH_RADIUS 6378137
    
    /* The following functions take their parameter and return their result in degrees */
    
    static double y2lat_m(double y)   {
        return RAD2DEG(2 * IMathUtils::instance()->atan(IMathUtils::instance()->exp( y/EARTH_RADIUS)) - M_PI/2);
    }
    
    static double x2lon_m(double x)   {
        return RAD2DEG(x/EARTH_RADIUS);
    }
    
    static double lat2y_m(double lat) {
        return IMathUtils::instance()->log(IMathUtils::instance()->tan( DEG2RAD(lat) / 2 + M_PI/4 )) * EARTH_RADIUS;
    }
    
    static double lon2x_m(double lon) {
        return DEG2RAD(lon) * EARTH_RADIUS;
    }
    
    static Vector2D getSize(){
        return Vector2D(lat2y_m(85.0) - lat2y_m(-85.0),
                        lon2x_m(180.0) - lon2x_m(-180.0));
    }
    
public:
    
    static const Planet* createEarth();
    
    FlatMercatorPlanet();
    
    ~FlatMercatorPlanet() {
#ifdef JAVA_CODE
        super.dispose();
#endif
    }
    
    Vector3D getRadii() const {
        return _radii;
    }
    
    Vector3D centricSurfaceNormal(const Vector3D& position) const {
        return Vector3D(0, 0, 1);
    }
    
    Vector3D geodeticSurfaceNormal(const Vector3D& position) const {
        return Vector3D(0, 0, 1);
    }
    
    Vector3D geodeticSurfaceNormal(const MutableVector3D& position) const {
        return Vector3D(0, 0, 1);
    }
    
    void geodeticSurfaceNormal(const Angle& latitude,
                               const Angle& longitude,
                               MutableVector3D& result) const {
        result.set(0, 0, 1);
    }
    
    Vector3D geodeticSurfaceNormal(const Angle& latitude,
                                   const Angle& longitude) const;
    
    Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const {
        return Vector3D(0, 0, 1);
    }
    
    Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const {
        return Vector3D(0, 0, 1);
    }
    
    std::vector<double> intersectionsDistances(double originX,
                                               double originY,
                                               double originZ,
                                               double directionX,
                                               double directionY,
                                               double directionZ) const;
    
    Vector3D toCartesian(const Angle& latitude,
                         const Angle& longitude,
                         const double height) const {
        const double latDeg = latitude.clampedTo(Angle::fromDegrees(-85.0),
                                                 Angle::fromDegrees(85.0))._degrees;
        Vector3D v(lon2x_m(longitude._degrees),
                   lat2y_m(latDeg),
                   height);
        return v;
    }
    
    Vector3D toCartesian(const Geodetic3D& geodetic) const {
        return toCartesian(geodetic._latitude,
                           geodetic._longitude,
                           geodetic._height);
    }
    
    Vector3D toCartesian(const Geodetic2D& geodetic) const {
        return toCartesian(geodetic._latitude,
                           geodetic._longitude,
                           0.0);
    }
    
    Vector3D toCartesian(const Geodetic2D& geodetic,
                         const double height) const {
        return toCartesian(geodetic._latitude,
                           geodetic._longitude,
                           height);
    }
    
    void toCartesian(const Angle& latitude,
                     const Angle& longitude,
                     const double height,
                     MutableVector3D& result) const {
        const double x = lon2x_m(longitude._degrees);
        const double y = lat2y_m(latitude._degrees);
        result.set(x, y, height);
    }
    
    void toCartesian(const Geodetic3D& geodetic,
                     MutableVector3D& result) const {
        toCartesian(geodetic._latitude,
                    geodetic._longitude,
                    geodetic._height,
                    result);
    }
    void toCartesian(const Geodetic2D& geodetic,
                     MutableVector3D& result) const {
        toCartesian(geodetic._latitude,
                    geodetic._longitude,
                    0.0,
                    result);
    }
    
    void toCartesian(const Geodetic2D& geodetic,
                     const double height,
                     MutableVector3D& result) const {
        toCartesian(geodetic._latitude,
                    geodetic._longitude,
                    height,
                    result);
    }
    
    Geodetic2D toGeodetic2D(const Vector3D& position) const;
    
    Geodetic3D toGeodetic3D(const Vector3D& position) const;
    
    Vector3D scaleToGeodeticSurface(const Vector3D& position) const;
    
    Vector3D scaleToGeocentricSurface(const Vector3D& position) const;
    
    Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const;
    
    
    double computePreciseLatLonDistance(const Geodetic2D& g1,
                                        const Geodetic2D& g2) const;
    
    double computeFastLatLonDistance(const Geodetic2D& g1,
                                     const Geodetic2D& g2) const;
    
    MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const;
    
    bool isFlat() const { return true; }
    
    void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const;
    
    MutableMatrix44D singleDrag(const Vector3D& finalRay) const;
    
    Effect* createEffectFromLastSingleDrag() const;
    
    void beginDoubleDrag(const Vector3D& origin,
                         const Vector3D& centerRay,
                         const Vector3D& initialRay0,
                         const Vector3D& initialRay1) const;
    
    MutableMatrix44D doubleDrag(const Vector3D& finalRay0,
                                const Vector3D& finalRay1) const;
    
    Effect* createDoubleTapEffect(const Vector3D& origin,
                                  const Vector3D& centerRay,
                                  const Vector3D& tapRay) const;
    
    double distanceToHorizon(const Vector3D& position) const;
    
    MutableMatrix44D drag(const Geodetic3D& origin, const Geodetic3D& destination) const;
    
    Vector3D getNorth() const {
        return Vector3D::upY();
    }
    
    void applyCameraConstrainers(const Camera* previousCamera,
                                 Camera* nextCamera) const;
    
    Geodetic3D getDefaultCameraPosition(const Sector& rendereSector) const {
        const Vector3D asw = toCartesian(rendereSector.getSW());
        const Vector3D ane = toCartesian(rendereSector.getNE());
        const double height = asw.sub(ane).length() * 1.9;
        
        return Geodetic3D(rendereSector._center,
                          height);
    }
    
    const std::string getType() const {
        return "Flat";
    }
    
};


#endif
