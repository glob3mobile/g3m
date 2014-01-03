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

class AbstractImageBuilder : public IImageBuilder {
private:
  ChangedListener* _listener;

public:
  AbstractImageBuilder() :
  _listener(NULL)
  {
  }

  virtual ~AbstractImageBuilder() {
  }
  
  void changed();

  void setChangeListener(ChangedListener* listener);

};

#endif
