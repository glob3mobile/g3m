package org.glob3.mobile.generated; 
///#include "JSONParser_iOS.hpp"




public class TokenDownloadListener implements IBufferDownloadListener
{
  private BingLayer _bingLayer;

  public TokenDownloadListener(BingLayer bingLayer)
  {
	  _bingLayer = bingLayer;
  }

  public final void onDownload(URL url, IByteBuffer buffer)
  {
  
  
	String String = buffer.getAsString();
	JSONBaseObject json = IJSONParser.instance().parse(String);
  
	String authentication = json.getObject().getObjectForKey("authenticationResultCode").getString().getValue();
	if (authentication.compareTo("ValidCredentials")!=0)
	{
	  ILogger.instance().logError("Could not validate against Bing. Please check your key!");
	}
	else
	{
	  JSONObject data = json.getObject().getObjectForKey("resourceSets").getArray().getElement(0).getObject().getObjectForKey("resources").getArray().getElement(0).getObject();
  
	  String rawTileURL = data.getObjectForKey("imageUrl").getString().getValue();
	  System.out.print("rawURL:");
	  System.out.print(rawTileURL);
	  System.out.print("\n");
  
	  int TODO_read_subdomains_and_somehow_choose_one;
  
	  //remove unneeded final part:&mkt={culture}
	  int lastValidChar = IStringUtils.instance().indexOf(rawTileURL, "&mkt");
	  rawTileURL = IStringUtils.instance().substring(rawTileURL, 0, lastValidChar);
	  System.out.print("rawURL (after cropping):");
	  System.out.print(rawTileURL);
	  System.out.print("\n");
  
  
	  String tileURL = IStringUtils.instance().replaceSubstring(rawTileURL, "{subdomain}", "t0");
	  System.out.print("With StringUtils: ");
	  System.out.print(tileURL);
	  System.out.print("\n");
	  _bingLayer.setTilePetitionString(tileURL);
  
	  IJSONParser.instance().deleteJSONData(json);
	}
  }

  public final void onError(URL url)
  {
  }

  public final void onCancel(URL url)
  {
  }

  public final void onCanceledDownload(URL url, IByteBuffer data)
  {
  }

  public void dispose()
  {
  }

}