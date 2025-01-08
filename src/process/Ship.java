package process;

import java.util.ArrayList;

public class Ship {
    private ArrayList<int[]> coordinates = new ArrayList<>();
    private GameBoard board;

    public Ship(GameBoard board) {
        this.board = board;
    }

    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }

    public void addCoordinate(int[] coordinate) {
        this.coordinates.add(coordinate);
    }

    public boolean isAlive() {
        boolean alive = false;
        Box[][] board = this.board.getBoard();
        for (int[] coordinate : coordinates) {
            if (board[coordinate[0]][coordinate[1]] == Box.SHIP) {
                alive = true;
                break;
            }
        }
        return alive;
    }

    public String toString() {
        StringBuilder res = new StringBuilder("coordinates={");
        for (int[] i : this.coordinates) {
            res.append(i[0]).append(",").append(i[1]).append("; ");
        }
        res.append("}");
        return res.toString();
    }
}
