import java.util.Scanner;

public class GameController {
    private Board board;
    private Scanner scanner;
    private boolean gameOver;
    private boolean gameWon;

    public GameController(int rows, int cols, Difficulty difficulty) {
        int mines = calculateMines(rows, cols, difficulty);
        this.board = new Board(rows, cols, mines);
        this.scanner = new Scanner(System.in);
        this.gameOver = false;
        this.gameWon = false;
    }

    private int calculateMines(int rows, int cols, Difficulty difficulty) {
        int totalCells = rows * cols;
        switch (difficulty) {
            case EASY: return totalCells / 8;
            case MEDIUM: return totalCells / 6; //
            case HARD: return totalCells / 4;
            default: return totalCells / 8;
        }
    }

    public void startGame() {
        System.out.println("Minesweeper Game Starting!");
        System.out.println("Commands: [row] [col] - Reveal cell | F [row] [col] - Place flag");

        while (!gameOver && !gameWon) {
            displayGameStatus();
            processUserInput();

            gameWon = board.isGameWon();
        }

        endGame();
    }

    private void displayGameStatus() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║         MINESWEEPER         ║");
        System.out.println("╠══════════════════════════════╣");
        board.display();
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ Revealed Cells: " + board.getRevealedCells() +
                " / " + (board.getRows() * board.getCols() - board.getTotalMines()) + " ║");
        System.out.println("║ Remaining Mines: " + board.getTotalMines() + "             ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private void processUserInput() {
        while (true) {
            System.out.print("\nYour command: ");
            String input = scanner.nextLine().trim();

            if (input.toUpperCase().startsWith("F ")) {
                // Flag command - Bayrak komutu
                if (processFlagCommand(input)) break;
            } else {
                // Reveal command - Açma komutu
                if (processRevealCommand(input)) break;
            }
        }
    }

    private boolean processFlagCommand(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 3) {
            try {
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                if (board.toggleFlag(row, col)) {
                    return true;
                } else {
                    System.out.println("Cannot place flag there!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid coordinates! Usage: F row col");
            }
        } else {
            System.out.println("Invalid command! Usage: F row col");
        }
        return false;
    }

    private boolean processRevealCommand(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (!board.isValidPosition(row, col)) {
                    System.out.println("Invalid coordinates! Try again.");
                    return false;
                }

                boolean safe = board.revealCell(row, col);
                if (!safe) {
                    gameOver = true;
                }
                return true;

            } catch (NumberFormatException e) {
                System.out.println("Invalid coordinates! Usage: row col");
            }
        } else {
            System.out.println("Invalid command! Usage: row col");
        }
        return false;
    }

    private void endGame() {
        board.revealAllMines();
        displayGameStatus();

        if (gameWon) {
            System.out.println("\n🎉 CONGRATULATIONS! YOU WON! 🎉");
        } else {
            System.out.println("\n💥 GAME OVER! You hit a mine! 💥");
        }
    }
}