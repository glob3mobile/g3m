package org.glob3.mobile.generated; 
public class Downloader implements IDownloadListener
{
  private final IStorage _storage; //CACHE
  private final int _maxSimultaneous; //MAX NUMBER OF SIMOULTANEOUS DOWNLOADS
  private final INetwork _network;

  private java.util.ArrayList<Download> _petitions = new java.util.ArrayList<Download>();
  private int _simultaneousDownloads;

  private void startDownload()
  {
	if (_maxSimultaneous > _simultaneousDownloads) //If we are able to throw more dowloads
	{
  
	  //Selecting download
	  int maxPrior = -99999999;
	  int downloadIndex = -1;
	  for (int i = 0; i < _petitions.size(); i++)
	  {
		if (_petitions.get(i)._priority > maxPrior)
		{
		  maxPrior = _petitions.get(i)._priority;
		  downloadIndex = i;
		}
	  }
	  if (downloadIndex < 0)
		  return;
  
	  //Downloading
	  _network.request(_petitions.get(downloadIndex)._url, this);
  
	  //One more in the net
	  _simultaneousDownloads++;
	}
  }

  public Downloader(IStorage storage, int maxSimultaneous, INetwork net)
  {
	  _storage = storage;
	  _maxSimultaneous = maxSimultaneous;
	  _network = net;
	  _simultaneousDownloads = 0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ByteBuffer* getByteBufferFromCache(const String& urlOfFile) const
  public final ByteBuffer getByteBufferFromCache(String urlOfFile)
  {
	return _storage.read(urlOfFile);
  }

  public final int request(URL url, int priority, IDownloadListener listener)
  {
	String urlOfFile = url.getPath();
  
	//First we check in storage
	if (_storage.contains(urlOfFile))
	{
	  ByteBuffer bb = _storage.read(urlOfFile);
	  Response r = new Response(url, bb);
	  if (listener != null)
	  {
		listener.onDownload(r);
	  }
	  bb = null;
	  return -1;
	}
  
	int currentID = -1;
	//We look for repeated petitions
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i)._url.equals(urlOfFile)) //IF WE FOUND THE SAME PETITION
	  {
		if (priority > _petitions.get(i)._priority) //MAX PRIORITY
		{
		  _petitions.get(i)._priority = priority;
		}
		currentID = _petitions.get(i).addListener(listener); //NEW LISTENER
	  }
	}
  
	if (currentID == -1)
	{
	  //NEW DOWNLOAD
	  Download d = new Download(urlOfFile, priority);
	  currentID = d.addListener(listener);
	  _petitions.add(d);
	}
  
	//When a new petition comes, we try to throw a download
	startDownload();
  
	return currentID;
  }

  public final void onDownload(Response e)
  {
	//Saving on storage
  	_storage.save(e.getURL().getPath(), e.getByteBuffer());
  
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i)._url.equals(e.getURL().getPath())) //RECEIVED RESPONSE
	  {
		Download pet = _petitions.get(i);
		for (int j = 0; j < pet._listeners.size(); j++)
		{
		  IDownloadListener dl = pet._listeners.get(j)._listener;
		  if (dl != null)
		  {
			dl.onDownload(e);
		  }
		}
  	  _petitions.remove(i);
  
		break;
	  }
	}
  
	//One less in the net
	_simultaneousDownloads--;
	startDownload(); //CHECK IF WE CAN THROW A NEW PETITION TO THE NET
  }

  public final void onError(Response e)
  {
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i)._url.equals(e.getURL().getPath())) //RECEIVED RESPONSE
	  {
		Download pet = _petitions.get(i);
		for (int j = 0; j < pet._listeners.size(); j++)
		{
		  IDownloadListener dl = pet._listeners.get(j)._listener;
		  if (dl != null)
		  {
			dl.onError(e);
		  }
		}
  	  _petitions.remove(i);
		break;
	  }
	}
  
	//One less in the net
	_simultaneousDownloads--;
	startDownload(); //CHECK IF WE CAN THROW A NEW PETITION TO THE NET
  }

  public final void onCancel(URL url)
  {
  }

  public final void cancelRequest(int id)
  {
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i).cancel(id))
	  {
		if (_petitions.get(i)._listeners.size() < 1)
		{
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		  _petitions.erase(_petitions.iterator() + i);
		}
		break;
	  }
	}
  }

}