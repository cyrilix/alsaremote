package fr.cyrilix.alsaremote.tools;

import android.app.AlertDialog;
import android.content.Context;
import fr.cyrilix.alsaremote.R;

/**
 * Class to display alert message
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AlertMessage {

    private final Context context;

    /**
     * Constructor
     * 
     * @param context application context
     */
    public AlertMessage(Context context) {
        this.context = context;
    }

    /**
     * Display exception message
     * 
     * @param e
     */
    public void displayError(Exception e) {
        String title = context.getResources().getString(R.string.error);
        displayAlert(title, e.getLocalizedMessage());
    }

    /**
     * Display alert to user
     * 
     * @param title alert title
     * @param message message content
     */
    public void displayAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(R.string.error);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
