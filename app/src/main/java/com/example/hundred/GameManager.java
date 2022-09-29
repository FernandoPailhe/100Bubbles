package com.example.hundred;

import java.util.Random;

import static com.example.hundred.Utilities.COMBO_MULTIPLE;
import static com.example.hundred.Utilities.EXTRA_BONUS;
import static com.example.hundred.Utilities.INTERVAL_BOMB_ROW;
import static com.example.hundred.Utilities.INTERVAL_COMODIN;
import static com.example.hundred.Utilities.INTERVAL_EXCHANGE;
import static com.example.hundred.Utilities.INTERVAL_NO_BUBBLE;
import static com.example.hundred.Utilities.INTERVAL_PLUS_BONUS;
import static com.example.hundred.Utilities.INTERVAL_RUSH;
import static com.example.hundred.Utilities.INTERVAL_RUSH_UP;
import static com.example.hundred.Utilities.INTERVAL_SWIPE_BONUS;
import static com.example.hundred.Utilities.MAX_EXCHANGE_GET_UP;
import static com.example.hundred.Utilities.MAX_PLUS_GET_UP;
import static com.example.hundred.Utilities.MAX_SWIPE_GET_UP;
import static com.example.hundred.Utilities.MULTIPLE_EXCHANGE;
import static com.example.hundred.Utilities.NUM_COLUMNS;
import static com.example.hundred.Utilities.RUSH_UP;
import static com.example.hundred.Utilities.START_NO_BUBBLE;

public class GameManager {

    public int firstRow;
    private int nextRow;
    private int thirdRow;
    private int fourthRow;
    private int level;
    private int columnBomb;
    private boolean isAnEvent;
    public Player player = new Player();
    public Rows[] row = new Rows[10];
    public User user = new User();
    private boolean playLeft;
    private boolean playUp;
    private boolean playRight;
    private boolean isMultiple100;
    private boolean isRushUp;

    public boolean isRushUp() {
        return this.isRushUp;
    }

    public boolean isAnEvent(){
        return this.isAnEvent;
    }

    public int getColumnBomb(){
        return this.columnBomb;
    }

    public int getNextRow() {
        return this.nextRow;
    }

    public void setNextRow(int nextRow) {
        this.nextRow = nextRow;
    }

    public int getThirdRow() {
        return this.thirdRow;
    }

    public void setThirdRow(int thirdRow) {
        this.thirdRow = thirdRow;
    }

    public int getFourthRow() {
        return this.fourthRow;
    }

    public void setFourthRow(int fourthRow) {
        this.fourthRow = fourthRow;
    }

    public boolean isPlayLeft() {
        return playLeft;
    }

    public void setPlayLeft(boolean playLeft) {
        this.playLeft = playLeft;
    }

    public boolean isPlayUp() {
        return playUp;
    }

    public void setPlayUp(boolean playUp) {
        this.playUp = playUp;
    }

    public boolean isPlayRight() {
        return playRight;
    }

    public void setPlayRight(boolean playRight) {
        this.playRight = playRight;
    }

    public GameManager() {
        for (int i = 0; i < 10; i++) {
            row[i] = new Rows();
        }
    }

    public GameManager(int firstRow, Player player) {
        this.firstRow = firstRow;
        this.player = player;
        for (int i = 0; i < 10; i++) {
            row[i] = new Rows();
        }
    }

    public int getFirstRow() {
        return this.firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Rows[] getRow() {
        return row;
    }

    public void setRow(Rows[] row) {
        this.row = row;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSwipeUp(){

        int combo = this.player.getCombo();

        int swipeGetUp = 1;

        if (combo >= COMBO_MULTIPLE) {

            swipeGetUp = (combo / COMBO_MULTIPLE ) + 1;

            if (swipeGetUp > 9 ){

                swipeGetUp = 9;
            }

        }

        return swipeGetUp;

    }

    public void moveRow(int actionRow, int direction, boolean isAutoTutorial){

        this.row[actionRow].moveRow(direction);
        if (isAutoTutorial == false) {
            this.player.substractBonusSwipe();
        }

    }

    public void bonusPlus (int actionRow, int thisColumn, int operation, boolean isAutoTutorial){

        this.row[actionRow].column[thisColumn].bonusPlus(operation);
        if (isAutoTutorial == false) {
            this.player.substractBonusPlus();
        }

    }

    public void exchangeBubble(int firstActionRow, int firstColumn, int secondActionRow, int secondColumn){

        int firstValue = this.row[firstActionRow].column[firstColumn].getValue();
        int firstState = this.row[firstActionRow].column[firstColumn].getState();
        int secondValue = this.row[secondActionRow].column[secondColumn].getValue();
        int secondState = this.row[secondActionRow].column[secondColumn].getState();

        this.row[firstActionRow].column[firstColumn].setValue(secondValue);
        this.row[firstActionRow].column[firstColumn].setState(secondState);

        this.row[secondActionRow].column[secondColumn].setValue(firstValue);
        this.row[secondActionRow].column[secondColumn].setState(firstState);

        this.player.substractBonusExchange();

    }

    private void updatePlayer(int originValue, int columnPlay){

        this.player.setSwipeGetUp(0);
        this.player.setPlusGetUp(0);
        this.player.setExchangeGetUp(0);
        this.player.setLastColumnPlay(columnPlay);
        this.player.setPlayValue(originValue);
        this.player.setProgressPlus1();

        int extraBonus = 0;

        if (row[firstRow].isHasExtraBonus()){
            extraBonus = EXTRA_BONUS + (int)(player.getMaxBubble() / (INTERVAL_RUSH/2));
        }

        switch (row[firstRow].column[columnPlay].getState()){
            case 1:
                this.player.setSwipeGetUp((INTERVAL_RUSH*2 + (int) this.player.getMaxBubble()) / (INTERVAL_RUSH*2) + extraBonus);
                if (this.player.getSwipeGetUp() > MAX_SWIPE_GET_UP){
                    this.player.setSwipeGetUp(MAX_SWIPE_GET_UP);
                }
                break;
            case 2:
                this.player.setPlusGetUp(((int)this.player.getMaxBubble() / INTERVAL_PLUS_BONUS) + extraBonus);
                if (this.player.getMaxBubble() < INTERVAL_PLUS_BONUS){
                    this.player.setPlusGetUp(1 + extraBonus);
                }
                if (this.player.getPlusGetUp() > MAX_PLUS_GET_UP){
                    this.player.setPlusGetUp(MAX_PLUS_GET_UP);
                }
                break;
            case 7:
                if (this.player.getMaxBubble() < MULTIPLE_EXCHANGE){
                    this.player.setExchangeGetUp(1 + extraBonus);
                } else {
                    this.player.setExchangeGetUp(((int)this.player.getMaxBubble()  / MULTIPLE_EXCHANGE) + extraBonus);
                    if (this.player.getExchangeGetUp() > MAX_EXCHANGE_GET_UP){
                        this.player.setExchangeGetUp(MAX_EXCHANGE_GET_UP);
                    }
                }
                break;
        }

        if (originValue > this.player.getMaxBubble()){
            this.player.setIsMaxPlay(true);
            this.player.setCombo(1);
            this.player.setMaxBubble(originValue);
            if (this.player.getCombo() >= COMBO_MULTIPLE && row[firstRow].column[columnPlay].getState() == 1) {
                this.player.setSwipeGetUp((this.player.getCombo() / COMBO_MULTIPLE ) + 1);
                if (this.player.getSwipeGetUp() > MAX_SWIPE_GET_UP){
                    this.player.setSwipeGetUp(MAX_SWIPE_GET_UP);
                }
            }
        } else if (row[firstRow].column[columnPlay].getState() == 6){

        } else {
            this.player.setIsMaxPlay(false);
            this.player.resetCombo();
        }

        this.player.setBonusExchange(this.player.getBonusExchange() + this.player.getExchangeGetUp());
        this.player.setBonusSwipe(this.player.getBonusSwipe() + this.player.getSwipeGetUp());
        this.player.setBonusPlus(this.player.getBonusPlus() + this.player.getPlusGetUp());
        this.player.setScore(originValue);

    }

    public boolean getIsMultiple100(){
        return this.isMultiple100;
    }

    private void updateUser(){

        if(this.player.getIsMaxPlay()){
            this.user.updateCountMoves();
            this.user.updateMaxBubble(this.player.getPlayValue());

            if ( this.player.getCombo() > this.user.maxCombo){
                this.user.updateMaxCombo(this.player.getCombo());
            } else {
                this.user.updateCombo(this.player.getCombo());
            }
            if (this.player.getMaxBubble() > this.user.maxBubbleCount){
                this.user.updateMaxBubbleCount((int)this.player.getMaxBubble());
            }
            if (isMultiple100) {
                if (this.player.getMaxBubble() == 100) {
                    this.user.updateOver100();
                } else if (this.player.getMaxBubble() == 200){
                    this.user.updateOver200();
                }
            }
        }

    }

    private void checkPossibilities(int columnPlay, int actualValue) {

        int upNumber = this.row[nextRow].column[columnPlay].getValue();

        if (this.row[nextRow].column[columnPlay].getState() == 6 || this.row[firstRow].column[columnPlay].getState() == 6 ){
            if (this.row[nextRow].column[columnPlay].getState() != 3) {
                this.playUp = true;
            }
        } else {
            if (actualValue == upNumber + 1 || actualValue == upNumber - 1) {
                this.playUp = true;
            } else {
                this.playUp = false;
            }
        }

        if (columnPlay == 0){
            this.playLeft = false;
        } else {
            if (this.row[nextRow].column[columnPlay - 1].getState() == 6 || this.row[firstRow].column[columnPlay].getState() == 6){
                if (this.row[nextRow].column[columnPlay - 1].getState() != 3) {
                    this.playLeft = true;
                }
            } else {
                int leftNumber = this.row[nextRow].column[columnPlay - 1].getValue();
                if (actualValue == leftNumber + 1 || actualValue == leftNumber - 1) {
                    this.playLeft = true;
                } else {
                    this.playLeft = false;
                }
            }
        }

        if (columnPlay == NUM_COLUMNS - 1){
            this.playRight = false;
        } else {
            if (this.row[nextRow].column[columnPlay + 1].getState() == 6 || this.row[firstRow].column[columnPlay].getState() == 6){
                if (this.row[nextRow].column[columnPlay + 1].getState() != 3) {
                    this.playRight = true;
                }
            } else {
                int rightNumber = this.row[nextRow].column[columnPlay + 1].getValue();
                if (actualValue == rightNumber + 1 || actualValue == rightNumber - 1) {
                    this.playRight = true;
                } else {
                    this.playRight = false;
                }
            }
        }

    }

    public boolean isGameOVer (){

        if (this.playLeft == false && this.playUp == false && this.playRight == false){
            return true;
        } else {
            return false;
        }
    }

    public void tutorialPlay (int columnPlay, int tutorialPlay){

        this.firstRow = this.nextRow;
        this.nextRow = this.thirdRow;
        this.thirdRow = this.fourthRow;
        this.fourthRow = (this.fourthRow + 11) % 10;

        int originValue = row[firstRow].column[columnPlay].getValue();

        updatePlayer(originValue, columnPlay);

        updateUser();

        if (tutorialPlay == 1) {
            row[fourthRow].column[0].setValue(5);
            row[fourthRow].column[1].setValue(2);
            row[fourthRow].column[2].setValue(3);
            row[fourthRow].column[2].setState(1);
            row[fourthRow].column[3].setValue(4);
            row[fourthRow].column[4].setValue(3);
        } else if (tutorialPlay == 2) {
            row[fourthRow].column[0].setValue(2);
            row[fourthRow].column[1].setValue(4);
            row[fourthRow].column[2].setValue(2);
            row[fourthRow].column[3].setValue(5);
            row[fourthRow].column[4].setValue(4);
        } else if (tutorialPlay == 3) {
            row[fourthRow].column[0].setValue(6);
            row[fourthRow].column[1].setValue(6);
            row[fourthRow].column[2].setValue(2);
            row[fourthRow].column[3].setValue(5);
            row[fourthRow].column[4].setValue(5);
        } else if (tutorialPlay > 3) {
            row[fourthRow].column[0].setValue(4);
            row[fourthRow].column[1].setValue(6);
            row[fourthRow].column[2].setValue(4);
            row[fourthRow].column[3].setValue(5);
            row[fourthRow].column[4].setValue(6);
        }

        checkPossibilities(columnPlay, originValue);

    }

    public void bomb(int column){

        this.row[nextRow].explode(column);
        this.row[thirdRow].explode(column);
        this.row[fourthRow].explode(column);
        this.playUp = false;
        this.columnBomb = column;
        this.isAnEvent = true;
        isGameOVer();

    }

    public void rushUp(int column, int originValue){

        int randomState = new Random().nextInt(3);

        if (randomState == 0){
            randomState = 7;
        }


        int anteUltimateRow = (getFirstRow() + 18) % 10;
        int lastRow = (getFirstRow() + 19) % 10;
        this.row[anteUltimateRow].createDefineRow(originValue + RUSH_UP - 2, 0);
        this.row[lastRow].createDefineRow(originValue + RUSH_UP - 1, 0);

        this.player.rushUp(originValue + RUSH_UP);
        this.row[firstRow].createDefineRow(originValue + RUSH_UP, 0);
        this.row[nextRow].createDefineRow(originValue + RUSH_UP + 1, randomState);
        this.row[thirdRow].createDefineRow(originValue + RUSH_UP + 2, 0);
        this.row[fourthRow].createRandomRow(originValue + RUSH_UP, (int)this.player.getMaxBubble(), true, this.player.getProgress(), 4);
        this.playLeft = true;
        this.playUp = true;
        this.playRight = true;
        this.isAnEvent = true;
        this.isRushUp = true;

    }

    public void play(int columnPlay){

        this.columnBomb = -1;
        this.isRushUp = false;
        this.isAnEvent = false;
        this.firstRow = this.nextRow;
        this.nextRow = this.thirdRow;
        this.thirdRow = this.fourthRow;
        this.fourthRow = (this.fourthRow + 11) % 10;

        int originValue = row[firstRow].column[columnPlay].getValue();

        if (row[firstRow].column[columnPlay].getState() == 6){
            originValue = this.player.getPlayValue() + 1;
        }

        updatePlayer(originValue, columnPlay);

        updateUser();

        if (originValue % INTERVAL_RUSH == 0 && this.player.getIsMaxPlay() == true){

            this.isMultiple100 = true;
            this.row[this.nextRow].createDefineRow(originValue + 1, 1);
            this.row[this.thirdRow].createDefineRow(originValue + 2, 2);
            this.row[this.fourthRow].createDefineRow(originValue + 3, 7);

        } else {

            this.isMultiple100 = false;
            row[this.fourthRow].createRandomRow(originValue, (int) this.player.getMaxBubble(), this.player.getIsMaxPlay(), this.player.getProgress(), 0);

        }

        if (row[firstRow].column[columnPlay].getState() == 4){
            bomb(columnPlay);
        } if (row[firstRow].column[columnPlay].getState() == 5){
            rushUp(columnPlay, originValue);
        }

        checkPossibilities(columnPlay, this.player.getPlayValue());

    }

    public void continueGame(){

        this.row[this.nextRow].createDefineRow(this.player.getPlayValue() + 1, 1);
        this.row[this.nextRow].setHasExtraBonus(true);
        this.row[this.thirdRow].createDefineRow(this.player.getPlayValue() + 2, 2);
        this.row[this.thirdRow].setHasExtraBonus(true);
        this.row[this.fourthRow].createDefineRow(this.player.getPlayValue() + 3, 7);
        this.row[this.fourthRow].setHasExtraBonus(true);

    }

    private void resetMap(int referenceRow){


        this.firstRow = referenceRow;
        this.nextRow = referenceRow + 1;
        this.thirdRow = referenceRow + 2;
        this.fourthRow = referenceRow + 3;

        this.row[this.firstRow].createDefineRow(1, 0);
        this.row[this.nextRow].createDefineRow(2, 0);
        this.row[this.thirdRow].createDefineRow(3, 0);
        this.row[this.fourthRow].createRandomRow(1,1, false, this.player.getProgress(), 0);

        for (int i = 4; i < row.length; i++){
            this.row[i].resetRow();
        }

    }

    private void tutorialMap(){

        int referenceRow = 0;

        this.firstRow = 0;
        this.nextRow = 1;
        this.thirdRow = 2;
        this.fourthRow = 3;

        this.row[this.firstRow].column[2].setValue(1);
        this.row[this.nextRow].column[1].setValue(1);
        this.row[this.nextRow].column[2].setValue(2);
        this.row[this.nextRow].column[3].setValue(3);
        this.row[this.thirdRow].column[0].setValue(3);
        this.row[this.thirdRow].column[1].setValue(4);
        this.row[this.thirdRow].column[2].setValue(1);
        this.row[this.thirdRow].column[3].setValue(3);
        this.row[this.thirdRow].column[4].setValue(1);
        this.row[this.fourthRow].column[0].setValue(4);
        this.row[this.fourthRow].column[1].setValue(4);
        this.row[this.fourthRow].column[2].setValue(2);
        this.row[this.fourthRow].column[3].setValue(5);
        this.row[this.fourthRow].column[4].setValue(3);

        for (int i = 4; i < row.length; i++){
            this.row[i].resetRow();
        }

    }

    public void newGame(){
        this.player.resetGame();
        this.resetMap(0);
        this.playLeft = true;
        this.playUp = true;
        this.playRight = true;
    }

    public void createTutorial(){
        this.player.resetGame();
        this.tutorialMap();
    }

    public void restoreRowCount(int fourthRow){

        this.fourthRow = fourthRow;
        this.thirdRow = (fourthRow - 1 + 20) % 10;
        this.nextRow = (fourthRow - 2 + 20) % 10;
        this.firstRow = (fourthRow - 3 + 20) % 10;

    }

    public void restoreGame(int columnPlay){


        int originValue = row[firstRow].column[columnPlay].getValue();

        checkPossibilities(columnPlay, originValue);

    }


}
