//
//  BILDownloader.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//

#ifndef BILDownloader_hpp
#define BILDownloader_hpp

#include "IThreadUtils.hpp"
#include "Sector.hpp"
#include "Vector2I.hpp"
#include "IBufferDownloadListener.hpp"
#include "GAsyncTask.hpp"

class G3MContext;
class URL;
class TimeInterval;
class ShortBufferDEMGrid;
class IByteBuffer;



class BILDownloader {
private:

  BILDownloader() {}

public:

  class Handler {
  public:
    virtual ~Handler() {
    }

    virtual void onDownloadError(const G3MContext* context,
                                 const URL& url) = 0;

    virtual void onParseError(const G3MContext* context) = 0;

    virtual void onBIL(const G3MContext* context,
                       ShortBufferDEMGrid* result) = 0;
  };

  static void request(const G3MContext*       context,
                      const URL&              url,
                      long long               priority,
                      const TimeInterval&     timeToCache,
                      bool                    readExpired,
                      const Sector&           sector,
                      const Vector2I&         extent,
                      const double            deltaHeight,
                      const short             noDataValue,
                      BILDownloader::Handler* handler,
                      const bool              deleteHandler);



  class ParserAsyncTask : public GAsyncTask {
  private:
    BILDownloader::Handler* _handler;
    const bool              _deleteHandler;
    IByteBuffer*            _buffer;
    const Sector            _sector;
    const Vector2I          _extent;
    const short             _noDataValue;
    const double            _deltaHeight;

    ShortBufferDEMGrid* _result;

  public:
    ParserAsyncTask(IByteBuffer*            buffer,
                    const Sector&           sector,
                    const Vector2I&         extent,
                    const short             noDataValue,
                    const double            deltaHeight,
                    BILDownloader::Handler* handler,
                    const bool              deleteHandler);

    ~ParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };




  class BufferDownloadListener : public IBufferDownloadListener {
  private:
    const Sector            _sector;
    const Vector2I          _extent;
    const short             _noDataValue;
    const double            _deltaHeight;
    BILDownloader::Handler* _handler;
    const bool              _deleteHandler;
    const G3MContext*       _context;

  public:
    BufferDownloadListener(const Sector&           sector,
                           const Vector2I&         extent,
                           const short             noDataValue,
                           const double            deltaHeight,
                           BILDownloader::Handler* handler,
                           const bool              deleteHandler,
                           const G3MContext*       context);


    virtual ~BufferDownloadListener();

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired);

  };
  
  
  
};

#endif
