//
//  ViewController.m
//  3gm-testing
//
//  Created by Stefanie Alfonso on 1/31/15.
//  Copyright (c) 2015 Stefanie Alfonso. All rights reserved.
//

#import "ViewController.h"
#include <G3MiOSSDK/G3MWidget_iOS.h>
#include <G3MiOSSDK/G3MBuilder_iOS.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/NonOverlapping3DMarksRenderer.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/IThreadUtils.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/Context.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/GInitializationTask.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/JSONBaseObject.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <vector.h>


@class G3MWidget_iOS;

@interface ViewController ()
@property (strong, nonatomic) IBOutlet G3MWidget_iOS *ibview;

@end

@implementation ViewController


class addMarkerTask : public GAsyncTask {
    
};

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


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    G3MBuilder_iOS builder([self ibview]);
    
    
    MarksRenderer *marksRenderer = new MarksRenderer(true);
    NonOverlapping3DMarksRenderer forceGraphRenderer = new NonOverlapping3DMarksRenderer(true);
    
 //   Mark *mark = new Mark("woo", URL("https://cdn4.iconfinder.com/data/icons/socialmediaicons_v120/48/wikipedia.png"), Geodetic3D::fromDegrees(0, 0, 0), //ABSOLUTE, 0);
  //  marksRenderer->addMark(mark);
    builder.addRenderer(marksRenderer);
    builder.addRenderer(forceGraphRenderer);
    //const IThreadUtils* _threadUtils;
    builder.setInitializationTask(new initialization(marksRenderer), true);
    
    //_threadUtils->invokeAsyncTask(new GAsyncTask(),
    //   true);
    
    
    
    
    //make initialization task - from builder
    //has run and isdone
    //isdone returns loadedmarkers.get() if done no spinny wheel
    //context get downloader, request buffer accepts url, priority, listener ->
    //litener - IBufferdownload listener - on download, on error, on cancel
    //delete buffer after using
    //parse on this method on download first then add async task
    //context get json
    //post execute - add mark and shape
    
    
    
    builder.initializeWidget();
    
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// Start animation when view has appeared
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    // Start the glob3 render loop
    [self.ibview startAnimation];
}
// Stop the animation when view has disappeared
- (void)viewDidDisappear:(BOOL)animated
{
    // Stop the glob3 render loop
    [self.ibview stopAnimation];
    [super viewDidDisappear:animated];
}
// Release property
- (void)viewDidUnload
{
    self.ibview        = nil;
}

@end
