package project_team2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 2015-09-24.
 */
public class Feature {

//    // TODO:
//    public double avgX;
//    public double avgY;
//    public double avgZ;

    /**
     * @author Kilho Kim
     * @description ApplicationsProbe features
     */
    public double numBooksReference,numBusiness,numComics,numCommuncation,
               numEducation,numEntertainment,numFinanace,numHealthFitness,
               numLibrariesDemo,numLifestyle,numLiveWallpaper,numMediaVideo,
               numMedical,numMusicAudio,numNewsMagazines,numPersonalization,
               numPhotography,numProductivity,numShopping,numSocial,
               numSports,numTools,numTransportation,numTravelLocal,
               numWeather,numWidgets,numAction,numAdventure,numArcade,
               numBoard,numCard,numCasino,numCasual,numEducational,
               numMusic,numPuzzle,numRacing,numRolePlaying,numSimulation,
               /*numSports,*/numStrategy,numTrivia,numWord,numAges5Under,
               numAges6_8,numAges9Up,numPopularCharacters,numActionAdventure,
               numBrainGames,numCreativity,/*numFamilyEducation;*/
               numMusicVideo,numPretendPlay;
    public static String[] categoryNames = {
      "Books&Reference", "Business", "Comics", "Communication",
      "Education", "Entertainment", "Finance", "Health&Fitness",
      "Libraries&Demo", "Lifestyle", "LiveWallpaper", "Media&Video",
      "Medical", "Music&Audio", "News&Magazines", "Personalization",
      "Photography", "Productivity", "Shopping", "Social",
      "Sports", "Tools", "Transportation", "Travel&Local",
      "Weather", "Widgets", "Action", "Adventure", "Arcade",
      "Board", "Card", "Casino", "Casual", "Educational",
      "Music", "Puzzle", "Racing", "RolePlaying", "Simulation",
      /*"Sports",*/ "Strategy", "Trivia", "Word", "Ages5&Under",
      "Ages6-8", "Ages9&Up", "PopularCharacters", "Action&Adventure",
      "BrainGames", "Creativity",/*FamilyEducation,*/"Music&Video",
      "PretendPlay"
    };
    public static Map<String, Integer> categoryMap;
    static {
        categoryMap = new HashMap<String, Integer>();
        for (int i = 0; i < categoryNames.length; i++) {
            categoryMap.put(categoryNames[i], i);
        }
    }

    /**
     * @author Kilho Kim
     * @description ImageMediaProbe features
     */
    public double top1BucketPhotoRatio;
    public double imageMediaSize;

    /**
     * @author Kilho Kim
     * @description VideoMediaProbe features
     */
    public double top1BucketVideoRatio;
    public double videoMediaSize;
    public double videoDuration;

    /**
     * @author Kilho Kim
     * @description AudioMediaProbe features
     */
    public double numAudios;
    public double audioMediaSize;
    public double audioDuration;

    /**
     * @author Kilho Kim
     * @description SmsProbe features
     */
    public double numSmss;
    public double top3AddressRatio;
    public double avgSmsInterval;

    /**
     * @author Kilho Kim
     * @description CallLogProbe features
     */
    public double numCallLogs;
    public double callDuration;
    public double top3NumberRatio;
    public double unknownCallRatio;
    public double callOutRatio;
    public double avgCallInterval;


    public String label;

    String[] numericAtts = {
               "numBooksReference","numBusiness","numComics","numCommuncation","numEducation","numEntertainment","numFinanace","numHealthFitness","numLibrariesDemo","numLifestyle","numLiveWallpaper","numMediaVideo","numMedical","numMusicAudio","numNewsMagazines","numPersonalization","numPhotography","numProductivity","numShopping","numSocial","numSports","numTools","numTransportation","numTravelLocal","numWeather","numWidgets","numAction","numAdventure","numArcade","numBoard","numCard","numCasino","numCasual","numEducational","numMusic","numPuzzle","numRacing","numRolePlaying","numSimulation",/*"numSports",*/"numStrategy","numTrivia","numWord","numAges5Under","numAges6_8","numAges9Up","numPopularCharacters","numActionAdventure","numBrainGames","numCreativity",/*"numFamilyEducation",*/"numMusicVideo","numPretendPlay",
            "top1BucketPhotoRatio", "imageMediaSize",
            "top1BucketVideoRatio", "videoMediaSize", "videoDuration",
            "numAudios", "audioMediaSize", "audioDuration",
            "numSmss", "top3AddressRatio", "avgSmsInterval",
            "numCallLogs", "callDuration", "top3NumberRatio", "unknownCallRatio", "callOutRatio", "avgCallInterval"
    };

    String[] nominalAtts = {"label"};   // label must be the last one!

    public Field[] getNumericAttributes(){
        Field[] fields = null;
        fields = new Field[numericAtts.length];
        for(int i=0; i<fields.length; i++){
            try {
                fields[i] = this.getClass().getField(numericAtts[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return fields;
    }

    public Field[] getNominalAttributes(){
        Field[] fields = null;
        fields = new Field[nominalAtts.length];
        for(int i=0; i<fields.length; i++){
            try {
                fields[i] = this.getClass().getField(nominalAtts[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return fields;
    }

    public void setValues_Accelerometer(String dataType, double[] values){
        if(dataType.equals("AccelerometerSensorProbe")){
//            avgX = values[0];
//            avgY = values[1];
//            avgZ = values[2];
        }
    }

    /**
     * @author Kilho Kim
     * @description Set values of Applications features
     * @param dataType
     * @param values
     */
    public void setValues_Applications(String dataType, int[] values) {
      if (dataType.equals("ApplicationsProbe")) {
        assert (values.length == Feature.categoryNames.length);
        numBooksReference = values[0];
        numBusiness = values[1];
        numComics = values[2];
        numCommuncation = values[3];
        numEducation = values[4];
        numEntertainment = values[5];
        numFinanace = values[6];
        numHealthFitness = values[7];
        numLibrariesDemo = values[8];
        numLifestyle = values[9];
        numLiveWallpaper = values[10];
        numMediaVideo = values[11];
        numMedical = values[12];
        numMusicAudio = values[13];
        numNewsMagazines = values[14];
        numPersonalization = values[15];
        numPhotography = values[16];
        numProductivity = values[17];
        numShopping = values[18];
        numSocial = values[19];
        /*numSports = values[]; */
        numTools = values[20];
        numTransportation = values[21];
        numTravelLocal = values[22];
        numWeather = values[23];
        numWidgets = values[24];
        numAction = values[25];
        numAdventure = values[26];
        numArcade = values[27];
        numBoard = values[28];
        numCard = values[29];
        numCasino = values[30];
        numCasual = values[31];
        numEducational = values[32];
        numMusic = values[33];
        numPuzzle = values[34];
        numRacing = values[35];
        numRolePlaying = values[36];
        numSimulation = values[37];
        numSports = values[38];
        numStrategy = values[39];
        numTrivia = values[40];
        numWord = values[41];
        numAges5Under = values[42];
        numAges6_8 = values[43];
        numAges9Up = values[44];
        numPopularCharacters = values[45];
        numActionAdventure = values[46];
        numBrainGames = values[47];
        numCreativity = values[48];
        /*numFamilyEducation = values[];*/
        numMusicVideo = values[49];
        numPretendPlay = values[50];
      }
    }

  public void setValues_ImageMedia(String dataType, double[] values){
    if (dataType.equals("ImageMediaProbe")) {
      assert (values.length == 2);
      top1BucketPhotoRatio = values[0];
      imageMediaSize = values[1];
    }
  }

  public void setValues_VideoMedia(String dataType, double[] values) {
    if (dataType.equals("VideoMediaProbe")) {
      assert (values.length == 3);
      top1BucketVideoRatio = values[0];
      videoMediaSize = values[1];
      videoDuration = values[2];
    }
  }

  public void setValues_AudioMedia(String dataType, int[] values) {
    if (dataType.equals("AudioMediaProbe")) {
      assert (values.length == 3);
      numAudios = values[0];
      audioMediaSize = values[1];
      audioDuration = values[2];
    }
  }

  public void setValues_Sms(String dataType, double[] values) {
    if (dataType.equals("SmsProbe")) {
      assert (values.length == 3);
      numSmss = values[0];
      top3AddressRatio = values[1];
      avgSmsInterval = values[2];
    }
  }

  public void setValues_CallLog(String dataType, double[] values) {
    if (dataType.equals("CallLogProbe")) {
      assert (values.length == 6);
      numCallLogs = values[0];
      callDuration = values[1];
      top3NumberRatio = values[2];
      unknownCallRatio = values[3];
      callOutRatio = values[4];
      avgCallInterval = values[5];
    }
  }


  public void setLabel(String label){
      this.label = label;
  }

  public String toString() {
    Object[] fields = {
      numBooksReference,numBusiness,numComics,numCommuncation,
            numEducation,numEntertainment,numFinanace,numHealthFitness,
            numLibrariesDemo,numLifestyle,numLiveWallpaper,numMediaVideo,
            numMedical,numMusicAudio,numNewsMagazines,numPersonalization,
            numPhotography,numProductivity,numShopping,numSocial,
            numSports,numTools,numTransportation,numTravelLocal,
            numWeather,numWidgets,numAction,numAdventure,numArcade,
            numBoard,numCard,numCasino,numCasual,numEducational,
            numMusic,numPuzzle,numRacing,numRolePlaying,numSimulation,
               /*numSports,*/numStrategy,numTrivia,numWord,numAges5Under,
            numAges6_8,numAges9Up,numPopularCharacters,numActionAdventure,
            numBrainGames,numCreativity,/*numFamilyEducation;*/
            numMusicVideo,numPretendPlay, top1BucketPhotoRatio, imageMediaSize,
            top1BucketVideoRatio, videoMediaSize, videoDuration, numAudios,
            audioMediaSize, audioDuration, numSmss, top3AddressRatio,
            avgSmsInterval, numCallLogs, callDuration, top3NumberRatio,
            unknownCallRatio, callOutRatio, avgCallInterval, label
    };
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < fields.length; i++) {
        sb.append(fields[i]);
        sb.append(" ");
    }
    return sb.toString();
  }
}
