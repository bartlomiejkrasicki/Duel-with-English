package vocabulary_level_category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cuboid.cuboidcirclebutton.CuboidButton;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vocabulary_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void declarationVariables(){
        CuboidButton A1Button = (CuboidButton) findViewById(R.id.button_category_A1);
        setButtonOnClick(A1Button);
        CuboidButton A2Button = (CuboidButton) findViewById(R.id.button_category_A2);
        setButtonOnClick(A2Button);
        CuboidButton B1Button = (CuboidButton) findViewById(R.id.button_category_B1);
        setButtonOnClick(B1Button);
        CuboidButton B2Button = (CuboidButton) findViewById(R.id.button_category_B2);
        setButtonOnClick(B2Button);
        CuboidButton C1Button = (CuboidButton) findViewById(R.id.button_category_C1);
        setButtonOnClick(C1Button);
        CuboidButton C2Button = (CuboidButton) findViewById(R.id.button_category_C2);
        setButtonOnClick(C2Button);
        intent = new Intent(this, LessonsVocabularyList.class);

    }

    private void setButtonOnClick(final CuboidButton categoryButton){
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("levelLanguage", categoryButton.getText().toString());
                startActivity(intent);
            }
        });
    }

}
