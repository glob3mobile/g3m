//
//  G3MDemoBuilder.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoBuilder.hpp"

#include <G3MiOSSDK/PlanetRendererBuilder.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/IG3MBuilder.hpp>
#include <G3MiOSSDK/ErrorHandling.hpp>
#include <G3MiOSSDK/GInitializationTask.hpp>

#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/GEORenderer.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/HUDRenderer.hpp>
#include <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#include <G3MiOSSDK/VectorStreamingRenderer.hpp>

#include "G3MDemoModel.hpp"


G3MDemoBuilder::G3MDemoBuilder(G3MDemoListener* listener) :
_listener(listener),
_initialized(false),
_model(NULL)
{
}

G3MDemoBuilder::~G3MDemoBuilder() {
}


class G3MDemoInitializationTask : public GInitializationTask {
private:
  G3MDemoModel* _model;

public:
  G3MDemoInitializationTask(G3MDemoModel* model) :
  _model(model)
  {
  }

  void run(const G3MContext* context) {
    _model->initializeG3MContext(context);
  }

  bool isDone(const G3MContext* context) {
    return true;
  }
};

void G3MDemoBuilder::build() {
  if (_initialized) {
    THROW_EXCEPTION("G3MWidget already initialized.");
  }

  IG3MBuilder* builder = getG3MBuilder();

  //builder->getPlanetRendererBuilder()->setRenderDebug(true);

  LayerSet* layerSet = new LayerSet();
  builder->getPlanetRendererBuilder()->setLayerSet(layerSet);

  MeshRenderer* meshRenderer = new MeshRenderer();
  builder->addRenderer(meshRenderer);

  ShapesRenderer* shapesRenderer = new ShapesRenderer();
  builder->addRenderer(shapesRenderer);

  MarksRenderer* marksRenderer = new MarksRenderer(false);
  builder->addRenderer(marksRenderer);

  PointCloudsRenderer* pointCloudsRenderer = new PointCloudsRenderer();
  builder->addRenderer(pointCloudsRenderer);
//  builder->getPlanetRendererBuilder()->setTileRenderingListener(pointCloudsRenderer->getTileRenderingListener());

  GEORenderer* geoRenderer = new GEORenderer(NULL, /* symbolizer */
                                             meshRenderer,
                                             shapesRenderer,
                                             marksRenderer,
                                             NULL  /* geoVectorLayer */);
  builder->addRenderer(geoRenderer);

  HUDRenderer* hudRenderer = new HUDRenderer();
  builder->setHUDRenderer(hudRenderer);

  NonOverlappingMarksRenderer* nonOverlappingMarksRenderer = new NonOverlappingMarksRenderer(10);
  builder->addRenderer(nonOverlappingMarksRenderer);

  VectorStreamingRenderer* vectorStreamingRenderer = new VectorStreamingRenderer(marksRenderer);
  builder->addRenderer(vectorStreamingRenderer);

  _initialized = true;
  _model = new G3MDemoModel(_listener,
                            layerSet,
                            meshRenderer,
                            shapesRenderer,
                            marksRenderer,
                            geoRenderer,
                            pointCloudsRenderer,
                            hudRenderer,
                            nonOverlappingMarksRenderer,
                            vectorStreamingRenderer);

  builder->setInitializationTask(new G3MDemoInitializationTask(_model), true);
}

G3MDemoModel* G3MDemoBuilder::getModel() {
  if (!_initialized || _model == NULL) {
    THROW_EXCEPTION("Model not yet created. Have to initialize the widget before getting the model.");
  }
  return _model;
}
