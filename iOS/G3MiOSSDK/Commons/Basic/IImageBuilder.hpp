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

#include <string>

class IImageBuilder {
public:

#ifdef C_CODE
  virtual ~IImageBuilder() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void build(const G3MContext* context,
                     IImageBuilderListener* listener,
                     bool deleteListener) = 0;

  virtual const std::string getImageName() = 0;

};

#endif
