package game.dots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoreAdapter extends ArrayAdapter<HighScore> {
    public HighScoreAdapter(Context context, ArrayList<HighScore> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HighScore hs = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_entry, parent, false);
        }

        TextView playerScore = (TextView) convertView.findViewById(R.id.playerScore);

        playerScore.setText("#" + (position + 1) + " - " + hs._name + " : " + hs._score);

        return convertView;
    }
}
