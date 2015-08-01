//
//  VectorStreamingRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//

#ifndef __G3MiOSSDK__VectorStreamingRenderer__
#define __G3MiOSSDK__VectorStreamingRenderer__

#include "DefaultRenderer.hpp"

#include "URL.hpp"
#include "TimeInterval.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "RCObject.hpp"

#include <vector>
#include <string>

class IThreadUtils;
class IByteBuffer;
class Sector;
class Geodetic2D;
class JSONArray;
class JSONObject;
class Mark;
class GEO2DPointGeometry;
class BoundingVolume;
class Camera;
class Frustum;
class IDownloader;
class GEOObject;

#include "MarksRenderer.hpp"

class VectorStreamingRenderer : public DefaultRenderer {
public:

  class VectorSet;
  class Node;


  class GEOJSONUtils {
  private:
    GEOJSONUtils();

  public:
    static Sector*     parseSector(const JSONArray* json);
    static Geodetic2D* parseGeodetic2D(const JSONArray* json);
    static Node*       parseNode(const JSONObject* json,
                                 const VectorSet*  vectorSet,
                                 const bool        verbose);

  };


  class FeaturesParserAsyncTask : public GAsyncTask {
  private:
    Node*        _node;
    bool         _verbose;
    IByteBuffer* _buffer;
    const IThreadUtils* _threadUtils;

    GEOObject* _features;

  public:
    FeaturesParserAsyncTask(Node*               node,
                            bool                verbose,
                            IByteBuffer*        buffer,
                            const IThreadUtils* threadUtils) :
    _node(node),
    _verbose(verbose),
    _buffer(buffer),
    _threadUtils(threadUtils),
    _features(NULL)
    {
      _node->_retain();
    }

    ~FeaturesParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class NodeFeaturesDownloadListener : public IBufferDownloadListener {
  private:
    Node*               _node;
    const IThreadUtils* _threadUtils;
    const bool          _verbose;

  public:
    NodeFeaturesDownloadListener(Node* node,
                                 const IThreadUtils* threadUtils,
                                 bool verbose) :
    _node(node),
    _threadUtils(threadUtils),
    _verbose(verbose)
    {
      _node->_retain();
    }

    ~NodeFeaturesDownloadListener() {
      _node->_release();
    }

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired);

  };


  class NodeMarksFilter : public MarksFilter {
  private:
    std::string _nodeToken;

  public:
    NodeMarksFilter(const Node* node);

    bool test(const Mark* mark) const;

  };


  class Node : public RCObject {
  private:
    const VectorSet*               _vectorSet;
    const std::string              _id;
    const Sector*                  _sector;
    const int                      _featuresCount;
    const Geodetic2D*              _averagePosition;
#ifdef C_CODE
    const std::vector<std::string> _childrenIDs;
#endif
#ifdef JAVA_CODE
    private final java.util.ArrayList<String> _childrenIDs;
#endif

    std::vector<Node*>* _children;
    size_t _childrenSize;

    const bool _verbose;

    GEOObject* _features;

    BoundingVolume* _boundingVolume;
    BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);

    IDownloader* _downloader;
    bool _loadingChildren;

    bool _wasVisible;
    bool isVisible(const G3MRenderContext* rc,
                   const Frustum* frustumInModelCoordinates);

    bool _loadedFeatures;
    bool _loadingFeatures;

    bool _wasBigEnough;
    bool isBigEnough(const G3MRenderContext *rc);
    long long renderFeatures(const G3MRenderContext *rc,
                             const GLState *glState);

    long long _featuresRequestID;
    void loadFeatures(const G3MRenderContext* rc);
    void unloadFeatures();
    void cancelLoadFeatures();

    void loadChildren(const G3MRenderContext* rc);
    void unloadChildren();
    void cancelLoadChildren();

    void unload();

    void removeMarks();

    size_t _marksCount;

  protected:
    ~Node();

  public:
    Node(const VectorSet*                vectorSet,
         const std::string&              id,
         const Sector*                   sector,
         const int                       featuresCount,
         const Geodetic2D*               averagePosition,
         const std::vector<std::string>& childrenIDs,
         const bool                      verbose) :
    _vectorSet(vectorSet),
    _id(id),
    _sector(sector),
    _featuresCount(featuresCount),
    _averagePosition(averagePosition),
    _childrenIDs(childrenIDs),
    _verbose(verbose),
    _wasVisible(false),
    _loadedFeatures(false),
    _loadingFeatures(false),
    _children(NULL),
    _childrenSize(0),
    _loadingChildren(false),
    _wasBigEnough(false),
    _boundingVolume(NULL),
    _featuresRequestID(-1),
    _downloader(NULL),
    _features(NULL),
    _marksCount(0)
    {

    }

    const std::string getFullName() const {
      return _vectorSet->getName() + "/" + _id;
    }

    const std::string getMarkToken() const {
      return getFullName();
    }

    long long render(const G3MRenderContext* rc,
                     const Frustum* frustumInModelCoordinates,
                     const long long cameraTS,
                     GLState* glState);

    void errorDownloadingFeatures() {
      // do nothing by now
    }

    void parsedFeatures(GEOObject* features,
                        const IThreadUtils* threadUtils);

  };


  class MetadataParserAsyncTask : public GAsyncTask {
  private:
    VectorSet*   _vectorSet;
    const bool   _verbose;
    IByteBuffer* _buffer;

    bool         _parsingError;

    Sector*             _sector;
    long long           _featuresCount;
    Geodetic2D*         _averagePosition;
    int                 _nodesCount;
    int                 _minNodeDepth;
    int                 _maxNodeDepth;
    std::vector<Node*>* _rootNodes;

  public:
    MetadataParserAsyncTask(VectorSet* vectorSet,
                            bool verbose,
                            IByteBuffer* buffer) :
    _vectorSet(vectorSet),
    _verbose(verbose),
    _buffer(buffer),
    _parsingError(false),
    _sector(NULL),
    _featuresCount(-1),
    _averagePosition(NULL),
    _nodesCount(-1),
    _minNodeDepth(-1),
    _maxNodeDepth(-1),
    _rootNodes(NULL)
    {
    }

    ~MetadataParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class MetadataDownloadListener : public IBufferDownloadListener {
  private:
    VectorSet*          _vectorSet;
    const IThreadUtils* _threadUtils;
    const bool          _verbose;

  public:
    MetadataDownloadListener(VectorSet* vectorSet,
                             const IThreadUtils* threadUtils,
                             bool verbose) :
    _vectorSet(vectorSet),
    _threadUtils(threadUtils),
    _verbose(verbose)
    {
    }

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired);

  };


  class VectorSetSymbolizer {
  public:
    virtual ~VectorSetSymbolizer() {
    }

    virtual Mark* createMark(const GEO2DPointGeometry* geometry) const = 0;

  };


  class VectorSet {
  private:
    VectorStreamingRenderer* _renderer;
#ifdef C_CODE
    const URL _serverURL;
#endif
#ifdef JAVA_CODE
    private final URL _serverURL;
#endif
    const std::string  _name;
    const VectorSetSymbolizer* _symbolizer;
    const bool                 _deleteSymbolizer;
    const long long    _downloadPriority;
#ifdef C_CODE
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;
    const bool         _verbose;

    const std::string _properties;
    
    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    Sector*             _sector;
    long long           _featuresCount;
    Geodetic2D*         _averagePosition;
    int                 _nodesCount;
    int                 _minNodeDepth;
    int                 _maxNodeDepth;
    std::vector<Node*>* _rootNodes;
    size_t              _rootNodesSize;

    long long _lastRenderedCount;

  public:

    VectorSet(VectorStreamingRenderer*   renderer,
              const URL&                 serverURL,
              const std::string&         name,
              const VectorSetSymbolizer* symbolizer,
              const std::string&         properties,
              const bool                 deleteSymbolizer,
              long long                  downloadPriority,
              const TimeInterval&        timeToCache,
              bool                       readExpired,
              bool                       verbose) :
    _renderer(renderer),
    _serverURL(serverURL),
    _name(name),
    _symbolizer(symbolizer),
    _properties(properties),
    _deleteSymbolizer(deleteSymbolizer),
    _downloadPriority(downloadPriority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _verbose(verbose),
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _sector(NULL),
    _averagePosition(NULL),
    _rootNodes(NULL),
    _rootNodesSize(0),
    _lastRenderedCount(0)
    {

    }

    ~VectorSet();

    const URL getServerURL() const {
      return _serverURL;
    }

    const std::string getName() const {
      return _name;
    }

    long long getDownloadPriority() const {
      return _downloadPriority;
    }

    TimeInterval getTimeToCache() const  {
      return _timeToCache;
    }

    bool getReadExpired() const {
      return _readExpired;
    }

    const std::string getProperties() const {
      return _properties;
    }

    void initialize(const G3MContext* context);

    RenderState getRenderState(const G3MRenderContext* rc);

    void errorDownloadingMetadata();
    void errorParsingMetadata();
    void parsedMetadata(Sector* sector,
                        long long featuresCount,
                        Geodetic2D* averagePosition,
                        int nodesCount,
                        int minNodeDepth,
                        int maxNodeDepth,
                        std::vector<Node*>* rootNodes);

    void render(const G3MRenderContext* rc,
                const Frustum* frustumInModelCoordinates,
                const long long cameraTS,
                GLState* glState);

    size_t createMark(const Node* node,
                      const GEO2DPointGeometry* geometry) const;

    MarksRenderer* getMarksRenderer() const {
      return _renderer->getMarkRenderer();
    }

  };


private:
  MarksRenderer* _markRenderer;

  size_t                  _vectorSetsSize;
  std::vector<VectorSet*> _vectorSets;

  std::vector<std::string> _errors;

  GLState* _glState;
  void updateGLState(const Camera* camera);

public:

  VectorStreamingRenderer(MarksRenderer* markRenderer);

  ~VectorStreamingRenderer();

  MarksRenderer* getMarkRenderer() const {
    return _markRenderer;
  }

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void onChangedContext();

  void addVectorSet(const URL&                 serverURL,
                    const std::string&         name,
                    const VectorSetSymbolizer* symbolizer,
                    const bool                 deleteSymbolizer,
                    long long                  downloadPriority,
                    const TimeInterval&        timeToCache,
                    bool                       readExpired,
                    bool                       verbose);
  
  void removeAllVectorSets();
  
  RenderState getRenderState(const G3MRenderContext* rc);

  MarksRenderer* getMarksRenderer() const {
    return _markRenderer;
  }

};

#endif
