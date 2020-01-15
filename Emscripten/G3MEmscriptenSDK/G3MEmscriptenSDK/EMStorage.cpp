//
//  EMStorage.cpp
//  G3MEmscriptenSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/15/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#include "EMStorage.hpp"

#include <emscripten.h>

#include <stddef.h>

using namespace emscripten;


EMStorage* EMStorage::_instance = NULL;

EMStorage::EMStorage() {
  EM_ASM({
    if (typeof document.MyStorage === 'undefined') {
      document.MyStorage = {
        "__idCounter" : 0
      };

      document.MyStorage["put"] = function(obj) {
        var self = document.MyStorage;
        var id = self.__idCounter + 1;
        self.__idCounter = id;
        self[id] = obj;
        return id;
      };
    }
  });
}

EMStorage* EMStorage::instance() {
  if (_instance == NULL) {
    _instance = new EMStorage();
  }
  return _instance;
}

emscripten::val EMStorage::take(int id) {
  val MyStorage = val::global("document")["MyStorage"];

  val result = MyStorage[id];

  EM_ASM({ delete document.MyStorage[$0]; }, id);

  return result;
}
