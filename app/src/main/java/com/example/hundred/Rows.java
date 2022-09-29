package com.example.hundred;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.hundred.MainActivity.NUM_COLUMNAS;
import static com.example.hundred.Utilities.*;

public class Rows {

    public Rows() {
        for (int i = 0; i < NUM_COLUMNS; i++) {
            column[i] = new Columns(0, i);
        }
    }

    public Rows(boolean hasBonusCas) {
        this.hasBonusCas = hasBonusCas;
        for (int i = 0; i < NUM_COLUMNS; i++) {
            column[i] = new Columns(0, i);
        }
    }

    public class Columns {

        private int state;
        private int value;

        public void bonusPlus(int operation) {
            int lastValue = this.getValue();
            this.setValue(lastValue + operation);
        }

        public Columns(int state, int value) {
            this.state = state;
            this.value = value;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getValue() {
            return value;
        }

        public String getString() {
            return Integer.toString(value);
        }

        public void setValue(int value) {
            this.value = value;
        }

        protected void resetColumn() {

            this.state = 0;
            this.value = 0;

        }

    }

    private boolean hasBonusCas;
    private boolean hasExtraBonus;
    protected Columns[] column = new Columns[NUM_COLUMNS];
    private int a = 3;
    private int b = 3;
    private int c = 3;
    private int d = 1;
    private int e = 3;
    private int f = 1;

    public boolean isHasBonusCas() {
        return hasBonusCas;
    }

    public void setHasBonusCas(boolean hasBonusCas) {
        this.hasBonusCas = hasBonusCas;
    }

    private void level(int maxBubble) {

        if (maxBubble < nivel_1) {
            a = 3;
            b = 3;
            c = 3;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble > nivel_1 && maxBubble < nivel_2) {
            a = 1;
            b = -1;
            c = 3;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_2 && maxBubble < nivel_2_2) {
            a = -1;
            b = -1;
            c = 3;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_2_2 && maxBubble < nivel_3) {
            a = -3;
            b = 3;
            c = 1;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_3 && maxBubble < nivel_3_2) {
            a = -3;
            b = -1;
            c = 1;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_3_2 && maxBubble < nivel_4) {
            a = -3;
            b = 1;
            c = 1;
            d = 0;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_4 && maxBubble < nivel_4_2) {
            a = -3;
            b = -1;
            c = 1;
            d = 0;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_4_2 && maxBubble < nivel_5) {
            a = -3;
            b = -3;
            c = 1;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_5 && maxBubble < nivel_5_2) {
            a = -3;
            b = -3;
            c = -1;
            d = 0;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_5_2 && maxBubble < nivel_6) {
            a = -3;
            b = -3;
            c = -1;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_6 && maxBubble < nivel_6_2) {
            a = -3;
            b = -3;
            c = -1;
            d = 0;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_6_2 && maxBubble < nivel_7) {
            a = -3;
            b = -3;
            c = 1;
            d = 0;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_7 && maxBubble < nivel_7_2) {
            a = -3;
            b = -3;
            c = -1;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_7_2 && maxBubble < nivel_8) {
            a = -3;
            b = 4;
            c = 1;
            d = 3;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_8 && maxBubble < nivel_8_2) {
            a = -3;
            b = -3;
            c = 0;
            d = 1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_8_2 && maxBubble < nivel_9) {
            a = -3;
            b = -3;
            c = 1;
            d = 0;
            e = 3;
            f = 7;
        } else if (maxBubble >= nivel_9 && maxBubble < nivel_9_2 ) {
            a = -3;
            //b=4;
            b = 5;
            //c = -3
            c = -4;
            d = -1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_9_2 && maxBubble < nivel_10 ){
            a = -3;
            b = -3;
            c = 1;
            d = 0;
            e = 3;
            f = 7;
        } else if (maxBubble >= nivel_10 && maxBubble < nivel_10_2 ) {
            a = -3;
            b = 4;
            c = -3;
            d = -1;
            e = 3;
            f = 1;
        } else if (maxBubble >= nivel_10_2 && maxBubble < nivel_11 ){
            a = -3;
            b = -1;
            c = 1;
            d = -5;
            e = 3;
            f = 9;
        } else if (maxBubble >= nivel_11 && maxBubble < nivel_11_2 ) {
            a = -3;
            b = 2;
            c = -9;
            d = -1;
            e = 3;
            f = -1;
        } else if (maxBubble >= nivel_11_2 && maxBubble < nivel_12 ){
            a = -3;
            b = -1;
            c = 1;
            d = -5;
            e = 2;
            f = 7;
        } else if (maxBubble >= nivel_12) {
            a = -3;
            b = 2;
            c = -9;
            d = -1;
            e = 3;
            f = 1;
        } else {
            int a = 3;
            int b = 3;
            int c = 3;
            int d = 1;
            int e = 3;
            int f = 1;
        }

    }

    public void resetRow() {

        for (int i = 0; i < column.length; i++) {

            this.column[i].resetColumn();

        }

    }

    public void createDefineRow(int value, int state){


        for (int i = 0; i < column.length; i++){
            this.column[i].setValue(value);
            this.column[i].setState(state);
        }

    }

    private void setComodinRow(int originValue, int maxBubble){

        final int randomColumn = new Random().nextInt(5);

        this.column[randomColumn].setState(6);

    }

    private void setPlusRow(int originValue, int maxBubble){

        this.column[2].setState(2);

    }

    private void setSwipeRow(int originValue, int maxBubble, boolean isMaxPlay){

        int bonusPossibilittis = 10;

        if (isMaxPlay) {
            if (maxBubble < 30) {
                bonusPossibilittis = 6;
            } else {
                bonusPossibilittis = 7;
            }
        } else if (maxBubble > 100) {
            bonusPossibilittis = 6;
        }

        final int randomColumn = new Random().nextInt(bonusPossibilittis);

        if (randomColumn < NUM_COLUMNS) {
            this.column[randomColumn].setState(1);
        }


    }

    private void setExchangeRow(int originValue, int maxBubble){

        this.column[1].setState(7);
        this.column[3].setState(7);

    }

    private void setNoBubbleRow(int originValue, int maxBubble, boolean isNearHundred) {

        final int randomColumn = new Random().nextInt(4 - 1) + 1;
        final int randomColumn2 = new Random().nextInt(5);

        for (int i = 0; i < NUM_COLUMNS; i++) {
            this.column[i].setState(0);
            if (isNearHundred) {
                if (i == 2) {
                    this.column[i].setState(3);
                    this.column[i].setValue(0);
                } else if (i == randomColumn && originValue % 2 == 0) {
                    this.column[i].setState(1);
                }
            } else {
                if (i == randomColumn) {
                    this.column[i].setState(3);
                    this.column[i].setValue(0);
                }
                if (maxBubble > SECOND_NO_BUBBLE) {
                    if (i == randomColumn2) {
                        this.column[i].setState(3);
                        this.column[i].setValue(0);
                    }
                }
            }

        }

    }

    protected void setBombRow(int originValue, int maxBubble){

        final int randomColumn = new Random().nextInt(5);
        int randomColumn2 = new Random().nextInt(5);
        final int randomColumn3 = new Random().nextInt(5);
        final int randomColumn4 = new Random().nextInt(5);

        if (randomColumn == randomColumn2){
            if (randomColumn > 0){
                randomColumn2 = 0;
            } else if (randomColumn == 4  ){
                randomColumn2 = 3;
            }
        }

        for (int i = 0 ; i<NUM_COLUMNS ; i++ ) {
            this.column[i].setState(0);
            if (i == randomColumn) {
                this.column[i].setState(4);
            }
            if (i == randomColumn2 && maxBubble > INTERVAL_BOMB_ROW) {
                this.column[i].setState(4);
            }
            if (i == randomColumn3 && maxBubble > INTERVAL_RUSH) {
                this.column[i].setState(4);
            }
            if (i == randomColumn4 && maxBubble > INTERVAL_RUSH * 2) {
                this.column[i].setState(4);
            }
            if (maxBubble > INTERVAL_BOMB_ROW * 6) {
                this.column[i].setState(4);
            }
        }

    }

    private void setRushUpRow(int originValue, int maxBubble){

        final int randomColumn = new Random().nextInt(5);

        for (int i = 0 ; i<NUM_COLUMNS ; i++ ){
            this.column[i].setState(0);
            if ( i == randomColumn){
                this.column[i].setState(5);
            }
        }

    }

    public void createRandomRow(int originValue, int maxBubble, boolean isMaxPlay, int progress, int specialRow){

        this.setHasExtraBonus(false);

        level(maxBubble);

        List<Integer> numFuturos = new ArrayList<>(NUM_COLUMNAS + 1);
        numFuturos.add(originValue + a);
        numFuturos.add(originValue + b);
        numFuturos.add(originValue + c);
        numFuturos.add(originValue + d);
        numFuturos.add(originValue + e);
        numFuturos.add(originValue + f);

        Random random = new Random();

        for (int i = 0 ; i<NUM_COLUMNS ; i++ ){
            int randomIndex = random.nextInt(numFuturos.size());
            this.column[i].setValue(numFuturos.get(randomIndex));
            this.column[i].setState(0);
            numFuturos.remove(randomIndex);
        }

        if (specialRow != 0) {
            if (specialRow == 4){
                setBombRow(originValue, maxBubble);
            }
        } else if (originValue > INTERVAL_RUSH - 6 && originValue < INTERVAL_RUSH || originValue > INTERVAL_RUSH * 2 - 7 && originValue < INTERVAL_RUSH * 2 || originValue > INTERVAL_RUSH * 3 - 8 && originValue < INTERVAL_RUSH * 3 || originValue > INTERVAL_RUSH * 4 - 9 && originValue < INTERVAL_RUSH * 4 || originValue > INTERVAL_RUSH * 5 - 10 && originValue < INTERVAL_RUSH * 5){
            setNoBubbleRow(originValue, maxBubble, true);
        } else if ( originValue % INTERVAL_RUSH_UP == 0 && isMaxPlay){
            setRushUpRow(originValue, maxBubble);
        } else if (progress % INTERVAL_EXCHANGE == 0){
            setExchangeRow(originValue, maxBubble);
        } else if (originValue % INTERVAL_COMODIN == 0 && isMaxPlay && originValue >= START_COMODIN){
            setComodinRow(originValue, maxBubble);
        } else if (originValue % INTERVAL_PLUS_BONUS == 0 && isMaxPlay){
            setPlusRow(originValue, maxBubble);
        } else if (originValue >= START_NO_BUBBLE && originValue % INTERVAL_NO_BUBBLE == 0 && isMaxPlay){
            setNoBubbleRow(originValue, maxBubble, false);
        } else if ( originValue % INTERVAL_BOMB_ROW == 0 && isMaxPlay) {
            setBombRow(originValue, maxBubble);
        } else if ( originValue % INTERVAL_SWIPE_BONUS == 0){
            setSwipeRow(originValue, maxBubble, isMaxPlay);
        } else if ( progress > INTERVAL_RUSH *2 && originValue % 7 == 0){
            setBombRow(originValue, maxBubble);
        } else if ( progress > INTERVAL_RUSH && originValue % 3 == 0){
            setNoBubbleRow(originValue, maxBubble, false);
        } else if ( progress > INTERVAL_RUSH * 4){
            setNoBubbleRow(originValue, maxBubble, false);
        }

    }

    public void explode (int column){

        this.column[column].setValue(0);
        this.column[column].setState(3);

    }

    public void moveRow(int direction){

        int[] casNuevos = new int[NUM_COLUMNS];
        int[] stateNew = new int[NUM_COLUMNS];
        if (direction == 1) {
            casNuevos[0] = this.column[4].getValue();
            casNuevos[1] = this.column[0].getValue();
            casNuevos[2] = this.column[1].getValue();
            casNuevos[3] = this.column[2].getValue();
            casNuevos[4] = this.column[3].getValue();
            stateNew[0] = this.column[4].getState();
            stateNew[1] = this.column[0].getState();
            stateNew[2] = this.column[1].getState();
            stateNew[3] = this.column[2].getState();
            stateNew[4] = this.column[3].getState();
        } else if (direction == 0) {
            casNuevos[0] = this.column[1].getValue();
            casNuevos[1] = this.column[2].getValue();
            casNuevos[2] = this.column[3].getValue();
            casNuevos[3] = this.column[4].getValue();
            casNuevos[4] = this.column[0].getValue();
            stateNew[0] = this.column[1].getState();
            stateNew[1] = this.column[2].getState();
            stateNew[2] = this.column[3].getState();
            stateNew[3] = this.column[4].getState();
            stateNew[4] = this.column[0].getState();
        }

        for (int i = 0; i < NUM_COLUMNS; i ++){
            this.column[i].setValue(casNuevos[i]);
            this.column[i].setState(stateNew[i]);
        }

    }

    public boolean isHasExtraBonus() {
        return hasExtraBonus;
    }

    public void setHasExtraBonus(boolean hasExtraBonus) {
        this.hasExtraBonus = hasExtraBonus;
    }
}
