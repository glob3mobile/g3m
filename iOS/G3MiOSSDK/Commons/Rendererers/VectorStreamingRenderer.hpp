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
#include "MarksFilter.hpp"
#include "GAsyncTask.hpp"

#include <vector>
#include <string>

class IThreadUtils;
class IByteBuffer;
class Sector;
class Geodetic2D;
class JSONBaseObject;
class JSONArray;
class JSONObject;
class Mark;
class GEO2DPointGeometry;
class BoundingVolume;
class Camera;
class Frustum;
class IDownloader;
class GEOObject;
class MarksRenderer;


class VectorStreamingRenderer : public DefaultRenderer {
public:

  enum Format {
    SERVER,
    PLAIN_FILES
  };

  class VectorSet;
  class Node;


  class GEOJSONUtils {
  private:
    GEOJSONUtils() {}

  public:
    static Sector*     parseSector(const JSONArray* json);
    static Geodetic2D* parseGeodetic2D(const JSONArray* json);
    static Node*       parseNode(const JSONObject* json,
                                 const VectorSet*  vectorSet,
                                 const bool        verbose);

  };


  class Cluster {
  private:
    const Geodetic2D* _position;
    const long long   _size;
  public:
    Cluster(const Geodetic2D* position,
            const long long   size) :
    _position(position),
    _size(size)
    {

    }

    const Geodetic2D* getPosition() const {
      return _position;
    }

    const long long getSize() const {
      return _size;
    }

    ~Cluster();

  };


  class ChildrenParserAsyncTask : public GAsyncTask {
  private:
    Node*               _node;
    bool                _verbose;
    bool                _isCanceled;
    IByteBuffer*        _buffer;

    std::vector<Node*>* _children;

  public:
    ChildrenParserAsyncTask(Node*        node,
                            bool         verbose,
                            IByteBuffer* buffer) :
    _node(node),
    _verbose(verbose),
    _buffer(buffer),
    _isCanceled(false),
    _children(NULL)
    {
      _node->_retain();
    }

    ~ChildrenParserAsyncTask();

    void cancel() {
      _isCanceled = true;
    }

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };



  class NodeChildrenDownloadListener : public IBufferDownloadListener {
  private:
    Node*               _node;
    const IThreadUtils* _threadUtils;
    const bool          _verbose;

  public:
    NodeChildrenDownloadListener(Node* node,
                                 const IThreadUtils* threadUtils,
                                 bool verbose) :
    _node(node),
    _threadUtils(threadUtils),
    _verbose(verbose)
    {
      _node->_retain();
    }

    ~NodeChildrenDownloadListener() {
      _node->_release();
#ifdef JAVA_CODE
      super.dispose();
#endif
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



  class FeaturesParserAsyncTask : public GAsyncTask {
  private:
    Node*               _node;
    bool                _verbose;
    bool                _isCanceled;
    IByteBuffer*        _buffer;

    std::vector<Cluster*>* _clusters;
    GEOObject*             _features;
    std::vector<Node*>*    _children;

  private:
    std::vector<Cluster*>* parseClusters(const JSONArray* clustersJson);
    std::vector<Node*>*    parseChildren(const JSONBaseObject* jsonBaseObject);

  public:
    FeaturesParserAsyncTask(Node*        node,
                            bool         verbose,
                            IByteBuffer* buffer) :
    _node(node),
    _verbose(verbose),
    _buffer(buffer),
    _isCanceled(false),
    _clusters(NULL),
    _features(NULL),
    _children(NULL)
    {
      _node->_retain();
    }

    ~FeaturesParserAsyncTask();

    void cancel() {
      _isCanceled = true;
    }

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
#ifdef JAVA_CODE
      super.dispose();
#endif
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


  class NodeAllMarksFilter : public MarksFilter {
  private:
    std::string _nodeClusterToken;
    std::string _nodeFeatureToken;

  public:
    NodeAllMarksFilter(const Node* node);

    bool test(const Mark* mark) const;

  };

  class NodeClusterMarksFilter : public MarksFilter {
  private:
    std::string _nodeClusterToken;

  public:
    NodeClusterMarksFilter(const Node* node);

    bool test(const Mark* mark) const;

  };



  class Node : public RCObject {
  private:
    const VectorSet*               _vectorSet;
    Node*                          _parent;
    const std::string              _id;
    const Sector*                  _nodeSector;
    const int                      _clustersCount;
    const int                      _featuresCount;
#ifdef C_CODE
    const std::vector<std::string> _childrenIDs;
#endif
#ifdef JAVA_CODE
    private final java.util.ArrayList<String> _childrenIDs;
#endif

    std::vector<Node*>* _children;
    size_t _childrenSize;

    const bool _verbose;

    std::vector<Cluster*>* _clusters;
    GEOObject*             _features;

    BoundingVolume* _boundingVolume;
    BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);

    IDownloader* _downloader;
    bool _loadingChildren;

    bool isVisible(const G3MRenderContext* rc,
                   const Frustum* frustumInModelCoordinates);

    bool _loadedFeatures;
    bool _loadingFeatures;

    bool isBigEnough(const G3MRenderContext *rc);

    bool _isBeingRendered;

    long long _featuresRequestID;
    void loadFeatures(const G3MRenderContext* rc);
    void unloadFeatures();
    void cancelLoadFeatures();

    long long _childrenRequestID;
    void loadChildren(const G3MRenderContext* rc);
    void unloadChildren();
    void cancelLoadChildren();


    void removeMarks();

    long long _clusterMarksCount;
    long long _featureMarksCount;

    void childRendered();
    void childStopRendered();

    void createClusterMarks();

    void cancelTasks();

    void setParent(Node* parent);

    void setChildren(std::vector<Node*>* children);

  protected:
    ~Node();

  public:
    ChildrenParserAsyncTask *_childrenTask;
    FeaturesParserAsyncTask *_featuresTask;

    Node(const VectorSet*                vectorSet,
         const std::string&              id,
         const Sector*                   nodeSector,
         const int                       clustersCount,
         const int                       featuresCount,
         const std::vector<std::string>& childrenIDs,
         std::vector<Node*>*             children,
         const bool                      verbose);

    void unload();

    const VectorSet* getVectorSet() const {
      return _vectorSet;
    }

    const std::string getFullName() const {
      return _vectorSet->getName() + "/" + _id;
    }

    const std::string getFeatureMarkToken() const {
      return _id + "_F_" + _vectorSet->getName() ;
    }

    const std::string getClusterMarkToken() const {
      return _id + "_C_" + _vectorSet->getName() ;
    }

    long long render(const G3MRenderContext* rc,
                     const Frustum* frustumInModelCoordinates,
                     const long long cameraTS,
                     GLState* glState);

    void errorDownloadingFeatures() {
      // do nothing by now
    }

    void parsedFeatures(std::vector<Cluster*>* clusters,
                        GEOObject*             features,
                        std::vector<Node*>*    children);

    void errorDownloadingChildren() {
      // do nothing by now
    }

    void parsedChildren(std::vector<Node*>* children);

  };


  class MetadataParserAsyncTask : public GAsyncTask {
  private:
    VectorSet*   _vectorSet;
    const bool   _verbose;
    IByteBuffer* _buffer;

    bool         _parsingError;

    Sector*             _sector;
    long long           _clustersCount;
    long long           _featuresCount;
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
    _clustersCount(-1),
    _featuresCount(-1),
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
    virtual ~VectorSetSymbolizer() { }

    virtual Mark* createFeatureMark(const VectorStreamingRenderer::Node* node,
                                    const GEO2DPointGeometry* geometry) const = 0;

    virtual Mark* createClusterMark(const VectorStreamingRenderer::Node* node,
                                    const VectorStreamingRenderer::Cluster* cluster,
                                    long long featuresCount) const = 0;

  };


  class VectorSet {
  private:
    VectorStreamingRenderer* _renderer;
#ifdef C_CODE
    const URL _serverURL;
    const VectorSetSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
    private final URL _serverURL;
    private VectorSetSymbolizer _symbolizer;
#endif
    const std::string          _name;
    const bool                 _deleteSymbolizer;
    const long long            _downloadPriority;
#ifdef C_CODE
    const TimeInterval         _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool                 _readExpired;
    const bool                 _verbose;
    const bool                 _haltOnError;
    const Format               _format;

    const std::string _properties;

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    Sector*             _sector;
    long long           _clustersCount;
    long long           _featuresCount;
    int                 _nodesCount;
    int                 _minNodeDepth;
    int                 _maxNodeDepth;
    std::vector<Node*>* _rootNodes;
    size_t              _rootNodesSize;

    long long _lastRenderedCount;

    const URL getMetadataURL() const;

    const std::string toNodesDirectories(const std::string& nodeID) const;

  public:

    VectorSet(VectorStreamingRenderer*   renderer,
              const URL&                 serverURL,
              const std::string&         name,
              const std::string&         properties,
              const VectorSetSymbolizer* symbolizer,
              const bool                 deleteSymbolizer,
              long long                  downloadPriority,
              const TimeInterval&        timeToCache,
              bool                       readExpired,
              bool                       verbose,
              bool                       haltOnError,
              const Format               format) :
    _renderer(renderer),
    _serverURL(serverURL),
    _name(name),
    _properties(properties),
    _symbolizer(symbolizer),
    _deleteSymbolizer(deleteSymbolizer),
    _downloadPriority(downloadPriority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _verbose(verbose),
    _haltOnError(haltOnError),
    _format(format),
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _sector(NULL),
    _rootNodes(NULL),
    _rootNodesSize(0),
    _lastRenderedCount(0)
    {

    }

    ~VectorSet();

    const URL getNodeFeaturesURL(const std::string& nodeID) const;

    const URL getNodeChildrenURL(const std::string& nodeID,
                                 const std::vector<std::string>& childrenIDs) const;

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

    void initialize(const G3MContext* context);

    RenderState getRenderState(const G3MRenderContext* rc);

    void errorDownloadingMetadata();
    void errorParsingMetadata();
    void parsedMetadata(Sector* sector,
                        long long clustersCount,
                        long long featuresCount,
                        int nodesCount,
                        int minNodeDepth,
                        int maxNodeDepth,
                        std::vector<Node*>* rootNodes);

    void render(const G3MRenderContext* rc,
                const Frustum* frustumInModelCoordinates,
                const long long cameraTS,
                GLState* glState);

    long long createFeatureMark(const Node* node,
                                const GEO2DPointGeometry* geometry) const;

    long long createClusterMarks(const Node* node,
                                 const std::vector<Cluster*>* clusters) const;

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
                    const std::string&         properties,
                    const VectorSetSymbolizer* symbolizer,
                    const bool                 deleteSymbolizer,
                    long long                  downloadPriority,
                    const TimeInterval&        timeToCache,
                    bool                       readExpired,
                    bool                       verbose,
                    bool                       haltOnError,
                    const Format               format);
  
  void removeAllVectorSets();
  
  RenderState getRenderState(const G3MRenderContext* rc);
  
  MarksRenderer* getMarksRenderer() const {
    return _markRenderer;
  }
  
};

#endif
