//
//  ICanvasUtils.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//

#include "ICanvasUtils.hpp"
#include "ICanvas.hpp"
#include "GFont.hpp"
#include "ColumnCanvasElement.hpp"
#include "TextCanvasElement.hpp"

void ICanvasUtils::drawStringsOn(const std::vector<std::string>& strings,
                                 ICanvas *canvas,
                                 const int width,
                                 const int height,
                                 const HorizontalAlignment hAlign,
                                 const VerticalAlignment vAlign,
                                 const HorizontalAlignment textAlign,
                                 const Color& color,
                                 const int maxFontSize,
                                 const int minFontSize,
                                 const Color& backgroundColor,
                                 const Color& shadowColor,
                                 const int padding,
                                 const int cornerRadius) {

  if (strings.empty()) {
    return;
  }

  const int maxWidth = width - (2 * padding);
  const int stringsSize = strings.size();

  int fontSize = maxFontSize;
  bool allFit = false;
  while (!allFit && (fontSize > minFontSize)) {
    allFit = true;
    canvas->setFont( GFont::sansSerif(fontSize) );
    for (int i = 0; i < stringsSize; i++) {
      const Vector2F extent = canvas->textExtent(strings[i]);
      if (extent._x > maxWidth) {
        allFit = false;
        fontSize--;
        continue;
      }
    }
  }
//  canvas->setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
  canvas->setShadow(shadowColor, 1.0f, 0.0f, 0.0f);

  ColumnCanvasElement column(backgroundColor,
                             0,  /* margin */
                             padding,
                             cornerRadius,
                             textAlign);
  const GFont labelFont  = GFont::sansSerif(fontSize);
  for (int i = 0; i < stringsSize; i++) {
    column.add( new TextCanvasElement(strings[i], labelFont, color) );
  }

  const Vector2F extent = column.getExtent(canvas);
  const Vector2F position = getPosition(extent,
                                        width,
                                        height,
                                        hAlign,
                                        vAlign);
  column.drawAt(position._x, position._y, canvas);
}


Vector2F ICanvasUtils::getPosition(const Vector2F& extent,
                                   const int canvasWidth,
                                   const int canvasHeight,
                                   const HorizontalAlignment hAlign,
                                   const VerticalAlignment vAlign) {
  float left;
  float top;
  switch (hAlign) {
    case Left:
      left = 0;
      break;
    case Right:
      left = canvasWidth - extent._x;
      break;
    case Center:
    default:
      left = (canvasWidth / 2) - (extent._x / 2);
  }
  switch (vAlign) {
    case Top:
      top = 0;
      break;
    case Bottom:
      top = canvasHeight - extent._y;
      break;
    case Middle:
    default:
      top = (canvasHeight / 2) - (extent._y / 2);
  }

  return Vector2F(left, top);
}
