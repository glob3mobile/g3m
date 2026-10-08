//
//  Mark.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 06/06/12.
//

#ifndef G3M_Mark
#define G3M_Mark

#include <string>
#include <vector>

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
class MarkOutfit;
class MarkOutfitImage;

class MarkUserData {
public:
  virtual ~MarkUserData() {
  }
};


class Mark : public SurfaceElevationListener {
private:

  std::vector<MarkOutfit*>      _outfits;
  std::vector<MarkOutfitImage*> _outfitImages; // one per outfit: the image and texture of the outfits not on screen
  size_t                        _outfitIndex;

  double _priority; // NAND: none, the renderer's order decides
  bool   _declutterHidden;

  // the outfit the renderer wants on screen (-1: none); the one on screen shrinks away before it grows in
  int       _declutterTarget;
  float     _transitionScale;
  long long _lastTransitionMS;

  // how high the camera is seen from the mark, above its horizon (NAND: unknown); the mark shrinks as it sinks
  double _grazingAngle;
  float  _horizonScale;
  void updateHorizonScale(double radiansPerPixel);

  void applyOutfitAnchor(const MarkOutfitImage* outfitImage);

  bool _hasHint; // the last outfit is a hint: drawn when nothing else fits, not touchable

  // the anchor the app set: outfits without their own anchor use it
  float _appAnchorU;
  float _appAnchorV;
  void changeAnchor(float anchorU, float anchorV);

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
  
  
  /** outfits: largest first, at least one; the mark keeps a copy of the vector and owns the outfits. MarkBuilder fills them */
  Mark(const std::vector<MarkOutfit*>& outfits,
       const Geodetic3D&               position,
       AltitudeMode                    altitudeMode,
       double                          minDistanceToCamera,
       double                          maxDistanceToCamera,
       MarkUserData*                   userData,
       bool                            autoDeleteUserData,
       MarkTouchListener*              listener,
       bool                            autoDeleteListener,
       bool                            zoomInAppears);

  ~Mark();

  bool isInitialized() const {
    return _initialized;
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

  void onImageCreated(size_t outfitIndex,
                      const IImage* image,
                      const std::string& imageName);

  void onImageCreationError(size_t outfitIndex,
                            const std::string& error);

  size_t getOutfitsCount() const {
    return _outfits.size();
  }

  size_t getOutfitIndex() const {
    return _outfitIndex;
  }

  /** the outfit drawn from now on; 0 is the largest */
  void setOutfit(size_t outfitIndex);

  /** the smallest outfit, after the others: a sign that there is more to see when zooming in; the mark owns it */
  void addHint(MarkOutfit* hint);

  bool hasHint() const {
    return _hasHint;
  }

  bool isShowingHint() const {
    return _hasHint && (_outfitIndex == (_outfits.size() - 1));
  }

  /** starts creating the images of the outfits that do not have one yet */
  void createPendingOutfitImages(const G3MContext* context);

  /** an outfit's size on screen (with the app's scale), or zero while its image does not exist */
  Vector2F getOutfitScreenSize(size_t outfitIndex) const;

  /** an outfit's anchor in UV: its own, or the mark's when the outfit has none */
  Vector2F getOutfitAnchor(size_t outfitIndex) const;

  /** true when the camera can see the position (distance limits and horizon); keeps the mark-camera vector */
  bool isVisibleFrom(const Planet* planet,
                     const MutableVector3D& cameraPosition,
                     double cameraHeight);

  bool isDeclutterHidden() const {
    return _declutterHidden;
  }

  /** the outfit a decluttering renderer chose, -1 when none fits; the mark gets there through stepDeclutterTransition */
  void setDeclutterTarget(int outfitIndex) {
    _declutterTarget = outfitIndex;
  }

  int getDeclutterTarget() const {
    return _declutterTarget;
  }

  /** shrinks the outfit on screen while it is not the target, then grows the target in; reversible halfway */
  void stepDeclutterTransition(long long nowMS,
                               long long growMS,
                               long long shrinkMS);

  /** back to the first outfit, on screen and at full size, with no transition */
  void resetDeclutter();

  /** the order among the marks that compete for space: the higher, the earlier */
  void setPriority(double priority) {
    _priority = priority;
  }

  bool hasPriority() const;

  double getPriority() const {
    return _priority;
  }

  float getTextureWidth() const {
    return _textureWidth;
  }

  float getTextureHeight() const {
    return _textureHeight;
  }

  /** the size drawn on screen: the texture size times the app's and the effects' scales */
  float getScreenWidth() const {
    return _textureWidth * _textureWidthScale * _effectScale * _transitionScale * _horizonScale;
  }

  float getScreenHeight() const {
    return _textureHeight * _textureHeightScale * _effectScale * _transitionScale * _horizonScale;
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

  /** horizonBandRadiansPerPixel: the camera's angle per screen pixel, so the mark shrinks while it sinks its own apparent height behind the horizon; 0: no band */
  void render(const G3MRenderContext* rc,
              MarksRenderer* renderer,
              const MutableVector3D& cameraPosition,
              double cameraHeight,
              const GLState* parentGLState,
              const Planet* planet,
              GL* gl,
              IFloatBuffer* billboardTexCoords,
              double horizonBandRadiansPerPixel);

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
