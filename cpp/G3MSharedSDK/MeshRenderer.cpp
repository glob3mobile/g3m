//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

#include "MeshRenderer.hpp"

#include "Mesh.hpp"
#include "Camera.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "BSONParser.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"
#include "IShortBuffer.hpp"
#include "IndexedMesh.hpp"
#include "IFactory.hpp"
#include "GAsyncTask.hpp"
#include "G3MRenderContext.hpp"
#include "BoundingVolume.hpp"
#include "Geodetic3D.hpp"
#include "GLState.hpp"
#include "MeshFilter.hpp"
#include "ILogger.hpp"


MeshRenderer::MeshRenderer(bool visibilityCulling):
_visibilityCulling(visibilityCulling),
_glState(new GLState())
{
  _context = NULL;
}

void MeshRenderer::clearMeshes() {
  const size_t meshesCount = _meshes.size();
  for (size_t i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }
  _meshes.clear();
}

MeshRenderer::~MeshRenderer() {
  const size_t meshesCount = _meshes.size();
  for (size_t i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MeshRenderer::updateGLState(const Camera* camera) {
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }
}

void MeshRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const size_t meshesCount = _meshes.size();
  if (meshesCount > 0) {
    const Camera* camera =  rc->getCurrentCamera();

    updateGLState(camera);
    _glState->setParent(glState);

    if (_visibilityCulling) {
      const Frustum* frustum = camera->getFrustumInModelCoordinates();

      for (size_t i = 0; i < meshesCount; i++) {
        Mesh* mesh = _meshes[i];
        const BoundingVolume* boundingVolume = mesh->getBoundingVolume();
        if ( (boundingVolume == NULL) || boundingVolume->touchesFrustum(frustum) ) {
          mesh->render(rc, _glState);
        }
      }
    }
    else {
      for (size_t i = 0; i < meshesCount; i++) {
        Mesh* mesh = _meshes[i];
        mesh->render(rc, _glState);
      }
    }
  }
}

void MeshRenderer::onChangedContext() {
  if (_context != NULL) {
    drainLoadQueue();
  }
}

void MeshRenderer::onLostContext() {
  if (_context == NULL) {
    cleanLoadQueue();
  }
}

void MeshRenderer::drainLoadQueue() {
  const size_t loadQueueSize = _loadQueue.size();
  for (size_t i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    requestMeshBuffer(item->_url,
                      item->_priority,
                      item->_timeToCache,
                      item->_readExpired,
                      item->_pointSize,
                      item->_deltaHeight,
                      item->_color,
                      item->_listener,
                      item->_deleteListener,
                      item->_isBSON,
                      item->_meshType);

    delete item;
  }

  _loadQueue.clear();
}

void MeshRenderer::cleanLoadQueue() {
  const size_t loadQueueSize = _loadQueue.size();
  for (size_t i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    delete item;
  }

  _loadQueue.clear();
}


void MeshRenderer::loadJSONPointCloud(const URL&          url,
                                      long long           priority,
                                      const TimeInterval& timeToCache,
                                      bool                readExpired,
                                      float               pointSize,
                                      double              deltaHeight,
                                      MeshLoadListener*   listener,
                                      bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           pointSize,
                                           deltaHeight,
                                           NULL, // color
                                           listener,
                                           deleteListener,
                                           false /* isBson */,
                                           POINT_CLOUD));
  }
  else {
    requestMeshBuffer(url,
                      priority,
                      timeToCache,
                      readExpired,
                      pointSize,
                      deltaHeight,
                      NULL, // color
                      listener,
                      deleteListener,
                      false, /* isBson */
                      POINT_CLOUD);
  }
}

void MeshRenderer::loadBSONPointCloud(const URL&          url,
                                      long long           priority,
                                      const TimeInterval& timeToCache,
                                      bool                readExpired,
                                      float               pointSize,
                                      double              deltaHeight,
                                      MeshLoadListener*   listener,
                                      bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           pointSize,
                                           deltaHeight,
                                           NULL, // color
                                           listener,
                                           deleteListener,
                                           true /* isBson */,
                                           POINT_CLOUD));
  }
  else {
    requestMeshBuffer(url,
                      priority,
                      timeToCache,
                      readExpired,
                      pointSize,
                      deltaHeight,
                      NULL, // color
                      listener,
                      deleteListener,
                      true, /* isBson */
                      POINT_CLOUD);
  }
}

void MeshRenderer::loadJSONMesh(const URL&          url,
                                const Color*        color,
                                long long           priority,
                                const TimeInterval& timeToCache,
                                bool                readExpired,
                                MeshLoadListener*   listener,
                                bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           1, // pointSize
                                           0, // deltaHeight
                                           color,
                                           listener,
                                           deleteListener,
                                           false /* isBson */,
                                           MESH));
  }
  else {
    requestMeshBuffer(url,
                      priority,
                      timeToCache,
                      readExpired,
                      1, // pointSize
                      0, // deltaHeight
                      color,
                      listener,
                      deleteListener,
                      false, /* isBson */
                      MESH);
  }
}

void MeshRenderer::loadBSONMesh(const URL&          url,
                                const Color*        color,
                                long long           priority,
                                const TimeInterval& timeToCache,
                                bool                readExpired,
                                MeshLoadListener*   listener,
                                bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           1, // pointSize
                                           0, // deltaHeight
                                           color,
                                           listener,
                                           deleteListener,
                                           true /* isBson */,
                                           MESH));
  }
  else {
    requestMeshBuffer(url,
                      priority,
                      timeToCache,
                      readExpired,
                      1, // pointSize
                      0, // deltaHeight
                      color,
                      listener,
                      deleteListener,
                      true, /* isBson */
                      MESH);
  }
}


class MeshRenderer_MeshParserAsyncTask : public GAsyncTask {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  MeshRenderer*     _meshRenderer;
  const URL         _url;
  IByteBuffer*      _buffer;
  const float       _pointSize;
  const double      _deltaHeight;
#ifdef C_CODE
  const Color*      _color;
#endif
#ifdef JAVA_CODE
  private Color     _color;
#endif
  MeshLoadListener* _listener;
  const bool        _deleteListener;
  const bool        _isBSON;
  const MeshType    _meshType;

  Mesh* _mesh;

  float normalize(float value,
                  float max,
                  float min) {
    return (value - min) / (max - min);
  }

  Color interpolateColor(const Color& from,
                         const Color& middle,
                         const Color& to,
                         float d) {
    if (d <= 0) {
      return from;
    }
    if (d >= 1) {
      return to;
    }
    if (d <= 0.5) {
      return from.mixedWith(middle, d * 2);
    }
    return middle.mixedWith(to, (d - 0.5f) * 2);
  }

  void parsePointCloudMesh(const JSONBaseObject* jsonBaseObject) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject == NULL) {
      ILogger::instance()->logError("Invalid format for PointCloud");
    }
    else {
      const int size = (int) jsonObject->getAsNumber("size", -1);
      if (size <= 0) {
        ILogger::instance()->logError("Invalid size for PointCloud");
      }
      else {
        Geodetic3D* averagePoint = NULL;

        const JSONArray* jsonAveragePoint = jsonObject->getAsArray("averagePoint");
        if ((jsonAveragePoint != NULL) &&
            (jsonAveragePoint->size() == 3)) {
          const double lonInDegrees = jsonAveragePoint->getAsNumber(0, 0);
          const double latInDegrees = jsonAveragePoint->getAsNumber(1, 0);
          const double height       = jsonAveragePoint->getAsNumber(2, 0);

          averagePoint = new Geodetic3D(Angle::fromDegrees(latInDegrees),
                                        Angle::fromDegrees(lonInDegrees),
                                        height + _deltaHeight);
        }
        else {
          ILogger::instance()->logError("Invalid averagePoint for PointCloud");
        }

        const JSONArray* jsonPoints = jsonObject->getAsArray("points");
        if ((jsonPoints == NULL) ||
            (size*3 != jsonPoints->size())) {
          ILogger::instance()->logError("Invalid points for PointCloud");
        }
        else {
          FloatBufferBuilderFromGeodetic* verticesBuilder = (averagePoint == NULL)
          /*                 */ ? FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_context->getPlanet())
          /*                 */ : FloatBufferBuilderFromGeodetic::builderWithGivenCenter(_context->getPlanet(), *averagePoint);

          double minHeight = jsonPoints->getAsNumber(2, 0);
          double maxHeight = minHeight;

          for (int i = 0; i < size*3; i += 3) {
            const double lonInDegrees = jsonPoints->getAsNumber(i    , 0);
            const double latInDegrees = jsonPoints->getAsNumber(i + 1, 0);
            const double height       = jsonPoints->getAsNumber(i + 2, 0);

            verticesBuilder->add(Angle::fromDegrees(latInDegrees),
                                 Angle::fromDegrees(lonInDegrees),
                                 height + _deltaHeight);

            if (height < minHeight) {
              minHeight = height;
            }
            if (height > maxHeight) {
              maxHeight = height;
            }
          }

          IFloatBuffer* colors = NULL;
          const JSONArray* jsonColors = jsonObject->getAsArray("colors");
          if (jsonColors == NULL) {
            const Color fromColor   = Color::RED;
            const Color middleColor = Color::GREEN;
            const Color toColor     = Color::BLUE;
            FloatBufferBuilderFromColor colorsBuilder;

            for (int i = 0; i < size*3; i += 3) {
              const double height = jsonPoints->getAsNumber(i + 2, 0);

              const Color interpolatedColor = interpolateColor(fromColor, middleColor, toColor,
                                                               normalize((float) height, (float) minHeight, (float) maxHeight));
              colorsBuilder.add(interpolatedColor);
            }

            colors = colorsBuilder.create();
          }
          else {
            FloatBufferBuilderFromColor colorsBuilder;

            const size_t colorsSize = jsonColors->size();
            for (size_t i = 0; i < colorsSize; i += 3) {
              const int red   = (int) jsonColors->getAsNumber(i    , 0);
              const int green = (int) jsonColors->getAsNumber(i + 1, 0);
              const int blue  = (int) jsonColors->getAsNumber(i + 2, 0);

              colorsBuilder.addBase255(red, green, blue, 1);
            }

            colors = colorsBuilder.create();
          }

          _mesh = new DirectMesh(GLPrimitive::points(),
                                 true,
                                 verticesBuilder->getCenter(),
                                 verticesBuilder->create(),
                                 1, // lineWidth
                                 _pointSize,
                                 NULL, // flatColor,
                                 colors,
                                 true);

          delete verticesBuilder;
        }

        delete averagePoint;
      }
    }
  }

  void parseMesh(const JSONBaseObject* jsonBaseObject) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject == NULL) {
      ILogger::instance()->logError("Invalid format for 'base object' of \"%s\"", _url._path.c_str());
    }
    else {
      const JSONArray* jsonCoordinates = jsonObject->getAsArray("coordinates");
      if (jsonCoordinates == NULL) {
        ILogger::instance()->logError("Invalid format for 'coordinates' of \"%s\"", _url._path.c_str());
        ILogger::instance()->logInfo("\"%s\"", jsonObject->description().c_str());
      }
      else {
        FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_context->getPlanet());

        const size_t coordinatesSize = jsonCoordinates->size();
        for (size_t i = 0; i < coordinatesSize; i += 3) {
          const double latInDegrees = jsonCoordinates->getAsNumber(i    , 0);
          const double lonInDegrees = jsonCoordinates->getAsNumber(i + 1, 0);
          const double height       = jsonCoordinates->getAsNumber(i + 2, 0);

          vertices->add(Angle::fromDegrees(latInDegrees),
                        Angle::fromDegrees(lonInDegrees),
                        height);
        }

        const JSONArray* jsonNormals = jsonObject->getAsArray("normals");
        const size_t normalsSize = jsonNormals->size();
        IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(normalsSize);
        for (size_t i = 0; i < normalsSize; i++) {
          normals->put(i, (float) jsonNormals->getAsNumber(i, 0) );
        }

        const JSONArray* jsonIndices = jsonObject->getAsArray("indices");
        const size_t indicesSize = jsonIndices->size();
        IShortBuffer* indices = IFactory::instance()->createShortBuffer(indicesSize);
        for (size_t i = 0; i < indicesSize; i++) {
          indices->put(i, (short) jsonIndices->getAsNumber(i, 0) );
        }

        _mesh = new IndexedMesh(GLPrimitive::triangles(),
                                vertices->getCenter(),
                                vertices->create(),
                                true,
                                indices,
                                true,
                                1, // lineWidth
                                1, // pointSize
                                _color, // flatColor
                                NULL, // colors,
                                true, // depthTest,
                                normals
                                );

        delete vertices;

        _color = NULL;
      }
    }
  }

public:

  MeshRenderer_MeshParserAsyncTask(MeshRenderer*     meshRenderer,
                                   const URL&        url,
                                   IByteBuffer*      buffer,
                                   float             pointSize,
                                   double            deltaHeight,
                                   const Color*      color,
                                   MeshLoadListener* listener,
                                   bool              deleteListener,
                                   bool              isBSON,
                                   const MeshType    meshType,
                                   const G3MContext* context) :
  _meshRenderer(meshRenderer),
  _url(url),
  _buffer(buffer),
  _pointSize(pointSize),
  _deltaHeight(deltaHeight),
  _listener(listener),
  _deleteListener(deleteListener),
  _isBSON(isBSON),
  _meshType(meshType),
  _context(context),
  _mesh(NULL),
  _color(color)
  {
  }

  void runInBackground(const G3MContext* context) {
    const JSONBaseObject* jsonBaseObject = _isBSON
    /*                                         */ ? BSONParser::parse(_buffer)
    /*                                         */ : IJSONParser::instance()->parse(_buffer);

    if (jsonBaseObject != NULL) {
      switch (_meshType) {
        case POINT_CLOUD:
          parsePointCloudMesh(jsonBaseObject);
          break;
        case MESH:
          parseMesh(jsonBaseObject);
          break;
      }

      delete jsonBaseObject;
    }

    delete _buffer;
    _buffer = NULL;
  }

  ~MeshRenderer_MeshParserAsyncTask() {
    if (_deleteListener) {
      delete _listener;
    }
    delete _buffer;
    delete _color;
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onPostExecute(const G3MContext* context) {
    if (_mesh == NULL) {
      ILogger::instance()->logError("Error parsing Mesh from \"%s\"", _url._path.c_str());
    }
    else {
      if (_listener != NULL) {
        _listener->onBeforeAddMesh(_mesh);
      }

      ILogger::instance()->logInfo("Adding Mesh to _meshRenderer");
      _meshRenderer->addMesh(_mesh);

      if (_listener != NULL) {
        _listener->onAfterAddMesh(_mesh);
      }

      _mesh = NULL;
    }
  }

};

class MeshRenderer_MeshBufferDownloadListener : public IBufferDownloadListener {
private:
  MeshRenderer*       _meshRenderer;
  const float         _pointSize;
  const double        _deltaHeight;
#ifdef C_CODE
  const Color*        _color;
#endif
#ifdef JAVA_CODE
  private Color _color;
#endif
  MeshLoadListener*   _listener;
  bool                _deleteListener;
  const IThreadUtils* _threadUtils;
  bool                _isBSON;
  const MeshType      _meshType;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

public:

  MeshRenderer_MeshBufferDownloadListener(MeshRenderer*       meshRenderer,
                                          float               pointSize,
                                          double              deltaHeight,
                                          const Color*        color,
                                          MeshLoadListener*   listener,
                                          bool                deleteListener,
                                          const IThreadUtils* threadUtils,
                                          bool                isBSON,
                                          const MeshType      meshType,
                                          const G3MContext*   context) :
  _meshRenderer(meshRenderer),
  _pointSize(pointSize),
  _deltaHeight(deltaHeight),
  _color(color),
  _listener(listener),
  _deleteListener(deleteListener),
  _threadUtils(threadUtils),
  _isBSON(isBSON),
  _meshType(meshType),
  _context(context)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    ILogger::instance()->logInfo("Downloaded Mesh buffer from \"%s\" (%db)",
                                 url._path.c_str(),
                                 buffer->size());

    _threadUtils->invokeAsyncTask(new MeshRenderer_MeshParserAsyncTask(_meshRenderer,
                                                                       url,
                                                                       buffer,
                                                                       _pointSize,
                                                                       _deltaHeight,
                                                                       _color,
                                                                       _listener,
                                                                       _deleteListener,
                                                                       _isBSON,
                                                                       _meshType,
                                                                       _context),
                                  true);
    _color = NULL;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());

    if (_listener != NULL) {
      _listener->onError(url);
    }

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("Canceled download of \"%s\"", url._path.c_str());

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

  ~MeshRenderer_MeshBufferDownloadListener() {
    delete _color;
    _color = NULL;
  }

};

void MeshRenderer::requestMeshBuffer(const URL&          url,
                                     long long           priority,
                                     const TimeInterval& timeToCache,
                                     bool                readExpired,
                                     float               pointSize,
                                     double              deltaHeight,
                                     const Color*        color,
                                     MeshLoadListener*   listener,
                                     bool                deleteListener,
                                     bool                isBSON,
                                     const MeshType      meshType) {
  IDownloader* downloader = _context->getDownloader();
  downloader->requestBuffer(url,
                            priority,
                            timeToCache,
                            readExpired,
                            new MeshRenderer_MeshBufferDownloadListener(this,
                                                                        pointSize,
                                                                        deltaHeight,
                                                                        color,
                                                                        listener,
                                                                        deleteListener,
                                                                        _context->getThreadUtils(),
                                                                        isBSON,
                                                                        meshType,
                                                                        _context),
                            true);

}

void MeshRenderer::enableAll() {
  const size_t meshesCount = _meshes.size();
  for (size_t i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    mesh->setEnable(true);
  }
}

void MeshRenderer::disableAll() {
  const size_t meshesCount = _meshes.size();
  for (size_t i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    mesh->setEnable(false);
  }
}

void MeshRenderer::addMesh(Mesh* mesh) {
  _meshes.push_back(mesh);
}

size_t MeshRenderer::removeAllMeshes(const MeshFilter& filter,
                                     bool deleteMeshes) {
  size_t removed = 0;
  const size_t meshesSize = _meshes.size();

  std::vector<Mesh*> surviving;
  for (size_t i = 0; i < meshesSize; i++) {
    Mesh* mesh = _meshes[i];
    if (filter.test(mesh)) {
      if (deleteMeshes) {
        delete mesh;
      }
      removed++;
    }
    else {
      surviving.push_back(mesh);
    }
  }

  if (removed > 0) {
    _meshes = surviving;
  }

  return removed;
}
