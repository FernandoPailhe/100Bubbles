package com.example.hundred;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;

import java.util.Random;

import static com.example.hundred.Utilities.ENABLED_GAME_OVER_BUTTON;


public class SoundPlayer {


    MediaPlayer soundUp = new MediaPlayer();
    MediaPlayer soundUpDo1 = new MediaPlayer();
    MediaPlayer soundUpMi1 = new MediaPlayer();
    MediaPlayer soundUpSol1 = new MediaPlayer();
    MediaPlayer soundUpSi1 = new MediaPlayer();
    MediaPlayer soundUpDo2 = new MediaPlayer();

    MediaPlayer soundMenu = new MediaPlayer();

    MediaPlayer soundSwipePlay = new MediaPlayer();
    MediaPlayer soundDown = new MediaPlayer();
    MediaPlayer soundCombo = new MediaPlayer();
    MediaPlayer soundSwipe = new MediaPlayer();
    MediaPlayer soundGameOver = new MediaPlayer();
    MediaPlayer soundBestScore = new MediaPlayer();
    MediaPlayer soundDont = new MediaPlayer();
    MediaPlayer soundReset = new MediaPlayer();
    MediaPlayer soundCasillero100 = new MediaPlayer();
    MediaPlayer soundBomb = new MediaPlayer();
    MediaPlayer soundBonusSwap = new MediaPlayer();
    MediaPlayer soundBonusSwipePlay = new MediaPlayer();
    MediaPlayer soundAtajo = new MediaPlayer();
    MediaPlayer soundBubble_1 = new MediaPlayer();
    MediaPlayer soundBubble_2 = new MediaPlayer();
    MediaPlayer soundBubble_3 = new MediaPlayer();
    MediaPlayer soundBubble_4 = new MediaPlayer();
    MediaPlayer bonusGetUp = new MediaPlayer();

    boolean isSoundPlayable = true;
    boolean sound;
    int comboBubble = 0;

    public void createMediaPlayer(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            soundUp = MediaPlayer.create(context, R.raw.up);
            soundUpDo1 = MediaPlayer.create(context, R.raw.up_do1);
            soundUpMi1 = MediaPlayer.create(context, R.raw.up_mi1);
            soundUpSol1 = MediaPlayer.create(context, R.raw.up_sol1);
            soundUpSi1 = MediaPlayer.create(context, R.raw.up_si1);
            soundUpDo2 = MediaPlayer.create(context, R.raw.up_do2);
            soundDown = MediaPlayer.create(context, R.raw.down);
            soundCombo = MediaPlayer.create(context, R.raw.combo);
            soundSwipe = MediaPlayer.create(context, R.raw.swipe);
            soundGameOver = MediaPlayer.create(context, R.raw.game_over);
            soundBestScore = MediaPlayer.create(context, R.raw.best_score);
            soundDont = MediaPlayer.create(context, R.raw.dont);
            soundSwipePlay = MediaPlayer.create(context, R.raw.swipe_play);
            soundReset = MediaPlayer.create(context, R.raw.reset);
            soundCasillero100 = MediaPlayer.create(context, R.raw.champion);
            soundMenu = MediaPlayer.create(context, R.raw.menu);
            soundBonusSwap = MediaPlayer.create(context, R.raw.bonus_swap);
            soundBomb = MediaPlayer.create(context, R.raw.bomb);
            soundBonusSwipePlay = MediaPlayer.create(context, R.raw.bonus_swipe_play);
            soundAtajo = MediaPlayer.create(context, R.raw.atajo);
            soundBubble_1 = MediaPlayer.create(context, R.raw.bubble_1);
            soundBubble_2 = MediaPlayer.create(context, R.raw.bubble_2);
            soundBubble_3 = MediaPlayer.create(context, R.raw.bubble_3);
            soundBubble_4 = MediaPlayer.create(context, R.raw.bubble_4);
            bonusGetUp = MediaPlayer.create(context, R.raw.bonus_get_up);
            this.comboBubble = 0;
            isSoundPlayable = true;

        } else {

            isSoundPlayable = false;


        }

    }





    public void playSoundBubble(Context context, int comboCantidad) {

        if (sound == true) {

            this.comboBubble++;

            final int diferentSound = 4;

            int secSound = new Random().nextInt(diferentSound);
            if (secSound % diferentSound == this.comboBubble % diferentSound) {
                secSound--;
            }
            final int randomInt = new Random().nextInt(400 - 300) + 300;
            final float volumeRandom = randomInt / 1000.00F;

            if (comboBubble % diferentSound == 0) {

                soundBubble_1.setVolume(volumeRandom, volumeRandom);
                soundBubble_1.start();

            } else if (comboBubble % diferentSound == 1) {

                soundBubble_2.setVolume(volumeRandom, volumeRandom);
                soundBubble_2.start();

            } else if (comboBubble % diferentSound == 2) {

                soundBubble_3.setVolume(volumeRandom, volumeRandom);
                soundBubble_3.start();

            } else if (comboBubble % diferentSound == 3) {

                soundBubble_4.setVolume(volumeRandom, volumeRandom);
                soundBubble_4.start();

            }

            final int secondSound = secSound;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (secondSound % diferentSound == 0) {

                        soundBubble_1.setVolume(volumeRandom - 0.15f, volumeRandom - 0.15f);
                        soundBubble_1.start();

                    } else if (secondSound % diferentSound == 1) {

                        soundBubble_2.setVolume(volumeRandom - 0.15f, volumeRandom - 0.15f);
                        soundBubble_2.start();

                    } else if (secondSound % diferentSound == 2) {

                        soundBubble_3.setVolume(volumeRandom - 0.15f, volumeRandom - 0.15f);
                        soundBubble_3.start();

                    } else if (secondSound % diferentSound == 3) {

                        soundBubble_4.setVolume(volumeRandom - 0.15f, volumeRandom - 0.15f);
                        soundBubble_4.start();

                    }

                }
            }, 500);

        }

    }

    public void playSoundUp(Context context, int jugadaColumna) {

        if (sound == true) {

            if (jugadaColumna == 0) {

                //soundUpSol1.setVolume(0.6f, 0.2f);
                soundUpSol1.start();

            } else if (jugadaColumna == 1) {

                //soundUpMi1.setVolume(0.5f, 0.3f);
                soundUpMi1.start();

            } else if (jugadaColumna == 2) {

                soundUpDo1.setVolume(0.7f, 0.7f);
                soundUpDo1.start();

            } else if (jugadaColumna == 3) {

                //soundUpMi1.setVolume(0.3f, 0.5f);
                soundUpMi1.start();

            } else {

                //soundUpSol1.setVolume(0.2f, 0.6f);
                soundUpSol1.start();

            }
        }


    }

    public void playSoundBomb(Context context) {
        if (sound) {
            soundBomb.start();
        }

    }

    public void playBonusGetUp(Context context) {
        if (sound) {
            bonusGetUp.start();
        }

    }

    public void playSoundAtajo(Context context) {

        if (sound) {

            soundAtajo.start();

        }

    }

    public void playSoundExchange(Context context) {

        if (sound) {

            soundBonusSwap.start();
        }

    }

    public void playSoundBonusSwipe(Context context) {

        if (sound) {

            soundBonusSwipePlay.start();
        }

    }

    public void playSoundCombo(Context context) {
        if (sound == true) {

            soundCombo.setVolume(0.5f, 0.5f);
            soundCombo.start();

        }
    }

    public void playSoundSwipe(Context context) {
        if (sound == true) {

            //soundSwipe.setVolume(0.7f, 0.7f);
            soundSwipe.start();

        }
    }

    public void playSoundGameOver(Context context) {
        if (sound == true) {

            //soundGameOver.setVolume(0.7f, 0.7f);
            soundGameOver.start();

        }
    }


    public void playSoundDont(Context context) {
        if (sound == true) {

            //soundDont.setVolume(0.7f, 0.7f);
            soundDont.start();

        }
    }

    public void playSoundSwipePlay(Context context) {

        if (sound == true) {

            //soundSwipePlay.setVolume(0.7f, 0.7f);
            soundSwipePlay.start();

        }
    }

    public void playSoundPlusPlay(Context context) {

        if (sound == true) {

            //soundSwipePlay.setVolume(0.7f, 0.7f);
            soundSwipePlay.start();

        }
    }

    public void playSoundReset(Context context) {
        if (sound == true) {

            soundReset.start();

        }
    }

    public void playSoundCasillero100(Context context) {
        if (sound == true) {

            soundCasillero100.start();

        }
    }

    public void stopSoundCasillero100(Context context) {

        if (sound == true) {

            soundCasillero100.stop();

        }
    }

    public void playSoundMenu(Context context) {
        if (sound == true) {

            //soundMenu.setVolume(0.7f, 0.7f);
            soundMenu.start();

        }
    }

    public void playSoundDown(Context context) {
        if (sound) {

            soundDown.start();
        }


    }


}
