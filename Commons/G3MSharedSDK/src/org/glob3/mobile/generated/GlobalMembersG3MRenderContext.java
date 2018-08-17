package org.glob3.mobile.generated;public class GlobalMembersG3MRenderContext
{



	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	//#if C_CODE
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	//#if C_CODE
	public static boolean MyDataSortPredicate(OrderedRenderable or1, OrderedRenderable or2)
	{
		if (or1 == null || or2 == null)
		{
			ILogger.instance().logError("Problem at MyDataSortPredicate");
			return false;
		}
	  return (or1.squaredDistanceFromEye() >= or2.squaredDistanceFromEye());
	}
	//#endif
	//#endif
}
