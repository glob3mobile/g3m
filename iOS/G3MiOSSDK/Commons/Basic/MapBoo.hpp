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
#include "VectorStreamingRenderer.hpp"
#include "MarkTouchListener.hpp"

class IG3MBuilder;
class LayerSet;
class IDownloader;
class JSONBaseObject;
class JSONArray;
class MarksRenderer;


class MapBoo {

public:

  class MBLayer {
  private:
    const std::string _type;
    const std::string _url;
    const std::string _attribution;
    const bool        _verbose;

    MBLayer(const std::string& type,
            const std::string& url,
            const std::string& attribution,
            const bool         verbose) :
    _type(type),
    _url(url),
    _attribution(attribution),
    _verbose(verbose)
    {
    }

    MBLayer(const MBLayer& that);


  public:
    static MapBoo::MBLayer* fromJSON(const JSONBaseObject* jsonBaseObject,
                                     bool verbose);

    ~MBLayer();

    void apply(LayerSet* layerSet) const;

  };

  class MBHandler;


//  class MBDataset : public RCObject {
//  private:
//    MBHandler*               _handler;
//    const std::string        _id;
//    const std::string        _name;
//    //    std::vector<std::string> _labelingCriteria;
//    //    std::vector<std::string> _infoCriteria;
//    const int                _timestamp;
//
//    MBDataset(const MBDataset& that);
//
//    MBDataset(MBHandler*                handler,
//              const std::string&        id,
//              const std::string&        name,
//              //              std::vector<std::string>& labelingCriteria,
//              //              std::vector<std::string>& infoCriteria,
//              const int                 timestamp) :
//    _handler(handler),
//    _id(id),
//    _name(name),
//    //    _labelingCriteria(labelingCriteria),
//    //    _infoCriteria(infoCriteria),
//    _timestamp(timestamp)
//    {
//    }
//
//    //    const std::string  createMarkLabel(const JSONObject* properties) const;
//    //    MarkTouchListener* createMarkTouchListener(const JSONObject* properties) const;
//
//  protected:
//    ~MBDataset();
//
//  public:
//    //    static MapBoo::MBDataset* fromJSON(MBHandler*            handler,
//    //                                       const JSONBaseObject* jsonBaseObject,
//    //                                       bool verbose);
//
//
//    //    void apply(const URL&               serverURL,
//    //               VectorStreamingRenderer* vectorStreamingRenderer) const;
//
//    //    Mark* createMark(const GEO2DPointGeometry* geometry) const;
//
//  };



  class MBShape {
  private:
    MBShape(const MBShape& that);

  protected:
    MBShape() {

    }

  public:
    static const MapBoo::MBShape* fromJSON(const JSONBaseObject* jsonBaseObject);

    virtual ~MBShape() {

    }
  };


  class MBCircleShape : public MBShape {
  private:
    const Color _color;
    const int   _radius;

    MBCircleShape(const Color& color,
                  int radius) :
    _color(color),
    _radius(radius)
    {

    }

  public:
    static const MapBoo::MBCircleShape* fromJSON(const JSONObject* jsonObject);

    ~MBCircleShape() {
#ifdef JAVA_CODE
      super.dispose();
#endif
    }
  };


  class MBSymbolizedDataset : public RCObject {
  private:
    MBHandler*               _handler;
    const std::string        _datasetID;
    const std::string        _datasetName;
    const std::string        _datasetAttribution;
    std::vector<std::string> _labeling;
    const MBShape*           _shape;
    std::vector<std::string> _info;

    MBSymbolizedDataset(MBHandler*                handler,
                        const std::string&        datasetID,
                        const std::string&        datasetName,
                        const std::string&        datasetAttribution,
                        std::vector<std::string>& labeling,
                        const MBShape*            shape,
                        std::vector<std::string>& info) :
    _handler(handler),
    _datasetID(datasetID),
    _datasetName(datasetName),
    _datasetAttribution(datasetAttribution),
    _labeling(labeling),
    _shape(shape),
    _info(info)
    {

    }

    const std::string  createMarkLabel(const JSONObject* properties) const;
    MarkTouchListener* createMarkTouchListener(const JSONObject* properties) const;

  protected:
    ~MBSymbolizedDataset() {
      delete _shape;
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

  public:
    static MapBoo::MBSymbolizedDataset* fromJSON(MBHandler*            handler,
                                                 const JSONBaseObject* jsonBaseObject,
                                                 bool verbose);

    void apply(const URL&               serverURL,
               VectorStreamingRenderer* vectorStreamingRenderer) const;

    Mark* createMark(const GEO2DPointGeometry* geometry) const;

  };


  class MBDatasetVectorSetSymbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
  private:
    const MBSymbolizedDataset* _symbolizedDataset;

  public:
    MBDatasetVectorSetSymbolizer(const MBSymbolizedDataset* symbolizedDataset) :
    _symbolizedDataset(symbolizedDataset)
    {
      _symbolizedDataset->_retain();
    }

    ~MBDatasetVectorSetSymbolizer() {
      _symbolizedDataset->_release();
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

    Mark* createMark(const GEO2DPointGeometry* geometry) const {
      return _symbolizedDataset->createMark( geometry );
    }
  };


  class MBMap {
  private:
    const std::string                         _id;
    const std::string                         _name;
    std::vector<MapBoo::MBLayer*>             _layers;
    std::vector<MapBoo::MBSymbolizedDataset*> _symbolizedDatasets;
    const int                                 _timestamp;
    const bool                                _verbose;

    MBMap(const MBMap& that);

    MBMap(const std::string&                         id,
          const std::string&                         name,
          std::vector<MapBoo::MBLayer*>&             layers,
          std::vector<MapBoo::MBSymbolizedDataset*>& symbolizedDatasets,
          int                                        timestamp,
          bool                                       verbose) :
    _id(id),
    _name(name),
    _layers(layers),
    _symbolizedDatasets(symbolizedDatasets),
    _timestamp(timestamp),
    _verbose(verbose)
    {
    }

    static std::vector<MapBoo::MBLayer*>   parseLayers(const JSONArray* jsonArray,
                                                       bool verbose);
    //    static std::vector<MapBoo::MBDataset*> parseDatasets(MBHandler*       handler,
    //                                                         const JSONArray* jsonArray,
    //                                                         bool verbose);

    static std::vector<MapBoo::MBSymbolizedDataset*> parseSymbolizedDatasets(MBHandler*       handler,
                                                                             const JSONArray* jsonArray,
                                                                             bool verbose);


  public:
    static MapBoo::MBMap* fromJSON(MBHandler*            handler,
                                   const JSONBaseObject* jsonBaseObject,
                                   bool verbose);

    ~MBMap();

    const std::string getName() const {
      return _name;
    }

    const std::string getID() const {
      return _id;
    }

    void apply(const URL&               serverURL,
               LayerSet*                layerSet,
               VectorStreamingRenderer* vectorStreamingRenderer);
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

    virtual void onFeatureTouched(const std::string& datasetName,
                                  const std::vector<std::string>& info,
                                  const JSONObject* properties) = 0;
  };


  class MBFeatureMarkTouchListener : public MarkTouchListener {
  private:
    const std::string        _datasetName;
    MBHandler*               _handler;
    std::vector<std::string> _info;
    const JSONObject*        _properties;

  public:
    MBFeatureMarkTouchListener(const std::string&              datasetName,
                               MBHandler*                      handler,
                               const std::vector<std::string>& info,
                               const JSONObject*               properties) :
    _datasetName(datasetName),
    _handler(handler),
    _info(info),
    _properties(properties)
    {
    }

    bool touchedMark(Mark* mark);

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
    MBHandler*   _handler;
    IByteBuffer* _buffer;
    MBMap*       _map;
    const bool   _verbose;

  public:
    MapParserAsyncTask(MapBoo*      mapboo,
                       MBHandler*   handler,
                       IByteBuffer* buffer,
                       const bool   verbose) :
    _mapboo(mapboo),
    _handler(handler),
    _buffer(buffer),
    _verbose(verbose),
    _map(NULL)
    {
    }

    ~MapParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class MapsParserAsyncTask : public GAsyncTask {
  private:
    MBHandler*     _handler;
    MBMapsHandler* _mapsHandler;
    bool           _deleteHandler;
    IByteBuffer*   _buffer;
    const bool     _verbose;

    bool                _parseError;
    std::vector<MBMap*> _maps;

  public:

    MapsParserAsyncTask(MBHandler*     handler,
                        MBMapsHandler* mapsHandler,
                        bool           deleteHandler,
                        IByteBuffer*   buffer,
                        bool           verbose) :
    _handler(handler),
    _mapsHandler(mapsHandler),
    _deleteHandler(deleteHandler),
    _buffer(buffer),
    _verbose(verbose),
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
    MBHandler*          _handler;
    const IThreadUtils* _threadUtils;
    const bool          _verbose;
  public:
    MapBufferDownloadListener(MapBoo*             mapboo,
                              MBHandler*          handler,
                              const IThreadUtils* threadUtils,
                              const bool          verbose) :
    _mapboo(mapboo),
    _handler(handler),
    _threadUtils(threadUtils),
    _verbose(verbose)
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
    MBHandler*          _handler;
    MBMapsHandler*      _mapsHandler;
    bool                _deleteHandler;
    const IThreadUtils* _threadUtils;
    const bool          _verbose;

  public:

    MapsBufferDownloadListener(MBHandler*          handler,
                               MBMapsHandler*      mapsHandler,
                               bool                deleteHandler,
                               const IThreadUtils* threadUtils,
                               bool                verbose) :
    _handler(handler),
    _mapsHandler(mapsHandler),
    _deleteHandler(deleteHandler),
    _threadUtils(threadUtils),
    _verbose(verbose)
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
  const bool        _verbose;

  std::string _mapID;

  LayerSet*                _layerSet;
  VectorStreamingRenderer* _vectorStreamingRenderer;
  MarksRenderer*           _markRenderer;
  IDownloader*             _downloader;
  const IThreadUtils*      _threadUtils;

  void requestMap();
  void applyMap(MapBoo::MBMap* map);


  MapBoo(const MapBoo& that);

public:
  MapBoo(IG3MBuilder* builder,
         const URL&   serverURL,
         MBHandler*   handler,
         bool         verbose);
  
  ~MapBoo();
  
  void requestMaps(MBMapsHandler* mapsHandler,
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
