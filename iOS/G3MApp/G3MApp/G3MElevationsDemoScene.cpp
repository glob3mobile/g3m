//
//  G3MElevationsDemoScene.cpp
//  G3MApp
//
//  Created by Sebastian Ortega Trujillo on 10/3/16.
//

#include "G3MElevationsDemoScene.hpp"
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/PyramidElevationDataProvider.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>

#include "G3MDemoModel.hpp"

class OrbitCameraEffect : public EffectWithDuration {
private:
    const Geodetic3D _position;
    
    const double _fromDistance;
    const double _toDistance;
    
    const double _fromAzimuthInRadians;
    const double _toAzimuthInRadians;
    
    const double _fromAltitudeInRadians;
    const double _toAltitudeInRadians;
    
public:
    
    OrbitCameraEffect(const TimeInterval& duration,
                           Geodetic3D &pos,
                           double fromDistance,       double toDistance,
                           const Angle& fromAzimuth,  const Angle& toAzimuth,
                           const Angle& fromAltitude, const Angle& toAltitude,
                           const bool linearTiming=false) :
    EffectWithDuration(duration, linearTiming),
    _position(pos),
    _fromDistance(fromDistance),
    _toDistance(toDistance),
    _fromAzimuthInRadians(fromAzimuth._radians),
    _toAzimuthInRadians(toAzimuth._radians),
    _fromAltitudeInRadians(fromAltitude._radians),
    _toAltitudeInRadians(toAltitude._radians)
    {
        
    }
    
    void doStep(const G3MRenderContext* rc,
                const TimeInterval& when){
        const double alpha = getAlpha(when);
        
        const IMathUtils* mu = IMathUtils::instance();
        const double distance          = mu->linearInterpolation(_fromDistance,          _toDistance,          alpha);
        const double azimuthInRadians  = mu->linearInterpolation(_fromAzimuthInRadians,  _toAzimuthInRadians,  alpha);
        const double altitudeInRadians = mu->linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);
        
        rc->getNextCamera()->setPointOfView(_position,
                                            distance,
                                            Angle::fromRadians(azimuthInRadians),
                                            Angle::fromRadians(altitudeInRadians));
    }
    
    void cancel(const TimeInterval& when) {}
    
    void stop(const G3MRenderContext* rc,
              const TimeInterval& when){
        
        rc->getNextCamera()->setPointOfView(_position,
                                            _toDistance,
                                            Angle::fromRadians(_toAzimuthInRadians),
                                            Angle::fromRadians(_toAltitudeInRadians));
        
    }
    
};

void G3MElevationsDemoScene::rawActivate(const G3MContext* context) {
    G3MDemoModel* model     = getModel();
    BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                             "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                             TimeInterval::fromDays(30));
    
    model->getLayerSet()->addLayer(layer);
}

void G3MElevationsDemoScene::rawSelectOption(const std::string& option,
                     int optionIndex) {
    switch (optionIndex){
        case 0:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fullSphere(),2.0f);
            break;
        case 1:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/var-16/",Sector::fullSphere(),2.0f);
            break;
        case 2:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fromDegrees(34,-10,70,52),2.0f);
            break;
        case 3:
            loadElevs("http://193.145.147.50:8080/DemoElevs/elevs/fix-16/",Sector::fromDegrees(34,-10,70,52),2.0f);
            break;
    }
}


void G3MElevationsDemoScene::loadElevs(std::string layerServer,const Sector &layerSector, float vertEx){
    G3MDemoModel* model     = getModel();
    
    PlanetRenderer* planetRenderer = model->getPlanetRenderer();
    planetRenderer->setVerticalExaggeration(vertEx);

    
    planetRenderer->setElevationDataProvider(new PyramidElevationDataProvider(layerServer,layerSector), true);
    //LOCALE BIL VERSION
    //planetRenderer->setElevationDataProvider(new PyramidElevationDataProvider("http://10.230.171.227:8080/DemoElevs/elevs/redim/",layerSector), true);
    
#warning: Tour Eiffel animation to perform profiling tests only active in option 2.
    if (layerServer.compare("http://193.145.147.50:8080/DemoElevs/elevs/var-16/") == 0) {
        
        const double fromDistance = 10000;
        const double toDistance = 1000;
        
        const Angle fromAzimuth = Angle::fromDegrees(-90);
        const Angle toAzimuth   = Angle::fromDegrees(270);
        
        const Angle fromAltitude = Angle::fromDegrees(90);
        const Angle toAltitude   = Angle::fromDegrees(15);
        
        Geodetic3D geo = Geodetic3D(Angle::fromDegreesMinutesSeconds(48, 51, 29.06),
                                        Angle::fromDegreesMinutesSeconds(2, 17, 40.48),
                                        0);
        
        model->getG3MWidget()->getEffectsScheduler()->startEffect(new OrbitCameraEffect(TimeInterval::fromSeconds(20),
                                                                                        geo,
                                                                                        fromDistance, toDistance,
                                                                                        fromAzimuth,  toAzimuth,
                                                                                        fromAltitude, toAltitude),
                                                                  model->getG3MWidget()->getNextCamera()->getEffectTarget());
        
        /**/
    }
}