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

//class PointCloudMesh : public Mesh {
//protected:
//  const bool              _owner;
//  const Vector3D                _center;
//  const MutableMatrix44D* _translationMatrix;
//  const IFloatBuffer*           _vertices;
//  const IFloatBuffer*           _colors;
//  const float             _pointSize;
//  const bool              _depthTest;
//  Color _borderColor;
//    
//    IFloatBuffer*     _valuesInColorRange;
//    IFloatBuffer*     _nextValuesInColorRange;
//    const Color* _colorRangeAt0;
//    const Color* _colorRangeAt1;
//    ColorRangeGLFeature* _colorRangeGLFeature;
//    DynamicColorRangeGLFeature* _dynamicColorRangeGLFeature;
//    float _currentTime;
//  
//  mutable BoundingVolume* _boundingVolume;
//  BoundingVolume* computeBoundingVolume() const;
//  
//  GLState* _glState;
//  
//  void createGLState();
//  
////  const std::vector<IFloatBuffer*> _colorsCollection;
//  void applyColor();
//  
//  
//public:
//  
//  PointCloudMesh(bool owner,
//                 const Vector3D& center,
//                 const IFloatBuffer* vertices,
//                 float pointSize,
//                 const IFloatBuffer* colors,
//                 bool depthTest,
//                 const Color& borderColor,
//                 IFloatBuffer* valuesInColorRange = NULL,
//                 const Color* colorRangeAt0 = NULL,
//                 const Color* colorRangeAt1 = NULL,
//                 IFloatBuffer* nextValuesInColorRange = NULL,
//                 float currentTime = 0.0f);
//  
//  ~PointCloudMesh();
//  
//  BoundingVolume* getBoundingVolume() const;
//  
//  size_t getVertexCount() const;
//  
//  const Vector3D getVertex(size_t i) const;
//  
//  bool isTransparent(const G3MRenderContext* rc) const;
//  
//  void rawRender(const G3MRenderContext* rc,
//                 const GLState* parentGLState) const;
//  
////  IFloatBuffer* getColorsFloatBuffer() const{
////    return (IFloatBuffer*)_colorsCollection[0];
////  }
//  
////  void changeToColors(int i);
//  
//  void showNormals(bool v) const{} //NO NORMALS
//  
////  int getNumberOfColors() const{
////    return (int)_colorsCollection.size();
////  }
//  
//  void changeToColors(IFloatBuffer* colors);
//    
//    void setDynamicColorRangeValues(IFloatBuffer* values, IFloatBuffer* valuesNext){
//        if (_dynamicColorRangeGLFeature != NULL){
//            _dynamicColorRangeGLFeature->setValues(values, valuesNext);
//        }
//    }
//};

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
                   IFloatBuffer* valuesInColorRange = NULL,
                   const Color* colorRangeAt0 = NULL,
                   const Color* colorRangeAt1 = NULL,
                   IFloatBuffer* nextValuesInColorRange = NULL,
                   float currentTime = 0.0f):
    DirectMesh(GLPrimitive::points(),
               owner,
               center,
               vertices,
               1.0,
               pointSize,
               NULL,
               NULL,
               0.0f,
               false, //Depth test
               NULL,
               false,
               0,
               0,
               valuesInColorRange,
               colorRangeAt0,
               colorRangeAt1,
               nextValuesInColorRange,
               currentTime,
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
