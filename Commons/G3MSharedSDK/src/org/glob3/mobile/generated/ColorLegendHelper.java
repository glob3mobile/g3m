package org.glob3.mobile.generated; 
//
//  ColorLegendHelper.cpp
//  EIFER App
//
//  Created by Jose Miguel SN on 19/5/16.
//
//

//
//  ColorLegendHelper.hpp
//  EIFER App
//
//  Created by Jose Miguel SN on 19/5/16.
//
//




public class ColorLegendHelper
{


  //  //Transformed from C# [http://www.gal-systems.com/2011/08/calculating-jenks-natural-breaks-in.html]
  //  static std::vector<double> getJenksBreaks(std::vector<double>& sListDouble, int sClassCount);

  //Adapted from https://github.com/schulzch/colorbrewercpp/blob/master/colorbrewer.h

  ////Transformed from C# [http://www.gal-systems.com/2011/08/calculating-jenks-natural-breaks-in.html]
  //std::vector<double> ColorLegendHelper::getJenksBreaks(std::vector<double>& sListDouble, int sClassCount)
  //{
  //  std::vector<double> pResult;
  //  for (int i = 0; i < sClassCount; i++)
  //    pResult.push_back(0);
  //  
  //  int numdata = (int)sListDouble.size();
  //  std::sort(sListDouble.begin(), sListDouble.end());
  //  //    sListDouble.Sort();
  //  
  //  double** mat1 = new double*[numdata+1];
  //  double** mat2 = new double*[numdata+1];
  //  for (int i = 0; i < numdata+1; i++) {
  //    mat1[i] = new double[sClassCount + 1];
  //    mat2[i] = new double[sClassCount + 1];
  //  }
  //  
  //  //    double* mat1 = new double[numdata + 1, sClassCount + 1];
  //  //    double* mat2 = new double[numdata + 1, sClassCount + 1];
  //  for (int i = 1; i <= sClassCount; i++)
  //  {
  //    mat1[1][i] = 1.0;
  //    mat2[1][i] = 0.0;
  //    for (int j = 2; j <= numdata; j++)
  //    {
  //      mat2[j][i] = 999999999;
  //    }
  //  }
  //  
  //  double ssd = 0;
  //  for (int rangeEnd = 2; rangeEnd <= numdata; rangeEnd++)
  //  {
  //    double sumX = 0;
  //    double sumX2 = 0;
  //    double w = 0;
  //    int dataId;
  //    for (int m = 1; m <= rangeEnd; m++)
  //    {
  //      dataId = rangeEnd - m + 1;
  //      double val = sListDouble[dataId - 1];
  //      sumX2 += val * val;
  //      sumX += val;
  //      w++;
  //      ssd = sumX2 - (sumX * sumX) / w;
  //      for (int j = 2; j <= sClassCount; j++)
  //      {
  //        if (!(mat2[rangeEnd][j] < (ssd + mat2[dataId - 1][j - 1])))
  //        {
  //          mat1[rangeEnd][j] = dataId;
  //          mat2[rangeEnd][j] = ssd + mat2[dataId - 1][j - 1];
  //        }
  //      }
  //    }
  //    mat1[rangeEnd][1] = 1;
  //    mat2[rangeEnd][1] = ssd;
  //  }
  //  
  //  pResult[sClassCount - 1] = sListDouble[numdata - 1];
  //  
  //  int k = (int)numdata;
  //  for (int j = sClassCount; j >= 2; j--)
  //  {
  //    int id = (int)(mat1[k][j]) - 2;
  //    pResult[j - 2] = sListDouble[id];
  //    k = (int)mat1[k][j] - 1;
  //  }
  //  
  //  for (int i = 0; i < numdata+1; i++) {
  //    delete[] mat1[i];
  //    delete[] mat2[i];
  //  }
  //  delete[] mat1;
  //  delete[] mat2;
  //  
  //  return pResult;
  //}
  
  
  
  public static java.util.ArrayList<String> brew(String colorName, int colorCount)
  {
    java.util.ArrayList<String> list = new java.util.ArrayList<String>();
    if (colorName.compareTo("YlGn") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#f7fcb9");
          list.add("#addd8e");
          list.add("#31a354");
          break;
        case 4:
          list.add("#ffffcc");
          list.add("#c2e699");
          list.add("#78c679");
          list.add("#238443");
          break;
        case 5:
          list.add("#ffffcc");
          list.add("#c2e699");
          list.add("#78c679");
          list.add("#31a354");
          list.add("#006837");
          break;
        case 6:
          list.add("#ffffcc");
          list.add("#d9f0a3");
          list.add("#addd8e");
          list.add("#78c679");
          list.add("#31a354");
          list.add("#006837");
          break;
        case 7:
          list.add("#ffffcc");
          list.add("#d9f0a3");
          list.add("#addd8e");
          list.add("#78c679");
          list.add("#41ab5d");
          list.add("#238443");
          list.add("#005a32");
          break;
        case 8:
          list.add("#ffffe5");
          list.add("#f7fcb9");
          list.add("#d9f0a3");
          list.add("#addd8e");
          list.add("#78c679");
          list.add("#41ab5d");
          list.add("#238443");
          list.add("#005a32");
          break;
        case 9:
          list.add("#ffffe5");
          list.add("#f7fcb9");
          list.add("#d9f0a3");
          list.add("#addd8e");
          list.add("#78c679");
          list.add("#41ab5d");
          list.add("#238443");
          list.add("#006837");
          list.add("#004529");
          break;
      }
    }
    else if (colorName.compareTo("YlGnBu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#edf8b1");
          list.add("#7fcdbb");
          list.add("#2c7fb8");
          break;
        case 4:
          list.add("#ffffcc");
          list.add("#a1dab4");
          list.add("#41b6c4");
          list.add("#225ea8");
          break;
        case 5:
          list.add("#ffffcc");
          list.add("#a1dab4");
          list.add("#41b6c4");
          list.add("#2c7fb8");
          list.add("#253494");
          break;
        case 6:
          list.add("#ffffcc");
          list.add("#c7e9b4");
          list.add("#7fcdbb");
          list.add("#41b6c4");
          list.add("#2c7fb8");
          list.add("#253494");
          break;
        case 7:
          list.add("#ffffcc");
          list.add("#c7e9b4");
          list.add("#7fcdbb");
          list.add("#41b6c4");
          list.add("#1d91c0");
          list.add("#225ea8");
          list.add("#0c2c84");
          break;
        case 8:
          list.add("#ffffd9");
          list.add("#edf8b1");
          list.add("#c7e9b4");
          list.add("#7fcdbb");
          list.add("#41b6c4");
          list.add("#1d91c0");
          list.add("#225ea8");
          list.add("#0c2c84");
          break;
        case 9:
          list.add("#ffffd9");
          list.add("#edf8b1");
          list.add("#c7e9b4");
          list.add("#7fcdbb");
          list.add("#41b6c4");
          list.add("#1d91c0");
          list.add("#225ea8");
          list.add("#253494");
          list.add("#081d58");
          break;
      }
    }
    else if (colorName.compareTo("GnBu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e0f3db");
          list.add("#a8ddb5");
          list.add("#43a2ca");
          break;
        case 4:
          list.add("#f0f9e8");
          list.add("#bae4bc");
          list.add("#7bccc4");
          list.add("#2b8cbe");
          break;
        case 5:
          list.add("#f0f9e8");
          list.add("#bae4bc");
          list.add("#7bccc4");
          list.add("#43a2ca");
          list.add("#0868ac");
          break;
        case 6:
          list.add("#f0f9e8");
          list.add("#ccebc5");
          list.add("#a8ddb5");
          list.add("#7bccc4");
          list.add("#43a2ca");
          list.add("#0868ac");
          break;
        case 7:
          list.add("#f0f9e8");
          list.add("#ccebc5");
          list.add("#a8ddb5");
          list.add("#7bccc4");
          list.add("#4eb3d3");
          list.add("#2b8cbe");
          list.add("#08589e");
          break;
        case 8:
          list.add("#f7fcf0");
          list.add("#e0f3db");
          list.add("#ccebc5");
          list.add("#a8ddb5");
          list.add("#7bccc4");
          list.add("#4eb3d3");
          list.add("#2b8cbe");
          list.add("#08589e");
          break;
        case 9:
          list.add("#f7fcf0");
          list.add("#e0f3db");
          list.add("#ccebc5");
          list.add("#a8ddb5");
          list.add("#7bccc4");
          list.add("#4eb3d3");
          list.add("#2b8cbe");
          list.add("#0868ac");
          list.add("#084081");
          break;
      }
    }
    else if (colorName.compareTo("BuGn") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e5f5f9");
          list.add("#99d8c9");
          list.add("#2ca25f");
          break;
        case 4:
          list.add("#edf8fb");
          list.add("#b2e2e2");
          list.add("#66c2a4");
          list.add("#238b45");
          break;
        case 5:
          list.add("#edf8fb");
          list.add("#b2e2e2");
          list.add("#66c2a4");
          list.add("#2ca25f");
          list.add("#006d2c");
          break;
        case 6:
          list.add("#edf8fb");
          list.add("#ccece6");
          list.add("#99d8c9");
          list.add("#66c2a4");
          list.add("#2ca25f");
          list.add("#006d2c");
          break;
        case 7:
          list.add("#edf8fb");
          list.add("#ccece6");
          list.add("#99d8c9");
          list.add("#66c2a4");
          list.add("#41ae76");
          list.add("#238b45");
          list.add("#005824");
          break;
        case 8:
          list.add("#f7fcfd");
          list.add("#e5f5f9");
          list.add("#ccece6");
          list.add("#99d8c9");
          list.add("#66c2a4");
          list.add("#41ae76");
          list.add("#238b45");
          list.add("#005824");
          break;
        case 9:
          list.add("#f7fcfd");
          list.add("#e5f5f9");
          list.add("#ccece6");
          list.add("#99d8c9");
          list.add("#66c2a4");
          list.add("#41ae76");
          list.add("#238b45");
          list.add("#006d2c");
          list.add("#00441b");
          break;
      }
    }
    else if (colorName.compareTo("PuBuGn") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#ece2f0");
          list.add("#a6bddb");
          list.add("#1c9099");
          break;
        case 4:
          list.add("#f6eff7");
          list.add("#bdc9e1");
          list.add("#67a9cf");
          list.add("#02818a");
          break;
        case 5:
          list.add("#f6eff7");
          list.add("#bdc9e1");
          list.add("#67a9cf");
          list.add("#1c9099");
          list.add("#016c59");
          break;
        case 6:
          list.add("#f6eff7");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#67a9cf");
          list.add("#1c9099");
          list.add("#016c59");
          break;
        case 7:
          list.add("#f6eff7");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#67a9cf");
          list.add("#3690c0");
          list.add("#02818a");
          list.add("#016450");
          break;
        case 8:
          list.add("#fff7fb");
          list.add("#ece2f0");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#67a9cf");
          list.add("#3690c0");
          list.add("#02818a");
          list.add("#016450");
          break;
        case 9:
          list.add("#fff7fb");
          list.add("#ece2f0");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#67a9cf");
          list.add("#3690c0");
          list.add("#02818a");
          list.add("#016c59");
          list.add("#014636");
          break;
      }
    }
    else if (colorName.compareTo("PuBu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#ece7f2");
          list.add("#a6bddb");
          list.add("#2b8cbe");
          break;
        case 4:
          list.add("#f1eef6");
          list.add("#bdc9e1");
          list.add("#74a9cf");
          list.add("#0570b0");
          break;
        case 5:
          list.add("#f1eef6");
          list.add("#bdc9e1");
          list.add("#74a9cf");
          list.add("#2b8cbe");
          list.add("#045a8d");
          break;
        case 6:
          list.add("#f1eef6");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#74a9cf");
          list.add("#2b8cbe");
          list.add("#045a8d");
          break;
        case 7:
          list.add("#f1eef6");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#74a9cf");
          list.add("#3690c0");
          list.add("#0570b0");
          list.add("#034e7b");
          break;
        case 8:
          list.add("#fff7fb");
          list.add("#ece7f2");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#74a9cf");
          list.add("#3690c0");
          list.add("#0570b0");
          list.add("#034e7b");
          break;
        case 9:
          list.add("#fff7fb");
          list.add("#ece7f2");
          list.add("#d0d1e6");
          list.add("#a6bddb");
          list.add("#74a9cf");
          list.add("#3690c0");
          list.add("#0570b0");
          list.add("#045a8d");
          list.add("#023858");
          break;
      }
    }
    else if (colorName.compareTo("BuPu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e0ecf4");
          list.add("#9ebcda");
          list.add("#8856a7");
          break;
        case 4:
          list.add("#edf8fb");
          list.add("#b3cde3");
          list.add("#8c96c6");
          list.add("#88419d");
          break;
        case 5:
          list.add("#edf8fb");
          list.add("#b3cde3");
          list.add("#8c96c6");
          list.add("#8856a7");
          list.add("#810f7c");
          break;
        case 6:
          list.add("#edf8fb");
          list.add("#bfd3e6");
          list.add("#9ebcda");
          list.add("#8c96c6");
          list.add("#8856a7");
          list.add("#810f7c");
          break;
        case 7:
          list.add("#edf8fb");
          list.add("#bfd3e6");
          list.add("#9ebcda");
          list.add("#8c96c6");
          list.add("#8c6bb1");
          list.add("#88419d");
          list.add("#6e016b");
          break;
        case 8:
          list.add("#f7fcfd");
          list.add("#e0ecf4");
          list.add("#bfd3e6");
          list.add("#9ebcda");
          list.add("#8c96c6");
          list.add("#8c6bb1");
          list.add("#88419d");
          list.add("#6e016b");
          break;
        case 9:
          list.add("#f7fcfd");
          list.add("#e0ecf4");
          list.add("#bfd3e6");
          list.add("#9ebcda");
          list.add("#8c96c6");
          list.add("#8c6bb1");
          list.add("#88419d");
          list.add("#810f7c");
          list.add("#4d004b");
          break;
      }
    }
    else if (colorName.compareTo("RdPu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fde0dd");
          list.add("#fa9fb5");
          list.add("#c51b8a");
          break;
        case 4:
          list.add("#feebe2");
          list.add("#fbb4b9");
          list.add("#f768a1");
          list.add("#ae017e");
          break;
        case 5:
          list.add("#feebe2");
          list.add("#fbb4b9");
          list.add("#f768a1");
          list.add("#c51b8a");
          list.add("#7a0177");
          break;
        case 6:
          list.add("#feebe2");
          list.add("#fcc5c0");
          list.add("#fa9fb5");
          list.add("#f768a1");
          list.add("#c51b8a");
          list.add("#7a0177");
          break;
        case 7:
          list.add("#feebe2");
          list.add("#fcc5c0");
          list.add("#fa9fb5");
          list.add("#f768a1");
          list.add("#dd3497");
          list.add("#ae017e");
          list.add("#7a0177");
          break;
        case 8:
          list.add("#fff7f3");
          list.add("#fde0dd");
          list.add("#fcc5c0");
          list.add("#fa9fb5");
          list.add("#f768a1");
          list.add("#dd3497");
          list.add("#ae017e");
          list.add("#7a0177");
          break;
        case 9:
          list.add("#fff7f3");
          list.add("#fde0dd");
          list.add("#fcc5c0");
          list.add("#fa9fb5");
          list.add("#f768a1");
          list.add("#dd3497");
          list.add("#ae017e");
          list.add("#7a0177");
          list.add("#49006a");
          break;
      }
    }
    else if (colorName.compareTo("PuRd") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e7e1ef");
          list.add("#c994c7");
          list.add("#dd1c77");
          break;
        case 4:
          list.add("#f1eef6");
          list.add("#d7b5d8");
          list.add("#df65b0");
          list.add("#ce1256");
          break;
        case 5:
          list.add("#f1eef6");
          list.add("#d7b5d8");
          list.add("#df65b0");
          list.add("#dd1c77");
          list.add("#980043");
          break;
        case 6:
          list.add("#f1eef6");
          list.add("#d4b9da");
          list.add("#c994c7");
          list.add("#df65b0");
          list.add("#dd1c77");
          list.add("#980043");
          break;
        case 7:
          list.add("#f1eef6");
          list.add("#d4b9da");
          list.add("#c994c7");
          list.add("#df65b0");
          list.add("#e7298a");
          list.add("#ce1256");
          list.add("#91003f");
          break;
        case 8:
          list.add("#f7f4f9");
          list.add("#e7e1ef");
          list.add("#d4b9da");
          list.add("#c994c7");
          list.add("#df65b0");
          list.add("#e7298a");
          list.add("#ce1256");
          list.add("#91003f");
          break;
        case 9:
          list.add("#f7f4f9");
          list.add("#e7e1ef");
          list.add("#d4b9da");
          list.add("#c994c7");
          list.add("#df65b0");
          list.add("#e7298a");
          list.add("#ce1256");
          list.add("#980043");
          list.add("#67001f");
          break;
      }
    }
    else if (colorName.compareTo("OrRd") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fee8c8");
          list.add("#fdbb84");
          list.add("#e34a33");
          break;
        case 4:
          list.add("#fef0d9");
          list.add("#fdcc8a");
          list.add("#fc8d59");
          list.add("#d7301f");
          break;
        case 5:
          list.add("#fef0d9");
          list.add("#fdcc8a");
          list.add("#fc8d59");
          list.add("#e34a33");
          list.add("#b30000");
          break;
        case 6:
          list.add("#fef0d9");
          list.add("#fdd49e");
          list.add("#fdbb84");
          list.add("#fc8d59");
          list.add("#e34a33");
          list.add("#b30000");
          break;
        case 7:
          list.add("#fef0d9");
          list.add("#fdd49e");
          list.add("#fdbb84");
          list.add("#fc8d59");
          list.add("#ef6548");
          list.add("#d7301f");
          list.add("#990000");
          break;
        case 8:
          list.add("#fff7ec");
          list.add("#fee8c8");
          list.add("#fdd49e");
          list.add("#fdbb84");
          list.add("#fc8d59");
          list.add("#ef6548");
          list.add("#d7301f");
          list.add("#990000");
          break;
        case 9:
          list.add("#fff7ec");
          list.add("#fee8c8");
          list.add("#fdd49e");
          list.add("#fdbb84");
          list.add("#fc8d59");
          list.add("#ef6548");
          list.add("#d7301f");
          list.add("#b30000");
          list.add("#7f0000");
          break;
      }
    }
    else if (colorName.compareTo("YlOrRd") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#ffeda0");
          list.add("#feb24c");
          list.add("#f03b20");
          break;
        case 4:
          list.add("#ffffb2");
          list.add("#fecc5c");
          list.add("#fd8d3c");
          list.add("#e31a1c");
          break;
        case 5:
          list.add("#ffffb2");
          list.add("#fecc5c");
          list.add("#fd8d3c");
          list.add("#f03b20");
          list.add("#bd0026");
          break;
        case 6:
          list.add("#ffffb2");
          list.add("#fed976");
          list.add("#feb24c");
          list.add("#fd8d3c");
          list.add("#f03b20");
          list.add("#bd0026");
          break;
        case 7:
          list.add("#ffffb2");
          list.add("#fed976");
          list.add("#feb24c");
          list.add("#fd8d3c");
          list.add("#fc4e2a");
          list.add("#e31a1c");
          list.add("#b10026");
          break;
        case 8:
          list.add("#ffffcc");
          list.add("#ffeda0");
          list.add("#fed976");
          list.add("#feb24c");
          list.add("#fd8d3c");
          list.add("#fc4e2a");
          list.add("#e31a1c");
          list.add("#b10026");
          break;
        case 9:
          list.add("#ffffcc");
          list.add("#ffeda0");
          list.add("#fed976");
          list.add("#feb24c");
          list.add("#fd8d3c");
          list.add("#fc4e2a");
          list.add("#e31a1c");
          list.add("#bd0026");
          list.add("#800026");
          break;
      }
    }
    else if (colorName.compareTo("YlOrBr") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fff7bc");
          list.add("#fec44f");
          list.add("#d95f0e");
          break;
        case 4:
          list.add("#ffffd4");
          list.add("#fed98e");
          list.add("#fe9929");
          list.add("#cc4c02");
          break;
        case 5:
          list.add("#ffffd4");
          list.add("#fed98e");
          list.add("#fe9929");
          list.add("#d95f0e");
          list.add("#993404");
          break;
        case 6:
          list.add("#ffffd4");
          list.add("#fee391");
          list.add("#fec44f");
          list.add("#fe9929");
          list.add("#d95f0e");
          list.add("#993404");
          break;
        case 7:
          list.add("#ffffd4");
          list.add("#fee391");
          list.add("#fec44f");
          list.add("#fe9929");
          list.add("#ec7014");
          list.add("#cc4c02");
          list.add("#8c2d04");
          break;
        case 8:
          list.add("#ffffe5");
          list.add("#fff7bc");
          list.add("#fee391");
          list.add("#fec44f");
          list.add("#fe9929");
          list.add("#ec7014");
          list.add("#cc4c02");
          list.add("#8c2d04");
          break;
        case 9:
          list.add("#ffffe5");
          list.add("#fff7bc");
          list.add("#fee391");
          list.add("#fec44f");
          list.add("#fe9929");
          list.add("#ec7014");
          list.add("#cc4c02");
          list.add("#993404");
          list.add("#662506");
          break;
      }
    }
    else if (colorName.compareTo("Purples") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#efedf5");
          list.add("#bcbddc");
          list.add("#756bb1");
          break;
        case 4:
          list.add("#f2f0f7");
          list.add("#cbc9e2");
          list.add("#9e9ac8");
          list.add("#6a51a3");
          break;
        case 5:
          list.add("#f2f0f7");
          list.add("#cbc9e2");
          list.add("#9e9ac8");
          list.add("#756bb1");
          list.add("#54278f");
          break;
        case 6:
          list.add("#f2f0f7");
          list.add("#dadaeb");
          list.add("#bcbddc");
          list.add("#9e9ac8");
          list.add("#756bb1");
          list.add("#54278f");
          break;
        case 7:
          list.add("#f2f0f7");
          list.add("#dadaeb");
          list.add("#bcbddc");
          list.add("#9e9ac8");
          list.add("#807dba");
          list.add("#6a51a3");
          list.add("#4a1486");
          break;
        case 8:
          list.add("#fcfbfd");
          list.add("#efedf5");
          list.add("#dadaeb");
          list.add("#bcbddc");
          list.add("#9e9ac8");
          list.add("#807dba");
          list.add("#6a51a3");
          list.add("#4a1486");
          break;
        case 9:
          list.add("#fcfbfd");
          list.add("#efedf5");
          list.add("#dadaeb");
          list.add("#bcbddc");
          list.add("#9e9ac8");
          list.add("#807dba");
          list.add("#6a51a3");
          list.add("#54278f");
          list.add("#3f007d");
          break;
      }
    }
    else if (colorName.compareTo("Blues") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#deebf7");
          list.add("#9ecae1");
          list.add("#3182bd");
          break;
        case 4:
          list.add("#eff3ff");
          list.add("#bdd7e7");
          list.add("#6baed6");
          list.add("#2171b5");
          break;
        case 5:
          list.add("#eff3ff");
          list.add("#bdd7e7");
          list.add("#6baed6");
          list.add("#3182bd");
          list.add("#08519c");
          break;
        case 6:
          list.add("#eff3ff");
          list.add("#c6dbef");
          list.add("#9ecae1");
          list.add("#6baed6");
          list.add("#3182bd");
          list.add("#08519c");
          break;
        case 7:
          list.add("#eff3ff");
          list.add("#c6dbef");
          list.add("#9ecae1");
          list.add("#6baed6");
          list.add("#4292c6");
          list.add("#2171b5");
          list.add("#084594");
          break;
        case 8:
          list.add("#f7fbff");
          list.add("#deebf7");
          list.add("#c6dbef");
          list.add("#9ecae1");
          list.add("#6baed6");
          list.add("#4292c6");
          list.add("#2171b5");
          list.add("#084594");
          break;
        case 9:
          list.add("#f7fbff");
          list.add("#deebf7");
          list.add("#c6dbef");
          list.add("#9ecae1");
          list.add("#6baed6");
          list.add("#4292c6");
          list.add("#2171b5");
          list.add("#08519c");
          list.add("#08306b");
          break;
      }
    }
    else if (colorName.compareTo("Greens") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e5f5e0");
          list.add("#a1d99b");
          list.add("#31a354");
          break;
        case 4:
          list.add("#edf8e9");
          list.add("#bae4b3");
          list.add("#74c476");
          list.add("#238b45");
          break;
        case 5:
          list.add("#edf8e9");
          list.add("#bae4b3");
          list.add("#74c476");
          list.add("#31a354");
          list.add("#006d2c");
          break;
        case 6:
          list.add("#edf8e9");
          list.add("#c7e9c0");
          list.add("#a1d99b");
          list.add("#74c476");
          list.add("#31a354");
          list.add("#006d2c");
          break;
        case 7:
          list.add("#edf8e9");
          list.add("#c7e9c0");
          list.add("#a1d99b");
          list.add("#74c476");
          list.add("#41ab5d");
          list.add("#238b45");
          list.add("#005a32");
          break;
        case 8:
          list.add("#f7fcf5");
          list.add("#e5f5e0");
          list.add("#c7e9c0");
          list.add("#a1d99b");
          list.add("#74c476");
          list.add("#41ab5d");
          list.add("#238b45");
          list.add("#005a32");
          break;
        case 9:
          list.add("#f7fcf5");
          list.add("#e5f5e0");
          list.add("#c7e9c0");
          list.add("#a1d99b");
          list.add("#74c476");
          list.add("#41ab5d");
          list.add("#238b45");
          list.add("#006d2c");
          list.add("#00441b");
          break;
      }
    }
    else if (colorName.compareTo("Oranges") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fee6ce");
          list.add("#fdae6b");
          list.add("#e6550d");
          break;
        case 4:
          list.add("#feedde");
          list.add("#fdbe85");
          list.add("#fd8d3c");
          list.add("#d94701");
          break;
        case 5:
          list.add("#feedde");
          list.add("#fdbe85");
          list.add("#fd8d3c");
          list.add("#e6550d");
          list.add("#a63603");
          break;
        case 6:
          list.add("#feedde");
          list.add("#fdd0a2");
          list.add("#fdae6b");
          list.add("#fd8d3c");
          list.add("#e6550d");
          list.add("#a63603");
          break;
        case 7:
          list.add("#feedde");
          list.add("#fdd0a2");
          list.add("#fdae6b");
          list.add("#fd8d3c");
          list.add("#f16913");
          list.add("#d94801");
          list.add("#8c2d04");
          break;
        case 8:
          list.add("#fff5eb");
          list.add("#fee6ce");
          list.add("#fdd0a2");
          list.add("#fdae6b");
          list.add("#fd8d3c");
          list.add("#f16913");
          list.add("#d94801");
          list.add("#8c2d04");
          break;
        case 9:
          list.add("#fff5eb");
          list.add("#fee6ce");
          list.add("#fdd0a2");
          list.add("#fdae6b");
          list.add("#fd8d3c");
          list.add("#f16913");
          list.add("#d94801");
          list.add("#a63603");
          list.add("#7f2704");
          break;
      }
    }
    else if (colorName.compareTo("Reds") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fee0d2");
          list.add("#fc9272");
          list.add("#de2d26");
          break;
        case 4:
          list.add("#fee5d9");
          list.add("#fcae91");
          list.add("#fb6a4a");
          list.add("#cb181d");
          break;
        case 5:
          list.add("#fee5d9");
          list.add("#fcae91");
          list.add("#fb6a4a");
          list.add("#de2d26");
          list.add("#a50f15");
          break;
        case 6:
          list.add("#fee5d9");
          list.add("#fcbba1");
          list.add("#fc9272");
          list.add("#fb6a4a");
          list.add("#de2d26");
          list.add("#a50f15");
          break;
        case 7:
          list.add("#fee5d9");
          list.add("#fcbba1");
          list.add("#fc9272");
          list.add("#fb6a4a");
          list.add("#ef3b2c");
          list.add("#cb181d");
          list.add("#99000d");
          break;
        case 8:
          list.add("#fff5f0");
          list.add("#fee0d2");
          list.add("#fcbba1");
          list.add("#fc9272");
          list.add("#fb6a4a");
          list.add("#ef3b2c");
          list.add("#cb181d");
          list.add("#99000d");
          break;
        case 9:
          list.add("#fff5f0");
          list.add("#fee0d2");
          list.add("#fcbba1");
          list.add("#fc9272");
          list.add("#fb6a4a");
          list.add("#ef3b2c");
          list.add("#cb181d");
          list.add("#a50f15");
          list.add("#67000d");
          break;
      }
    }
    else if (colorName.compareTo("Greys") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#f0f0f0");
          list.add("#bdbdbd");
          list.add("#636363");
          break;
        case 4:
          list.add("#f7f7f7");
          list.add("#cccccc");
          list.add("#969696");
          list.add("#525252");
          break;
        case 5:
          list.add("#f7f7f7");
          list.add("#cccccc");
          list.add("#969696");
          list.add("#636363");
          list.add("#252525");
          break;
        case 6:
          list.add("#f7f7f7");
          list.add("#d9d9d9");
          list.add("#bdbdbd");
          list.add("#969696");
          list.add("#636363");
          list.add("#252525");
          break;
        case 7:
          list.add("#f7f7f7");
          list.add("#d9d9d9");
          list.add("#bdbdbd");
          list.add("#969696");
          list.add("#737373");
          list.add("#525252");
          list.add("#252525");
          break;
        case 8:
          list.add("#ffffff");
          list.add("#f0f0f0");
          list.add("#d9d9d9");
          list.add("#bdbdbd");
          list.add("#969696");
          list.add("#737373");
          list.add("#525252");
          list.add("#252525");
          break;
        case 9:
          list.add("#ffffff");
          list.add("#f0f0f0");
          list.add("#d9d9d9");
          list.add("#bdbdbd");
          list.add("#969696");
          list.add("#737373");
          list.add("#525252");
          list.add("#252525");
          list.add("#000000");
          break;
      }
    }
    else if (colorName.compareTo("PuOr") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#f1a340");
          list.add("#f7f7f7");
          list.add("#998ec3");
          break;
        case 4:
          list.add("#e66101");
          list.add("#fdb863");
          list.add("#b2abd2");
          list.add("#5e3c99");
          break;
        case 5:
          list.add("#e66101");
          list.add("#fdb863");
          list.add("#f7f7f7");
          list.add("#b2abd2");
          list.add("#5e3c99");
          break;
        case 6:
          list.add("#b35806");
          list.add("#f1a340");
          list.add("#fee0b6");
          list.add("#d8daeb");
          list.add("#998ec3");
          list.add("#542788");
          break;
        case 7:
          list.add("#b35806");
          list.add("#f1a340");
          list.add("#fee0b6");
          list.add("#f7f7f7");
          list.add("#d8daeb");
          list.add("#998ec3");
          list.add("#542788");
          break;
        case 8:
          list.add("#b35806");
          list.add("#e08214");
          list.add("#fdb863");
          list.add("#fee0b6");
          list.add("#d8daeb");
          list.add("#b2abd2");
          list.add("#8073ac");
          list.add("#542788");
          break;
        case 9:
          list.add("#b35806");
          list.add("#e08214");
          list.add("#fdb863");
          list.add("#fee0b6");
          list.add("#f7f7f7");
          list.add("#d8daeb");
          list.add("#b2abd2");
          list.add("#8073ac");
          list.add("#542788");
          break;
        case 10:
          list.add("#7f3b08");
          list.add("#b35806");
          list.add("#e08214");
          list.add("#fdb863");
          list.add("#fee0b6");
          list.add("#d8daeb");
          list.add("#b2abd2");
          list.add("#8073ac");
          list.add("#542788");
          list.add("#2d004b");
          break;
        case 11:
          list.add("#7f3b08");
          list.add("#b35806");
          list.add("#e08214");
          list.add("#fdb863");
          list.add("#fee0b6");
          list.add("#f7f7f7");
          list.add("#d8daeb");
          list.add("#b2abd2");
          list.add("#8073ac");
          list.add("#542788");
          list.add("#2d004b");
          break;
      }
    }
    else if (colorName.compareTo("BrBG") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#d8b365");
          list.add("#f5f5f5");
          list.add("#5ab4ac");
          break;
        case 4:
          list.add("#a6611a");
          list.add("#dfc27d");
          list.add("#80cdc1");
          list.add("#018571");
          break;
        case 5:
          list.add("#a6611a");
          list.add("#dfc27d");
          list.add("#f5f5f5");
          list.add("#80cdc1");
          list.add("#018571");
          break;
        case 6:
          list.add("#8c510a");
          list.add("#d8b365");
          list.add("#f6e8c3");
          list.add("#c7eae5");
          list.add("#5ab4ac");
          list.add("#01665e");
          break;
        case 7:
          list.add("#8c510a");
          list.add("#d8b365");
          list.add("#f6e8c3");
          list.add("#f5f5f5");
          list.add("#c7eae5");
          list.add("#5ab4ac");
          list.add("#01665e");
          break;
        case 8:
          list.add("#8c510a");
          list.add("#bf812d");
          list.add("#dfc27d");
          list.add("#f6e8c3");
          list.add("#c7eae5");
          list.add("#80cdc1");
          list.add("#35978f");
          list.add("#01665e");
          break;
        case 9:
          list.add("#8c510a");
          list.add("#bf812d");
          list.add("#dfc27d");
          list.add("#f6e8c3");
          list.add("#f5f5f5");
          list.add("#c7eae5");
          list.add("#80cdc1");
          list.add("#35978f");
          list.add("#01665e");
          break;
        case 10:
          list.add("#543005");
          list.add("#8c510a");
          list.add("#bf812d");
          list.add("#dfc27d");
          list.add("#f6e8c3");
          list.add("#c7eae5");
          list.add("#80cdc1");
          list.add("#35978f");
          list.add("#01665e");
          list.add("#003c30");
          break;
        case 11:
          list.add("#543005");
          list.add("#8c510a");
          list.add("#bf812d");
          list.add("#dfc27d");
          list.add("#f6e8c3");
          list.add("#f5f5f5");
          list.add("#c7eae5");
          list.add("#80cdc1");
          list.add("#35978f");
          list.add("#01665e");
          list.add("#003c30");
          break;
      }
    }
    else if (colorName.compareTo("PRGn") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#af8dc3");
          list.add("#f7f7f7");
          list.add("#7fbf7b");
          break;
        case 4:
          list.add("#7b3294");
          list.add("#c2a5cf");
          list.add("#a6dba0");
          list.add("#008837");
          break;
        case 5:
          list.add("#7b3294");
          list.add("#c2a5cf");
          list.add("#f7f7f7");
          list.add("#a6dba0");
          list.add("#008837");
          break;
        case 6:
          list.add("#762a83");
          list.add("#af8dc3");
          list.add("#e7d4e8");
          list.add("#d9f0d3");
          list.add("#7fbf7b");
          list.add("#1b7837");
          break;
        case 7:
          list.add("#762a83");
          list.add("#af8dc3");
          list.add("#e7d4e8");
          list.add("#f7f7f7");
          list.add("#d9f0d3");
          list.add("#7fbf7b");
          list.add("#1b7837");
          break;
        case 8:
          list.add("#762a83");
          list.add("#9970ab");
          list.add("#c2a5cf");
          list.add("#e7d4e8");
          list.add("#d9f0d3");
          list.add("#a6dba0");
          list.add("#5aae61");
          list.add("#1b7837");
          break;
        case 9:
          list.add("#762a83");
          list.add("#9970ab");
          list.add("#c2a5cf");
          list.add("#e7d4e8");
          list.add("#f7f7f7");
          list.add("#d9f0d3");
          list.add("#a6dba0");
          list.add("#5aae61");
          list.add("#1b7837");
          break;
        case 10:
          list.add("#40004b");
          list.add("#762a83");
          list.add("#9970ab");
          list.add("#c2a5cf");
          list.add("#e7d4e8");
          list.add("#d9f0d3");
          list.add("#a6dba0");
          list.add("#5aae61");
          list.add("#1b7837");
          list.add("#00441b");
          break;
        case 11:
          list.add("#40004b");
          list.add("#762a83");
          list.add("#9970ab");
          list.add("#c2a5cf");
          list.add("#e7d4e8");
          list.add("#f7f7f7");
          list.add("#d9f0d3");
          list.add("#a6dba0");
          list.add("#5aae61");
          list.add("#1b7837");
          list.add("#00441b");
          break;
      }
    }
    else if (colorName.compareTo("PiYG") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e9a3c9");
          list.add("#f7f7f7");
          list.add("#a1d76a");
          break;
        case 4:
          list.add("#d01c8b");
          list.add("#f1b6da");
          list.add("#b8e186");
          list.add("#4dac26");
          break;
        case 5:
          list.add("#d01c8b");
          list.add("#f1b6da");
          list.add("#f7f7f7");
          list.add("#b8e186");
          list.add("#4dac26");
          break;
        case 6:
          list.add("#c51b7d");
          list.add("#e9a3c9");
          list.add("#fde0ef");
          list.add("#e6f5d0");
          list.add("#a1d76a");
          list.add("#4d9221");
          break;
        case 7:
          list.add("#c51b7d");
          list.add("#e9a3c9");
          list.add("#fde0ef");
          list.add("#f7f7f7");
          list.add("#e6f5d0");
          list.add("#a1d76a");
          list.add("#4d9221");
          break;
        case 8:
          list.add("#c51b7d");
          list.add("#de77ae");
          list.add("#f1b6da");
          list.add("#fde0ef");
          list.add("#e6f5d0");
          list.add("#b8e186");
          list.add("#7fbc41");
          list.add("#4d9221");
          break;
        case 9:
          list.add("#c51b7d");
          list.add("#de77ae");
          list.add("#f1b6da");
          list.add("#fde0ef");
          list.add("#f7f7f7");
          list.add("#e6f5d0");
          list.add("#b8e186");
          list.add("#7fbc41");
          list.add("#4d9221");
          break;
        case 10:
          list.add("#8e0152");
          list.add("#c51b7d");
          list.add("#de77ae");
          list.add("#f1b6da");
          list.add("#fde0ef");
          list.add("#e6f5d0");
          list.add("#b8e186");
          list.add("#7fbc41");
          list.add("#4d9221");
          list.add("#276419");
          break;
        case 11:
          list.add("#8e0152");
          list.add("#c51b7d");
          list.add("#de77ae");
          list.add("#f1b6da");
          list.add("#fde0ef");
          list.add("#f7f7f7");
          list.add("#e6f5d0");
          list.add("#b8e186");
          list.add("#7fbc41");
          list.add("#4d9221");
          list.add("#276419");
          break;
      }
    }
    else if (colorName.compareTo("RdBu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#ef8a62");
          list.add("#f7f7f7");
          list.add("#67a9cf");
          break;
        case 4:
          list.add("#ca0020");
          list.add("#f4a582");
          list.add("#92c5de");
          list.add("#0571b0");
          break;
        case 5:
          list.add("#ca0020");
          list.add("#f4a582");
          list.add("#f7f7f7");
          list.add("#92c5de");
          list.add("#0571b0");
          break;
        case 6:
          list.add("#b2182b");
          list.add("#ef8a62");
          list.add("#fddbc7");
          list.add("#d1e5f0");
          list.add("#67a9cf");
          list.add("#2166ac");
          break;
        case 7:
          list.add("#b2182b");
          list.add("#ef8a62");
          list.add("#fddbc7");
          list.add("#f7f7f7");
          list.add("#d1e5f0");
          list.add("#67a9cf");
          list.add("#2166ac");
          break;
        case 8:
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#d1e5f0");
          list.add("#92c5de");
          list.add("#4393c3");
          list.add("#2166ac");
          break;
        case 9:
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#f7f7f7");
          list.add("#d1e5f0");
          list.add("#92c5de");
          list.add("#4393c3");
          list.add("#2166ac");
          break;
        case 10:
          list.add("#67001f");
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#d1e5f0");
          list.add("#92c5de");
          list.add("#4393c3");
          list.add("#2166ac");
          list.add("#053061");
          break;
        case 11:
          list.add("#67001f");
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#f7f7f7");
          list.add("#d1e5f0");
          list.add("#92c5de");
          list.add("#4393c3");
          list.add("#2166ac");
          list.add("#053061");
          break;
      }
    }
    else if (colorName.compareTo("RdGy") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#ef8a62");
          list.add("#ffffff");
          list.add("#999999");
          break;
        case 4:
          list.add("#ca0020");
          list.add("#f4a582");
          list.add("#bababa");
          list.add("#404040");
          break;
        case 5:
          list.add("#ca0020");
          list.add("#f4a582");
          list.add("#ffffff");
          list.add("#bababa");
          list.add("#404040");
          break;
        case 6:
          list.add("#b2182b");
          list.add("#ef8a62");
          list.add("#fddbc7");
          list.add("#e0e0e0");
          list.add("#999999");
          list.add("#4d4d4d");
          break;
        case 7:
          list.add("#b2182b");
          list.add("#ef8a62");
          list.add("#fddbc7");
          list.add("#ffffff");
          list.add("#e0e0e0");
          list.add("#999999");
          list.add("#4d4d4d");
          break;
        case 8:
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#e0e0e0");
          list.add("#bababa");
          list.add("#878787");
          list.add("#4d4d4d");
          break;
        case 9:
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#ffffff");
          list.add("#e0e0e0");
          list.add("#bababa");
          list.add("#878787");
          list.add("#4d4d4d");
          break;
        case 10:
          list.add("#67001f");
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#e0e0e0");
          list.add("#bababa");
          list.add("#878787");
          list.add("#4d4d4d");
          list.add("#1a1a1a");
          break;
        case 11:
          list.add("#67001f");
          list.add("#b2182b");
          list.add("#d6604d");
          list.add("#f4a582");
          list.add("#fddbc7");
          list.add("#ffffff");
          list.add("#e0e0e0");
          list.add("#bababa");
          list.add("#878787");
          list.add("#4d4d4d");
          list.add("#1a1a1a");
          break;
      }
    }
    else if (colorName.compareTo("RdYlBu") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fc8d59");
          list.add("#ffffbf");
          list.add("#91bfdb");
          break;
        case 4:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#abd9e9");
          list.add("#2c7bb6");
          break;
        case 5:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#ffffbf");
          list.add("#abd9e9");
          list.add("#2c7bb6");
          break;
        case 6:
          list.add("#d73027");
          list.add("#fc8d59");
          list.add("#fee090");
          list.add("#e0f3f8");
          list.add("#91bfdb");
          list.add("#4575b4");
          break;
        case 7:
          list.add("#d73027");
          list.add("#fc8d59");
          list.add("#fee090");
          list.add("#ffffbf");
          list.add("#e0f3f8");
          list.add("#91bfdb");
          list.add("#4575b4");
          break;
        case 8:
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee090");
          list.add("#e0f3f8");
          list.add("#abd9e9");
          list.add("#74add1");
          list.add("#4575b4");
          break;
        case 9:
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee090");
          list.add("#ffffbf");
          list.add("#e0f3f8");
          list.add("#abd9e9");
          list.add("#74add1");
          list.add("#4575b4");
          break;
        case 10:
          list.add("#a50026");
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee090");
          list.add("#e0f3f8");
          list.add("#abd9e9");
          list.add("#74add1");
          list.add("#4575b4");
          list.add("#313695");
          break;
        case 11:
          list.add("#a50026");
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee090");
          list.add("#ffffbf");
          list.add("#e0f3f8");
          list.add("#abd9e9");
          list.add("#74add1");
          list.add("#4575b4");
          list.add("#313695");
          break;
      }
    }
    else if (colorName.compareTo("Spectral") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fc8d59");
          list.add("#ffffbf");
          list.add("#99d594");
          break;
        case 4:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#abdda4");
          list.add("#2b83ba");
          break;
        case 5:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#ffffbf");
          list.add("#abdda4");
          list.add("#2b83ba");
          break;
        case 6:
          list.add("#d53e4f");
          list.add("#fc8d59");
          list.add("#fee08b");
          list.add("#e6f598");
          list.add("#99d594");
          list.add("#3288bd");
          break;
        case 7:
          list.add("#d53e4f");
          list.add("#fc8d59");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#e6f598");
          list.add("#99d594");
          list.add("#3288bd");
          break;
        case 8:
          list.add("#d53e4f");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#e6f598");
          list.add("#abdda4");
          list.add("#66c2a5");
          list.add("#3288bd");
          break;
        case 9:
          list.add("#d53e4f");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#e6f598");
          list.add("#abdda4");
          list.add("#66c2a5");
          list.add("#3288bd");
          break;
        case 10:
          list.add("#9e0142");
          list.add("#d53e4f");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#e6f598");
          list.add("#abdda4");
          list.add("#66c2a5");
          list.add("#3288bd");
          list.add("#5e4fa2");
          break;
        case 11:
          list.add("#9e0142");
          list.add("#d53e4f");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#e6f598");
          list.add("#abdda4");
          list.add("#66c2a5");
          list.add("#3288bd");
          list.add("#5e4fa2");
          break;
      }
    }
    else if (colorName.compareTo("RdYlGn") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fc8d59");
          list.add("#ffffbf");
          list.add("#91cf60");
          break;
        case 4:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#a6d96a");
          list.add("#1a9641");
          break;
        case 5:
          list.add("#d7191c");
          list.add("#fdae61");
          list.add("#ffffbf");
          list.add("#a6d96a");
          list.add("#1a9641");
          break;
        case 6:
          list.add("#d73027");
          list.add("#fc8d59");
          list.add("#fee08b");
          list.add("#d9ef8b");
          list.add("#91cf60");
          list.add("#1a9850");
          break;
        case 7:
          list.add("#d73027");
          list.add("#fc8d59");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#d9ef8b");
          list.add("#91cf60");
          list.add("#1a9850");
          break;
        case 8:
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#d9ef8b");
          list.add("#a6d96a");
          list.add("#66bd63");
          list.add("#1a9850");
          break;
        case 9:
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#d9ef8b");
          list.add("#a6d96a");
          list.add("#66bd63");
          list.add("#1a9850");
          break;
        case 10:
          list.add("#a50026");
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#d9ef8b");
          list.add("#a6d96a");
          list.add("#66bd63");
          list.add("#1a9850");
          list.add("#006837");
          break;
        case 11:
          list.add("#a50026");
          list.add("#d73027");
          list.add("#f46d43");
          list.add("#fdae61");
          list.add("#fee08b");
          list.add("#ffffbf");
          list.add("#d9ef8b");
          list.add("#a6d96a");
          list.add("#66bd63");
          list.add("#1a9850");
          list.add("#006837");
          break;
      }
    }
    else if (colorName.compareTo("Accent") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          break;
        case 4:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          list.add("#ffff99");
          break;
        case 5:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          list.add("#ffff99");
          list.add("#386cb0");
          break;
        case 6:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          list.add("#ffff99");
          list.add("#386cb0");
          list.add("#f0027f");
          break;
        case 7:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          list.add("#ffff99");
          list.add("#386cb0");
          list.add("#f0027f");
          list.add("#bf5b17");
          break;
        case 8:
          list.add("#7fc97f");
          list.add("#beaed4");
          list.add("#fdc086");
          list.add("#ffff99");
          list.add("#386cb0");
          list.add("#f0027f");
          list.add("#bf5b17");
          list.add("#666666");
          break;
      }
    }
    else if (colorName.compareTo("Dark2") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          break;
        case 4:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          list.add("#e7298a");
          break;
        case 5:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          list.add("#e7298a");
          list.add("#66a61e");
          break;
        case 6:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          list.add("#e7298a");
          list.add("#66a61e");
          list.add("#e6ab02");
          break;
        case 7:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          list.add("#e7298a");
          list.add("#66a61e");
          list.add("#e6ab02");
          list.add("#a6761d");
          break;
        case 8:
          list.add("#1b9e77");
          list.add("#d95f02");
          list.add("#7570b3");
          list.add("#e7298a");
          list.add("#66a61e");
          list.add("#e6ab02");
          list.add("#a6761d");
          list.add("#666666");
          break;
      }
    }
    else if (colorName.compareTo("Paired") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          break;
        case 4:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          break;
        case 5:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          break;
        case 6:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          break;
        case 7:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          break;
        case 8:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          list.add("#ff7f00");
          break;
        case 9:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          list.add("#ff7f00");
          list.add("#cab2d6");
          break;
        case 10:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          list.add("#ff7f00");
          list.add("#cab2d6");
          list.add("#6a3d9a");
          break;
        case 11:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          list.add("#ff7f00");
          list.add("#cab2d6");
          list.add("#6a3d9a");
          list.add("#ffff99");
          break;
        case 12:
          list.add("#a6cee3");
          list.add("#1f78b4");
          list.add("#b2df8a");
          list.add("#33a02c");
          list.add("#fb9a99");
          list.add("#e31a1c");
          list.add("#fdbf6f");
          list.add("#ff7f00");
          list.add("#cab2d6");
          list.add("#6a3d9a");
          list.add("#ffff99");
          list.add("#b15928");
          break;
      }
    }
    else if (colorName.compareTo("Pastel1") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          break;
        case 4:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          break;
        case 5:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          list.add("#fed9a6");
          break;
        case 6:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          list.add("#fed9a6");
          list.add("#ffffcc");
          break;
        case 7:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          list.add("#fed9a6");
          list.add("#ffffcc");
          list.add("#e5d8bd");
          break;
        case 8:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          list.add("#fed9a6");
          list.add("#ffffcc");
          list.add("#e5d8bd");
          list.add("#fddaec");
          break;
        case 9:
          list.add("#fbb4ae");
          list.add("#b3cde3");
          list.add("#ccebc5");
          list.add("#decbe4");
          list.add("#fed9a6");
          list.add("#ffffcc");
          list.add("#e5d8bd");
          list.add("#fddaec");
          list.add("#f2f2f2");
          break;
      }
    }
    else if (colorName.compareTo("Pastel2") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          break;
        case 4:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          list.add("#f4cae4");
          break;
        case 5:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          list.add("#f4cae4");
          list.add("#e6f5c9");
          break;
        case 6:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          list.add("#f4cae4");
          list.add("#e6f5c9");
          list.add("#fff2ae");
          break;
        case 7:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          list.add("#f4cae4");
          list.add("#e6f5c9");
          list.add("#fff2ae");
          list.add("#f1e2cc");
          break;
        case 8:
          list.add("#b3e2cd");
          list.add("#fdcdac");
          list.add("#cbd5e8");
          list.add("#f4cae4");
          list.add("#e6f5c9");
          list.add("#fff2ae");
          list.add("#f1e2cc");
          list.add("#cccccc");
          break;
      }
    }
    else if (colorName.compareTo("Set1") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          break;
        case 4:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          break;
        case 5:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          list.add("#ff7f00");
          break;
        case 6:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          list.add("#ff7f00");
          list.add("#ffff33");
          break;
        case 7:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          list.add("#ff7f00");
          list.add("#ffff33");
          list.add("#a65628");
          break;
        case 8:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          list.add("#ff7f00");
          list.add("#ffff33");
          list.add("#a65628");
          list.add("#f781bf");
          break;
        case 9:
          list.add("#e41a1c");
          list.add("#377eb8");
          list.add("#4daf4a");
          list.add("#984ea3");
          list.add("#ff7f00");
          list.add("#ffff33");
          list.add("#a65628");
          list.add("#f781bf");
          list.add("#999999");
          break;
      }
    }
    else if (colorName.compareTo("Set2") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          break;
        case 4:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          list.add("#e78ac3");
          break;
        case 5:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          list.add("#e78ac3");
          list.add("#a6d854");
          break;
        case 6:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          list.add("#e78ac3");
          list.add("#a6d854");
          list.add("#ffd92f");
          break;
        case 7:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          list.add("#e78ac3");
          list.add("#a6d854");
          list.add("#ffd92f");
          list.add("#e5c494");
          break;
        case 8:
          list.add("#66c2a5");
          list.add("#fc8d62");
          list.add("#8da0cb");
          list.add("#e78ac3");
          list.add("#a6d854");
          list.add("#ffd92f");
          list.add("#e5c494");
          list.add("#b3b3b3");
          break;
      }
    }
    else if (colorName.compareTo("Set3") == 0)
    {
      switch (colorCount)
      {
        case 3:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          break;
        case 4:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          break;
        case 5:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          break;
        case 6:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          break;
        case 7:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          break;
        case 8:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          list.add("#fccde5");
          break;
        case 9:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          list.add("#fccde5");
          list.add("#d9d9d9");
          break;
        case 10:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          list.add("#fccde5");
          list.add("#d9d9d9");
          list.add("#bc80bd");
          break;
        case 11:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          list.add("#fccde5");
          list.add("#d9d9d9");
          list.add("#bc80bd");
          list.add("#ccebc5");
          break;
        case 12:
          list.add("#8dd3c7");
          list.add("#ffffb3");
          list.add("#bebada");
          list.add("#fb8072");
          list.add("#80b1d3");
          list.add("#fdb462");
          list.add("#b3de69");
          list.add("#fccde5");
          list.add("#d9d9d9");
          list.add("#bc80bd");
          list.add("#ccebc5");
          list.add("#ffed6f");
          break;
      }
    }
    return list;
  }

  public static ColorLegend createColorBrewLegendWithNaturalBreaks(java.util.ArrayList<Double> sListDouble, String colorName, int sClassCount)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO CHECK CJenksBreaks
    CJenksBreaks jenks = new CJenksBreaks(sListDouble, sClassCount);
    java.util.ArrayList<Integer> res = jenks.get_Results(); //NOT WORKING
  
    java.util.ArrayList<Double> naturalBreaks = new java.util.ArrayList<Double>();
    for (int i = 0; i < res.size(); i++)
    {
      naturalBreaks.add((int)res.get(i));
    }
    res = null;
    res = null;
  
  
    java.util.ArrayList<String> colorNames = brew(colorName, sClassCount);
  
    if (naturalBreaks.size() != colorNames.size())
    {
      throw new RuntimeException("Error at ColorLegendHelper::createColorBrewLegendWithNaturalBreaks()");
    }
  
    java.util.ArrayList<ColorLegend.ColorAndValue> legend = new java.util.ArrayList<ColorLegend.ColorAndValue>();
    for (int i = 0; i < sClassCount;i++)
    {
      Color c = Color.parse(colorNames.get(i));
      legend.add(new ColorLegend.ColorAndValue(c, naturalBreaks.get(i)));
      if (c != null)
         c.dispose();
    }
  
    return new ColorLegend(legend);
  }


  public static ColorLegend createColorBrewLegendWithEquallySpacedBreaks(java.util.ArrayList<Double> sListDouble, String colorName, int sClassCount)
  {
    double min = sListDouble.get(0);
    double max = sListDouble.get(0);
  
    for (int i = 1; i < sListDouble.size(); i++)
    {
      double v = sListDouble.get(i);
      if (v < min)
      {
        min = v;
      }
      else
      {
        if (v > max)
        {
          max = v;
        }
      }
    }
    java.util.ArrayList<Double> res = new java.util.ArrayList<Double>();
    for (int i = 0; i < sClassCount; i++)
    {
      double v = min + i*((max-min) / sClassCount);
      res.add(v);
    }
  
  
    java.util.ArrayList<String> colorNames = brew(colorName, sClassCount);
  
    if (res.size() != colorNames.size())
    {
      throw new RuntimeException("Error at ColorLegendHelper::createColorBrewLegendWithNaturalBreaks()");
    }
  
    java.util.ArrayList<ColorLegend.ColorAndValue> legend = new java.util.ArrayList<ColorLegend.ColorAndValue>();
    for (int i = 0; i < sClassCount;i++)
    {
      Color c = Color.parse(colorNames.get(i));
      legend.add(new ColorLegend.ColorAndValue(c, res.get(i)));
      if (c != null)
         c.dispose();
    }
  
    return new ColorLegend(legend);
  }

}