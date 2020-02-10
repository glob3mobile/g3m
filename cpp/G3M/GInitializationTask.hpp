//
//  GInitializationTask.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/11/12.
//
//

#ifndef __G3M__GInitializationTask__
#define __G3M__GInitializationTask__

#include "GTask.hpp"

class GInitializationTask : public GTask {
public:

  virtual bool isDone(const G3MContext* context) = 0;
};

#endif
