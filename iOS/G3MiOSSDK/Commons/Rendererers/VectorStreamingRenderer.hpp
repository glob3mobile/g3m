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

#include <vector>
#include <string>

class IThreadUtils;
class IByteBuffer;
class Sector;
class Geodetic2D;
class JSONArray;
class JSONObject;


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
    static Node*       parseNode(const JSONObject* json);

  };


  class Node {
  private:
    const std::string              _id;
    const Sector*                  _sector;
    const int                      _featuresCount;
    const Geodetic2D*              _averagePosition;
#ifdef C_CODE
    const std::vector<std::string> _children;
#endif
#ifdef JAVA_CODE
    private final java.util.ArrayList<String> _children;
#endif

  public:
    Node(const std::string&              id,
         const Sector*                   sector,
         const int                       featuresCount,
         const Geodetic2D*               averagePosition,
         const std::vector<std::string>& children) :
    _id(id),
    _sector(sector),
    _featuresCount(featuresCount),
    _averagePosition(averagePosition),
    _children(children)
    {

    }

    ~Node();

    long long render(const G3MRenderContext* rc,
                     const long long cameraTS,
                     GLState* glState);

  };


  class MetadataParserAsyncTask : public GAsyncTask {
  private:
    VectorSet*   _vectorSet;
    IByteBuffer* _buffer;
    const bool   _verbose;

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
                            IByteBuffer* buffer,
                            bool verbose) :
    _vectorSet(vectorSet),
    _buffer(buffer),
    _verbose(verbose),
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


  class VectorSet {
  private:
    const URL          _serverURL;
    const std::string  _name;
    const long long    _downloadPriority;
#ifdef C_CODE
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;
    const bool         _verbose;

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

    VectorSet(const URL& serverURL,
              const std::string& name,
              long long downloadPriority,
              const TimeInterval& timeToCache,
              bool readExpired,
              bool verbose) :
    _serverURL(serverURL),
    _name(name),
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

    const std::string getName() const {
      return _name;
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
                const long long cameraTS,
                GLState* glState);

  };


private:
  size_t                  _vectorSetsSize;
  std::vector<VectorSet*> _vectorSets;

  std::vector<std::string> _errors;

public:

  VectorStreamingRenderer() :
  _vectorSetsSize(0)
  {
  }

  ~VectorStreamingRenderer();

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void onChangedContext();

  void addVectorSet(const URL& serverURL,
                    const std::string& cloudName,
                    long long downloadPriority,
                    const TimeInterval& timeToCache,
                    bool readExpired,
                    bool verbose);

  void removeAllVectorSets();

  RenderState getRenderState(const G3MRenderContext* rc);
  
};

#endif
