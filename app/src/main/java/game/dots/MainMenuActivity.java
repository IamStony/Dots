package game.dots;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainMenuActivity extends Activity {
    DatabaseHandler db;
    ListView listView;
    SharedPreferences sp;
    HighScoreAdapter adapter;
    ArrayList<HighScore> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.deleteDatabase("HighScoresManager"); //Using it clear the thingy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = new DatabaseHandler(this);
        listView = (ListView) findViewById(R.id.listView);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        String grid = sp.getString("gridSize", "6");
        grid = grid + "x" + grid;

        scores = db.getAllScores(grid);

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
