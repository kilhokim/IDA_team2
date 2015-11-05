package project_team2.util;

/**
 * Created by kilho on 15. 11. 5.
 */
public class Converters {

  public static String
          UNDER_50 = "0",
          OVER_50_UNDER_65 = "1",
          OVER_65_UNDER_80 = "2",
          OVER_80 = "3";
//          UNDER_40 = "0",
//          OVER_40_UNDER_50 = "1",
//          OVER_50_UNDER_60 = "2",
//          OVER_60_UNDER_70 = "3",
//          OVER_70_UNDER_80 = "4",
//          OVER_80_UNDER_90 = "5",
//          OVER_90 = "6";

  public static String weightToClassNum(double weight) {
//		int classNum = -1;
    String classNum = "";
    if (weight < 50)                      classNum = UNDER_50;
    else if (50 <= weight && weight < 65) classNum = OVER_50_UNDER_65;
    else if (65 <= weight && weight < 80) classNum = OVER_65_UNDER_80;
    else  /* 80 <= weight */						  classNum = OVER_80;
//    if (weight < 40)                      classNum = UNDER_40;
//    else if (40 <= weight && weight < 50) classNum = OVER_40_UNDER_50;
//    else if (50 <= weight && weight < 60) classNum = OVER_50_UNDER_60;
//    else if (60 <= weight && weight < 70) classNum = OVER_60_UNDER_70;
//    else if (70 <= weight && weight < 80) classNum = OVER_70_UNDER_80;
//    else if (80 <= weight && weight < 90) classNum = OVER_80_UNDER_90;
//    else  /* 90 <= weight */						  classNum = OVER_90;

    return classNum;
  }

  public static double classNumToWeight(String classNum) {
    double weight = 0;
    if      (classNum.equals(UNDER_50))         weight = 42.5;
    else if (classNum.equals(OVER_50_UNDER_65)) weight = 57.5;
    else if (classNum.equals(OVER_65_UNDER_80)) weight = 72.5;
    else  /* classNum.equals(Keys.OVER_80) */   weight = 87.5;
//    if      (classNum.equals(UNDER_40))         weight = 40;
//    else if (classNum.equals(OVER_40_UNDER_50)) weight = 45;
//    else if (classNum.equals(OVER_50_UNDER_60)) weight = 55;
//    else if (classNum.equals(OVER_60_UNDER_70)) weight = 65;
//    else if (classNum.equals(OVER_70_UNDER_80)) weight = 75;
//    else if (classNum.equals(OVER_80_UNDER_90)) weight = 85;
//    else  /* classNum.equals(Keys.OVER_90) */   weight = 90;

    return weight;
  }

}
