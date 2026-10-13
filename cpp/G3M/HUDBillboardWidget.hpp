//
//  HUDBillboardWidget.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

#ifndef __G3M__HUDBillboardWidget__
#define __G3M__HUDBillboardWidget__

#include "HUDWidget.hpp"
#include "ChangedListener.hpp"

#include <vector>
#include <string>

class IImageFactory;
class HUDWidgetImage;
class GLState;
class TextureIDReference;
class BillboardGLFeature;
class ModelTransformGLFeature;
class TextureGLFeature;
class ViewportExtentGLFeature;
class IFloatBuffer;


// An image drawn with the Billboard shader at a pixel position: position, size, alpha and mirror can change every frame
class HUDBillboardWidget : public HUDWidget, public ChangedListener {
private:
  HUDWidgetImage* _image;

  // the point of the image (as drawn, not mirrored) placed at the position, in UV: (0,0) top-left; may lie outside the image
  float _anchorU;
  float _anchorV;

  // pixels, (0,0) top-left, the frame Camera::point2Pixel uses
  float _x;
  float _y;

  // pixels
  float _width;
  float _height;

  float _alpha;

  // flipped left to right, around the anchor
  bool _mirrored;

#ifdef C_CODE
  const TextureIDReference* _textureID;
  const G3MContext*         _context;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureID;
  private G3MContext         _context;
#endif

  GLState*                 _glState;
  BillboardGLFeature*      _billboardGLF;
  ModelTransformGLFeature* _modelTransformGLF;
  TextureGLFeature*        _textureGLF;
  ViewportExtentGLFeature* _viewportExtentGLF;
  IFloatBuffer*            _texCoords;

  std::vector<std::string> _errors;

  bool createGLState(const G3MRenderContext* rc);

  void releaseGLState();

  void updateGLState(const G3MRenderContext* rc);

protected:
  void rawRender(const G3MRenderContext* rc,
                 GLState* glState);

public:
  HUDBillboardWidget(IImageFactory* imageFactory,
                     float          anchorU,
                     float          anchorV);

  ~HUDBillboardWidget();

  void setPosition(float x,
                   float y) {
    _x = x;
    _y = y;
  }

  void setSize(float width,
               float height) {
    _width  = width;
    _height = height;
  }

  void setAnchor(float anchorU,
                 float anchorV) {
    _anchorU = anchorU;
    _anchorV = anchorV;
  }

  void setAlpha(float alpha) {
    _alpha = alpha;
  }

  void setMirrored(bool mirrored) {
    _mirrored = mirrored;
  }

  /** the image's own size in pixels, zero until it is built */
  int getImageWidth() const;

  int getImageHeight() const;

  void initialize(const G3MContext* context);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  RenderState getRenderState(const G3MRenderContext* rc);

  void changed();

};

#endif
