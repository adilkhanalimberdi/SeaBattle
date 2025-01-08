package game;

import java.util.HashMap;
import java.util.Map;
import process.*;

public class GameWith2P extends Supporter {
    private int currentPlayer;
    private GameBoard[] boards;
    private Map <Integer, String> mp = new HashMap<>(Map.of(1, "First", 2, "Second"));

    public GameWith2P() {
        this.currentPlayer = (int)Math.round(Math.random());

        GameBoard player1 = new GameBoard();
        GameBoard player2 = new GameBoard();

        this.boards = new GameBoard[]{player1, player2};
    }

    public void start() {

        System.out.println("Let's start placing ships on the first players board.");
        boards[0].initBoard();

        System.out.println("Let's start placing ships on the second players board.");
        boards[1].initBoard();

        System.out.println("Game is start!");
        boolean gameOver = false;
        int winner = -1;
        while (!gameOver) {
            GameBoard opponent = boards[1 - currentPlayer];

            opponent.displayBoardForOpponent();
            System.out.println(mp.get(currentPlayer + 1) + " player's turn.");
            currentPlayer = boards[currentPlayer].attack(opponent, currentPlayer);

            if (opponent.getShips() == 0) {
                gameOver = true;
                winner = currentPlayer;
            }
            Supporter.timeSleep(1.3);
        }

        System.out.printf("\nThe %s player wins!!!", mp.get(winner + 1));
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
}
