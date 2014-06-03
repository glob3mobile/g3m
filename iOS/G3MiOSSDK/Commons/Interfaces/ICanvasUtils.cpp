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

Vector2F ICanvasUtils::drawStringsOn(const std::vector<std::string> &strings,
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
  
  const int stringsSize = strings.size();
  int fontSize = maxFontSize;
  if (stringsSize > 0 ) {
    int longestTextIndex = 0;
    int maxLength = strings.at(longestTextIndex).length();
    for (int i = 1; i < stringsSize; i++) {
      const int itemLength = strings.at(i).length();
      if (maxLength < itemLength) {
        maxLength = itemLength;
        longestTextIndex = i;
      }
    }
    
    
    
    const int maxWidth = width - (2 * padding);
    bool fit = false;
    while (!fit && fontSize > minFontSize) {
      GFont labelFont = GFont::sansSerif(fontSize);
      const std::string longestText = strings.at(longestTextIndex);
      canvas->setFont(labelFont);
      const Vector2F extent = canvas->textExtent(longestText);
      if (extent._x <= maxWidth) {
        fit = true;
      }
      else {
        fontSize--;
      }
    }
  }
  canvas->setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
  
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
  
  return extent;
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