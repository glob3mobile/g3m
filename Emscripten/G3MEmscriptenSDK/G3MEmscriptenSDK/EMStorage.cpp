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


EMStorage::EMStorage() {
}

void EMStorage::initialize() {
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

      document.EMStorage["null"] = function() {
        return -1;
      };

    }
  });
}

int EMStorage::put(const val& value) {
  if (value.isNull()) {
    return -1;
  }

  val jsEMStorage = val::global("document")["EMStorage"];
  const int id = jsEMStorage["__idCounter"].as<int>() + 1;
  jsEMStorage.set("__idCounter", id);
  jsEMStorage.set(id, value);
  return id;
}

int EMStorage::null() {
  return -1;
}

val EMStorage::take(const int id) {
  if (id < 0) {
    return val::null();
  }
  val jsEMStorage = val::global("document")["EMStorage"];
  val obj = jsEMStorage[id];
  EM_ASM({ delete document.EMStorage[$0]; }, id);
  return obj;
}
