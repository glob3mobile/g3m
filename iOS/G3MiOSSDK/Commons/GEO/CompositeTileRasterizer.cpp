//
//  CompositeTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/12/13.
//
//

#include "CompositeTileRasterizer.hpp"

#include "IStringBuilder.hpp"
#include "IImage.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"

void CompositeTileRasterizer::initialize(const G3MContext* context) {
  _context = context;
  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    TileRasterizer* child = _children[i];
    child->initialize(context);
  }
}

CompositeTileRasterizer::~CompositeTileRasterizer() {
  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    TileRasterizer* child = _children[i];
    delete child;
  }
}

std::string CompositeTileRasterizer::getId() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("CompositeTileRasterizer");

  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    isb->addString("-");
    TileRasterizer* child = _children[i];
    isb->addString(child->getId());
  }

  const std::string s = isb->getString();
  delete isb;
  return s;
}

void CompositeTileRasterizer::addTileRasterizer(TileRasterizer* tileRasterizer) {
  if (tileRasterizer != NULL) {
    _children.push_back(tileRasterizer);
    tileRasterizer->setChangeListener(this);
    if (_context != NULL) {
      tileRasterizer->initialize(_context);
    }
    notifyChanges();
  }
}

void CompositeTileRasterizer::changed() {
  notifyChanges();
}

class CompositeTileRasterizer_ChildImageListener : public IImageListener {
private:
  const int                           _childIndex;
  const std::vector<TileRasterizer*>* _children;
  const TileRasterizerContext*        _trc;
  IImageListener*                     _listener;
  bool                                _autodeleteListener;

public:
  CompositeTileRasterizer_ChildImageListener(int                                 childIndex,
                                             const std::vector<TileRasterizer*>* children,
                                             const TileRasterizerContext*        trc,
                                             IImageListener*                     listener,
                                             bool                                autodeleteListener) :
  _childIndex(childIndex),
  _children(children),
  _trc(trc),
  _listener(listener),
  _autodeleteListener(autodeleteListener)
  {
  }

  void imageCreated(const IImage* image) {
    TileRasterizer* child = _children->at(_childIndex);

    const int nextChildIndex = _childIndex + 1;
    if (nextChildIndex > _children->size()-1) {
      child->rasterize(image,
                       *_trc,
                       _listener,
                       _autodeleteListener);
    }
    else {
      child->rasterize(image,
                       *_trc,
                       new CompositeTileRasterizer_ChildImageListener(nextChildIndex,
                                                                      _children,
                                                                      _trc,
                                                                      _listener,
                                                                      _autodeleteListener),
                       true);
    }
  }

};


void CompositeTileRasterizer::rawRasterize(const IImage* image,
                                           const TileRasterizerContext& trc,
                                           IImageListener* listener,
                                           bool autodeleteListener) const {
  if (_children.size() == 0) {
    listener->imageCreated(image);
    if (autodeleteListener) {
      delete listener;
    }
  }
  else {
    const int width  = image->getWidth();
    const int height = image->getHeight();

    ICanvas* canvas = getCanvas(width, height);

    canvas->drawImage(image, 0, 0);

    canvas->createImage(new CompositeTileRasterizer_ChildImageListener(0,
                                                                       &_children,
                                                                       &trc,
                                                                       listener,
                                                                       autodeleteListener),
                        true);
  }
  
  delete image;
}
