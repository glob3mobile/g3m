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
  ChangedListener* _changeListener;

protected:
  void changed();

public:
  AbstractImageBuilder() :
  _changeListener(NULL)
  {
  }

  virtual ~AbstractImageBuilder() {
  }
  
  void setChangeListener(ChangedListener* changeListener);

};

#endif
