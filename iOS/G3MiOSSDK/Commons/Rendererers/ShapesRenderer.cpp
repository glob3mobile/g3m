//
//  ShapesRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "ShapesRenderer.hpp"

#include "OrderedRenderable.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"

class TransparentShapeWrapper : public OrderedRenderable {
private:
  Shape* _shape;
  const double _squaredDistanceFromEye;
  GLState* _parentGLState;
  const bool _renderNotReadyShapes;
public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye,
                          GLState* parentGLState,
                          bool renderNotReadyShapes) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _parentGLState(parentGLState),
  _renderNotReadyShapes(renderNotReadyShapes)
  {
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc) {
    _shape->render(rc, _parentGLState, _renderNotReadyShapes);
  }
};

bool ShapesRenderer::isReadyToRender(const G3MRenderContext* rc) {
  if (_renderNotReadyShapes) {
    return true;
  }

  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    const bool shapeReady = shape->isReadyToRender(rc);
    if (!shapeReady) {
      return false;
    }
  }
  return true;
}


void ShapesRenderer::updateGLState(const G3MRenderContext* rc) {

  const Camera* cam = rc->getCurrentCamera();
  /*

  if (_projection == NULL) {
    _projection = new ProjectionGLFeature(cam->getProjectionMatrix44D());
    _glState->addGLFeature(_projection, true);
    _glStateTransparent->addGLFeature(_projection, true);
  } else{
    _projection->setMatrix(cam->getProjectionMatrix44D());
  }

  if (_model == NULL) {
    _model = new ModelGLFeature(cam->getModelMatrix44D());
    _glState->addGLFeature(_model, true);
    _glStateTransparent->addGLFeature(_model, true);
  } else{
    _model->setMatrix(cam->getModelMatrix44D());
  }
*/
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL){
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  } else{
    f->setMatrix(cam->getModelViewMatrix44D());
  }

  f = (ModelViewGLFeature*) _glStateTransparent->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL){
    _glStateTransparent->addGLFeature(new ModelViewGLFeature(cam), true);
  } else{
    f->setMatrix(cam->getModelViewMatrix44D());
  }

}

void ShapesRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();

  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();
  
  //Setting camera matrixes
  updateGLState(rc);

  _glState->setParent(glState);
  _glStateTransparent->setParent(glState);


  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    if (shape->isEnable()) {
      if (shape->isTransparent(rc)) {
        const Planet* planet = rc->getPlanet();
        const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
        const double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();

        rc->addOrderedRenderable(new TransparentShapeWrapper(shape,
                                                             squaredDistanceFromEye,
                                                             _glStateTransparent,
                                                             _renderNotReadyShapes));
      }
      else {
        shape->render(rc, _glState, _renderNotReadyShapes);
      }
    }
  }
}

void ShapesRenderer::removeAllShapes(bool deleteShapes) {
  if (deleteShapes) {
    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      delete shape;
    }
  }

  _shapes.clear();
}


#ifdef C_CODE
class SortShapeDistanceClass {
public:
  bool operator() (ShapeDistance sd1, ShapeDistance sd2) {
    return (sd1._distance < sd2._distance);
  }
} sortShapeDistanceObject;
#endif



std::vector<ShapeDistance> ShapesRenderer::intersectionsDistances(const Vector3D& origin,
                                                                   const Vector3D& direction) const
{
  std::vector<ShapeDistance> shapeDistances;
  for (int n=0; n<_shapes.size(); n++) {
    Shape* shape = _shapes[n];
    std::vector<double> distances = shape->intersectionsDistances(origin, direction);
    for (int i=0; i<distances.size(); i++) {
      shapeDistances.push_back(ShapeDistance(distances[i], shape));
    }
  }
  
  // sort vector
#ifdef C_CODE
    std::sort(shapeDistances.begin(),
              shapeDistances.end(),
              sortShapeDistanceObject);
#endif
#ifdef JAVA_CODE
  java.util.Collections.sort(shapeDistances, new java.util.Comparator<ShapeDistance>() {
    @Override
    public int compare(final ShapeDistance sd1,
                       final ShapeDistance sd2) {
      final double distance1 = sd1._distance;
      final double distance2 = sd2._distance;
      if (distance1 == distance2) {
        return 0;
      }
      else if (distance1 < distance2) {
        return -1;
      }
      else {
        return 1;
      }
    }
  });
#endif

  return shapeDistances;
}


bool ShapesRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent)
{
  if (_lastCamera != NULL) {
    if (touchEvent->getTouchCount() ==1 &&
        touchEvent->getTapCount()==1 &&
        touchEvent->getType()==Down) {
      const Vector3D origin = _lastCamera->getCartesianPosition();
      const Vector2I pixel = touchEvent->getTouch(0)->getPos();
      const Vector3D direction = _lastCamera->pixel2Ray(pixel);
      std::vector<ShapeDistance> shapeDistances = intersectionsDistances(origin, direction);
      
      if (!shapeDistances.empty()) {
//        printf ("Found %d intersections with shapes:\n",
//                (int)shapeDistances.size());
        for (int i=0; i<shapeDistances.size(); i++) {
//          printf ("   %d: shape %x to distance %f\n",
//                  i+1,
//                  (unsigned int)shapeDistances[i]._shape,
//                  shapeDistances[i]._distance);
        }
      }
    }
  }
  return false;
}



