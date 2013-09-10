//
//  ImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__ImageBuilder__
#define __G3MiOSSDK__ImageBuilder__

class CanvasElement;
class IImageListener;
class Color;

class ImageBuilder {
private:
  ImageBuilder() {

  }

public:

  static void build(CanvasElement* element,
                    IImageListener* listener,
                    bool autodelete);

};

#endif
