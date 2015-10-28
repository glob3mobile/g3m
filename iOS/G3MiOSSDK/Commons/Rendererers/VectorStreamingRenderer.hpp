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
#include "MarksRenderer.hpp"

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


class VectorStreamingRenderer : public DefaultRenderer {
public:

  class VectorSet;
  class Node;


  class GEOJSONUtils {
  private:
    GEOJSONUtils() {}

  public:
    static Sector*     parseSector(const JSONArray* json);
    static Geodetic2D* parseGeodetic2D(const JSONArray* json);
    static Node*       parseNode(Node*             parent,
                                 const JSONObject* json,
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

    ~Cluster() {
      delete _position;
    }

  };


  class ChildrenParserAsyncTask : public GAsyncTask {
  private:
    Node*               _node;
    bool                _verbose;
    IByteBuffer*        _buffer;
    const IThreadUtils* _threadUtils;

    std::vector<Node*>* _children;

  public:
    ChildrenParserAsyncTask(Node*               node,
                            bool                verbose,
                            IByteBuffer*        buffer,
                            const IThreadUtils* threadUtils) :
    _node(node),
    _verbose(verbose),
    _buffer(buffer),
    _threadUtils(threadUtils),
    _children(NULL)
    {
      _node->_retain();
    }

    ~ChildrenParserAsyncTask();

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
    IByteBuffer*        _buffer;
    const IThreadUtils* _threadUtils;

    std::vector<Cluster*>* _clusters;
    GEOObject*             _features;

  private:
    std::vector<Cluster*>* parseClusters(const JSONArray* clustersJson);

  public:
    FeaturesParserAsyncTask(Node*               node,
                            bool                verbose,
                            IByteBuffer*        buffer,
                            const IThreadUtils* threadUtils) :
    _node(node),
    _verbose(verbose),
    _buffer(buffer),
    _threadUtils(threadUtils),
    _clusters(NULL),
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
    const Sector*                  _minimumSector;
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

    bool _wasVisible;
    bool isVisible(const G3MRenderContext* rc,
                   const Frustum* frustumInModelCoordinates);

    bool _loadedFeatures;
    bool _loadingFeatures;

    bool _wasBigEnough;
    bool isBigEnough(const G3MRenderContext *rc);

    long long _featuresRequestID;
    void loadFeatures(const G3MRenderContext* rc);
    void unloadFeatures();
    void cancelLoadFeatures();

    long long _childrenRequestID;
    void loadChildren(const G3MRenderContext* rc);
    void unloadChildren();
    void cancelLoadChildren();

    void unload();

    void removeMarks();

    long long _clusterMarksCount;
    long long _featureMarksCount;

    void childRendered();
    void childStopRendered();

    void createClusterMarks();

  protected:
    ~Node();

  public:
    Node(const VectorSet*                vectorSet,
         Node*                           parent,
         const std::string&              id,
         const Sector*                   nodeSector,
         const Sector*                   minimumSector,
         const int                       clustersCount,
         const int                       featuresCount,
         const std::vector<std::string>& childrenIDs,
         const bool                      verbose) :
    _vectorSet(vectorSet),
    _parent(parent),
    _id(id),
    _nodeSector(nodeSector),
    _minimumSector(minimumSector),
    _clustersCount(clustersCount),
    _featuresCount(featuresCount),
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
    _childrenRequestID(-1),
    _downloader(NULL),
    _clusters(NULL),
    _features(NULL),
    _clusterMarksCount(0),
    _featureMarksCount(0)
    {
      if (_parent != NULL) {
        _parent->_retain();
      }
    }

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
                        const IThreadUtils*    threadUtils);

    void errorDownloadingChildren() {
      // do nothing by now
    }

    void parsedChildren(std::vector<Node*>* children,
                        const IThreadUtils* threadUtils);

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

    virtual Mark* createFeatureMark(const GEO2DPointGeometry* geometry) const = 0;

    virtual Mark* createClusterMark(const VectorStreamingRenderer::Cluster* cluster,
                                    long long featuresCount) const = 0;

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
    const std::string          _name;
    const VectorSetSymbolizer* _symbolizer;
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
              bool                       haltOnError) :
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
                    bool                       haltOnError);
  
  void removeAllVectorSets();
  
  RenderState getRenderState(const G3MRenderContext* rc);
  
  MarksRenderer* getMarksRenderer() const {
    return _markRenderer;
  }
  
};

#endif
