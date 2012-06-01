/*
*  Camera.cpp
*  Prueba Opengl iPad
*
*  Created by Agustín Trujillo Pino on 24/01/11.
*  Copyright 2011 Universidad de Las Palmas. All rights reserved.
*
*/

//
//#include <math.h>
//#include <string.h>
//
//#define PI 3.141592653589793
//
//#include "GLU.h"
//
//#include "Camera.hpp"
//
//
//Camera::Camera(int width, int height) :
//pos(0.0, 0.0, 0.0),
//center(0.0, 0.0, 0.0),
//up(0.0, 0.0, 0.0),
//pitch(0.0),
//roll(0.0)
//{
//    ResizeViewport(width, height);
//}
//
//
////void Camera::Init(int width, int height) {
////    //Globe *g = SceneController::GetInstance()->getGlobe();
////    //double R = g->GetGlobeRadius();
////    Vector3D radius = SceneController::GetInstance()->getGlobe()->GetEllipsoid()->getRadii();
////    double R = (radius.X + radius.Y + radius.Z) / 3;
////    Geodetic3D g3(Angle::zero(), Angle::zero(), 9 * R);
////    SetPosGeo3D(g3);
////
////    //SetPosXYZ (0, 0, 10*globeRadius);
////    //SetPosXYZ (-242.303, 479.036, 868.973);
////    //SetPosLatLon(0, 0, 1000.05);
////
////    /* //Las Palmas
////    ox=-234.622740;
////    oy=471.490415;
////    oz=850.155299;
////    cx=2139.474064;
////    cy=7604.225265;
////    cz=-5744.369545;
////    upx=-0.175136;
////    upy=0.699166;
////    upz=0.693177;*/
////
////    ResizeViewport(width, height);
////}
//
//
//void Camera::ResizeViewport(int width, int height) {
//    viewport[0] = viewport[1] = 0;
//    viewport[2] = width;
//    viewport[3] = height;
//}
//
//
//void Camera::Draw(RenderContext &rc) {
//    double znear;
//
//    // update znear
//    double height = GetPosGeo3D().height();
//    if (height > 1273000.0) znear = 636500.0;
//    else if (height > 12730.0) znear = 6365.0;
//    else if (height > 3182.5) znear = 63.65;
//    else
//        znear = 19.095;
//
//    // compute projection matrix
//    double ratioScreen = (double) viewport[3] / viewport[2];
//    Glu::ComputeProjectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear, projection);
//
//    // obtaing gl object reference
//    IGL *gl = rc.getGL();
//    gl->SetProjection(projection);
//
//    // make the lookat
//    //glMatrixMode(GL_MODELVIEW);
//    Glu::ComputeLookAtMatrix(pos, center, up, lookAt);
//    float L[16];
//    for (int k = 0; k < 16; k++) L[k] = (float) lookAt[k];
//    gl->LoadMatrixf(L);
//
//    posl.copyFrom(g->GetEllipsoid()->ToGeodetic3D(pos).GetLatLon());
//
//    // compute the view center above the globe
//    viewIntersectGlobe = IntersectionViewWithGlobe(pv);
//    if (!viewIntersectGlobe)
//    iprintf ("***** WARNING: VIEW DIRECTION DOESN'T INTERSECT GLOBE! \n");
//    else {
//        pvl.copyFrom(g->GetEllipsoid()->ToGeodetic3D(pv).GetLatLon());
//        distv = pv.sub(pos).Magnitude();
//    }
//}
//
//
//// THIS COPY CONSTRUCTOR IS ONLY FOR THE JAVA CONVERTER
//
//Camera::Camera(const Camera &c):
////frustumFactor(c.frustumFactor),
//lastYValid(c.lastYValid),
////dragWx(c.dragWx), dragWy(c.dragWy), dragWz(c.dragWz),
//dragAngle(c.dragAngle), dragStep(c.dragStep),
//rotAngle(c.rotAngle), rotStep(c.rotStep),
//zoomDesp(c.zoomDesp), zoomStep(c.zoomStep),
//moveDesp(c.moveDesp), moveStep(c.moveStep),
//gotoposDesp(c.gotoposDesp), gotoposStep(c.gotoposStep),
////upx(c.upx), upy(c.upy), upz(c.upz),
////radius(c.radius),
//distv(c.distv),
//viewIntersectGlobe(c.viewIntersectGlobe),
//pitch(c.pitch), roll(c.roll) {
//    up.copyFrom(c.up);
//    dragW.copyFrom(c.dragW);
//    pv.X = c.pv.X;
//    pv.Y = c.pv.Y;
//    pv.Z = c.pv.Z;
//    pos.X = c.pos.X;
//    pos.Y = c.pos.Y;
//    pos.Z = c.pos.Z;
//    center.X = c.center.X;
//    center.Y = c.center.Y;
//    center.Z = c.center.Z;
//    //posl.lat=c.posl.lat;  posl.lon=c.posl.lon;
//    posl.copyFrom(c.posl);
//    //pvl.lat=c.pvl.lat;  pvl.lon=c.pvl.lon;
//    pvl.copyFrom(c.pvl);
//    for (int i = 0; i < 4; i++) {
//        viewport[i] = c.viewport[i];
//    }
//    for (int i = 0; i < 16; i++) {
//        lookAt[i] = c.lookAt[i];
//    }
//    for (int i = 0; i < 16; i++) {
//        projection[i] = c.projection[i];
//    }
//
//}
//
//Geodetic3D Camera::GetPosGeo3D() {
//    Vector3D v(pos.X, pos.Y, pos.Z);
//    return SceneController::GetInstance()->getGlobe()->GetEllipsoid()->ToGeodetic3D(v);
//}
//
//
//void Camera::SetPosGeo3D(Geodetic3D g3) {
//    // set position
//    Globe *g = SceneController::GetInstance()->getGlobe();
//    Vector3D v = g->GetEllipsoid()->ToVector3D(g3);
//    pos.X = v.X;
//    pos.Y = v.Y;
//    pos.Z = v.Z;
//    center.X = center.Y = center.Z = 0;
//
//    // compute position in lat/lon
//    //posl.lat = g3.latitude();
//    //posl.lon = g3.longitude();
//    posl.copyFrom(g3.GetLatLon());
//    //radius = pos.Dist2Origin();
//
//    // set vertical axe
//    //double cosLon = posl.CosLon();
//    //double sinLon = posl.SinLon();
//    Angle lon = posl.longitude();
//    switch (View::GetInstance()->GetScreenOrientation()) {
//        case Portrait:
//            up.X = 0;
//            up.Y = 0;
//            up.Z = 1;
//            break;
//        case LandscapeRight:
//            up.X = -lon.sinus();
//            up.Y = lon.cosinus();
//            up.Z = 0;
//            break;
//        case InversePortrait:
//            up.X = 0;
//            up.Y = 0;
//            up.Z = -1;
//            break;
//        case LandscapeLeft:
//            up.X = lon.sinus();
//            up.Y = -lon.cosinus();
//            up.Z = 0;
//            break;
//    }
//
//    // WARNING: this code is for working properly in the iphone simulator
//    if (fabs(up.X + up.Y + up.Z) < 0.1 || fabs(up.X + up.Y + up.Z) > 2) {
//        up.X = up.Y = 0;
//        up.Z = 1;
//    }
//
//
//    /*iprintf ("SetGeo3D: O=(%.1f,%.1f,%.1f)  C=(%.1f,%.1f,%.1f)  up=(%.1f,%.1f,%.1f)\n",
//             pos.x, pos.y, pos.z, center.x, center.y, center.z, upx, upy, upz);	 */
//}
//
//
//void Camera::SetPosXYZ(const Vector3D p, double lon) {
//    // set position
//    //pos = Point3D(p);
//    pos.X = p.X;
//    pos.Y = p.Y;
//    pos.Z = p.Z;
//
//    //pos = p;
//    center.X = center.Y = center.Z = 0;
//
//    // set vertical axe
//    double cosLon = cos(lon / 180 * PI);
//    double sinLon = sin(lon / 180 * PI);
//    switch (View::GetInstance()->GetScreenOrientation()) {
//        case Portrait:
//            up.X = 0;
//            up.Y = 1;
//            up.Z = 0;
//            break;
//        case LandscapeRight:
//            up.X = cosLon;
//            up.Y = 0;
//            up.Z = -sinLon;
//            break;
//        case InversePortrait:
//            up.X = 0;
//            up.Y = -1;
//            up.Z = 0;
//            break;
//        case LandscapeLeft:
//            up.X = -cosLon;
//            up.Y = 0;
//            up.Z = sinLon;
//            break;
//    }
//
//    // WARNING: this code is for working properly in the iphone simulator
//    if (fabs(up.X + up.Y + up.Z) < 0.1 || fabs(up.X + up.Y + up.Z) > 2) {
//        up.X = up.Z = 0;
//        up.Y = 1;
//    }
//
//
//    iprintf ("SetPos: O=(%.1f,%.1f,%.1f)  C=(%.1f,%.1f,%.1f)  up=(%.1f,%.1f,%.1f)\n",
//    pos.X, pos.Y, pos.Z, center.X, center.Y, center.Z, up.X, up.Y, up.Z);
//}
//
///*
//const void Camera::SetPosLatLon (const LatLon l, const double R)
//{
//    Point3D p (l, R);
//	SetPosXYZ (p, posl.lon);
//}*/
//
//
//void Camera::InitMovement() {
//    dragAngle = dragStep = 0;
//    zoomDesp = zoomStep = 0;
//    rotAngle = rotStep = 0;
//    moveDesp = 0;
//    moveStep = (float) (1.0 / NUM_FRAMES_DOUBLE_TAP);
//    gotoposDesp = 0;
//    gotoposStep = (float) (1.0 / NUM_FRAMES_GO_TO_POS);
//    gotoposIter = 0;
//}
//
//
//bool Camera::IntersectionViewWithGlobe(Vector3D &p) {
//    //return IntersectionRayWithGlobe(center.x-pos.x, center.y-pos.y, center.z-pos.z, p);
//    Vector3D ray = center.sub(pos);
//    return IntersectionRayWithGlobe(ray, p);
//}
//
//
//bool Camera::IntersectionRayWithGlobe(Vector3D ray, Vector3D &p) {
//    Globe *g = SceneController::GetInstance()->getGlobe();
//    //Vector3D P0 (pos.x, pos.y, pos.z);
//    //Vector3D ray (rx, ry, rz);
//    vector<double> t = g->GetEllipsoid()->Intersections(pos, ray);
//    if (t.empty()) return false;
//    //p.x=pos.x+t[0]*ray.X;  p.y=pos.y+t[0]*ray.Y;  p.z=pos.z+t[0]*ray.Z;  
//    Vector3D solution = pos.sum(ray.mult(t[0]));
//    p.copyFrom(solution);
//    return true;
//}
//
//
///*
//void Camera::GetHorizVector (double &ux, double &uy, double &uz) const
//{
//	double M0[16];
//	Glu::ComputeLookAtMatrix (pos, center, upx, upy, upz, M0);
//	
//	switch (View::GetInstance()->GetScreenOrientation()) {
//		case Portrait:
//			ux=M0[0];  uy=M0[4];  uz=M0[8];
//			break;
//		case LandscapeRight:
//			ux=M0[1];  uy=M0[5];  uz=M0[9];
//			break;
//		case InversePortrait:
//			ux=-M0[0];  uy=-M0[4];  uz=-M0[8];
//			break;
//		case LandscapeLeft:
//			ux=-M0[1];  uy=-M0[5];  uz=-M0[9];
//			break;
//	}
//}
// */
//
//Vector3D Camera::GetHorizVector() const {
//    double ux = 0, uy = 0, uz = 0;
//    double M0[16];
//    Glu::ComputeLookAtMatrix(pos, center, up, M0);
//
//    switch (View::GetInstance()->GetScreenOrientation()) {
//        case Portrait:
//            ux = M0[0];
//            uy = M0[4];
//            uz = M0[8];
//            break;
//        case LandscapeRight:
//            ux = M0[1];
//            uy = M0[5];
//            uz = M0[9];
//            break;
//        case InversePortrait:
//            ux = -M0[0];
//            uy = -M0[4];
//            uz = -M0[8];
//            break;
//        case LandscapeLeft:
//            ux = -M0[1];
//            uy = -M0[5];
//            uz = -M0[9];
//            break;
//    }
//
//    Vector3D v(ux, uy, uz);
//    return v;
//}
//
///*
//
//void Camera::Pixel2Vector (double px, double py, double modelView[16], 
//						   double &ux, double &uy, double &uz) const
//{
//	double posX=0, posY=0, posZ=0;
//	py = viewport[3] - py;
//	double proj[16];
//	for (int n=0; n<16; n++) proj[n] = projection[n];
//    Glu::gluUnProject (px, py, 0, modelView, proj, viewport, posX, posY, posZ);
//	
//	ux = posX - pos.x;
//    uy = posY - pos.y;
//    uz = posZ - pos.z;	
//}
// 
// */
//
//Vector3D Camera::Pixel2Vector(double px, double py, double modelView[16]) const {
//    py = viewport[3] - py;
//    double proj[16];
//    for (int n = 0; n < 16; n++) proj[n] = projection[n];
//
//    Vector3D *obj = Glu::gluUnProject(px, py, 0, modelView, proj, viewport);
//
//    //double ux = obj->X - pos.x;
//    //double uy = obj->Y - pos.y;
//    //double uz = obj->Z - pos.z;*/
//    //Vector3D v(ux,uy,uz);
//    Vector3D v = obj->sub(pos);
//
//    delete obj;
//    return v;
//}
//
//
//bool Camera::Pixel2GlobeSurface(double px, double py, double modelView[16], Vector3D &p) {
//    //double ux=0, uy=0, uz=0;
//    //Pixel2Vector(px, py, modelView, ux, uy, uz);
//    //return IntersectionRayWithGlobe (ux, uy, uz, p);	
//
//    Vector3D u = Pixel2Vector(px, py, modelView);
//    return IntersectionRayWithGlobe(u, p);
//}
//
//
//bool Camera::GlobeSurface2Pixel(Geodetic3D g3, double modelView[16], ProjectedPixel &p) {
//    // test first if point is hidden side of the globe
//    Globe *g = SceneController::GetInstance()->getGlobe();
//    Vector3D v = g->GetEllipsoid()->ToVector3D(g3);
//    //Vector3D cam(pos.x-v.X, pos.y-v.Y, pos.z-v.Z);
//    Vector3D cam = pos.sub(v);
//    double dot = v.Dot(cam);
//    if (dot < 0) return false;
//
//    // compute pixel coordinates
//    double proj[16];
//    for (int n = 0; n < 16; n++) proj[n] = projection[n];
//    Vector3D *pixel = Glu::gluProject(v.X, v.Y, v.Z, modelView, proj, viewport);
//    if (!pixel) return false;
//
//    // test if pixel are inside canvas
//    p.x = pixel->X;
//    p.y = viewport[3] - pixel->Y;
//    delete pixel;
//    if (p.x < 0 || p.x >= viewport[2] || p.y < 0 || p.y >= viewport[3]) return false;
//
//    // computes inclination
//    p.incl = dot / v.Magnitude() / cam.Magnitude();
//    return true;
//}
//
//
//bool Camera::ZoomCameraAuto(Vector3D p0, const Camera &camera0) {
//    //Globe *g = SceneController::GetInstance()->getGlobe();
//    //double R = g->GetGlobeRadius();
//
//    // compute distance to the ground
//    //double wx=p0.x-pos.x, wy=p0.y-pos.y, wz=p0.z-pos.z;
//    Vector3D w = p0.sub(pos);
//    //double dist = sqrt (wx*wx+wy*wy+wz*wz);
//    double dist = w.Magnitude();
//
//    // compute params
//    zoomStep *= AUTO_ZOOM_FRICTION;
//
//    // displacement value must not be high
//    if (zoomStep / dist > 0.10) {
//        zoomStep = dist * 0.10;
//    }
//
//    // update other params
//    double desp = zoomDesp + zoomStep;
//    zoomDesp = desp;
//    rotStep *= AUTO_ROTATE_FRICTION;
//    double angle = rotAngle + rotStep;
//    rotAngle = angle;
//
//    // recompute displacement value depending on the distance to camera0
//    //wx=p0.x-camera0.pos.x;  wy=p0.y-camera0.pos.y;  wz=p0.z-camera0.pos.z;
//    w.copyFrom(p0.sub(camera0.pos));
//    //double dist0 = sqrt (wx*wx+wy*wy+wz*wz);
//    double dist0 = w.Magnitude();
//    desp /= dist0;
//
//    // don't allow much closer
//    if (dist0 * (1 - desp) < MIN_CAMERA_HEIGHT) return false;
//
//    // don't allow much further away
//    Vector3D radius = SceneController::GetInstance()->getGlobe()->GetEllipsoid()->getRadii();
//    double R = (radius.X + radius.Y + radius.Z) / 3;
//    if (dist0 * (1 - desp) > 11 * R) return false;
//
//    // update camera
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    Glu::ComputeRotationMatrix(rotAngle, p0, M);
//    Vector3D w1 = w.mult(desp);
//    //Glu::ComputeTranslationMatrix (desp*wx, desp*wy, desp*wz, M);
//    Glu::ComputeTranslationMatrix(w1, M);
//    ApplyTransform(camera0, M);
//
//    // if movement step too low, stop animation
//    if (fabs(zoomStep) < 1e-2 && fabs(rotStep) < 1e-2) {
//        zoomStep = rotStep = 0;
//        return false;
//    } else {
//        return true;
//    }
//}
//
//
//void Camera::ZoomCamera(Vector3D p0, double factor, double angle, const Camera &camera0) {
//    //Globe *g = SceneController::GetInstance()->getGlobe();
//    //double R = g->GetGlobeRadius();
//
//    // we move the camera in that direction
//    //double wx=p0.x-camera0.pos.x, wy=p0.y-camera0.pos.y, wz=p0.z-camera0.pos.z;
//    Vector3D w = p0.sub(camera0.pos);
//    //double dist = sqrt (wx*wx+wy*wy+wz*wz);
//    double dist = w.Magnitude();
//
//    // don't allow much closer
//    if (dist * factor < MIN_CAMERA_HEIGHT && factor < 0.999) return;
//
//    // don't allow much further away
//    Vector3D radius = SceneController::GetInstance()->getGlobe()->GetEllipsoid()->getRadii();
//    double R = (radius.X + radius.Y + radius.Z) / 3;
//    if (dist * factor > 11 * R && factor > 1.001) return;
//
//    // make rotation
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    double desp = 1 - factor;
//    Glu::ComputeRotationMatrix(angle, p0, M);
//    Vector3D w1 = w.mult(desp);
//    Glu::ComputeTranslationMatrix(w1, M);
//    ApplyTransform(camera0, M);
//
//    // save displacement and angle, in the case of zoom afterwards
//    zoomStep = desp * dist - zoomDesp;
//    zoomDesp = desp * dist;
//
//    // update angle value
//    if (angle > 5) angle -= 2 * PI;
//    if (angle < -5) angle += 2 * PI;
//    rotStep = angle - rotAngle;
//    if (fabs(rotStep) > 1) {
//        //iprintf ("rotstep grande = %f   angle=%f  rotangle=%f\n", rotStep, angle, rotAngle);
//        rotStep *= 1 / fabs(rotStep);
//    }
//    rotAngle = angle;
//
//
//    //iprintf ("zooming up=%f %f %f\n", upx, upy, upz);
//
//}
//
//
//void Camera::RotateCamera(Vector3D p0, float px, float py, float px0, float py0, const Camera &camera0) {
//    // compute normal vector to that point
//    //Vector3D normal(p0.x, p0.y, p0.z);
//    //Vector3D punto(p0.x, p0.y, p0.z);
//    Vector3D normal = SceneController::GetInstance()->getGlobe()->GetEllipsoid()->GeodeticSurfaceNormal(p0);
//
//    // vertical rotation around normal vector to globe
//    ScreenOrientation so = View::GetInstance()->GetScreenOrientation();
//    char signo = (char) ((so == Portrait || so == InversePortrait) ? -1 : 1);
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    Glu::ComputeTranslationMatrix(p0, M);
//    Glu::ComputeRotationMatrix((px - px0) * 0.25 / 180 * PI * signo, normal, M);
//    Vector3D p0_neg = p0.mult(-1.0);
//    Glu::ComputeTranslationMatrix(p0_neg, M);
//
//    // rotate less than 90 degrees or more than 180 is not allowed
//    //double modp = p0.Dist2Origin();
//    //double modp = normal.Magnitude();
//    //double pox=p0.x-camera0.pos.x, poy=p0.y-camera0.pos.y, poz=p0.z-camera0.pos.z;
//    Vector3D po = p0.sub(camera0.pos);
//    //double modpo=sqrt(pox*pox+poy*poy+poz*poz);
//    //double modpo = po.Magnitude();
//    //double pe = (p0.x*pox+p0.y*poy+p0.z*poz) / modp / modpo;
//    //double pe = (normal.X*pox+normal.Y*poy+normal.Z*poz) / modp / modpo;
//    double pe = normal.Normalize().Dot(po.Normalize());
//
//    if (pe < -1) pe = -1.0;
//    if (pe > 1) pe = 1.0;
//    double ang = acos(pe) * 180 / PI - (py - py0) * 0.25;
//
//    // don't allow a minimum height above ground
//    //Globe *g = SceneController::GetInstance()->getGlobe();
//    //double R = g->GetGlobeRadius();
//    if (py < lastYValid && ang < 179) lastYValid = py;
//    double height = GetPosGeo3D().height();
//    if (py > lastYValid && ang > 100 && height > MIN_CAMERA_HEIGHT * 0.25) lastYValid = py;
//
//    // horizontal rotation over the original camera horizontal axix
//    Vector3D u = camera0.GetHorizVector();
//    Glu::ComputeTranslationMatrix(p0, M);
//    Glu::ComputeRotationMatrix((lastYValid - py0) * 0.25 / 180 * PI, u, M);
//    Glu::ComputeTranslationMatrix(p0_neg, M);
//
//    // update camera only if new view intersects globe
//    Camera camTest;
//    camTest.ApplyTransform(camera0, M);
//    if (camTest.IntersectionViewWithGlobe(pv)) {
//        ApplyTransform(camera0, M);
//    }
//}
//
//
//bool Camera::DragCameraAuto(const Camera &camera0) {
//    //double threshold = radius*1e-11;
//    double threshold = 1e-7;
//
//    // compute angle
//    dragStep *= AUTO_DRAG_FRICTION;
//    double angle_rad = dragAngle + dragStep;
//    dragAngle = angle_rad;
//
//    //Geodetic3D g3 = GetPosGeo3D();
//    //printf ("dragstep=%.10f.  umbral=%.10f  angle_rad=%f\n", dragStep, threshold, angle_rad);
//
//    // update the camera
//    double M[16] = {1, 0, 0, 0,
//            0, 1, 0, 0,
//            0, 0, 1, 0,
//            0, 0, 0, 1};
//    Glu::ComputeRotationMatrix(-angle_rad, dragW, M);
//    ApplyTransform(camera0, M);
//
//    // if it is too low, stop animation
//    if (fabs(dragStep) < threshold) {
//        dragStep = 0;
//        return false;
//    } else {
//        return true;
//    }
//
//}
//
//
//void Camera::DragCamera(Vector3D p0, double px, double py,
//        double modelView[16], const Camera &camera0) {
//    double t = 0;
//
//    // compute radius for the rotation
//    //double R0 = p0.Dist2Origin();
//    double R0 = p0.Magnitude();
//
//    // U represents the vector from eye to new pixel
//    //double ux=0, uy=0, uz=0;
//    //camera0.Pixel2Vector (px+0.5, py+0.5, modelView, ux, uy, uz);
//
//    Vector3D u = camera0.Pixel2Vector(px + 0.5, py + 0.5, modelView);
//    //double ux=u.X, uy=u.Y, uz=u.Z;
//
//    // compute the point in this ray that are to a distance R from the origin.
//    //double U2 = ux*ux + uy*uy + uz*uz;
//    double U2 = u.MagnitudeSquared();
//    //double O2 = camera0.pos.x*camera0.pos.x + camera0.pos.y*camera0.pos.y + camera0.pos.z*camera0.pos.z;
//    Vector3D pos_camera0(camera0.pos);
//    double O2 = pos_camera0.MagnitudeSquared();
//    //double OU = camera0.pos.x*ux + camera0.pos.y*uy + camera0.pos.z*uz;
//    double OU = pos_camera0.Dot(u);
//    double a = U2;
//    double b = 2 * OU;
//    double c = O2 - R0 * R0;
//    double rad = b * b - 4 * a * c;
//
//    // if there is solution, the ray intersects the sphere
//    if (rad > 0) {
//        // compute the final point (the smaller positive t value)
//        t = (-b - sqrt(rad)) / (2 * a);
//        if (t < 1) t = (-b + sqrt(rad)) / (2 * a);
//        // if the ideal ray intersects, but not the mesh --> case 2
//        if (t < 1) rad = -12345;
//    }
//
//    // if no solution found, find a point in the contour line
//    if (rad < 0) {
//        double D = sqrt(O2);
//        double co2 = R0 * R0 / (D * D);
//        //co2 *= 1.5;    // para suavizar el salto al salirme de la esfera
//        double a_ = OU * OU - co2 * O2 * U2;
//        double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
//        double c_ = O2 * O2 - co2 * O2 * O2;
//        double rad_ = b_ * b_ - 4 * a_ * c_;
//        t = (-b_ - sqrt(rad_)) / (2 * a_);
//    }
//
//    // compute the final point
//    //double x1 = camera0.pos.x + t*ux;
//    //double y1 = camera0.pos.y + t*uy;
//    //double z1 = camera0.pos.z + t*uz;
//    //double R1 = sqrt (x1*x1 + y1*y1 + z1*z1);
//    Vector3D p1 = pos_camera0.sum(u.mult(t));
//
//    // compute the rotation axe
//    //dragWx = p0.y*z1 - p0.z*y1;
//    //dragWy = p0.z*x1 - p0.x*z1;
//    //dragWz = p0.x*y1 - p0.y*x1;
//    dragW.copyFrom(p0.Cross(p1));
//
//    // compute the angle
//    //double angle_rad = acos ((p0.x*x1 + p0.y*y1 + p0.z*z1) / R0 / R1);
//    double angle_rad = acos(p0.Normalize().Dot(p1.Normalize()));
//    IUtils *ut = SceneController::GetInstance()->GetFactory()->GetUtils();
//    if (ut->isNaN(angle_rad)) return;
//
//    // update the camera
//    double M[16] = {1, 0, 0, 0,
//            0, 1, 0, 0,
//            0, 0, 1, 0,
//            0, 0, 0, 1};
//    Glu::ComputeRotationMatrix(-angle_rad, dragW, M);
//    ApplyTransform(camera0, M);
//
//    // save angles in the case of drag afterwards
//    dragStep = angle_rad - dragAngle;
//    dragAngle = angle_rad;
//
//}
//
//
//void Camera::ApplyTransform(const Camera &camera0, double M[16]) {
//    IUtils *u = SceneController::GetInstance()->GetFactory()->GetUtils();
//    if (u->isNaN(M[0])) {
//        u->debug("CAMERA FAILURE!\n");
//        return;
//    }
//
//    pos.X = camera0.pos.X * M[0] + camera0.pos.Y * M[4] + camera0.pos.Z * M[8] + M[12];
//    pos.Y = camera0.pos.X * M[1] + camera0.pos.Y * M[5] + camera0.pos.Z * M[9] + M[13];
//    pos.Z = camera0.pos.X * M[2] + camera0.pos.Y * M[6] + camera0.pos.Z * M[10] + M[14];
//    center.X = camera0.center.X * M[0] + camera0.center.Y * M[4] + camera0.center.Z * M[8] + M[12];
//    center.Y = camera0.center.X * M[1] + camera0.center.Y * M[5] + camera0.center.Z * M[9] + M[13];
//    center.Z = camera0.center.X * M[2] + camera0.center.Y * M[6] + camera0.center.Z * M[10] + M[14];
//    up.X = camera0.up.X * M[0] + camera0.up.Y * M[4] + camera0.up.Z * M[8];
//    up.Y = camera0.up.X * M[1] + camera0.up.Y * M[5] + camera0.up.Z * M[9];
//    up.Z = camera0.up.X * M[2] + camera0.up.Y * M[6] + camera0.up.Z * M[10];
//
//
//}
//
///*
//double Camera::GetDistanceToPoint (double x, double y, double z)
//{
//	return sqrt((x-pos.x)*(x-pos.x)+(y-pos.y)*(y-pos.y)+(z-pos.z)*(z-pos.z)); 
//}*/
//
//
//double Camera::GetDistanceToPoint(Vector3D p) {
//    //return GetDistanceToPoint(v.X, v.Y, v.Z);
//    return p.sub(pos).Magnitude();
//}
//
//
//double Camera::GetDistanceToPoint(Geodetic3D g3) {
//    Globe *g = SceneController::GetInstance()->getGlobe();
//    Vector3D p = g->GetEllipsoid()->ToVector3D(g3);
//    return GetDistanceToPoint(p);
//}
//
//
//void Camera::RotateScreen(int angle) {
//    // compute rotation matrix
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    Vector3D cp = center.sub(pos);
//    //Glu::ComputeRotationMatrix (float(angle)/180*PI, center.x-pos.x, center.y-pos.y, center.z-pos.z, M);
//    Glu::ComputeRotationMatrix(float(angle) / 180 * PI, cp, M);
//
//    // update vertical direction
//    //double up[3];
//    Vector3D up_temp(up);
//    //up[0] = upx*M[0] + upy*M[4] + upz*M[8];
//    //up[1] = upx*M[1] + upy*M[5] + upz*M[9];
//    //up[2] = upx*M[2] + upy*M[6] + upz*M[10];
//    //upx=up[0];  upy=up[1];  upz=up[2];
//    up.X = up_temp.X * M[0] + up_temp.Y * M[4] + up_temp.Z * M[8];
//    up.Y = up_temp.X * M[1] + up_temp.Y * M[5] + up_temp.Z * M[9];
//    up.Z = up_temp.X * M[2] + up_temp.Y * M[6] + up_temp.Z * M[10];
//
//
//    /*iprintf ("RotateScreen: O=(%.1f,%.1f,%.1f)  C=(%.1f,%.1f,%.1f)  up=(%.1f,%.1f,%.1f)\n",
//pos.x, pos.y, pos.z, center.x, center.y, center.z, upx, upy, upz);*/
//
//}
//
//
//bool Camera::MoveCameraAuto(Vector3D p0, Vector3D p1, const Camera &camera0) {
//    // compute displacement
//    moveDesp += moveStep;
//    if (moveDesp > 1) {
//        moveDesp = 1;
//        moveStep = 0;
//    }
//
//    // compute the rotation axe
//    //double jx = p0.y*p1.z - p0.z*p1.y;
//    //double jy = p0.z*p1.x - p0.x*p1.z;
//    //double jz = p0.x*p1.y - p0.y*p1.x;
//    Vector3D j = p0.Cross(p1);
//
//    // compute the angle
//    //double R0 = p0.Dist2Origin();
//    //double R1 = p1.Dist2Origin();
//    //double angle_rad = acos ((p0.x*p1.x + p0.y*p1.y + p0.z*p1.z) / R0 / R1);
//    double angle_rad = acos(p0.Normalize().Dot(p1.Normalize()));
//    IUtils *u = SceneController::GetInstance()->GetFactory()->GetUtils();
//    if (u->isNaN(angle_rad)) return false;
//
//    // compute the displacement
//    //double wx=p0.x-camera0.pos.x, wy=p0.y-camera0.pos.y, wz=p0.z-camera0.pos.z;
//    Vector3D w = p0.sub(camera0.pos);
//    //double dist = sqrt (wx*wx+wy*wy+wz*wz);
//    double dist = w.Magnitude();
//    double desp = moveDesp * DOUBLE_TAP_MOVEMENT_PERCENTAGE;
//
//    // don't allow much closer
//    if (dist * (1 - desp) < MIN_CAMERA_HEIGHT) return false;
//
//    // update the camera
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    Glu::ComputeRotationMatrix(angle_rad * moveDesp, j, M);
//    Vector3D w1 = w.mult(desp);
//    //Glu::ComputeTranslationMatrix (desp*wx, desp*wy, desp*wz, M);
//    Glu::ComputeTranslationMatrix(w1, M);
//    ApplyTransform(camera0, M);
//
//    if (moveStep < 1e-8)
//        return false;
//    else
//        return true;
//}
//
//
//bool Camera::GoToPosAuto(Vector3D p0, Vector3D p2, const Camera &camera0) {
//    // increment iterations count
//    gotoposIter++;
//
//    // Step 1: we move back a little
//    if (gotoposIter <= 50) {
//        float desp1 = (float) (gotoposIter * 0.02);
//
//        // compute axe and angle to achieve view perpendicular to the globe surface (camera center in globe origin)
//        //double wx0=camera0.center.x-camera0.pos.x, wy0=camera0.center.y-camera0.pos.y, wz0=camera0.center.z-camera0.pos.z;
//        Vector3D center0(camera0.center), pos0(camera0.pos);
//        Vector3D w0 = center0.sub(pos0);
//        //double wx1=-camera0.pos.x, wy1=-camera0.pos.y, wz1=-camera0.pos.z;
//        Vector3D w1 = pos0.mult(-1.0);
//        //double ix = wy0*wz1 - wz0*wy1;
//        //double iy = wz0*wx1 - wx0*wz1;
//        //double iz = wx0*wy1 - wy0*wx1;
//        Vector3D i = w0.Cross(w1);
//        //double Q0 = sqrt (wx0*wx0+wy0*wy0+wz0*wz0);
//        //double Q1 = sqrt (wx1*wx1+wy1*wy1+wz1*wz1);
//        //double anglew_rad = acos ((wx0*wx1 + wy0*wy1 + wz0*wz1) / Q0 / Q1);
//        double anglew_rad = acos(w0.Normalize().Dot(w1.Normalize()));
//        IUtils *u = SceneController::GetInstance()->GetFactory()->GetUtils();
//        if (u->isNaN(anglew_rad) || anglew_rad < 0.0001) {
//            anglew_rad = 0;
//            i.X = i.Z = 0;
//            i.Y = 1;
//        }
//
//        // update the camera
//        double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//        Vector3D t = pos0.mult(0.25 * desp1);
//        //Glu::ComputeTranslationMatrix (0.25*desp1*camera0.pos.x, 0.25*desp1*camera0.pos.y, 0.25*desp1*camera0.pos.z, M);
//        Glu::ComputeTranslationMatrix(t, M);
//        //Glu::ComputeTranslationMatrix (camera0.pos.x, camera0.pos.y, camera0.pos.z, M);
//        Glu::ComputeTranslationMatrix(pos0, M);
//        Glu::ComputeRotationMatrix(anglew_rad * desp1, i, M);
//        //Glu::ComputeTranslationMatrix (-camera0.pos.x, -camera0.pos.y, -camera0.pos.z, M);
//        Vector3D pos0_neg = pos0.mult(-1.0);
//        Glu::ComputeTranslationMatrix(pos0_neg, M);
//        ApplyTransform(camera0, M);
//
//        // update center of view
//        //center.x = camera0.center.x * (1-desp1);
//        //center.y = camera0.center.y * (1-desp1);
//        //center.z = camera0.center.z * (1-desp1);
//        center.copyFrom(center0.mult(1 - desp1));
//
//        // end of animation in this frame
//        return true;
//    }
//
//    // new iter value
//    int iter = gotoposIter - 50;
//
//    // compute current position
//    //Point3D p1 (pos.x, pos.y, pos.z);
//    Vector3D p1(pos);
//
//    // compute the rotation axe
//    //Point3D j (p1.y*p2.z-p1.z*p2.y, p1.z*p2.x-p1.x*p2.z, p1.x*p2.y-p1.y*p2.x);
//    Vector3D j = p1.Cross(p2);
//    if (j.Magnitude() < 0.001) {
//        j.X = j.Z = 0;
//        j.Y = 1;
//    }
//
//    // compute the angle
//    //double R1 = p1.Dist2Origin();
//    //double R2 = p2.Dist2Origin();
//    //double angle_rad = acos ((p1.x*p2.x + p1.y*p2.y + p1.z*p2.z) / R1 / R2);
//    double angle_rad = acos(p1.Normalize().Dot(p2.Normalize()));
//    IUtils *utils = SceneController::GetInstance()->GetFactory()->GetUtils();
//    if (utils->isNaN(angle_rad)) angle_rad = 0;
//
//    // rotation
//    double factorRot = (iter < 200) ? 0.0005 * iter : 0.1;
//    double M[16] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
//    Glu::ComputeRotationMatrix(factorRot * angle_rad, j, M);
//    ApplyTransform(*this, M);
//
//    // compute factor to get closer, depending of the height and the iteration
//    //Globe *g = SceneController::GetInstance()->getGlobe();
//    //double R = g->GetGlobeRadius();
//    double factorProp = 1 - 0.0003 * iter;
//    if (iter > 100) factorProp = 0.97;
//    //double dist=pos.Dist2Origin()-R;
//    double dist = GetPosGeo3D().height();
//    if (dist < 40) factorProp = 0.99;
//    else if (dist < 250) factorProp = 0.9919 - 4.7619e-5 * dist;
//    else if (dist < 1600) factorProp = 0.98185 - 7.4074e-6 * dist;
//
//    // get closer using previous factor
//    //double radius = pos.Dist2Origin();
//    double radius = pos.Magnitude();
//    double R2 = p2.Magnitude();
//    double f = (R2 + factorProp * (radius - R2)) / radius;
//    //pos.x*=f;  pos.y*=f;  pos.z*=f;
//    pos.copyFrom(pos.mult(f));
//
//    // moving north vector to up direction in 100 iterations
//    //double ux=0, uy=0, uz=0;
//    Vector3D u;
//    //double cosLon = cos(posl.lon/180*PI);
//    //double sinLon = sin(posl.lon/180*PI);
//    //double cosLon = posl.CosLon();
//    //double sinLon = posl.SinLon();
//    Angle lon = posl.longitude();
//    switch (View::GetInstance()->GetScreenOrientation()) {
//        case Portrait:
//            u.X = 0.0;
//            u.Y = 0.0;
//            u.Z = 1.0;
//            break;
//        case LandscapeRight:
//            u.X = -lon.sinus();
//            u.Y = lon.cosinus();
//            u.Z = 0.0;
//            break;
//        case InversePortrait:
//            u.X = 0.0;
//            u.Y = 0.0;
//            u.Z = -1.0;
//            break;
//        case LandscapeLeft:
//            u.X = lon.sinus();
//            u.Y = -lon.cosinus();
//            u.Z = 0.0;
//            break;
//    }
//
//    //upx += factorRot * (ux-upx);
//    //upy += factorRot * (uy-upy);
//    //upz += factorRot * (uz-upz);
//    up.copyFrom(u.sub(up).mult(factorRot));
//
//    // finish when arrive too close
//    //if (pos.Dist2Origin()-R > 10*MIN_CAMERA_HEIGHT) return true; else return false;
//    if (GetPosGeo3D().height() > 10 * MIN_CAMERA_HEIGHT) return true; else return false;
//}
//
//
//double Camera::GetTilt() {
//    // vector u = camera direction
//    //double ux=pos.x-center.x, uy=pos.y-center.y, uz=pos.z-center.z;
//    Vector3D u = pos.sub(center);
//    //double modu=sqrt(ux*ux+uy*uy+uz*uz);
//
//    // vector n = normal to surface in central point of view
//    //double nx=pv.x, ny=pv.y, nz=pv.z;
//    //double modn=sqrt(nx*nx+ny*ny+nz*nz);
//
//    // compute cos of tilt
//    //double cosTilt = (ux*nx+uy*ny+uz*nz) / modu / modn;
//    double cosTilt = u.Normalize().Dot(pv.Normalize());
//    double tilt = acos(cosTilt);
//    IUtils *utils = SceneController::GetInstance()->GetFactory()->GetUtils();
//    if (utils->isNaN(tilt)) tilt = 0;
//    return tilt;
//}
//
//

