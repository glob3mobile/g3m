//
//  ICanvas.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#ifndef __G3MiOSSDK__ICanvas__
#define __G3MiOSSDK__ICanvas__

class Color;
class IImageListener;
class GFont;
#include <string>

#include "Vector2F.hpp"

class ICanvas {
private:
  int _width;
  int _height;
  GFont* _currentFont;

  bool isInitialized() const {
    return (_width > 0) && (_height > 0);
  }

  inline void checkInitialized() const;
  inline void checkCurrentFont() const;

protected:

  virtual void _initialize(int width, int height) = 0;


  virtual void _setFillColor(const Color& color) = 0;

  virtual void _setStrokeColor(const Color& color) = 0;

  virtual void _setStrokeWidth(float width) = 0;


  virtual void _fillRectangle(float x, float y,
                              float width, float height) = 0;

  virtual void _strokeRectangle(float x, float y,
                                float width, float height) = 0;

  virtual void _fillAndStrokeRectangle(float x, float y,
                                       float width, float height) = 0;


  virtual void _fillRoundedRectangle(float x, float y,
                                     float width, float height,
                                     float radius) = 0;

  virtual void _strokeRoundedRectangle(float x, float y,
                                       float width, float height,
                                       float radius) = 0;
  virtual void _fillAndStrokeRoundedRectangle(float x, float y,
                                              float width, float height,
                                              float radius) = 0;

  virtual void _setShadow(const Color& color,
                          float blur,
                          float offsetX,
                          float offsetY) = 0;

  virtual void _removeShadow() = 0;


  virtual void _createImage(IImageListener* listener,
                            bool autodelete) = 0;

  virtual void _setFont(const GFont& font) = 0;

  virtual const Vector2F _textExtent(const std::string& text) = 0;

  virtual void _fillText(const std::string& text,
                         float x, float y) = 0;

public:
  ICanvas() :
  _width(-1),
  _height(-1),
  _currentFont(NULL)
  {
  }

  virtual ~ICanvas();

  /**
   Initialize the Canvas, must be called just one time before calling most methods.
   */
  void initialize(int width, int height);


  /**
   Returns the size of the text if it were to be rendered with the actual font on a single line.

   NOTE: The current font has to be set before calling this method.
   NOTE: No need to initialize the canvas before calling this method.
   */
  const Vector2F textExtent(const std::string& text);

  /**
   Set the actual font.

   NOTE: No need to initialize the canvas before calling this method.
   */
  void setFont(const GFont& font);

  void setFillColor(const Color& color);

  void setStrokeColor(const Color& color);

  void setStrokeWidth(float width);


  void setShadow(const Color& color,
                 float blur,
                 float offsetX,
                 float offsetY);

  void removeShadow();


  void fillRectangle(float x, float y,
                     float width, float height);

  void strokeRectangle(float x, float y,
                       float width, float height);

  void fillAndStrokeRectangle(float x, float y,
                              float width, float height);

  void fillRoundedRectangle(float x, float y,
                            float width, float height,
                            float radius);

  void strokeRoundedRectangle(float x, float y,
                              float width, float height,
                              float radius);

  void fillAndStrokeRoundedRectangle(float x, float y,
                                     float width, float height,
                                     float radius);

  void createImage(IImageListener* listener,
                   bool autodelete);

  void fillText(const std::string& text,
                float x, float y);
  
};

#endif
