package game.dots;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends Activity {
    DatabaseHandler db;
    ListView listView;
    HighScoreAdapter adapter;
    ArrayList<HighScore> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = new DatabaseHandler(this);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        scores = db.getAllScores();

        adapter = new HighScoreAdapter(this, scores);

        listView.setAdapter(adapter);

        /*for (HighScore s : scores) {
            String row = s._name + ": " + s._score;
            System.out.println(row);
        }*/
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
