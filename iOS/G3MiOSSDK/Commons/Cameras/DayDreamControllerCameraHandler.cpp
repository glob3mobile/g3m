//
//  DayDreamControllerCameraHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 09/03/2017.
//
//

#include "DayDreamControllerCameraHandler.hpp"
#include "Geodetic3D.hpp"
#include "ITimer.hpp"


void DayDreamControllerCameraHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext){


  if (_meshRenderer != NULL){
    
    
    if (_initialCamCS == NULL){
      
      ILogger::instance()->logInfo(cameraContext->getNextCamera()->
                                   getGeodeticPosition().description().c_str());
      
      _initialCamCS = new CoordinateSystem(cameraContext->getNextCamera()->getCameraCoordinateSystem());
    }
    
    
    _meshRenderer->clearMeshes();
    
    //Drawing initial camera
    _meshRenderer->addMesh(_initialCamCS->createMesh(5e7,
                                                     Color::red().muchDarker(),
                                                     Color::green().muchDarker(),
                                                     Color::blue().muchDarker()));
    
    //Global
    CoordinateSystem global = CoordinateSystem::global();
    
//    _meshRenderer->addMesh(global.createMesh(5e7, Color::red(), Color::green(), Color::blue()));
    
    //First we have to rotate the canonical CS to adjust it to the daydream's rotations
    MutableMatrix44D ddRotationAxisTrans = CoordinateSystem(Vector3D::UP_X, //X
                                                            Vector3D::DOWN_Z, //Y
                                                            Vector3D::UP_Y, //Z
                                                            Vector3D::ZERO).getRotationMatrix();
    
    CoordinateSystem globalAdjToDDRotations = global.applyRotation(ddRotationAxisTrans);
    
    
    //_meshRenderer->addMesh(globalAdjToDDRotations.createMesh(5e7, Color::red().muchDarker(), Color::green().muchDarker(), Color::blue().muchDarker()));
    
    //DayDream matrix placeholder!!!!!
    Angle angle = Angle::fromDegrees( rc->getFrameStartTimer()->nowInMilliseconds() / 1000 ).normalized();
    ILogger::instance()->logInfo("Angle %f", angle._degrees);
    
    //We must be able to rotate in "heading" with a rotation in Y
    MutableMatrix44D rotY = MutableMatrix44D::createRotationMatrix(angle, Vector3D::UP_Y);
    
    //RotX -> Pitch
    MutableMatrix44D rotX = MutableMatrix44D::createRotationMatrix(angle, Vector3D::UP_X);
    
    //RotZ -> Roll
    MutableMatrix44D rotZ = MutableMatrix44D::createRotationMatrix(angle, Vector3D::UP_Z);
    
    CoordinateSystem globalPlusDDAdjToDDRotations = globalAdjToDDRotations.applyRotation(rotZ);
    
    //_meshRenderer->addMesh(globalPlusDDAdjToDDRotations.createMesh(5e7, Color::red().muchDarker(), Color::green().muchDarker(), Color::blue().muchDarker()));
    
    CoordinateSystem globalPlusDD = globalPlusDDAdjToDDRotations.applyRotation(ddRotationAxisTrans.inversed());
    
    //_meshRenderer->addMesh(globalPlusDD.createMesh(5e7, Color::red(), Color::green(), Color::blue()));
    
    
    MutableMatrix44D iniCamGeoTransf = _initialCamCS->getRotationMatrix();
    CoordinateSystem geoTransGlobal = globalPlusDD.applyRotation(iniCamGeoTransf).changeOrigin(_initialCamCS->_origin);
    _meshRenderer->addMesh(geoTransGlobal.createMesh(5e7, Color::red(), Color::green(), Color::blue()));
    
    
    
    
    
  }

}
