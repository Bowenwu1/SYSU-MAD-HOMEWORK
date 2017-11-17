package com.bowenwu.musicbox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicService extends Service {
    public class myBinder extends Binder {
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    // play button
                    if (mp.isPlaying()) {
                        mp.pause();
                    } else {
                        mp.start();
                    }
                    break;
                case 102:
                    // stop button
                    if (mp.isPlaying()) {
                        mp.pause();
                    }
                    mp.seekTo(0);
                    break;
                case 103:
                    // quit button
                    mp.stop();
                    mp.release();
                    break;
                case 104:
                    // return play position
                    reply.writeInt(mp.getCurrentPosition());
                    break;
                case 105:
                    // seekbar action
                    mp.seekTo(data.readInt());
                    break;
                case 106:
                    // return total length
                    reply.writeInt(mp.getDuration());
                    break;
                default:
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };
    public static MediaPlayer mp;
    public MusicService() {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (mp == null) {
            try {
                mp = MediaPlayer.create(MusicService.this, R.raw.melt);
//                mp.prepare();
                mp.setLooping(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("on Bind");
        return new myBinder();
    }
}
