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
class Tile;
class IImageListener;
class ChangedListener;
class G3MContext;


class TileRasterizerContext {
private:
  TileRasterizerContext(const TileRasterizerContext& that);

public:
#ifdef C_CODE
  const Tile*   const _tile;
  const bool          _mercator;
#endif
#ifdef JAVA_CODE
  public final Tile    _tile;
  public final boolean _mercator;
#endif

  TileRasterizerContext(const Tile* tile,
                        bool mercator) :
  _tile(tile),
  _mercator(mercator)
  {
  }

  ~TileRasterizerContext() {
  }
};


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
