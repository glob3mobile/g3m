//
//  ViewController.m
//  nomarks-g3m-demo
//
//  Created by Stefanie Alfonso on 2/13/15.
//  Copyright (c) 2015 Stefanie Alfonso. All rights reserved.
//

#import "ViewController.h"
#import "G3MWidget_iOS.h"
#import "G3MBuilder_iOS.hpp"
#include "MarksRenderer.hpp"
#include "NonOverlapping3DMarksRenderer.hpp"
#include "Geodetic3D.hpp"
#include "IThreadUtils.hpp"
#include "JSONObject.hpp"
#include "Context.hpp"
#include "IDownloader.hpp"
#include "GInitializationTask.hpp"
#include "IBufferDownloadListener.hpp"
#include "IJSONParser.hpp"
#include "JSONBaseObject.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "DownloaderImageBuilder.hpp"
#include <vector.h>
#include "Planet.hpp"
#include "EllipsoidalPlanet.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "Vector3D.hpp"
#include "Shape.hpp"
#include "Color.hpp"
#include "EllipsoidShape.hpp"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    G3MBuilder_iOS builder([self g3mwidget]);
    //EllipsoidalPlanet *p = new EllipsoidalPlanet(Ellipsoid(Vector3D(0,0,0), Vector3D(1000,1000,1000)));
    //builder.setPlanet(p);
    Shape* sphere = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
                                                              Angle::fromDegrees(0),
                                                              5),
                                               ABSOLUTE,
                                               Vector3D(100, 100, 100),
                                               16,
                                               0,
                                               false,
                                               false,
                                               Color::fromRGBA(1, 0, 0, .5));
    
    Shape* anchor_sphere = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
                                                      Angle::fromDegrees(0),
                                                      5),
                                       ABSOLUTE,
                                       Vector3D(100, 100, 100),
                                       16,
                                       0,
                                       false,
                                       false,
                                       Color::fromRGBA(0, 1, 0, .5));
    anchor_sphere->setScale(1000);
    sphere->setScale(1000);
    
    MarksRenderer *marksRenderer = new MarksRenderer(true);
    NonOverlapping3DMarksRenderer *forceGraphRenderer = new NonOverlapping3DMarksRenderer(3);
    NonOverlapping3DMark *anchor = new NonOverlapping3DMark(anchor_sphere, sphere, Geodetic3D::fromDegrees(5, 5, 2));
    NonOverlapping3DMark *node = new NonOverlapping3DMark(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 0, 1));
   // NonOverlapping3DMark *node2 = new NonOverlapping3DMark(imageBuilder3, Geodetic3D::fromDegrees(10, 20, 1));
   // node->addAnchor(anchor);
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
   // forceGraphRenderer->addMark(node2);

    builder.addRenderer(marksRenderer);
    builder.addRenderer(forceGraphRenderer);
    //const IThreadUtils* _threadUtils;
    builder.setInitializationTask(new initialization(marksRenderer), true);

    builder.initializeWidget();
}

// Start animation when view has appeared
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    // Start the glob3 render loop
    [self.g3mwidget startAnimation];
}
// Stop the animation when view has disappeared
- (void)viewDidDisappear:(BOOL)animated
{
    // Stop the glob3 render loop
    [self.g3mwidget stopAnimation];
    [super viewDidDisappear:animated];
}
// Release property
- (void)viewDidUnload
{
    self.g3mwidget        = nil;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

class downloadListener : public IBufferDownloadListener {
    const IJSONParser *_parser;
    MarksRenderer* _marksRenderer;
public:
    
    downloadListener(const IJSONParser* parser, MarksRenderer* marksRenderer) : _parser(parser), _marksRenderer(marksRenderer){
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired) {
        
        /*std::vector<std::string> names;
         std::vector<Geodetic3D> coords;
         std::vector<double> pops;*/
        
        //parse stuff
        /* IJSONParser *parse = _parser->instance();
         const JSONBaseObject *obj = parse->parse(buffer);
         const JSONObject *featureCollection = obj->asObject();
         const JSONArray *features = featureCollection->asArray();
         for(int i = 0; i < features->size(); i++) {
         const JSONObject *feature = features->getAsObject(i);
         const JSONObject *properties = feature->getAsObject("properties");
         const std::string name = properties->getAsString("name", "");
         const double population = properties->getAsNumber("population", 0);
         const JSONObject *geometry = feature->getAsObject("geometry");
         const JSONArray *coordinates = geometry->getAsArray("coordinates");
         double lat = coordinates->getAsNumber(1, 0);
         double lon = coordinates->getAsNumber(0, 0);
         names.push_back(name);
         coords.push_back(Geodetic3D::fromDegrees(lat, lon, 100));
         pops.push_back(population);
         
         _marksRenderer->addMark(new Mark(name, Geodetic3D::fromDegrees(lat, lon, 0), ABSOLUTE, 0));
         
         }
         
         delete obj;
         
         delete buffer;*/
    }
    
    void onError(const URL& url) {
        
    }
    
    void onCancel(const URL& url) {
        
    }
    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
        
    }
    
};

class initialization : public GInitializationTask {
    MarksRenderer *_marksRenderer;
    
public:
    initialization(MarksRenderer *marksRenderer) : _marksRenderer(marksRenderer) {
        
    }
    
    bool isDone(const G3MContext* context) {
        
        return true;
    }
    
    void run(const G3MContext* context) {
        IDownloader *downloader = context->getDownloader();
        const IJSONParser *parser = context->getJSONParser();
        downloader->requestBuffer(URL("file:///cities.geojson"), 1, TimeInterval::fromDays(30), true, new downloadListener(parser, _marksRenderer), true);
    }
    
};

@end
