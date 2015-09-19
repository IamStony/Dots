package game.dots;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.widget.EditText;

public class Popup {
    public Popup(Context context, final int score, final String grid)
    {
        final DatabaseHandler m_db = new DatabaseHandler(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Your Name");
        input.setHintTextColor(Color.RED);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Your Score: " + Integer.toString(score));
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_user = "";
                m_user = input.getText().toString();
                if (m_user.length() > 20) {
                    m_user = m_user.substring(0, 20);
                }
                //System.out.println("Your score is ;;; " + Integer.toString(m_finalScore));
                HighScore hscore = new HighScore(m_user, score, grid);
                m_db.addScore(hscore);
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
