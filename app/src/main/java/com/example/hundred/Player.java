package com.example.hundred;

import static com.example.hundred.Utilities.*;

public class Player {

    private long maxBubble = 0;
    private long score = 0;
    private int combo;
    private int bonusPlus;
    private int bonusSwipe;
    private int bonusExchange;
    private int lastColumnPlay;
    private int playValue;
    private int progress;
    private int swipeGetUp;
    private int plusGetUp;
    private int exchangeGetUp;

    private boolean isMaxPlay;

    public Player(){

    }

    public Player(long maxBubble, long score, int combo, int bonusPlus, int bonusSwipe, boolean isMaxPlay, int lastColumnPlay) {
        this.maxBubble = maxBubble;
        this.score = score;
        this.combo = combo;
        this.bonusPlus = bonusPlus;
        this.bonusSwipe = bonusSwipe;
        this.isMaxPlay = isMaxPlay;
        this.lastColumnPlay = lastColumnPlay;
    }

    public long getMaxBubble() {
        return maxBubble;
    }

    public void setMaxBubble(long maxBubble) {
        this.maxBubble = maxBubble;
    }

    public long getScore() {
        return score;
    }

    public void setScore(int originValue) {

        if (isMaxPlay) {
            if (combo > 4) {

                this.score = score + (maxBubble * (originValue * combo));

            } else {

                this.score = score + (maxBubble * originValue);

            }
        } else {
            this.score += originValue;
        }
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo += combo;
    }

    public void resetCombo(){
        this.combo = 0;
    }

    public int getBonusPlus() {
        return this.bonusPlus;
    }

    public void setBonusPlus(int bonusPlus) {
        this.bonusPlus = bonusPlus;
    }

    public int getBonusSwipe() {
        return this.bonusSwipe;
    }

    public void setBonusSwipe(int bonusSwipe) {
        this.bonusSwipe = bonusSwipe;
    }

    public int getBonusExchange() {
        return bonusExchange;
    }

    public void setBonusExchange(int bonusExchange) {
        this.bonusExchange = bonusExchange;
    }

    public int getExchangeGetUp() {
        return exchangeGetUp;
    }

    public void setExchangeGetUp(int exchangeGetUp) {
        this.exchangeGetUp = exchangeGetUp;
    }

    public void substractBonusSwipe(){
        this.bonusSwipe -= 1;
        if (this.bonusSwipe < 0) { this.bonusSwipe = 0;}
    }

    public void substractBonusExchange(){
        this.bonusExchange -= 1;
        if ( this.bonusExchange < 0) {this.bonusExchange = 0;}
    }

    public void substractBonusPlus(){
        this.bonusPlus -= 1;
        if ( this.bonusPlus < 0) {this.bonusPlus = 0;}
    }

    public boolean getIsMaxPlay() {
        return isMaxPlay;
    }

    public void setIsMaxPlay(boolean maxPlay) {
        isMaxPlay = maxPlay;
    }

    public int getLastColumnPlay() {
        return lastColumnPlay;
    }

    public void setLastColumnPlay(int lastColumnPlay) {
        this.lastColumnPlay = lastColumnPlay;
    }

    public int getPlayValue() {
        return playValue;
    }

    public void setPlayValue(int playValue) {
        this.playValue = playValue;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setProgressPlus1() {
        this.progress += 1 ;
    }

    public int getSwipeGetUp() {
        return swipeGetUp;
    }

    public void setSwipeGetUp(int swipeGetUp) {
        if (swipeGetUp > MAX_SWIPE_GET_UP){
            swipeGetUp = MAX_SWIPE_GET_UP;
        }

        this.swipeGetUp = swipeGetUp;

    }

    public int getPlusGetUp() {
        return plusGetUp;
    }

    public void setPlusGetUp(int plusGetUp) {

        if (plusGetUp > MAX_PLUS_GET_UP){
            plusGetUp = MAX_PLUS_GET_UP;
        }

        this.plusGetUp = plusGetUp;

    }

    public void resetGame(){

        this.maxBubble = 1;
        this.score = 0;
        this.combo = 0;
        this.bonusPlus = 0;
        this.bonusSwipe = 0;
        this.bonusExchange = 0;
        this.isMaxPlay = true;
        this.lastColumnPlay = 2;
        this.playValue = 1;
        this.progress = 1;

    }

    public void rushUp(int newValue){
        this.maxBubble = newValue;
        this.playValue = newValue;
        this.progress = newValue;
    }

    public void restoreGame(int progress, int playValue, int lastColumnPlay, int score, int maxBubble, int combo, int bonusSwipe, int bonusPlus, int bonusExchange){

        this.progress = progress;
        this.playValue = playValue;
        this.lastColumnPlay = lastColumnPlay;
        this.score = score;
        this.maxBubble = maxBubble;
        this.combo = combo;
        this.bonusSwipe = bonusSwipe;
        this.bonusPlus = bonusPlus;
        this.bonusExchange = bonusExchange;
        if (this.maxBubble == this.playValue) {
            this.isMaxPlay = true;
        } else {
            this.isMaxPlay = false;
        }



    }

}
