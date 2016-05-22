//
//  ColorLegendHelper.cpp
//  EIFER App
//
//  Created by Jose Miguel SN on 19/5/16.
//
//

#include "ColorLegendHelper.hpp"

ColorLegend* ColorLegendHelper::createColorBrewLegendWithNaturalBreaks(std::vector<double>& sListDouble,
                                                                       const std::string& colorName,
                                                                       int sClassCount){
//  std::vector<double>  naturalBreaks = getJenksBreaks(sListDouble, sClassCount);
  
  CJenksBreaks jenks(&sListDouble, sClassCount);
  std::vector<long>* res = jenks.get_Results();
  
  std::vector<double> naturalBreaks;
  for (size_t i = 0; i < res->size(); i++) {
    naturalBreaks.push_back((long)res->at(i));
  }
  delete res;
  res = NULL;
  
  
  std::vector<std::string> colorNames = brew(colorName, sClassCount);
  
  if (naturalBreaks.size() != colorNames.size()){
    THROW_EXCEPTION("Error at ColorLegendHelper::createColorBrewLegendWithNaturalBreaks()");
  }
  
  std::vector<ColorLegend::ColorAndValue*> legend;
  for (int i = 0; i < sClassCount;i++) {
    Color* c = Color::parse(colorNames[i]);
    legend.push_back(new ColorLegend::ColorAndValue(*c, naturalBreaks[i]));
    delete c;
  }
  
  return new ColorLegend(legend);
}

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



std::vector<std::string> ColorLegendHelper::brew(const std::string& colorName, size_t colorCount){
  std::vector<std::string> list;
  if (colorName.compare("YlGn") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#f7fcb9");
        list.push_back("#addd8e");
        list.push_back("#31a354");
        break;
      case 4:
        list.push_back("#ffffcc");
        list.push_back("#c2e699");
        list.push_back("#78c679");
        list.push_back("#238443");
        break;
      case 5:
        list.push_back("#ffffcc");
        list.push_back("#c2e699");
        list.push_back("#78c679");
        list.push_back("#31a354");
        list.push_back("#006837");
        break;
      case 6:
        list.push_back("#ffffcc");
        list.push_back("#d9f0a3");
        list.push_back("#addd8e");
        list.push_back("#78c679");
        list.push_back("#31a354");
        list.push_back("#006837");
        break;
      case 7:
        list.push_back("#ffffcc");
        list.push_back("#d9f0a3");
        list.push_back("#addd8e");
        list.push_back("#78c679");
        list.push_back("#41ab5d");
        list.push_back("#238443");
        list.push_back("#005a32");
        break;
      case 8:
        list.push_back("#ffffe5");
        list.push_back("#f7fcb9");
        list.push_back("#d9f0a3");
        list.push_back("#addd8e");
        list.push_back("#78c679");
        list.push_back("#41ab5d");
        list.push_back("#238443");
        list.push_back("#005a32");
        break;
      case 9:
        list.push_back("#ffffe5");
        list.push_back("#f7fcb9");
        list.push_back("#d9f0a3");
        list.push_back("#addd8e");
        list.push_back("#78c679");
        list.push_back("#41ab5d");
        list.push_back("#238443");
        list.push_back("#006837");
        list.push_back("#004529");
        break;
    }
  }
  else if (colorName.compare("YlGnBu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#edf8b1");
        list.push_back("#7fcdbb");
        list.push_back("#2c7fb8");
        break;
      case 4:
        list.push_back("#ffffcc");
        list.push_back("#a1dab4");
        list.push_back("#41b6c4");
        list.push_back("#225ea8");
        break;
      case 5:
        list.push_back("#ffffcc");
        list.push_back("#a1dab4");
        list.push_back("#41b6c4");
        list.push_back("#2c7fb8");
        list.push_back("#253494");
        break;
      case 6:
        list.push_back("#ffffcc");
        list.push_back("#c7e9b4");
        list.push_back("#7fcdbb");
        list.push_back("#41b6c4");
        list.push_back("#2c7fb8");
        list.push_back("#253494");
        break;
      case 7:
        list.push_back("#ffffcc");
        list.push_back("#c7e9b4");
        list.push_back("#7fcdbb");
        list.push_back("#41b6c4");
        list.push_back("#1d91c0");
        list.push_back("#225ea8");
        list.push_back("#0c2c84");
        break;
      case 8:
        list.push_back("#ffffd9");
        list.push_back("#edf8b1");
        list.push_back("#c7e9b4");
        list.push_back("#7fcdbb");
        list.push_back("#41b6c4");
        list.push_back("#1d91c0");
        list.push_back("#225ea8");
        list.push_back("#0c2c84");
        break;
      case 9:
        list.push_back("#ffffd9");
        list.push_back("#edf8b1");
        list.push_back("#c7e9b4");
        list.push_back("#7fcdbb");
        list.push_back("#41b6c4");
        list.push_back("#1d91c0");
        list.push_back("#225ea8");
        list.push_back("#253494");
        list.push_back("#081d58");
        break;
    }
  }
  else if (colorName.compare("GnBu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e0f3db");
        list.push_back("#a8ddb5");
        list.push_back("#43a2ca");
        break;
      case 4:
        list.push_back("#f0f9e8");
        list.push_back("#bae4bc");
        list.push_back("#7bccc4");
        list.push_back("#2b8cbe");
        break;
      case 5:
        list.push_back("#f0f9e8");
        list.push_back("#bae4bc");
        list.push_back("#7bccc4");
        list.push_back("#43a2ca");
        list.push_back("#0868ac");
        break;
      case 6:
        list.push_back("#f0f9e8");
        list.push_back("#ccebc5");
        list.push_back("#a8ddb5");
        list.push_back("#7bccc4");
        list.push_back("#43a2ca");
        list.push_back("#0868ac");
        break;
      case 7:
        list.push_back("#f0f9e8");
        list.push_back("#ccebc5");
        list.push_back("#a8ddb5");
        list.push_back("#7bccc4");
        list.push_back("#4eb3d3");
        list.push_back("#2b8cbe");
        list.push_back("#08589e");
        break;
      case 8:
        list.push_back("#f7fcf0");
        list.push_back("#e0f3db");
        list.push_back("#ccebc5");
        list.push_back("#a8ddb5");
        list.push_back("#7bccc4");
        list.push_back("#4eb3d3");
        list.push_back("#2b8cbe");
        list.push_back("#08589e");
        break;
      case 9:
        list.push_back("#f7fcf0");
        list.push_back("#e0f3db");
        list.push_back("#ccebc5");
        list.push_back("#a8ddb5");
        list.push_back("#7bccc4");
        list.push_back("#4eb3d3");
        list.push_back("#2b8cbe");
        list.push_back("#0868ac");
        list.push_back("#084081");
        break;
    }
  }
  else if (colorName.compare("BuGn") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e5f5f9");
        list.push_back("#99d8c9");
        list.push_back("#2ca25f");
        break;
      case 4:
        list.push_back("#edf8fb");
        list.push_back("#b2e2e2");
        list.push_back("#66c2a4");
        list.push_back("#238b45");
        break;
      case 5:
        list.push_back("#edf8fb");
        list.push_back("#b2e2e2");
        list.push_back("#66c2a4");
        list.push_back("#2ca25f");
        list.push_back("#006d2c");
        break;
      case 6:
        list.push_back("#edf8fb");
        list.push_back("#ccece6");
        list.push_back("#99d8c9");
        list.push_back("#66c2a4");
        list.push_back("#2ca25f");
        list.push_back("#006d2c");
        break;
      case 7:
        list.push_back("#edf8fb");
        list.push_back("#ccece6");
        list.push_back("#99d8c9");
        list.push_back("#66c2a4");
        list.push_back("#41ae76");
        list.push_back("#238b45");
        list.push_back("#005824");
        break;
      case 8:
        list.push_back("#f7fcfd");
        list.push_back("#e5f5f9");
        list.push_back("#ccece6");
        list.push_back("#99d8c9");
        list.push_back("#66c2a4");
        list.push_back("#41ae76");
        list.push_back("#238b45");
        list.push_back("#005824");
        break;
      case 9:
        list.push_back("#f7fcfd");
        list.push_back("#e5f5f9");
        list.push_back("#ccece6");
        list.push_back("#99d8c9");
        list.push_back("#66c2a4");
        list.push_back("#41ae76");
        list.push_back("#238b45");
        list.push_back("#006d2c");
        list.push_back("#00441b");
        break;
    }
  }
  else if (colorName.compare("PuBuGn") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#ece2f0");
        list.push_back("#a6bddb");
        list.push_back("#1c9099");
        break;
      case 4:
        list.push_back("#f6eff7");
        list.push_back("#bdc9e1");
        list.push_back("#67a9cf");
        list.push_back("#02818a");
        break;
      case 5:
        list.push_back("#f6eff7");
        list.push_back("#bdc9e1");
        list.push_back("#67a9cf");
        list.push_back("#1c9099");
        list.push_back("#016c59");
        break;
      case 6:
        list.push_back("#f6eff7");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#67a9cf");
        list.push_back("#1c9099");
        list.push_back("#016c59");
        break;
      case 7:
        list.push_back("#f6eff7");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#67a9cf");
        list.push_back("#3690c0");
        list.push_back("#02818a");
        list.push_back("#016450");
        break;
      case 8:
        list.push_back("#fff7fb");
        list.push_back("#ece2f0");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#67a9cf");
        list.push_back("#3690c0");
        list.push_back("#02818a");
        list.push_back("#016450");
        break;
      case 9:
        list.push_back("#fff7fb");
        list.push_back("#ece2f0");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#67a9cf");
        list.push_back("#3690c0");
        list.push_back("#02818a");
        list.push_back("#016c59");
        list.push_back("#014636");
        break;
    }
  }
  else if (colorName.compare("PuBu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#ece7f2");
        list.push_back("#a6bddb");
        list.push_back("#2b8cbe");
        break;
      case 4:
        list.push_back("#f1eef6");
        list.push_back("#bdc9e1");
        list.push_back("#74a9cf");
        list.push_back("#0570b0");
        break;
      case 5:
        list.push_back("#f1eef6");
        list.push_back("#bdc9e1");
        list.push_back("#74a9cf");
        list.push_back("#2b8cbe");
        list.push_back("#045a8d");
        break;
      case 6:
        list.push_back("#f1eef6");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#74a9cf");
        list.push_back("#2b8cbe");
        list.push_back("#045a8d");
        break;
      case 7:
        list.push_back("#f1eef6");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#74a9cf");
        list.push_back("#3690c0");
        list.push_back("#0570b0");
        list.push_back("#034e7b");
        break;
      case 8:
        list.push_back("#fff7fb");
        list.push_back("#ece7f2");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#74a9cf");
        list.push_back("#3690c0");
        list.push_back("#0570b0");
        list.push_back("#034e7b");
        break;
      case 9:
        list.push_back("#fff7fb");
        list.push_back("#ece7f2");
        list.push_back("#d0d1e6");
        list.push_back("#a6bddb");
        list.push_back("#74a9cf");
        list.push_back("#3690c0");
        list.push_back("#0570b0");
        list.push_back("#045a8d");
        list.push_back("#023858");
        break;
    }
  }
  else if (colorName.compare("BuPu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e0ecf4");
        list.push_back("#9ebcda");
        list.push_back("#8856a7");
        break;
      case 4:
        list.push_back("#edf8fb");
        list.push_back("#b3cde3");
        list.push_back("#8c96c6");
        list.push_back("#88419d");
        break;
      case 5:
        list.push_back("#edf8fb");
        list.push_back("#b3cde3");
        list.push_back("#8c96c6");
        list.push_back("#8856a7");
        list.push_back("#810f7c");
        break;
      case 6:
        list.push_back("#edf8fb");
        list.push_back("#bfd3e6");
        list.push_back("#9ebcda");
        list.push_back("#8c96c6");
        list.push_back("#8856a7");
        list.push_back("#810f7c");
        break;
      case 7:
        list.push_back("#edf8fb");
        list.push_back("#bfd3e6");
        list.push_back("#9ebcda");
        list.push_back("#8c96c6");
        list.push_back("#8c6bb1");
        list.push_back("#88419d");
        list.push_back("#6e016b");
        break;
      case 8:
        list.push_back("#f7fcfd");
        list.push_back("#e0ecf4");
        list.push_back("#bfd3e6");
        list.push_back("#9ebcda");
        list.push_back("#8c96c6");
        list.push_back("#8c6bb1");
        list.push_back("#88419d");
        list.push_back("#6e016b");
        break;
      case 9:
        list.push_back("#f7fcfd");
        list.push_back("#e0ecf4");
        list.push_back("#bfd3e6");
        list.push_back("#9ebcda");
        list.push_back("#8c96c6");
        list.push_back("#8c6bb1");
        list.push_back("#88419d");
        list.push_back("#810f7c");
        list.push_back("#4d004b");
        break;
    }
  }
  else if (colorName.compare("RdPu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fde0dd");
        list.push_back("#fa9fb5");
        list.push_back("#c51b8a");
        break;
      case 4:
        list.push_back("#feebe2");
        list.push_back("#fbb4b9");
        list.push_back("#f768a1");
        list.push_back("#ae017e");
        break;
      case 5:
        list.push_back("#feebe2");
        list.push_back("#fbb4b9");
        list.push_back("#f768a1");
        list.push_back("#c51b8a");
        list.push_back("#7a0177");
        break;
      case 6:
        list.push_back("#feebe2");
        list.push_back("#fcc5c0");
        list.push_back("#fa9fb5");
        list.push_back("#f768a1");
        list.push_back("#c51b8a");
        list.push_back("#7a0177");
        break;
      case 7:
        list.push_back("#feebe2");
        list.push_back("#fcc5c0");
        list.push_back("#fa9fb5");
        list.push_back("#f768a1");
        list.push_back("#dd3497");
        list.push_back("#ae017e");
        list.push_back("#7a0177");
        break;
      case 8:
        list.push_back("#fff7f3");
        list.push_back("#fde0dd");
        list.push_back("#fcc5c0");
        list.push_back("#fa9fb5");
        list.push_back("#f768a1");
        list.push_back("#dd3497");
        list.push_back("#ae017e");
        list.push_back("#7a0177");
        break;
      case 9:
        list.push_back("#fff7f3");
        list.push_back("#fde0dd");
        list.push_back("#fcc5c0");
        list.push_back("#fa9fb5");
        list.push_back("#f768a1");
        list.push_back("#dd3497");
        list.push_back("#ae017e");
        list.push_back("#7a0177");
        list.push_back("#49006a");
        break;
    }
  }
  else if (colorName.compare("PuRd") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e7e1ef");
        list.push_back("#c994c7");
        list.push_back("#dd1c77");
        break;
      case 4:
        list.push_back("#f1eef6");
        list.push_back("#d7b5d8");
        list.push_back("#df65b0");
        list.push_back("#ce1256");
        break;
      case 5:
        list.push_back("#f1eef6");
        list.push_back("#d7b5d8");
        list.push_back("#df65b0");
        list.push_back("#dd1c77");
        list.push_back("#980043");
        break;
      case 6:
        list.push_back("#f1eef6");
        list.push_back("#d4b9da");
        list.push_back("#c994c7");
        list.push_back("#df65b0");
        list.push_back("#dd1c77");
        list.push_back("#980043");
        break;
      case 7:
        list.push_back("#f1eef6");
        list.push_back("#d4b9da");
        list.push_back("#c994c7");
        list.push_back("#df65b0");
        list.push_back("#e7298a");
        list.push_back("#ce1256");
        list.push_back("#91003f");
        break;
      case 8:
        list.push_back("#f7f4f9");
        list.push_back("#e7e1ef");
        list.push_back("#d4b9da");
        list.push_back("#c994c7");
        list.push_back("#df65b0");
        list.push_back("#e7298a");
        list.push_back("#ce1256");
        list.push_back("#91003f");
        break;
      case 9:
        list.push_back("#f7f4f9");
        list.push_back("#e7e1ef");
        list.push_back("#d4b9da");
        list.push_back("#c994c7");
        list.push_back("#df65b0");
        list.push_back("#e7298a");
        list.push_back("#ce1256");
        list.push_back("#980043");
        list.push_back("#67001f");
        break;
    }
  }
  else if (colorName.compare("OrRd") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fee8c8");
        list.push_back("#fdbb84");
        list.push_back("#e34a33");
        break;
      case 4:
        list.push_back("#fef0d9");
        list.push_back("#fdcc8a");
        list.push_back("#fc8d59");
        list.push_back("#d7301f");
        break;
      case 5:
        list.push_back("#fef0d9");
        list.push_back("#fdcc8a");
        list.push_back("#fc8d59");
        list.push_back("#e34a33");
        list.push_back("#b30000");
        break;
      case 6:
        list.push_back("#fef0d9");
        list.push_back("#fdd49e");
        list.push_back("#fdbb84");
        list.push_back("#fc8d59");
        list.push_back("#e34a33");
        list.push_back("#b30000");
        break;
      case 7:
        list.push_back("#fef0d9");
        list.push_back("#fdd49e");
        list.push_back("#fdbb84");
        list.push_back("#fc8d59");
        list.push_back("#ef6548");
        list.push_back("#d7301f");
        list.push_back("#990000");
        break;
      case 8:
        list.push_back("#fff7ec");
        list.push_back("#fee8c8");
        list.push_back("#fdd49e");
        list.push_back("#fdbb84");
        list.push_back("#fc8d59");
        list.push_back("#ef6548");
        list.push_back("#d7301f");
        list.push_back("#990000");
        break;
      case 9:
        list.push_back("#fff7ec");
        list.push_back("#fee8c8");
        list.push_back("#fdd49e");
        list.push_back("#fdbb84");
        list.push_back("#fc8d59");
        list.push_back("#ef6548");
        list.push_back("#d7301f");
        list.push_back("#b30000");
        list.push_back("#7f0000");
        break;
    }
  }
  else if (colorName.compare("YlOrRd") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#ffeda0");
        list.push_back("#feb24c");
        list.push_back("#f03b20");
        break;
      case 4:
        list.push_back("#ffffb2");
        list.push_back("#fecc5c");
        list.push_back("#fd8d3c");
        list.push_back("#e31a1c");
        break;
      case 5:
        list.push_back("#ffffb2");
        list.push_back("#fecc5c");
        list.push_back("#fd8d3c");
        list.push_back("#f03b20");
        list.push_back("#bd0026");
        break;
      case 6:
        list.push_back("#ffffb2");
        list.push_back("#fed976");
        list.push_back("#feb24c");
        list.push_back("#fd8d3c");
        list.push_back("#f03b20");
        list.push_back("#bd0026");
        break;
      case 7:
        list.push_back("#ffffb2");
        list.push_back("#fed976");
        list.push_back("#feb24c");
        list.push_back("#fd8d3c");
        list.push_back("#fc4e2a");
        list.push_back("#e31a1c");
        list.push_back("#b10026");
        break;
      case 8:
        list.push_back("#ffffcc");
        list.push_back("#ffeda0");
        list.push_back("#fed976");
        list.push_back("#feb24c");
        list.push_back("#fd8d3c");
        list.push_back("#fc4e2a");
        list.push_back("#e31a1c");
        list.push_back("#b10026");
        break;
      case 9:
        list.push_back("#ffffcc");
        list.push_back("#ffeda0");
        list.push_back("#fed976");
        list.push_back("#feb24c");
        list.push_back("#fd8d3c");
        list.push_back("#fc4e2a");
        list.push_back("#e31a1c");
        list.push_back("#bd0026");
        list.push_back("#800026");
        break;
    }
  }
  else if (colorName.compare("YlOrBr") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fff7bc");
        list.push_back("#fec44f");
        list.push_back("#d95f0e");
        break;
      case 4:
        list.push_back("#ffffd4");
        list.push_back("#fed98e");
        list.push_back("#fe9929");
        list.push_back("#cc4c02");
        break;
      case 5:
        list.push_back("#ffffd4");
        list.push_back("#fed98e");
        list.push_back("#fe9929");
        list.push_back("#d95f0e");
        list.push_back("#993404");
        break;
      case 6:
        list.push_back("#ffffd4");
        list.push_back("#fee391");
        list.push_back("#fec44f");
        list.push_back("#fe9929");
        list.push_back("#d95f0e");
        list.push_back("#993404");
        break;
      case 7:
        list.push_back("#ffffd4");
        list.push_back("#fee391");
        list.push_back("#fec44f");
        list.push_back("#fe9929");
        list.push_back("#ec7014");
        list.push_back("#cc4c02");
        list.push_back("#8c2d04");
        break;
      case 8:
        list.push_back("#ffffe5");
        list.push_back("#fff7bc");
        list.push_back("#fee391");
        list.push_back("#fec44f");
        list.push_back("#fe9929");
        list.push_back("#ec7014");
        list.push_back("#cc4c02");
        list.push_back("#8c2d04");
        break;
      case 9:
        list.push_back("#ffffe5");
        list.push_back("#fff7bc");
        list.push_back("#fee391");
        list.push_back("#fec44f");
        list.push_back("#fe9929");
        list.push_back("#ec7014");
        list.push_back("#cc4c02");
        list.push_back("#993404");
        list.push_back("#662506");
        break;
    }
  }
  else if (colorName.compare("Purples") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#efedf5");
        list.push_back("#bcbddc");
        list.push_back("#756bb1");
        break;
      case 4:
        list.push_back("#f2f0f7");
        list.push_back("#cbc9e2");
        list.push_back("#9e9ac8");
        list.push_back("#6a51a3");
        break;
      case 5:
        list.push_back("#f2f0f7");
        list.push_back("#cbc9e2");
        list.push_back("#9e9ac8");
        list.push_back("#756bb1");
        list.push_back("#54278f");
        break;
      case 6:
        list.push_back("#f2f0f7");
        list.push_back("#dadaeb");
        list.push_back("#bcbddc");
        list.push_back("#9e9ac8");
        list.push_back("#756bb1");
        list.push_back("#54278f");
        break;
      case 7:
        list.push_back("#f2f0f7");
        list.push_back("#dadaeb");
        list.push_back("#bcbddc");
        list.push_back("#9e9ac8");
        list.push_back("#807dba");
        list.push_back("#6a51a3");
        list.push_back("#4a1486");
        break;
      case 8:
        list.push_back("#fcfbfd");
        list.push_back("#efedf5");
        list.push_back("#dadaeb");
        list.push_back("#bcbddc");
        list.push_back("#9e9ac8");
        list.push_back("#807dba");
        list.push_back("#6a51a3");
        list.push_back("#4a1486");
        break;
      case 9:
        list.push_back("#fcfbfd");
        list.push_back("#efedf5");
        list.push_back("#dadaeb");
        list.push_back("#bcbddc");
        list.push_back("#9e9ac8");
        list.push_back("#807dba");
        list.push_back("#6a51a3");
        list.push_back("#54278f");
        list.push_back("#3f007d");
        break;
    }
  }
  else if (colorName.compare("Blues") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#deebf7");
        list.push_back("#9ecae1");
        list.push_back("#3182bd");
        break;
      case 4:
        list.push_back("#eff3ff");
        list.push_back("#bdd7e7");
        list.push_back("#6baed6");
        list.push_back("#2171b5");
        break;
      case 5:
        list.push_back("#eff3ff");
        list.push_back("#bdd7e7");
        list.push_back("#6baed6");
        list.push_back("#3182bd");
        list.push_back("#08519c");
        break;
      case 6:
        list.push_back("#eff3ff");
        list.push_back("#c6dbef");
        list.push_back("#9ecae1");
        list.push_back("#6baed6");
        list.push_back("#3182bd");
        list.push_back("#08519c");
        break;
      case 7:
        list.push_back("#eff3ff");
        list.push_back("#c6dbef");
        list.push_back("#9ecae1");
        list.push_back("#6baed6");
        list.push_back("#4292c6");
        list.push_back("#2171b5");
        list.push_back("#084594");
        break;
      case 8:
        list.push_back("#f7fbff");
        list.push_back("#deebf7");
        list.push_back("#c6dbef");
        list.push_back("#9ecae1");
        list.push_back("#6baed6");
        list.push_back("#4292c6");
        list.push_back("#2171b5");
        list.push_back("#084594");
        break;
      case 9:
        list.push_back("#f7fbff");
        list.push_back("#deebf7");
        list.push_back("#c6dbef");
        list.push_back("#9ecae1");
        list.push_back("#6baed6");
        list.push_back("#4292c6");
        list.push_back("#2171b5");
        list.push_back("#08519c");
        list.push_back("#08306b");
        break;
    }
  }
  else if (colorName.compare("Greens") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e5f5e0");
        list.push_back("#a1d99b");
        list.push_back("#31a354");
        break;
      case 4:
        list.push_back("#edf8e9");
        list.push_back("#bae4b3");
        list.push_back("#74c476");
        list.push_back("#238b45");
        break;
      case 5:
        list.push_back("#edf8e9");
        list.push_back("#bae4b3");
        list.push_back("#74c476");
        list.push_back("#31a354");
        list.push_back("#006d2c");
        break;
      case 6:
        list.push_back("#edf8e9");
        list.push_back("#c7e9c0");
        list.push_back("#a1d99b");
        list.push_back("#74c476");
        list.push_back("#31a354");
        list.push_back("#006d2c");
        break;
      case 7:
        list.push_back("#edf8e9");
        list.push_back("#c7e9c0");
        list.push_back("#a1d99b");
        list.push_back("#74c476");
        list.push_back("#41ab5d");
        list.push_back("#238b45");
        list.push_back("#005a32");
        break;
      case 8:
        list.push_back("#f7fcf5");
        list.push_back("#e5f5e0");
        list.push_back("#c7e9c0");
        list.push_back("#a1d99b");
        list.push_back("#74c476");
        list.push_back("#41ab5d");
        list.push_back("#238b45");
        list.push_back("#005a32");
        break;
      case 9:
        list.push_back("#f7fcf5");
        list.push_back("#e5f5e0");
        list.push_back("#c7e9c0");
        list.push_back("#a1d99b");
        list.push_back("#74c476");
        list.push_back("#41ab5d");
        list.push_back("#238b45");
        list.push_back("#006d2c");
        list.push_back("#00441b");
        break;
    }
  }
  else if (colorName.compare("Oranges") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fee6ce");
        list.push_back("#fdae6b");
        list.push_back("#e6550d");
        break;
      case 4:
        list.push_back("#feedde");
        list.push_back("#fdbe85");
        list.push_back("#fd8d3c");
        list.push_back("#d94701");
        break;
      case 5:
        list.push_back("#feedde");
        list.push_back("#fdbe85");
        list.push_back("#fd8d3c");
        list.push_back("#e6550d");
        list.push_back("#a63603");
        break;
      case 6:
        list.push_back("#feedde");
        list.push_back("#fdd0a2");
        list.push_back("#fdae6b");
        list.push_back("#fd8d3c");
        list.push_back("#e6550d");
        list.push_back("#a63603");
        break;
      case 7:
        list.push_back("#feedde");
        list.push_back("#fdd0a2");
        list.push_back("#fdae6b");
        list.push_back("#fd8d3c");
        list.push_back("#f16913");
        list.push_back("#d94801");
        list.push_back("#8c2d04");
        break;
      case 8:
        list.push_back("#fff5eb");
        list.push_back("#fee6ce");
        list.push_back("#fdd0a2");
        list.push_back("#fdae6b");
        list.push_back("#fd8d3c");
        list.push_back("#f16913");
        list.push_back("#d94801");
        list.push_back("#8c2d04");
        break;
      case 9:
        list.push_back("#fff5eb");
        list.push_back("#fee6ce");
        list.push_back("#fdd0a2");
        list.push_back("#fdae6b");
        list.push_back("#fd8d3c");
        list.push_back("#f16913");
        list.push_back("#d94801");
        list.push_back("#a63603");
        list.push_back("#7f2704");
        break;
    }
  }
  else if (colorName.compare("Reds") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fee0d2");
        list.push_back("#fc9272");
        list.push_back("#de2d26");
        break;
      case 4:
        list.push_back("#fee5d9");
        list.push_back("#fcae91");
        list.push_back("#fb6a4a");
        list.push_back("#cb181d");
        break;
      case 5:
        list.push_back("#fee5d9");
        list.push_back("#fcae91");
        list.push_back("#fb6a4a");
        list.push_back("#de2d26");
        list.push_back("#a50f15");
        break;
      case 6:
        list.push_back("#fee5d9");
        list.push_back("#fcbba1");
        list.push_back("#fc9272");
        list.push_back("#fb6a4a");
        list.push_back("#de2d26");
        list.push_back("#a50f15");
        break;
      case 7:
        list.push_back("#fee5d9");
        list.push_back("#fcbba1");
        list.push_back("#fc9272");
        list.push_back("#fb6a4a");
        list.push_back("#ef3b2c");
        list.push_back("#cb181d");
        list.push_back("#99000d");
        break;
      case 8:
        list.push_back("#fff5f0");
        list.push_back("#fee0d2");
        list.push_back("#fcbba1");
        list.push_back("#fc9272");
        list.push_back("#fb6a4a");
        list.push_back("#ef3b2c");
        list.push_back("#cb181d");
        list.push_back("#99000d");
        break;
      case 9:
        list.push_back("#fff5f0");
        list.push_back("#fee0d2");
        list.push_back("#fcbba1");
        list.push_back("#fc9272");
        list.push_back("#fb6a4a");
        list.push_back("#ef3b2c");
        list.push_back("#cb181d");
        list.push_back("#a50f15");
        list.push_back("#67000d");
        break;
    }
  }
  else if (colorName.compare("Greys") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#f0f0f0");
        list.push_back("#bdbdbd");
        list.push_back("#636363");
        break;
      case 4:
        list.push_back("#f7f7f7");
        list.push_back("#cccccc");
        list.push_back("#969696");
        list.push_back("#525252");
        break;
      case 5:
        list.push_back("#f7f7f7");
        list.push_back("#cccccc");
        list.push_back("#969696");
        list.push_back("#636363");
        list.push_back("#252525");
        break;
      case 6:
        list.push_back("#f7f7f7");
        list.push_back("#d9d9d9");
        list.push_back("#bdbdbd");
        list.push_back("#969696");
        list.push_back("#636363");
        list.push_back("#252525");
        break;
      case 7:
        list.push_back("#f7f7f7");
        list.push_back("#d9d9d9");
        list.push_back("#bdbdbd");
        list.push_back("#969696");
        list.push_back("#737373");
        list.push_back("#525252");
        list.push_back("#252525");
        break;
      case 8:
        list.push_back("#ffffff");
        list.push_back("#f0f0f0");
        list.push_back("#d9d9d9");
        list.push_back("#bdbdbd");
        list.push_back("#969696");
        list.push_back("#737373");
        list.push_back("#525252");
        list.push_back("#252525");
        break;
      case 9:
        list.push_back("#ffffff");
        list.push_back("#f0f0f0");
        list.push_back("#d9d9d9");
        list.push_back("#bdbdbd");
        list.push_back("#969696");
        list.push_back("#737373");
        list.push_back("#525252");
        list.push_back("#252525");
        list.push_back("#000000");
        break;
    }
  }
  else if (colorName.compare("PuOr") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#f1a340");
        list.push_back("#f7f7f7");
        list.push_back("#998ec3");
        break;
      case 4:
        list.push_back("#e66101");
        list.push_back("#fdb863");
        list.push_back("#b2abd2");
        list.push_back("#5e3c99");
        break;
      case 5:
        list.push_back("#e66101");
        list.push_back("#fdb863");
        list.push_back("#f7f7f7");
        list.push_back("#b2abd2");
        list.push_back("#5e3c99");
        break;
      case 6:
        list.push_back("#b35806");
        list.push_back("#f1a340");
        list.push_back("#fee0b6");
        list.push_back("#d8daeb");
        list.push_back("#998ec3");
        list.push_back("#542788");
        break;
      case 7:
        list.push_back("#b35806");
        list.push_back("#f1a340");
        list.push_back("#fee0b6");
        list.push_back("#f7f7f7");
        list.push_back("#d8daeb");
        list.push_back("#998ec3");
        list.push_back("#542788");
        break;
      case 8:
        list.push_back("#b35806");
        list.push_back("#e08214");
        list.push_back("#fdb863");
        list.push_back("#fee0b6");
        list.push_back("#d8daeb");
        list.push_back("#b2abd2");
        list.push_back("#8073ac");
        list.push_back("#542788");
        break;
      case 9:
        list.push_back("#b35806");
        list.push_back("#e08214");
        list.push_back("#fdb863");
        list.push_back("#fee0b6");
        list.push_back("#f7f7f7");
        list.push_back("#d8daeb");
        list.push_back("#b2abd2");
        list.push_back("#8073ac");
        list.push_back("#542788");
        break;
      case 10:
        list.push_back("#7f3b08");
        list.push_back("#b35806");
        list.push_back("#e08214");
        list.push_back("#fdb863");
        list.push_back("#fee0b6");
        list.push_back("#d8daeb");
        list.push_back("#b2abd2");
        list.push_back("#8073ac");
        list.push_back("#542788");
        list.push_back("#2d004b");
        break;
      case 11:
        list.push_back("#7f3b08");
        list.push_back("#b35806");
        list.push_back("#e08214");
        list.push_back("#fdb863");
        list.push_back("#fee0b6");
        list.push_back("#f7f7f7");
        list.push_back("#d8daeb");
        list.push_back("#b2abd2");
        list.push_back("#8073ac");
        list.push_back("#542788");
        list.push_back("#2d004b");
        break;
    }
  }
  else if (colorName.compare("BrBG") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#d8b365");
        list.push_back("#f5f5f5");
        list.push_back("#5ab4ac");
        break;
      case 4:
        list.push_back("#a6611a");
        list.push_back("#dfc27d");
        list.push_back("#80cdc1");
        list.push_back("#018571");
        break;
      case 5:
        list.push_back("#a6611a");
        list.push_back("#dfc27d");
        list.push_back("#f5f5f5");
        list.push_back("#80cdc1");
        list.push_back("#018571");
        break;
      case 6:
        list.push_back("#8c510a");
        list.push_back("#d8b365");
        list.push_back("#f6e8c3");
        list.push_back("#c7eae5");
        list.push_back("#5ab4ac");
        list.push_back("#01665e");
        break;
      case 7:
        list.push_back("#8c510a");
        list.push_back("#d8b365");
        list.push_back("#f6e8c3");
        list.push_back("#f5f5f5");
        list.push_back("#c7eae5");
        list.push_back("#5ab4ac");
        list.push_back("#01665e");
        break;
      case 8:
        list.push_back("#8c510a");
        list.push_back("#bf812d");
        list.push_back("#dfc27d");
        list.push_back("#f6e8c3");
        list.push_back("#c7eae5");
        list.push_back("#80cdc1");
        list.push_back("#35978f");
        list.push_back("#01665e");
        break;
      case 9:
        list.push_back("#8c510a");
        list.push_back("#bf812d");
        list.push_back("#dfc27d");
        list.push_back("#f6e8c3");
        list.push_back("#f5f5f5");
        list.push_back("#c7eae5");
        list.push_back("#80cdc1");
        list.push_back("#35978f");
        list.push_back("#01665e");
        break;
      case 10:
        list.push_back("#543005");
        list.push_back("#8c510a");
        list.push_back("#bf812d");
        list.push_back("#dfc27d");
        list.push_back("#f6e8c3");
        list.push_back("#c7eae5");
        list.push_back("#80cdc1");
        list.push_back("#35978f");
        list.push_back("#01665e");
        list.push_back("#003c30");
        break;
      case 11:
        list.push_back("#543005");
        list.push_back("#8c510a");
        list.push_back("#bf812d");
        list.push_back("#dfc27d");
        list.push_back("#f6e8c3");
        list.push_back("#f5f5f5");
        list.push_back("#c7eae5");
        list.push_back("#80cdc1");
        list.push_back("#35978f");
        list.push_back("#01665e");
        list.push_back("#003c30");
        break;
    }
  }
  else if (colorName.compare("PRGn") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#af8dc3");
        list.push_back("#f7f7f7");
        list.push_back("#7fbf7b");
        break;
      case 4:
        list.push_back("#7b3294");
        list.push_back("#c2a5cf");
        list.push_back("#a6dba0");
        list.push_back("#008837");
        break;
      case 5:
        list.push_back("#7b3294");
        list.push_back("#c2a5cf");
        list.push_back("#f7f7f7");
        list.push_back("#a6dba0");
        list.push_back("#008837");
        break;
      case 6:
        list.push_back("#762a83");
        list.push_back("#af8dc3");
        list.push_back("#e7d4e8");
        list.push_back("#d9f0d3");
        list.push_back("#7fbf7b");
        list.push_back("#1b7837");
        break;
      case 7:
        list.push_back("#762a83");
        list.push_back("#af8dc3");
        list.push_back("#e7d4e8");
        list.push_back("#f7f7f7");
        list.push_back("#d9f0d3");
        list.push_back("#7fbf7b");
        list.push_back("#1b7837");
        break;
      case 8:
        list.push_back("#762a83");
        list.push_back("#9970ab");
        list.push_back("#c2a5cf");
        list.push_back("#e7d4e8");
        list.push_back("#d9f0d3");
        list.push_back("#a6dba0");
        list.push_back("#5aae61");
        list.push_back("#1b7837");
        break;
      case 9:
        list.push_back("#762a83");
        list.push_back("#9970ab");
        list.push_back("#c2a5cf");
        list.push_back("#e7d4e8");
        list.push_back("#f7f7f7");
        list.push_back("#d9f0d3");
        list.push_back("#a6dba0");
        list.push_back("#5aae61");
        list.push_back("#1b7837");
        break;
      case 10:
        list.push_back("#40004b");
        list.push_back("#762a83");
        list.push_back("#9970ab");
        list.push_back("#c2a5cf");
        list.push_back("#e7d4e8");
        list.push_back("#d9f0d3");
        list.push_back("#a6dba0");
        list.push_back("#5aae61");
        list.push_back("#1b7837");
        list.push_back("#00441b");
        break;
      case 11:
        list.push_back("#40004b");
        list.push_back("#762a83");
        list.push_back("#9970ab");
        list.push_back("#c2a5cf");
        list.push_back("#e7d4e8");
        list.push_back("#f7f7f7");
        list.push_back("#d9f0d3");
        list.push_back("#a6dba0");
        list.push_back("#5aae61");
        list.push_back("#1b7837");
        list.push_back("#00441b");
        break;
    }
  }
  else if (colorName.compare("PiYG") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e9a3c9");
        list.push_back("#f7f7f7");
        list.push_back("#a1d76a");
        break;
      case 4:
        list.push_back("#d01c8b");
        list.push_back("#f1b6da");
        list.push_back("#b8e186");
        list.push_back("#4dac26");
        break;
      case 5:
        list.push_back("#d01c8b");
        list.push_back("#f1b6da");
        list.push_back("#f7f7f7");
        list.push_back("#b8e186");
        list.push_back("#4dac26");
        break;
      case 6:
        list.push_back("#c51b7d");
        list.push_back("#e9a3c9");
        list.push_back("#fde0ef");
        list.push_back("#e6f5d0");
        list.push_back("#a1d76a");
        list.push_back("#4d9221");
        break;
      case 7:
        list.push_back("#c51b7d");
        list.push_back("#e9a3c9");
        list.push_back("#fde0ef");
        list.push_back("#f7f7f7");
        list.push_back("#e6f5d0");
        list.push_back("#a1d76a");
        list.push_back("#4d9221");
        break;
      case 8:
        list.push_back("#c51b7d");
        list.push_back("#de77ae");
        list.push_back("#f1b6da");
        list.push_back("#fde0ef");
        list.push_back("#e6f5d0");
        list.push_back("#b8e186");
        list.push_back("#7fbc41");
        list.push_back("#4d9221");
        break;
      case 9:
        list.push_back("#c51b7d");
        list.push_back("#de77ae");
        list.push_back("#f1b6da");
        list.push_back("#fde0ef");
        list.push_back("#f7f7f7");
        list.push_back("#e6f5d0");
        list.push_back("#b8e186");
        list.push_back("#7fbc41");
        list.push_back("#4d9221");
        break;
      case 10:
        list.push_back("#8e0152");
        list.push_back("#c51b7d");
        list.push_back("#de77ae");
        list.push_back("#f1b6da");
        list.push_back("#fde0ef");
        list.push_back("#e6f5d0");
        list.push_back("#b8e186");
        list.push_back("#7fbc41");
        list.push_back("#4d9221");
        list.push_back("#276419");
        break;
      case 11:
        list.push_back("#8e0152");
        list.push_back("#c51b7d");
        list.push_back("#de77ae");
        list.push_back("#f1b6da");
        list.push_back("#fde0ef");
        list.push_back("#f7f7f7");
        list.push_back("#e6f5d0");
        list.push_back("#b8e186");
        list.push_back("#7fbc41");
        list.push_back("#4d9221");
        list.push_back("#276419");
        break;
    }
  }
  else if (colorName.compare("RdBu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#ef8a62");
        list.push_back("#f7f7f7");
        list.push_back("#67a9cf");
        break;
      case 4:
        list.push_back("#ca0020");
        list.push_back("#f4a582");
        list.push_back("#92c5de");
        list.push_back("#0571b0");
        break;
      case 5:
        list.push_back("#ca0020");
        list.push_back("#f4a582");
        list.push_back("#f7f7f7");
        list.push_back("#92c5de");
        list.push_back("#0571b0");
        break;
      case 6:
        list.push_back("#b2182b");
        list.push_back("#ef8a62");
        list.push_back("#fddbc7");
        list.push_back("#d1e5f0");
        list.push_back("#67a9cf");
        list.push_back("#2166ac");
        break;
      case 7:
        list.push_back("#b2182b");
        list.push_back("#ef8a62");
        list.push_back("#fddbc7");
        list.push_back("#f7f7f7");
        list.push_back("#d1e5f0");
        list.push_back("#67a9cf");
        list.push_back("#2166ac");
        break;
      case 8:
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#d1e5f0");
        list.push_back("#92c5de");
        list.push_back("#4393c3");
        list.push_back("#2166ac");
        break;
      case 9:
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#f7f7f7");
        list.push_back("#d1e5f0");
        list.push_back("#92c5de");
        list.push_back("#4393c3");
        list.push_back("#2166ac");
        break;
      case 10:
        list.push_back("#67001f");
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#d1e5f0");
        list.push_back("#92c5de");
        list.push_back("#4393c3");
        list.push_back("#2166ac");
        list.push_back("#053061");
        break;
      case 11:
        list.push_back("#67001f");
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#f7f7f7");
        list.push_back("#d1e5f0");
        list.push_back("#92c5de");
        list.push_back("#4393c3");
        list.push_back("#2166ac");
        list.push_back("#053061");
        break;
    }
  }
  else if (colorName.compare("RdGy") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#ef8a62");
        list.push_back("#ffffff");
        list.push_back("#999999");
        break;
      case 4:
        list.push_back("#ca0020");
        list.push_back("#f4a582");
        list.push_back("#bababa");
        list.push_back("#404040");
        break;
      case 5:
        list.push_back("#ca0020");
        list.push_back("#f4a582");
        list.push_back("#ffffff");
        list.push_back("#bababa");
        list.push_back("#404040");
        break;
      case 6:
        list.push_back("#b2182b");
        list.push_back("#ef8a62");
        list.push_back("#fddbc7");
        list.push_back("#e0e0e0");
        list.push_back("#999999");
        list.push_back("#4d4d4d");
        break;
      case 7:
        list.push_back("#b2182b");
        list.push_back("#ef8a62");
        list.push_back("#fddbc7");
        list.push_back("#ffffff");
        list.push_back("#e0e0e0");
        list.push_back("#999999");
        list.push_back("#4d4d4d");
        break;
      case 8:
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#e0e0e0");
        list.push_back("#bababa");
        list.push_back("#878787");
        list.push_back("#4d4d4d");
        break;
      case 9:
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#ffffff");
        list.push_back("#e0e0e0");
        list.push_back("#bababa");
        list.push_back("#878787");
        list.push_back("#4d4d4d");
        break;
      case 10:
        list.push_back("#67001f");
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#e0e0e0");
        list.push_back("#bababa");
        list.push_back("#878787");
        list.push_back("#4d4d4d");
        list.push_back("#1a1a1a");
        break;
      case 11:
        list.push_back("#67001f");
        list.push_back("#b2182b");
        list.push_back("#d6604d");
        list.push_back("#f4a582");
        list.push_back("#fddbc7");
        list.push_back("#ffffff");
        list.push_back("#e0e0e0");
        list.push_back("#bababa");
        list.push_back("#878787");
        list.push_back("#4d4d4d");
        list.push_back("#1a1a1a");
        break;
    }
  }
  else if (colorName.compare("RdYlBu") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fc8d59");
        list.push_back("#ffffbf");
        list.push_back("#91bfdb");
        break;
      case 4:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#abd9e9");
        list.push_back("#2c7bb6");
        break;
      case 5:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#ffffbf");
        list.push_back("#abd9e9");
        list.push_back("#2c7bb6");
        break;
      case 6:
        list.push_back("#d73027");
        list.push_back("#fc8d59");
        list.push_back("#fee090");
        list.push_back("#e0f3f8");
        list.push_back("#91bfdb");
        list.push_back("#4575b4");
        break;
      case 7:
        list.push_back("#d73027");
        list.push_back("#fc8d59");
        list.push_back("#fee090");
        list.push_back("#ffffbf");
        list.push_back("#e0f3f8");
        list.push_back("#91bfdb");
        list.push_back("#4575b4");
        break;
      case 8:
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee090");
        list.push_back("#e0f3f8");
        list.push_back("#abd9e9");
        list.push_back("#74add1");
        list.push_back("#4575b4");
        break;
      case 9:
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee090");
        list.push_back("#ffffbf");
        list.push_back("#e0f3f8");
        list.push_back("#abd9e9");
        list.push_back("#74add1");
        list.push_back("#4575b4");
        break;
      case 10:
        list.push_back("#a50026");
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee090");
        list.push_back("#e0f3f8");
        list.push_back("#abd9e9");
        list.push_back("#74add1");
        list.push_back("#4575b4");
        list.push_back("#313695");
        break;
      case 11:
        list.push_back("#a50026");
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee090");
        list.push_back("#ffffbf");
        list.push_back("#e0f3f8");
        list.push_back("#abd9e9");
        list.push_back("#74add1");
        list.push_back("#4575b4");
        list.push_back("#313695");
        break;
    }
  }
  else if (colorName.compare("Spectral") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fc8d59");
        list.push_back("#ffffbf");
        list.push_back("#99d594");
        break;
      case 4:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#abdda4");
        list.push_back("#2b83ba");
        break;
      case 5:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#ffffbf");
        list.push_back("#abdda4");
        list.push_back("#2b83ba");
        break;
      case 6:
        list.push_back("#d53e4f");
        list.push_back("#fc8d59");
        list.push_back("#fee08b");
        list.push_back("#e6f598");
        list.push_back("#99d594");
        list.push_back("#3288bd");
        break;
      case 7:
        list.push_back("#d53e4f");
        list.push_back("#fc8d59");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#e6f598");
        list.push_back("#99d594");
        list.push_back("#3288bd");
        break;
      case 8:
        list.push_back("#d53e4f");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#e6f598");
        list.push_back("#abdda4");
        list.push_back("#66c2a5");
        list.push_back("#3288bd");
        break;
      case 9:
        list.push_back("#d53e4f");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#e6f598");
        list.push_back("#abdda4");
        list.push_back("#66c2a5");
        list.push_back("#3288bd");
        break;
      case 10:
        list.push_back("#9e0142");
        list.push_back("#d53e4f");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#e6f598");
        list.push_back("#abdda4");
        list.push_back("#66c2a5");
        list.push_back("#3288bd");
        list.push_back("#5e4fa2");
        break;
      case 11:
        list.push_back("#9e0142");
        list.push_back("#d53e4f");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#e6f598");
        list.push_back("#abdda4");
        list.push_back("#66c2a5");
        list.push_back("#3288bd");
        list.push_back("#5e4fa2");
        break;
    }
  }
  else if (colorName.compare("RdYlGn") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fc8d59");
        list.push_back("#ffffbf");
        list.push_back("#91cf60");
        break;
      case 4:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#a6d96a");
        list.push_back("#1a9641");
        break;
      case 5:
        list.push_back("#d7191c");
        list.push_back("#fdae61");
        list.push_back("#ffffbf");
        list.push_back("#a6d96a");
        list.push_back("#1a9641");
        break;
      case 6:
        list.push_back("#d73027");
        list.push_back("#fc8d59");
        list.push_back("#fee08b");
        list.push_back("#d9ef8b");
        list.push_back("#91cf60");
        list.push_back("#1a9850");
        break;
      case 7:
        list.push_back("#d73027");
        list.push_back("#fc8d59");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#d9ef8b");
        list.push_back("#91cf60");
        list.push_back("#1a9850");
        break;
      case 8:
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#d9ef8b");
        list.push_back("#a6d96a");
        list.push_back("#66bd63");
        list.push_back("#1a9850");
        break;
      case 9:
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#d9ef8b");
        list.push_back("#a6d96a");
        list.push_back("#66bd63");
        list.push_back("#1a9850");
        break;
      case 10:
        list.push_back("#a50026");
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#d9ef8b");
        list.push_back("#a6d96a");
        list.push_back("#66bd63");
        list.push_back("#1a9850");
        list.push_back("#006837");
        break;
      case 11:
        list.push_back("#a50026");
        list.push_back("#d73027");
        list.push_back("#f46d43");
        list.push_back("#fdae61");
        list.push_back("#fee08b");
        list.push_back("#ffffbf");
        list.push_back("#d9ef8b");
        list.push_back("#a6d96a");
        list.push_back("#66bd63");
        list.push_back("#1a9850");
        list.push_back("#006837");
        break;
    }
  }
  else if (colorName.compare("Accent") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        break;
      case 4:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        list.push_back("#ffff99");
        break;
      case 5:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        list.push_back("#ffff99");
        list.push_back("#386cb0");
        break;
      case 6:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        list.push_back("#ffff99");
        list.push_back("#386cb0");
        list.push_back("#f0027f");
        break;
      case 7:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        list.push_back("#ffff99");
        list.push_back("#386cb0");
        list.push_back("#f0027f");
        list.push_back("#bf5b17");
        break;
      case 8:
        list.push_back("#7fc97f");
        list.push_back("#beaed4");
        list.push_back("#fdc086");
        list.push_back("#ffff99");
        list.push_back("#386cb0");
        list.push_back("#f0027f");
        list.push_back("#bf5b17");
        list.push_back("#666666");
        break;
    }
  }
  else if (colorName.compare("Dark2") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        break;
      case 4:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        list.push_back("#e7298a");
        break;
      case 5:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        list.push_back("#e7298a");
        list.push_back("#66a61e");
        break;
      case 6:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        list.push_back("#e7298a");
        list.push_back("#66a61e");
        list.push_back("#e6ab02");
        break;
      case 7:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        list.push_back("#e7298a");
        list.push_back("#66a61e");
        list.push_back("#e6ab02");
        list.push_back("#a6761d");
        break;
      case 8:
        list.push_back("#1b9e77");
        list.push_back("#d95f02");
        list.push_back("#7570b3");
        list.push_back("#e7298a");
        list.push_back("#66a61e");
        list.push_back("#e6ab02");
        list.push_back("#a6761d");
        list.push_back("#666666");
        break;
    }
  }
  else if (colorName.compare("Paired") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        break;
      case 4:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        break;
      case 5:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        break;
      case 6:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        break;
      case 7:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        break;
      case 8:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        list.push_back("#ff7f00");
        break;
      case 9:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        list.push_back("#ff7f00");
        list.push_back("#cab2d6");
        break;
      case 10:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        list.push_back("#ff7f00");
        list.push_back("#cab2d6");
        list.push_back("#6a3d9a");
        break;
      case 11:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        list.push_back("#ff7f00");
        list.push_back("#cab2d6");
        list.push_back("#6a3d9a");
        list.push_back("#ffff99");
        break;
      case 12:
        list.push_back("#a6cee3");
        list.push_back("#1f78b4");
        list.push_back("#b2df8a");
        list.push_back("#33a02c");
        list.push_back("#fb9a99");
        list.push_back("#e31a1c");
        list.push_back("#fdbf6f");
        list.push_back("#ff7f00");
        list.push_back("#cab2d6");
        list.push_back("#6a3d9a");
        list.push_back("#ffff99");
        list.push_back("#b15928");
        break;
    }
  }
  else if (colorName.compare("Pastel1") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        break;
      case 4:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        break;
      case 5:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        list.push_back("#fed9a6");
        break;
      case 6:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        list.push_back("#fed9a6");
        list.push_back("#ffffcc");
        break;
      case 7:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        list.push_back("#fed9a6");
        list.push_back("#ffffcc");
        list.push_back("#e5d8bd");
        break;
      case 8:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        list.push_back("#fed9a6");
        list.push_back("#ffffcc");
        list.push_back("#e5d8bd");
        list.push_back("#fddaec");
        break;
      case 9:
        list.push_back("#fbb4ae");
        list.push_back("#b3cde3");
        list.push_back("#ccebc5");
        list.push_back("#decbe4");
        list.push_back("#fed9a6");
        list.push_back("#ffffcc");
        list.push_back("#e5d8bd");
        list.push_back("#fddaec");
        list.push_back("#f2f2f2");
        break;
    }
  }
  else if (colorName.compare("Pastel2") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        break;
      case 4:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        list.push_back("#f4cae4");
        break;
      case 5:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        list.push_back("#f4cae4");
        list.push_back("#e6f5c9");
        break;
      case 6:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        list.push_back("#f4cae4");
        list.push_back("#e6f5c9");
        list.push_back("#fff2ae");
        break;
      case 7:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        list.push_back("#f4cae4");
        list.push_back("#e6f5c9");
        list.push_back("#fff2ae");
        list.push_back("#f1e2cc");
        break;
      case 8:
        list.push_back("#b3e2cd");
        list.push_back("#fdcdac");
        list.push_back("#cbd5e8");
        list.push_back("#f4cae4");
        list.push_back("#e6f5c9");
        list.push_back("#fff2ae");
        list.push_back("#f1e2cc");
        list.push_back("#cccccc");
        break;
    }
  }
  else if (colorName.compare("Set1") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        break;
      case 4:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        break;
      case 5:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        list.push_back("#ff7f00");
        break;
      case 6:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        list.push_back("#ff7f00");
        list.push_back("#ffff33");
        break;
      case 7:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        list.push_back("#ff7f00");
        list.push_back("#ffff33");
        list.push_back("#a65628");
        break;
      case 8:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        list.push_back("#ff7f00");
        list.push_back("#ffff33");
        list.push_back("#a65628");
        list.push_back("#f781bf");
        break;
      case 9:
        list.push_back("#e41a1c");
        list.push_back("#377eb8");
        list.push_back("#4daf4a");
        list.push_back("#984ea3");
        list.push_back("#ff7f00");
        list.push_back("#ffff33"); 
        list.push_back("#a65628"); 
        list.push_back("#f781bf"); 
        list.push_back("#999999"); 
        break;
    }
  }
  else if (colorName.compare("Set2") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        break;
      case 4:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        list.push_back("#e78ac3"); 
        break;
      case 5:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        list.push_back("#e78ac3"); 
        list.push_back("#a6d854"); 
        break;
      case 6:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        list.push_back("#e78ac3"); 
        list.push_back("#a6d854"); 
        list.push_back("#ffd92f"); 
        break;
      case 7:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        list.push_back("#e78ac3"); 
        list.push_back("#a6d854"); 
        list.push_back("#ffd92f"); 
        list.push_back("#e5c494"); 
        break;
      case 8:
        list.push_back("#66c2a5"); 
        list.push_back("#fc8d62"); 
        list.push_back("#8da0cb"); 
        list.push_back("#e78ac3"); 
        list.push_back("#a6d854"); 
        list.push_back("#ffd92f"); 
        list.push_back("#e5c494"); 
        list.push_back("#b3b3b3"); 
        break;
    }
  }
  else if (colorName.compare("Set3") == 0) {
    switch (colorCount) {
      case 3:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        break;
      case 4:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        break;
      case 5:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        break;
      case 6:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        break;
      case 7:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        break;
      case 8:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        list.push_back("#fccde5"); 
        break;
      case 9:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        list.push_back("#fccde5"); 
        list.push_back("#d9d9d9"); 
        break;
      case 10:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        list.push_back("#fccde5"); 
        list.push_back("#d9d9d9"); 
        list.push_back("#bc80bd"); 
        break;
      case 11:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        list.push_back("#fccde5"); 
        list.push_back("#d9d9d9"); 
        list.push_back("#bc80bd"); 
        list.push_back("#ccebc5"); 
        break;
      case 12:
        list.push_back("#8dd3c7"); 
        list.push_back("#ffffb3"); 
        list.push_back("#bebada"); 
        list.push_back("#fb8072"); 
        list.push_back("#80b1d3"); 
        list.push_back("#fdb462"); 
        list.push_back("#b3de69"); 
        list.push_back("#fccde5"); 
        list.push_back("#d9d9d9"); 
        list.push_back("#bc80bd"); 
        list.push_back("#ccebc5"); 
        list.push_back("#ffed6f"); 
        break;
    }
  }
  return list;
}

