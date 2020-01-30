//
//  AbstractImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3M__AbstractImageBuilder__
#define __G3M__AbstractImageBuilder__

#include "IImageBuilder.hpp"

#include <stddef.h>

class AbstractImageBuilder : public IImageBuilder {
private:
  ChangedListener* _changeListener;

protected:

  void changed();

  virtual ~AbstractImageBuilder() {
  }

public:
  AbstractImageBuilder() :
  _changeListener(NULL)
  {
  }

  void setChangeListener(ChangedListener* changeListener);

};

#endif
