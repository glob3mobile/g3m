package org.glob3.mobile.generated;import java.util.*;

public class Mark extends SurfaceElevationListener
{

  private IImageBuilder _imageBuilder;

  /**
   * The text the mark displays.
   * Useless if the mark does not have label.
   */
  private final String _label;
  /**
   * Flag to know if the label will be located under the icon (if TRUE) or on its right (if FALSE).
   * Useless if the mark does not have label or icon.
   * Default value: TRUE
   */
  private final boolean _labelBottom;
  /**
   * The font size of the text.
   * Useless if the mark does not have label.
   * Default value: 20
   */
  private final float _labelFontSize;


  /**
   * The color of the text.
   * Useless if the mark does not have label.
   * Default value: white
   */
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Color _labelFontColor;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Color _labelFontColor = new internal();
//#endif

  /**
   * The color of the text shadow.
   * Useless if the mark does not have label.
   * Default value: black
   */
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Color _labelShadowColor;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Color _labelShadowColor = new internal();
//#endif

  /**
   * The number of pixels between the icon and the text.
   * Useless if the mark does not have label or icon.
   * Default value: 2
   */
  private final int _labelGapSize;
  /**
   * The URL to get the image file.
   * Useless if the mark does not have icon.
   */
  private URL _iconURL = new URL();
  /**
   * The point where the mark will be geo-located.
   */
  private Geodetic3D _position;
  /**
   * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
   * Default value: 4.5e+06
   */
  private double _minDistanceToCamera;
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

  private String _token = "";

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TextureIDReference _textureId;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public TextureIDReference _textureId = new internal();
//#endif

  private Vector3D _cartesianPosition;

  private boolean _textureSolved;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _textureImage;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _textureImage = new internal();
//#endif
  private float _textureWidth;
  private float _textureHeight;
  private float _textureWidthProportion;
  private float _textureHeightProportion;
  private boolean _textureSizeSetExternally;
  private boolean _textureProportionSetExternally;
  private String _imageID;

  private boolean _hasTCTransformations;
  private float _translationTCX;
  private float _translationTCY;
  private float _scalingTCX;
  private float _scalingTCY;

  private boolean _renderedMark;


  private GLState _glState;
  private void createGLState(Planet planet, IFloatBuffer billboardTexCoords)
  {
	_glState = new GLState();
  
	  Vector3D pos = getCartesianPosition(planet);
	_billboardGLF = new BillboardGLFeature(new Vector3D(.0,.0,.0), _textureWidth, _textureHeight, _anchorU, _anchorV);
  
	  MutableMatrix44D m = MutableMatrix44D.createTranslationMatrix(pos);
	  ModelTransformGLFeature mtglf = new ModelTransformGLFeature(m.asMatrix44D());
	  _glState.addGLFeature(mtglf, false);
  
	_glState.addGLFeature(_billboardGLF, false);
  
	if (_textureId != null)
	{
  
	  if (_hasTCTransformations)
	  {
		_textureGLF = new TextureGLFeature(_textureId.getID(), billboardTexCoords, 2, 0, false, 0, true, _textureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationTCX, _translationTCY, _scalingTCX, _scalingTCY, 0.0f, 0.0f, 0.0f);
	  }
	  else
	  {
		_textureGLF = new TextureGLFeature(_textureId.getID(), billboardTexCoords, 2, 0, false, 0, true, _textureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
	  }
  
	  _glState.addGLFeature(_textureGLF, false);
	}
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
	}
  }

  private MutableVector3D _markCameraVector = new MutableVector3D();

  private float _anchorU;
  private float _anchorV;
  private BillboardGLFeature _billboardGLF;

  private boolean _initialized;

  private boolean _zoomInAppears;
  private EffectsScheduler _effectsScheduler;
  private boolean _firstRender;


  private EffectTarget _effectTarget;
  private EffectTarget getEffectTarget()
  {
	if (_effectTarget == null)
	{
	  _effectTarget = new MarkEffectTarget();
	}
	return _effectTarget;
  }



  public static class ImageBuilderListener implements IImageBuilderListener
  {
	private Mark _mark;

	public ImageBuilderListener(Mark mark)
	{
		_mark = mark;

	}

	public void dispose()
	{

	}

	public final void imageCreated(IImage image, String imageName)
	{
	  _mark.onImageCreated(image, imageName);
	}

	public final void onError(String error)
	{
	  _mark.onImageCreationError(error);
	}

  }


  /**
   * Creates a mark with icon and label
   */
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, userData, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, labelGapSize, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, labelShadowColor, 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, labelBottom, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
	  this(label, iconURL, position, altitudeMode, minDistanceToCamera, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  this(label, iconURL, position, altitudeMode, 4.5e+06, true, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), 2, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& label, const URL& iconURL, const Geodetic3D& position, AltitudeMode altitudeMode, double minDistanceToCamera =4.5e+06, const boolean labelBottom =true, const float labelFontSize =20, const Color* labelFontColor =Color::newFromRGBA(1, 1, 1, 1), const Color* labelShadowColor =Color::newFromRGBA(0, 0, 0, 1), const int labelGapSize =2, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _imageBuilder(null), _label(label), _iconURL(iconURL), _position(new Geodetic3D(position)), _altitudeMode(altitudeMode), _labelBottom(labelBottom), _labelFontSize(labelFontSize), _labelFontColor(labelFontColor), _labelShadowColor(labelShadowColor), _labelGapSize(labelGapSize), _textureId(null), _cartesianPosition(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener), _imageID(iconURL._path + "_" + label), _surfaceElevationProvider(null), _currentSurfaceElevation(0.0), _glState(null), _normalAtMarkPosition(null), _textureSizeSetExternally(false), _hasTCTransformations(false), _textureGLF(null), _anchorU(0.5), _anchorV(0.5), _billboardGLF(null), _textureHeightProportion(1.0), _textureWidthProportion(1.0), _textureProportionSetExternally(false), _initialized(false), _zoomInAppears(true), _effectsScheduler(null), _firstRender(true), _effectTarget(null)
  public Mark(String label, URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, boolean labelBottom, float labelFontSize, Color labelFontColor, Color labelShadowColor, int labelGapSize, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _imageBuilder = null;
	  _label = label;
	  _iconURL = new URL(iconURL);
	  _position = new Geodetic3D(position);
	  _altitudeMode = altitudeMode;
	  _labelBottom = labelBottom;
	  _labelFontSize = labelFontSize;
	  _labelFontColor = labelFontColor;
	  _labelShadowColor = labelShadowColor;
	  _labelGapSize = labelGapSize;
	  _textureId = null;
	  _cartesianPosition = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0F;
	  _textureHeight = 0F;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
	  _imageID = iconURL._path + "_" + label;
	  _surfaceElevationProvider = null;
	  _currentSurfaceElevation = 0.0;
	  _glState = null;
	  _normalAtMarkPosition = null;
	  _textureSizeSetExternally = false;
	  _hasTCTransformations = false;
	  _textureGLF = null;
	  _anchorU = 0.5F;
	  _anchorV = 0.5F;
	  _billboardGLF = null;
	  _textureHeightProportion = 1.0F;
	  _textureWidthProportion = 1.0F;
	  _textureProportionSetExternally = false;
	  _initialized = false;
	  _zoomInAppears = true;
	  _effectsScheduler = null;
	  _firstRender = true;
	  _effectTarget = null;
  
  }

  /**
   * Creates a mark just with label, without icon
   */
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, userData, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, labelShadowColor, null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, labelFontColor, Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, labelFontSize, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
	  this(label, position, altitudeMode, minDistanceToCamera, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  this(label, position, altitudeMode, 4.5e+06, 20, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& label, const Geodetic3D& position, AltitudeMode altitudeMode, double minDistanceToCamera =4.5e+06, const float labelFontSize =20, const Color* labelFontColor =Color::newFromRGBA(1, 1, 1, 1), const Color* labelShadowColor =Color::newFromRGBA(0, 0, 0, 1), MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _imageBuilder(null), _label(label), _labelBottom(true), _iconURL("", false), _position(new Geodetic3D(position)), _altitudeMode(altitudeMode), _labelFontSize(labelFontSize), _labelFontColor(labelFontColor), _labelShadowColor(labelShadowColor), _labelGapSize(2), _textureId(null), _cartesianPosition(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener), _imageID("_" + label), _surfaceElevationProvider(null), _currentSurfaceElevation(0.0), _glState(null), _normalAtMarkPosition(null), _textureSizeSetExternally(false), _textureGLF(null), _hasTCTransformations(false), _anchorU(0.5), _anchorV(0.5), _billboardGLF(null), _textureHeightProportion(1.0), _textureWidthProportion(1.0), _textureProportionSetExternally(false), _initialized(false), _zoomInAppears(true), _effectsScheduler(null), _firstRender(true), _effectTarget(null)
  public Mark(String label, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, float labelFontSize, Color labelFontColor, Color labelShadowColor, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _imageBuilder = null;
	  _label = label;
	  _labelBottom = true;
	  _iconURL = new URL("", false);
	  _position = new Geodetic3D(position);
	  _altitudeMode = altitudeMode;
	  _labelFontSize = labelFontSize;
	  _labelFontColor = labelFontColor;
	  _labelShadowColor = labelShadowColor;
	  _labelGapSize = 2;
	  _textureId = null;
	  _cartesianPosition = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0F;
	  _textureHeight = 0F;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
	  _imageID = "_" + label;
	  _surfaceElevationProvider = null;
	  _currentSurfaceElevation = 0.0;
	  _glState = null;
	  _normalAtMarkPosition = null;
	  _textureSizeSetExternally = false;
	  _textureGLF = null;
	  _hasTCTransformations = false;
	  _anchorU = 0.5F;
	  _anchorV = 0.5F;
	  _billboardGLF = null;
	  _textureHeightProportion = 1.0F;
	  _textureWidthProportion = 1.0F;
	  _textureProportionSetExternally = false;
	  _initialized = false;
	  _zoomInAppears = true;
	  _effectsScheduler = null;
	  _firstRender = true;
	  _effectTarget = null;
  
  }

  /**
   * Creates a mark just with icon, without label
   */
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(iconURL, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(iconURL, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData)
  {
	  this(iconURL, position, altitudeMode, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
	  this(iconURL, position, altitudeMode, minDistanceToCamera, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  this(iconURL, position, altitudeMode, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const URL& iconURL, const Geodetic3D& position, AltitudeMode altitudeMode, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _imageBuilder(null), _label(""), _labelBottom(true), _iconURL(iconURL), _position(new Geodetic3D(position)), _altitudeMode(altitudeMode), _labelFontSize(20), _labelFontColor(Color::newFromRGBA(1, 1, 1, 1)), _labelShadowColor(Color::newFromRGBA(0, 0, 0, 1)), _labelGapSize(2), _textureId(null), _cartesianPosition(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener), _imageID(iconURL._path + "_"), _surfaceElevationProvider(null), _currentSurfaceElevation(0.0), _glState(null), _normalAtMarkPosition(null), _textureSizeSetExternally(false), _textureGLF(null), _hasTCTransformations(false), _anchorU(0.5), _anchorV(0.5), _billboardGLF(null), _textureHeightProportion(1.0), _textureWidthProportion(1.0), _textureProportionSetExternally(false), _initialized(false), _zoomInAppears(true), _effectsScheduler(null), _firstRender(true), _effectTarget(null)
  public Mark(URL iconURL, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _imageBuilder = null;
	  _label = "";
	  _labelBottom = true;
	  _iconURL = new URL(iconURL);
	  _position = new Geodetic3D(position);
	  _altitudeMode = altitudeMode;
	  _labelFontSize = 20F;
	  _labelFontColor = Color.newFromRGBA(1, 1, 1, 1);
	  _labelShadowColor = Color.newFromRGBA(0, 0, 0, 1);
	  _labelGapSize = 2;
	  _textureId = null;
	  _cartesianPosition = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0F;
	  _textureHeight = 0F;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
	  _imageID = iconURL._path + "_";
	  _surfaceElevationProvider = null;
	  _currentSurfaceElevation = 0.0;
	  _glState = null;
	  _normalAtMarkPosition = null;
	  _textureSizeSetExternally = false;
	  _textureGLF = null;
	  _hasTCTransformations = false;
	  _anchorU = 0.5F;
	  _anchorV = 0.5F;
	  _billboardGLF = null;
	  _textureHeightProportion = 1.0F;
	  _textureWidthProportion = 1.0F;
	  _textureProportionSetExternally = false;
	  _initialized = false;
	  _zoomInAppears = true;
	  _effectsScheduler = null;
	  _firstRender = true;
	  _effectTarget = null;
  
  }

  /**
   * Creates a mark whith a given pre-renderer IImage
   */
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData)
  {
	  this(image, imageID, position, altitudeMode, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
	  this(image, imageID, position, altitudeMode, minDistanceToCamera, null, true, null, false);
  }
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  this(image, imageID, position, altitudeMode, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const IImage* image, const String& imageID, const Geodetic3D& position, AltitudeMode altitudeMode, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _imageBuilder(null), _label(""), _labelBottom(true), _iconURL(URL("", false)), _position(new Geodetic3D(position)), _altitudeMode(altitudeMode), _labelFontSize(20), _labelFontColor(null), _labelShadowColor(null), _labelGapSize(2), _textureId(null), _cartesianPosition(null), _textureSolved(true), _textureImage(image), _renderedMark(false), _textureWidth(image->getWidth()), _textureHeight(image->getHeight()), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener), _imageID(imageID), _surfaceElevationProvider(null), _currentSurfaceElevation(0.0), _glState(null), _normalAtMarkPosition(null), _textureSizeSetExternally(false), _hasTCTransformations(false), _anchorU(0.5), _anchorV(0.5), _billboardGLF(null), _textureHeightProportion(1.0), _textureWidthProportion(1.0), _initialized(false), _zoomInAppears(true), _effectsScheduler(null), _firstRender(true), _effectTarget(null)
  public Mark(IImage image, String imageID, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _imageBuilder = null;
	  _label = "";
	  _labelBottom = true;
	  _iconURL = new URL(new URL("", false));
	  _position = new Geodetic3D(position);
	  _altitudeMode = altitudeMode;
	  _labelFontSize = 20F;
	  _labelFontColor = null;
	  _labelShadowColor = null;
	  _labelGapSize = 2;
	  _textureId = null;
	  _cartesianPosition = null;
	  _textureSolved = true;
	  _textureImage = image;
	  _renderedMark = false;
	  _textureWidth = image.getWidth();
	  _textureHeight = image.getHeight();
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
	  _imageID = imageID;
	  _surfaceElevationProvider = null;
	  _currentSurfaceElevation = 0.0;
	  _glState = null;
	  _normalAtMarkPosition = null;
	  _textureSizeSetExternally = false;
	  _hasTCTransformations = false;
	  _anchorU = 0.5F;
	  _anchorV = 0.5F;
	  _billboardGLF = null;
	  _textureHeightProportion = 1.0F;
	  _textureWidthProportion = 1.0F;
	  _initialized = false;
	  _zoomInAppears = true;
	  _effectsScheduler = null;
	  _firstRender = true;
	  _effectTarget = null;
  
  }

  /**
   * Creates a mark whith a IImageBuilder, in future versions it'll be the only constructor
   */
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(imageBuilder, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(imageBuilder, position, altitudeMode, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData)
  {
	  this(imageBuilder, position, altitudeMode, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera)
  {
	  this(imageBuilder, position, altitudeMode, minDistanceToCamera, null, true, null, false);
  }
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  this(imageBuilder, position, altitudeMode, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(IImageBuilder* imageBuilder, const Geodetic3D& position, AltitudeMode altitudeMode, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _imageBuilder(imageBuilder), _label(""), _labelBottom(true), _iconURL(URL("", false)), _position(new Geodetic3D(position)), _altitudeMode(altitudeMode), _labelFontSize(20), _labelFontColor(null), _labelShadowColor(null), _labelGapSize(2), _textureId(null), _cartesianPosition(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener), _imageID(""), _surfaceElevationProvider(null), _currentSurfaceElevation(0.0), _glState(null), _normalAtMarkPosition(null), _textureSizeSetExternally(false), _hasTCTransformations(false), _anchorU(0.5), _anchorV(0.5), _billboardGLF(null), _textureHeightProportion(1.0), _textureWidthProportion(1.0), _initialized(false), _zoomInAppears(true), _effectsScheduler(null), _firstRender(true), _effectTarget(null)
  public Mark(IImageBuilder imageBuilder, Geodetic3D position, AltitudeMode altitudeMode, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _imageBuilder = imageBuilder;
	  _label = "";
	  _labelBottom = true;
	  _iconURL = new URL(new URL("", false));
	  _position = new Geodetic3D(position);
	  _altitudeMode = altitudeMode;
	  _labelFontSize = 20F;
	  _labelFontColor = null;
	  _labelShadowColor = null;
	  _labelGapSize = 2;
	  _textureId = null;
	  _cartesianPosition = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0F;
	  _textureHeight = 0F;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
	  _imageID = "";
	  _surfaceElevationProvider = null;
	  _currentSurfaceElevation = 0.0;
	  _glState = null;
	  _normalAtMarkPosition = null;
	  _textureSizeSetExternally = false;
	  _hasTCTransformations = false;
	  _anchorU = 0.5F;
	  _anchorV = 0.5F;
	  _billboardGLF = null;
	  _textureHeightProportion = 1.0F;
	  _textureWidthProportion = 1.0F;
	  _initialized = false;
	  _zoomInAppears = true;
	  _effectsScheduler = null;
	  _firstRender = true;
	  _effectTarget = null;
	if (_imageBuilder.isMutable())
	{
	  ILogger.instance().logError("Marks doesn't support mutable image builders");
	}
  }

  public void dispose()
  {
	if (_effectsScheduler != null)
	{
	  _effectsScheduler.cancelAllEffectsFor(getEffectTarget());
	}
	if (_effectTarget != null)
		_effectTarget.dispose();
  
	if (_labelFontColor != null)
		_labelFontColor.dispose();
	if (_labelShadowColor != null)
		_labelShadowColor.dispose();
  
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
  
	if (_textureImage != null)
		_textureImage.dispose();
  
	if (_imageBuilder != null)
		_imageBuilder.dispose();
  
	if (_glState != null)
	{
	  _glState._release();
	}
  
	if (_textureId != null)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  _textureId.dispose();
//#endif
	  if (_textureId != null)
		  _textureId.dispose(); //Releasing texture
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isInitialized() const
  public final boolean isInitialized()
  {
	return _initialized;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getLabel() const
  public final String getLabel()
  {
	return _label;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic3D getPosition() const
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
  
	if (!_textureSolved)
	{
	  if (_imageBuilder != null)
	  {
		_imageBuilder.build(context, new ImageBuilderListener(this), true);
	  }
	  else
	  {
		final boolean hasIconURL = (_iconURL._path.length() != 0);
		if (hasIconURL)
		{
		  IDownloader downloader = context.getDownloader();
  
		  downloader.requestImage(_iconURL, downloadPriority, TimeInterval.fromDays(30), true, new IconDownloadListener(this, _label, _labelBottom, _labelFontSize, _labelFontColor, _labelShadowColor, _labelGapSize), true);
		}
		else
		{
		  final boolean hasLabel = (_label.length() != 0);
		  if (hasLabel)
		  {
			ITextUtils.instance().createLabelImage(_label, _labelFontSize, _labelFontColor, _labelShadowColor, new MarkLabelImageListener(null, this), true);
		  }
		  else
		  {
			ILogger.instance().logWarning("Mark created without label nor icon");
		  }
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady() const
  public final boolean isReady()
  {
	return _textureSolved;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isRendered() const
  public final boolean isRendered()
  {
	return _renderedMark;
  }

  public final void onTextureDownloadError()
  {
	_textureSolved = true;
  
	if (_labelFontColor != null)
		_labelFontColor.dispose();
	_labelFontColor = null;
	if (_labelShadowColor != null)
		_labelShadowColor.dispose();
	_labelShadowColor = null;
  
	ILogger.instance().logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")", _iconURL._path.c_str(), _label.c_str());
  }

  public final void onTextureDownload(IImage image)
  {
	_textureSolved = true;
  
	if (_labelFontColor != null)
		_labelFontColor.dispose();
	_labelFontColor = null;
	if (_labelShadowColor != null)
		_labelShadowColor.dispose();
	_labelShadowColor = null;
  
	_textureImage = image;
  
	if (!_textureSizeSetExternally)
	{
	  _textureWidth = _textureImage.getWidth();
	  _textureHeight = _textureImage.getHeight();
  
	  if (_textureProportionSetExternally)
	  {
		_textureWidth *= _textureWidthProportion;
		_textureHeight *= _textureHeightProportion;
	  }
	}
  
  }

  public final void onImageCreated(IImage image, String imageName)
  {
	_textureSolved = true;
	_imageID = imageName;
  
  //  delete _labelFontColor;
  //  _labelFontColor = NULL;
  //  delete _labelShadowColor;
  //  _labelShadowColor = NULL;
	if (_imageBuilder != null)
		_imageBuilder.dispose();
	_imageBuilder = null;
  
	_textureImage = image;
  
	if (!_textureSizeSetExternally)
	{
	  _textureWidth = _textureImage.getWidth();
	  _textureHeight = _textureImage.getHeight();
  
	  if (_textureProportionSetExternally)
	  {
		_textureWidth *= _textureWidthProportion;
		_textureHeight *= _textureHeightProportion;
	  }
	}
  }

  public final void onImageCreationError(String error)
  {
	_textureSolved = true;
  
  //  delete _labelFontColor;
  //  _labelFontColor = NULL;
  //  delete _labelShadowColor;
  //  _labelShadowColor = NULL;
	if (_imageBuilder != null)
		_imageBuilder.dispose();
	_imageBuilder = null;
  
	ILogger.instance().logError("Can't create image for Mark: \"%s\"", error.c_str());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getTextureWidth() const
  public final float getTextureWidth()
  {
	return _textureWidth;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getTextureHeight() const
  public final float getTextureHeight()
  {
	return _textureHeight;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F getTextureExtent() const
  public final Vector2F getTextureExtent()
  {
	return new Vector2F(_textureWidth, _textureHeight);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MarkUserData* getUserData() const
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

  public final boolean touched()
  {
	return (_listener == null) ? false : _listener.touchedMark(this);
  }

  public final void setMinDistanceToCamera(double minDistanceToCamera)
  {
	_minDistanceToCamera = minDistanceToCamera;
  }
  public final double getMinDistanceToCamera()
  {
	return _minDistanceToCamera;
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
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _cartesianPosition = new Vector3D(planet->toCartesian(positionWithSurfaceElevation));
	  _cartesianPosition = new Vector3D(planet.toCartesian(new Geodetic3D(positionWithSurfaceElevation)));
	}
	return _cartesianPosition;
  }

  public final void render(G3MRenderContext rc, MutableVector3D cameraPosition, double cameraHeight, GLState parentGLState, Planet planet, GL gl, IFloatBuffer billboardTexCoords)
  {
  
	final Vector3D markPosition = getCartesianPosition(planet);
  
	_markCameraVector.set(markPosition._x - cameraPosition.x(), markPosition._y - cameraPosition.y(), markPosition._z - cameraPosition.z());
  
	// mark will be renderered only if is renderable by distance and placed on a visible globe area
	boolean renderableByDistance;
	if (_minDistanceToCamera == 0)
	{
	  renderableByDistance = true;
	}
	else
	{
	  final double squaredDistanceToCamera = _markCameraVector.squaredLength();
	  renderableByDistance = (squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera));
	}
  
	_renderedMark = false;
  
	if (renderableByDistance)
	{
	  boolean occludedByHorizon = false;
  
	  if (_position._height > cameraHeight)
	  {
		// Computing horizon culling
		final java.util.ArrayList<Double> dists = planet.intersectionsDistances(cameraPosition.x(), cameraPosition.y(), cameraPosition.z(), _markCameraVector.x(), _markCameraVector.y(), _markCameraVector.z());
		if (dists.size() > 0)
		{
		  final double dist = dists.get(0);
		  if (dist > 0.0 && dist < 1.0)
		  {
			occludedByHorizon = true;
		  }
		}
	  }
	  else
	  {
		// if camera position is upper than mark we can compute horizon culling in a much simpler way
		if (_normalAtMarkPosition == null)
		{
		  _normalAtMarkPosition = new Vector3D(planet.geodeticSurfaceNormal(markPosition));
		}
		//      occludedByHorizon = (_normalAtMarkPosition->angleInRadiansBetween(markCameraVector) <= HALF_PI);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: occludedByHorizon = (Vector3D::angleInRadiansBetween(*_normalAtMarkPosition, _markCameraVector) <= DefineConstants.HALF_PI);
		occludedByHorizon = (Vector3D.angleInRadiansBetween(_normalAtMarkPosition, new MutableVector3D(_markCameraVector)) <= DefineConstants.HALF_PI);
	  }
  
	  if (!occludedByHorizon)
	  {
		if ((_textureId == null) && (_textureImage != null))
		{
		  _textureId = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _imageID, false);
  
		  if (_textureImage != null)
			  _textureImage.dispose();
		  _textureImage = null;
		}
  
		if (_textureId != null)
		{
		  if (_glState == null)
		  {
			createGLState(planet, billboardTexCoords); // If GLState was disposed due to elevation change
		  }
		  _glState.setParent(parentGLState);
  
		  if (_firstRender)
		  {
			_firstRender = false;
			if (_zoomInAppears)
			{
			  _effectsScheduler = rc.getEffectsScheduler();
			  _effectsScheduler.startEffect(new MarkZoomInEffect(this), getEffectTarget());
			}
		  }
  
		  rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, rc.getGPUProgramManager());
  
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
  }

  public final void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration) //Without considering vertical exaggeration
  {
  }

  public final void setPosition(Geodetic3D position)
  {
	if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
	{
	  THROW_EXCEPTION("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
	}
  
	if (_position != null)
		_position.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	_position = new Geodetic3D(position);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_position = position;
//#endif
  
	if (_cartesianPosition != null)
		_cartesianPosition.dispose();
	_cartesianPosition = null;
  
	clearGLState();
  }

  public final void setOnScreenSizeOnPixels(int width, int height)
  {
  
	_textureWidth = width;
	_textureHeight = height;
	_textureSizeSetExternally = true;
  
	if (_glState != null)
	{
	  BillboardGLFeature b = (BillboardGLFeature) _glState.getGLFeature(GLFeatureID.GLF_BILLBOARD);
	  if (b != null)
	  {
		b.changeSize(IMathUtils.instance().round(_textureWidth), IMathUtils.instance().round(_textureHeight));
	  }
	}
  }
  public final void setOnScreenSizeOnProportionToImage(float width, float height)
  {
	_textureWidthProportion = width;
	_textureHeightProportion = height;
	_textureProportionSetExternally = true;
  
	if (_glState != null)
	{
	  BillboardGLFeature b = (BillboardGLFeature) _glState.getGLFeature(GLFeatureID.GLF_BILLBOARD);
	  if (b != null)
	  {
		b.changeSize(IMathUtils.instance().round(_textureWidth * _textureWidthProportion), IMathUtils.instance().round(_textureHeight * _textureHeightProportion));
	  }
	}
  }

  public final void setTextureCoordinatesTransformation(Vector2F translation, Vector2F scaling)
  {
  
	_translationTCX = translation._x;
	_translationTCY = translation._y;
  
	_scalingTCX = scaling._x;
	_scalingTCY = scaling._y;
  
	if (_translationTCX != 0 || _translationTCY != 0 || _scalingTCX != 1 || _scalingTCY != 1)
	{
	  _hasTCTransformations = true;
	}
  
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
	if (_billboardGLF != null)
	{
	  _billboardGLF.changeAnchor(anchorU, anchorV);
	}
	_anchorU = anchorU;
	_anchorV = anchorV;
  }

  public final void setToken(String token)
  {
	_token = token;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getToken() const
  public final String getToken()
  {
	return _token;
  }

  public final void setZoomInAppears(boolean zoomInAppears)
  {
	_zoomInAppears = zoomInAppears;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getZoomInAppears() const
  public final boolean getZoomInAppears()
  {
	return _zoomInAppears;
  }

}
