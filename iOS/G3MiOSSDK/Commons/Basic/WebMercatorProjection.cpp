//
//  WebMercatorProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "WebMercatorProjection.hpp"

#include <stddef.h>


WebMercatorProjection* WebMercatorProjection::INSTANCE = NULL;


WebMercatorProjection::WebMercatorProjection() {

}

WebMercatorProjection::~WebMercatorProjection() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

WebMercatorProjection* WebMercatorProjection::instance() {
  if (INSTANCE == NULL) {
    INSTANCE = new WebMercatorProjection();
  }
  return INSTANCE;
}

const std::string WebMercatorProjection::getEPSG() const {
  return "EPSG:3857";
}
