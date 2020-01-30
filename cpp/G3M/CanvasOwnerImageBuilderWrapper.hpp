//
//  CanvasOwnerImageBuilderWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#ifndef CanvasOwnerImageBuilderWrapper_hpp
#define CanvasOwnerImageBuilderWrapper_hpp

#include "IImageBuilder.hpp"

class ICanvas;

class CanvasOwnerImageBuilderWrapper : public IImageBuilder {
private:
  ICanvas*       _canvas;
  IImageBuilder* _imageBuilder;

public:
  CanvasOwnerImageBuilderWrapper(ICanvas* canvas,
                                 IImageBuilder* imageBuilder);

  ~CanvasOwnerImageBuilderWrapper();

  bool isMutable() const;

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);

  void setChangeListener(ChangedListener* listener);

};

#endif
