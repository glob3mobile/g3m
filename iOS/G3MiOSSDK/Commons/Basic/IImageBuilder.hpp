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
private:
  ChangedListener* _listener;

protected:
  void changed();

public:
  IImageBuilder() :
  _listener(NULL)
  {
  }

  virtual ~IImageBuilder() {
  }

  virtual void build(const G3MContext* context,
                     IImageBuilderListener* listener,
                     bool deleteListener) = 0;

  virtual const std::string getImageName() = 0;

  void setChangeListener(ChangedListener* listener);

};

#endif
