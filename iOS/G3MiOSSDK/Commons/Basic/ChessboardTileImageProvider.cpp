//
//  ChessboardTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#include "ChessboardTileImageProvider.hpp"

#include "IImage.hpp"
#include "TileImageListener.hpp"
#include "Tile.hpp"
#include "RectangleF.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "IImageListener.hpp"
#include "TileImageContribution.hpp"

class ChessboardTileImageProvider_IImageListener : public IImageListener {
private:
  ChessboardTileImageProvider* _parent;
  const Tile*                  _tile;
  TileImageListener*           _listener;
  const bool                   _deleteListener;

public:

  ChessboardTileImageProvider_IImageListener(ChessboardTileImageProvider* parent,
                                             const Tile* tile,
                                             TileImageListener* listener,
                                             bool deleteListener) :
  _parent(parent),
  _tile(tile),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  void imageCreated(const IImage* image) {
    _parent->imageCreated(image,
                          _tile,
                          _listener,
                          _deleteListener);
  }
};

ChessboardTileImageProvider::~ChessboardTileImageProvider() {
  delete _image;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const TileImageContribution* ChessboardTileImageProvider::contribution(const Tile* tile) {
  return TileImageContribution::fullCoverageOpaque();
}

void ChessboardTileImageProvider::imageCreated(const IImage* image,
                                               const Tile* tile,
                                               TileImageListener* listener,
                                               bool deleteListener) {
  _image = image->shallowCopy();

  listener->imageCreated(tile->_id,
                         image,
                         "ChessboardTileImageProvider_image",
                         TileImageContribution::fullCoverageOpaque());

  if (deleteListener) {
    delete listener;
  }
}

void ChessboardTileImageProvider::create(const Tile* tile,
                                         const TileImageContribution* contribution,
                                         const Vector2I& resolution,
                                         long long tileDownloadPriority,
                                         bool logDownloadActivity,
                                         TileImageListener* listener,
                                         bool deleteListener,
                                         FrameTasksExecutor* frameTasksExecutor) {
  if (_image == NULL) {
    const int width = resolution._x;
    const int height = resolution._y;

    ICanvas* canvas = IFactory::instance()->createCanvas();
    canvas->initialize(width, height);

    canvas->setFillColor(_backgroundColor);
    canvas->fillRectangle(0, 0, width, height);

    canvas->setFillColor(_boxColor);

    const float xInterval = (float) width  / _splits;
    const float yInterval = (float) height / _splits;

    for (int col = 0; col < _splits; col += 2) {
      const float x  = col * xInterval;
      const float x2 = (col + 1) * xInterval;
      for (int row = 0; row < _splits; row += 2) {
        const float y  = row * yInterval;
        const float y2 = (row + 1) * yInterval;

        canvas->fillRoundedRectangle(x + 2,
                                     y + 2,
                                     xInterval - 4,
                                     yInterval - 4,
                                     4);
        canvas->fillRoundedRectangle(x2 + 2,
                                     y2 + 2,
                                     xInterval - 4,
                                     yInterval - 4,
                                     4);
      }
    }

    canvas->createImage(new ChessboardTileImageProvider_IImageListener(this,
                                                                       tile,
                                                                       listener,
                                                                       deleteListener),
                        true);

    delete canvas;
  }
  else {
    IImage* image = _image->shallowCopy();
    listener->imageCreated(tile->_id,
                           image,
                           "ChessboardTileImageProvider_image",
                           contribution);
    if (deleteListener) {
      delete listener;
    }
  }
}

void ChessboardTileImageProvider::cancel(const std::string& tileId) {
  // do nothing, can't cancel
}
