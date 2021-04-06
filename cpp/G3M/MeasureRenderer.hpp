//
//  MeasureRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/10/21.
//

#ifndef MeasureRenderer_hpp
#define MeasureRenderer_hpp

#include "DefaultRenderer.hpp"

class ShapesRenderer;
class MeshRenderer;
class MarksRenderer;
class CompositeRenderer;
class Measure;


class MeasureRenderer : public DefaultRenderer {
private:
  ShapesRenderer*    _shapesRenderer;
  MeshRenderer*      _meshRenderer;
  MarksRenderer*     _marksRenderer;
  CompositeRenderer* _compositeRenderer;

  std::vector<Measure*> _measures;

public:
  MeasureRenderer(ShapesRenderer*    shapesRenderer,
                  MeshRenderer*      meshRenderer,
                  MarksRenderer*     marksRenderer,
                  CompositeRenderer* compositeRenderer);

  void initialize(const G3MContext* context);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void addMeasure(Measure* measure);

  bool removeMeasure(Measure* measure);

  void removeAllMeasures();
  
};

#endif
