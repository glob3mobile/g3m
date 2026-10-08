//
//  Mark.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 06/06/12.
//

#ifndef G3M_Mark
#define G3M_Mark

#include <string>

#include "Geodetic3D.hpp"
#include "G3MContext.hpp"

#include "Vector3D.hpp"
#include "URL.hpp"
#include "Vector2I.hpp"
#include "Color.hpp"
#include "GLState.hpp"
#include "SurfaceElevationProvider.hpp"
#include "MutableVector3D.hpp"
#include "GTask.hpp"
#include "PeriodicalTask.hpp"
#include "AltitudeMode.hpp"
#include "Vector2F.hpp"

class IImage;
class IFloatBuffer;
class IGLTextureID;
class MarkTouchListener;
class GLGlobalState;
class GPUProgramState;
class TextureIDReference;
class EffectTarget;
class IImageFactory;
class MarksRenderer;
class MarkImageFactoryListener;
class TouchEvent;
class ModelTransformGLFeature;

class MarkUserData {
public:
  virtual ~MarkUserData() {
  }
};


class Mark : public SurfaceElevationListener {
private:

  IImageFactory* _imageFactory;
  MarkImageFactoryListener* _imageFactoryListener;

  /**
   * The text the mark displays.
   * Useless if the mark does not have label.
   */
  const std::string _label;
  /**
   * Flag to know if the label will be located under the icon (if TRUE) or on its right (if FALSE).
   * Useless if the mark does not have label or icon.
   * Default value: TRUE
   */
  const bool        _labelBottom;
  /**
   * The font size of the text.
   * Useless if the mark does not have label.
   * Default value: 20
   */
  const float       _labelFontSize;


  /**
   * The color of the text.
   * Useless if the mark does not have label.
   * Default value: white
   */
#ifdef C_CODE
  const Color*      _labelFontColor;
#endif
#ifdef JAVA_CODE
  private Color     _labelFontColor;
#endif

  /**
   * The color of the text shadow.
   * Useless if the mark does not have label.
   * Default value: black
   */
#ifdef C_CODE
  const Color*      _labelShadowColor;
#endif
#ifdef JAVA_CODE
  private Color     _labelShadowColor;
#endif

  /**
   * The number of pixels between the icon and the text.
   * Useless if the mark does not have label or icon.
   * Default value: 2
   */
  const int         _labelGapSize;
  /**
   * The URL to get the image file.
   * Useless if the mark does not have icon.
   */
  const URL         _iconURL;
  /**
   * The point where the mark will be geo-located.
   */
  Geodetic3D*  _position;
  /**
   * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
   * Default value: 4.5e+06
   */
  double      _minDistanceToCamera;

  double      _maxDistanceToCamera;

  /**
   * The extra data to be stored by the mark.
   * Usefull to store data such us name, URL...
   */
  MarkUserData*     _userData;
  /**
   * Flag to know if the mark is the owner of _userData and thus it must delete it on destruction.
   * Default value: TRUE
   */
  const bool        _autoDeleteUserData;
  /**
   * Interface for listening to the touch event.
   */
  MarkTouchListener* _listener;
  /**
   * Flag to know if the mark is the owner of _listener and thus it must delete it on destruction.
   * Default value: FALSE
   */
  const bool        _autoDeleteListener;

  std::string _token;

#ifdef C_CODE
  const TextureIDReference* _textureID;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureID;
#endif

  Vector3D* _cartesianPosition;

  bool           _textureSolved;
#ifdef C_CODE
  const IImage*  _textureImage;
#endif
#ifdef JAVA_CODE
  private IImage _textureImage;
#endif
  float          _textureWidth;
  float          _textureHeight;
  float          _textureWidthScale;
  float          _textureHeightScale;
  bool           _textureSizeSetExternally;
  float          _effectScale;   // owned by the zoom effects; the app owns _texture*Scale
  std::string    _imageID;
  
  float _translationTCX, _translationTCY;
  float _scalingTCX, _scalingTCY;

  bool    _renderedMark;


  GLState* _glState;
  void createGLState(const Planet* planet,
                     IFloatBuffer* billboardTexCoords);

  ModelTransformGLFeature* _modelTransformGLF;
  bool                     _glPositionOutdated;
  void updateGLPosition(const Planet* planet);

  SurfaceElevationProvider* _surfaceElevationProvider;
  double _currentSurfaceElevation;
  AltitudeMode _altitudeMode;

  Vector3D* _normalAtMarkPosition;
  
  TextureGLFeature* _textureGLF;
  
  void clearGLState();

  void updateBillboardSize();

  MutableVector3D _markCameraVector;

  bool isRenderableByDistance() const;

  bool isOccludedByHorizon(const Planet* planet,
                           const MutableVector3D& cameraPosition,
                           double cameraHeight,
                           const Vector3D* markPosition);

  void ensureTexture(const G3MRenderContext* rc);

  void ensureGLState(const Planet* planet,
                     IFloatBuffer* billboardTexCoords,
                     const GLState* parentGLState);

  void startPendingEffects(const G3MRenderContext* rc,
                           MarksRenderer* renderer);

  void draw(const G3MRenderContext* rc);

  void onTextureResolved(const IImage* image);
  
  float _anchorU;
  float _anchorV;
  BillboardGLFeature* _billboardGLF;

  bool _initialized;

  bool _zoomInAppears;
  EffectsScheduler* _effectsScheduler;
  bool _firstRender;

  bool _zoomOutDisappears;
  bool _deleteMarkOnDisappears;
  bool _zoomOutDisappearsStarted;

  EffectTarget* _effectTarget;
  EffectTarget* getEffectTarget();

  
public:
  
  
  /**
   * Creates a mark with icon and label
   */
  Mark(const std::string& label,
       const URL&         iconURL,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       const bool         labelBottom=true,
       const float        labelFontSize=20,
       const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
       const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
       const int          labelGapSize=2,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark just with label, without icon
   */
  Mark(const std::string& label,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       const float        labelFontSize=20,
       const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
       const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark just with icon, without label
   */
  Mark(const URL&         iconURL,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark whith a given pre-renderer IImage
   */
  Mark(const IImage*      image,
       const std::string& imageID,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark whith a IImageFactory, in future versions it'll be the only constructor
   */
  Mark(IImageFactory*     imageFactory,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  ~Mark();

  bool isInitialized() const {
    return _initialized;
  }

  const std::string getLabel() const {
    return _label;
  }

  const Geodetic3D getPosition() const {
    return *_position;
  }

  void initialize(const G3MContext* context,
                  long long downloadPriority);

  bool isReady() const;

  bool isRendered() const {
    return _renderedMark;
  }

  void onTextureDownloadError();

  void onTextureDownload(const IImage* image);

  void onImageCreated(const IImage* image,
                      const std::string& imageName);

  void onImageCreationError(const std::string& error);

  float getTextureWidth() const {
    return _textureWidth;
  }

  float getTextureHeight() const {
    return _textureHeight;
  }

  /** the size drawn on screen: the texture size times the app's and the effects' scales */
  float getScreenWidth() const {
    return _textureWidth * _textureWidthScale * _effectScale;
  }

  float getScreenHeight() const {
    return _textureHeight * _textureHeightScale * _effectScale;
  }

  Vector2F getTextureExtent() const {
    return Vector2F(_textureWidth, _textureHeight);
  }

  const MarkUserData* getUserData() const {
    return _userData;
  }

  void setUserData(MarkUserData* userData) {
    if (_autoDeleteUserData) {
      delete _userData;
    }
    _userData = userData;
  }

  bool touched(const TouchEvent* touchEvent);

  void setMinDistanceToCamera(double minDistanceToCamera);
  double getMinDistanceToCamera();

  void setMaxDistanceToCamera(double maxDistanceToCamera);
  double getMaxDistanceToCamera();

  const Vector3D* getCartesianPosition(const Planet* planet);

  void render(const G3MRenderContext* rc,
              MarksRenderer* renderer,
              const MutableVector3D& cameraPosition,
              double cameraHeight,
              const GLState* parentGLState,
              const Planet* planet,
              GL* gl,
              IFloatBuffer* billboardTexCoords);

  void elevationChanged(const Geodetic2D& position,
                        double rawElevation,            //Without considering vertical exaggeration
                        double verticalExaggeration);

  void elevationChanged(const Sector& position,
                        const ElevationData* rawElevationData, //Without considering vertical exaggeration
                        double verticalExaggeration) {}

  void setPosition(const Geodetic3D& position);
  
  void setScreenSize(int width, int height);
  void setScreenSizeScale(float scaleWidth, float scaleHeight);

  /** for the zoom effects only; the app scales with setScreenSizeScale */
  void setEffectScale(float effectScale);

  void setTextureCoordinatesTransformation(const Vector2F& translation,
                                           const Vector2F& scaling);
  
  void setTextureCoordinatesTransformation(const float translationX,
                                           const float translationY,
                                           const float scalingX,
                                           const float scalingY);

  void setMarkAnchor(float anchorU, float anchorV);

  Vector2F getMarkAnchor() const;
  float getMarkAnchorU() const;
  float getMarkAnchorV() const;

  void setToken(const std::string& token) {
    _token = token;
  }

  const std::string getToken() const {
    return _token;
  }

  void setZoomInAppears(bool zoomInAppears) {
    _zoomInAppears = zoomInAppears;
  }

  bool getZoomInAppears() const {
    return _zoomInAppears;
  }


  void animatedRemove(bool deleteMark);

  // true once the zoom-out effect owns the mark and will delete it on its own
  bool isDisappearing() const {
    return _zoomOutDisappearsStarted;
  }

  void cancelEffects();

};

#endif
