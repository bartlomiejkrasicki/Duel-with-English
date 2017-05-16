package vocabulary_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.util.Random;

import pl.flanelowapopijava.angielski_slownictwo.R;
import test_fragments.VocabularyTestChoiceEnFragment;
import test_fragments.VocabularyTestChoicePlFragment;

public class VocabularyTest extends FragmentActivity {

    private Random random = new Random();
    public static int manyGoodAnswer = 0;
    public static int manyBadAnswer = 0;
    public static int manyTestWords = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        showFirstFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manyGoodAnswer = 0;
        manyTestWords = 0;
        manyBadAnswer = 0;
    }

    @Override
    public void recreate() {
        super.recreate();
        manyGoodAnswer = 0;
        manyTestWords = 0;
        manyBadAnswer = 0;
    }

    private void showFirstFragment(){
        switch (randomNumber(2)){
            case 0:{
                getSupportFragmentManager().beginTransaction().add(R.id.testFragmentId, new VocabularyTestChoiceEnFragment()).commit();
                break;
            }
            case 1:{
                getSupportFragmentManager().beginTransaction().add(R.id.testFragmentId, new VocabularyTestChoicePlFragment()).commit();
                break;
            }
            default:{
                Toast.makeText(this, "Przełączanie widoku na nowy nie zadziałało :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int randomNumber(int i){
        return random.nextInt(i);
    }

    public int getSPnumberOfWords(SharedPreferences sharedPreferences) {
        return Integer.parseInt(sharedPreferences.getString("numberOfWords", ""));
    }

    public int getSPlevelOfLanguage(SharedPreferences sharedPreferences){
        return Integer.parseInt(sharedPreferences.getString("levelOfLanguage", ""));
    }
}
