//
//  AbstractImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3MiOSSDK__AbstractImageBuilder__
#define __G3MiOSSDK__AbstractImageBuilder__

#include "IImageBuilder.hpp"

#include <stddef.h>

class AbstractImageBuilder : public IImageBuilder {
private:
  ChangedListener* _listener;

protected:
  void changed();

public:
  AbstractImageBuilder() :
  _listener(NULL)
  {
  }

  virtual ~AbstractImageBuilder() {
  }
  
  void setChangeListener(ChangedListener* listener);

};

#endif
