//
//  G3MDemoBuilder.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoBuilder__
#define __G3MApp__G3MDemoBuilder__

#include <stddef.h>

class G3MWidget;

class G3MDemoBuilder {
private:
  G3MWidget* _g3mWidget;

protected:
  G3MDemoBuilder() :
  _g3mWidget(NULL)
  {

  }

public:

  void setG3MWidget(G3MWidget* g3mWidget) {
    _g3mWidget = g3mWidget;
  }

};

#endif
