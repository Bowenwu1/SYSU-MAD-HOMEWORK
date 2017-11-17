package com.bowenwu.musicbox;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    IBinder mBinder;
    Thread thread;
    Button playButton;
    Button stopButton;
    Button quitButton;
    Boolean playOrPaused;
    Boolean threadRunning = false;

    TextView status;
    TextView playPosition;
    TextView musicLength;
    ImageView diskView;
    SeekBar seekBar;
    int totalTime;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    private enum STATE {playing, pausing, stopping, shifting};
    private STATE state;

    private ObjectAnimator diskAnimator;
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = iBinder;
            System.out.println(mBinder == null);
            // set length of music
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                mBinder.transact(106, data, reply, 0);
                totalTime = reply.readInt();
                musicLength.setText(convertMSecondsToFormatedString(totalTime));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            threadRunning = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sc = null;
        }
    };

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            super.handleMessage(m);
            switch (m.what) {
                case 101:
                    if (state == STATE.playing) {
                        // update UI
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try {
                            mBinder.transact(104, data, reply, 0);
                            int current = reply.readInt();
                            playPosition.setText(convertMSecondsToFormatedString(current));
                            seekBar.setProgress((int) (current / (double) totalTime * 100));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playOrPaused = false;
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

        playButton = (Button)findViewById(R.id.play_button);
        stopButton = (Button)findViewById(R.id.stop_button);
        quitButton = (Button)findViewById(R.id.quit_button);
        status     = (TextView)findViewById(R.id.status);
        playPosition = (TextView)findViewById(R.id.play_position);
        musicLength  = (TextView)findViewById(R.id.music_length);
        diskView     = (ImageView)findViewById(R.id.music_disk);
        seekBar      = (SeekBar)findViewById(R.id.seekbar);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int code = 101;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (playOrPaused.equals(true)) {
                    // playing
                   setUIForState(STATE.pausing);
                } else {
                    // paused or stopping
                    setUIForState(STATE.playing);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int code = 102;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setUIForState(STATE.stopping);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(sc);
                try {

                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private STATE beforeShiftState;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // i : 0~100
                if (true == b) {
                    int expectedTime = (int)(totalTime * (double)(i / 100.0));
                    playPosition.setText(convertMSecondsToFormatedString(expectedTime));
                    // by user
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeInt(expectedTime);
                    try {
                        // Perform a generic operation with the object.
                        mBinder.transact(105, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                beforeShiftState = state;
                state = STATE.shifting;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                state = beforeShiftState;
            }
        });

        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sc != null && threadRunning.equals(true)) {
                        mHandler.obtainMessage(101).sendToTarget();
                    }
                }
            }
        };

        thread.start();

        // Animation
        diskAnimator = ObjectAnimator.ofFloat(diskView, "rotation", 0, 360);
        diskAnimator.setDuration(20000);
        diskAnimator.setInterpolator(new LinearInterpolator());
        diskAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void setUIForState(STATE s) {
        this.state = s;
        switch (s) {
            case playing:
                // playing
                System.out.println("state : playing");
                playOrPaused = true;
                playButton.setText(getString(R.string.pause));
                status.setText(getString(R.string.status_playing));
                if (!diskAnimator.isStarted()) {
                    diskAnimator.start();
                }
                diskAnimator.resume();
                break;
            case pausing:
                // paused
                playOrPaused = false;
                playButton.setText(getString(R.string.play));
                status.setText(getString(R.string.status_paused));
                diskAnimator.pause();
                break;
            case stopping:
                // stopped
                playOrPaused = false;
                playButton.setText(getString(R.string.play));
                status.setText(getString(R.string.status_stopped));
                playPosition.setText(convertMSecondsToFormatedString(0));
                diskAnimator.pause();
                seekBar.setProgress(0);
                break;
            default:
                break;
        }
    }

    private String convertMSecondsToFormatedString(int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, m / (1000*60), ((m %(1000*60))/1000));
        return simpleDateFormat.format(calendar);
    }
}
