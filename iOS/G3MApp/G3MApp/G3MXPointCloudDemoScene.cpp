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
#include <G3M/XPCMetadataListener.hpp>
#include <G3M/XPCMetadata.hpp>
#include <G3M/XPCTree.hpp>
#include <G3M/XPCRGBPointColorizer.hpp>
#include <G3M/XPCIntensityPointColorizer.hpp>
#include <G3M/XPCClassificationPointColorizer.hpp>
#include <G3M/XPCPointSelectionListener.hpp>
#include <G3M/ShapesRenderer.hpp>
#include <G3M/EllipsoidShape.hpp>
#include <G3M/FloatBufferBuilderFromGeodetic.hpp>
#include <G3M/DirectMesh.hpp>
#include <G3M/MeshRenderer.hpp>
#include <G3M/G3MContext.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/Mark.hpp>

#include "G3MDemoModel.hpp"


class G3MXPointCloudDemoScene_PointSelectionListener : public XPCPointSelectionListener {
private:
  G3MXPointCloudDemoScene* _scene;

  mutable Geodetic3D* _previousPosition;

public:

  G3MXPointCloudDemoScene_PointSelectionListener(G3MXPointCloudDemoScene* scene) :
  _scene(scene),
  _previousPosition(NULL)
  {

  }

  ~G3MXPointCloudDemoScene_PointSelectionListener() {
    delete _previousPosition;
  }

  bool onSelectedPoint(const XPCPointCloud* pointCloud,
                       const Vector3D& cartesian,
                       const Geodetic3D& geodetic,
                       const std::string& treeID,
                       const std::string& nodeID,
                       const int pointIndex,
                       const double distanceToRay) const {
    G3MDemoModel* model = _scene->getModel();

    {
      Shape* sphere = new EllipsoidShape(new Geodetic3D(geodetic),
                                         AltitudeMode::ABSOLUTE,
                                         Vector3D(0.2,0.2,0.2),
                                         (short) 16, // resolution,
                                         0,  // float borderWidth,
                                         false, // bool texturedInside,
                                         false, // bool mercator,
                                         Color::fromRGBA(1, 1, 0, 0.6f) // const Color& surfaceColor,
                                         );


      model->getShapesRenderer()->addShape( sphere );
    }

    if (_previousPosition != NULL) {
      const Planet* planet = model->getG3MWidget()->getG3MContext()->getPlanet();

      FloatBufferBuilderFromGeodetic* fbb = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);

      fbb->add( *_previousPosition );
      fbb->add(geodetic);

      Mesh* mesh = new DirectMesh(GLPrimitive::lines(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  25.0f, // float lineWidth
                                  1.0f, // float pointSize
                                  Color::newFromRGBA(1, 1, 0, 0.6f), // const Color* flatColor
                                  NULL,  // const IFloatBuffer* colors
                                  false, // depthTest
                                  NULL,  // const IFloatBuffer* normals = NULL,
                                  true,  // bool polygonOffsetFill      = false,
                                  10,    // float polygonOffsetFactor   = 0,
                                  10     // float polygonOffsetUnits    = 0,
                                  );


//      DirectMesh(const int primitive,
//                 bool owner,
//                 const Vector3D& center,
//                 const IFloatBuffer* vertices,
//                 float lineWidth,
//                 float pointSize,
//                 const Color* flatColor      = NULL,
//                 const IFloatBuffer* colors  = NULL,
//                 bool depthTest              = true,
//                 const IFloatBuffer* normals = NULL,
//                 bool polygonOffsetFill      = false,
//                 float polygonOffsetFactor   = 0,
//                 float polygonOffsetUnits    = 0,
//                 bool cullFace               = false,
//                 int  culledFace             = GLCullFace::back());

      model->getMeshRenderer()->addMesh( mesh );

      Geodetic3D middle = Geodetic3D::linearInterpolation(geodetic, *_previousPosition, 0.5);
      Mark* mark = new Mark( IStringUtils::instance()->toString(   10  ),
                            middle,
                            ABSOLUTE);

//      Mark(const std::string& label,
//           const Geodetic3D&  position,
//           AltitudeMode       altitudeMode,
//           double             minDistanceToCamera=4.5e+06,
//           const float        labelFontSize=20,
//           const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
//           const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
//           MarkUserData*      userData=NULL,
//           bool               autoDeleteUserData=true,
//           MarkTouchListener* listener=NULL,
//           bool               autoDeleteListener=false);

      model->getMarksRenderer()->addMark( mark );
    }

    delete _previousPosition;
    _previousPosition = new Geodetic3D(geodetic);


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
      const double height = tree->getMaxHeight() * 10;
      
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
  const bool  depthTest            = true;
  const bool  verbose              = true;

  const std::string cloudName   = "Leica_FFCC_COMPLETE_LOD";
  const float       deltaHeight = -170;

//  const std::string cloudName   = "Leica_FFCC_SMALL_LOD";
//  const float       deltaHeight = -210;

//  const std::string cloudName   = "Leica_M40_LOD";
//  const float       deltaHeight = -580;

//  const std::string cloudName   = "NEON_LOD";
//  const float       deltaHeight = -385;

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
  model->getXPCRenderer()->addPointCloud(URL("http://192.168.1.69:8080/INROAD_visor/xpc/"),
                                         cloudName,
                                         DownloadPriority::LOWER,
                                         TimeInterval::zero(),
                                         false,
                                         new XPCRGBPointColorizer(),
                                         // new XPCIntensityPointColorizer(),
                                         // new XPCClassificationPointColorizer(),
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

}
