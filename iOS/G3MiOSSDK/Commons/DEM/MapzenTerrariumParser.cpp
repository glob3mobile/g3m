//
//  MapzenTerrariumParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

#include "MapzenTerrariumParser.hpp"

#include "MutableColor255.hpp"
#include "IImage.hpp"
#include "FloatBufferTerrainElevationGrid.hpp"


FloatBufferTerrainElevationGrid* MapzenTerrariumParser::parse(IImage* image,
                                                              const Sector& sector,
                                                              double deltaHeight) {
  MutableColor255 pixel((unsigned char) 0,
                        (unsigned char) 0,
                        (unsigned char) 0,
                        (unsigned char) 0);

  const int width  = image->getWidth();
  const int height = image->getHeight();

  const int bufferSize = width * height;

  float* buffer = new float[bufferSize];

  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      image->getPixel(x, y, pixel);
      const float elevation = ((pixel._red * 256.0f) + pixel._green + (pixel._blue / 256.0f)) - 32768.0f;
      const int index = ((height-1-y) * width) + x;
      buffer[index] = elevation;
    }
  }

  delete image;

  return new FloatBufferTerrainElevationGrid(sector,
                                             Vector2I(width, height),
                                             buffer,
                                             bufferSize,
                                             deltaHeight);
}
