
/**
 * A fun dice game of betting and rolling to play with your friends!
 *
 * @author Jon Rabideau
 * @version 1.0.0 10/29/19
 */

import java.util.*;
public class Chuck{
    private GVdie[] dieArray = new GVdie[3];
    private int creditBalance = 0;
    private String currentMessage = "Welcome to Chuck!";
    private boolean[] bets = new boolean[9];
    private int amountWon = 0;


    /**
     * Default constructor to start new game of Chuck
     */
    public Chuck(){
        for(int i = 0; i < dieArray.length; ++i){
            dieArray[i] = new GVdie();    
        }

        for(int i = 0; i < bets.length; ++i){
            bets[i] = false;
        }

        creditBalance = 10;

        for(int i = 0; i < dieArray.length; ++i){
            dieArray[i].setBlank();
        }
    }

    /**
     * message getter
     * @return currentMessage
     */
    public String getMessage(){
        return currentMessage;
    }

    /**
     * balance getter
     * @return creditBalance
     */
    public int getCredits(){
        return creditBalance;
    }

    /**
     * die array getter
     * @return dieArray
     */
    public GVdie[] getDice(){
        return dieArray;
    }

    /**
     * @return the sum of the value of the dice
     */
    private int getSumDiceValues(){
        int sum = 0;
        for(int i = 0; i < dieArray.length; ++i){
            sum = sum + dieArray[i].getValue();
        }
        return sum;
    }

    /**
     * checks to see if two of the dies values match provided number
     * @return true if there are doubles, false if there aren't
     */
    private boolean isDoubles(int num){
        int matches = 0;
        for(int i = 0; i < dieArray.length; ++i){
            if(dieArray[i].getValue() == num){
                ++matches;
            }
        }

        if(matches >= 2){
            return true;
        }else{
            return false;
        }
    }

    /**
     * checks to see if the value of all three dice are the same
     * @return true if values are equal, otherwise false
     */
    private boolean isTriplets(){
        if((dieArray[0].getValue() == dieArray[1].getValue()) && (dieArray[1].getValue() == dieArray[2].getValue())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * checks if the total value of die is greater than 10, and there are
     * no triplets. If true, gives 10 credits and outputs message stating
     * the player won
     */
    private void checkLarge(){
        if(getSumDiceValues() > 10 && isTriplets() == false){
            amountWon = amountWon + 2;
            currentMessage = "You've got a Large! You won 10 credits!";
            
        }
    }

    /**
     * checks if the total value of die is less than 11, and there are
     * no triplets. If true, gives 2 credits and outputs message stating
     * the player won
     */
    private void checkSmall(){
        if(getSumDiceValues() < 11 && isTriplets() == false){
            amountWon = amountWon + 2;
            currentMessage = "You've got a Small! You won 2 credits!";
        }
    }

    /**
     * checks if the total value of die is less than 8 or greater
     * than 12. If true, gives 2 credits and outputs message stating
     * the player won
     */
    private void checkField(){
        if(getSumDiceValues() < 8 || getSumDiceValues() > 12){
            amountWon = amountWon + 2;
            currentMessage = "You've got a Field! You won 2 credits!";
        }
    }

    /**
     * checks the dice against inputted num to determine if one two
     * or all three match, and gives credits accordingly
     */
    private void checkNumber(int num){
        if(isTriplets() && dieArray[0].getValue() == num){
            amountWon = amountWon + 11;
            currentMessage = "You've got Triplets! You won 11 credits!";
        }else if(isDoubles(num)){
            amountWon = amountWon + 3;
            currentMessage = "You've got Doubles! You won 3 credits!";
        }else if(dieArray[0].getValue() == num || dieArray[1].getValue() == num || dieArray[2].getValue() == num){
            amountWon = amountWon + 2;
            currentMessage = "You have one " + num + "! You won 2 credits!";
        }
    }

    /**
     * checks which bets are made, takes credits for the bets,
     * and checks to see which bets were won
     */
    private void checkAllBets(){
        currentMessage = "Sorry, you lost this bet";
        int betAmount = 0;

        for(int i = 0; i < bets.length; ++i){
            if(bets[i] == true){
                ++betAmount;
                

                if(i <= 5){
                    checkNumber(i+1);
                }

                if(i == 6){
                    checkField();
                }

                if(i == 7){
                    checkSmall();
                }

                if(i == 8){
                    checkLarge();
                }
            }
        }

        creditBalance = creditBalance - betAmount + amountWon;

        if(amountWon > betAmount){
            currentMessage = "You bet " + betAmount + ", and won " + amountWon + "! Congratulations!";
        }else{
            currentMessage = "You bet " + betAmount + ", and won " + amountWon + "! Try Again!"; 
        }

    }

    /**
     * adds amount to credits if positive
     */
    public void addCredits(int amount){
        if(amount > 0){
            creditBalance = creditBalance + amount;
        }
    }

    /**
     * allows the player to place a bet by entering int associated
     * with desired results
     * @param 1-9 inclusive
     */
    public void placeBet(int bet){
        if(bet >= 1 && bet <= 9){
            bets[bet - 1] = true;
        }
    }

    /**
     * allows player to cancel bet by entering associated int
     * @param 1-9 inclusive
     */
    public void cancelBet(int bet){
        if(bet >= 1 && bet <= 9){
            bets[bet - 1] = false;
        }
    }

    /**
     * clears all bets
     */
    public void clearAllBets(){
        for(int i = 0; i < bets.length; ++i){
            bets[i] = false;
        }
    }

    /**
     * resets amountWon back to 0 between rounds
     */
    public void clearAmountWon(){
        amountWon = 0;
    }

    /**
     * rolls the dice, checks if the bets placed were won,
     * and clears the board for the next round
     */
    public void roll(){
        if(enoughCredits()){
            for(int i = 0; i < dieArray.length; ++i){
                dieArray[i].roll();
            }

            checkAllBets();

            clearAllBets();

            clearAmountWon();
        }
    }

    /**
     * resets the game back to default
     */
    public void reset(){
        for(int i = 0; i < dieArray.length; ++i){
            dieArray[i].setBlank();
        }

        creditBalance = 10;
        currentMessage = "Welcome to Chuck!";
        clearAllBets();
    }

    /**
     * checks if player has enough credits to make desired bets,
     * if true deducts credits from balance
     */
    public boolean enoughCredits(){
        int betAmount = 0;

        for(int i = 0; i < bets.length; ++i){
            if(bets[i] == true){
                ++betAmount;
            }
        }

        if(betAmount <= creditBalance){
            return true;
        }else{
            currentMessage = "Sorry, you don't have enough credits to make the desired bet(s).";
            return false;
        }
    }

    public void testRoll(int v1, int v2, int v3){
        while(dieArray[0].getValue() != v1){
            dieArray[0].roll();
        }

        while(dieArray[1].getValue() != v2){
            dieArray[1].roll();
        }

        while(dieArray[2].getValue() != v3){
            dieArray[2].roll();
        }

        checkAllBets();

        clearAllBets();
        
        clearAmountWon();
    }
    
    
}
