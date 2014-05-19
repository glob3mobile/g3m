//
//  TileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#ifndef __G3MiOSSDK__TileRasterizer__
#define __G3MiOSSDK__TileRasterizer__

#include <string>

class IImage;
class IImageListener;
class ChangedListener;
class G3MContext;
class TileRasterizerContext;

class TileRasterizer {
private:
  ChangedListener* _listener;
  bool _enable;

protected:
  TileRasterizer() :
  _enable(true),
  _listener(NULL)
  {
  }

public:

  virtual ~TileRasterizer() {
  }

  virtual void initialize(const G3MContext* context) = 0;

  virtual std::string getId() const = 0;

  void rasterize(const IImage* image,
                 const TileRasterizerContext& trc,
                 IImageListener* listener,
                 bool autodelete) const;

  virtual void rawRasterize(const IImage* image,
                            const TileRasterizerContext& trc,
                            IImageListener* listener,
                            bool autodelete) const = 0;

  void setChangeListener(ChangedListener* listener);

  void notifyChanges() const;

  bool isEnable() const {
    return _enable;
  }

  void setEnable(bool enable);
  
};

#endif
