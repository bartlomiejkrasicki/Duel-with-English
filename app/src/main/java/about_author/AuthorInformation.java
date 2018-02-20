package about_author;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.flanelowapopijava.duel_with_english.R;

public class AuthorInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_information);
    }

    public void rateAppOnClick(View view) {
//        do uzupełnienia
    }

    public void sendEmailOnClick(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "barkrasicki@gmail.com");
        emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
        startActivity(Intent.createChooser(emailIntent, "Wyślij wiadomość do autora"));
    }
}
