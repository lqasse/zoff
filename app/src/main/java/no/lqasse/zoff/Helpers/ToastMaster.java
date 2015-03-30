package no.lqasse.zoff.Helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import no.lqasse.zoff.Player.PlayerActivity;
import no.lqasse.zoff.R;
import no.lqasse.zoff.Remote.RemoteActivity;

/**
 * Created by lassedrevland on 24.03.15.
 */
public class ToastMaster {

    public enum TYPE{
        NEEDS_PASS_TO_VOTE,
        NEEDS_PASS_TO_ADD,
        NEEDS_PASS_TO_SHUFFLE,
        VIDEO_ADDED,
        HOLD_TO_VOTE,
        SHUFFLED,
        EMBEDDING_DISABLED,
        VIDEO_VOTED,
        HOLD_TO_ADD,
        BACKGROUND,
        FOREGROUND,
        PAUSE,
        STOP
    }



    public static void showToast(Object context,TYPE type){
        showToast(context,type,"");

    }

    public static void showToast(Object context, TYPE type, String CONTEXTUAL_STRING){



        Toast t;
        String toastText = "Toast error";

        switch (type){
            case NEEDS_PASS_TO_VOTE:
                toastText = "This room is password protected, set a password to vote";
                break;
            case NEEDS_PASS_TO_ADD:
                toastText = "This room is password protected, set a password to add videos";
                break;
            case VIDEO_ADDED:
                toastText = CONTEXTUAL_STRING + " was added";
                break;
            case VIDEO_VOTED:
                toastText = "+1 to " + CONTEXTUAL_STRING;
                break;
            case HOLD_TO_VOTE:
                toastText = "Click and hold to vote";
                break;
            case SHUFFLED:
                toastText = "Shuffled!";
                break;
            case NEEDS_PASS_TO_SHUFFLE:
                toastText = "This room is password protected, set a password to shuffle";
                break;
            case EMBEDDING_DISABLED:
                toastText = CONTEXTUAL_STRING + " could not be played, embedded playback disabled.";
                break;
            case HOLD_TO_ADD:
                toastText = "Click and hold to add videos";
                break;
            case BACKGROUND:
                toastText ="WENT TO BACKGROUND";
                break;
            case FOREGROUND:
                toastText = "Came to foreground";
                break;
            case STOP:
                toastText = "STOPPed";
                break;
            case PAUSE:
                toastText = "Paused";



        }

        if (context instanceof Activity) {

            t = Toast.makeText((Activity) context, toastText, Toast.LENGTH_SHORT);
            View v = t.getView();
            v.setBackgroundResource(R.drawable.toast_background);
            t.show();
        }


    }
}
