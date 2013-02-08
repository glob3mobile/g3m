//
//  DownloadPriority.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 08/02/13.
//
//

#include "DownloadPriority.hpp"

const long long DownloadPriority::LOWEST  = 1000;
const long long DownloadPriority::LOWER   = 100000;
const long long DownloadPriority::MEDIUM  = 10000000;
const long long DownloadPriority::HIGHER  = 1000000000;
const long long DownloadPriority::HIGHEST = 100000000000;

const long long DownloadPriority::getMarkDownloadPriority() {
  return HIGHEST;
}

const long long DownloadPriority::getTileDownloadPriority() {
  //#define TILE_DOWNLOAD_PRIORITY 1000000000
  return HIGHER;
}

const long long DownloadPriority::getTextureDownloadPriority() {
  //#define TEXTURES_DOWNLOAD_PRIORITY 1000000
  return MEDIUM;
}