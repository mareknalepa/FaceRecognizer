package pl.polsl.wkiro.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private MenuItem menuAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuAbout = menu.add("About application");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuAbout) {
            menuAbout();
        }
        return true;
    }

    public void startRecognizerClick(View view) {
        Intent intent = new Intent(this, RecognizerActivity.class);
        startActivity(intent);
    }

    public void openTrainerClick(View view) {
        Intent intent = new Intent(this, TrainerActivity.class);
        startActivity(intent);
    }

    public void menuAbout() {
        Toast.makeText(getApplicationContext(),
                "FaceRecognizer\nApplication developed for Computer Vision and Image Recognition subject at Silesian University of Technology\n2015",
                Toast.LENGTH_LONG).show();
    }
}
