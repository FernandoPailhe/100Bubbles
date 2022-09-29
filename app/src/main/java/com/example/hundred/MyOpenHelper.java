package com.example.hundred;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String MAPA_TABLE_CREATE = "CREATE TABLE mapa(fila INTEGER, casNum INTEGER, casVal INTEGER, state INTEGER, casBonus INTEGER)";

    private static final String BEST_TABLE_CREATE = "CREATE TABLE best(jugadaMayor INTEGER, puntaje INTEGER, partidas INTEGER, maxBubbleClassic INTEGER, maxScoreClassic INTEGER)";

    private static final String PARTIDA_TABLE_CREATE = "CREATE TABLE game(filaActual INTEGER, jugadaValor INTEGER, jugadaColumna INTEGER, puntaje INTEGER, jugadaMayor INTEGER, combo INTEGER, bonusSwipe INTEGER, bonusPlus INTEGER, lives INTEGER, isMaxBubbleCreditsWon INTEGER, isComboCreditsWon INTEGER, bonusExchange INTEGER, bonusNew INTEGER, bonusNew2 INTEGER)";

    private static final String DB_MAPA_NAME = "100bubblesdb.sqlite";

    private static final int DB_VERSION = 1;

    public MyOpenHelper(Context context, String s, Object o, int i){
        super(context, DB_MAPA_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MAPA_TABLE_CREATE);//Este metodo crea la tabla.
        db.execSQL(BEST_TABLE_CREATE);
        db.execSQL(PARTIDA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
