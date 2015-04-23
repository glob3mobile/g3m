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
#include "ForceGraphRenderer.hpp"
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
#include "Sphere.hpp"
#include "Mesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilder.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "Box.hpp"

@interface ViewController ()

@end

@implementation ViewController
/*Test one node as anchor and many nodes anchored to it. All nodes connected to each other.
 They should spread out evenly but still stay above the earth
 Issues: double adding as neighbor messes things up - need to add a check for existing or a "visited" bool for force calc*/
void testOneAnchorManyNodes4Clique(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 0, 1.5e5));
    
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 5));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 0));
    ForceGraphNode *node3 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(35, 30, 3e5));
    ForceGraphNode *node4 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node5 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(61, 30, 3e5));
    ForceGraphNode *node6 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 1, 3));
    ForceGraphNode *node7 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    ForceGraphNode *node8 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    
    node->addAnchor(anchor);
    node2->addAnchor(anchor);
    node3->addAnchor(anchor);
    node4->addAnchor(anchor);
    node5->addAnchor(anchor);
    node6->addAnchor(anchor);
    node7->addAnchor(anchor);
    node8->addAnchor(anchor);
    
    node->addNeighbor(node2);
    node->addNeighbor(node3);
    node->addNeighbor(node4);
    node->addNeighbor(node5);
    node->addNeighbor(node6);
    node->addNeighbor(node7);
    node->addNeighbor(node8);
    
    node2->addNeighbor(node3);
    node2->addNeighbor(node4);
    node2->addNeighbor(node5);
    node2->addNeighbor(node6);
    node2->addNeighbor(node7);
    node2->addNeighbor(node8);
    
    node3->addNeighbor(node4);
    node3->addNeighbor(node5);
    node3->addNeighbor(node6);
    node3->addNeighbor(node7);
    node3->addNeighbor(node8);
    
    node4->addNeighbor(node5);
    node4->addNeighbor(node6);
    node4->addNeighbor(node7);
    node4->addNeighbor(node8);
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(node3);
    forceGraphRenderer->addMark(node4);
    forceGraphRenderer->addMark(node5);
    forceGraphRenderer->addMark(node6);
    forceGraphRenderer->addMark(node7);
    forceGraphRenderer->addMark(node8);
}

/*Test one node as anchor and many nodes anchored to it. They should spread out evenly but still stay above the earth
 Issues: move anchor too far down makes the planetForce go crazy?? */
void testOneAnchorManyNodes(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(-70, 90, 1.5e5));
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 5));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 0));
    ForceGraphNode *node3 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node4 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node5 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node6 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 1, 3));
    ForceGraphNode *node7 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    ForceGraphNode *node8 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    
    node->addAnchor(anchor);
    node2->addAnchor(anchor);
    node3->addAnchor(anchor);
    node4->addAnchor(anchor);
    node5->addAnchor(anchor);
    node6->addAnchor(anchor);
    node7->addAnchor(anchor);
    node8->addAnchor(anchor);
    
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(node3);
    forceGraphRenderer->addMark(node4);
    forceGraphRenderer->addMark(node5);
    forceGraphRenderer->addMark(node6);
    forceGraphRenderer->addMark(node7);
    forceGraphRenderer->addMark(node8);
}

/*Test one node as anchor and many nodes anchored to it, some of those nodes have edges between them. They should spread out evenly but still stay above the earth
 Issues: */
void testOneAnchorManyNodesCycles(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(45, -20, 1.5e5));
    
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(40, 0, 5));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 0));
    ForceGraphNode *node3 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node4 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node5 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node6 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 1, 3));
    ForceGraphNode *node7 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    ForceGraphNode *node8 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    
    node->addAnchor(anchor);
    node2->addAnchor(anchor);
    node3->addAnchor(anchor);
    node4->addAnchor(anchor);
    node5->addAnchor(anchor);
    node6->addAnchor(anchor);
    node7->addAnchor(anchor);
    node8->addAnchor(anchor);
    node2->addNeighbor(node3);
    node3->addNeighbor(node4);
    
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(node3);
    forceGraphRenderer->addMark(node4);
    forceGraphRenderer->addMark(node5);
    forceGraphRenderer->addMark(node6);
    forceGraphRenderer->addMark(node7);
    forceGraphRenderer->addMark(node8);
}

/*Test with a node anchored 180 degrees away from another anchor its neighbor is attached to
 Testing that edges don't go into the earth - if plenty of nodes in between seems to be less of an issue
 Issues: wiggles a little bit b/c of changing planetCharge*/
void testPlanetChargeNodesBetween(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 2, 1.5e5));
    ForceGraphNode *anchor2 = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(70, -180, 1.5e5));
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(40, 0, 5));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 0));
    ForceGraphNode *node3 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node4 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node5 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    ForceGraphNode *node6 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 1, 3));
    ForceGraphNode *node7 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    ForceGraphNode *node8 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 0, 3));
    
    node->addAnchor(anchor);
    node2->addNeighbor(node);
    node3->addNeighbor(node2);
    node4->addNeighbor(node3);
    node5->addNeighbor(node4);
    node6->addNeighbor(node5);
    
    // node->addNeighbor(node6);
    node7->addNeighbor(node3);
    node8->addNeighbor(node7);
    node8->addAnchor(anchor2);
    node6->addNeighbor(node);
    
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(node3);
    forceGraphRenderer->addMark(node4);
    forceGraphRenderer->addMark(node5);
    forceGraphRenderer->addMark(node6);
    forceGraphRenderer->addMark(node7);
    forceGraphRenderer->addMark(node8);
    forceGraphRenderer->addMark(anchor2);
}


/*Test with a simple graph with node anchored 90 degrees away from another anchor its neighbor is attached to but 2 nodes in between
 Testing that edges don't go into the earth
 Issues: min height -> add to planet charge - this makes nodes shake
 */
void testPlanetCharge90DegreesNodeBetween(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 0, 1.5e5));
    ForceGraphNode *anchor2 = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 90, 1.5e5));
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(40, 0, 5e7));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 5e8));
    ForceGraphNode *node3 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(31, 30, 3e5));
    
    node->addAnchor(anchor);
    node3->addAnchor(anchor2);
    node->addNeighbor(node2);
    node3->addNeighbor(node2);
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(anchor2);
    forceGraphRenderer->addMark(node3);
}

/*Test with a simple graph with node anchored 90 degrees away from another anchor its neighbor is attached to but 2 nodes in between
 Testing that edges don't go into the earth
 Issues: min height -> add to planet charge - this makes nodes shake
 */
void testPlanetCharge90DegreesOneBetween(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 0, 1.5e5));
    ForceGraphNode *anchor2 = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(0, 90, 1.5e5));
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(40, 0, 5e7));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 5e8));
    
    node->addAnchor(anchor);
    node2->addAnchor(anchor2);
    node->addNeighbor(node2);
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(anchor2);
}



/*Test with a simple graph with node anchored 360 degrees away from another anchor its neighbor is attached to
 Testing that edges don't go into the earth
 Issues: how do we deal with this? Edges go straight into earth.  */
void testPlanetCharge360Degrees(Shape* anchor_sphere, Shape* sphere, ForceGraphRenderer* forceGraphRenderer) {
    
    ForceGraphNode *anchor = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(-90, -180, 1.5e5));
    ForceGraphNode *anchor2 = new ForceGraphNode(anchor_sphere, sphere, Geodetic3D::fromDegrees(90, 180, 1.5e5));
    ForceGraphNode *node = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(40, 0, 5));
    ForceGraphNode *node2 = new ForceGraphNode(sphere, anchor_sphere, Geodetic3D::fromDegrees(0, 50, 0));
    
    node->addAnchor(anchor);
    node2->addAnchor(anchor2);
    node->addNeighbor(node2);
    
    
    forceGraphRenderer->addMark(node);
    forceGraphRenderer->addMark(anchor);
    forceGraphRenderer->addMark(node2);
    forceGraphRenderer->addMark(anchor2);
}

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
    
    
    ShapesRenderer* shapesRenderer = new ShapesRenderer();
    
    
    ForceGraphRenderer *forceGraphRenderer = new ForceGraphRenderer(shapesRenderer, 50);
    //testOneAnchorManyNodes4Clique(anchor_sphere, sphere, forceGraphRenderer);
    // testOneAnchorManyNodes(anchor_sphere, sphere, forceGraphRenderer);
    // testOneAnchorManyNodesCycles(anchor_sphere, sphere, forceGraphRenderer);
    /* testPlanetCharge90DegreesOneBetween(anchor_sphere, sphere, forceGraphRenderer);
     testPlanetCharge90DegreesNodeBetween(anchor_sphere, sphere, forceGraphRenderer);
     testPlanetCharge360Degrees(anchor_sphere, sphere, forceGraphRenderer);
     testPlanetChargeNodesBetween(anchor_sphere, sphere, forceGraphRenderer);*/
    
    
    builder.addRenderer(forceGraphRenderer);
    //const IThreadUtils* _threadUtils;
    //  builder.setInitializationTask(new initialization(marksRenderer), true);
    
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
 