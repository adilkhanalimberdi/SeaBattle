package process;

import java.util.*;

public class GameBoard extends Supporter {
    private int n = 10;
    private int ships;
    private Box[][] board = new Box[n][n];
    private ArrayList <Ship> shipList;


    public GameBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = Box.EMPTY;
            }
        }
        shipList = new ArrayList<>();
        ships = 0;
    }


    public void initBoard() {

        // Four-decker ship
        fourDeckShips();

        // Three-decker ships
        threeDeckShips();

        // Double-decker ships
        // doubleDeckShips();

        // Single-decker ships
        // singleDeckShips();

        System.out.println("Alright!\n");
    }

    // getter

    public ArrayList <Ship> getShipList() {
        return this.shipList;
    }

    public Box[][] getBoard() {
        return this.board;
    }

    public int getShips() {
        return ships;
    }

    // initializing ships
    public void fourDeckShips() {
        System.out.print("Enter the coordinates of a four-decker ship (x,y; x,y; x,y; x,y): ");
        boolean check = true;
        while (check) {
            String usr = Supporter.in.nextLine();
            check = placeShip(usr, 4);
        }
    }

    public void threeDeckShips() {
        boolean check;
        for (int i = 1; i <= 2; i++) {
            System.out.printf("Enter the coordinates of %s three-decker ships (x,y; x,y; x,y): ", Supporter.mp.get(i));
            check = true;
            while (check) {
                String usr = Supporter.in.nextLine();
                check = placeShip(usr, 3);
            }
        }
    }

    public void doubleDeckShips() {
        boolean check;
        for (int i = 1; i <= 3; i++) {
            System.out.printf("Enter the coordinates of %s double-decker ships (x,y; x,y): ", Supporter.mp.get(i));
            check = true;
            while (check) {
                String usr = Supporter.in.nextLine();
                check = placeShip(usr, 2);
            }
        }
    }

    public void singleDeckShips() {
        boolean check;
        for (int i = 1; i <= 4; i++) {
            System.out.printf("Enter the coordinates of %s single-decker ships (x,y): ", Supporter.mp.get(i));
            check = true;
            while (check) {
                String usr = Supporter.in.nextLine();
                check = placeShip(usr, 1);
            }
        }
    }


    public boolean placeShip(String usr, int deckNum) {

        // checking number of coordinates
        String[] parts = usr.trim().split(";");
        if (parts.length != deckNum) {
            System.out.print("Invalid number of coordinates.\nTry again: ");
            return true;
        }

        int[][] coordinates = new int[deckNum][2];
        for (int i = 0; i < deckNum; i++) {

            // checking number of arguments
            String[] temp = parts[i].trim().split(",");
            if (temp.length != 2) {
                System.out.println("Invalid number of arguments.\nTry again: ");
                return true;
            }

            // checking coordinate for range
            int x = Integer.parseInt(temp[0]), y = Integer.parseInt(temp[1]);
            if (!(x >= 0 && x <= 9 && y >= 0 && y <= 9)) {
                System.out.printf("The coordinate (%d, %d) is outside the valid range (0–9).\nTry again: ", x, y);
                return true;
            }

            // checking coordinate for EMPTY
            Box place = this.board[x][y];
            if (place != Box.EMPTY) {
                System.out.printf("The coordinate (%d, %d) isn't empty.\nTry again: ", x, y);
                return true;
            }

            // checking coordinate for distance
            boolean ok = false;
            for (int[] p : Supporter.dir) {
                int nx = p[0] + x, ny = p[1] + y;
                if (!(nx >= 0 && nx <= 9 && ny >= 0 && ny <= 9)) {
                    continue;
                }
                if ((i - 1) >= 0 && nx == coordinates[i - 1][0] && ny == coordinates[i - 1][1]) {
                    continue;
                }
                if (this.board[nx][ny] != Box.EMPTY) {
                    System.out.printf("The coordinate (%d, %d) is too close to another ship.\nPlease try again: ", x, y);
                    return true;
                }
            }

            // checking for diagonal
            if (i > 0 && Math.abs(coordinates[i - 1][0] - x) == Math.abs(coordinates[i - 1][1] - y)) {
                System.out.printf("The coordinate (%d, %d) isn't correct!\nPlease try again: ", x, y);
                return true;
            }

            coordinates[i][0] = x;
            coordinates[i][1] = y;
        }

        Ship ship = new Ship(this);

        // placing ships
        for (int i = 0; i < deckNum; i++) {
            this.placeBox(coordinates[i][0], coordinates[i][1], Box.SHIP);
            ship.addCoordinate(coordinates[i]);
        }

        ships++;
        shipList.add(ship);
        return false;
    }


    public int attack(GameBoard opponent, int currentPlayer) {
        System.out.print("Enter the coordinate to attack (x, y): ");

        int[] coordinates = nextCoordinate();
        int x = coordinates[0], y = coordinates[1];

        if (opponent.board[x][y] == Box.SHIP) {
            // player hits

            opponent.placeBox(x, y, Box.DEFEATED);
            Ship currentShip = findShip(opponent.shipList, x, y);
            if (!currentShip.isAlive()) {
                // player defeated ship

                opponent.shipList.remove(currentShip);
                opponent.ships--;
                System.out.println("The ship was defeated!");
                opponent.update(opponent, currentShip);
            } else
                // player hits ship

                System.out.println("Hitting the ship!");
            return currentPlayer;
        } else if (opponent.board[x][y] == Box.DEFEATED) {
            // player entered invalid coordinate

            System.out.println("You have already attacked this coordinate!");
            return currentPlayer;
        }
        // player misses

        opponent.placeBox(x, y, Box.MISSED);
        System.out.println("Missed!");
        return 1 - currentPlayer;
    }


    public Ship findShip(ArrayList <Ship> shipList, int x, int y) {
        for (Ship ship : shipList) {
            for (int[] coordinates : ship.getCoordinates()) {
                if (coordinates[0] == x && coordinates[1] == y) {
                    return ship;
                }
            }
        }
        return null;
    }


    public void update(GameBoard player, Ship defeatedShip) {
        for (int[] shipPiece : defeatedShip.getCoordinates()) {
            for (int[] p : Supporter.dir) {
                int nx = shipPiece[0] + p[0], ny = shipPiece[1] + p[1];
                if (!(nx >= 0 && nx <= 9 && ny >= 0 && ny <= 9))
                    continue;
                if (player.board[nx][ny] == Box.DEFEATED)
                    continue;
                player.placeBox(nx, ny, Box.MISSED);
            }
        }
    }


    public int[] nextCoordinate() {
        boolean check = true;
        int x = -1, y = -1;

        while (check) {
            String usr = Supporter.in.nextLine().trim();
            String[] parts = usr.split(",");

            if (parts.length != 2) {
                System.out.print("Enter the correct arguments!\nPlease try again: ");
                continue;
            }
            try {
                x = Integer.parseInt(parts[0].trim());
                y = Integer.parseInt(parts[1].trim());
                check = false;
            } catch (Exception e) {
                System.out.print("Invalid input, use the format (x,y).\nPlease try again: ");
            }
        }
        return new int[]{x, y};
    }


    public void displayBoard() {
        Box[][] board = this.board;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j].getRep() + " ");
            }
            System.out.println();
        }
    }

    public void displayBoardForOpponent() {
        Box[][] board = this.board;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String rep = board[i][j].getRep();
                if (rep.equals(Box.SHIP.getRep())) {
                    System.out.print(Box.EMPTY.getRep());
                } else {
                    System.out.print(rep);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void placeBox(int x, int y, Box box) {
        this.board[x][y] = box;
    }

}
