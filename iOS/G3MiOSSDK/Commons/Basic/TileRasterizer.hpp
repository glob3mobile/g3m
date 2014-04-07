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
#include "ILogger.hpp"
#include "IThreadUtils.hpp"
#include "Tile.hpp"

class IImage;
//class Tile;
class IImageListener;
class ChangedListener;
class G3MContext;
class TileRasterizer_AsyncTask;
class TileTextureBuilder;


//class TileRasterizerContext : public RCObject {
//private:
//  TileRasterizerContext(const TileRasterizerContext& that);
//    ~TileRasterizerContext() {
//        ILogger::instance()->logInfo("muere TileRasterizerContext: %p, [%d,%d]", this, this->_tile->_row, this->_tile->_column);//fpulido
//    }
//
//public:
//#ifdef C_CODE
//  const Tile*   const _tile;
//  const bool          _mercator;
//#endif
//#ifdef JAVA_CODE
//  public final Tile    _tile;
//  public final boolean _mercator;
//#endif
//
//  TileRasterizerContext(const Tile* tile,
//                        bool mercator) :
//  _tile(tile),
//  _mercator(mercator)
//  {
//      ILogger::instance()->logInfo("nace TileRasterizerContext: %p, [%d,%d]", this, this->_tile->_row, this->_tile->_column);//fpulido
//  }
//
//};


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
        ILogger::instance()->logInfo("nace TileRasterizerContext: %p, [%d,%d]", this, this->_tile->_row, this->_tile->_column);//fpulido
    }
    
    ~TileRasterizerContext() {
        ILogger::instance()->logInfo("muere TileRasterizerContext: %p, [%d,%d]", this, this->_tile->_row, this->_tile->_column);//fpulido
    }
    
};


class TileRasterizer {
private:
  ChangedListener* _listener;
  bool _enable;

protected:
  const IThreadUtils* _threadUtils;
    
  TileRasterizer() :
  _enable(true),
  _listener(NULL),
  _threadUtils(NULL) // needs to be set at initialize method for background rasterize.
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
    
  virtual TileRasterizer_AsyncTask* getRawRasterizeTask(const IImage* image,
                                         const TileRasterizerContext& trc,
                                         IImageListener* listener,
                                         bool autodelete) const = 0;
  
};



class TileRasterizer_AsyncTask : public GAsyncTask {
private:
    //const TileTextureBuilder* _builder;

protected:
    const TileTextureBuilder* _builder;
#ifdef C_CODE
    mutable const IImage*        _image;
#endif
#ifdef JAVA_CODE
    private IImage _image;
#endif
    const TileRasterizerContext* _trc;
    IImageListener* _listener;
    bool _autodelete;

public:
    TileRasterizer_AsyncTask(const IImage* image,
                             const TileRasterizerContext* trc,
                             IImageListener* listener,
                             bool autodelete);
    
    ~TileRasterizer_AsyncTask();
    
    virtual void runInBackground(const G3MContext* context) = 0;
    
    virtual void onPostExecute(const G3MContext* context) = 0;
};


#endif
