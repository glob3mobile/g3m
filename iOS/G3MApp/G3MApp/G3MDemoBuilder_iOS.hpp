//
//  G3MDemoBuilder_iOS.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoBuilder_iOS__
#define __G3MApp__G3MDemoBuilder_iOS__

#include "G3MDemoBuilder.hpp"

class G3MBuilder_iOS;

class G3MDemoBuilder_iOS : public G3MDemoBuilder {
private:
  G3MBuilder_iOS* _builder;

protected:

  IG3MBuilder* getG3MBuilder();

public:
  G3MDemoBuilder_iOS(G3MBuilder_iOS* builder) :
  _builder(builder)
  {
  }


  ~G3MDemoBuilder_iOS();

  void initializeWidget();

};

#endif
