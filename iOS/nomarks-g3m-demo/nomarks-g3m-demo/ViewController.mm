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
                                                              40),
                                               ABSOLUTE,
                                               Vector3D(100000, 100000, 100000),
                                               16,
                                               0,
                                               false,
                                               false,
                                               Color::fromRGBA(1, 0, 0, .9));
    
    Shape* anchor_sphere = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
                                                      Angle::fromDegrees(0),
                                                      40),
                                       ABSOLUTE,
                                       Vector3D(100000, 100000, 100000),
                                       16,
                                       0,
                                       false,
                                       false,
                                       Color::fromRGBA(0, 1, 0, .9));
   // anchor_sphere->setScale(1000);
    //sphere->setScale(1000);
    
    MarksRenderer *marksRenderer = new MarksRenderer(true);
    NonOverlapping3DMarksRenderer *forceGraphRenderer = new NonOverlapping3DMarksRenderer(4);
    NonOverlapping3DMark *anchor = new NonOverlapping3DMark(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 0, 5e5));
    NonOverlapping3DMark *node = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D::fromDegrees(4, 0, 5e5));
     NonOverlapping3DMark *node2 = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 4, 5));
    NonOverlapping3DMark *node3 = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D::fromDegrees(30, 30, 3));
    node->addAnchor(anchor);
    node2->addAnchor(anchor);
   // node3->addNeighbor(anchor);
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    //forceGraphRenderer->addMark(node3);

    builder.addRenderer(marksRenderer);
    builder.addRenderer(forceGraphRenderer);
    //const IThreadUtils* _threadUtils;
    builder.setInitializationTask(new initialization(marksRenderer), true);

    builder.initializeWidget();
    
   // delete sphere;
  // delete anchor_sphere;
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
