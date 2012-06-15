//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MarksRenderer.hpp"
#include "Camera.hpp"

void MarksRenderer::initialize(const InitializationContext* ic) {
  
}

bool MarksRenderer::onTouchEvent(const TouchEvent* touchEvent) {
  return false;
}

int MarksRenderer::render(const RenderContext* rc) {
//  rc.getLogger()->logInfo("MarksRenderer::render()");
  
  IGL* gl = rc->getGL();
  
  const Planet* planet = rc->getPlanet();

  gl->enableVertices();
  gl->enableTextures();

  gl->depthTest(false);
  gl->blend(true);

  int __dgd_at_work;

  //const Camera* camera = rc->getCamera();
  //const Vector3D cameraPosition = camera->getPos();
  
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
    
    mark->render(rc, planet);
  }
  
  gl->depthTest(true);
  gl->blend(false);

  gl->disableTextures();
  gl->disableVertices();
  
  
  return MAX_TIME_TO_RENDER;
}


//
//int MarksRenderer::Render(RenderContext &rc) {
//  // obtaing gl object reference
//  IGL *gl = rc.GetGL();
//  //Point3D po = View::GetInstance()->GetCamera()->GetPos();
//  //Vector3D c(po.x, po.y, po.z); 
//  Vector3D c = View::GetInstance()->GetCamera()->GetPos();
//  
//  gl->DepthTestEnabled(false);
//  gl->BlendingEnabled(true);
//  
//  //DRAWING LOOP
//  Ellipsoid *ellipsoid = SceneController::GetInstance()->getGlobe()->GetEllipsoid();
//  Vector3D radius = ellipsoid->getRadii();
//  double minDist = (radius.X + radius.Y + radius.Z) / 3;
//  for (int i = 0; i < marks.markSet.size(); i++) {
//    Mark *m = marks.markSet.at(i);
//    
//    Vector3D v = ellipsoid->ToVector3D(m->point);
//    Vector3D normal = Ellipsoid::CentricSurfaceNormal(v);
//    Vector3D vecCam = v.minus(c);
//    
//    int textureID = getMarkTexID(rc, m);
//    
//    // draw mark if close to the point and not eclipsed by the earth
//    if (vecCam.Magnitude() < minDist && normal.AngleBetween(vecCam) > PI / 2) {
//      gl->DrawBillBoard(textureID, (float) v.X, (float) v.Y, (float) v.Z);
//      m->setVisible(true);
//    } else
//      m->setVisible(false);
//  }
//  
//  gl->DepthTestEnabled(true);
//  gl->BlendingEnabled(false);
//  
//  
//  return 9999;
//}	
