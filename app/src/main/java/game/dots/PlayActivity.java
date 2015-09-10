package game.dots;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PlayActivity extends Activity {

    private View game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}