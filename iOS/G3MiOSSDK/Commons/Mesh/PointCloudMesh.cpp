//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "PointCloudMesh.hpp"

#include "IFloatBuffer.hpp"
#include "Color.hpp"
#include "GL.hpp"
#include "Box.hpp"

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "CompositeMesh.hpp"
#include "Sphere.hpp"

#include "Camera.hpp"

#include "GLFeature.hpp"

void PointCloudMesh::updatePopUpEffect(float completionRatio){
    if (completionRatio > 1.0){
        setTransformation(Matrix44D::createIdentity());
    } else{
        
        const float a = completionRatio*(2.0f-completionRatio); //Ease-Out
        
        
        const float scale = 1.0f + (1.0f - a) * 0.2f;
        
        MutableMatrix44D ms = MutableMatrix44D::createScaleMatrix(scale, scale, scale);
        MutableMatrix44D m = _mt2.multiply(ms).multiply(_mt1);
        setTransformation(m.asMatrix44D());
    }
}

