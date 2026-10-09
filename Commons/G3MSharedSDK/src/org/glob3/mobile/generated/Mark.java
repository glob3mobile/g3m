package org.glob3.mobile.generated;
public class Mark implements SurfaceElevationListener
{

  private java.util.ArrayList<MarkOutfit> _outfits = new java.util.ArrayList<MarkOutfit>();
  private java.util.ArrayList<MarkOutfitImage> _outfitImages = new java.util.ArrayList<MarkOutfitImage>(); // one per outfit: the image and texture of the outfits not on screen
  private int _outfitIndex;
  private java.util.ArrayList<Integer> _detailLevels = new java.util.ArrayList<Integer>(); // one per outfit: the outfit's own, or by its order

  private double _priority; // NAND: none, the renderer's order decides
  private boolean _declutterHidden;

  // removed from a decluttering renderer: gone for touches and room at once, it fades out only for the eye
  private boolean _leavingRenderer;
  private boolean _deleteWhenGone;

  // its renderer was disabled: the mark fades out and waits, still in the renderer
  private boolean _hiding;

  // the outfit the renderer plans (-1: none), and since when; it becomes the target once the plan holds for the delay
  private int _plannedTarget;
  private long _plannedSinceMS;

  // the outfit on its way to the screen (-1: none); the one on screen goes away while the target comes in
  private int _declutterTarget;
  private float _presence; // how far the outfit on screen has come in: 0 gone, 1 complete
  private float _transitionWidthScale;
  private float _transitionHeightScale;
  private float _transitionAlpha;
  private long _lastTransitionMS;

  // the outfit that was on screen before, drawn under the one coming in until it is gone (-1: none)
  private int _leavingOutfitIndex;
  private float _leavingPresence;
  private float _leavingWidthScale;
  private float _leavingHeightScale;
  private float _leavingAlpha;
  private GLState _leavingGLState;
  private BillboardGLFeature _leavingBillboardGLF;
  private ModelTransformGLFeature _leavingModelTransformGLF;


  // coming back to the outfit that is leaving resumes it from where it was
  private void startOutfitTransition(int outfitIndex)
  {
    float presence = 0F;
    if (_leavingOutfitIndex == (int) outfitIndex)
    {
      presence = _leavingPresence;
      releaseLeavingOutfit();
    }
  
    if (!_declutterHidden && (_presence > 0) && (_glState != null))
    {
      releaseLeavingOutfit();
      moveGLStateToLeaving();
    }
  
    setOutfit(outfitIndex);
    _presence = presence;
  }

  // the GL state on screen already holds the leaving outfit's texture, size and anchor
  private void moveGLStateToLeaving()
  {
    _leavingOutfitIndex = (int) _outfitIndex;
    _leavingPresence = _presence;
    _leavingGLState = _glState;
    _leavingBillboardGLF = _billboardGLF;
    _leavingModelTransformGLF = _modelTransformGLF;
  
    _glState = null;
    _billboardGLF = null;
    _modelTransformGLF = null;
    _textureGLF = null;
  }
  private void releaseLeavingOutfit()
  {
    if (_leavingGLState != null)
    {
      _leavingGLState._release();
      _leavingGLState = null;
    }
    _leavingBillboardGLF = null;
    _leavingModelTransformGLF = null;
    _leavingOutfitIndex = -1;
    _leavingPresence = 0F;
  }

  // each outfit comes in or goes away its own way: one may fold sideways while the other unfolds upwards
  private void applyTransitionMode(MarkTransitionMode rendererMode)
  {
    final MarkTransitionMode arrivingMode = _outfits.get(_outfitIndex).getTransitionMode(rendererMode);
    _transitionWidthScale = scalesWidth(arrivingMode) ? _presence : 1;
    _transitionHeightScale = scalesHeight(arrivingMode) ? _presence : 1;
    _transitionAlpha = fades(arrivingMode) ? _presence : 1;
  
    final MarkTransitionMode leavingMode = (_leavingOutfitIndex < 0) ? rendererMode : _outfits.get(_leavingOutfitIndex).getTransitionMode(rendererMode);
    _leavingWidthScale = scalesWidth(leavingMode) ? _leavingPresence : 1;
    _leavingHeightScale = scalesHeight(leavingMode) ? _leavingPresence : 1;
    _leavingAlpha = fades(leavingMode) ? _leavingPresence : 1;
  
    updateBillboard();
  }

  private static boolean scalesWidth(MarkTransitionMode mode)
  {
    return (mode == MarkTransitionMode.SCALE) || (mode == MarkTransitionMode.SCALE_AND_ALPHA) || (mode == MarkTransitionMode.WIDTH) || (mode == MarkTransitionMode.WIDTH_AND_ALPHA);
  }
  private static boolean scalesHeight(MarkTransitionMode mode)
  {
    return (mode == MarkTransitionMode.SCALE) || (mode == MarkTransitionMode.SCALE_AND_ALPHA) || (mode == MarkTransitionMode.HEIGHT) || (mode == MarkTransitionMode.HEIGHT_AND_ALPHA);
  }
  private static boolean fades(MarkTransitionMode mode)
  {
    return (mode == MarkTransitionMode.ALPHA) || (mode == MarkTransitionMode.SCALE_AND_ALPHA) || (mode == MarkTransitionMode.WIDTH_AND_ALPHA) || (mode == MarkTransitionMode.HEIGHT_AND_ALPHA);
  }
  private void drawLeavingOutfit(G3MRenderContext rc, GLState parentGLState)
  {
    if (_leavingGLState != null)
    {
      _leavingGLState.setParent(parentGLState);
      draw(rc, _leavingGLState);
    }
  }

  // how high the camera is seen from the mark, above its horizon (NAND: unknown); the mark shrinks as it sinks
  private double _grazingAngle;
  private float _horizonScale;

  // the band is the mark's own apparent height: a big mark starts shrinking earlier than a small one
  private void updateHorizonScale(double radiansPerPixel)
  {
    float horizonScale = 1F;
    if ((radiansPerPixel > 0) && !(_grazingAngle != _grazingAngle))
    {
      final double apparentAngle = _textureHeight * _textureHeightScale * radiansPerPixel;
      if ((apparentAngle > 0) && (_grazingAngle < apparentAngle))
      {
        horizonScale = (float)(_grazingAngle / apparentAngle);
      }
    }
    if (horizonScale != _horizonScale)
    {
      _horizonScale = horizonScale;
      updateBillboard();
    }
  }

  private void applyOutfitAnchor(MarkOutfitImage outfitImage)
  {
    if (outfitImage._hasAnchor)
    {
      changeAnchor(outfitImage._anchorU, outfitImage._anchorV);
    }
    else
    {
      changeAnchor(_appAnchorU, _appAnchorV);
    }
  }

  private boolean _hasHint; // the last outfit is a hint: drawn when nothing else fits, not touchable

  // the anchor the app set: outfits without their own anchor use it
  private float _appAnchorU;
  private float _appAnchorV;
  private void changeAnchor(float anchorU, float anchorV)
  {
    if (_billboardGLF != null)
    {
      _billboardGLF.changeAnchor(anchorU, anchorV);
    }
    _anchorU = anchorU;
    _anchorV = anchorV;
  }

  /**
   * The point where the mark will be geo-located.
   */
  private Geodetic3D _position;
  /**
   * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
   * Default value: 4.5e+06
   */
  private double _minDistanceToCamera;

  private double _maxDistanceToCamera;

  /**
   * The extra data to be stored by the mark.
   * Usefull to store data such us name, URL...
   */
  private MarkUserData _userData;
  /**
   * Flag to know if the mark is the owner of _userData and thus it must delete it on destruction.
   * Default value: TRUE
   */
  private final boolean _autoDeleteUserData;
  /**
   * Interface for listening to the touch event.
   */
  private MarkTouchListener _listener;
  /**
   * Flag to know if the mark is the owner of _listener and thus it must delete it on destruction.
   * Default value: FALSE
   */
  private final boolean _autoDeleteListener;

  private String _token;

  private TextureIDReference _textureID;

  private Vector3D _cartesianPosition;

  private boolean _textureSolved;
  private IImage _textureImage;
  private float _textureWidth;
  private float _textureHeight;
  private float _textureWidthScale;
  private float _textureHeightScale;
  private boolean _textureSizeSetExternally;
  private float _effectScale; // owned by the zoom effects; the app owns _texture*Scale
  private String _imageID;

  private float _translationTCX;
  private float _translationTCY;
  private float _scalingTCX;
  private float _scalingTCY;

  private boolean _renderedMark;


  private GLState _glState;
  private void createGLState(Planet planet, IFloatBuffer billboardTexCoords)
  {
    _glState = new GLState();
  
    _billboardGLF = new BillboardGLFeature(getScreenWidth(), getScreenHeight(), _anchorU, _anchorV, (_textureID == null) || _textureID.isPremultiplied());
    _billboardGLF.changeAlpha(_transitionAlpha);
  
    _glState.addGLFeature(_billboardGLF, false);
  
  
    final Vector3D position = getCartesianPosition(planet);
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(position);
  
    _modelTransformGLF = new ModelTransformGLFeature(translation.asMatrix44D());
    _glState.addGLFeature(_modelTransformGLF, false);
    _glPositionOutdated = false;
  
  
    if (_textureID != null)
    {
  
      _textureGLF = new TextureGLFeature(_textureID.getID(), billboardTexCoords, 2, 0, false, 0, true, _textureID.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationTCX, _translationTCY, _scalingTCX, _scalingTCY, 0.0f, 0.0f, 0.0f);
  
      _glState.addGLFeature(_textureGLF, false);
    }
  }

  private ModelTransformGLFeature _modelTransformGLF;
  private boolean _glPositionOutdated;
  private void updateGLPosition(Planet planet)
  {
    final Vector3D position = getCartesianPosition(planet);
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(position);
  
    if (_modelTransformGLF != null)
    {
      _modelTransformGLF.setMatrix(translation.asMatrix44D());
    }
    if (_leavingModelTransformGLF != null)
    {
      _leavingModelTransformGLF.setMatrix(translation.asMatrix44D());
    }
    _glPositionOutdated = false;
  }

  private SurfaceElevationProvider _surfaceElevationProvider;
  private double _currentSurfaceElevation;
  private AltitudeMode _altitudeMode;

  private Vector3D _normalAtMarkPosition;

  private TextureGLFeature _textureGLF;

  private void clearGLState()
  {
    if (_glState != null)
    {
      _glState._release();
      _glState = null;
      // the features are owned by the GLState
      _modelTransformGLF = null;
      _billboardGLF = null;
      _textureGLF = null;
    }
  }

  private void updateBillboard()
  {
    final IMathUtils mu = IMathUtils.instance();
    if (_billboardGLF != null)
    {
      _billboardGLF.changeSize(mu.round(getScreenWidth()), mu.round(getScreenHeight()));
      _billboardGLF.changeAlpha(_transitionAlpha);
    }
    if (_leavingBillboardGLF != null)
    {
      final Vector2F leavingSize = getOutfitScreenSize(_leavingOutfitIndex);
      final float leavingFactor = _effectScale * _horizonScale;
      _leavingBillboardGLF.changeSize(mu.round(leavingSize._x * leavingFactor * _leavingWidthScale), mu.round(leavingSize._y * leavingFactor * _leavingHeightScale));
      _leavingBillboardGLF.changeAlpha(_leavingAlpha);
    }
  }

  private MutableVector3D _markCameraVector = new MutableVector3D();

  private boolean isRenderableByDistance()
  {
    final boolean hasMinDistanceToCamera = (_minDistanceToCamera > 0);
    final boolean hasMaxDistanceToCamera = (_maxDistanceToCamera > 0);
    if (!hasMinDistanceToCamera && !hasMaxDistanceToCamera)
    {
      return true;
    }
  
    final double squaredDistanceToCamera = _markCameraVector.squaredLength();
  
    if (hasMinDistanceToCamera && (squaredDistanceToCamera > (_minDistanceToCamera * _minDistanceToCamera)))
    {
      return false;
    }
  
    if (hasMaxDistanceToCamera && (squaredDistanceToCamera < (_maxDistanceToCamera * _maxDistanceToCamera)))
    {
      return false;
    }
  
    return true;
  }

  private boolean isOccludedByHorizon(Planet planet, MutableVector3D cameraPosition, double cameraHeight, Vector3D markPosition)
  {
    final double markHeight = (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND) ? (_position._height + _currentSurfaceElevation) : _position._height;
    if (markHeight > cameraHeight)
    {
      _grazingAngle = Double.NaN;
      final java.util.ArrayList<Double> dists = planet.intersectionsDistances(cameraPosition.x(), cameraPosition.y(), cameraPosition.z(), _markCameraVector.x(), _markCameraVector.y(), _markCameraVector.z());
      if (dists.size() > 0)
      {
        final double dist = dists.get(0);
        return (dist > 0.0 && dist < 1.0);
      }
      return false;
    }
  
    // if camera position is upper than mark we can compute horizon culling in a much simpler way
    if (_normalAtMarkPosition == null)
    {
      _normalAtMarkPosition = new Vector3D(planet.geodeticSurfaceNormal(markPosition));
    }
    final double angle = Vector3D.angleInRadiansBetween(_normalAtMarkPosition, _markCameraVector);
    _grazingAngle = angle - DefineConstants.HALF_PI;
    return (angle <= DefineConstants.HALF_PI);
  }

  private void ensureTexture(G3MRenderContext rc)
  {
    if ((_textureID == null) && (_textureImage != null))
    {
      _textureID = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _imageID, false, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge());
  
      _textureImage = null;
      _textureImage = null;
    }
  }

  private void ensureGLState(Planet planet, IFloatBuffer billboardTexCoords, GLState parentGLState)
  {
    if (_glState == null)
    {
      createGLState(planet, billboardTexCoords); // If GLState was disposed due to elevation change
    }
    _glState.setParent(parentGLState);
  }

  private void startPendingEffects(G3MRenderContext rc, MarksRenderer renderer)
  {
    if (_firstRender)
    {
      _firstRender = false;
      if (_zoomInAppears)
      {
        _effectsScheduler = rc.getEffectsScheduler();
        _effectsScheduler.startEffect(new MarkZoomInEffect(this), getEffectTarget());
      }
    }
  
    if (_zoomOutDisappears && !_zoomOutDisappearsStarted)
    {
      _zoomOutDisappearsStarted = true;
      if (_effectsScheduler != null)
      {
        _effectsScheduler.cancelAllEffectsFor(getEffectTarget());
      }
      else
      {
        _effectsScheduler = rc.getEffectsScheduler();
      }
      _zoomOutEffect = new MarkZoomOutAndRemoveEffect(this, renderer, _deleteMarkOnDisappears);
      _effectsScheduler.startEffect(_zoomOutEffect, getEffectTarget());
    }
  }

  private void draw(G3MRenderContext rc, GLState glState)
  {
    rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, glState, rc.getGPUProgramManager());
  }

  private void onTextureResolved(IImage image)
  {
    _textureSolved = true;
  
    _textureImage = image;
  
    if (!_textureSizeSetExternally)
    {
      _textureWidth = _textureImage.getWidth();
      _textureHeight = _textureImage.getHeight();
    }
  }

  private float _anchorU;
  private float _anchorV;
  private BillboardGLFeature _billboardGLF;

  private boolean _initialized;

  private boolean _zoomInAppears;
  private EffectsScheduler _effectsScheduler;
  private boolean _firstRender;

  private boolean _zoomOutDisappears;
  private boolean _deleteMarkOnDisappears;
  private boolean _zoomOutDisappearsStarted;
  private MarkZoomOutAndRemoveEffect _zoomOutEffect; // while it runs: whoever is deleted first unlinks the other

  private EffectTarget _effectTarget;
  private EffectTarget getEffectTarget()
  {
    if (_effectTarget == null)
    {
      _effectTarget = new MarkEffectTarget();
    }
    return _effectTarget;
  }




  /** outfits: largest first, at least one; the mark keeps a copy of the vector and owns the outfits. MarkBuilder fills them */
  public Mark(java.util.ArrayList<MarkOutfit> outfits, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, double maxDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener, boolean zoomInAppears)
  {
     _outfitIndex = 0;
     _priority = Double.NaN;
     _hasHint = false;
     _appAnchorU = 0.5F;
     _appAnchorV = 0.5F;
     _declutterHidden = false;
     _leavingRenderer = false;
     _deleteWhenGone = false;
     _hiding = false;
     _plannedTarget = 0;
     _plannedSinceMS = -1;
     _declutterTarget = 0;
     _presence = 1F;
     _transitionWidthScale = 1F;
     _transitionHeightScale = 1F;
     _transitionAlpha = 1F;
     _lastTransitionMS = -1;
     _leavingOutfitIndex = -1;
     _leavingPresence = 0F;
     _leavingWidthScale = 1F;
     _leavingHeightScale = 1F;
     _leavingAlpha = 1F;
     _leavingGLState = null;
     _leavingBillboardGLF = null;
     _leavingModelTransformGLF = null;
     _grazingAngle = Double.NaN;
     _horizonScale = 1F;
     _position = new Geodetic3D(position);
     _altitudeMode = altitudeMode;
     _textureID = null;
     _cartesianPosition = null;
     _textureSolved = false;
     _textureImage = null;
     _renderedMark = false;
     _textureWidth = 0F;
     _textureHeight = 0F;
     _userData = userData;
     _autoDeleteUserData = autoDeleteUserData;
     _minDistanceToCamera = minDistanceToCamera;
     _maxDistanceToCamera = maxDistanceToCamera;
     _listener = listener;
     _autoDeleteListener = autoDeleteListener;
     _imageID = "";
     _surfaceElevationProvider = null;
     _currentSurfaceElevation = 0.0;
     _glState = null;
     _modelTransformGLF = null;
     _glPositionOutdated = false;
     _normalAtMarkPosition = null;
     _textureSizeSetExternally = false;
     _translationTCX = 0F;
     _translationTCY = 0F;
     _scalingTCX = 1F;
     _scalingTCY = 1F;
     _anchorU = 0.5F;
     _anchorV = 0.5F;
     _billboardGLF = null;
     _textureGLF = null;
     _effectScale = 1F;
     _textureHeightScale = 1.0F;
     _textureWidthScale = 1.0F;
     _initialized = false;
     _zoomInAppears = zoomInAppears;
     _effectsScheduler = null;
     _firstRender = true;
     _effectTarget = null;
     _zoomOutDisappears = false;
     _deleteMarkOnDisappears = false;
     _zoomOutDisappearsStarted = false;
     _zoomOutEffect = null;
     _token = "";
    if (outfits.isEmpty())
    {
      throw new RuntimeException("Mark: at least one outfit is needed");
    }
  
    // element by element: in Java an assignment would share the caller's list
    for (int i = 0; i < outfits.size(); i++)
    {
      MarkOutfit outfit = outfits.get(i);
      _outfits.add(outfit);
  
      IImageFactory imageFactory = outfit.takeImageFactory();
      if (imageFactory.isMutable())
      {
        throw new RuntimeException("Mark: mutable image factories are not supported");
      }
      _outfitImages.add(new MarkOutfitImage(imageFactory, outfit.getAnchor()));
  
      // by order: the first outfit shows the most
      _detailLevels.add(outfit.getDetailLevel((int)(outfits.size() - i)));
    }
  }

  public void dispose()
  {
    // not cancelled: the effect may be the one deleting this mark, from inside the scheduler's loop
    if (_zoomOutEffect != null)
    {
      _zoomOutEffect.forgetMark();
      _zoomOutEffect = null;
    }
    else if (!_zoomOutDisappearsStarted)
    {
      cancelEffects();
    }
  
    for (int i = 0; i < _outfitImages.size(); i++)
    {
      if (_outfitImages.get(i) != null)
         _outfitImages.get(i).dispose();
    }
  
    if (_effectTarget != null)
       _effectTarget.dispose();
  
    for (int i = 0; i < _outfits.size(); i++)
    {
      if (_outfits.get(i) != null)
         _outfits.get(i).dispose();
    }
  
    if (_position != null)
       _position.dispose();
  
    if (_normalAtMarkPosition != null)
       _normalAtMarkPosition.dispose();
  
    if (_surfaceElevationProvider != null)
    {
      if (!_surfaceElevationProvider.removeListener(this))
      {
        ILogger.instance().logError("Couldn't remove mark as listener of Surface Elevation Provider.");
      }
    }
  
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
  
    if (_autoDeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
  
    _textureImage = null;
  
  
    if (_glState != null)
    {
      _glState._release();
    }
    releaseLeavingOutfit();
  
    if (_textureID != null)
    {
      _textureID.dispose();
      _textureID = null; //Releasing texture
    }
  }

  public final boolean isInitialized()
  {
    return _initialized;
  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final void initialize(G3MContext context, long downloadPriority)
  {
    _initialized = true;
    if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
    {
      _surfaceElevationProvider = context.getSurfaceElevationProvider();
      if (_surfaceElevationProvider != null)
      {
        _surfaceElevationProvider.addListener(_position._latitude, _position._longitude, this);
      }
    }
  
    createPendingOutfitImages(context);
  }

  public final boolean isReady()
  {
    return _textureSolved;
  }

  public final boolean isRendered()
  {
    return _renderedMark;
  }

  public final void onImageCreated(int outfitIndex, IImage image, String imageName)
  {
    MarkOutfitImage outfitImage = _outfitImages.get(outfitIndex);
    outfitImage._listener = null;
    outfitImage._solved = true;
    outfitImage._imageID = imageName;
    outfitImage._width = image.getWidth();
    outfitImage._height = image.getHeight();
    if (outfitImage._anchor != null)
    {
      final Vector2F anchorUV = outfitImage._anchor.getAnchor(image);
      outfitImage._hasAnchor = true;
      outfitImage._anchorU = anchorUV._x;
      outfitImage._anchorV = anchorUV._y;
    }
  
    if (outfitIndex == _outfitIndex)
    {
      _imageID = imageName;
      applyOutfitAnchor(outfitImage);
      onTextureResolved(image);
    }
    else
    {
      outfitImage._image = image;
    }
  }

  public final void onImageCreationError(int outfitIndex, String error)
  {
    MarkOutfitImage outfitImage = _outfitImages.get(outfitIndex);
    outfitImage._listener = null;
    outfitImage._solved = true;
  
    if (outfitIndex == _outfitIndex)
    {
      _textureSolved = true;
    }
  
    ILogger.instance().logError("Can't create image for Mark: \"%s\"", error);
  }

  public final int getOutfitsCount()
  {
    return _outfits.size();
  }

  /** outfits with the same level are alternatives; a higher one shows more */
  public final int getOutfitDetailLevel(int outfitIndex)
  {
    return _detailLevels.get(outfitIndex);
  }

  public final int getOutfitIndex()
  {
    return _outfitIndex;
  }

  /** the outfit drawn from now on; 0 is the largest */
  public final void setOutfit(int outfitIndex)
  {
    if (outfitIndex == _outfitIndex)
    {
      return;
    }
  
    // the outfit leaving the screen keeps its image or texture
    MarkOutfitImage leaving = _outfitImages.get(_outfitIndex);
    leaving._textureID = _textureID;
    leaving._image = _textureImage;
    leaving._imageID = _imageID;
  
    MarkOutfitImage arriving = _outfitImages.get(outfitIndex);
    _textureID = arriving._textureID;
    _textureImage = arriving._image;
    _imageID = arriving._imageID;
    _textureSolved = arriving._solved;
    arriving._textureID = null;
    arriving._image = null;
  
    if (!_textureSizeSetExternally)
    {
      _textureWidth = arriving._width;
      _textureHeight = arriving._height;
    }
    applyOutfitAnchor(arriving);
  
    _outfitIndex = outfitIndex;
  
    clearGLState();
  }

  /** the smallest outfit, after the others: a sign that there is more to see when zooming in; the mark owns it; only one */
  public final void addHint(MarkOutfit hint)
  {
    if (_hasHint)
    {
      throw new RuntimeException("Mark: the mark already has a hint");
    }
    int lowestLevel = _detailLevels.get(0);
    for (int i = 1; i < _detailLevels.size(); i++)
    {
      if (_detailLevels.get(i) < lowestLevel)
      {
        lowestLevel = _detailLevels.get(i);
      }
    }
    _outfits.add(hint);
    _outfitImages.add(new MarkOutfitImage(hint.takeImageFactory(), hint.getAnchor()));
    _detailLevels.add(lowestLevel - 1); // below every other outfit
    _hasHint = true;
  }

  public final boolean hasHint()
  {
    return _hasHint;
  }

  public final boolean isShowingHint()
  {
    return _hasHint && (_outfitIndex == (_outfits.size() - 1));
  }

  /** starts creating the images of the outfits that do not have one yet */

  // every outfit at once: choosing between them needs their sizes
  public final void createPendingOutfitImages(G3MContext context)
  {
    for (int i = 0; i < _outfitImages.size(); i++)
    {
      MarkOutfitImage outfitImage = _outfitImages.get(i);
      if (outfitImage._imageFactory != null)
      {
        outfitImage._listener = new MarkImageFactoryListener(outfitImage._imageFactory, this, i);
        IImageFactory imageFactory = outfitImage._imageFactory;
        outfitImage._imageFactory = null; // ownership moved to MarkImageFactoryListener
        imageFactory.create(context, outfitImage._listener, true);
      }
    }
  }

  /** an outfit's size on screen (with the app's scale), or zero while its image does not exist */
  public final Vector2F getOutfitScreenSize(int outfitIndex)
  {
    float width;
    float height;
    if (_textureSizeSetExternally || (outfitIndex == _outfitIndex))
    {
      width = _textureWidth;
      height = _textureHeight;
    }
    else
    {
      final MarkOutfitImage outfitImage = _outfitImages.get(outfitIndex);
      width = outfitImage._width;
      height = outfitImage._height;
    }
    return new Vector2F(width * _textureWidthScale, height * _textureHeightScale);
  }

  /** an outfit's anchor in UV: its own, or the mark's when the outfit has none */
  public final Vector2F getOutfitAnchor(int outfitIndex)
  {
    final MarkOutfitImage outfitImage = _outfitImages.get(outfitIndex);
    if (outfitImage._hasAnchor)
    {
      return new Vector2F(outfitImage._anchorU, outfitImage._anchorV);
    }
    return new Vector2F(_appAnchorU, _appAnchorV);
  }

  /** true when the camera can see the position (distance limits and horizon); keeps the mark-camera vector */
  public final boolean isVisibleFrom(Planet planet, MutableVector3D cameraPosition, double cameraHeight)
  {
    final Vector3D markPosition = getCartesianPosition(planet);
  
    _markCameraVector.set(markPosition._x - cameraPosition.x(), markPosition._y - cameraPosition.y(), markPosition._z - cameraPosition.z());
  
    return (isRenderableByDistance() && !isOccludedByHorizon(planet, cameraPosition, cameraHeight, markPosition));
  }

  public final boolean isDeclutterHidden()
  {
    return _declutterHidden;
  }

  /** the outfit a decluttering renderer plans, -1 when none fits; the mark goes there through stepDeclutterTransition once the plan holds */
  public final void setDeclutterTarget(int outfitIndex)
  {
    if (_leavingRenderer || _hiding)
    {
      return;
    }
    if (outfitIndex != _plannedTarget)
    {
      _plannedTarget = outfitIndex;
      _plannedSinceMS = -1;
    }
  }

  /** the outfit the renderer planned, maybe still waiting for the delay */
  public final int getDeclutterTarget()
  {
    return _plannedTarget;
  }

  /** a plan that holds for delayMS becomes the target; the outfit on screen goes away while the target comes in, both at once and in durationMS; reversible halfway */
  public final void stepDeclutterTransition(long nowMS, long delayMS, long durationMS, MarkTransitionMode mode)
  {
    // a plan that comes and goes within the delay never reaches the screen
    if (_plannedTarget != _declutterTarget)
    {
      if (_plannedSinceMS < 0)
      {
        _plannedSinceMS = nowMS;
      }
      if ((nowMS - _plannedSinceMS) >= delayMS)
      {
        _declutterTarget = _plannedTarget;
      }
    }
  
    final long elapsedMS = (_lastTransitionMS < 0) ? 0 : (nowMS - _lastTransitionMS);
    _lastTransitionMS = nowMS;
    final float step = (durationMS <= 0) ? 1 : ((float) elapsedMS / durationMS);
    final IMathUtils mu = IMathUtils.instance();
  
    if ((_declutterTarget >= 0) && ((int) _declutterTarget != _outfitIndex))
    {
      startOutfitTransition(_declutterTarget);
    }
  
    if (_declutterTarget >= 0)
    {
      _declutterHidden = false;
      _presence = mu.min(1.0f, _presence + step);
    }
    else if (!_declutterHidden)
    {
      _presence = mu.max(0.0f, _presence - step);
      _declutterHidden = (_presence <= 0);
    }
  
    if (_leavingOutfitIndex >= 0)
    {
      _leavingPresence = mu.max(0.0f, _leavingPresence - step);
      if (_leavingPresence <= 0)
      {
        releaseLeavingOutfit();
      }
    }
  
    applyTransitionMode(mode);
  }

  /** back to the first outfit, on screen and complete, with no transition */
  public final void resetDeclutter()
  {
    if (_hiding)
    {
      hideUntilDecluttered(); // its renderer is disabled: it stays off screen
      _hiding = true;
      return;
    }
    releaseLeavingOutfit();
    _plannedTarget = 0;
    _plannedSinceMS = -1;
    _declutterTarget = 0;
    _declutterHidden = false;
    _presence = 1F;
    _lastTransitionMS = -1;
    setOutfit(0);
    applyTransitionMode(MarkTransitionMode.SCALE_AND_ALPHA);
  }

  /** off screen until a decluttering renderer finds it room, so a new mark does not flash in before it is placed */
  public final void hideUntilDecluttered()
  {
    releaseLeavingOutfit();
    _plannedTarget = -1;
    _plannedSinceMS = -1;
    _declutterTarget = -1;
    _declutterHidden = true;
    _presence = 0F;
    _lastTransitionMS = -1;
    applyTransitionMode(MarkTransitionMode.SCALE_AND_ALPHA);
  }

  /** a decluttering renderer removes the mark with its transition: from now on it takes no room and no touches; deleteMark: once gone */
  public final void startLeavingRenderer(boolean deleteMark)
  {
    _leavingRenderer = true;
    _deleteWhenGone = deleteMark;
    _plannedTarget = -1;
    _plannedSinceMS = -1;
    _declutterTarget = -1; // no delay: it is gone already, the transition is for the eye
  }

  public final boolean isLeavingRenderer()
  {
    return _leavingRenderer;
  }

  /** the leaving mark is off the screen: the renderer can let it go */
  public final boolean hasLeftRenderer()
  {
    return _leavingRenderer && isOffScreen();
  }

  /** fades out with its transition, at once, and stays in the renderer: for a renderer being disabled */
  public final void startHiding()
  {
    _hiding = true;
    _plannedTarget = -1;
    _plannedSinceMS = -1;
    _declutterTarget = -1;
  }

  /** comes back from startHiding: decluttered, it waits for the renderer to find it room; otherwise it fades in its current outfit at once */
  public final void stopHiding(boolean decluttered)
  {
    _hiding = false;
    if (!decluttered)
    {
      _plannedTarget = (int) _outfitIndex;
      _plannedSinceMS = -1;
      _declutterTarget = (int) _outfitIndex;
    }
  }

  /** nothing of it is drawn: not its outfit, not one fading out */
  public final boolean isOffScreen()
  {
    return _declutterHidden && (_leavingGLState == null);
  }

  public final boolean deletesWhenGone()
  {
    return _deleteWhenGone;
  }

  /** the order among the marks that compete for space: the higher, the earlier */
  public final void setPriority(double priority)
  {
    _priority = priority;
  }

  public final boolean hasPriority()
  {
    return !(_priority != _priority);
  }

  public final double getPriority()
  {
    return _priority;
  }

  public final float getTextureWidth()
  {
    return _textureWidth;
  }

  public final float getTextureHeight()
  {
    return _textureHeight;
  }

  /** the size drawn on screen: the texture size times the app's and the effects' scales */
  public final float getScreenWidth()
  {
    return _textureWidth * _textureWidthScale * _effectScale * _transitionWidthScale * _horizonScale;
  }

  public final float getScreenHeight()
  {
    return _textureHeight * _textureHeightScale * _effectScale * _transitionHeightScale * _horizonScale;
  }

  public final Vector2F getTextureExtent()
  {
    return new Vector2F(_textureWidth, _textureHeight);
  }

  public final MarkUserData getUserData()
  {
    return _userData;
  }

  public final void setUserData(MarkUserData userData)
  {
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
    _userData = userData;
  }

  public final boolean touched(TouchEvent touchEvent)
  {
    return (_listener == null) ? false : _listener.touchedMark(this, touchEvent);
  }

  public final void setMinDistanceToCamera(double minDistanceToCamera)
  {
    _minDistanceToCamera = minDistanceToCamera;
  }
  public final double getMinDistanceToCamera()
  {
    return _minDistanceToCamera;
  }

  public final void setMaxDistanceToCamera(double maxDistanceToCamera)
  {
    _maxDistanceToCamera = maxDistanceToCamera;
  }
  public final double getMaxDistanceToCamera()
  {
    return _maxDistanceToCamera;
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
    if (_cartesianPosition == null)
    {
      double altitude = _position._height;
      if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
      {
        altitude += _currentSurfaceElevation;
      }
  
      Geodetic3D positionWithSurfaceElevation = new Geodetic3D(_position._latitude, _position._longitude, altitude);
  
      _cartesianPosition = new Vector3D(planet.toCartesian(positionWithSurfaceElevation));
    }
    return _cartesianPosition;
  }

  /** horizonBandRadiansPerPixel: the camera's angle per screen pixel, so the mark shrinks while it sinks its own apparent height behind the horizon; 0: no band */
  public final void render(G3MRenderContext rc, MarksRenderer renderer, MutableVector3D cameraPosition, double cameraHeight, GLState parentGLState, Planet planet, GL gl, IFloatBuffer billboardTexCoords, double horizonBandRadiansPerPixel)
  {
    _renderedMark = false;
  
    final boolean hasSomethingToDraw = !_declutterHidden || (_leavingGLState != null);
    if (hasSomethingToDraw && isVisibleFrom(planet, cameraPosition, cameraHeight))
    {
      updateHorizonScale(horizonBandRadiansPerPixel);
      if (_glPositionOutdated)
      {
        updateGLPosition(planet);
      }
      drawLeavingOutfit(rc, parentGLState);
  
      if (!_declutterHidden)
      {
        ensureTexture(rc);
        if (_textureID != null)
        {
          ensureGLState(planet, billboardTexCoords, parentGLState);
          startPendingEffects(rc, renderer);
          draw(rc, _glState);
          _renderedMark = true;
        }
      }
    }
  }

  public final void elevationChanged(Geodetic2D position, double rawElevation, double verticalExaggeration)
  {
  
    if ((rawElevation != rawElevation))
    {
      _currentSurfaceElevation = 0; //USING 0 WHEN NO ELEVATION DATA
    }
    else
    {
      _currentSurfaceElevation = rawElevation * verticalExaggeration;
    }
  
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
    _cartesianPosition = null;
  
    clearGLState();
    releaseLeavingOutfit();
  }

  public final void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration) //Without considering vertical exaggeration
  {
  }

  public final void setPosition(Geodetic3D position)
  {
    if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
    {
      throw new RuntimeException("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
    }
  
    if (_position != null)
       _position.dispose();
    _position = position;
  
    if (_cartesianPosition != null)
       _cartesianPosition.dispose();
    _cartesianPosition = null;
  
    if (_normalAtMarkPosition != null)
       _normalAtMarkPosition.dispose();
    _normalAtMarkPosition = null;
  
    _glPositionOutdated = true;
  }

  public final void setScreenSize(int width, int height)
  {
    _textureWidth = width;
    _textureHeight = height;
    _textureSizeSetExternally = true;
  
    updateBillboard();
  }
  public final void setScreenSizeScale(float scaleWidth, float scaleHeight)
  {
    _textureWidthScale = scaleWidth;
    _textureHeightScale = scaleHeight;
  
    updateBillboard();
  }

  /** for the zoom effects only; the app scales with setScreenSizeScale */
  public final void setEffectScale(float effectScale)
  {
    _effectScale = effectScale;
  
    updateBillboard();
  }

  public final void setTextureCoordinatesTransformation(Vector2F translation, Vector2F scaling)
  {
    setTextureCoordinatesTransformation(translation._x, translation._y, scaling._x, scaling._y);
  }

  public final void setTextureCoordinatesTransformation(float translationX, float translationY, float scalingX, float scalingY)
  {
  
    _translationTCX = translationX;
    _translationTCY = translationY;
  
    _scalingTCX = scalingX;
    _scalingTCY = scalingY;
  
    if (_textureGLF != null)
    {
  
      if (!_textureGLF.hasTranslateAndScale())
      {
        clearGLState();
      }
  
      _textureGLF.setTranslation(_translationTCX, _translationTCY);
      _textureGLF.setScale(_scalingTCX, _scalingTCY);
    }
  }

  public final void setMarkAnchor(float anchorU, float anchorV)
  {
    _appAnchorU = anchorU;
    _appAnchorV = anchorV;
    changeAnchor(anchorU, anchorV);
  }

  public final Vector2F getMarkAnchor()
  {
    return new Vector2F(_anchorU, _anchorV);
  }
  public final float getMarkAnchorU()
  {
    return _anchorU;
  }
  public final float getMarkAnchorV()
  {
    return _anchorV;
  }

  public final void setToken(String token)
  {
    _token = token;
  }

  public final String getToken()
  {
    return _token;
  }

  public final void setZoomInAppears(boolean zoomInAppears)
  {
    _zoomInAppears = zoomInAppears;
  }

  public final boolean getZoomInAppears()
  {
    return _zoomInAppears;
  }


  public final void animatedRemove(boolean deleteMark)
  {
    _zoomOutDisappears = true;
    _deleteMarkOnDisappears = deleteMark;
  }

  // true once the zoom-out effect owns the mark and will delete it on its own
  public final boolean isDisappearing()
  {
    return _zoomOutDisappearsStarted;
  }

  public final void cancelEffects()
  {
    if (_effectsScheduler != null)
    {
      _effectsScheduler.cancelAllEffectsFor(getEffectTarget());
    }
  }

  public final void forgetZoomOutEffect()
  {
    _zoomOutEffect = null;
  }

}