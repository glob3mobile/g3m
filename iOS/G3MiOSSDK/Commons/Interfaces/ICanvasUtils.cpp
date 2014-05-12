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
                                 const VerticalAlignment vAlign,
                                 const HorizontalAlignment hAlign,
                                 const Color& color,
                                 const int maxFontSize,
                                 const int minFontSize,
                                 const Color& backgroundColor,
                                 const Color& shadowColor,
                                 const int padding,
                                 const int cornerRadius) {
  int longestTextIndex = 0;
  int maxLength = strings.at(longestTextIndex).length();
  const int stringsSize = strings.size();
  for (int i = 1; i < stringsSize; i++) {
    const int itemLength = strings.at(i).length();
    if (maxLength < itemLength) {
      maxLength = itemLength;
      longestTextIndex = i;
    }
  }
  
  int fontSize = maxFontSize;
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

  canvas->setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
  
  ColumnCanvasElement column(backgroundColor,
                             0,  /* margin */
                             padding,
                             cornerRadius);
  const GFont labelFont  = GFont::sansSerif(fontSize);
  for (int i = 0; i < stringsSize; i++) {
    column.add( new TextCanvasElement(strings[i], labelFont, color) );
  }
  
  float top;
  float left;
  const Vector2F extent = column.getExtent(canvas);
  switch (vAlign) {
    case Top:
      top = 0;
      break;
    case Middle:
      top = (height / 2) - (extent._y / 2);
      break;
    case Bottom:
      top = height - extent._y;
      break;
  }
  switch (hAlign) {
    case Left:
      left = 0;
      break;
    case Center:
      left = (width / 2) - (extent._x / 2);
      break;
    case Right:
      left = width - extent._x;
      break;
  }

  column.drawAt(left, top, canvas);
  
  return extent;
}