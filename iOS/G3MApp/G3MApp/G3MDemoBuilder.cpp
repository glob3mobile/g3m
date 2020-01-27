//
//  G3MDemoBuilder.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//

#include "G3MDemoBuilder.hpp"

#include <G3M/PlanetRendererBuilder.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/IG3MBuilder.hpp>
#include <G3M/ErrorHandling.hpp>
#include <G3M/GInitializationTask.hpp>

#include <G3M/MeshRenderer.hpp>
#include <G3M/ShapesRenderer.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/GEORenderer.hpp>
#include <G3M/PointCloudsRenderer.hpp>
#include <G3M/HUDRenderer.hpp>
#include <G3M/NonOverlappingMarksRenderer.hpp>
#include <G3M/VectorStreamingRenderer.hpp>
#include <G3M/AtmosphereRenderer.hpp>
#include <G3M/WrapperNearFrustumRenderer.hpp>

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

  builder->setAtmosphere(true);

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

  VectorStreamingRenderer* vectorStreamingRenderer = new VectorStreamingRenderer(marksRenderer, meshRenderer);
  builder->addRenderer(vectorStreamingRenderer);

  //Uncomment to see render debug mesh on top of tiles
  //#warning remove setRenderDebug(true);
  //builder->getPlanetRendererBuilder()->setRenderDebug(true);

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

  const double zNear = 0.1;
  Renderer* renderer = new MeshRenderer();
  builder->setNearFrustumRenderer(new WrapperNearFrustumRenderer(zNear, renderer));

  builder->setInitializationTask(new G3MDemoInitializationTask(_model), true);
}

G3MDemoModel* G3MDemoBuilder::getModel() {
  if (!_initialized || _model == NULL) {
    THROW_EXCEPTION("Model not yet created. Have to initialize the widget before getting the model.");
  }
  return _model;
}
