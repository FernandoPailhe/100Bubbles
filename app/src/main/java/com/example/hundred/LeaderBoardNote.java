package com.example.hundred;

public class LeaderBoardNote {

    private static String name;
    private static String email;
    private static long maxBubble;
    private static long maxScore;
    private static long livesUses;
    private static long maxBubbleClassic;
    private static long maxScoreClassic;

    public LeaderBoardNote(){
        //Empty constructor needed by Firebase
    }

    public LeaderBoardNote (String name, String email, long maxBubble, long maxScore){

        this.name = name;
        this.email = email;
        this.maxBubble = maxBubble;
        this.maxScore = maxScore;

    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public long getMaxBubble(){
        return maxBubble;
    }

    public long getMaxScore(){
        return maxScore;
    }

    public long getLivesUses(){
        return livesUses;
    }

    public long getMaxBubbleClassic(){
        return maxBubbleClassic;
    }

    public long getMaxScoreClassic(){
        return maxScoreClassic;
    }

}
