//
//  PerspectiveCameraMatrixProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 17/10/2018.
//

#include "PerspectiveCameraMatrixProvider.hpp"

void PerspectiveCameraMatrixProvider::updateCamera(const Camera& cam){
    if (cam.getTimestamp() != _lastCamTimestamp || !_model.isValid() || _projection.isValid()){
        _lastCamTimestamp = cam.getTimestamp();
        
        _projection.copyValue(MutableMatrix44D::createProjectionMatrix(cam.getFrustumData()));
        
        MutableVector3D pos = MutableVector3D(cam.getCartesianPosition());
        MutableVector3D center = MutableVector3D(cam.getCenter());
        MutableVector3D up = MutableVector3D(cam.getUp());
        
//        printf("pos: %.2f, %.2f, %.2f\n", pos.x(), pos.y(), pos.z());
//        printf("center: %.2f, %.2f, %.2f\n", center.x(), center.y(), center.z());
//        printf("UP: %.2f, %.2f, %.2f\n", up.x(), up.y(), up.z());
        
        _model.copyValue(MutableMatrix44D::createModelMatrix(pos, center, up));
        
//        printf("MODEL\n %s\n", _model.description().c_str());
        
        
        _modelView.copyValueOfMultiplication(_projection, _model);
    }
}
