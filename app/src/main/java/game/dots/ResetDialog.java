package game.dots;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ResetDialog extends DialogPreference {

    public ResetDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(positiveResult) {
            DatabaseHandler db = new DatabaseHandler(getContext());
            db.clearDB();
        }
    }

}
