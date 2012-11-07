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
  
	String authentication = json.asObject().getAsString("authenticationResultCode").value();
	if (authentication.compareTo("ValidCredentials")!=0)
	{
	  ILogger.instance().logError("Could not validate against Bing. Please check your key!");
	}
	else
	{
	  JSONObject data = json.asObject().getAsArray("resourceSets").getAsObject(0).getAsArray("resources").getAsObject(0);
  
	  JSONArray subDomArray = data.getAsArray("imageUrlSubdomains");
	  java.util.ArrayList<String> subdomains = new java.util.ArrayList<String>();
	  int numSubdomains = subDomArray.size();
	  for (int i = 0; i<numSubdomains; i++)
	  {
		subdomains.add(subDomArray.getAsString(i).value());
	  }
	  _bingLayer.setSubDomains(subdomains);
  
  
	  String tileURL = data.getAsString("imageUrl").value();
  
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