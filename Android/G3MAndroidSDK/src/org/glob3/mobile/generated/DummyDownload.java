package org.glob3.mobile.generated; 
//
//  DummyDownload.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class DummyDownload implements IDownloadListener
{

  private IFactory _factory;
  private IStorage _fss;
  private Downloader _downloader;


  public DummyDownload(IFactory fac, IStorage storage)
  {
	  _factory = fac;
	  _fss = storage;
	_downloader = new Downloader(_fss, 5, fac.createNetwork());
  }

  public final void run()
  {
	URL url = new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?Request=GetCapabilities&SERVICE=WMS");
	URL url2 = new URL("http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?request=capabilities");

	URL urlPNG = new URL("http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/300px-PNG_transparency_demonstration_1.png");

	_downloader.request(url, 10, this);

	_downloader.request(url, 10, this);

	_downloader.request(url2, 20, this);

	_downloader.request(new URL("....."), 15, this); //THIS SHOULD PRODUCE AN ERROR
	_downloader.request(url, 30, this);

	_downloader.request(urlPNG, 60, this);
  }

	public final void runSqlite(String root, String filename, IStorage fssAux)
	{
		System.out.printf("\nFileName: %s;", filename);
		if (_fss.contains(filename))
		{
		  ByteBuffer bb = _fss.read(filename);
		  String resp = _factory.stringFromUTF8(bb.getData());
		  System.out.printf("\nFileName: %s;\nData: %s;\nDataLength:%i;\n\n",(root+filename), resp, bb.getLength());
		  fssAux.save(("_(1)" + filename), bb);
		  if (bb != null)
			  bb.dispose();
		}
		else
		{
		  ByteBuffer bb = fssAux.read(filename);
		  if(bb.getData() != null)
		  {
			fssAux.save(("_withoutsaveinsqlite_" + filename), bb);
			_fss.save(filename, bb);
		  }
		  if (bb != null)
			  bb.dispose();
		}
	}

  public final void onDownload(Response response)
  {
	String urlPNG = "http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/300px-PNG_transparency_demonstration_1.png";
	if (urlPNG.equals(response.getURL().getPath()))
	{
	  //DECOMPRESSING PNG IMAGE
	  IImage image = _factory.createImageFromData(response.getByteBuffer());
	  if (image != null)
	  {
		System.out.print("Image PNG readed");
	  }
	}
	else
	{
	  String resp = _factory.stringFromUTF8(response.getByteBuffer().getData());

	  resp = resp.substring(0,10);
	  System.out.printf("GETTING RESPONSE %s\n", resp);

	}


  }

  public final void onError(Response response)
  {
	System.out.printf("GETTING ERROR IN URL: %s\n", response.getURL().getPath());
  }

  public final void onCancel(URL url)
  {
	System.out.printf("GETTING CANCELR IN URL: %s\n", url.getPath());
  }

}