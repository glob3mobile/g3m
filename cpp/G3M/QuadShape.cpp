//
//  QuadShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "QuadShape.hpp"

#include "IGLTextureID.hpp"
#include "IImage.hpp"
#include "TexturesHandler.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "TexturedMesh.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "Color.hpp"
#include "SimpleTextureMapping.hpp"
#include "G3MRenderContext.hpp"
#include "TimeInterval.hpp"

QuadShape::QuadShape(Geodetic3D* position,
                     AltitudeMode altitudeMode,
                     const URL& textureURL,
                     float width,
                     float height,
                     bool withNormals) :
AbstractMeshShape(position, altitudeMode),
_textureURL(textureURL),
_width(width),
_height(height),
_textureRequested(false),
_textureImage(NULL),
_color(NULL),
_withNormals(withNormals),
_depthTest(true),
_cullFace(false),
_culledFace(GLCullFace::back())
{

}

QuadShape::QuadShape(Geodetic3D* position,
                     AltitudeMode altitudeMode,
                     const IImage* textureImage,
                     float width,
                     float height,
                     bool withNormals) :
AbstractMeshShape(position, altitudeMode),
_textureURL(URL("", false)),
_width(width),
_height(height),
_textureRequested(true),
_textureImage(textureImage),
_color(NULL),
_withNormals(withNormals),
_depthTest(true),
_cullFace(false),
_culledFace(GLCullFace::back())
{

}


QuadShape::QuadShape(Geodetic3D* position,
                     AltitudeMode altitudeMode,
                     float width,
                     float height,
                     Color* color,
                     bool withNormals) :
AbstractMeshShape(position, altitudeMode),
_textureURL(URL("", false)),
_width(width),
_height(height),
_textureRequested(false),
_textureImage(NULL),
_color(color),
_withNormals(withNormals),
_depthTest(true),
_cullFace(false),
_culledFace(GLCullFace::back())
{

}

void QuadShape::setDepthTest(bool depthTest) {
  if (_depthTest != depthTest) {
    _depthTest = depthTest;
    cleanMesh();
  }
}

void QuadShape::setCullFace(bool cullFace) {
  if (_cullFace != cullFace) {
    _cullFace = cullFace;
    cleanMesh();
  }
}

void QuadShape::setCulledFace(int culledFace) {
  if (_culledFace != culledFace) {
    _culledFace = culledFace;
    cleanMesh();
  }
}

const TextureIDReference* QuadShape::getTextureID(const G3MRenderContext* rc) {
  if (_textureImage == NULL) {
    return NULL;
  }

  const TextureIDReference* texID = rc->getTexturesHandler()->getTextureIDReference(_textureImage,
                                                                                    GLFormat::rgba(),
                                                                                    _textureURL._path,
                                                                                    false,
                                                                                    GLTextureParameterValue::clampToEdge(),
                                                                                    GLTextureParameterValue::clampToEdge());

  delete _textureImage;
  _textureImage = NULL;

  if (texID == NULL) {
    rc->getLogger()->logError("Can't load texture %s", _textureURL._path.c_str());
  }

  return texID;
}

class QuadShape_IImageDownloadListener : public IImageDownloadListener {
private:
  QuadShape* _quadShape;

public:

  QuadShape_IImageDownloadListener(QuadShape* quadShape) :
  _quadShape(quadShape)
  {

  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired)  {
    _quadShape->imageDownloaded(image);
  }

  void onError(const URL& url) {

  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired)  {

  }
};

void QuadShape::imageDownloaded(IImage* image) {
  _textureImage = image;

  cleanMesh();
}

QuadShape::~QuadShape() {
  delete _color;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

Mesh* QuadShape::createMesh(const G3MRenderContext* rc) {
  if (!_textureRequested) {
    _textureRequested = true;
    if (_textureURL._path.length() != 0) {
      rc->getDownloader()->requestImage(_textureURL,
                                        1000000,
                                        TimeInterval::fromDays(30),
                                        true,
                                        new QuadShape_IImageDownloadListener(this),
                                        true);
    }
  }

  const float halfWidth  = _width / 2.0f;
  const float halfHeight = _height / 2.0f;

  const float left   = -halfWidth;
  const float right  = +halfWidth;
  const float bottom = -halfHeight;
  const float top    = +halfHeight;

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add(left,  bottom, 0);
  vertices->add(right, bottom, 0);
  vertices->add(left,  top,    0);
  vertices->add(right, top,    0);

  Color* color = (_color == NULL) ? NULL : new Color(*_color);
  Mesh* im = NULL;
  if (_withNormals) {
    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    normals->add(0.0, 0.0, 1.0);
    normals->add(0.0, 0.0, 1.0);
    normals->add(0.0, 0.0, 1.0);
    normals->add(0.0, 0.0, 1.0);

    im = new DirectMesh(GLPrimitive::triangleStrip(), /* const int primitive          */
                        true,                         /* bool owner                   */
                        vertices->getCenter(),        /* const Vector3D& center       */
                        vertices->create(),           /* const IFloatBuffer* vertices */
                        1,                            /* float lineWidth              */
                        1,                            /* float pointSize              */
                        color,                        /* const Color* flatColor       */
                        NULL,                         /* const IFloatBuffer* colors   */
                        _depthTest,                   /* bool depthTest               */
                        normals->create(),            /* const IFloatBuffer* normals  */
                        false,                        /* bool polygonOffsetFill       */
                        0,                            /* float polygonOffsetFactor    */
                        0,                            /* float polygonOffsetUnits     */
                        _cullFace,                    /* bool cullFace                */
                        _culledFace                   /* int  culledFace              */);

    delete normals;
  }
  else {
    im = new DirectMesh(GLPrimitive::triangleStrip(), /* const int primitive          */
                        true,                         /* bool owner                   */
                        vertices->getCenter(),        /* const Vector3D& center       */
                        vertices->create(),           /* const IFloatBuffer* vertices */
                        1,                            /* float lineWidth              */
                        1,                            /* float pointSize              */
                        color,                        /* const Color* flatColor       */
                        NULL,                         /* const IFloatBuffer* colors   */
                        _depthTest,                   /* bool depthTest               */
                        NULL,                         /* const IFloatBuffer* normals  */
                        false,                        /* bool polygonOffsetFill       */
                        0,                            /* float polygonOffsetFactor    */
                        0,                            /* float polygonOffsetUnits     */
                        _cullFace,                    /* bool cullFace                */
                        _culledFace                   /* int  culledFace              */);
  }

  delete vertices;

  const TextureIDReference* texID = getTextureID(rc);
  if (texID == NULL) {
    return im;
  }

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 1);
  texCoords.add(1, 1);
  texCoords.add(0, 0);
  texCoords.add(1, 0);

  TextureMapping* texMap = new SimpleTextureMapping(texID,
                                                    texCoords.create(),
                                                    true,
                                                    true);
  
  return new TexturedMesh(im, true, texMap, true, true);
}

std::vector<double> QuadShape::intersectionsDistances(const Planet* planet,
                                                      const Vector3D& origin,
                                                      const Vector3D& direction) const {
  std::vector<double> intersections;
  return intersections;
}
