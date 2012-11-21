package org.glob3.mobile.generated; 
//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LeveledTexturedMesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;


public class MultiLayerTileTexturizer extends TileTexturizer
{
//  LayerSet* _layerSet;

  private TilesRenderParameters _parameters;

  private IFloatBuffer _texCoordsCache;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTextureCoordinates(const TileRenderContext* trc) const
  private IFloatBuffer getTextureCoordinates(TileRenderContext trc)
  {
	if (_texCoordsCache == null)
	{
	  _texCoordsCache = trc.getTessellator().createUnitTextCoords();
	}
	return _texCoordsCache;
  }

  private int _pendingTopTileRequests;

  private TexturesHandler _texturesHandler;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LeveledTexturedMesh* getMesh(Tile* tile) const
  private LeveledTexturedMesh getMesh(Tile tile)
  {
	TileTextureBuilderHolder tileBuilderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
	return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getMesh();
  }

  public MultiLayerTileTexturizer()
  {
	  _parameters = null;
	  _texCoordsCache = null;
	  _pendingTopTileRequests = 0;
	  _texturesHandler = null;
  
  }

  public final void countTopTileRequest()
  {
	_pendingTopTileRequests--;
  }

  public void dispose()
  {
	  if (_texCoordsCache != null)
		  _texCoordsCache.dispose();
	  _texCoordsCache = null;
  }

  public final boolean isReady(RenderContext rc, LayerSet layerSet)
  {
	if (_pendingTopTileRequests > 0)
	{
	  return false;
	}
	if (layerSet != null)
	{
	  return layerSet.isReady();
	}
	return true;
	//  return (_pendingTopTileRequests <= 0) && _layerSet->isReady();
  }

  public final void initialize(InitializationContext ic, TilesRenderParameters parameters)
  {
	_parameters = parameters;
  //  _layerSet->initialize(ic);
  }

  public final Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
	_texturesHandler = rc.getTexturesHandler();
  
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder == null)
	{
	  builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, rc, trc.getLayerSet(), _parameters, rc.getDownloader(), tile, tessellatorMesh, getTextureCoordinates(trc)));
	  tile.setTexturizerData(builderHolder);
	}
  
	if (trc.isForcedFullRender())
	{
	  builderHolder.get().start();
	}
	else
	{
//C++ TO JAVA CONVERTER TODO TASK: Java does not allow declaring types within methods:
//	  class BuilderStartTask : public FrameTask
//	  {
//	  private:
//		TileTextureBuilder* _builder;
//  
//	  public:
//		BuilderStartTask(TileTextureBuilder* builder) : _builder(builder)
//		{
//		  _builder->_retain();
//		}
//  
//		virtual ~BuilderStartTask()
//		{
//		  _builder->_release();
//		}
//  
//		void execute(const RenderContext* rc)
//		{
//		  _builder->start();
//		}
//  
//		boolean isCanceled(const RenderContext *rc)
//		{
//		  return _builder->isCanceled();
//		}
//	  };
	  rc.getFrameTasksExecutor().addPreRenderTask(new BuilderStartTask(builderHolder.get()));
	}
  
	tile.setTexturizerDirty(false);
	return builderHolder.get().getMesh();
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder != null)
	{
	  builderHolder.get().cancel();
	  builderHolder.get().cleanTile();
	  builderHolder.get().cleanMesh();
	}
	else
	{
	  if (mesh != null)
	  {
		ILogger.instance().logInfo("break (point) on me 4\n");
	  }
	}
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
	return false;
  }

  public final void justCreatedTopTile(RenderContext rc, Tile tile, LayerSet layerSet)
  {
	java.util.ArrayList<Petition> petitions = layerSet.createTileMapPetitions(rc, tile, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
  
	_pendingTopTileRequests += petitions.size();
  
	final int priority = 1000000000; // very big priority for toplevel tiles
	for (int i = 0; i < petitions.size(); i++)
	{
	  final Petition petition = petitions.get(i);
	  rc.getDownloader().requestImage(new URL(petition.getURL()), priority, new TopTileDownloadListener(this), true);
  
	  if (petition != null)
		  petition.dispose();
	}
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
	if (!textureSolved)
	{
	  return;
	}
  
	if (tile.isTextureSolved())
	{
	  return;
	}
  
	LeveledTexturedMesh ancestorMesh = getMesh(ancestorTile);
	if (ancestorMesh == null)
	{
	  return;
	}
  
	final IGLTextureId glTextureId = ancestorMesh.getTopLevelGLTextureId();
	if (glTextureId == null)
	{
	  return;
	}
  
	LeveledTexturedMesh tileMesh = getMesh(tile);
	if (tileMesh == null)
	{
	  return;
	}
  
	final int level = tile.getLevel() - ancestorTile.getLevel() - _parameters._topLevel;
	_texturesHandler.retainGLTextureId(glTextureId);
	if (!tileMesh.setGLTextureIdForLevel(level, glTextureId))
	{
	  _texturesHandler.releaseGLTextureId(glTextureId);
	}
  }

  public final IGLTextureId getTopLevelGLTextureIdForTile(Tile tile)
  {
	LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();
  
	return (mesh == null) ? null : mesh.getTopLevelGLTextureId();
  }

  public final void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet)
  {
	if (layerSet != null)
	{
	  layerSet.onTerrainTouchEvent(ec, position, tile);
	}
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
	if (builderHolder != null)
	{
	  builderHolder.get().cancel();
	  builderHolder.get().cleanMesh();
	}
	else
	{
	  if (mesh != null)
	  {
		ILogger.instance().logInfo("break (point) on me 5\n");
	  }
	}
  }

}







public enum PetitionStatus
{
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PetitionStatus forValue(int value)
	{
		return values()[value];
	}
}


public class BuilderDownloadStepDownloadListener implements IImageDownloadListener
{
  private TileTextureBuilder _builder;
  private final int _position;
  public BuilderDownloadStepDownloadListener(TileTextureBuilder builder, int position)
  //_onDownload(0),
  //_onError(0),
  //_onCancel(0)
  {
	  _builder = builder;
	  _position = position;
	_builder._retain();
  }

  public final void onDownload(URL url, IImage image)
  {
	//  _onDownload++;
	_builder.stepDownloaded(_position, image);
  }

  public final void onError(URL url)
  {
	//  _onError++;
	_builder.stepCanceled(_position);
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
  }

  public final void onCancel(URL url)
  {
	//  _onCancel++;
	_builder.stepCanceled(_position);
  }

  public void dispose()
  {
	//  testState();
  
	if (_builder != null)
	{
	  _builder._release();
	}
  }

}


public class LTMInitializer extends LazyTextureMappingInitializer
{
  private final Tile _tile;
  private final Tile _ancestor;

  private MutableVector2D _scale = new MutableVector2D();
  private MutableVector2D _translation = new MutableVector2D();

  private IFloatBuffer _texCoords;

  public LTMInitializer(Tile tile, Tile ancestor, IFloatBuffer texCoords)
  {
	  _tile = tile;
	  _ancestor = ancestor;
	  _texCoords = texCoords;
	  _scale = new MutableVector2D(1,1);
	  _translation = new MutableVector2D(0,0);

  }

  public void dispose()
  {

  }

  public final void initialize()
  {
	// The default scale and translation are ok when (tile == _ancestor)
	if (_tile != _ancestor)
	{
	  final Sector tileSector = _tile.getSector();
	  final Sector ancestorSector = _ancestor.getSector();

	  _scale = tileSector.getScaleFactor(ancestorSector).asMutableVector2D();
	  _translation = tileSector.getTranslationFactor(ancestorSector).asMutableVector2D();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MutableVector2D getScale() const
  public final MutableVector2D getScale()
  {
	return _scale;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MutableVector2D getTranslation() const
  public final MutableVector2D getTranslation()
  {
	return _translation;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTexCoords() const
  public final IFloatBuffer getTexCoords()
  {
	return _texCoords;
  }

}


public class TileTextureBuilderHolder implements ITexturizerData
{
  private TileTextureBuilder _builder;

  public TileTextureBuilderHolder(TileTextureBuilder builder)
  {
	  _builder = builder;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileTextureBuilder* get() const
  public final TileTextureBuilder get()
  {
	return _builder;
  }

  public void dispose()
  {
	if (_builder != null)
	{
	  _builder._release();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTexturizerData() const
  public final boolean isTexturizerData()
  {
	return true;
  }
}


public class TileTextureBuilder extends RCObject
{
  private MultiLayerTileTexturizer _texturizer;
  private Tile _tile;

  //  const TileKey             _tileKey;

  private java.util.ArrayList<Petition> _petitions = new java.util.ArrayList<Petition>();
  private int _petitionsCount;
  private int _stepsDone;

  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
  private TexturesHandler _texturesHandler;
  private TextureBuilder _textureBuilder;
  private GL _gl;

  private final TilesRenderParameters _parameters;
  private IDownloader _downloader;

  private final Mesh _tessellatorMesh;

  private IFloatBuffer _texCoords;

  private java.util.ArrayList<PetitionStatus> _status = new java.util.ArrayList<PetitionStatus>();
  private java.util.ArrayList<Long> _requestsIds = new java.util.ArrayList<Long>();


  private boolean _finalized;
  private boolean _canceled;
  private boolean _anyCanceled;
  private boolean _alreadyStarted;

  public LeveledTexturedMesh _mesh;

  public TileTextureBuilder(MultiLayerTileTexturizer texturizer, RenderContext rc, LayerSet layerSet, TilesRenderParameters parameters, IDownloader downloader, Tile tile, Mesh tessellatorMesh, IFloatBuffer texCoords)
  //_tileKey(tile->getKey()),
  {
	  _texturizer = texturizer;
	  _factory = rc.getFactory();
	  _texturesHandler = rc.getTexturesHandler();
	  _textureBuilder = rc.getTextureBuilder();
	  _gl = rc.getGL();
	  _parameters = parameters;
	  _downloader = downloader;
	  _tile = tile;
	  _tessellatorMesh = tessellatorMesh;
	  _stepsDone = 0;
	  _anyCanceled = false;
	  _mesh = null;
	  _texCoords = texCoords;
	  _finalized = false;
	  _canceled = false;
	  _alreadyStarted = false;
	_petitions = layerSet.createTileMapPetitions(rc, tile, parameters._tileTextureWidth, parameters._tileTextureHeight);

	_petitionsCount = _petitions.size();

	for (int i = 0; i < _petitionsCount; i++)
	{
	  _status.add(PetitionStatus.STATUS_PENDING);
	}

	_mesh = createMesh();
  }

  public final void start()
  {
	if (_canceled)
	{
	  return;
	}
	if (_alreadyStarted)
	{
	  return;
	}
	_alreadyStarted = true;

	if (_tile == null)
	{
	  return;
	}

	for (int i = 0; i < _petitionsCount; i++)
	{
	  final Petition petition = _petitions.get(i);

	  final long priority = (_parameters._incrementalTileQuality ? 1000 - _tile.getLevel() : _tile.getLevel());

	  final long requestId = _downloader.requestImage(new URL(petition.getURL()), priority, new BuilderDownloadStepDownloadListener(this, i), true);

	  _requestsIds.add(requestId);
	}
  }

  public void dispose()
  {
	if (!_finalized && !_canceled)
	{
	  cancel();
	}

	deletePetitions();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: RectangleD* getImageRectangleInTexture(const Sector& wholeSector, const Sector& imageSector, int textureWidth, int textureHeight) const
  public final RectangleD getImageRectangleInTexture(Sector wholeSector, Sector imageSector, int textureWidth, int textureHeight)
  {
	final Vector2D lowerFactor = wholeSector.getUVCoordinates(imageSector.lower());

	final double widthFactor = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
	final double heightFactor = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());

	return new RectangleD(lowerFactor._x * textureWidth, (1.0 - lowerFactor._y) * textureHeight, widthFactor * textureWidth, heightFactor * textureHeight);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void composeAndUploadTexture() const
  public final void composeAndUploadTexture()
  {
	final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	final java.util.ArrayList<RectangleD> rectangles = new java.util.ArrayList<RectangleD>();
	String textureId = _tile.getKey().tinyDescription();

	final int textureWidth = _parameters._tileTextureWidth;
	final int textureHeight = _parameters._tileTextureHeight;

	final Sector tileSector = _tile.getSector();

	for (int i = 0; i < _petitionsCount; i++)
	{
	  final Petition petition = _petitions.get(i);
	  IImage image = petition.getImage();

	  if (image != null)
	  {
		images.add(image);

		rectangles.add(getImageRectangleInTexture(tileSector, petition.getSector(), textureWidth, textureHeight));

		textureId += petition.getURL().getPath();
		textureId += "_";
	  }
	}

	if (images.size() > 0)
	{
	  //        int __TESTING_mipmapping;
	  final boolean isMipmap = false;

	  IImage image = _textureBuilder.createTextureFromImages(_gl, _factory, images, rectangles, textureWidth, textureHeight);

	  final IGLTextureId glTextureId = _texturesHandler.getGLTextureId(image, GLFormat.rgba(), textureId, isMipmap);

	  if (glTextureId != null)
	  {
		if (!_mesh.setGLTextureIdForLevel(0, glTextureId))
		{
		  _texturesHandler.releaseGLTextureId(glTextureId);
		}
	  }

	  if (image != null)
		  image.dispose();
	}


  }

  public final void finalize()
  {
	if (!_finalized)
	{
	  _finalized = true;

	  if (!_canceled && (_tile != null) && (_mesh != null))
	  {
		composeAndUploadTexture();
	  }

	  _tile.setTextureSolved(true);
	}
  }

  public final void deletePetitions()
  {
	for (int i = 0; i < _petitionsCount; i++)
	{
	  Petition petition = _petitions.get(i);
	  if (petition != null)
		  petition.dispose();
	}
	_petitions.clear();
	_petitionsCount = 0;
  }

  public final void stepDone()
  {
	_stepsDone++;

	if (_stepsDone == _petitionsCount)
	{
	  if (_anyCanceled)
	  {
		ILogger.instance().logInfo("Completed with cancelation\n");
	  }

	  finalize();
	}
  }

  public final void cancel()
  {
	if (_canceled)
	{
	  return;
	}

	_canceled = true;

	if (!_finalized)
	{
	  for (int i = 0; i < _requestsIds.size(); i++)
	  {
		final long requestId = _requestsIds.get(i);
		_downloader.cancelRequest(requestId);
	  }
	}
	_requestsIds.clear();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isCanceled() const
  public final boolean isCanceled()
  {
	return _canceled;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void checkIsPending(int position) const
  public final void checkIsPending(int position)
  {
	if (_status.get(position) != PetitionStatus.STATUS_PENDING)
	{
	  ILogger.instance().logError("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n", position, _status.get(position));
	}
  }

  public final void stepDownloaded(int position, IImage image)
  {
	if (_canceled)
	{
	  return;
	}
	checkIsPending(position);

	_status.set(position, PetitionStatus.STATUS_DOWNLOADED);
	_petitions.get(position).setImage(image.shallowCopy());

	stepDone();
  }

  public final void stepCanceled(int position)
  {
	if (_canceled)
	{
	  return;
	}
	checkIsPending(position);

	_anyCanceled = true;

	_status.set(position, PetitionStatus.STATUS_CANCELED);

	stepDone();
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
  SuppressWarnings("null") LeveledTexturedMesh* createMesh() const
  {
//#else
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LeveledTexturedMesh* createMesh() const
  public final LeveledTexturedMesh createMesh()
  {
//#endif
	java.util.ArrayList<LazyTextureMapping> mappings = new java.util.ArrayList<LazyTextureMapping>();

	Tile ancestor = _tile;
	boolean fallbackSolved = false;
	while (ancestor != null)
	{
	  LazyTextureMapping mapping;
	  if (fallbackSolved)
	  {
		mapping = null;
	  }
	  else
	  {
		mapping = new LazyTextureMapping(new LTMInitializer(_tile, ancestor, _texCoords), _texturesHandler, false, false);
	  }

	  if (ancestor != _tile)
	  {
		if (!fallbackSolved)
		{
		  final IGLTextureId glTextureId = _texturizer.getTopLevelGLTextureIdForTile(ancestor);
		  if (glTextureId != null)
		  {
			_texturesHandler.retainGLTextureId(glTextureId);
			mapping.setGLTextureId(glTextureId);
			fallbackSolved = true;
		  }
		}
	  }
	  else
	  {
		if (mapping.getGLTextureId() != null)
		{
		  ILogger.instance().logInfo("break (point) on me 3\n");
		}
	  }

	  mappings.add(mapping);
	  ancestor = ancestor.getParent();
	}

	if (mappings.size() != _tile.getLevel() + 1)
	{
	  ILogger.instance().logInfo("pleae break (point) me\n");
	}

	return new LeveledTexturedMesh(_tessellatorMesh, false, mappings);
  }

  public final LeveledTexturedMesh getMesh()
  {
	return _mesh;
  }

  public final void cleanMesh()
  {
	if (_mesh != null)
	{
	  _mesh = null;
	}
  }

  public final void cleanTile()
  {
	if (_tile != null)
	{
	  _tile = null;
	}
  }

}

public class BuilderStartTask extends FrameTask
{
  private TileTextureBuilder _builder;

  public BuilderStartTask(TileTextureBuilder builder)
  {
	  _builder = builder;
	_builder._retain();
  }

  public void dispose()
  {
	_builder._release();
  }

  public final void execute(RenderContext rc)
  {
	_builder.start();
  }

  public final boolean isCanceled(RenderContext rc)
  {
	return false;
  }
}

public class TopTileDownloadListener implements IImageDownloadListener
{
  private MultiLayerTileTexturizer _texturizer;

  public TopTileDownloadListener(MultiLayerTileTexturizer texturizer)
  {
	  _texturizer = texturizer;
  }

  public void dispose()
  {

  }

  public final void onDownload(URL url, IImage image)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onError(URL url)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
  }

  public final void onCancel(URL url)
  {
	_texturizer.countTopTileRequest();
  }

}
