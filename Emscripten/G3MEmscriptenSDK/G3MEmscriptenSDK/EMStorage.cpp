//
//  EMStorage.cpp
//  G3MEmscriptenSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/15/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#include "EMStorage.hpp"

#include <stddef.h>


EMStorage* EMStorage::_instance = NULL;

EMStorage::EMStorage() {

}

EMStorage* EMStorage::instance() {
  if (_instance == NULL) {
    _instance = new EMStorage();
  }
  return _instance;
}
