package vocabulary_test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDataHelper {

    public static int manyGoodAnswer = 0, manyTestWords = 0, randomNumberOfWords[];
    public static boolean inEnglish[], isTestFromLesson = true;
    public static int amountOfWords = 0, amountOfButtons = 0;
    public static String lvlOfLanguage = "", categoryName = "";
    public static Integer[] wordTable;

    public static double calculatePercentage(){
        return Math.round((manyGoodAnswer * 100)/amountOfWords);
    }



    public static Integer[] prepareWordRandomTable (){

        Integer[] numberTable = new Integer[amountOfWords];

        for (int i = 0; i< amountOfWords; i++){
            numberTable[i] = i;
        }

        List<Integer> numberList = Arrays.asList(numberTable);

        Collections.shuffle(numberList);

//        Random random = new Random();
//        boolean numberIsOther;
//        for(int i = 0; i < randomWordsWithoutReply.length; i++){
//            if(i==0){
//                randomWordsWithoutReply[i] = random.nextInt(cursor.getCount());
//            }
//            else {
//                do {
//                    numberIsOther = true;
//                    randomWordsWithoutReply[i] = random.nextInt(cursor.getCount());
//                    for(int j = 0; j < i; j++){
//                        if(randomWordsWithoutReply[j]==randomWordsWithoutReply[i]){
//                            numberIsOther = true;
//                            break;
//                        } else {
//                           numberIsOther = false;
//                        }
//                    }
//                } while (numberIsOther);
//            }
//        }
//        cursor.close();
        return numberTable;
    }
}
