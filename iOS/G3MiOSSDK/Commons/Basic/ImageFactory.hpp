//
//  ImageFactory.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

#ifndef __G3MiOSSDK__ImageFactory__
#define __G3MiOSSDK__ImageFactory__

class G3MRenderContext;
class IImageListener;

class ImageFactory {
public:
#ifdef C_CODE
  virtual ~ImageFactory() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif

  virtual void create(const G3MRenderContext* rc,
                      int width,
                      int height,
                      IImageListener* listener,
                      bool deleteListener) = 0;

};

#endif
