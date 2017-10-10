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
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "barkrasicki@gmail.com");
        emailIntent.setType("text/plain");
        startActivity(emailIntent);
    }
}
