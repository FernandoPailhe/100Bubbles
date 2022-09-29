package com.example.hundred;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class User {

    public long getMaxBubble() {
        return maxBubble;
    }

    public long getMaxBubbleClassic() {
        return maxBubbleClassic;
    }

    public long getMaxScore() {
        return maxScore;
    }

    public long getMaxScoreClassic() {
        return maxScoreClassic;
    }

    long maxBubble = 0;
    long maxBubbleCount = 0;
    long maxBubbleClassic = 0;

    public void setMaxBubble(long maxBubble) {
        this.maxBubble = maxBubble;
    }

    public void setMaxBubbleClassic(long maxBubbleClassic) {
        this.maxBubbleClassic = maxBubbleClassic;
    }

    public void setMaxScore(long maxScore) {
        this.maxScore = maxScore;
    }

    public void setMaxScoreClassic(long maxScoreClassic) {
        this.maxScoreClassic = maxScoreClassic;
    }

    long maxScore = 0;
    long maxScoreCount = 0;
    long maxScoreClassic = 0;

    public long getCredits() {
        return this.credits;
    }

    public void setCredits(long credits) {
        this.credits = credits;
    }

    long credits = 0;

    public long getNewCredits() {
        return newCredits;
    }

    long newCredits = 0;
    long spendCredits = 0;
    long countMoves = 0;
    long maxCombo = 0;
    long maxBubbleCredits = 0;
    long over100 = 0;
    long over200 = 0;
    long userLevel = 1000;
    long livesUses = 0;
    long liveCost = 0;
    long newGames = 0;
    long totalLivesUses = 0;
    int level = 2;
    String userMail = "";
    String userName = "";
    boolean isLoggedIn = false;
    boolean isLevelUp= false;
    int isMaxBubbleCreditsWon = 0;
    int isComboCreditsWon = 0;

    private boolean isNewCredits = false;


    Utilities utilities = new Utilities();
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


    public boolean isLevelUp() {
        return isLevelUp;
    }

    public User(){

    }

    private void updateUsers (Map updateHashMap){

        if ( isLoggedIn == true) {
            mFirestore.collection("users").document(userMail).update(updateHashMap);
        }
    }

    private void updateNewCredits(long wonCredits){
        if (this.userLevel > 1499) {
            this.newCredits = this.newCredits + (wonCredits / 2);
            Map<String, Object> updateUserCredits = new HashMap<>();
            updateUserCredits.put("newCredits", this.newCredits);
            updateUsers(updateUserCredits);
            this.isNewCredits = true;
        }
    }//TODO Sonido won credits

    public void getWonCredits (boolean isANewGame){
        if (this.userLevel > 1499) {
        long preLevel = userLevel % 1000;
        this.credits = this.credits + this.newCredits;
        this.userLevel = this.userLevel + this.newCredits/2;
        long postLevel = level % 1000;
        this.newCredits = 0;
        this.totalLivesUses = this.totalLivesUses + this.livesUses;
        this.livesUses = 0;
        if (isANewGame == true){
            this.newGames = this.newGames + 1;
        }
        Map<String, Object> updateUserCredits = new HashMap<>();
        updateUserCredits.put("level", this.userLevel);
        updateUserCredits.put("livesUses", this.livesUses);
        updateUserCredits.put("credits", this.credits);
        updateUserCredits.put("newCredits", this.countMoves);
        updateUserCredits.put("newGames", this.newGames);
        updateUserCredits.put("totalLivesUses", this.totalLivesUses);
        updateUsers(updateUserCredits);
        if ( postLevel > preLevel ){
            this.isLevelUp = true;
        } else {
            this.isLevelUp = false;
        }
        }
    }

    public void spentCredits (long creditsCost){

        if ( isLoggedIn == true) {
            credits = credits - creditsCost;
            spendCredits = spendCredits + creditsCost;
            Map<String, Object> updateUserCredits = new HashMap<>();
            updateUserCredits.put("credits", credits);
            updateUserCredits.put("spendCredits", spendCredits);
            updateUsers(updateUserCredits);
        }
    }

    public void updateCountMoves (){

        if ( isLoggedIn == true) {

            countMoves = countMoves + 1;
            Map<String, Object> updateUserProgress = new HashMap<>();
            if (countMoves > utilities.MOVES_TO_WIN_CREDITS) {
                updateNewCredits(utilities.CREDITS_MOVES);
                countMoves = countMoves - utilities.MOVES_TO_WIN_CREDITS;
            }
            updateUserProgress.put("countMoves", countMoves);
            updateUsers(updateUserProgress);
        }
    }

    public void updateMaxCombo (int combo){

        if ( isLoggedIn == true) {
            if (isComboCreditsWon < utilities.LIMIT_MAX_COMBO) {
                maxCombo = combo;
                Map<String, Object> updateUserProgress = new HashMap<>();
                updateUserProgress.put("maxCombo", maxCombo);
                updateUsers(updateUserProgress);
                if (combo % utilities.COMBO_MULTIPLE_CREDITS == 0) {
                    updateNewCredits(utilities.CREDITS_COMBO * utilities.COMBO_MULTIPLE_CREDITS);
                    isComboCreditsWon = isComboCreditsWon + 1;
                }
            }
        }
    }

    public void updateMaxBubbleCount(int jugadaMayor){

        if ( isLoggedIn == true) {
            if (isMaxBubbleCreditsWon < utilities.LIMIT_MAX_BUBBLE) {
                maxBubbleCount = jugadaMayor;
                if (maxBubbleCount == (utilities.MAXBUBBLE_MULTIPLE_CREDITS + maxBubbleCredits)) {
                    updateNewCredits(utilities.CREDITS_MAX * maxBubbleCount);
                    maxBubbleCredits = maxBubbleCredits + maxBubbleCount;
                    isMaxBubbleCreditsWon = isMaxBubbleCreditsWon + 1;
                }
                Map<String, Object> updateUserProgress = new HashMap<>();
                updateUserProgress.put("maxBubbleCount", maxBubbleCount);
                updateUserProgress.put("maxBubbleCredits", maxBubbleCredits);
                updateUsers(updateUserProgress);
            }
        }
    }

    public void updateMaxBubble(int maxBubble) {

        if (isLoggedIn == true) {
            if (maxBubble % utilities.LOOP_MAX_BUBBLE == 0) {
                updateNewCredits(maxBubble);
            }
        }
    }

    public void updateCombo ( int combo){

        if ( isLoggedIn == true) {
            if (combo % utilities.LOOP_COMBO == 0) {
                updateNewCredits(utilities.LOOP_COMBO_MULTIPLE * combo);
            }
        }
    }

    public void updateOver100 (){

        if ( isLoggedIn == true) {
            over100 = over100 + 1;
            long substractLives = livesUses;
            if (substractLives > utilities.OVER_MULTIPLE_CREDITS/3 ){
                substractLives = utilities.OVER_MULTIPLE_CREDITS/3;
            }
            Map<String, Object> updateUserProgress = new HashMap<>();
            updateUserProgress.put("over100", over100);
            updateUsers(updateUserProgress);
            updateNewCredits(100 * utilities.OVER_MULTIPLE_CREDITS - utilities.LIVESUSES_MULTIPLE_SUBSTRACT * substractLives);

        }
    }

    public void updateOver200 (){

        if ( isLoggedIn == true) {
            over200 = over200 + 1;
            long substractLives = livesUses;
            if (substractLives > utilities.OVER_MULTIPLE_CREDITS/3 ){
                substractLives = utilities.OVER_MULTIPLE_CREDITS/3;
            }
            Map<String, Object> updateUserProgress = new HashMap<>();
            updateUserProgress.put("over200", over200);
            updateUsers(updateUserProgress);
            updateNewCredits(200 * utilities.OVER_MULTIPLE_CREDITS - utilities.LIVESUSES_MULTIPLE_SUBSTRACT * substractLives);

        }
    }

    public void updateLivesUses (boolean reset){

        if ( isLoggedIn == true) {

            if (reset == false) {
                livesUses++;
            } else {
                livesUses = 0;
            }
            Map<String, Object> updateUserProgress = new HashMap<>();
            updateUserProgress.put("livesUses", livesUses);
            updateUsers(updateUserProgress);
        }

    }

    public void syncUserData(final boolean firstCheck, String email){

            isLoggedIn = true;
            mFirestore.collection("users").document(email).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (firstCheck == true ){
                                maxBubbleCount = documentSnapshot.getLong("maxBubbleCount");
                                maxScoreCount = documentSnapshot.getLong("maxScoreCount");
                                countMoves = documentSnapshot.getLong("countMoves");
                                credits = documentSnapshot.getLong("credits");
                                newCredits = documentSnapshot.getLong("newCredits");
                                spendCredits = documentSnapshot.getLong("spendCredits");
                                maxCombo = documentSnapshot.getLong("maxCombo");
                                maxBubbleCredits = documentSnapshot.getLong("maxBubbleCredits");
                                livesUses = documentSnapshot.getLong("livesUses");
                                userLevel = documentSnapshot.getLong("level");
                                over100 = documentSnapshot.getLong("over100");
                                over200 = documentSnapshot.getLong("over200");
                            }
                            userMail = documentSnapshot.getString("email");
                            userName = documentSnapshot.getString("name");
                            maxBubble = documentSnapshot.getLong("maxBubble");
                            maxScore = documentSnapshot.getLong("maxScore");
                        }
                    });

    }



    public long getLivesUses() {
        return livesUses;
    }

    public int getIsMaxBubbleCreditsWon() {
        return isMaxBubbleCreditsWon;
    }

    public int getIsComboCreditsWon() {
        return isComboCreditsWon;
    }

    public boolean isNewCredits() {
        return isNewCredits;
    }
}
