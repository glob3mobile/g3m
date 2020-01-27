//
//  MapzenTerrariumParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

#include "MapzenTerrariumParser.hpp"

#include "MutableColor255.hpp"
#include "IImage.hpp"
#include "FloatBufferDEMGrid.hpp"
#include "G3MContext.hpp"
#include "IThreadUtils.hpp"
#include "WebMercatorProjection.hpp"


MapzenTerrariumParser::ParserTask::ParserTask(const IImage* image,
                                              const Sector& sector,
                                              const double  deltaHeight,
                                              MapzenTerrariumParser::Listener* listener,
                                              bool deleteListener) :
_image(image),
_sector(sector),
_deltaHeight(deltaHeight),
_listener(listener),
_deleteListener(deleteListener),
_result(NULL)
{

}

MapzenTerrariumParser::ParserTask::~ParserTask()
{
  if (_result != NULL) {
    _result->_release();
  }
  if (_deleteListener) {
    delete _listener;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MapzenTerrariumParser::ParserTask::runInBackground(const G3MContext* context) {
  _result = MapzenTerrariumParser::parse(_image, _sector, _deltaHeight);
}

void MapzenTerrariumParser::ParserTask::onPostExecute(const G3MContext* context) {
  _listener->onGrid(_result);
  _result = NULL; // moved ownership to _listener
}


FloatBufferDEMGrid* MapzenTerrariumParser::parse(const IImage* image,
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

  return new FloatBufferDEMGrid(WebMercatorProjection::instance(),
                                sector,
                                Vector2I(width, height),
                                buffer,
                                bufferSize,
                                deltaHeight);
}

void MapzenTerrariumParser::parse(const G3MContext* context,
                                  const IImage* image,
                                  const Sector& sector,
                                  double deltaHeight,
                                  MapzenTerrariumParser::Listener* listener,
                                  bool deleteListener) {
  const IThreadUtils* threadUtils = context->getThreadUtils();
  GAsyncTask* parserTask = new MapzenTerrariumParser::ParserTask(image,
                                                                 sector,
                                                                 deltaHeight,
                                                                 listener,
                                                                 deleteListener);
  threadUtils->invokeAsyncTask(parserTask, true);
}
