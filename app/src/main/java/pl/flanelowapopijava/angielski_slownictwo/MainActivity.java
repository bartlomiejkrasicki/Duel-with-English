package pl.flanelowapopijava.angielski_slownictwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //menu glowne
    Button vocabularyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // wyświetla xml activity_main
        vocabularyButton = (Button) findViewById(R.id.button_vocabulary);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                 // menu
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mainMenuSettings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void vocabularyButtonOnClick(View view){         //klikniecie przycisku słownictwo
        Intent intent = new Intent(this, VocabularyList.class);
        startActivity(intent);
    }

    public void favouriteButtonOnClick(View view){
        Intent intent = new Intent(this, FavouriteList.class);
        startActivity(intent);
    }
}
