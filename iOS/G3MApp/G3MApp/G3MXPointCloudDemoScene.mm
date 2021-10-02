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
#include <G3M/GLConstants.hpp>

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
#include <G3M/MeasureRenderer.hpp>
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
  }

  bool onSelectedPoint(XPCPointCloud* pointCloud,
                       const Vector3D& cartesian,
                       const Geodetic3D& geodetic,
                       const std::string& treeID,
                       const std::string& nodeID,
                       const int pointIndex,
                       const double distanceToRay) const {
    G3MDemoModel* model = _scene->getModel();

    //model->getShapesRenderer()->removeAllShapes();
    //
    //const double radius = 75;
    //pointCloud->setSelection( new Sphere(cartesian, radius) );
    //
    //model->getShapesRenderer()->addShape( new EllipsoidShape(new Geodetic3D(geodetic),
    //                                                         AltitudeMode::ABSOLUTE,
    //                                                         Vector3D(radius, radius, radius),
    //                                                         (short) 24,                        /* resolution     */
    //                                                         1,                                 /* borderWidth    */
    //                                                         false,                             /* texturedInside */
    //                                                         false,                             /* mercator       */
    //                                                         Color::fromRGBA(0, 0, 0, 0.0f),    /* surfaceColor   */
    //                                                         Color::newFromRGBA(0, 0, 0, 1.0f), /* borderColor    */
    //                                                         true                               /* withNormals    */) );

    if (_measure == NULL) {
      _measure = new Measure(0.5,                             // vertexSphereRadius
                             Color::fromRGBA(1, 1, 0, 0.6f),  // vertexColor
                             Color::fromRGBA(1, 1, 1, 0.5f),  // vertexSelectedColor
                             5.0f,                            // segmentLineWidth
                             Color::fromRGBA(1, 1, 0, 0.6f),  // segmentColor
                             geodetic,                        // firstVertex
                             pointCloud->getVerticalExaggeration(),
                             pointCloud->getDeltaHeight(),
                             true,                            // closed
                             new G3MXPointCloudDemoScene_MeasureHandler(),
                             true);

      model->getMeasureRenderer()->addMeasure(_measure);
    }
    else {
      _measure->addVertex(geodetic);
    }

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
      
//      _g3mWidget->setAnimatedCameraPosition( Geodetic3D(center._latitude, center._longitude, height) );
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


  // const std::string cloudName   = "TOPCON_IPS3_LOD";
  // const double      deltaHeight = -50;

  // const std::string cloudName   = "Leica_FFCC_COMPLETE_LOD";
  // const double      deltaHeight = -180;

  // const std::string cloudName   = "PC_601fa9f0fe459753703dca36_LOD";
  // const double      deltaHeight = -180;

  const std::string cloudName   = "605dd8884db6b34761d54af1";
  // const double      deltaHeight = -396;
  //  const double      deltaHeight = -192.6893538717;
  const double      deltaHeight = 0;

  // const std::string cloudName   = "Leica_FFCC_SMALL_LOD";
  // const double      deltaHeight = -170;

  // const std::string cloudName   = "Leica_M40_LOD";
  // const double      deltaHeight = -580;

  // const std::string cloudName   = "NEON_LOD";
  // const double      deltaHeight = -385;

  const double minProjectedArea = 50000; //50000;
  const double draftPoints      = true;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

  // ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
  //                                                                                   Sector::fullSphere(),
  //                                                                                   Vector2I(2048, 1024));
  // planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30),
                                           true, // readExpired
                                           2,    // initialLevel
                                           19    // maxLevel     = 25
                                           );
  model->getLayerSet()->addLayer(layer);


  if (false) {
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
                                                  draftPoints,
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


  {
    // Track_A-Sphere-70.jpg
    // =====================
    //
    // time      472906.836
    //
    // camera X   610605.568
    // camera Y  4139976.049
    // camera z      189.019
    //
    // Omega     -100.06027963
    // Phi        -29.21672519
    // Kappa      198.66168520
    //
    // m11 m21 m31   -8.963265904306E-01  -1.884553278557E-02  -4.429937800704E-01
    // m12 m22 m32   -4.429156022003E-01  -8.365352766194E-03   8.965242831071E-01
    // m13 m23 m33   -2.060127701387E-02   9.997874107865E-01  -8.488923798331E-04


    const double size = 100.0;

    Geodetic3D geoPos = Geodetic3D::fromDegrees(37.39996584, -1.75035672, 189.019);
    
    EllipsoidShape* ellipsoid = new EllipsoidShape(new Geodetic3D(geoPos),                          // Geodetic3D* position,
                                                   AltitudeMode::ABSOLUTE,                          // AltitudeMode altitudeMode
                                                   g3mWidget->getG3MContext()->getPlanet(),         // const Planet* planet,
                                                   URL("file:///Track_A-Sphere-70-2048x2048.png"),  // const URL& textureURL,
                                                   Vector3D(size, size, size),                      // const Vector3D& radius
                                                   24,                                              // short resolution
                                                   0,                                               // float borderWidth
                                                   true,                                            // bool texturedInside
                                                   false,                                           // bool mercator
                                                   false                                            // bool withNormals = true
                                                   );
    
    //ellipsoid->setSurfaceColor(Color::fromRGBA(1, 1, 1, 0.5f));
    ellipsoid->setDepthTest(false);
    ellipsoid->setCullFace(true);
    ellipsoid->setCulledFace(GLCullFace::back());

    const double OmegaGradians = -100.06027963;
    const double PhiGradians   =  -29.21672519;
    const double KappaGradians =  198.66168520;
    

    const Angle omega = Angle::fromGradians( OmegaGradians );
    const Angle phi   = Angle::fromGradians( PhiGradians   );
    const Angle kappa = Angle::fromGradians( KappaGradians );
    
    ellipsoid->setOmegaPhiKappa(context->getPlanet(), omega, phi, kappa);
//
//    const MutableMatrix44D mm = MutableMatrix44D::createRotationMatrix(omega, phi, kappa);
//    //ellipsoid->setUserTransformMatrix(new MutableMatrix44D(mm));
//    ellipsoid->setLocalTransform( mm );


//    const double headingDegrees = /*360.0 -*/ -1.0 * (Phi / 400.0 * 360.0);
//    const double headingDegrees = 360.0 - Phi;

//    const double headingDegrees = ( Omega / 400.0 * 360.0); // -> NO
//    const double headingDegrees = (-Omega / 400.0 * 360.0); // -> NO
//    const double headingDegrees = (   Phi / 400.0 * 360.0); // -> NO
//    const double headingDegrees = (  -Phi / 400.0 * 360.0); // -> NO
//    const double headingDegrees = ( Kappa / 400.0 * 360.0); // -> NO
//    const double headingDegrees = (-Kappa / 400.0 * 360.0);

//    const double headingDegrees = 0;

//    ellipsoid->setHeading(Angle::fromDegrees( headingDegrees ));


    g3mWidget->setAnimatedCameraPosition(Geodetic3D(geoPos._latitude, geoPos._longitude, geoPos._height + 1000 /*1400*/));


    model->getShapesRenderer()->addShape( ellipsoid );
  }


}
