//
//  G3MDemoListener.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoListener__
#define __G3MApp__G3MDemoListener__

class G3MDemoScene;

class G3MDemoListener {
public:
  virtual void onChangedScene(const G3MDemoScene* scene) = 0;

  virtual ~G3MDemoListener() {

  }
};

#endif
