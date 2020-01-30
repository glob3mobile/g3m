//
//  CompositeTileImageProvider.h
//  G3M
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#ifndef __G3M__CompositeTileImageProvider__
#define __G3M__CompositeTileImageProvider__

#include <vector>
#include <map>

#include "CanvasTileImageProvider.hpp"
#include "TileImageListener.hpp"
#include "IImageListener.hpp"
#include "FrameTask.hpp"
#include "RectangleF.hpp"
#include "Sector.hpp"

class CompositeTileImageContribution;


class CompositeTileImageProvider : public CanvasTileImageProvider {
private:

  class ChildResult {
  public:
    const bool                   _isError;
    const bool                   _isCanceled;
    const IImage*                _image;
    const std::string            _imageID;
    const TileImageContribution* _contribution;
    const std::string            _error;

    static const ChildResult* image(const IImage*                image,
                                    const std::string&           imageID,
                                    const TileImageContribution* contribution);

    static const ChildResult* error(const std::string& error);

    static const ChildResult* cancelation();

    ~ChildResult();

  private:

    ChildResult(const bool                   isError,
                const bool                   isCanceled,
                const IImage*                image,
                const std::string&           imageID,
                const TileImageContribution* contribution,
                const std::string&           error);

  };


  class Composer : public RCObject {
  private:
    CompositeTileImageProvider*           _compositeTileImageProvider;
    TileImageListener*                    _listener;
    const bool                            _deleteListener;
#ifdef C_CODE
    const CompositeTileImageContribution* _compositeContribution;
#endif
#ifdef JAVA_CODE
    private CompositeTileImageContribution _compositeContribution;
#endif
    std::vector<const ChildResult*>       _results;
    const size_t                          _contributionsSize;

    int _stepsDone;
    void stepDone();
    void done();

    bool _anyError;
    bool _anyCancelation;
    bool _canceled;

    void cleanUp();

    const int _width;
    const int _height;

    std::string _imageID;

    FrameTasksExecutor* _frameTasksExecutor;

    const Sector _tileSector;

  protected:
    ~Composer();

  public:
    const std::string _tileID;

    Composer(int width,
             int height,
             CompositeTileImageProvider* compositeTileImageProvider,
             const std::string& tileID,
             const Sector& tileSector,
             TileImageListener* listener,
             bool deleteListener,
             const CompositeTileImageContribution* compositeContribution,
             FrameTasksExecutor* frameTasksExecutor);


    void imageCreated(const std::string&           tileID,
                      const IImage*                image,
                      const std::string&           imageID,
                      const TileImageContribution* contribution,
                      const size_t                 index);

    void imageCreationError(const std::string& error,
                            const size_t       index);

    void imageCreationCanceled(const size_t index);

    void cancel(const std::string& tileID);

    void imageCreated(const IImage* image);

    void mixResult();

  };


  class ComposerImageListener : public IImageListener {
  private:
    Composer* _composer;

  public:
    ComposerImageListener(Composer* composer) :
    _composer(composer)
    {
      _composer->_retain();
    }

    ~ComposerImageListener() {
      _composer->_release();
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

    void imageCreated(const IImage* image) {
      _composer->imageCreated(image);
    }
  };


  class ComposerFrameTask : public FrameTask {
  private:
    Composer* _composer;

  public:
    ComposerFrameTask(Composer* composer) :
    _composer(composer)
    {
      _composer->_retain();
    }

    ~ComposerFrameTask() {
      _composer->_release();
    }

    bool isCanceled(const G3MRenderContext* rc);

    void execute(const G3MRenderContext* rc);
  };


  class ChildTileImageListener : public TileImageListener {
  private:
    Composer*    _composer;
    const size_t _index;

  public:
    ChildTileImageListener(Composer* composer,
                           size_t index) :
    _composer(composer),
    _index(index)
    {
      _composer->_retain();
    }

    ~ChildTileImageListener() {
      _composer->_release();
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

    void imageCreated(const std::string&           tileID,
                      const IImage*                image,
                      const std::string&           imageID,
                      const TileImageContribution* contribution);

    void imageCreationError(const std::string& tileID,
                            const std::string& error);

    void imageCreationCanceled(const std::string& tileID);

  };


  std::vector<TileImageProvider*> _children;
  size_t                          _childrenSize;

  std::map<const std::string, Composer*> _composers;

protected:
  ~CompositeTileImageProvider();

public:
  CompositeTileImageProvider() :
  _childrenSize(0)
  {
  }

  void addProvider(TileImageProvider* child) {
    _children.push_back(child);
    _childrenSize = _children.size();
  }

  const TileImageContribution* contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution* contribution,
              const Vector2S& resolution,
              long long tileTextureDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const std::string& tileID);

  void composerDone(Composer* composer);
  
  void cancelChildren(const std::string& tileID,
                      const CompositeTileImageContribution* compositeContribution);
  
};

#endif
