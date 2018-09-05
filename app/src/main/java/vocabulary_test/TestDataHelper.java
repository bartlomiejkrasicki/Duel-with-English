package vocabulary_test;

public class TestDataHelper {

    public static int manyGoodAnswer = 0, manyTestWords = 0, randomNumberOfWords[];
    public static boolean inEnglish[], isTestFromLesson = true;
    public static int amountOfWords = 0, amountOfButtons = 0;
    public static String lvlOfLanguage = "", categoryName = "";

    public static double calculatePercentage(){
        return Math.round((manyGoodAnswer * 100)/amountOfWords);
    }
}
