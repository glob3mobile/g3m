package org.glob3.mobile.generated; 
public class TilePetitions implements IDownloadListener
{

  private final int _level;
  private final int _row;
  private final int _column;
  private final Sector _tileSector ;

  private java.util.ArrayList<Petition> _petitions = new java.util.ArrayList<Petition>();

  private int _texID; //TEXTURE ID ONCE IS FINISHED

  private int _downloadsCounter;
  private int _errorsCounter;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TilePetitions(TilePetitions that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Rectangle* getImageRectangleInTexture(const Sector& wholeSector, const Sector& imageSector, int texWidth, int texHeight) const
  private Rectangle getImageRectangleInTexture(Sector wholeSector, Sector imageSector, int texWidth, int texHeight)
  {
	Vector2D pos = wholeSector.getUVCoordinates(imageSector.lower().latitude(), imageSector.lower().longitude());
  
	double width = wholeSector.getDeltaLongitude().degrees() / imageSector.getDeltaLongitude().degrees();
	double height = wholeSector.getDeltaLatitude().degrees() / imageSector.getDeltaLatitude().degrees();
  
  
  
	Rectangle r = new Rectangle(pos.x() * texWidth, pos.y() * texHeight, width * texWidth, height * texHeight);
	return r;
  }

  private Petition getPetition(int i)
  {
	  return _petitions.get(i);
  }

  private int getNumPetitions()
  {
	  return _petitions.size();
  }


  public TilePetitions(int level, int row, int column, Sector sector, java.util.ArrayList<Petition> petitions)
  {
	  _level = level;
	  _row = row;
	  _column = column;
	  _tileSector = new Sector(sector);
	  _downloadsCounter = 0;
	  _errorsCounter = 0;
	  _petitions = petitions;
	  _texID = -1;
	removeUnnecesaryPetitions();
  }

  public void dispose()
  {
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i) != null)
		  _petitions.get(i).dispose();
	}
  }

  public final void requestToNet(Downloader downloader, int priority)
  {
	for (int i = 0; i < getNumPetitions(); i++)
	{
	  Petition pet = getPetition(i);
	  if (!pet.isArrived())
	  {
		final URL url = new URL(pet.getURL());
		int id = downloader.request(url, priority, this);
		pet.setDownloadID(id);
	  }
	}
  }
  public final void requestToCache(Downloader downloader)
  {
	for (int i = 0; i < getNumPetitions(); i++)
	{
	  Petition pet = getPetition(i);
	  if (!pet.isArrived())
	  {
		final String url = pet.getURL();
		ByteBuffer bb = downloader.getByteBufferFromCache(url);
		if (bb != null)
		{
		  pet.setByteBuffer(bb);
		}
	  }
	}
  }
  public final void cancelPetitions(Downloader downloader)
  {
	for (int i = 0; i < _petitions.size(); i++)
	{
	  int id = _petitions.get(i).getDownloadID();
	  if (id > -1)
		  downloader.cancelRequest(id);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getLevel() const
  public final int getLevel()
  {
	return _level;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getRow() const
  public final int getRow()
  {
	return _row;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getColumn() const
  public final int getColumn()
  {
	return _column;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return _tileSector;
  }

  public final void createTexture(TexturesHandler texHandler, IFactory factory, int width, int height)
  {
	if (allFinished())
	{
	  //Creating images (opaque one must be the first)
	  final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	  java.util.ArrayList<Rectangle> rectangles = new java.util.ArrayList<Rectangle>();
	  for (int i = 0; i < getNumPetitions(); i++)
	  {
		final ByteBuffer bb = getPetition(i).getByteBuffer();
		IImage im = factory.createImageFromData(bb);
		if (im != null)
		{
		  images.add(im);
		  Rectangle rec = getImageRectangleInTexture(_tileSector, getPetition(i).getSector(), width, height);
		  rectangles.add(rec);
		}
	  }
  
	  //Creating the texture
	  final String url = getPetitionsID();
	  _texID = texHandler.getTextureId(images, url, width, height);
  
	  //RELEASING MEMORY
	  for (int i = 0; i < _petitions.size(); i++)
	  {
		_petitions.get(i).releaseData();
	  }
	  for (int i = 0; i < images.size(); i++)
	  {
		factory.deleteImage(images.get(i));
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTexID() const
  public final int getTexID()
  {
	  return _texID;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getPetitionsID() const
  public final String getPetitionsID()
  {
	String id;
	for (int j = 0; j < _petitions.size(); j++)
	{
	  if (j > 0)
	  {
		id += "__";
	  }
	  id += _petitions.get(j).getURL();
	}
	return id;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean allFinished() const
  public final boolean allFinished()
  {
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (!_petitions.get(i).isArrived())
	  {
		return false;
	  }
	}
	return true;
  }

  public final void onDownload(Response response)
  {
	_downloadsCounter++;
  
	String url = response.getURL().getPath();
  
	for (int j = 0; j < _petitions.size(); j++)
	{
	  if (url.equals(_petitions.get(j).getURL()))
	  {
		//STORING PIXEL DATA FOR RECEIVED URL
  		ByteBuffer bb = new ByteBuffer(response.getByteBuffer());
		_petitions.get(j).setByteBuffer(bb);
	  }
	}
  }
  public final void onError(Response e)
  {
	_errorsCounter++;
  }
  public final void onCancel(URL url)
  {
	_errorsCounter++;
  }

  public final void removeUnnecesaryPetitions()
  {
	//For each opaque Bbox, we delete every covered request beneath
	java.util.ArrayList<Petition> visiblePetitions = new java.util.ArrayList<Petition>();
  
	for(int i = 0; i < _petitions.size(); i++)
	{
	  boolean isVisible = true;
	  for (int j = i+1; j < _petitions.size(); j++)
	  {
		if (!_petitions.get(j).isTransparent() && _petitions.get(j).getSector().fullContains(_petitions.get(i).getSector()))
		{
		  isVisible = false;
		  break;
		}
	  }
  
	  if (isVisible)
	  {
		visiblePetitions.add(_petitions.get(i));
	  }
	}
  
	_petitions = visiblePetitions;
  }

}