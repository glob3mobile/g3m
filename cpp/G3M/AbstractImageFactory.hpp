//
//  AbstractImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3M__AbstractImageFactory__
#define __G3M__AbstractImageFactory__

#include "IImageFactory.hpp"

#include <stddef.h>

class AbstractImageFactory : public IImageFactory {
private:
  ChangedListener* _changeListener;

protected:

  void changed();

  virtual ~AbstractImageFactory() {
  }

public:
  AbstractImageFactory() :
  _changeListener(NULL)
  {
  }

  void setChangeListener(ChangedListener* changeListener);

};

#endif
