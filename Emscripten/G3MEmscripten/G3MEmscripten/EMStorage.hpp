//
//  EMStorage.hpp
//  G3MEmscripten
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/15/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#ifndef EMStorage_hpp
#define EMStorage_hpp

#include <emscripten/val.h>


class EMStorage {
private:
  EMStorage();
  
public:
  static void initialize();
  
  static int put(const emscripten::val& value);

  static int null();

  static emscripten::val take(const int id);


};

#endif
