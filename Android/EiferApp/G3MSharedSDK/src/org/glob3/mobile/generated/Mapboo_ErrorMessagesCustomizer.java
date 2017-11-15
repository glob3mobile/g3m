package org.glob3.mobile.generated; 
public class Mapboo_ErrorMessagesCustomizer implements ErrorMessagesCustomizer
{
  private MapBooOLDBuilder _mbBuilder;

  public Mapboo_ErrorMessagesCustomizer(MapBooOLDBuilder mbBuilder)
  {
    _mbBuilder = mbBuilder;
  }

  public void dispose()
  {
  }

  public final java.util.ArrayList<String> customize(java.util.ArrayList<String> errors)
  {
    java.util.ArrayList<String> customizedErrorMessages = new java.util.ArrayList<String>();
    final IStringUtils stringUtils = IStringUtils.instance();
    final int errorsSize = errors.size();

    final String appNotFound = "Invalid request: Application #" + _mbBuilder.getApplicationId() + " not found";

    for (int i = 0; i < errorsSize; i++)
    {
      String error = errors.get(i);
      if (stringUtils.beginsWith(error, appNotFound))
      {
        customizedErrorMessages.add("Oops, application not found!");
        break;
      }
      customizedErrorMessages.add(error);
    }

    return customizedErrorMessages;
  }
}