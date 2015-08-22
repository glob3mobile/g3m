//
//  MapBoo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

#ifndef __G3MiOSSDK__MapBoo__
#define __G3MiOSSDK__MapBoo__

#include "URL.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"

class IG3MBuilder;
class LayerSet;
class IDownloader;
class JSONBaseObject;
class JSONArray;

class MapBoo {

public:


  class Layer {
  private:
    const std::string _type;
    const std::string _url;

    Layer(const std::string& type,
          const std::string& url) :
    _type(type),
    _url(url)
    {
    }

  public:
    static const MapBoo::Layer* fromJSON(const JSONBaseObject* jsonBaseObject);

  };


  class Map {
  private:
    const std::string                       _id;
    const std::string                       _name;
    const std::vector<const MapBoo::Layer*> _layers;
    std::vector<std::string>                _datasetsIDs;
    const int                               _timestamp;

    Map(const std::string&                 id,
        const std::string&                 name,
        std::vector<const MapBoo::Layer*>& layers,
        std::vector<std::string>&          datasetsIDs,
        int                                timestamp) :
    _id(id),
    _name(name),
    _layers(layers),
    _datasetsIDs(datasetsIDs),
    _timestamp(timestamp)
    {
    }

    static std::vector<const MapBoo::Layer*> parseLayers(const JSONArray* jsonArray);
    static std::vector<std::string>          parseDatasetsIDs(const JSONArray* jsonArray);

  public:
    static const MapBoo::Map* fromJSON(const JSONBaseObject* jsonBaseObject);

    ~Map();
  };


  class MapsHandler {
  public:
#ifdef C_CODE
    virtual ~MapsHandler() { }
#endif
#ifdef JAVA_CODE
    void dispose();
#endif

    virtual void onMaps(const std::vector<const MapBoo::Map*> maps) = 0;

    virtual void onDownloadError() = 0;
    virtual void onParseError() = 0;

  };

  class MapsParserAsyncTask : public GAsyncTask {
  private:
    MapsHandler* _handler;
    bool         _deleteHandler;
    IByteBuffer* _buffer;
    bool _parseError;
    std::vector<const Map*> _maps;

  public:

    MapsParserAsyncTask(MapsHandler* handler,
                        bool deleteHandler,
                        IByteBuffer* buffer) :
    _handler(handler),
    _deleteHandler(deleteHandler),
    _buffer(buffer),
    _parseError(true)
    {
    }

    ~MapsParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class MapsBufferDownloadListener : public IBufferDownloadListener {
  private:
    MapsHandler*        _handler;
    bool                _deleteHandler;
    const IThreadUtils* _threadUtils;

  public:

    MapsBufferDownloadListener(MapsHandler* handler,
                               bool deleteHandler,
                               const IThreadUtils* threadUtils) :
    _handler(handler),
    _deleteHandler(deleteHandler),
    _threadUtils(threadUtils)
    {
    }

    ~MapsBufferDownloadListener();

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url) {
      // do nothing
    }

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
      // do nothing
    }

  };


private:
  IG3MBuilder*      _builder;
#ifdef C_CODE
  const URL         _serverURL;
#endif
#ifdef JAVA_CODE
  private final URL _serverURL;
#endif

  LayerSet*           _layerSet;
  IDownloader*        _downloader;
  const IThreadUtils* _threadUtils;

public:
  MapBoo(IG3MBuilder* builder,
         const URL& serverURL);
  
  ~MapBoo();
  
  void requestMaps(MapsHandler* handler,
                   bool deleteHandler = true);
  
};

#endif
