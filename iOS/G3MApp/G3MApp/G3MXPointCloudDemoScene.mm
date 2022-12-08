//
//  G3MXPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/15/21.
//

#include "G3MXPointCloudDemoScene.hpp"

#include <G3M/BingMapsLayer.hpp>
#include <G3M/WMSLayer.hpp>
#include <G3M/SectorTileCondition.hpp>
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
#include <G3M/IDownloader.hpp>
#include <G3M/IImageDownloadListener.hpp>
#include <G3M/QuadShape.hpp>
#include <G3M/IImage.hpp>

#import <G3MiOSSDK/NSString_CppAdditions.h>

#include "G3MDemoModel.hpp"

class G3MXPointCloudDemoScene_ImageDownloadListener : public IImageDownloadListener {
private:
  ShapesRenderer* _shapesRenderer;

public:
  G3MXPointCloudDemoScene_ImageDownloadListener(ShapesRenderer* shapesRenderer) :
  _shapesRenderer(shapesRenderer)
  {
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {

    // Camera position=(lat=36.911921721862746892d, lon=-2.412118908692491015d, height=19.599668045149311268) heading=10.657284 pitch=-17.247392

    // Camera position=(lat=36.912264117445509726d, lon=-2.4121385547341192002d, height=10.000047160904266264) heading=37.678233 pitch=-17.211769


    QuadShape* quad = new QuadShape(new Geodetic3D(Angle::fromDegrees(36.912264117445509726),
                                                   Angle::fromDegrees(-2.4121385547341192002),
                                                   10.000047160904266264+10),
                                    ABSOLUTE,
                                    image,
                                    image->getWidth()  * 15.0f/1000,
                                    image->getHeight() * 10.0f/1000,
                                    true);
    quad->setPitch(Angle::_HALF_PI);
    quad->setHeading(Angle::fromDegrees(-25));
    _shapesRenderer->addShape(quad);
  }

  void onError(const URL& url) {

  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};

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


  PlanetRenderer* planetRenderer = getModel()->getPlanetRenderer();
  //planetRenderer->setShowStatistics(true);
  planetRenderer->setIncrementalTileQuality(true);

  {
    const Sector demSector = Sector::fromDegrees(36.905181, -2.418163,
                                                 36.917903, -2.404156);

    //  g3mWidget->setRenderedSector(demSector.shrinkedByPercent(0.2f));

    ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///OUTPUT-4326.bil"),
                                                                                      demSector,
                                                                                      Vector2I(1394, 1267),
                                                                                      -144.9 /*-112*/ /*deltaHeight*/);
    planetRenderer->setElevationDataProvider(elevationDataProvider, true);
  }


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

//  const std::string cloudName   = "605dd8884db6b34761d54af1";
//  // const double      deltaHeight = -396;
//    const double      deltaHeight = -192.6893538717;
//  //const double      deltaHeight = 0;


//  const double      deltaHeight = -135.7643350558;
  const std::string cloudName   = "616e8b9cc55ad062c2111101";
  const double      deltaHeight = -144.9; // -135.7643350558 + 5;


  // const std::string cloudName   = "Leica_FFCC_SMALL_LOD";
  // const double      deltaHeight = -170;

  // const std::string cloudName   = "Leica_M40_LOD";
  // const double      deltaHeight = -580;

  // const std::string cloudName   = "NEON_LOD";
  // const double      deltaHeight = -385;

  const double minProjectedArea = 50000; //50000;
  const double draftPoints      = true;

//  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
//  planetRenderer->setVerticalExaggeration(verticalExaggeration);
//
//   ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
//                                                                                     Sector::fullSphere(),
//                                                                                     Vector2I(2048, 1024));
//   planetRenderer->setElevationDataProvider(elevationDataProvider, true);

//  BingMapsLayer* layer1 = new BingMapsLayer(BingMapType::AerialWithLabels(),
//                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
//                                           TimeInterval::fromDays(30),
//                                           true, // readExpired
//                                           2,    // initialLevel
//                                           19    // maxLevel     = 25
//                                           );
//  model->getLayerSet()->addLayer(layer1);



  {
    WMSLayer* layer2 = WMSLayer::newMercator("fondo",                                        // const std::string&        mapLayer,
                                             URL("https://www.ign.es/wms-inspire/pnoa-ma"),  // const URL&                mapServerURL,
                                             WMS_1_1_0,                                      // const WMSServerVersion    mapServerVersion,
                                             Sector::FULL_SPHERE,                            // const Sector&             dataSector,
                                             "image/jpeg",                                   // const std::string&        format,
                                             "",                                             // const std::string&        style,
                                             false,                                          // const bool                isTransparent,
                                             2,                                              // const int                 firstLevel   = 2,
                                             14                                              // const int                 maxLevel     = 17,
                                             // const LayerCondition*     condition    = NULL,
                                             // const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                                             // const bool                readExpired  = true,
                                             // const float               transparency = 1,
                                             // std::vector<const Info*>* layerInfo    = new std::vector<const Info*>()
                                             );
    
    model->getLayerSet()->addLayer(layer2);
  }


  {
    const Sector s3 = Sector::fromDegrees(27, -19,
                                          44,   5);

    WMSLayer* layer3 = WMSLayer::newMercator(//"OI.MosaicElement",
                                             "OI.OrthoimageCoverage",                       // const std::string&        mapLayer,
                                             URL("https://www.ign.es/wms-inspire/pnoa-ma"), // const URL&                mapServerURL,
                                             WMS_1_1_0,                                     // const WMSServerVersion    mapServerVersion,
                                             s3,                                            // const Sector&             dataSector,
                                             "image/png",                                   // const std::string&        format,
                                             "",                                            // const std::string&        style,
                                             true,                                          // const bool                isTransparent,
                                             2,                                             // const int                 firstLevel   = 2,
                                             19,                                            // const int                 maxLevel     = 17,
                                             new SectorTileCondition(s3)                    // const LayerCondition*     condition    = NULL,
                                             // const LayerCondition*     condition    = NULL,
                                             // const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                                             // const bool                readExpired  = true,
                                             // const float               transparency = 1,
                                             // std::vector<const Info*>* layerInfo    = new std::vector<const Info*>()
                                             );

    model->getLayerSet()->addLayer(layer3);
  }



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
    // Track_E-CAM8-1_2022.06.27_11.32.23(451).jpg;
    //
    // time:
    //     127927.986;
    //
    // camera position (x,y,z):
    //     250922.968;4432817.579;392.575;
    //
    // Omega / Phi / Kappa
    //    -24.74762636;-73.73090914;277.82929184;
    //
    // Rotation matrix:
    //    -1.36853159813E-001; 3.76949742662E-001; -9.16067739939E-001;
    //    -9.88324777859E-001; 1.05649465610E-002;  1.51995116287E-001;
    //     6.69727266888E-002; 9.26173457518E-001;  3.71102924360E-001



//    final String           projection = "EPSG:25830";

    QuadShape* quad = new QuadShape(new Geodetic3D(Angle::fromDegrees(40.0088347),
                                                   Angle::fromDegrees(-5.9181247),
                                                   0*392.575 + 5),
                                    AltitudeMode::ABSOLUTE,
                                    URL("file:///Track_E-CAM8-1_2022.06.27_11.32.23(451).jpg"), /* textureURL  */
                                    15.0f,                                                      /* width       */
                                    15.0f,                                                      /* height      */
                                    false                                                        /* withNormals */);

    quad->setDepthTest(false);
    quad->setCullFace(true);
    quad->setCulledFace(GLCullFace::back());


//    // Omega / Phi / Kappa
//    const double OmegaGradians = -24.74762636;
//    const double PhiGradians   = -73.73090914;
//    const double KappaGradians = 277.82929184;
//
//    const Angle omega = Angle::fromGradians( OmegaGradians * 1 );
//    const Angle phi   = Angle::fromGradians( PhiGradians   * 0 );
//    const Angle kappa = Angle::fromGradians( KappaGradians * 0 );
//
//    quad->setOmegaPhiKappa(context->getPlanet(),
//                           omega,
//                           phi,
//                           kappa);

//    <heading>97.8836</heading>
//    <tilt>68.1648</tilt>
//    <roll>3.8401</roll>

    quad->setHeadingPitchRoll(Angle::fromDegrees( 97.8836 ),
                              Angle::fromDegrees( 68.1648 ),
                              Angle::fromDegrees(  3.8401 ));

    model->getShapesRenderer()->addShape( quad );

//    gotoPo();
//    _g3mWidget->setAnimatedCameraPosition( Geodetic3D(center._latitude, center._longitude, 1000) );
    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40.0088347, -5.9181247, 19.599674182039432679 + 50),
                                         Angle::fromDegrees(0   /*  10.657284 */),  /* heading */
                                         Angle::fromDegrees(-90 /* -17.247392 */)   /* pitch   */);

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

//    // Track_A-Sphere-70-2048x2048
//    const double OmegaGradians = -100.06027963;
//    const double PhiGradians   =  -29.21672519;
//    const double KappaGradians =  198.66168520;
//    const Geodetic3D geoPos = Geodetic3D::fromDegrees(37.39996584, -1.75035672, 189.019 + deltaHeight);

    // Track_A-Sphere-972
    const double OmegaGradians = -101.44147520;
    const double PhiGradians   =  -49.94186732;
    const double KappaGradians = -198.47304174;
    const Geodetic3D geoPos = Geodetic3D::fromDegrees(37.3662688, -1.7256187, 218.096 + deltaHeight);


    EllipsoidShape* ellipsoid = new EllipsoidShape(new Geodetic3D(geoPos),                           // Geodetic3D* position,
                                                   AltitudeMode::ABSOLUTE,                           // AltitudeMode altitudeMode
                                                   g3mWidget->getG3MContext()->getPlanet(),          // const Planet* planet,
                                                   URL("file:///Track_A-Sphere-972-2048x2048.png"),  // const URL& textureURL,
                                                   Vector3D(size, size, size),                       // const Vector3D& radius
                                                   36,                                               // short resolution
                                                   0,                                                // float borderWidth
                                                   true,                                             // bool texturedInside
                                                   false,                                            // bool mercator
                                                   false                                             // bool withNormals = true
                                                   );
    
    //ellipsoid->setSurfaceColor(Color::fromRGBA(1, 1, 1, 0.5f));
    ellipsoid->setDepthTest(false);
    ellipsoid->setCullFace(true);
    ellipsoid->setCulledFace(GLCullFace::back());


//    Track_A-Sphere-972.jpg;
//    473329.283;
//    612845.710;
//    4136266.739;
//    218.096;
//
//    -101.44147520;
//    -49.94186732;
//    -198.47304174;
//
//    -7.075486030427e-001;
//    1.697409443181e-002;
//    -7.064607947017e-001;
//    -7.055335591834e-001;
//    3.957297252408e-002;
//    7.075707573887e-001;
//    3.996712647117e-002;
//    9.990724998537e-001;
//    -1.602400816927e-002


    const Angle omega = Angle::fromGradians( OmegaGradians );
    const Angle phi   = Angle::fromGradians( PhiGradians   );
    const Angle kappa = Angle::fromGradians( KappaGradians );

    ellipsoid->setOmegaPhiKappa(context->getPlanet(),
                                omega,
                                phi,
                                kappa);

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


//    g3mWidget->setAnimatedCameraPosition(Geodetic3D(geoPos._latitude, geoPos._longitude, geoPos._height /*+ size*3*/ /*1400*/),
//                                         Angle::_ZERO,
//                                         Angle::fromDegrees(-15));

//    g3mWidget->setAnimatedCameraPosition(Geodetic3D(geoPos._latitude, geoPos._longitude, geoPos._height + size*4));

//    // Camera position=(lat=36.911809142935325667d, lon=-2.4120819810846030329d, height=30.350913169156211069) heading=8.702367 pitch=-23.165191
//    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(36.911809142935325667, -2.4120819810846030329, 30.350913169156211069),
//                                         Angle::fromDegrees(8.702367),  /* heading */
//                                         Angle::fromDegrees(-23.165191) /* pitch   */);

    // Camera position=(lat=36.911921721862562151d, lon=-2.4121189086925118872d, height=19.599674182039432679) heading=10.657284 pitch=-17.247392
//    g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(36.911921721862562151, -2.4121189086925118872, 19.599674182039432679),
//                                         Angle::fromDegrees(0 /*10.657284*/),  /* heading */
//                                         Angle::fromDegrees(-17.247392)  /* pitch   */);

    model->getShapesRenderer()->addShape( ellipsoid );
  }


  {
    context->getDownloader()->requestImage(URL("file:///OverviewCamera_19666_20210310_114027335.PNG"),
                                           1, // priority,
                                           TimeInterval::zero(),
                                           false,
                                           new G3MXPointCloudDemoScene_ImageDownloadListener(model->getShapesRenderer()),
                                           true);

  }

}
