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

#include "GAsyncTask.hpp"
#include "IBufferDownloadListener.hpp"
#include "MarkFilter.hpp"
#include "MeshFilter.hpp"
#include "RCObject.hpp"
#include "TimeInterval.hpp"
#include "Angle.hpp"

class Sector;
class JSONArray;
class Geodetic2D;
class JSONObject;
class IByteBuffer;
class GEOObject;
class IThreadUtils;
class JSONBaseObject;
class BoundingVolume;
class IDownloader;
class Frustum;
class GEO2DPointGeometry;
class GEO3DPointGeometry;
class MarksRenderer;
class Camera;
class GEOMeshes;
class Planet;
class Mesh;
class MeshRenderer;


class VectorStreamingRenderer : public DefaultRenderer {
public:

  enum Format {
    SERVER,
    PLAIN_FILES
  };

  class VectorSet;
  class Metadata;
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
    std::vector<Cluster*>* parseClusters(const JSONArray* jsonArray);
    GEOObject*             parseFeatures(const JSONObject* jsonObject,
                                         const Planet* planet);
    GEOMeshes*             parseMeshes(const JSONObject* jsonObject,
                                       const Planet* planet);
    std::vector<Node*>*    parseChildren(const JSONArray* jsonArray);

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


  class NodeAllMarkFilter : public MarkFilter {
  private:
    const std::string _clusterToken;
    const std::string _featureToken;

  public:
    NodeAllMarkFilter(const Node* node);

    bool test(const Mark* mark) const;

  };


  class NodeAllMeshFilter : public MeshFilter {
  private:
    const std::string _featureToken;

  public:
    NodeAllMeshFilter(const Node* node);

    bool test(const Mesh* mesh) const;

  };


  class NodeClusterMarkFilter : public MarkFilter {
  private:
    const std::string _clusterToken;

  public:
    NodeClusterMarkFilter(const Node* node);

    bool test(const Mark* mark) const;

  };



  class Node : public RCObject {
  private:
    const VectorSet*               _vectorSet;
    Node*                          _parent;
    const std::string              _id;
    const Sector*                  _nodeSector;
    const double                   _minHeight;
    const double                   _maxHeight;
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

    BoundingVolume* _boundingVolume;
    BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);

    IDownloader* _downloader;
    bool _loadingChildren;

    bool isVisible(const G3MRenderContext* rc,
                   const VectorStreamingRenderer::VectorSet* vectorSet,
                   const Frustum* frustumInModelCoordinates);

    bool _loadedFeatures;
    bool _loadingFeatures;

    bool isBigEnough(const G3MRenderContext *rc,
                     const VectorStreamingRenderer::VectorSet* vectorSet);

    bool _isBeingRendered;

    long long _featuresRequestID;
    void loadFeatures(const G3MRenderContext* rc);
    void unloadFeatures();
    void cancelLoadFeatures();

    long long _childrenRequestID;
    void loadChildren(const G3MRenderContext* rc);
    void unloadChildren();
    void cancelLoadChildren();


    void removeFeaturesSymbols();

    int _featureSymbolsCount;
    int _clusterSymbolsCount;

    void childRendered();
    void childStopRendered();

    void createClusterMarks();

    void cancelTasks();

    void setParent(Node* parent);

    void setChildren(std::vector<Node*>* children);

    int getDepth() const {
      return (_parent == NULL) ? 1 : (_parent->getDepth() + 1);
    }

  protected:
    ~Node();

  public:
    ChildrenParserAsyncTask *_childrenTask;
    FeaturesParserAsyncTask *_featuresTask;

    Node(const VectorSet*                vectorSet,
         const std::string&              id,
         const Sector*                   nodeSector,
         const double                    minHeight,
         const double                    maxHeight,
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

    const std::string getFeatureToken() const {
      return _id + "_F_" + _vectorSet->getName() ;
    }

    const std::string getClusterToken() const {
      return _id + "_C_" + _vectorSet->getName() ;
    }

    long long render(const G3MRenderContext* rc,
                     const VectorStreamingRenderer::VectorSet* vectorSet,
                     const Frustum* frustumInModelCoordinates,
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

    Metadata*           _metadata;
    std::vector<Node*>* _rootNodes;

  public:
    MetadataParserAsyncTask(VectorSet* vectorSet,
                            bool verbose,
                            IByteBuffer* buffer) :
    _vectorSet(vectorSet),
    _verbose(verbose),
    _buffer(buffer),
    _parsingError(false),
    _metadata(NULL),
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


  class MagnitudeMetadata {
  public:
    static const MagnitudeMetadata* fromJSON(const JSONObject* jsonObject);

    const std::string _name;
    const double      _min;
    const double      _max;
    const double      _average;

    virtual ~MagnitudeMetadata() { }

  private:
    MagnitudeMetadata(const std::string& name,
                      const double       min,
                      const double       max,
                      const double       average) :
    _name(name),
    _min(min),
    _max(max),
    _average(average)
    {

    }

  };


  class VectorSetSymbolizer {
  public:
    virtual ~VectorSetSymbolizer() { }

    virtual Mark* createGeometryMark(const VectorStreamingRenderer::Metadata* metadata,
                                     const VectorStreamingRenderer::Node* node,
                                     const GEO2DPointGeometry* geometry) const = 0;

    virtual Mark* createGeometryMark(const VectorStreamingRenderer::Metadata* metadata,
                                     const VectorStreamingRenderer::Node* node,
                                     const GEO3DPointGeometry* geometry) const = 0;

    virtual Mark* createClusterMark(const VectorStreamingRenderer::Metadata* metadata,
                                    const VectorStreamingRenderer::Node* node,
                                    const VectorStreamingRenderer::Cluster* cluster) const = 0;
  };


  class Metadata {
  public:
    const Sector*            _sector;
    const long long          _clustersCount;
    const long long          _featuresCount;
    const int                _nodesCount;
    const int                _minNodeDepth;
    const int                _maxNodeDepth;
    const std::string        _language;
    const std::string        _nameFieldName;
    const std::string        _urlFieldName;
    const MagnitudeMetadata* _magnitudeMetadata;

    Metadata(const Sector*            sector,
             const long long          clustersCount,
             const long long          featuresCount,
             const int                nodesCount,
             const int                minNodeDepth,
             const int                maxNodeDepth,
             const std::string&       language,
             const std::string&       nameFieldName,
             const std::string&       urlFieldName,
             const MagnitudeMetadata* magnitudeMetadata) :
    _sector(sector),
    _clustersCount(clustersCount),
    _featuresCount(featuresCount),
    _nodesCount(nodesCount),
    _minNodeDepth(minNodeDepth),
    _maxNodeDepth(maxNodeDepth),
    _language(language),
    _nameFieldName(nameFieldName),
    _urlFieldName(urlFieldName),
    _magnitudeMetadata(magnitudeMetadata)
    {

    }

    ~Metadata();

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

    Metadata*           _metadata;
    std::vector<Node*>* _rootNodes;
    size_t              _rootNodesSize;

    long long _lastRenderedCount;

    const URL getMetadataURL() const;

    const std::string toNodesDirectories(const std::string& nodeID) const;

  public:
    const Angle  _minSectorSize;
    const double _minProjectedArea;

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
              const Format               format,
              const Angle&               minSectorSize,
              const double               minProjectedArea) :
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
    _metadata(NULL),
    _rootNodes(NULL),
    _rootNodesSize(0),
    _lastRenderedCount(0),
    _minSectorSize(minSectorSize),
    _minProjectedArea(minProjectedArea)
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
    void parsedMetadata(Metadata* metadata,
                        std::vector<Node*>* rootNodes);

    void render(const G3MRenderContext* rc,
                const Frustum* frustumInModelCoordinates,
                GLState* glState);

    int symbolizeGeometry(const Node* node,
                          const GEO2DPointGeometry* geometry) const;
    
    int symbolizeGeometry(const Node* node,
                          const GEO3DPointGeometry* geometry) const;
    
    int symbolizeClusters(const Node* node,
                          const std::vector<Cluster*>* clusters) const;

    int symbolizeMeshes(const Node* node,
                        const std::vector<Mesh*>& meshes) const;

    MarksRenderer* getMarksRenderer() const {
      return _renderer->getMarksRenderer();
    }

    MeshRenderer* getMeshRenderer() const {
      return _renderer->getMeshRenderer();
    }

  };




private:
  MarksRenderer* _marksRenderer;
  MeshRenderer*  _meshRenderer;

  size_t                  _vectorSetsSize;
  std::vector<VectorSet*> _vectorSets;

  std::vector<std::string> _errors;

  GLState* _glState;
  void updateGLState(const Camera* camera);

public:

  VectorStreamingRenderer(MarksRenderer* marksRenderer,
                          MeshRenderer*  meshRenderer);

  ~VectorStreamingRenderer();

  MarksRenderer* getMarksRenderer() const {
    return _marksRenderer;
  }

  MeshRenderer* getMeshRenderer() const {
    return _meshRenderer;
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
                    const Format               format,
                    const Angle&               minSectorSize,
                    const double               minProjectedArea);
  
  void removeAllVectorSets();
  
  RenderState getRenderState(const G3MRenderContext* rc);
  

};

#endif
