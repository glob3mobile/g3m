//
//  GLUniformID_Emscripten.hpp
//  G3MEmscripten
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/9/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#ifndef GLUniformID_Emscripten_hpp
#define GLUniformID_Emscripten_hpp

#include "G3M/IGLUniformID.hpp"

#include <emscripten/val.h>


class GLUniformID_Emscripten : public IGLUniformID {
private:
  emscripten::val _id;

public:
  GLUniformID_Emscripten(const emscripten::val& id);

  const emscripten::val getId() const;

  bool isValid() const;

  ~GLUniformID_Emscripten();

};

#endif
