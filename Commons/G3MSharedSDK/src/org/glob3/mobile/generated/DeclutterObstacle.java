package org.glob3.mobile.generated;
//
//  DeclutterObstacle.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

//
//  DeclutterObstacle.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//


//class G3MRenderContext;
//class RectangleF;


/** something drawn over the marks (a 3D model, a widget) whose screen area a decluttering MarksRenderer keeps free */
public abstract class DeclutterObstacle
{
  public void dispose()
  {
  }

  /** the screen rectangle it takes in this frame, in the camera's pixels; NULL: none */
  public abstract RectangleF createScreenRectangle(G3MRenderContext rc);

}