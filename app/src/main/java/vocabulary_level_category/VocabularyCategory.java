package vocabulary_level_category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import lessons_vocabulary_list.LessonsVocabularyList;
import pl.flanelowapopijava.duel_with_english.R;

public class VocabularyCategory extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_category);
        declarationVariables();
    }

    private void declarationVariables(){
        Button A1Button = (Button) findViewById(R.id.button_category_A1);
        setButtonOnClick(A1Button);
        Button A2Button = (Button) findViewById(R.id.button_category_A2);
        setButtonOnClick(A2Button);
        Button B1Button = (Button) findViewById(R.id.button_category_B1);
        setButtonOnClick(B1Button);
        Button B2Button = (Button) findViewById(R.id.button_category_B2);
        setButtonOnClick(B2Button);
        Button C1Button = (Button) findViewById(R.id.button_category_C1);
        setButtonOnClick(C1Button);
        Button C2Button = (Button) findViewById(R.id.button_category_C2);
        setButtonOnClick(C2Button);
        intent = new Intent(this, LessonsVocabularyList.class);

    }

    private void setButtonOnClick(final Button categoryButton){
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("levelLanguage", categoryButton.getText().toString());
                startActivity(intent);
            }
        });
    }
}
