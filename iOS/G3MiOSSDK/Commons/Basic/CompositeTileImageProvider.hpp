//
//  CompositeTileImageProvider.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#ifndef __G3MiOSSDK__CompositeTileImageProvider__
#define __G3MiOSSDK__CompositeTileImageProvider__

#include <vector>
#include <map>

#include "CanvasTileImageProvider.hpp"
#include "TileImageListener.hpp"
#include "IImageListener.hpp"
#include "FrameTasksExecutor.hpp"

class CompositeTileImageContribution;


class CompositeTileImageProvider : public CanvasTileImageProvider {
private:

  class ChildResult {
  public:
    const bool                   _isError;
    const bool                   _isCanceled;
    const IImage*                _image;
    const std::string            _imageId;
    const TileImageContribution* _contribution;
    const std::string            _error;

    static const ChildResult* image(const IImage*                image,
                                    const std::string&           imageId,
                                    const TileImageContribution* contribution);

    static const ChildResult* error(const std::string& error);

    static const ChildResult* cancelation();

    ~ChildResult();

  private:

    ChildResult(const bool                   isError,
                const bool                   isCanceled,
                const IImage*                image,
                const std::string&           imageId,
                const TileImageContribution* contribution,
                const std::string&           error) :
    _isError(isError),
    _isCanceled(isCanceled),
    _image(image),
    _imageId(imageId),
    _contribution(contribution),
    _error(error)
    {
    }

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
    const int                             _contributionsSize;

    int _stepsDone;
    void stepDone();
    void done();

    bool _anyError;
    bool _anyCancelation;
    bool _canceled;

    void cleanUp();

    const int _width;
    const int _height;

    std::string _imageId;

    FrameTasksExecutor* _frameTasksExecutor;

  protected:
    ~Composer();

  public:
    const std::string _tileId;

    Composer(int width,
             int height,
             CompositeTileImageProvider* compositeTileImageProvider,
             const std::string& tileId,
             TileImageListener* listener,
             bool deleteListener,
             const CompositeTileImageContribution* compositeContribution,
             FrameTasksExecutor* frameTasksExecutor);


    void imageCreated(const std::string&           tileId,
                      const IImage*                image,
                      const std::string&           imageId,
                      const TileImageContribution* contribution,
                      const int                    index);

    void imageCreationError(const std::string& error,
                            const int          index);

    void imageCreationCanceled(const int index);

    void cancel(const Tile* tile);

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
    Composer* _composer;
    const int _index;

  public:
    ChildTileImageListener(Composer* composer,
                           int index) :
    _composer(composer),
    _index(index)
    {
    }

    void imageCreated(const std::string&           tileId,
                      const IImage*                image,
                      const std::string&           imageId,
                      const TileImageContribution* contribution);

    void imageCreationError(const std::string& tileId,
                            const std::string& error);

    void imageCreationCanceled(const std::string& tileId);

  };


  std::vector<TileImageProvider*> _children;
  int                             _childrenSize;

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
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const Tile* tile);

  void composerDone(Composer* composer);
  
  void cancelChildren(const Tile* tile,
                      const CompositeTileImageContribution* compositeContribution);
  
};

#endif
