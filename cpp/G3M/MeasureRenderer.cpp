//
//  MeasureRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/10/21.
//

#include "MeasureRenderer.hpp"

#include "G3MContext.hpp"
#include "Measure.hpp"


MeasureRenderer::MeasureRenderer(ShapesRenderer*    shapesRenderer,
                                 MeshRenderer*      meshRenderer,
                                 MarksRenderer*     marksRenderer,
                                 CompositeRenderer* compositeRenderer) :
_shapesRenderer(shapesRenderer),
_meshRenderer(meshRenderer),
_marksRenderer(marksRenderer),
_compositeRenderer(compositeRenderer)
{
  
}

void MeasureRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                            int width, int height) {
  // do nothing
}

void MeasureRenderer::render(const G3MRenderContext* rc,
                             GLState* glState) {

}

void MeasureRenderer::addMeasure(Measure* measure) {
  if (_context != NULL) {
    measure->initialize(_shapesRenderer,
                        _meshRenderer,
                        _marksRenderer,
                        _compositeRenderer,
                        _context->getPlanet());
  }

  _measures.push_back(measure);
}

void MeasureRenderer::removeAllMeasures() {
  for (size_t i = 0; i < _measures.size(); i++) {
    Measure* measure = _measures[i];
    delete measure;
  }
  _measures.clear();
}

void MeasureRenderer::initialize(const G3MContext* context) {
  DefaultRenderer::initialize(context);

  for (size_t i = 0; i < _measures.size(); i++) {
    Measure* measure = _measures[i];
    measure->initialize(_shapesRenderer,
                        _meshRenderer,
                        _marksRenderer,
                        _compositeRenderer,
                        _context->getPlanet());
  }
}
