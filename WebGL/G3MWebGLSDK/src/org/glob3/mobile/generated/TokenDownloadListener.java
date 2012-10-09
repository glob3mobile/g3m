package org.glob3.mobile.generated; 
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
  
	  int TODO_read_subdomains_and_somehow_choose_one;
  
  
	  //set language
	  rawTileURL = IStringUtils.instance().replaceSubstring(rawTileURL, "{culture}", _bingLayer.getLocale());
  
	  String tileURL = IStringUtils.instance().replaceSubstring(rawTileURL, "{subdomain}", "t0");
	  //std::cout<<"final URL: "<<tileURL<<"\n";
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