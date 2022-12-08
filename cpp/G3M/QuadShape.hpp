//
//  QuadShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3M__QuadShape__
#define __G3M__QuadShape__

#include "AbstractMeshShape.hpp"

#include "URL.hpp"

class TextureIDReference;


class QuadShape : public AbstractMeshShape {
private:
  const URL    _textureURL;
  const float  _width;
  const float  _height;
  const Color* _color;

  bool _depthTest;
  bool _cullFace;
  int  _culledFace;

  bool _textureRequested;
#ifdef C_CODE
  const IImage* _textureImage;
#endif
#ifdef JAVA_CODE
  private IImage _textureImage;
#endif
  
  const TextureIDReference* getTextureID(const G3MRenderContext* rc);
  
  const bool _withNormals;
  
protected:
  Mesh* createMesh(const G3MRenderContext* rc);
  
public:
  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            const URL& textureURL,
            float width,
            float height,
            bool withNormals);
  
  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            const IImage* textureImage,
            float width,
            float height,
            bool withNormals);

  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            float width,
            float height,
            Color* color,
            bool withNormals);
  
  virtual ~QuadShape();

  void setDepthTest(bool depthTest);

  void setCullFace(bool cullFace);

  void setCulledFace(int culledFace);

  void imageDownloaded(IImage* image);
  
  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Vector3D& origin,
                                             const Vector3D& direction) const;
  
};

#endif
