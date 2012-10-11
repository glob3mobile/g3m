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
  
	  JSONArray subDomArray = data.getObjectForKey("imageUrlSubdomains").getArray();
	  java.util.ArrayList<String> subdomains = new java.util.ArrayList<String>();
	  int numSubdomains = subDomArray.getSize();
	  for (int i = 0; i<numSubdomains; i++)
	  {
		subdomains.add(subDomArray.getElement(i).getString().getValue());
	  }
	  _bingLayer.setSubDomains(subdomains);
  
  
	  String tileURL = data.getObjectForKey("imageUrl").getString().getValue();
  
	  //set language
	  tileURL = IStringUtils.instance().replaceSubstring(tileURL, "{culture}", _bingLayer.getLocale());
  
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