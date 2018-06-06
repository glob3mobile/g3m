//
//  ViewController.m
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"
#import "PopupViewController.h"

#import "PipesRenderer.hpp"

#import <G3MiOSSDK/G3MBuilder_iOS.hpp>
#import <G3MiOSSDK/VisibleSectorListener.hpp>
#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/ShapesRenderer.hpp>
#import <G3MiOSSDK/GEORenderer.hpp>
#import <G3MiOSSDK/BusyMeshRenderer.hpp>
#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromGeodetic.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromColor.hpp>
#import <G3MiOSSDK/DirectMesh.hpp>
#import <G3MiOSSDK/WMSLayer.hpp>
#import <G3MiOSSDK/CameraSingleDragHandler.hpp>
#import <G3MiOSSDK/CameraDoubleDragHandler.hpp>
#import <G3MiOSSDK/CameraZoomAndRotateHandler.hpp>
#import <G3MiOSSDK/CameraRotationHandler.hpp>
#import <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#import <G3MiOSSDK/LevelTileCondition.hpp>
#import <G3MiOSSDK/SectorTileCondition.hpp>
#import <G3MiOSSDK/AndTileCondition.hpp>
#import <G3MiOSSDK/LayerBuilder.hpp>
#import <G3MiOSSDK/PlanetRendererBuilder.hpp>
#import <G3MiOSSDK/MarkTouchListener.hpp>
#import <G3MiOSSDK/TrailsRenderer.hpp>
#import <G3MiOSSDK/Mark.hpp>
#import <G3MiOSSDK/CircleShape.hpp>
#import <G3MiOSSDK/QuadShape.hpp>
#import <G3MiOSSDK/BoxShape.hpp>
#import <G3MiOSSDK/EllipsoidShape.hpp>
#import <G3MiOSSDK/SceneJSShapesParser.hpp>
#import <G3MiOSSDK/LayoutUtils.hpp>
#import <G3MiOSSDK/IJSONParser.hpp>
#import <G3MiOSSDK/JSONGenerator.hpp>
#import <G3MiOSSDK/JSONString.hpp>
#import <G3MiOSSDK/BSONParser.hpp>
#import <G3MiOSSDK/BSONGenerator.hpp>
#import <G3MiOSSDK/MeshShape.hpp>
#import <G3MiOSSDK/IShortBuffer.hpp>
#import <G3MiOSSDK/SimpleCameraConstrainer.hpp>
#import <G3MiOSSDK/WMSBilElevationDataProvider.hpp>
#import <G3MiOSSDK/ElevationData.hpp>
#import <G3MiOSSDK/IBufferDownloadListener.hpp>
#import <G3MiOSSDK/BilParser.hpp>
#import <G3MiOSSDK/ShortBufferBuilder.hpp>
#import <G3MiOSSDK/BilinearInterpolator.hpp>
#import <G3MiOSSDK/SubviewElevationData.hpp>
#import <G3MiOSSDK/GInitializationTask.hpp>
#import <G3MiOSSDK/PeriodicalTask.hpp>
#import <G3MiOSSDK/IDownloader.hpp>
#import <G3MiOSSDK/OSMLayer.hpp>
#import <G3MiOSSDK/CartoDBLayer.hpp>
#import <G3MiOSSDK/HereLayer.hpp>
#import <G3MiOSSDK/MapQuestLayer.hpp>
#import <G3MiOSSDK/MapBoxLayer.hpp>
#import <G3MiOSSDK/GoogleMapsLayer.hpp>
#import <G3MiOSSDK/BingMapsLayer.hpp>
#import <G3MiOSSDK/BusyQuadRenderer.hpp>
#import <G3MiOSSDK/Factory_iOS.hpp>
#import <G3MiOSSDK/G3MWidget.hpp>
#import <G3MiOSSDK/GEOJSONParser.hpp>
//import <G3MiOSSDK/WMSBilElevationDataProvider.hpp>
#import <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#import <G3MiOSSDK/FloatBufferElevationData.hpp>
#import <G3MiOSSDK/GEOSymbolizer.hpp>
#import <G3MiOSSDK/GEO2DMultiLineStringGeometry.hpp>
#import <G3MiOSSDK/GEO2DLineStringGeometry.hpp>
#import <G3MiOSSDK/GEOFeature.hpp>
#import <G3MiOSSDK/JSONObject.hpp>
#import <G3MiOSSDK/GEOLine2DMeshSymbol.hpp>
#import <G3MiOSSDK/GEOMultiLine2DMeshSymbol.hpp>
#import <G3MiOSSDK/GEOLine2DStyle.hpp>
#import <G3MiOSSDK/GEO2DPointGeometry.hpp>
#import <G3MiOSSDK/GEOShapeSymbol.hpp>
#import <G3MiOSSDK/GEOMarkSymbol.hpp>
#import <G3MiOSSDK/GFont.hpp>
#import <G3MiOSSDK/CompositeElevationDataProvider.hpp>
#import <G3MiOSSDK/LayerTilesRenderParameters.hpp>
#import <G3MiOSSDK/RectangleI.hpp>
#import <G3MiOSSDK/LayerTilesRenderParameters.hpp>
#import <G3MiOSSDK/QuadShape.hpp>
#import <G3MiOSSDK/IImageUtils.hpp>
#import <G3MiOSSDK/IImageDownloadListener.hpp>
#import <G3MiOSSDK/RectangleF.hpp>
#import <G3MiOSSDK/ShortBufferElevationData.hpp>
#import <G3MiOSSDK/SGShape.hpp>
#import <G3MiOSSDK/SGNode.hpp>
#import <G3MiOSSDK/SGMaterialNode.hpp>
#import <G3MiOSSDK/MapBooOLDBuilder_iOS.hpp>
#import <G3MiOSSDK/IWebSocketListener.hpp>
#import <G3MiOSSDK/GPUProgramFactory.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromCartesian3D.hpp>
#import <G3MiOSSDK/Color.hpp>
#import <G3MiOSSDK/GEOLineRasterSymbol.hpp>
#import <G3MiOSSDK/GEOMultiLineRasterSymbol.hpp>
#import <G3MiOSSDK/GEO2DLineRasterStyle.hpp>
#import <G3MiOSSDK/GEO2DPolygonGeometry.hpp>
#import <G3MiOSSDK/GEOPolygonRasterSymbol.hpp>
#import <G3MiOSSDK/GEO2DSurfaceRasterStyle.hpp>
#import <G3MiOSSDK/GEO2DMultiPolygonGeometry.hpp>
#import <G3MiOSSDK/GPUProgramFactory.hpp>
#import <G3MiOSSDK/GenericQuadTree.hpp>
#import <G3MiOSSDK/GEOFeatureCollection.hpp>
#import <G3MiOSSDK/Angle.hpp>
#import <G3MiOSSDK/SectorAndHeightCameraConstrainer.hpp>
#import <G3MiOSSDK/HUDImageRenderer.hpp>
#import <G3MiOSSDK/ColumnCanvasElement.hpp>
#import <G3MiOSSDK/TextCanvasElement.hpp>
#import <G3MiOSSDK/URLTemplateLayer.hpp>
#import <G3MiOSSDK/JSONArray.hpp>
#import <G3MiOSSDK/SceneLighting.hpp>
#import <G3MiOSSDK/HUDRenderer.hpp>
#import <G3MiOSSDK/HUDQuadWidget.hpp>
#import <G3MiOSSDK/HUDAbsolutePosition.hpp>
#import <G3MiOSSDK/HUDRelativePosition.hpp>
#import <G3MiOSSDK/MultiTexturedHUDQuadWidget.hpp>
#import <G3MiOSSDK/HUDAbsoluteSize.hpp>
#import <G3MiOSSDK/HUDRelativeSize.hpp>
#import <G3MiOSSDK/DownloaderImageBuilder.hpp>
#import <G3MiOSSDK/LabelImageBuilder.hpp>
#import <G3MiOSSDK/CanvasImageBuilder.hpp>
#import <G3MiOSSDK/TerrainTouchListener.hpp>
#import <G3MiOSSDK/PlanetRenderer.hpp>
#import <G3MiOSSDK/G3MMeshParser.hpp>
#import <G3MiOSSDK/CoordinateSystem.hpp>
#import <G3MiOSSDK/TaitBryanAngles.hpp>
#import <G3MiOSSDK/GEOLabelRasterSymbol.hpp>
#import <G3MiOSSDK/LayerTouchEventListener.hpp>
#import <G3MiOSSDK/TiledVectorLayer.hpp>
#import <G3MiOSSDK/GEORasterSymbolizer.hpp>
#import <G3MiOSSDK/GEO2DPolygonData.hpp>
#import <G3MiOSSDK/ChessboardLayer.hpp>
#import <G3MiOSSDK/GEORectangleRasterSymbol.hpp>
#import <G3MiOSSDK/CompositeRenderer.hpp>

#import <G3MiOSSDK/DefaultInfoDisplay.hpp>
#import <G3MiOSSDK/DebugTileImageProvider.hpp>
#import <G3MiOSSDK/GEOVectorLayer.hpp>
#import <G3MiOSSDK/Info.hpp>

#import <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#import <G3MiOSSDK/LayerSet.hpp>
#import <G3MiOSSDK/PlanetRenderer.hpp>
#import <G3MiOSSDK/ILocationModifier.hpp>
#import <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#import <G3MiOSSDK/CityGMLBuilding.hpp>


#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLNode.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/CityGMLParser.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include <G3MiOSSDK/EllipsoidalPlanet.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include <G3MiOSSDK/TerrainTouchListener.hpp>
#include <G3MiOSSDK/ColorLegend.hpp>
#include <G3MiOSSDK/BuildingDataParser.hpp>

#include <G3MiOSSDK/CameraSingleDragHandler.hpp>
#include <G3MiOSSDK/CameraDoubleDragHandler.hpp>
#include <G3MiOSSDK/CameraRotationHandler.hpp>
#include <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/AbstractMesh.hpp>
#include <G3MiOSSDK/CityGMLBuildingTessellator.hpp>
#include <G3MiOSSDK/URL.hpp>

#include <G3MiOSSDK/HUDQuadWidget.hpp>
#include <G3MiOSSDK/HUDRenderer.hpp>
#include <G3MiOSSDK/HUDRelativePosition.hpp>
#include <G3MiOSSDK/HUDRelativeSize.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/HUDAbsolutePosition.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>
#include <G3MiOSSDK/FloatBuffer_iOS.hpp>
#include <G3MiOSSDK/GInitializationTask.hpp>

#include <G3MiOSSDK/CityGMLRenderer.hpp>
#include <G3MiOSSDK/SphericalPlanet.hpp>
#include <G3MiOSSDK/ElevationData.hpp>
#include <G3MiOSSDK/PointCloudMesh.hpp>
#include <G3MiOSSDK/Surface.hpp>

#include <G3MiOSSDK/Cylinder.hpp>
#include <G3MiOSSDK/G3MEventContext.hpp> //Línea puesta por Chano.
#include <G3MiOSSDK/TexturesHandler.hpp>
#include <G3MiOSSDK/TextureIDReference.hpp>
#include <G3MiOSSDK/TexturedMesh.hpp>
#include <G3MiOSSDK/FloatBufferBuilderFromCartesian2D.hpp>
#include <G3MiOSSDK/SimpleTextureMapping.hpp>
#include "PipesModel.hpp"
#include "UtilityNetworkParser.hpp"


#import <QuartzCore/QuartzCore.h>

#import "AppDelegate.h"

#include <typeinfo>

class PipeListener: public PipeTouchedListener{
public:
    void onPipeTouched(Cylinder *c, Cylinder::CylinderMeshInfo info){
        //NSString *str = [NSString stringWithFormat:@"ID: %d",info._cylId];
        NSString *str = [NSString stringWithFormat:@"%s",info.getMessage().c_str()];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Pipe Touched"
                                                         message:str
                                                        delegate:nil
                                               cancelButtonTitle:@"OK"
                                               otherButtonTitles:nil];
        [alert show];
    }
    
};

class HoleTouchListener : public TerrainTouchListener{
private:
    ViewController *vC;
public:
    HoleTouchListener(ViewController * viewController):
    vC(viewController){}
    
    ~HoleTouchListener(){
        vC = NULL;
    }
    
    bool onTerrainTouch(const G3MEventContext* ec,
                        const Vector2F&        pixel,
                        const Camera*          camera,
                        const Geodetic3D&      position,
                        const Tile*            tile){
        if ([vC isHole]){
            if([vC getMapMode] == 0){
              [vC changeHole:position];
            }
            else{
                const Vector3D cameraDir = camera->getViewDirection();
                Camera c(camera->getTimestamp());
                c.copyFrom(*camera, true);
                c.translateCamera(cameraDir.normalized().times(25));
                Geodetic3D cameraPos = c.getGeodeticPosition();
                [vC changeHole:cameraPos];
            }
        }
        
        return false;
    }
};

class MyHoleListener : public IElevationDataListener {
    
private:
    ViewController *vC;
    
public:
    MyHoleListener(ViewController *viewController):
    vC(viewController){}
    
    ~MyHoleListener() {
        vC = NULL;
    }
    
    void onData(const Sector& sector, const Vector2I& extent, ElevationData *elevationData) {
        vC.holeElevData = elevationData;
        [vC loadCityModel];
        vC = NULL;
    }
    
    void onError(const Sector&, const Vector2I& extent) {
        vC = NULL;
    }
    
    void onCancel(const Sector&, const Vector2I& extent) {
        vC = NULL;
    }
    
};


class HoleCoverHelper {

    public:
    static void generateHoleCover(Sector sector, Vector3D center, const Planet *p, const G3MRenderContext *rc,
                                         const ElevationData *elevData, MeshRenderer *holeRenderer){
        
        Sector holeSector = generateSector(center,p,9);
        Sector outerSector = generateSector(center,p,15);
        lineWidth = 8;
        pointWidth = 8;
        //Color.fromRGBA255(128,0,0,255);
        wallColor = new Color(Color::fromRGBA255(153,76,0,255)); //Color.yellow().muchLighter();
        coverColor = new Color(wallColor->lighter());
        groundColor = new Color(wallColor->darker());
        holeRenderer->clearMeshes();
        
        generateHole(holeSector,p,rc,elevData,holeRenderer);
        generateOuterCover(holeSector,outerSector,p,elevData,holeRenderer,rc);
        
        delete wallColor;
        delete coverColor;
        delete groundColor;
        
    }
    
    static void loadHoleImage(IDownloader *downloader, URL path){
        downloader->requestImage(path,1000,TimeInterval::fromDays(30),true, new HoleImageListener(), true);
    }
    
    static void loadCoverImage(IDownloader *downloader, URL path){
        downloader->requestImage(path, 1000, TimeInterval::fromDays(30), true, new CoverImageListener(), true);
    }
    
    static void deleteImages(){
        delete coverImage;
        delete holeImage;
        coverImage = NULL;
        holeImage = NULL;
    }
    
    
private:
    static Color *groundColor, *wallColor, *coverColor;
    static float lineWidth,pointWidth;
    static const int HOLE_DEPTH = 20;
    static IImage *coverImage, *holeImage;
    
    class HoleImageListener : public IImageDownloadListener{
    public:
        void onDownload(const URL& url, IImage *image, bool expired) {
            holeImage = image;
        }
        void onError(const URL& url) {}
        void onCancel(const URL& url) {}
        void onCanceledDownload(const URL& url,IImage* image, bool expired){}
        
       ~HoleImageListener(){}
    };
    class CoverImageListener : public IImageDownloadListener{
    public:
        void onDownload(const URL& url, IImage *image, bool expired) {
            coverImage = image;
        }
        void onError(const URL& url) {}
        void onCancel(const URL& url) {}
        void onCanceledDownload(const URL& url,IImage* image, bool expired){}
        
        ~CoverImageListener(){}
    };
    
    static Sector generateSector(Vector3D v1, const Planet *p, double offset){
        Vector3D v2 = Vector3D(v1._x - offset, v1._y - offset, v1._z);
        Vector3D v3 = Vector3D(v1._x + offset, v1._y + offset, v1._z);
        Geodetic3D l = p->toGeodetic3D(v2);
        Geodetic3D u = p->toGeodetic3D(v3);
        Geodetic2D lower = Geodetic2D::fromDegrees(
                                                  fmin(l._latitude._degrees, u._latitude._degrees),
                                                  fmin(l._longitude._degrees, u._longitude._degrees));
        Geodetic2D upper = Geodetic2D::fromDegrees(
                                                  fmax(l._latitude._degrees, u._latitude._degrees),
                                                  fmax(l._longitude._degrees, u._longitude._degrees));
        
        return Sector(lower,upper);
    }
    
    static void generateHole(Sector holeSector, const Planet *p, const G3MRenderContext *rc,
                                     const ElevationData *elevData, MeshRenderer *holeRenderer){
        
        Geodetic2D nw = holeSector.getNW();
        Geodetic2D ne = holeSector.getNE();
        Geodetic2D sw = holeSector.getSW();
        Geodetic2D se = holeSector.getSE();
        
        double hnw = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(nw) + 0.5 : 1;
        double hne = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(ne) + 0.5 : 1;
        double hsw = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(sw) + 0.5 : 1;
        double hse = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(se) + 0.5 : 1;
        
        double md = ((hnw + hne + hsw + hse) / 4) - HOLE_DEPTH;
        
        generateHoleGround(holeSector,md,p,rc,holeRenderer);
        generateHoleWall(nw,ne,hnw,hne,md,p,holeRenderer,rc);
        generateHoleWall(ne,se,hne,hse,md,p,holeRenderer,rc);
        generateHoleWall(se,sw,hse,hsw,md,p,holeRenderer,rc);
        generateHoleWall(sw,nw,hsw,hnw,md,p,holeRenderer,rc);
        
    }
    
    static void generateHoleGround(Sector holeSector,double depth, const Planet *p,
                                   const G3MRenderContext *rc, MeshRenderer *holeRenderer){
        Geodetic2D nw = holeSector.getNW();
        Geodetic2D ne = holeSector.getNE();
        Geodetic2D sw = holeSector.getSW();
        Geodetic2D se = holeSector.getSE();
        // Ground
        FloatBufferBuilderFromGeodetic *fbb =
        FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(p);
        
        double md = fmax(depth,1);
        fbb->add(Geodetic3D(nw,md));
        fbb->add(Geodetic3D(ne,md));
        fbb->add(Geodetic3D(se,md));
        fbb->add(Geodetic3D(sw,md));
        fbb->add(Geodetic3D(nw,md));
        
        Mesh *mesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                   true,
                                   fbb->getCenter(),
                                   fbb->create(),
                                   lineWidth,
                                   pointWidth,
                                   new Color(*groundColor),
                                   NULL, 0.0f, true);
        if (coverImage != NULL){
            const std::string texName = "HOLETEXTURE_" + holeSector.description();

            
            const TextureIDReference *texId = rc->getTexturesHandler()->getTextureIDReference(coverImage, GLFormat::rgba(),texName,false);
            FloatBufferBuilderFromCartesian2D texCoords;
            texCoords.add(0,0);
            texCoords.add(0,1);
            texCoords.add(1,1);
            texCoords.add(1,0);
            TextureMapping *tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
            TexturedMesh *tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
            holeRenderer->addMesh(tMesh);
         }
         else {
             holeRenderer->addMesh(mesh);
         }

        
        delete fbb;
    }
    
    static void generateHoleWall(Geodetic2D start, Geodetic2D end,
                                         double hStart, double hEnd, double depth, const Planet *p, MeshRenderer *holeRenderer,const G3MRenderContext *rc){
        
        FloatBufferBuilderFromGeodetic *fbb =
        FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(p);
        
        double md = fmax(depth,1);
        
        fbb->add(Geodetic3D(start,hStart));
        fbb->add(Geodetic3D(end,hEnd));
        fbb->add(Geodetic3D(end,md));
        fbb->add(Geodetic3D(start,md));
        fbb->add(Geodetic3D(start,hStart));
        
        Mesh *mesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                   true,
                                   fbb->getCenter(),
                                   fbb->create(),
                                   lineWidth,
                                   pointWidth,
                                   new Color(*wallColor),//color,
                                   NULL, 0.0f, true);
        if (holeImage != NULL){
            const std::string texName = "HOLETEXTURE_" + start.description() + end.description();
            const TextureIDReference *texId = rc->getTexturesHandler()->getTextureIDReference(holeImage, GLFormat::rgba(),texName,false);
            FloatBufferBuilderFromCartesian2D texCoords;
            texCoords.add(1,0);
            texCoords.add(0,0);
            texCoords.add(0,1);
            texCoords.add(1,1);
            
            TextureMapping *tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
            TexturedMesh *tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
            holeRenderer->addMesh(tMesh);
        }
        else {
            holeRenderer->addMesh(mesh);
        }
        
        delete fbb;
    }
    
   static void generateOuterCover(Sector holeSector,Sector outerSector,
                                           const Planet *p, const ElevationData *elevData, MeshRenderer *holeRenderer,const G3MRenderContext *rc){
        Geodetic2D nw = holeSector.getNW();
        Geodetic2D ne = holeSector.getNE();
        Geodetic2D sw = holeSector.getSW();
        Geodetic2D se = holeSector.getSE();
        
        double hnw = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(nw) + 0.5 : 1;
        double hne = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(ne) + 0.5 : 1;
        double hsw = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(sw) + 0.5 : 1;
        double hse = (elevData != NULL &&
                      elevData->getSector().fullContains(holeSector)) ?
        elevData->getElevationAt(se) + 0.5 : 1;
        
        Geodetic2D onw = outerSector.getNW();
        Geodetic2D one = outerSector.getNE();
        Geodetic2D osw = outerSector.getSW();
        Geodetic2D ose = outerSector.getSE();
        
        double honw = (elevData != NULL &&
                       elevData->getSector().fullContains(outerSector)) ?
        elevData->getElevationAt(onw) : 1;
        double hone = (elevData != NULL &&
                       elevData->getSector().fullContains(outerSector)) ?
        elevData->getElevationAt(one) : 1;
        double hosw = (elevData != NULL &&
                       elevData->getSector().fullContains(outerSector)) ?
        elevData->getElevationAt(osw) : 1;
        double hose = (elevData != NULL &&
                       elevData->getSector().fullContains(outerSector)) ?
        elevData->getElevationAt(ose) : 1;
        
        Geodetic3D outerNW = Geodetic3D(onw,honw);
        Geodetic3D outerNE = Geodetic3D(one,hone);
        Geodetic3D outerSW = Geodetic3D(osw,hosw);
        Geodetic3D outerSE = Geodetic3D(ose,hose);
        Geodetic3D innerNW = Geodetic3D(nw,hnw);
        Geodetic3D innerNE = Geodetic3D(ne,hne);
        Geodetic3D innerSW = Geodetic3D(sw,hsw);
        Geodetic3D innerSE = Geodetic3D(se,hse);
        
        generateOuterGround(outerNW,outerNE,innerNW,innerNE,p,holeRenderer,rc);
        generateOuterGround(outerNE,outerSE,innerNE,innerSE,p,holeRenderer,rc);
        generateOuterGround(outerSE,outerSW,innerSE,innerSW,p,holeRenderer,rc);
        generateOuterGround(outerSW,outerNW,innerSW,innerNW,p,holeRenderer,rc);
        
    }
    
    static void generateOuterGround(Geodetic3D outerStart, Geodetic3D outerEnd,
                                            Geodetic3D innerStart, Geodetic3D innerEnd, const Planet *p, MeshRenderer *holeRenderer,const G3MRenderContext *rc ){
        FloatBufferBuilderFromGeodetic *fbb =
        FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(p);
        
        fbb->add(innerStart);
        fbb->add(innerEnd);
        fbb->add(outerEnd);
        fbb->add(outerStart);
        fbb->add(innerStart);
        
        Mesh *mesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                   true,
                                   fbb->getCenter(),
                                   fbb->create(),
                                   lineWidth,
                                   pointWidth,
                                   new Color(*coverColor),//color,
                                   NULL, 0.0f, true);
        
        if (coverImage != NULL){
            const std::string texName = "HOLETEXTURE_" + outerStart.description() + outerEnd.description();
            const TextureIDReference *texId = rc->getTexturesHandler()->getTextureIDReference(coverImage, GLFormat::rgba(),texName,false);
            FloatBufferBuilderFromCartesian2D texCoords;
            texCoords.add(0,0);
            texCoords.add(0,1);
            texCoords.add(1,1);
            texCoords.add(1,0);
            
            TextureMapping *tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
            TexturedMesh *tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
            holeRenderer->addMesh(tMesh);
        }
        else {
            holeRenderer->addMesh(mesh);
        }
        
        delete fbb;
    }
};

Color * HoleCoverHelper::groundColor, *HoleCoverHelper::wallColor, *HoleCoverHelper::coverColor;
float HoleCoverHelper::lineWidth, HoleCoverHelper::pointWidth;
IImage *HoleCoverHelper::coverImage, *HoleCoverHelper::holeImage;


class changeHoleTask : public GTask {
    
    ViewController *vc;
    bool enable;

public:
    changeHoleTask(ViewController *viewController, bool isEnabled):
    vc(viewController),
    enable(isEnabled){}
    
    void run(const G3MContext *context){
        vc.pipeMeshRenderer->clearMeshes();
        if (!enable){
            [vc changeHole:Geodetic3D::fromDegrees(1,1,0)];
        }
        [vc addPipeMeshes];
        
    }
    
    ~changeHoleTask(){
        vc = nil;
    }
    
    
    
};

class changeDitchTask : public GTask {
    
    ViewController *vc;
    bool enable;
    
public:
    changeDitchTask(ViewController *viewController, bool isEnabled):
    vc(viewController),
    enable(isEnabled){}
    
    void run(const G3MContext *context){
        vc.pipeMeshRenderer->clearMeshes();
        [vc addPipeMeshes];
    }
    
    ~changeDitchTask(){
        vc = nil;
    }
};

class changeFirstEDPTask : public GTask{
    
    ViewController *vc;
    Sector holeSector;
    ElevationDataProvider *holeEDP;
    
public:
    
    changeFirstEDPTask(ViewController *viewController,const Sector sector, ElevationDataProvider *edp):
    vc(viewController),
    holeSector(sector),
    holeEDP(edp){}
    
    
    void run (const G3MContext *context){
        //vc.holeElevData->setSector(holeSector);
        vc.combo->changeFirstEDP(holeEDP);
    }
    
    ~changeFirstEDPTask(){
        vc = nil;
    }
};

class PointCloudEvolutionTask: public GTask{
  
  PointCloudMesh* _pcMesh;
  
  float _delta;
  int _step;
  ViewController* _vc;
  
  ColorLegend* _colorLegend;
  bool _using0Color;
  public:
  
  PointCloudEvolutionTask(ViewController* vc):
  _pcMesh(NULL),
  _delta(0.0),
  _step(0),
  _vc(vc),
  _colorLegend(NULL),
  _using0Color(false)
  {
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::black(), 0)); //Min
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(51, 31, 0, 255), 6)); //Percentile 10 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(128, 77, 0, 255), 21)); //Percentile 25 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(255, 153, 0, 255), 75)); //Mean (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(255, 204, 128, 255), 100)); //Percentile 75 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::white(), 806.0)); //Max
    _colorLegend = new ColorLegend(legend);
  }
  
  ~PointCloudEvolutionTask(){
    delete _colorLegend;
    
    [_vc removePointCloudMesh];
  }
  
  void run(const G3MContext* context){
    
    _step++;
    
    //#warning REMOVE DEMOSNTRATION FOR SCREENSHOTS
    //    _step = 59;
    
    NSString* folder = @"EIFER Resources/Solar Radiation/buildings_imp_table_0";
    
    if (_pcMesh == NULL){
      
      NSString *filePath = [[NSBundle mainBundle] pathForResource:@"vertices" ofType:@"csv" inDirectory:folder];
      if (filePath) {
        NSString *myText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
        
        _pcMesh = (PointCloudMesh*) BuildingDataParser::createSolarRadiationMeshFromCSV([myText UTF8String],
                                                                                        [[_vc G3MWidget] widget]->getG3MContext()->getPlanet(),
                                                                                        [_vc elevationData],
                                                                                        *_colorLegend);
        [_vc addPointCloudMesh:_pcMesh];
      }
      
    }
    
    if (_pcMesh != NULL){
      NSString* fileColors = [NSString stringWithFormat:@"values_t%d", _step, nil];
      NSString *filePath = [[NSBundle mainBundle] pathForResource:fileColors ofType:@"csv" inDirectory:folder];
      if (filePath) {
        NSString *myText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
        
        IFloatBuffer* colors = BuildingDataParser::createColorsForSolarRadiationMeshFromCSV([myText UTF8String], *_colorLegend);
        
        
        _pcMesh->changeToColors(colors);
        _using0Color = false;
      } else{
        if (!_using0Color){
          IFloatBuffer* colors = BuildingDataParser::create0ColorsForSolarRadiationMeshFromCSV(*_colorLegend, (int) _pcMesh->getVertexCount());
          _pcMesh->changeToColors(colors);
          _using0Color = true;
        }
      }
      
      int hour = _step;
      
      //Label
      std::string s = "Day " + context->getStringUtils()->toString(hour / 24) + " " +
      context->getStringUtils()->toString(hour % 24) + ":00";
      
      [_vc.timeLabel setHidden:FALSE];
      
      [[_vc timeLabel] setText:[NSString stringWithUTF8String:s.c_str()]];
    }
    
  }
  
};

class ColorChangingMeshTask: public GTask{
  
  AbstractMesh* _abstractMesh;
  int _step;
  std::vector<IFloatBuffer*> _colors;
  public:
  
  ColorChangingMeshTask(AbstractMesh* abstractMesh, std::vector<IFloatBuffer*> colors):
  _abstractMesh(abstractMesh),
  _step(0),
  _colors(colors)
  {
    IFloatBuffer* meshColors = _abstractMesh->getColorsFloatBuffer();
    
    for (size_t i = 0; i < _colors.size(); i++) {
      if (colors[i]->size() != meshColors->size()){
        THROW_EXCEPTION("WRONG NUMBER OF COLORS");
      }
    }
  }
  
  void run(const G3MContext* context){
    IFloatBuffer* colors = _abstractMesh->getColorsFloatBuffer();
    IFloatBuffer* newColors = _colors[_step];
    
    colors->put(0, newColors);
    
    _step++; //Advance
  }
  
};

class MyEDCamConstrainer: public ICameraConstrainer {
  
  ElevationData* _ed;
  __weak ViewController *vc;
  bool _shouldCaptureMotion;
  MeshRenderer * _mr;
  std::string &_lastText;
  Mark * positionMark;
  static double markHeading;
    
  public:
    
  
    MyEDCamConstrainer(ElevationData* ed,std::string &text,ViewController *viewController):
    _ed(ed),
    _shouldCaptureMotion(false),
    _mr(NULL),
    _lastText(text),
    positionMark(NULL),
    vc(viewController){}
  
  void setED(ElevationData* ed){
    _ed = ed;
  }
    
  void setMark (Mark *mark){
    positionMark = mark;
  }
    
  Angle getMarkHeading(){
      if (markHeading == -1000){
          return Angle::nan();
      }
      return Angle::fromDegrees(markHeading);
  }
    

    
#warning Unfortunately Chano was here!
  void shouldCaptureMotion(bool capture, MeshRenderer *meshRenderer){
    _shouldCaptureMotion = capture;
    _mr = meshRenderer;
  }
  
  //Returns false if it could not create a valid nextCamera
  virtual bool onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const{
    
    if (previousCamera->computeZNear() < 5){
      //We are using VR
        
        const std::string text = Cylinder::adaptMeshes(_mr,
                                                       &PipesModel::cylinderInfo,
                                                       nextCamera,
                                                       planet);
        
        
        if (text.compare("") != 0 && text.compare(_lastText) != 0){
            _lastText = text;
        }
      return true;
    }
    if (_ed != NULL){
      Geodetic3D g = nextCamera->getGeodeticPosition();
      Geodetic2D g2D = g.asGeodetic2D();
      if (_ed->getSector().contains(g2D)){
        double d = _ed->getElevationAt(g2D);
        const double limit = d + 1.1 * nextCamera->computeZNear();
        
        if (g._height < limit){
          nextCamera->copyFrom(*previousCamera, false);
        }
      }
    }
    if (_shouldCaptureMotion && !nextCamera->getGeodeticPosition().isEquals(previousCamera->getGeodeticPosition())){
        
        const std::string text = Cylinder::adaptMeshes(_mr,
                                                 &PipesModel::cylinderInfo,
                                                 nextCamera,
                                                 planet);
        
        
        if (text.compare("") != 0 && text.compare(_lastText) != 0){
            _lastText = text;
        }

    }
    if (positionMark != NULL){
          Geodetic2D nAsG2D = nextCamera->getGeodeticPosition().asGeodetic2D();
           double hgt = (_ed != NULL)? _ed->getElevationAt(nAsG2D) : 0;
          Geodetic3D mg = Geodetic3D(nAsG2D,hgt);
          positionMark->setPosition(mg);
          markHeading = nextCamera->getHeading()._degrees;
#warning Do something at this point ...
        NSString *string = [NSString stringWithFormat:@"Lat: %.8f, Lon: %.8f \nHgt: %.2f, Heading: %.2f",
                            mg._latitude._degrees,mg._longitude._degrees,mg._height,markHeading];
        [vc updatePositionFixer:string];
    }
      
    return true;
  }
};

double MyEDCamConstrainer::markHeading = -1000;

class MyEDListener: public IElevationDataListener{
  
  
  private:
  ViewController* _demo;
  const IThreadUtils* _threadUtils;
  
  public:
  
  MyEDListener(ViewController* demo, const IThreadUtils* threadUtils):_demo(demo), _threadUtils(threadUtils){}
  
  virtual void onData(const Sector& sector,
                      const Vector2I& extent,
                      ElevationData* elevationData){
    
    _demo.cityGMLRenderer->setElevationData(elevationData);
    
    [_demo camConstrainer]->setED(elevationData);
    
    [_demo setElevationData:elevationData];
    
    [_demo loadCityModel];
  }
  
  virtual void onError(const Sector& sector,
                       const Vector2I& extent){
    
  }
  
  virtual void onCancel(const Sector& sector,
                        const Vector2I& extent){
    
  }
};

class MyCityGMLBuildingTouchedListener : public CityGMLBuildingTouchedListener{
  ViewController* _vc;
  public:
  
  MyCityGMLBuildingTouchedListener(ViewController* vc):_vc(vc){}
  
  virtual ~MyCityGMLBuildingTouchedListener(){}
  virtual void onBuildingTouched(CityGMLBuilding* building){
    std::string name = "ID: " + building->_name + "\n" + building->getPropertiesDescription();
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Building selected"
                                  message:[NSString stringWithUTF8String:name.c_str()]
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* yesButton = [UIAlertAction
                                actionWithTitle:@"Ok"
                                style:UIAlertActionStyleDefault
                                handler:^(UIAlertAction * action)
                                {
                                  //Handel your yes please button action here
                                  
                                }];
    
    [alert addAction:yesButton];
    
    
    
    UIAlertAction* SRButton = [UIAlertAction
                               actionWithTitle:@"Show Solar Radiation Data"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                 [_vc loadSolarRadiationPointCloudForBuilding:building];
                               }];
    
    [alert addAction:SRButton];
    
    [_vc presentViewController:alert animated:YES completion:nil];
  }
  
};



class MyInitTask: public GInitializationTask{
  bool _useDEM;
  ViewController* _vc;
  public:
  
  MyInitTask(ViewController* vc, bool useDEM):_useDEM(useDEM), _vc(vc){
    
  }
  
  void run(const G3MContext* context){
    if (_useDEM){
        HoleCoverHelper::loadHoleImage([_vc.G3MWidget widget]->getG3MContext()->getDownloader(),
                                      URL("file:///holetexture.jpg",false));
        HoleCoverHelper::loadCoverImage([_vc.G3MWidget widget]->getG3MContext()->getDownloader(),
                                       URL("file:///covertexture3.jpg",false));
      Geodetic3D pipeCenter = Geodetic3D::fromDegrees(1,1,0); //Geodetic3D.fromDegrees(49.01664816, 8.43442120,0);
      const Planet *p = [_vc.G3MWidget widget]->getG3MContext()->getPlanet();
      Vector3D v1= p->toCartesian(pipeCenter);
      Vector3D v2 = Vector3D(v1._x - 10, v1._y - 10, v1._z);
      Vector3D v3 = Vector3D(v1._x + 10, v1._y + 10, v1._z);
      Geodetic3D l = p->toGeodetic3D(v2);
      Geodetic3D u = p->toGeodetic3D(v3);
      Geodetic2D lower = Geodetic2D::fromDegrees(fmin(l._latitude._degrees, u._latitude._degrees),
                                                 fmin(l._longitude._degrees, u._longitude._degrees));
      Geodetic2D upper = Geodetic2D::fromDegrees(fmax(l._latitude._degrees, u._latitude._degrees),
                                                 fmax(l._longitude._degrees, u._longitude._degrees));
        
      Sector holeSector = Sector(lower,upper);
      SingleBilElevationDataProvider *holeEdp = new SingleBilElevationDataProvider(URL("file:///hole3.bil"),
                                                                                    holeSector,
                                                                                    Vector2I(11, 11));
        
      holeEdp->requestElevationData(holeSector, Vector2I(11, 11),new MyHoleListener(_vc), true);
        
      Sector karlsruheSector = Sector::fromDegrees(48.9397891179, 8.27643508429, 49.0930546874, 8.5431344933);
      SingleBilElevationDataProvider* edp = new SingleBilElevationDataProvider(URL("file:///ka_31467.bil"),
                                                                               karlsruheSector,
                                                                               Vector2I(308, 177));
        
      _vc.combo = new CompositeElevationDataProvider();
      _vc.combo->addElevationDataProvider(holeEdp);
      _vc.combo->addElevationDataProvider(edp);
      [_vc.G3MWidget widget]->getPlanetRenderer()->setElevationDataProvider(_vc.combo,true);
      
      edp->requestElevationData(karlsruheSector, Vector2I(308, 177), new MyEDListener(_vc, context->getThreadUtils()), true);
      [_vc.G3MWidget widget]->getPlanetRenderer()->addTerrainTouchListener(new HoleTouchListener(_vc));
    } else{
      [_vc loadCityModel];
    }
      
  }
  
  bool isDone(const G3MContext* context){
    return true;
  }
  
};

class MyCityGMLRendererListener: public CityGMLRendererListener{
  ViewController* _vc;
  public:
  MyCityGMLRendererListener(ViewController* vc):_vc(vc){}
  
  void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings){
    [_vc onCityModelLoaded];
    
#pragma mark UNCOMMENT TO SAVE MEMORY
    //Decreasing consumed memory
    for (size_t i = 0; i < buildings.size(); i++) {
      buildings[i]->removeSurfaceData();
    }
  }
  
};

class KarlsruheVirtualWalkLM: public ILocationModifier{
  const ElevationData* _ed;
  const Geodetic2D* _initialPosition;
  
  // compute what I have walked from initial position
  const Geodetic2D _karlsruhePos;
  const double _viewHeight;
  
  public:
  KarlsruheVirtualWalkLM(const ElevationData* ed):
  _ed(ed),
  _initialPosition(NULL),
  _karlsruhePos(Angle::fromDegrees(49.011059), Angle::fromDegrees(8.404109)),
  _viewHeight(2.0)
  {}
  
  ~KarlsruheVirtualWalkLM() {
    if (_initialPosition != NULL)
    delete _initialPosition;
  }
  
  Geodetic3D modify(const Geodetic3D& location){
    // code to virtually walk in Karlsruhe
    
    // the first time save GPS position
    if (_initialPosition == NULL){
      _initialPosition = new Geodetic2D(location._latitude, location._longitude);
    }
    
    // compute what I have walked from initial position
    const Geodetic2D incGeo = location.asGeodetic2D().sub(*_initialPosition);
    
    Geodetic2D fakePos = _karlsruhePos.add(incGeo.times(100));
    const double h = _ed->getElevationAt(fakePos);
    
    
    return Geodetic3D(fakePos, h + _viewHeight);
  }
};


class CorrectedAltitudeFixedLM: public ILocationModifier {
    const ElevationData *_ed;
    Geodetic2D *_initialPosition;
    Geodetic2D _markPosition;
    double _viewHeight;
    
    public :
    CorrectedAltitudeFixedLM(const ElevationData *ed, Geodetic2D markPosition):
    _ed(ed),
    _markPosition(markPosition),
    _viewHeight(2.0),
    _initialPosition(NULL){}
    
    
    ~CorrectedAltitudeFixedLM(){
        if (_initialPosition != NULL)
            delete _initialPosition;
    }
    
    Geodetic3D modify(const Geodetic3D &location){
        if (_initialPosition == NULL){
            _initialPosition = new Geodetic2D(location._latitude, location._longitude);
        }
        
        // compute what I have walked from initial position
        Geodetic2D incGeo = location.asGeodetic2D().sub(*_initialPosition);
        
        Geodetic2D fakePos = _markPosition.add(incGeo);
        double h = _ed->getElevationAt(fakePos);
        if (isnan(h)) {
            h = 0;
        }
        
        return Geodetic3D(fakePos, h + _viewHeight);
    }
    
};


class AltitudeFixerLM: public ILocationModifier{
  const ElevationData* _ed;
  const Geodetic2D* _initialPosition;
  public:
  AltitudeFixerLM(const ElevationData* ed):
  _ed(ed),
  _initialPosition(NULL)
  {}
  
  ~AltitudeFixerLM() {
    if (_initialPosition != NULL)
    delete _initialPosition;
  }
  
  Geodetic3D modify(const Geodetic3D& location){
    const double heightDEM = _ed->getElevationAt(location._latitude, location._longitude);
    if (location._height < heightDEM + 1.6){
      return Geodetic3D::fromDegrees(location._latitude._degrees,
                                     location._longitude._degrees,
                                     heightDEM + 1.6);
    }
    
    if (location._height > heightDEM + 25){
      return Geodetic3D::fromDegrees(location._latitude._degrees,
                                     location._longitude._degrees,
                                     heightDEM + 25);
    }
    
    return location;
  }
};

///////////////////

@implementation ViewController{
    std::vector<Cylinder::CylinderMeshInfo> cylinderInfo;
    std::string lastText;
    __weak IBOutlet UIImageView *eiferLogo;
    
    bool pipes,buildings,correction;
    int alpha,color,mode,method;
    
    Mark *positionMark;
}

@synthesize G3MWidget;
@synthesize meshRenderer;
@synthesize pipeMeshRenderer;
@synthesize holeRenderer;
@synthesize marksRenderer;
@synthesize falseMarksRenderer;
@synthesize meshRendererPC;
@synthesize pipesRenderer;
@synthesize cityGMLRenderer;
@synthesize elevationData;
@synthesize combo;
@synthesize holeElevData;
@synthesize timeLabel;
@synthesize camConstrainer;
@synthesize vectorLayer;
@synthesize shapesRenderer;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

-(void) addCityGMLFile:(const std::string&) fileName needsToBeFixOnGround:(BOOL) fix{
  CityGMLModelFile m;
  m._fileName = fileName;
  m._needsToBeFixedOnGround = fix;
  _cityGMLFiles.push_back(m);
}

- (void)viewDidLoad
{
  [super viewDidLoad];
  //Previous considerations
  pipes = true;
  buildings = true;
  correction = false;
  mode = 0;
  alpha = 1; //Now it is not Alpha/Hole, but Alpha/Ditch. Ditch gains preference ...
  method = 1;
  color = 0;
  
  elevationData = NULL;
  meshRenderer = NULL;
  pipeMeshRenderer = NULL;
  marksRenderer = NULL;
  holeRenderer = NULL;
    
  UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapDetected)];
  [eiferLogo setUserInteractionEnabled:YES];
  [eiferLogo addGestureRecognizer:singleTap];
  
  //VR;
  _prevPos = NULL;
  _prevHeading = NULL;
  _prevRoll = NULL;
  _prevPitch = NULL;
  
  _isMenuAvailable = false;
  
  _locationUsesRealGPS = true;
  _dac = NULL;
  
  _waitingMessageView.layer.cornerRadius = 5;
  _waitingMessageView.layer.masksToBounds = TRUE;
  
  G3MWidget.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleBottomMargin |
  UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin |
  UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
  
  [timeLabel setHidden:TRUE];
  
  _pickerArray = @[@"Random Colors", @"Heat Demand", @"Building Volume", @"GHG Emissions", @"Demographic Clusters (SOM)", @"Demographic Clusters (k-means)"];
    
    /*[self addCityGMLFile:"file:///building-1.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-2.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-12.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-15.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-16.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-17.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-18.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-20.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-25.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-26.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-28.gml" needsToBeFixOnGround:true];
    [self addCityGMLFile:"file:///building-29.gml" needsToBeFixOnGround:true];*/
  
 [self addCityGMLFile:"file:///innenstadt_ost_4326_lod2.gml" needsToBeFixOnGround:false];
//  [self addCityGMLFile:"file:///innenstadt_west_4326_lod2.gml" needsToBeFixOnGround:false];
//  // [self addCityGMLFile:"file:///technologiepark_WGS84.gml" needsToBeFixOnGround:true];
//  [self addCityGMLFile:"file:///AR_demo_with_buildings.gml" needsToBeFixOnGround:true]; //NOT WORKING
//  [self addCityGMLFile:"file:///hagsfeld_4326_lod2.gml" needsToBeFixOnGround:false];
//  [self addCityGMLFile:"file:///durlach_4326_lod2_PART_1.gml" needsToBeFixOnGround:false];
//  [self addCityGMLFile:"file:///durlach_4326_lod2_PART_2.gml" needsToBeFixOnGround:false];
//  [self addCityGMLFile:"file:///hohenwettersbach_4326_lod2.gml" needsToBeFixOnGround:false];
//        [self addCityGMLFile:"file:///bulach_4326_lod2.gml" needsToBeFixOnGround:false];
//        [self addCityGMLFile:"file:///daxlanden_4326_lod2.gml" needsToBeFixOnGround:false];
//        [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_1.gml" needsToBeFixOnGround:false];
//        [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_2.gml" needsToBeFixOnGround:false];
//        [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_3.gml" needsToBeFixOnGround:false];
    //[self addCityGMLFile:"file:///tpk_lod3_t1_wgs84.gml" needsToBeFixOnGround:true];

  
  _modelsLoadedCounter = 0;
  
  [_progressBar setProgress:0.0f];
  
  _useDem = true;
  [self initEIFERG3m:_useDem];
  
  //HIDING MENU
  [self showMenuButtonPressed:_showMenuButton];
  
  
  [[self G3MWidget] startAnimation];
  
  //Las Palmas de G.C.
  [G3MWidget widget]->setCameraPosition(Geodetic3D::fromDegrees(27.995258816253532075, -15.431324237687769951, 19995.736280026820168));
  [G3MWidget widget]->setCameraPitch(Angle::fromDegrees(-53.461659));
  
  
}

- (int) getMapMode{
    return mode;
}

- (void) tapDetected{
    if (_isMenuAvailable ) {
    PopupViewController *popupController = [[self storyboard] instantiateViewControllerWithIdentifier:@"popupViewController"];
        [popupController setCurrentStateWithMode:mode Color:color Alpha:alpha Method:method Buildings:buildings Pipes:pipes Correction:correction];
    [self presentViewController:popupController animated:YES completion:nil];
    }
}

-(void) loadSolarRadiationPointCloudForBuilding:(CityGMLBuilding*) building{
  
  if (_buildingShowingPC != NULL){
    CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color::blue());
  }
  
  _buildingShowingPC = building;
  CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color::transparent());
  
  if (_pointCloudTask != NULL){
    [G3MWidget widget]->removeAllPeriodicalTasks();
  }
  _pointCloudTask = new PointCloudEvolutionTask(self);
  
  [G3MWidget widget]->addPeriodicalTask(new PeriodicalTask(TimeInterval::fromSeconds(0.1),
                                                           new PointCloudEvolutionTask(self)));
}

-(void) addPointCloudMesh:(Mesh*) pc{
  meshRendererPC->addMesh(pc);
}

-(void) removePointCloudMesh{
  meshRendererPC->clearMeshes();
  [timeLabel setHidden:TRUE];
}

-(void) useOSM:(BOOL) v{
  layerSet->removeAllLayers(true);
  if (v){
    OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
    layerSet->addLayer(layer);
  } else{
    BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                             "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                             TimeInterval::fromDays(30),
                                             true,
                                             2,
                                             19);
    layerSet->addLayer(layer);
  }
  
}


- (void)initEIFERG3m:(BOOL) useDEM
{
  
  G3MBuilder_iOS builder([self G3MWidget]);
  layerSet = new LayerSet();
  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  
  layerSet->addLayer(layer);
  
  _planet = SphericalPlanet::createEarth();
  builder.setPlanet(_planet);
  
  _hudRenderer = new HUDRenderer();
  
  builder.addRenderer(_hudRenderer);
  
  meshRenderer = new MeshRenderer();
  meshRendererPC = new MeshRenderer();
  pipeMeshRenderer = new MeshRenderer();
    
  pipesRenderer = new PipesRenderer(pipeMeshRenderer,self);
  pipesRenderer->setHoleMode([self isHole]);
  pipesRenderer->setTouchListener(new PipeListener());
    
    
    
  holeRenderer = new MeshRenderer();
  builder.addRenderer(meshRendererPC);
//  builder.addRenderer(pipesRenderer);
  builder.addRenderer(holeRenderer);
  marksRenderer = new MarksRenderer(false);
  builder.addRenderer(marksRenderer);
  positionMark = new Mark(URL("file:///bolita.png",false),
                          Geodetic3D::fromDegrees(28,-15.50,0),
                          AltitudeMode::ABSOLUTE);
  marksRenderer->addMark(positionMark);
  marksRenderer->setEnable(false);
  falseMarksRenderer = new MarksRenderer(false);
  builder.addRenderer(falseMarksRenderer);
  
  //Karlsruhe Schloss model
  shapesRenderer = new ShapesRenderer();
  builder.addRenderer(shapesRenderer);
  
  //Showing Footprints
  vectorLayer = new GEOVectorLayer(2,18,
                                   0,18,
                                   1.0f,
                                   new LevelTileCondition(17, 18));
  layerSet->addLayer(vectorLayer);
  
  cityGMLRenderer = new CityGMLRenderer(meshRenderer,
                                        NULL /* marksRenderer */,
                                        vectorLayer);
  
  cityGMLRenderer->setTouchListener(new MyCityGMLBuildingTouchedListener(self));
  
  //builder.addRenderer(cityGMLRenderer);
  
  builder.setInitializationTask(new MyInitTask(self, useDEM));
  
  camConstrainer = new MyEDCamConstrainer(NULL,lastText,self); //Wait for ED to arrive
  builder.addCameraConstraint(camConstrainer);
  
  builder.setBackgroundColor(new Color(Color::fromRGBA255(0, 0, 0, 0)));
  
  
//  //Cylinder Test
//  Vector3D s = _planet->toCartesian(Geodetic3D::fromDegrees(0, 0, 0));
//  Vector3D e = _planet->toCartesian(Geodetic3D::fromDegrees(0, 0, 1e5));
//  Cylinder cyl(s,e, 1.0e4);
//  meshRenderer->addMesh(cyl.createMesh(Color::red(), 20));

  
  builder.initializeWidget();
    
    
    CompositeRenderer* cr = new CompositeRenderer();
    cr->addRenderer(pipesRenderer);
    cr->addRenderer(cityGMLRenderer);
    
    [self.G3MWidget widget]->setSecondPassRenderer(cr);
}

-(void) onCityModelLoaded{
  _modelsLoadedCounter++;
  if (_modelsLoadedCounter == _cityGMLFiles.size()){
    cityGMLRenderer->addBuildingDataFromURL(URL("file:///karlsruhe_data.geojson"));
  }
  [self onProgress];
}

-(void) loadCityModel{
    if ((_useDem && holeElevData != NULL && elevationData != NULL) || (!_useDem)) {
        
        for (size_t i = 0; i < _cityGMLFiles.size(); i++) {
            cityGMLRenderer->addBuildingsFromURL(URL(_cityGMLFiles[i]._fileName),
                                                 _cityGMLFiles[i]._needsToBeFixedOnGround,
                                                 new MyCityGMLRendererListener(self),
                                                 true);
        }
        
        [self addPipeMeshes];
    }
    
  
  
}

- (void) addPipeMeshes{
    PipesModel::reset();
    UtilityNetworkParser::initialize([G3MWidget widget]->getG3MContext(),elevationData,pipeMeshRenderer,-4.0);
    URL url = URL("file:///jochen_underground.gml");
    UtilityNetworkParser::parseFromURL(url);
    
    #warning Si hiciera falta colocar las otras tuberias, irian aqui ....
}

-(void) onProgress {
  //N MODELS + 1 POINT CLOUD
  float p = (float)(_modelsLoadedCounter) / ((float)_cityGMLFiles.size());
  [_progressBar setProgress: p animated:TRUE];
  
  if (p == 1){
    [self onAllDataLoaded];
  }
}

-(void) onAllDataLoaded{
  ILogger::instance()->logInfo("City Model Loaded");
  
  const bool includeCastleModel = true;
  const bool TPKAsShapes = true;
  if (includeCastleModel){
    class SchlossListener : public ShapeLoadListener {
      public:
      SchlossListener()
      {
      }
      
      void onBeforeAddShape(SGShape* shape) {
        shape->setPitch(Angle::fromDegrees(90));
      }
      
      void onAfterAddShape(SGShape* shape) {
        shape->setScale(250);
        //      shape->setHeading(Angle::fromDegrees(-4));
      }
    };
      
    class MeteoListener : public ShapeLoadListener {
      public:
          MeteoListener()
          {
          }
          
          void onBeforeAddShape(SGShape* shape) {
//              shape->setPitch(Angle::fromDegrees(90));
          }
          
          void onAfterAddShape(SGShape* shape) {
              shape->setScale(5);
              shape->setPitch(Angle::fromDegrees(90));
              //      shape->setHeading(Angle::fromDegrees(-4));
          }
      };
      
      class RandomListener : public MarkTouchListener {
      public:
          bool touchedMark(Mark * mark){
              UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"SENSOR TOUCHED"
                                                              message:@"Information about the sensor can be displayed instead of this screen."
                                                             delegate:nil
                                                    cancelButtonTitle:@"OK"
                                                    otherButtonTitles:nil];
              [alert show];
              return true;
          }
      };
    
    shapesRenderer->loadJSONSceneJS(URL("file:///k_s/k_schloss.json"),
                                    "file:///k_s/",
                                    false, true, // isTransparent
                                    new Geodetic3D(Geodetic3D::fromDegrees(49.013500, 8.404249, 117.82)), //
                                    ABSOLUTE,
                                    new SchlossListener());
      
      
      shapesRenderer->loadJSONSceneJS(URL("file:///boyas.json"),
                                     "file:///k_s/",
                                     false, true,
                                     new Geodetic3D(Geodetic3D::fromDegrees(49.01098659,8.40410182,121.91)),
                                      AltitudeMode::ABSOLUTE,
                                     new MeteoListener());
      
      falseMarksRenderer->addMark(new Mark(
                                          URL("file:///transparent2.png",false),
                                           Geodetic3D::fromDegrees(49.01098659,8.40410182,0),
                                           AltitudeMode::RELATIVE_TO_GROUND,
                                          1000,
                                          NULL,
                                          true,
                                          new RandomListener(),
                                          true));
    
    std::string v[] = {"91214493", "23638639", "15526553", "15526562", "15526550", "13956101", "156061723", "15526578"};
    
    for (int i = 0; i < 8; i++){
      CityGMLBuilding* b = cityGMLRenderer->getBuildingWithName(v[i]);
      if (b != NULL){
        CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(b, Color::transparent());
      }
    }
    
    
  }
    if (TPKAsShapes){
        [self loadTPKShapes];
    }
  //Whole city!
  [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                Geodetic3D::fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                                                Angle::fromDegrees(-109.452892),
                                                Angle::fromDegrees(-44.938813)
                                                );
  
/*
//METZ
  [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                Geodetic3D::fromDegrees(49.1034419341, 6.2225732157,  100.0),//1000
                                                Angle::fromDegrees(0.0),//0
                                                Angle::fromDegrees(-15.0) //-90
                                                );
 */
  
  
  [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(activateMenu) userInfo:nil repeats:FALSE];
  
  //NO WAITING ANYMORE
  _waitingMessageView.hidden = TRUE;
  
    printf("N OF WALLS: %d\n", Surface::numberOfWalls);
    printf("N OF TESSELLATED WALLS: %d\n", Polygon3D::numberOfP3D);
    printf("N OF TESSELLATED WALLS_4: %d\n", Polygon3D::numberOfP3D_4);
}

- (void) loadTPKShapes{
    class TPK1LoadListener : public ShapeLoadListener {
        
        //double _amp;
        
        
    public:
        TPK1LoadListener()
        {
            //_amp = amp;
        }
        
        void onBeforeAddShape(SGShape* shape) {
            shape->setPitch(Angle::fromDegrees(90));
            /*shape->setHeading(Angle::fromDegrees(85));
             shape->setScale(1.5, 2.5, 1);*/
        }
        
        void onAfterAddShape(SGShape* shape) {
            shape->setTranslation(55,0, -90);
        }
    };
    
    class TPK2LoadListener : public ShapeLoadListener {
        
        //double _amp;
        
        
    public:
        TPK2LoadListener()
        {
            //_amp = amp;
        }
        
        void onBeforeAddShape(SGShape* shape) {
            shape->setPitch(Angle::fromDegrees(90));
            /*shape->setHeading(Angle::fromDegrees(85));
             shape->setScale(1.5, 2.5, 1);*/
        }
        
        void onAfterAddShape(SGShape* shape) {
            shape->setTranslation(55,0,-90);
        }
    };
    
    class TPK3LoadListener : public ShapeLoadListener {
        
        //double _amp;
        
        
    public:
        TPK3LoadListener()
        {
            //_amp = amp;
        }
        
        void onBeforeAddShape(SGShape* shape) {
            shape->setPitch(Angle::fromDegrees(90));
            /*shape->setHeading(Angle::fromDegrees(85));
             shape->setScale(1.5, 2.5, 1);*/
        }
        
        void onAfterAddShape(SGShape* shape) {
            shape->setTranslation(55 ,0, -90);
        }
    };
    
    
    double lat = 49.02004116;
    double lon = 8.43959427;
    double hgt = (elevationData != NULL) ? elevationData->getElevationAt(Geodetic2D::fromDegrees(lat, lon)) : 0;
    
    shapesRenderer->loadJSONSceneJS(URL("file:///tpk1.json",false),
                                    "file:///",
                                    true,true,
                                    new Geodetic3D(Geodetic3D::fromDegrees(lat,lon,hgt)),
                                    AltitudeMode::ABSOLUTE,
                                    new TPK1LoadListener());
    shapesRenderer->loadJSONSceneJS(URL("file:///tpk2.json",false),
                                    "file:///",
                                    true,true,
                                    new Geodetic3D(Geodetic3D::fromDegrees(lat,lon,hgt)),
                                    AltitudeMode::ABSOLUTE,
                                    new TPK2LoadListener());
    shapesRenderer->loadJSONSceneJS(URL("file:///tpk3.json",false),
                                    "file:///",
                                    true,true,
                                    new Geodetic3D(Geodetic3D::fromDegrees(lat,lon,hgt)),
                                    AltitudeMode::ABSOLUTE,
                                    new TPK3LoadListener());
}


-(void) activateMenu{
  _isMenuAvailable = true;
  printf("Menu activated");
  camConstrainer->shouldCaptureMotion(true, pipeMeshRenderer);
}

- (IBAction)switchCityGML:(id)sender {
  bool viewBuildings = [((UISwitch*) sender) isOn];
    
  [self setBuildingsActive:viewBuildings];
}

- (void) setAlphaMethod:(int) alphaMethod{
    Cylinder::setDistanceMethod(alphaMethod+1);
    method = alphaMethod;
}



- (void) changeHole:(Geodetic3D) position{
    const Planet *planet = [G3MWidget widget]->getG3MContext()->getPlanet();
    const IThreadUtils *_tUtils = [G3MWidget widget]->getG3MContext()->getThreadUtils();
    const G3MRenderContext *rc = [G3MWidget widget]->getG3MRenderContext();
    
    Vector3D v1 = planet->toCartesian(position);
    Vector3D v2 = Vector3D(v1._x - 10, v1._y - 10, v1._z);
    Vector3D v3 = Vector3D(v1._x + 10, v1._y + 10, v1._z);
    Geodetic3D l = planet->toGeodetic3D(v2);
    Geodetic3D u = planet->toGeodetic3D(v3);
    Geodetic2D lower = Geodetic2D::fromDegrees(
                                              fmin(l._latitude._degrees, u._latitude._degrees),
                                              fmin(l._longitude._degrees, u._longitude._degrees));
    Geodetic2D upper = Geodetic2D::fromDegrees(
                                              fmax(l._latitude._degrees, u._latitude._degrees),
                                              fmax(l._longitude._degrees, u._longitude._degrees));
    Sector holeSector = Sector(lower,upper);
    HoleCoverHelper::generateHoleCover(holeSector, v1, planet,rc, elevationData, holeRenderer);
    
    ILogger::instance()->logError(holeSector.description());
    SingleBilElevationDataProvider *holeEdp = new SingleBilElevationDataProvider(URL("file:///hole3.bil"),
                                                                                      holeSector,
                                                                                      Vector2I(11, 11));
    //holeEdp.requestElevationData(holeSector, new Vector2I(11, 11),new MyHoleListener(this), true);
    
    
    _tUtils->invokeInRendererThread(new changeFirstEDPTask(self,holeSector,holeEdp), true);
}

- (bool) isHole{
    return Cylinder::getDepthEnabled();
}

- (void) setHole:(bool) enable{
    if (enable != Cylinder::getDepthEnabled()){
        holeRenderer->setEnable(enable);
        pipesRenderer->setHoleMode(enable);
        Cylinder::setDepthEnabled(enable);
        [G3MWidget widget]->getG3MContext()->getThreadUtils()
            ->invokeInRendererThread(new changeHoleTask(self,enable),true);
    }
    alpha = (enable)? 1:0;
}

- (bool) isDitch{
    return Cylinder::getDitchEnabled();
}

- (void) setDitch:(bool) enable{
    if (enable != Cylinder::getDitchEnabled()){
        Cylinder::setDitchEnabled(enable);
        [G3MWidget widget]->getG3MContext()->getThreadUtils()
        ->invokeInRendererThread(new changeDitchTask(self,enable),true);
    }
    alpha = (enable)? 1:0;
}

- (void) setBuildingsActive:(bool)active{
#warning Esto desaparecerá al caer el menú viejo.
    if (active){
        cityGMLRenderer->setEnable(true);
        [_dataPicker setUserInteractionEnabled:TRUE];
        
    } else{
        cityGMLRenderer->setEnable(false);
        [_dataPicker setUserInteractionEnabled:FALSE];
    }
    buildings = active;
}

- (void) setPipesActive:(bool)active{
    //pipeMeshRenderer->setEnable(active);
    pipesRenderer->setEnable(active);
    pipes = active;
}

- (void) setWidgetAnimation:(bool)active{
    if (active)
        [G3MWidget startAnimation];
    else
        [G3MWidget stopAnimation];
}

- (IBAction)switchSolarRadiationPC:(id)sender {
  bool viewPC = [((UISwitch*) sender) isOn];
  
  meshRendererPC->setEnable(viewPC);
}

-(void) activateMapMode{
  
  [self useOSM:TRUE];
  
  _headerView.hidden = FALSE;
  
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [G3MWidget widget]->setViewMode(MONO);
  
  CameraRenderer* cameraRenderer = [G3MWidget widget]->getCameraRenderer();
  cameraRenderer->clearHandlers();
  _dac = NULL; //Clear Handlers has destroyed it
  
  //Restoring prev cam
  const Camera* cam = [G3MWidget widget]->getCurrentCamera();
  [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(2),
                                                cam->getGeodeticPosition(), *_prevPos,
                                                cam->getHeading(), *_prevHeading,
                                                cam->getPitch(), *_prevPitch);
  delete _prevPitch;
  _prevPitch = NULL;
  delete _prevHeading;
  _prevHeading = NULL;
  delete _prevRoll;
  _prevRoll = NULL;
  delete _prevPos;
  _prevPos = NULL;
  
  
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
  [G3MWidget widget]->getNextCamera()->forceZNear(NAND);
  
  [locationModeButton setHidden:TRUE];
}

-(void) activateMonoVRMode{
  
  [self useOSM:FALSE];
  
  _headerView.hidden = FALSE;
  
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [G3MWidget widget]->setViewMode(MONO);
  [_camVC enableVideo:FALSE];
  [self activateDeviceAttitudeTracking];
  
  
  [G3MWidget widget]->getNextCamera()->setFOV(Angle::nan(), Angle::nan());
  
  [locationModeButton setHidden:FALSE];
}

-(void) activateDeviceAttitudeTracking{
  
  CameraRenderer* cameraRenderer = [G3MWidget widget]->getCameraRenderer();
  cameraRenderer->clearHandlers();
  _dac = NULL; //Clear Handlers has destroyed it
  
  //Storing prev cam
  if (_prevPos == NULL){
    const Camera* cam = [G3MWidget widget]->getCurrentCamera();
    _prevPos = new Geodetic3D(cam->getGeodeticPosition());
    _prevRoll = new Angle(cam->getRoll());
    _prevPitch = new Angle(cam->getPitch());
    _prevHeading = new Angle(cam->getHeading());
  }
  
  if (_dac == NULL){
    ILocationModifier * lm = (_locationUsesRealGPS)?
      (correction && ![self getMarkHeading].isNan())?
        (ILocationModifier *) new CorrectedAltitudeFixedLM([self elevationData],[self getMarkPosition].asGeodetic2D()) :
      (ILocationModifier *) new AltitudeFixerLM([self elevationData]) :
    (ILocationModifier *) new KarlsruheVirtualWalkLM(elevationData);
    
    _dac = new DeviceAttitudeCameraHandler(true, lm);
  }
  cameraRenderer->addHandler(_dac);
  
  [G3MWidget widget]->getNextCamera()->forceZNear(0.1);
  
  //Theoretical horizontal FOV
  float hFOVDegrees = [_camVC fieldOfViewInDegrees];
  [G3MWidget widget]->getNextCamera()->setFOV(Angle::nan(), Angle::fromDegrees(hFOVDegrees));
}

-(void) activateStereoVRMode{
  
  [self useOSM:FALSE];
  
  ///_headerView.hidden = TRUE;
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [_camVC enableVideo:FALSE];
  
  [G3MWidget widget]->setViewMode(STEREO);
  [G3MWidget widget]->setInterocularDistanceForStereoView(0.03); //VR distance between eyes
  
  //Forcing orientation
  NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
  [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
  
  [self activateDeviceAttitudeTracking];
  
  [locationModeButton setHidden:FALSE];
}

-(void) activateARMode{
  
  _headerView.hidden = FALSE;
  
  [_camVC enableVideo:TRUE];
  [G3MWidget widget]->getPlanetRenderer()->setEnable(false);
  [G3MWidget widget]->setViewMode(MONO);
  [self activateDeviceAttitudeTracking];
  
  [locationModeButton setHidden:TRUE];
  [self changeLocationMode:true];
}

- (void)viewDidUnload
{
  G3MWidget = nil;
  PipesModel::reset();
  HoleCoverHelper::deleteImages();
  delete positionMark;
  [super viewDidUnload];
  // Release any retained subviews of the main view.
  // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
  [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
  [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
  [G3MWidget stopAnimation];
  [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  
  const bool usingStereo = [G3MWidget widget]->getViewMode() == STEREO;
  
  //FORCE ORTIENTATION FOR STEREO
  if (usingStereo && interfaceOrientation != UIInterfaceOrientationLandscapeLeft){
    return FALSE;
  }
  
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  } else {
    return YES;
  }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
  if ([segue.identifier isEqualToString:@"cameraViewSegue"])
  _camVC = (CameraViewController *)segue.destinationViewController;
  
}

- (void) setMode:(int)activeMode{
    switch (activeMode){
        case 0:
            [self activateMapMode];
            break;
        case 1:
            [self activateMonoVRMode];
            break;
        case 2:
            [self activateStereoVRMode];
            break;
        case 3:
            [self activateARMode];
            break;
    }
    mode = activeMode;
}

//// MENU
- (IBAction)modeChanged:(UISegmentedControl *)sender {
  
  [self setMode:(int)sender.selectedSegmentIndex];
}


- (IBAction)showMenuButtonPressed:(id)sender {
  
  if (_menuHeightConstraint.constant == 0){
    
    UIImage* image = [UIImage imageNamed:@"down"];
    [_showMenuButton setImage:image forState:UIControlStateNormal];
    
    _menuHeightConstraint.constant = - _menuView.bounds.size.height + _showMenuButton.bounds.size.height;
    
    //Gradient background
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.frame = CGRectMake(_menuView.bounds.origin.x, _menuView.bounds.origin.y,
                                _menuView.bounds.size.width*3, _menuView.bounds.size.height);
      gradient.colors = [NSArray arrayWithObjects:(id)[[UIColor clearColor] CGColor], (id)[[UIColor whiteColor] CGColor], nil];
    [_menuView.layer insertSublayer:gradient atIndex:0];
    
  } else{
    if (!_isMenuAvailable){
      return;
    }
    
    UIImage* image = [UIImage imageNamed:@"up"];
    [_showMenuButton setImage:image forState:UIControlStateNormal];
    _menuHeightConstraint.constant = 0;
  }
  
  [UIView animateWithDuration:1 animations:^{
    [self.view layoutIfNeeded];
  }];
}

- (IBAction)onChangeLocationMode:(UIButton *)sender {
  
  [self changeLocationMode:!_locationUsesRealGPS];
  
  if (_locationUsesRealGPS){
    [sender setImage:[UIImage imageNamed:@"plane_off"] forState:UIControlStateNormal];
  }else{
    [sender setImage:[UIImage imageNamed:@"plane_on"] forState:UIControlStateNormal];
  }
}

-(void) changeLocationMode:(BOOL) v{
  _locationUsesRealGPS = v;
    
    if (_dac == NULL){
        ILocationModifier * lm = (_locationUsesRealGPS)?
        (correction && ![self getMarkHeading].isNan())?
        (ILocationModifier *) new CorrectedAltitudeFixedLM([self elevationData],[self getMarkPosition].asGeodetic2D()) :
        (ILocationModifier *) new AltitudeFixerLM([self elevationData]) :
        (ILocationModifier *) new KarlsruheVirtualWalkLM(elevationData);
        
        _dac = new DeviceAttitudeCameraHandler(true, lm);
    }
}

-(void) setActiveColor:(int)row{
    if (row == 0){
        RandomBuildingColorPicker* rcp = new RandomBuildingColorPicker();
        cityGMLRenderer->colorBuildings(rcp);
        delete rcp;
    }
    
    if (row == 1){
        cityGMLRenderer->colorBuildingsWithColorBrewer("Heat Demand", "Pastel1", 8);
    }
    
    if (row == 2){
        cityGMLRenderer->colorBuildingsWithColorBrewer("Building Volume", "Pastel1", 8);
    }
    
    if (row == 3){
        cityGMLRenderer->colorBuildingsWithColorBrewer("GHG Emissions", "Pastel1", 8);
    }
    
    if (row == 4){
        cityGMLRenderer->colorBuildingsWithColorBrewer("Demographic Clusters (SOM)", "Pastel1", 8);
    }
    
    if (row == 5){
        cityGMLRenderer->colorBuildingsWithColorBrewer("Demographic Clusters (k-Means)", "Pastel1", 8);
    }
    color = row;
}

- (Geodetic3D) getMarkPosition{
    if (positionMark != NULL)
        return positionMark->getPosition();
    return Geodetic3D::zero();
}

- (Angle) getMarkHeading{
    return camConstrainer->getMarkHeading();
}

- (void) setCorrectionActive:(bool)active{
    correction = active;
    [self changeLocationMode:_locationUsesRealGPS];
}

- (void) activePositionFixer{
    [PositionView setHidden:FALSE];
    marksRenderer->setEnable(true);
    camConstrainer->setMark(positionMark);
}

- (IBAction)PositionSetterAction:(id)sender {
    [self stopPositionFixer];
}

- (void) stopPositionFixer{
    camConstrainer->setMark(NULL);
    marksRenderer->setEnable(false);
    [PositionView setHidden:TRUE];
    [self tapDetected];
}

- (void) updatePositionFixer:(NSString *)message{
    [PositionLabel setText:message];
}


/////PICKER VIEW

// The number of columns of data
- (int)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
  return 1;
}

// The number of rows of data
- (int)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
  return (int)_pickerArray.count;
}

// The data to return for the row and component (column) that's being passed in
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
  return _pickerArray[row];
}

// Catpure the picker view selection
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    [self setActiveColor:(int)row];
}

@end
