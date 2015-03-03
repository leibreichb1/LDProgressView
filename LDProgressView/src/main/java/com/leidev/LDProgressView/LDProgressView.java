package com.leidev.LDProgressView;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by Brian on 2/24/15.
 */
public class LDProgressView {

    private View mView;
    private Context mContext;
    private String mMessageText;

    private LDProgressView(Builder builder){
        mContext = builder.context;
        mMessageText = builder.messageText;
    }

    /**
     * Shows the progress view over the active window, preventing user interaction from the view behind.
     */
    public void show(){
        if(mView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = inflater.inflate(R.layout.progress_view, null, false);

            TextView messageTextView = (TextView)mView.findViewById(R.id.ld_progress_message_text_view);
            messageTextView.setText(mMessageText);
        }

        if(mView != null && mView.getParent() != null){
            removeView(mContext);
        }

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP ;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;

        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mView, windowParams);
    }

    /**
     * Removes the view from the window, returning focus to the application.
     */
    public void dismiss(){

        if(mView != null){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.exit_animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            removeView(mContext);
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mView.findViewById(R.id.ld_progress_container).startAnimation(animation);
        }
    }

    /**
     * Removes the veiw from the WindowManager
     * @param context
     */
    private void removeView(Context context){
    if(mView != null){
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(mView);
        }
    }

    public static class Builder {
        private Context context;
        private String messageText;

        /**
         * Constructor using a context for this builder and the progress view it creates
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the message that the progress view will display below the progress indicator.
         *
         * @param messageText Message to display in dialog.
         */
        public Builder setMessage(String messageText){
            this.messageText = messageText;
            return this;
        }

        /**
         * Creates a AlertDialog with the arguments supplied to this builder. It does not show() the dialog. This allows the user to keep a reference to the view in order to remove it later.
         * @return
         */
        public LDProgressView build(){
            return new LDProgressView(this);
        }
    }

}
