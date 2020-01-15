//
//  EMStorage.hpp
//  G3MEmscriptenSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/15/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#ifndef EMStorage_hpp
#define EMStorage_hpp

#include <emscripten/val.h>


class EMStorage {
private:
  static EMStorage* _instance;

  EMStorage();

public:
  static EMStorage* instance();

  emscripten::val take(int id);

};

#endif
