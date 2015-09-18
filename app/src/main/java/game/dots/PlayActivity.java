package game.dots;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class PlayActivity extends Activity {

    private View game;
    //static MediaPlayer playfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //playfile = MediaPlayer.create(this, R.raw.filename);
        setContentView(R.layout.activity_play);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /*static void playSound() {
        playfile.start();
    }*/
}