package pl.polsl.wkiro.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startRecognizerClick(View view) {
        Intent intent = new Intent(this, RecognizerActivity.class);
        startActivity(intent);
    }

    public void openTrainerClick(View view) {
        Intent intent = new Intent(this, TrainerActivity.class);
        startActivity(intent);
    }

    public void aboutClick(View view) {
        Toast.makeText(getApplicationContext(),
                "FaceRecognizer\nApplication developed for Computer Vision and Image Recognition subject at Silesian University of Technology\n2015",
                Toast.LENGTH_LONG).show();
    }
}
