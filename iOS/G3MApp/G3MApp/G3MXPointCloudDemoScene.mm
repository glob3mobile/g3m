//
//  G3MXPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/15/21.
//

#include "G3MXPointCloudDemoScene.hpp"

#include <G3M/BingMapsLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/URL.hpp>
#include <G3M/DownloadPriority.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/SingleBILElevationDataProvider.hpp>
#include <G3M/PlanetRenderer.hpp>

#include <G3M/XPCRenderer.hpp>
#include <G3M/XPCPointCloud.hpp>
#include <G3M/XPCMetadataListener.hpp>
#include <G3M/XPCMetadata.hpp>
#include <G3M/XPCTree.hpp>
#include <G3M/XPCRGBPointColorizer.hpp>
#include <G3M/XPCIntensityPointColorizer.hpp>
#include <G3M/XPCClassificationPointColorizer.hpp>
#include <G3M/XPCHeightPointColorizer.hpp>
#include <G3M/XPCPointSelectionListener.hpp>
#include <G3M/G3MContext.hpp>
#include <G3M/Measure.hpp>
#include <G3M/MeasureHandler.hpp>
#include <G3M/RampColorizer.hpp>
#include <G3M/Sphere.hpp>
#include <G3M/ShapesRenderer.hpp>
#include <G3M/EllipsoidShape.hpp>

#import <G3MiOSSDK/NSString_CppAdditions.h>

#include "G3MDemoModel.hpp"


class G3MXPointCloudDemoScene_MeasureHandler : public MeasureHandler {
  NSNumberFormatter* _angleFormatter;
  NSNumberFormatter* _distanceFormatter;

public:

  G3MXPointCloudDemoScene_MeasureHandler() {
    _angleFormatter = [[NSNumberFormatter alloc] init];
    _angleFormatter.numberStyle = NSNumberFormatterDecimalStyle;
    _angleFormatter.minimumFractionDigits = 0;
    _angleFormatter.maximumFractionDigits = 1;

    _distanceFormatter = [[NSNumberFormatter alloc] init];
    _distanceFormatter.numberStyle = NSNumberFormatterDecimalStyle;
    _distanceFormatter.minimumFractionDigits = 0;
    _distanceFormatter.maximumFractionDigits = 3;
  }


  void onVertexDeselection(Measure* measure) {

  }

  void onVertexSelection(Measure* measure,
                         const Geodetic3D& geodetic,
                         const Vector3D& cartesian,
                         int selectedIndex) {
//    measure->removeVertex(selectedIndex);

  }

  std::string getAngleLabel(Measure* measure,
                            size_t vertexIndex,
                            const Angle& angle) {
    NSString* nsString = [_angleFormatter stringFromNumber:[NSNumber numberWithDouble:angle._degrees]];

    return [nsString toCppString] + "°";
  }

  std::string getDistanceLabel(Measure* measure,
                               size_t vertexIndexFrom,
                               size_t vertexIndexTo,
                               const double distanceInMeters) {
    NSString* nsString = [_distanceFormatter stringFromNumber:[NSNumber numberWithDouble:distanceInMeters]];

    return [nsString toCppString] + "m";
  }

};


class G3MXPointCloudDemoScene_PointSelectionListener : public XPCPointSelectionListener {
private:
  G3MXPointCloudDemoScene* _scene;

  mutable Measure* _measure;

public:

  G3MXPointCloudDemoScene_PointSelectionListener(G3MXPointCloudDemoScene* scene) :
  _scene(scene),
  _measure(NULL)
  {

  }

  ~G3MXPointCloudDemoScene_PointSelectionListener() {
//    delete _previousGeodetic;
//    delete _previousCartesian;
    delete _measure;
  }

  bool onSelectedPoint(XPCPointCloud* pointCloud,
                       const Vector3D& cartesian,
                       const Geodetic3D& geodetic,
                       const std::string& treeID,
                       const std::string& nodeID,
                       const int pointIndex,
                       const double distanceToRay) const {
    G3MDemoModel* model = _scene->getModel();


    model->getShapesRenderer()->removeAllShapes();

    const double radius = 75;
    pointCloud->setSelection( new Sphere(cartesian, radius) );

    model->getShapesRenderer()->addShape( new EllipsoidShape(new Geodetic3D(geodetic),
                                                             AltitudeMode::ABSOLUTE,
                                                             Vector3D(radius, radius, radius),
                                                             (short) 24,                        /* resolution     */
                                                             1,                                 /* borderWidth    */
                                                             false,                             /* texturedInside */
                                                             false,                             /* mercator       */
                                                             Color::fromRGBA(0, 0, 0, 0.0f),    /* surfaceColor   */
                                                             Color::newFromRGBA(0, 0, 0, 1.0f), /* borderColor    */
                                                             true                               /* withNormals    */) );

//    if (_measure == NULL) {
//      _measure = new Measure(0.5,                             // vertexSphereRadius
//                             Color::fromRGBA(1, 1, 0, 0.6f),  // vertexColor
//                             Color::fromRGBA(1, 1, 1, 0.5f),  // vertexSelectedColor
//                             5.0f,                            // segmentLineWidth
//                             Color::fromRGBA(1, 1, 0, 0.6f),  // segmentColor
//                             geodetic,                        // firstVertex
//                             pointCloud->getVerticalExaggeration(),
//                             pointCloud->getDeltaHeight(),
//                             model->getShapesRenderer(),
//                             model->getMeshRenderer(),
//                             model->getMarksRenderer(),
//                             model->getG3MWidget()->getG3MContext()->getPlanet(),
//                             new G3MXPointCloudDemoScene_MeasureHandler(),
//                             true);
//    }
//    else {
//      _measure->addVertex(geodetic,
//                          pointCloud->getVerticalExaggeration(),
//                          pointCloud->getDeltaHeight());
//    }

    return true; // accepted point
  }

};


class G3MXPointCloudDemoScene_PointCloudMetadataListener : public XPCMetadataListener {
private:
  G3MWidget* _g3mWidget;

public:
  G3MXPointCloudDemoScene_PointCloudMetadataListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onMetadata(const XPCMetadata* metadata) const {
    if (metadata->getTreesCount() > 0) {
      const XPCTree* tree = metadata->getTree(0);

      const Geodetic2D center = tree->getSector()->_center;
      const double height = (tree->getMaxHeight() - metadata->_averagePosition._height) * 10;
      
      _g3mWidget->setAnimatedCameraPosition( Geodetic3D(center._latitude, center._longitude, height) );
    }
  }

};


void G3MXPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float pointSize            = 2;
  const bool  dynamicPointSize     = true;
  const float verticalExaggeration = 1;
  const bool  depthTest            = false;
  const bool  verbose              = true;


//  const std::string cloudName   = "TOPCON_IPS3_LOD";
//  const double      deltaHeight = -50;


//  const std::string cloudName   = "Leica_FFCC_COMPLETE_LOD";
//  const double      deltaHeight = -180;

  // const std::string cloudName   = "PC_601fa9f0fe459753703dca36_LOD";
//  const double       deltaHeight = -180;
  const std::string cloudName   = "PC_6024eaa3bb098a3ad6a9176d_LOD";
  const double      deltaHeight = -396;

//  const std::string cloudName   = "Leica_FFCC_SMALL_LOD";
//  const double      deltaHeight = -170;

//  const std::string cloudName   = "Leica_M40_LOD";
//  const double      deltaHeight = -580;

//  const std::string cloudName   = "NEON_LOD";
//  const double      deltaHeight = -385;

  const double minProjectedArea = 50000; //50000;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

  //  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
  //                                                                                    Sector::fullSphere(),
  //                                                                                    Vector2I(2048, 1024));
  //  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30),
                                           true, // readExpired
                                           2,    // initialLevel
                                           19    // maxLevel     = 25
                                           );
  model->getLayerSet()->addLayer(layer);


#warning TODO cache
  XPCPointCloud* pointCould = new XPCPointCloud(URL("http://192.168.1.69:8080/INROAD_visor/xpc/"),
                                                cloudName,
                                                DownloadPriority::LOWER,
                                                TimeInterval::zero(),
                                                false,
                                                // new XPCHeightPointColorizer(RampColorizer::visibleSpectrum(), true, 1),
                                                new XPCRGBPointColorizer(1),
                                                // new XPCIntensityPointColorizer(1),
                                                // new XPCClassificationPointColorizer(1),
                                                true, // deletePointColorizer,
                                                minProjectedArea,
                                                pointSize,
                                                dynamicPointSize,
                                                depthTest,
                                                verticalExaggeration,
                                                deltaHeight,
                                                new G3MXPointCloudDemoScene_PointCloudMetadataListener(g3mWidget),
                                                true,  // deleteMetadataListener,
                                                new G3MXPointCloudDemoScene_PointSelectionListener(this),
                                                true,  //  deletePointSelectionListener,
                                                verbose);

  model->getXPCRenderer()->addPointCloud(pointCould);

}
