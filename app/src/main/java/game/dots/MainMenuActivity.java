package game.dots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainMenuActivity extends Activity {
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        //db.addScore(new HighScore("Steinar", 80085));

        List<HighScore> scores = db.getAllScores();

        for (HighScore s : scores) {
            String row = s._name + ": " + s._score;
            System.out.println(row);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<HighScore> scores = db.getAllScores();

        for (HighScore s : scores) {
            String row = s._name + ": " + s._score;
            System.out.println(row);
        }
    }

    public void game_play(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void game_settings(View view) {
        Intent intent = new Intent(this, MyPreferencesActivity.class);
        startActivity(intent);
    }
}
