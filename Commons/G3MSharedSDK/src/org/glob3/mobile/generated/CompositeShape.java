package org.glob3.mobile.generated;
//
//  CompositeShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

//
//  CompositeShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//





public class CompositeShape extends Shape
{
  private java.util.ArrayList<Shape> _children = new java.util.ArrayList<Shape>();

  public CompositeShape()
  {
     super(null, AltitudeMode.ABSOLUTE);

  }

  public CompositeShape(Geodetic3D position, AltitudeMode altitudeMode)
  {
     super(position, altitudeMode);

  }


  public void dispose()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Shape child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  
    super.dispose();
  }

  public final void addShape(Shape shape)
  {
    _children.add(shape);
  }
  public final void removeAllShapes(boolean deleteShapes)
  {
    if (deleteShapes)
    {
      final int childrenCount = _children.size();
      for (int i = 0; i < childrenCount; i++)
      {
        Shape child = _children.get(i);
        if (child != null)
           child.dispose();
      }
    }
    _children.clear();
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Shape child = _children.get(i);
      if (!child.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    return true;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Shape child = _children.get(i);
      child.render(rc, parentState, renderNotReadyShapes);
    }
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Shape child = _children.get(i);
      if (child.isTransparent(rc))
      {
        return true;
      }
    }
    return false;
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Shape child = _children.get(i);
      java.util.ArrayList<Double> childResults = child.intersectionsDistances(planet, origin, direction);
  
      for (int j = 0; j < childResults.size(); j++)
      {
        intersections.add(childResults.get(j));
      }
    }
  
    // sort vector
    java.util.Collections.sort(intersections);
  
    return intersections;
  }

}