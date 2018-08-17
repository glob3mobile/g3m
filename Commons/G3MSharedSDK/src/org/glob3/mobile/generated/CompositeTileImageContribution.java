package org.glob3.mobile.generated;//
//  CompositeTileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//

//
//  CompositeTileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//




public class CompositeTileImageContribution extends TileImageContribution
{

  public static class ChildContribution
  {
	public final int _childIndex;
	public final TileImageContribution _contribution;

	public ChildContribution(int childIndex, TileImageContribution contribution)
	{
		_childIndex = childIndex;
		_contribution = contribution;
	}

	public void dispose()
	{
	  TileImageContribution.releaseContribution(_contribution);
	}
  }


  public static TileImageContribution create(java.util.ArrayList<const ChildContribution> contributions)
  {
	if (contributions.isEmpty())
	{
	  return null;
	}
  
	//if first contribution is opaque, then composite is opaque.
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final ChildContribution firsChild = contributions.get(0);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final ChildContribution firsChild = contributions.get(0);
//#endif
  
	return new CompositeTileImageContribution(contributions, firsChild._contribution.isOpaque());
  }


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final java.util.ArrayList<const ChildContribution> _contributions = new java.util.ArrayList<const ChildContribution>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final java.util.ArrayList<ChildContribution> _contributions = new internal();
//#endif

  private CompositeTileImageContribution(java.util.ArrayList<const ChildContribution> contributions, boolean transparent)
  {
	  super(transparent, 1.0f);
	  _contributions = contributions;


  }


//  CompositeTileImageContribution(const std::vector<const ChildContribution*>& contributions,
//                                 const Sector& sector,
//                                 bool isFullCoverage,
//                                 bool isTransparent,
//                                 float alpha) :
//  TileImageContribution(sector, isFullCoverage, isTransparent, alpha),
//  _contributions(contributions)
//  {
//    
//    
//  }

  public void dispose()
  {
	final int contributionsSize = _contributions.size();
	for (int i = 0; i < contributionsSize; i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  final ChildContribution contribution = _contributions.get(i);
	  if (contribution != null)
		  contribution.dispose();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  final ChildContribution contribution = _contributions.get(i);
	  contribution.dispose();
//#endif
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const int size() const
  public final int size()
  {
	return _contributions.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ChildContribution* get(int index) const
  public final ChildContribution get(int index)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	return _contributions.get(index);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return _contributions.get(index);
//#endif
  }
}
