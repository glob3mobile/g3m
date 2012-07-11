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

  public final void request(String urlOfFile, int priority, IDownloadListener listener)
  {
  
	//First we check in storage
	if (_storage.contains(urlOfFile))
	{
	  ByteBuffer bb = _storage.getByteBuffer(urlOfFile);
	  Response r = new Response(urlOfFile, bb);
	  listener.onDownload(r);
  
	  //WE MUST DELETE THE BYTE BUFFER WE HAVE CREATED
	  bb.release();
  
	  return;
	}
  
	//We look for repeated petitions
	for (int i = 0; i < _petitions.size(); i++)
	{
	  if (_petitions.get(i)._url.equals(urlOfFile)) //IF WE FOUND THE SAME PETITION
	  {
		if (priority > _petitions.get(i)._priority) //MAX PRIORITY
		{
		  _petitions.get(i)._priority = priority;
		}
		_petitions.get(i).addListener(listener); //NEW LISTENER
		return;
	  }
	}
  
	//NEW DOWNLOAD
	Download d = new Download(urlOfFile, priority, listener);
	_petitions.add(d);
  
	//When a new petition comes, we try to throw a download
	startDownload();
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
		  IDownloadListener dl = pet._listeners.get(j);
  
		  dl.onDownload(e);
		}
  	  _petitions.remove(i);
		return;
	  }
	}
  
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
		  pet._listeners.get(j).onError(e);
		}
  	  _petitions.remove(i);
		return;
	  }
	}
  }

}