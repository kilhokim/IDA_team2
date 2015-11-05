package project_team2.util;

/**
 * Created by kilho on 15. 11. 5.
 */
public class Converters {

  // FIXME:
  public static String weightToClassNum(double weight) {
//		int classNum = -1;
    String classNum = "";
    if (weight < 40) classNum = Keys.UNDER_40;
    else if (40 <= weight && weight < 50) classNum = Keys.OVER_40_UNDER_50;
    else if (50 <= weight && weight < 60) classNum = Keys.OVER_50_UNDER_60;
    else if (60 <= weight && weight < 70) classNum = Keys.OVER_60_UNDER_70;
    else if (70 <= weight && weight < 80) classNum = Keys.OVER_70_UNDER_80;
    else if (80 <= weight && weight < 90) classNum = Keys.OVER_80_UNDER_90;
    else  /* 90 <= weight */						  classNum = Keys.OVER_90;

    return classNum;
  }

  public static double classNumToWeight(String classNum) {
    double weight = 0;
    if      (classNum.equals(Keys.UNDER_40))         weight = 40;
    else if (classNum.equals(Keys.OVER_40_UNDER_50)) weight = 45;
    else if (classNum.equals(Keys.OVER_50_UNDER_60)) weight = 55;
    else if (classNum.equals(Keys.OVER_60_UNDER_70)) weight = 65;
    else if (classNum.equals(Keys.OVER_70_UNDER_80)) weight = 75;
    else if (classNum.equals(Keys.OVER_80_UNDER_90)) weight = 85;
    else  /* classNum.equals(Keys.OVER_90) */        weight = 90;

    return weight;
  }

}
