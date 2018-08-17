package org.glob3.mobile.generated;import java.util.*;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
public class SortTilesClass
{
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
  boolean operator ()(Tile i, Tile j)
  {
	final int rowI = i._row;
	final int rowJ = j._row;

	if (rowI < rowJ)
	{
	  return true;
	}
	if (rowI > rowJ)
	{
	  return false;
	}

	return (i._column < j._column);
  }
}
//#endif
