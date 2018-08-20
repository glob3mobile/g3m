//
//  PointCloudMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__PointCloudMesh__
#define __G3MiOSSDK__PointCloudMesh__

#include "Mesh.hpp"

#include "Vector3D.hpp"
#include "GLState.hpp"
#include "DirectMesh.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

class PointCloudMesh: public DirectMesh{
    Vector3D _buildingAnchor;
    MutableMatrix44D _mt1;
    MutableMatrix44D _mt2;
public:
    
    PointCloudMesh(bool owner,
                   const Vector3D& buildingAnchor,
                   const Vector3D& center,
                   const IFloatBuffer* vertices,
                   float pointSize,
                   const IFloatBuffer* colors,
                   bool depthTest,
                   const Color& borderColor,
                   VertexColorScheme* vertexColorScheme):
    DirectMesh(GLPrimitive::points(),
               owner,
               center,
               vertices,
               1.0f,
               pointSize,
               NULL,
               NULL,
               0.0f,
               false, //Depth test
               NULL,
               false,
               0,
               0,
               vertexColorScheme,
               -1.0f),
    _buildingAnchor(buildingAnchor),
    _mt1(MutableMatrix44D::createTranslationMatrix(_buildingAnchor.times(-1))),
    _mt2(MutableMatrix44D::createTranslationMatrix(_buildingAnchor))
    {
        _glState->addGLFeature(new PointShapeGLFeature(borderColor), false);
    }
    
    void updatePopUpEffect(float completionRatio);
    
};

#endif
