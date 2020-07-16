package minesweeper;

import java.util.Random;
import java.util.Scanner;


public class Main {
    static int fSize = 9;
    static int minesNum = 0;
    static char[][] mineField = new char[fSize][fSize];
    static char[][] gameField = new char[fSize][fSize];
    static int [][] numbers = new int[fSize][fSize];
    static int rowX;
    static int colY;
    static String arg;
    static int flags;
    static boolean steppedMine;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        {
            steppedMine = false;
            for (int i = 0; i < fSize; i++) {
                for (int j = 0; j < fSize; j++) {
                    mineField[i][j] = '.';
                    gameField[i][j] = '.';
                }
            }
        }
        System.out.print("How many mines do you want on the field? ");
        minesNum = Integer.parseInt(scanner.nextLine());
        printField();
        initField();
        if(!gameState()) {
            setCoordinates();
            printField();
            while (!gameState()) {
                setCoordinates();
                if (!steppedMine) {
                    printField();
                }
            }
            if (!steppedMine) {
                System.out.println("Congratulations! You found all mines!");
            }
        }
    }

    public static void initField() {
        setFirstCell();
        printField();

    }

    public static void printField() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < fSize; i++) {
            System.out.printf("%d|",i+1);
            for (int j = 0; j < fSize; j++) {
                System.out.print(gameField[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }
    public static void printSteppedMine() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < fSize; i++) {
            System.out.printf("%d|",i+1);
            for (int j = 0; j < fSize; j++) {
                if (mineField[i][j] == 'X'){
                    System.out.print(mineField[i][j]);
                } else {
                    System.out.print(gameField[i][j]);
                }

            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }
    public static void generateMines() {
        Random random = new Random();
        int counter = 0;
        if (minesNum < fSize * fSize) {
            while (counter < minesNum) {
                int row = random.nextInt(fSize);
                int col = random.nextInt(fSize);
                if (mineField[row][col] == '.' && gameField[row][col] == '.') {
                    counter++;
                    mineField[row][col] = 'X';
                }
            }
        }
    }

    public static void checkCells() {
        //corners
        numbers[0][0] = isMine(0,1) + isMine(1,0) + isMine (1 ,1);
        numbers[0][fSize - 1] = isMine(0,fSize - 2) + isMine(1, fSize - 2) + isMine(1, fSize - 1);
        numbers[fSize - 1][0] = isMine(fSize - 2, 0) + isMine(fSize -2, 1) + isMine(fSize - 1, 1);
        numbers[fSize - 1][fSize - 1] = isMine(fSize - 1,fSize - 2) + isMine(fSize - 2, fSize -2) + isMine(fSize - 2, fSize - 1);
        //side
        for (int i = 1; i < fSize - 1; i++) {
            //top
            numbers[0][i] = isMine(0, i - 1) + isMine(0,i + 1) + isMine(1,i - 1) +
                            isMine(1,i) + isMine(1,i + 1);
            //down
            numbers[fSize - 1][i] = isMine(fSize - 1, i - 1) + isMine(fSize - 1, i + 1) +
                                    isMine(fSize - 2,i - 1) + isMine(fSize - 2,i) +
                                    isMine(fSize - 2,i + 1);
            //left
            numbers[i][0] = isMine(i - 1, 0) + isMine(i - 1, 1) + isMine(i, 1) +
                            isMine(i + 1, 0) + isMine(i + 1,1);
            //right
            numbers[i][fSize-1] = isMine(i - 1, fSize - 1) + isMine(i - 1, fSize - 2) +
                                  isMine(i, fSize - 2) + isMine(i + 1, fSize - 2) +
                                  isMine(i + 1, fSize -1);

        }
        //middle
        for (int i = 1; i < fSize - 1; i++) {
            for (int j = 1; j < fSize - 1; j++) {
                numbers[i][j] = isMine(i - 1, j - 1) + isMine(i - 1, j) + isMine(i - 1, j + 1) +
                                isMine(i,j - 1) + isMine(i, j + 1) +
                                isMine(i + 1, j - 1) + isMine(i + 1, j) + isMine(i + 1, j + 1);
            }

        }
    }
    public static int isMine (int row, int col) {
        if (mineField[row][col] == 'X') return 1;
        return 0;
    }

    public static void setFirstCell() {
        boolean corect = false;
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        String cInput = scanner.nextLine().trim();
        String[] tmp = cInput.split(" ");
        if (tmp.length == 3) {
            try {
                rowX = Integer.parseInt(tmp[1]) - 1;
                colY = Integer.parseInt(tmp[0]) - 1;
                arg = tmp[2];
                if (rowX > fSize && rowX < 0 && colY > fSize && colY < 0) {
                    System.out.println("Coordinates should be from 1 to 9!");

                }
                corect = true;
            } catch (NumberFormatException ex) {
                System.out.println("You should enter numbers!");
            }
        } else {
            System.out.println("You should enter numbers!");
        }
        if (corect) {
            //mine
            if (arg.equals("mine")) {
                gameField[rowX][colY] = '*';
                flags++;
            }
            //free
            if (arg.equals("free")) {
                gameField[rowX][colY] = '/';
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (rowX + i < fSize && rowX + i >= 0 && colY + j < fSize && colY + j >= 0)
                        mineField[rowX + i][colY + j] = 0;
                    }
                }
            }
        }
        generateMines();
        checkCells();
        if (arg.equals("free")) {
            exploreEmptyCells(rowX, colY);
        }
        if (gameState()) {
            System.out.println("Congratulations! You found all mines!");
        }
    }
    public static void setCoordinates() {
        boolean corect = false;
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        String cInput = scanner.nextLine().trim();
        String[] tmp = cInput.split(" ");
        if (tmp.length == 3) {
            try {
                rowX = Integer.parseInt(tmp[1]) - 1;
                colY = Integer.parseInt(tmp[0]) - 1;
                arg = tmp[2];
                if (rowX > fSize && rowX < 0 && colY > fSize && colY < 0) {
                    System.out.println("Coordinates should be from 1 to 9!");

                }
                corect = true;
            } catch (NumberFormatException ex) {
                System.out.println("You should enter numbers!");
            }
        } else {
            System.out.println("You should enter numbers!");
        }
        if (corect) {
            //mine
            if (arg.equals("mine")) {
                if (gameField[rowX][colY] == '.') {
                    gameField[rowX][colY] = '*';
                } else if (gameField[rowX][colY] == '*') {
                    gameField[rowX][colY] = '.';
                } else {
                    System.out.println("There is a number here!");
                }
            }
            //free
            if (arg.equals("free")) {
                if (gameField[rowX][colY] == '.' &&
                        mineField[rowX][colY] != 'X' &&
                        numbers[rowX][colY] == 0) {
                    gameField[rowX][colY] = '/';
                    exploreEmptyCells(rowX, colY);
                } else if (gameField[rowX][colY] == '.' &&
                        mineField[rowX][colY] != 'X' &&
                        numbers[rowX][colY] > 0) {
                    gameField[rowX][colY] = Character.forDigit(numbers[rowX][colY], 10);
                } else {
                    System.out.println("You stepped on a mine and failed!");
                    printSteppedMine();
                    steppedMine = true;
                }
            }
        }
        }
    public static boolean gameState() {
        int counter = minesNum;
        int counter2 = 0;
        if(steppedMine) {
            return true;
        }
        flags = 0;
        for (int i = 0; i < fSize; i++) {
            for (int j = 0; j < fSize; j++) {
                if (gameField[i][j] == '*') {
                    flags++;
                }
            }
        }
        if (minesNum == flags) {
            for (int i = 0; i < fSize; i++) {
                for (int j = 0; j < fSize; j++) {
                    if (gameField[i][j] == '*' && mineField[i][j] == 'X') {
                        counter--;
                    }
                }
            }
        }
        if (flags == 0) {
            for (int i = 0; i < fSize; i++) {
                for (int j = 0; j < fSize; j++) {
                    if (gameField[i][j] == '.') {
                        counter2++;
                    }
                }
            }
        }
        if (counter == 0) {
            return true;
        }
        return counter2 == minesNum;
    }
    public static void exploreEmptyCells(int rowX, int colY) {
        cellDirection[] d = cellDirection.values();
        for (cellDirection x : d) {
            if (checkDirection(x, rowX, colY)) {
                if (numbers[rowX + x.row][colY + x.col] > 0) {
                    gameField[rowX + x.row][colY + x.col] = Character.forDigit(numbers[rowX + x.row][colY + x.col], 10);
                } else {
                    gameField[rowX + x.row][colY + x.col] ='/';
                    exploreEmptyCells(rowX + x.row, colY + x.col);
                }

            }
        }


    }
    enum cellDirection {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1),
        ULC(-1, -1),
        ULR(-1, 1),
        DLC(1, -1),
        DRC(1, 1);
        int row;
        int col;

        cellDirection(int row, int col) {
            this.row = row;
            this.col = col;
        }

    }

    public static boolean checkDirection(cellDirection d, int row, int col) {
        int x = row + d.row;
        int y = col + d.col;
        if (x < fSize && x >= 0 && y < fSize && y >= 0) {
            return numbers[x][y] >= 0 && mineField[x][y] != 'X' && gameField[x][y] == '.' || gameField[x][y] == '*';
        }
        return false;
    }


}


