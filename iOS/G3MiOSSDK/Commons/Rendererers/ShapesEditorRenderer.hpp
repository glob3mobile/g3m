//
//  ShapesEditorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#ifndef __G3MiOSSDK__ShapesEditorRenderer__
#define __G3MiOSSDK__ShapesEditorRenderer__


#include "ShapesRenderer.hpp"


class GEOTileRasterizer;


class ShapesEditorRenderer: public ShapesRenderer {
  
public:
  ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer);
  
  void addShape(Shape* shape)
  {
    ShapesRenderer::addShape(shape);
  }
  
  
};



#endif /* defined(__G3MiOSSDK__ShapesEditorRenderer__) */
