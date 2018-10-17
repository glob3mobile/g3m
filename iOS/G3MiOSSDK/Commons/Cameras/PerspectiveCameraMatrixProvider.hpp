//
//  PerspectiveCameraMatrixProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 17/10/2018.
//

#ifndef PerspectiveCameraMatrixProvider_hpp
#define PerspectiveCameraMatrixProvider_hpp

#include "Camera.hpp"
#include "MutableMatrix44D.hpp"
#include "Vector3D.hpp"

class PerspectiveCameraMatrixProvider : public CameraMatrixProvider{
    
    long long _lastCamTimestamp;
    MutableMatrix44D _model;
    MutableMatrix44D _projection;
    MutableMatrix44D _modelView;
public:
    
    PerspectiveCameraMatrixProvider():
    _lastCamTimestamp(0),
    _model(MutableMatrix44D::invalid()),
    _projection(MutableMatrix44D::invalid()),
    _modelView(MutableMatrix44D::invalid()){
    }
    
    void updateCamera(const Camera& cam);
    
    const MutableMatrix44D& getModelMatrix() const{
        return _model;
    }
    
    const MutableMatrix44D& getProjectionMatrix() const{
        return _projection;
    }
    
    const MutableMatrix44D& getModelViewMatrix() const{
        return _modelView;
    }
};

#endif /* PerspectiveCameraMatrixProvider_hpp */
