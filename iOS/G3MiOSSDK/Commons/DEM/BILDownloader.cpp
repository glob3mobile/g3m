//
//  BILDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//

#include "BILDownloader.hpp"

#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "BILParser.hpp"
#include "Sector.hpp"
#include "Vector2I.hpp"


class BILDownloader_ParserAsyncTask : public GAsyncTask {
private:
  BILDownloader::Handler* _handler;
  const bool              _deleteHandler;
  IByteBuffer*            _buffer;
  const Sector            _sector;
  const Vector2I          _extent;
  const short             _noDataValue;
  const double            _deltaHeight;

  ShortBufferTerrainElevationGrid* _result;

public:
  BILDownloader_ParserAsyncTask(IByteBuffer*            buffer,
                                const Sector&           sector,
                                const Vector2I&         extent,
                                const short             noDataValue,
                                const double            deltaHeight,
                                BILDownloader::Handler* handler,
                                const bool              deleteHandler) :
  _buffer(buffer),
  _sector(sector),
  _extent(extent),
  _noDataValue(noDataValue),
  _deltaHeight(deltaHeight),
  _handler(handler),
  _deleteHandler(deleteHandler),
  _result(NULL)
  {
  }

  ~BILDownloader_ParserAsyncTask() {
    delete _buffer;
    delete _result;
    if (_deleteHandler) {
      delete _handler;
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void runInBackground(const G3MContext* context) {
    _result = BILParser::parseBIL16(_sector, _extent, _buffer, _noDataValue, _deltaHeight);

    delete _buffer;
    _buffer = NULL;
  }

  void onPostExecute(const G3MContext* context) {
    if (_result == NULL) {
      _handler->onParseError(context);
    }
    else {
      _handler->onBIL(context, _result);
      _result = NULL; // moves _result ownership to _handler
    }
  }

};


class BILDownloader_BufferDownloadListener : public IBufferDownloadListener {
private:
  const Sector            _sector;
  const Vector2I          _extent;
  const short             _noDataValue;
  const double            _deltaHeight;
  BILDownloader::Handler* _handler;
  const bool              _deleteHandler;
  const G3MContext*       _context;

public:
  BILDownloader_BufferDownloadListener(const Sector&           sector,
                                       const Vector2I&         extent,
                                       const short             noDataValue,
                                       const double            deltaHeight,
                                       BILDownloader::Handler* handler,
                                       const bool              deleteHandler,
                                       const G3MContext*       context) :
  _sector(sector),
  _extent(extent),
  _noDataValue(noDataValue),
  _deltaHeight(deltaHeight),
  _handler(handler),
  _deleteHandler(deleteHandler),
  _context(context)
  {
  }


  virtual ~BILDownloader_BufferDownloadListener() {
    if (_deleteHandler) {
      delete _handler;
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    GAsyncTask* parserTask = new BILDownloader_ParserAsyncTask(buffer,
                                                               _sector,
                                                               _extent,
                                                               _noDataValue,
                                                               _deltaHeight,
                                                               _handler,
                                                               _deleteHandler);
    _context->getThreadUtils()->invokeAsyncTask(parserTask, true);

    _handler = NULL; // moves _handler ownership to BILDownloader_ParserAsyncTask
  }

  void onError(const URL& url) {
    _handler->onDownloadError(_context, url);
  }

  void onCancel(const URL& url) {
    // do nothing!
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired)  {
    // do nothing!
  }
};


void BILDownloader::request(const G3MContext*       context,
                            const URL&              url,
                            long long               priority,
                            const TimeInterval&     timeToCache,
                            bool                    readExpired,
                            const Sector&           sector,
                            const Vector2I&         extent,
                            const double            deltaHeight,
                            const short             noDataValue,
                            BILDownloader::Handler* handler,
                            const bool              deleteHandler) {


  context->getDownloader()->requestBuffer(url,
                                          priority,
                                          timeToCache,
                                          readExpired,
                                          new BILDownloader_BufferDownloadListener(sector,
                                                                                   extent,
                                                                                   noDataValue,
                                                                                   deltaHeight,handler,
                                                                                   deleteHandler,
                                                                                   context),
                                          true);
}
