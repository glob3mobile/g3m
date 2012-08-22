//
//  TileKey.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#include "TileKey.hpp"

#include <sstream>

const std::string TileKey::description() const {
  std::ostringstream buffer;
  buffer << "(level=";
  buffer << _level;
  buffer << ", row=";
  buffer << _row;
  buffer << ", col=";
  buffer << _column;
  buffer << ")";
  return buffer.str();
}

const std::string TileKey::tinyDescription() const {
  std::ostringstream buffer;
  buffer << _level;
  buffer << "-";
  buffer << _row;
  buffer << "/";
  buffer << _column;
  return buffer.str();
}
