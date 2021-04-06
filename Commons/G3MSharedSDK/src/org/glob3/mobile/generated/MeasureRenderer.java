package org.glob3.mobile.generated;
//
//  MeasureRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/10/21.
//

//
//  MeasureRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/10/21.
//



//class ShapesRenderer;
//class MeshRenderer;
//class MarksRenderer;
//class CompositeRenderer;
//class Measure;


public class MeasureRenderer extends DefaultRenderer
{
  private ShapesRenderer _shapesRenderer;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;
  private CompositeRenderer _compositeRenderer;

  private java.util.ArrayList<Measure> _measures = new java.util.ArrayList<Measure>();

  public MeasureRenderer(ShapesRenderer shapesRenderer, MeshRenderer meshRenderer, MarksRenderer marksRenderer, CompositeRenderer compositeRenderer)
  {
     _shapesRenderer = shapesRenderer;
     _meshRenderer = meshRenderer;
     _marksRenderer = marksRenderer;
     _compositeRenderer = compositeRenderer;
  
  }

  public final void initialize(G3MContext context)
  {
    super.initialize(context);
  
    for (int i = 0; i < _measures.size(); i++)
    {
      Measure measure = _measures.get(i);
      measure.initialize(_shapesRenderer, _meshRenderer, _marksRenderer, _compositeRenderer, _context.getPlanet());
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    // do nothing
  }

  public final void addMeasure(Measure measure)
  {
    if (_context != null)
    {
      measure.initialize(_shapesRenderer, _meshRenderer, _marksRenderer, _compositeRenderer, _context.getPlanet());
    }
  
    _measures.add(measure);
  }

  public final boolean removeMeasure(Measure measure)
  {
    for (int i = 0; i < _measures.size(); i++)
    {
      if (_measures.get(i) == measure)
      {
        measure.clearSelection();
        measure.cleanUI();
        _measures.remove(i);
        return true;
      }
    }
    return false;
  }

  public final void removeAllMeasures()
  {
    for (int i = 0; i < _measures.size(); i++)
    {
      Measure measure = _measures.get(i);
      measure.clearSelection();
      measure.cleanUI();
      if (measure != null)
         measure.dispose();
    }
    _measures.clear();
  }

}