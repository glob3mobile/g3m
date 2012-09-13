package org.glob3.mobile.generated; 
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

	  //const long priority = _tile->getLevel() * 1000000 + _tile->getRow() * 1000 + _tile->getColumn();
	  final long priority = _tile.getLevel();

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
//ORIGINAL LINE: Rectangle* getImageRectangleInTexture(const Sector& wholeSector, const Sector& imageSector, int textureWidth, int textureHeight) const
  public final Rectangle getImageRectangleInTexture(Sector wholeSector, Sector imageSector, int textureWidth, int textureHeight)
  {
	final Vector2D lowerFactor = wholeSector.getUVCoordinates(imageSector.lower());

	final double widthFactor = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
	final double heightFactor = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());

	return new Rectangle(lowerFactor.x() * textureWidth, (1.0 - lowerFactor.y()) * textureHeight, widthFactor * textureWidth, heightFactor * textureHeight);
  }

  public final void finalize()
  {
	if (_finalized)
	{
	  return;
	}

	_finalized = true;

	if (!_canceled && (_tile != null) && (_mesh != null))
	{
	  final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	  final java.util.ArrayList<Rectangle> rectangles = new java.util.ArrayList<Rectangle>();
	  String petitionsID = _tile.getKey().tinyDescription();

	  final int textureWidth = _parameters._tileTextureWidth;
	  final int textureHeight = _parameters._tileTextureHeight;

	  final Sector tileSector = _tile.getSector();

	  for (int i = 0; i < _petitionsCount; i++)
	  {
		Petition petition = _petitions.get(i);
		IImage image = petition.getImage();

		if (image != null)
		{
		  images.add(image);

		  final Sector petitionSector = petition.getSector();

		  Rectangle rectangle = getImageRectangleInTexture(tileSector, petitionSector, textureWidth, textureHeight);
		  rectangles.add(rectangle);

		  petitionsID += petition.getURL().getPath();
		  petitionsID += "_";
		}
	  }

	  if (images.size() > 0)
	  {
		//        int __TESTING_mipmapping;
		final boolean isMipmap = false;

		IImage image = _textureBuilder.createTextureFromImages(_gl, _factory, images, rectangles, textureWidth, textureHeight);

		GLTextureId glTextureId = _texturesHandler.getGLTextureId(image, GLFormat.RGBA, petitionsID, isMipmap);

		if (glTextureId.isValid())
		{
		  if (!_mesh.setGLTextureIdForLevel(0, glTextureId))
		  {
			_texturesHandler.releaseGLTextureId(glTextureId);
		  }
		}

	  }

	}

	_tile.setTextureSolved(true);
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
		System.out.print("Completed with cancelation\n");
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
	  System.out.printf("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n", position, _status.get(position));
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
	_petitions.get(position).setImage(image.copy());

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LeveledTexturedMesh* createMesh() const
  public final LeveledTexturedMesh createMesh()
  {
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
		mapping = new LazyTextureMapping(new LTMInitializer(_tile, ancestor, _texCoords), _texturesHandler, false);
	  }

	  if (ancestor != _tile)
	  {
		if (!fallbackSolved)
		{
		  final GLTextureId glTextureId = _texturizer.getTopLevelGLTextureIdForTile(ancestor);
		  if (glTextureId.isValid())
		  {
			_texturesHandler.retainGLTextureId(glTextureId);
			mapping.setGLTextureId(glTextureId);
			fallbackSolved = true;
		  }
		}
	  }
	  else
	  {
		if (mapping.getGLTextureId().isValid())
		{
		  System.out.print("break (point) on me 3\n");
		}
	  }

	  mappings.add(mapping);
	  ancestor = ancestor.getParent();
	}

	if (mappings.size() != _tile.getLevel() + 1)
	{
	  System.out.print("pleae break (point) me\n");
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