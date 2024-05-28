package spw4.connectfour;

public class ConnectFourImpl implements ConnectFour {

    private Player playerOnTurn;
    private final Player[][] board = new Player[Constants.ROWS][Constants.COLS];

    public ConnectFourImpl(Player playerOnTurn) {
        init(playerOnTurn);
    }

    private void init(Player playerOnTurn) {
        if (playerOnTurn == null || playerOnTurn == Player.none) {
            throw new IllegalArgumentException("Player on turn must be red or yellow");
        }

        this.playerOnTurn = playerOnTurn;
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                board[row][col] = Player.none;
            }
        }
    }

    @Override
    public Player getPlayerAt(int row, int col) {
        if (row < 0 || row >= Constants.ROWS || col < 0 || col >= Constants.COLS) {
            throw new IllegalArgumentException("Row or column out of range");
        }
        return board[row][col];
    }

    @Override
    public Player getPlayerOnTurn() {
        if(isGameOver())
            return Player.none;
        return playerOnTurn;
    }

    @Override
    public boolean isGameOver() {
        return getWinner() != Player.none || isBoardFull();
    }

    private boolean isBoardFull() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                if (board[row][col] == Player.none) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Player getWinner() {

        // check horizontal
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS - 3; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row][col + 1] &&
                        board[row][col] == board[row][col + 2] &&
                        board[row][col] == board[row][col + 3]) {
                    return board[row][col];
                }
            }
        }

        // check vertical
        for (int row = 0; row < Constants.ROWS - 3; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row + 1][col] &&
                        board[row][col] == board[row + 2][col] &&
                        board[row][col] == board[row + 3][col]) {
                    return board[row][col];
                }
            }
        }

        // check diagonal (top-left to bottom-right)
        for (int row = 0; row < Constants.ROWS - 3; row++) {
            for (int col = 0; col < Constants.COLS - 3; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row + 1][col + 1] &&
                        board[row][col] == board[row + 2][col + 2] &&
                        board[row][col] == board[row + 3][col + 3]) {
                    return board[row][col];
                }
            }
        }

        // check diagonal (bottom-left to top-right)
        for (int row = 3; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS - 3; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row - 1][col + 1] &&
                        board[row][col] == board[row - 2][col + 2] &&
                        board[row][col] == board[row - 3][col + 3]) {
                    return board[row][col];
                }
            }
        }

        return Player.none;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Player: " + playerOnTurn.getName() + "\n");

        for (int row = 0; row < Constants.ROWS; row++) {
            sb.append("| ");
            for (int col = 0; col < Constants.COLS; col++) {
                sb.append(board[row][col].toString()).append(col == Constants.COLS - 1 ? " " : "  ");
            }

            sb.append("|");

            if(row != Constants.ROWS - 1)
                sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void reset(Player playerOnTurn) {
        init(playerOnTurn);
    }

    @Override
    public void drop(int col) {

        if (col < 0 || col >= Constants.COLS) {
            throw new IllegalArgumentException("Column out of range");
        }

        for (int row = Constants.ROWS - 1; row >= 0; row--) {
            if (board[row][col] == Player.none) {
                board[row][col] = playerOnTurn;
                playerOnTurn = playerOnTurn == Player.red ? Player.yellow : Player.red;
                return;
            }
        }

        throw new IllegalArgumentException("Column is full");
    }
}
