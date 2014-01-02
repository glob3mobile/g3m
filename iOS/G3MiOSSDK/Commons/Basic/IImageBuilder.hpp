//
//  IImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3MiOSSDK__IImageBuilder__
#define __G3MiOSSDK__IImageBuilder__

class G3MContext;
class IImageBuilderListener;

class IImageBuilder {
protected:
  IImageBuilder() {
  }

public:

  virtual ~IImageBuilder() {
  }

  virtual void build(const G3MContext* context,
                     IImageBuilderListener* listener,
                     bool deleteListener) = 0;

};

#endif
