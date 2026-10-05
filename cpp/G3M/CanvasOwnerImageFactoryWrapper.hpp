//
//  CanvasOwnerImageFactoryWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#ifndef CanvasOwnerImageFactoryWrapper_hpp
#define CanvasOwnerImageFactoryWrapper_hpp

#include "IImageFactory.hpp"

class ICanvas;

class CanvasOwnerImageFactoryWrapper : public IImageFactory {
private:
  ICanvas*       _canvas;
  IImageFactory* _imageFactory;
  const bool     _autodelete;

public:
  CanvasOwnerImageFactoryWrapper(ICanvas* canvas,
                                 IImageFactory* imageFactory,
                                 const bool autodelete);

  ~CanvasOwnerImageFactoryWrapper();

  bool isMutable() const;

  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);

  void setChangeListener(ChangedListener* listener);

};

#endif
