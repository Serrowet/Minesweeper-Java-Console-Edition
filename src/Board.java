import java.util.Random;

public class Board {
    private Cell[][] grid;
    private int rows;
    private int cols;
    private int totalMines;
    private int revealedCells;
    private Random random;

    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.random = new Random();
        this.grid = new Cell[rows][cols];
        initializeBoard();
        placeMines();
        calculateAdjacentMines();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void placeMines() {
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            if (!grid[row][col].isMine()) {
                grid[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }


    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].isMine()) {
                    int count = countAdjacentMines(i, j);
                    grid[i][j].setAdjacentMines(count);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int newRow = row + i;
                int newCol = col + j;

                if (isValidPosition(newRow, newCol) && grid[newRow][newCol].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }


    public boolean revealCell(int row, int col) {
        if (!isValidPosition(row, col) || grid[row][col].isRevealed() || grid[row][col].isFlagged()) {
            return true;
        }

        grid[row][col].setRevealed(true);
        revealedCells++;


        if (grid[row][col].isMine()) {
            return false;  // Oyun bitti
        }


        if (grid[row][col].getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    revealCell(row + i, col + j);
                }
            }
        }

        return true;
    }

    public boolean toggleFlag(int row, int col) {
        if (!isValidPosition(row, col) || grid[row][col].isRevealed()) {
            return false;
        }

        grid[row][col].setFlagged(!grid[row][col].isFlagged());
        return true;
    }

    public boolean isGameWon() {
        return revealedCells == (rows * cols - totalMines);
    }

    public void revealAllMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].isMine()) {
                    grid[i][j].setRevealed(true);
                }
            }
        }
    }


    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }


    public void display() {

        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();


        for (int i = 0; i < rows; i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }


    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getTotalMines() { return totalMines; }
    public int getRevealedCells() { return revealedCells; }
}