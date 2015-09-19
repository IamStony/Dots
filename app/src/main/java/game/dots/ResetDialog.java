package game.dots;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by kristinn on 19/09/15.
 */
public class ResetDialog extends DialogPreference {

    public ResetDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(positiveResult)
        {
            DatabaseHandler db = new DatabaseHandler(getContext());
            db.clearDB();
        }
    }

}
