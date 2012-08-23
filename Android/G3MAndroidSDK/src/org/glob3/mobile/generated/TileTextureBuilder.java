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

  private final TilesRenderParameters _parameters;
  private IDownloader _downloader;

  private final Mesh _tessellatorMesh;

  private final float[] _texCoords;

  private java.util.ArrayList<PetitionStatus> _status = new java.util.ArrayList<PetitionStatus>();
  private java.util.ArrayList<Integer> _requestsIds = new java.util.ArrayList<Integer>();


  private boolean _finalized;
  private boolean _canceled;
  private boolean _anyCanceled;
  private boolean _alreadyStarted;

  public LeveledTexturedMesh _mesh;

  public TileTextureBuilder(MultiLayerTileTexturizer texturizer, RenderContext rc, LayerSet layerSet, TilesRenderParameters parameters, IDownloader downloader, Tile tile, Mesh tessellatorMesh, float[] texCoords)
  //_tileKey(tile->getKey()),
  {
	  _texturizer = texturizer;
	  _factory = rc.getFactory();
	  _texturesHandler = rc.getTexturesHandler();
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
	_petitions = layerSet.createTilePetitions(rc, tile, parameters._tileTextureWidth, parameters._tileTextureHeight);

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
	  final int priority = _tile.getLevel();

	  final int requestId = _downloader.request(new URL(petition.getURL()), priority, new BuilderDownloadStepDownloadListener(this, i), true);

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
	final Vector2D pos = wholeSector.getUVCoordinates(imageSector.lower());

	final double width = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
	final double height = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());

	return new Rectangle(pos.x() * textureWidth, (1.0 - pos.y()) * textureHeight, width * textureWidth, height * textureHeight);
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
		final ByteBuffer buffer = petition.getByteBuffer();

		if (buffer != null)
		{
		  IImage image = _factory.createImageFromData(buffer);
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
	  }

	  if (images.size() > 0)
	  {
		final GLTextureID glTextureID = _texturesHandler.getGLTextureId(images, rectangles, new TextureSpec(petitionsID, textureWidth, textureHeight));
		if (glTextureID.isValid())
		{
		  if (!_mesh.setGLTextureIDForLevel(0, glTextureID))
		  {
			_texturesHandler.releaseGLTextureId(glTextureID);
		  }
		}

	  }

	  for (int i = 0; i < images.size(); i++)
	  {
		_factory.deleteImage(images.get(i));
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
		final int requestId = _requestsIds.get(i);
		_downloader.cancelRequest(requestId);
	  }
	}
	_requestsIds.clear();
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

  public final void stepDownloaded(int position, ByteBuffer buffer)
  {
	if (_canceled)
	{
	  return;
	}
	checkIsPending(position);

	_status.set(position, PetitionStatus.STATUS_DOWNLOADED);
	_petitions.get(position).setByteBuffer(buffer.copy());

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
		  final GLTextureID glTextureId = _texturizer.getTopLevelGLTextureIDForTile(ancestor);
		  if (glTextureId.isValid())
		  {
			_texturesHandler.retainGLTextureId(glTextureId);
			mapping.setGLTextureID(glTextureId);
			fallbackSolved = true;
		  }
		}
	  }
	  else
	  {
		if (mapping.getGLTextureID().isValid())
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