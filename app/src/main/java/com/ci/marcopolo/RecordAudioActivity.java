package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Alex on 3/13/15.
 */
public class RecordAudioActivity extends Activity {
    public final static String TAG = "RecordAudioActivity";

    // audio recorder objects
    private final String RECORDING_FILENAME = "/temp_autopolo_audio_recording.3gp";
    private String recordingFilePath;
    private MediaRecorder mediaRecorder;

    // layout objects
    private Button mainButton;
    private Button cancelButton;
    private Button okButton;

    // OCLs
    private View.OnClickListener mainButtonOCL = new View.OnClickListener() {
        private boolean isRecording = false;

        @Override
        public void onClick(View v) {
            if (isRecording) {
                Log.d(TAG, "Stopped recording");
                mainButton.setText("Record");

                stopRecording();
                playRecording();
            } else {
                Log.d(TAG, "Recording...");
                mainButton.setText("Stop");

                startRecording();
            }
            isRecording = !isRecording;
        }
    };

    private View.OnClickListener cancelOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    private View.OnClickListener okOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            intent.putExtra("autopolo_audio", recordingFilePath);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        // get layout objects
        mainButton = (Button) findViewById(R.id.record_audio_main_button);
        cancelButton = (Button) findViewById(R.id.record_audio_cancel_button);
        okButton = (Button) findViewById(R.id.record_audio_ok_button);

        // setup layout objects
        mainButton.setOnClickListener(mainButtonOCL);
        cancelButton.setOnClickListener(cancelOCL);
        okButton.setOnClickListener(okOCL);

        // setup audio recording objects
        recordingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        recordingFilePath += RECORDING_FILENAME;
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordingFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "prepare() failed");
            return;
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void playRecording() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordingFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "prepare() failed");
            return;
        }
    }
}
