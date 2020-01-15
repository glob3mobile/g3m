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
    if (typeof document.EMStorage === 'undefined') {
      document.EMStorage = {
        "__idCounter" : 0
      };

      document.EMStorage["put"] = function(obj) {
        var id = this.__idCounter + 1;
        this.__idCounter = id;
        this[id] = obj;
        return id;
      };

      document.EMStorage["take"] = function(id) {
        var obj = this[id];
        delete this[id];
        return obj;
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

int EMStorage::put(const emscripten::val& value) {
  val jsEMStorage = val::global("document")["EMStorage"];
  const int id = jsEMStorage["__idCounter"].as<int>() + 1;
  jsEMStorage.set("__idCounter", id);
  jsEMStorage.set(id, value);
  return id;
}

emscripten::val EMStorage::take(const int id) {
  val jsEMStorage = val::global("document")["EMStorage"];
  val obj = jsEMStorage[id];
  EM_ASM({ delete document.EMStorage[$0]; }, id);
  return obj;
}
