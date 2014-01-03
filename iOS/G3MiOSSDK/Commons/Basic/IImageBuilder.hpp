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
class ChangedListener;

#include <string>

class IImageBuilder {
protected:
  virtual void changed() = 0;

public:

#ifdef C_CODE
  virtual ~IImageBuilder() {
  }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual bool isMutable() const = 0;

  virtual void build(const G3MContext* context,
                     IImageBuilderListener* listener,
                     bool deleteListener) = 0;

  virtual const std::string getImageName() = 0;

  virtual void setChangeListener(ChangedListener* listener) = 0;
  
};

#endif
