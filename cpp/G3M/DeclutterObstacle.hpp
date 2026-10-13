//
//  DeclutterObstacle.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#ifndef G3M_DeclutterObstacle
#define G3M_DeclutterObstacle

class G3MRenderContext;
class RectangleF;


/** something drawn over the marks (a 3D model, a widget) whose screen area a decluttering MarksRenderer keeps free */
class DeclutterObstacle {
public:
  virtual ~DeclutterObstacle() {
  }

  /** the screen rectangle it takes in this frame, in the camera's pixels; NULL: none */
  virtual RectangleF* createScreenRectangle(const G3MRenderContext* rc) = 0;

};

#endif
