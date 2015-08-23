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


  class MBLayer {
  private:
    const std::string _type;
    const std::string _url;

    MBLayer(const std::string& type,
            const std::string& url) :
    _type(type),
    _url(url)
    {
    }


  public:
    static MapBoo::MBLayer* fromJSON(const JSONBaseObject* jsonBaseObject);

    ~MBLayer();

    void apply(LayerSet* layerSet);

  };


  class MBMap {
  private:
    const std::string                 _id;
    const std::string                 _name;
#ifdef C_CODE
    std::vector<MapBoo::MBLayer*> _layers;
#endif
#ifdef JAVA_CODE
    private final java.util.ArrayList<MapBoo.MBLayer> _layers;
#endif
    std::vector<std::string>          _datasetsIDs;
    const int                         _timestamp;

    MBMap(const std::string&             id,
          const std::string&             name,
          std::vector<MapBoo::MBLayer*>& layers,
          std::vector<std::string>&      datasetsIDs,
          int                            timestamp) :
    _id(id),
    _name(name),
    _layers(layers),
    _datasetsIDs(datasetsIDs),
    _timestamp(timestamp)
    {
    }

    static std::vector<MapBoo::MBLayer*> parseLayers(const JSONArray* jsonArray);
    static std::vector<std::string>      parseDatasetsIDs(const JSONArray* jsonArray);

  public:
    static MapBoo::MBMap* fromJSON(const JSONBaseObject* jsonBaseObject);

    ~MBMap();

    const std::string getName() const {
      return _name;
    }

    const std::string getID() const {
      return _id;
    }

    void apply(LayerSet* layerSet);
  };

  class MBHandler {
  public:
#ifdef C_CODE
    virtual ~MBHandler() { }
#endif
#ifdef JAVA_CODE
    void dispose();
#endif

    virtual void onMapDownloadError() = 0;
    virtual void onMapParseError() = 0;
    virtual void onSelectedMap(MapBoo::MBMap* map) = 0;
  };


  class MBMapsHandler {
  public:
#ifdef C_CODE
    virtual ~MBMapsHandler() { }
#endif
#ifdef JAVA_CODE
    void dispose();
#endif

    virtual void onMaps(std::vector<MapBoo::MBMap*> maps) = 0;

    virtual void onDownloadError() = 0;
    virtual void onParseError() = 0;

  };


  class MapParserAsyncTask : public GAsyncTask {
  private:
    MapBoo*      _mapboo;
    IByteBuffer* _buffer;
    MBMap*       _map;

  public:
    MapParserAsyncTask(MapBoo*      mapboo,
                       IByteBuffer* buffer) :
    _mapboo(mapboo),
    _buffer(buffer),
    _map(NULL)
    {
    }

    ~MapParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class MapsParserAsyncTask : public GAsyncTask {
  private:
    MBMapsHandler* _handler;
    bool           _deleteHandler;
    IByteBuffer*   _buffer;

    bool                _parseError;
    std::vector<MBMap*> _maps;

  public:

    MapsParserAsyncTask(MBMapsHandler* handler,
                        bool           deleteHandler,
                        IByteBuffer*   buffer) :
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


  class MapBufferDownloadListener : public IBufferDownloadListener {
  private:
    MapBoo*             _mapboo;
    const IThreadUtils* _threadUtils;

  public:
    MapBufferDownloadListener(MapBoo*             mapboo,
                              const IThreadUtils* threadUtils) :
    _mapboo(mapboo),
    _threadUtils(threadUtils)
    {
    }

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


  class MapsBufferDownloadListener : public IBufferDownloadListener {
  private:
    MBMapsHandler*      _handler;
    bool                _deleteHandler;
    const IThreadUtils* _threadUtils;

  public:

    MapsBufferDownloadListener(MBMapsHandler*      handler,
                               bool                deleteHandler,
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
  MBHandler*        _handler;

  std::string _mapID;

  LayerSet*           _layerSet;
  IDownloader*        _downloader;
  const IThreadUtils* _threadUtils;

  void requestMap();
  void applyMap(MapBoo::MBMap* map);


public:
  MapBoo(IG3MBuilder* builder,
         const URL&   serverURL,
         MBHandler*   handler);

  ~MapBoo();

  void requestMaps(MBMapsHandler* handler,
                   bool deleteHandler = true);
  
  void setMapID(const std::string& mapID);
  void setMap(MapBoo::MBMap* map);
  
  void onMapDownloadError();
  void onMapParseError();
  void onMap(MapBoo::MBMap* map);

  void reloadMap() {
    requestMap();
  }

};

#endif
