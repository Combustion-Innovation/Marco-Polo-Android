package com.ci.marcopolo;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Alex on 3/11/15.
 */
public class AutoPoloActivity extends Activity {
    public final static String TAG = "AutoPoloActivity";

    // layout objects
    private TextView backButton;

    // OCLs
    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAfterTransition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopolo);

        // get layout objects
        backButton = (TextView) findViewById(R.id.back);

        // setup layout objects
        backButton.setOnClickListener(backOCL);

        // setup transitions
        Window window = getWindow();
        window.setSharedElementEnterTransition(new Explode());
        window.setSharedElementExitTransition(new Explode());
    }
}
