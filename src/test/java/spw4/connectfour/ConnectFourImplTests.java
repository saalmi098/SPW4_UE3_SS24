package spw4.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConnectFourImplTest {

    private ConnectFourImpl connectFour;

    @BeforeEach
    void setUp() {
        connectFour = new ConnectFourImpl(Player.red);
    }

    @Test
    void startPlayerRed_getPlayerOnTurn() {
        assertEquals(Player.red, connectFour.getPlayerOnTurn());
    }

    @Test
    void startPlayerYellow_getPlayerOnTurn() {
        connectFour = new ConnectFourImpl(Player.yellow);
        assertEquals(Player.yellow, connectFour.getPlayerOnTurn());
    }

    @Test
    void resetPlayerYellow_startPlayerYellow() {
        connectFour.reset(Player.yellow);
        assertEquals(Player.yellow, connectFour.getPlayerOnTurn());
    }

    @Test
    void reset_emptyBoard() {
        connectFour.reset(Player.yellow);

        assertAll(() -> {
            for (int row = 0; row < Constants.ROWS; row++) {
                for (int col = 0; col < Constants.COLS; col++) {
                    assertEquals(Player.none, connectFour.getPlayerAt(row, col));
                }
            }
        });
    }

    @Test
    void playerRedDropCol1_playerAtCol1IsRed() {
        connectFour.drop(0);
        assertAll(() -> {
            assertEquals(Player.red, connectFour.getPlayerAt(Constants.ROWS - 1, 0));
            for (int row = Constants.ROWS - 2; row >= 0; row--) {
                for (int col = 0; col < Constants.COLS; col++) {
                    if (col != 0 && row != Constants.ROWS - 1)
                        assertEquals(Player.none, connectFour.getPlayerAt(row, col));
                }
            }
        });
    }

    @Test
    void dropColOutOfRange_throwsException() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> connectFour.drop(-1));
            assertThrows(IllegalArgumentException.class, () -> connectFour.drop(Constants.COLS));
        });
    }

    @Test
    void playerRedDrop_playerOnTurnIsYellow() {
        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getPlayerOnTurn());
    }

    @Test
    void dropMultipleSameColumn() {
        for (int i = 0; i < Constants.ROWS; i++) {
            connectFour.drop(0);
        }

        assertAll(() -> {
            for (int row = Constants.ROWS - 1; row >= 0; row--) {
                assertEquals(row % 2 == 1 ? Player.red : Player.yellow, connectFour.getPlayerAt(row, 0));
            }
        });
    }

    @Test
    void playerAtOutOfRange_throwsException() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> connectFour.getPlayerAt(-1, 0));
            assertThrows(IllegalArgumentException.class, () -> connectFour.getPlayerAt(0, -1));
            assertThrows(IllegalArgumentException.class, () -> connectFour.getPlayerAt(Constants.ROWS, 0));
            assertThrows(IllegalArgumentException.class, () -> connectFour.getPlayerAt(0, Constants.COLS));
        });
    }

    @Test
    void emptyBoard_toString() {
        assertEquals(TestConstants.SCENARIO_0, connectFour.toString());
    }

    @Test
    void playerRedDropCol4_toString() {
        connectFour.drop(3);
        assertEquals(TestConstants.SCENARIO_1, connectFour.toString());
    }

    @Test
    void playerRedAndYellowDrop_toString() {
        connectFour.drop(3);
        connectFour.drop(2);
        connectFour.drop(4);
        connectFour.drop(3);
        connectFour.drop(5);
        connectFour.drop(6);
        connectFour.drop(4);
        connectFour.drop(5);
        connectFour.drop(3);
        assertEquals(TestConstants.SCENARIO_2, connectFour.toString());
    }

    @Test
    void ctor_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new ConnectFourImpl(null));
    }

    @Test
    void ctor_PlayerNone_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new ConnectFourImpl(Player.none));
    }

    @Test
    void reset_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> connectFour.reset(null));
    }

    @Test
    void reset_PlayerNone_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> connectFour.reset(Player.none));
    }

    @Test
    void isGameOver_emptyBoard_returnsFalse() {
        assertFalse(connectFour.isGameOver());
    }

    @Test
    void isGameOver_duringGame_returnsFalse() {
        connectFour.drop(0);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());
    }

    @ParameterizedTest
    @MethodSource
    void isGameOver_vertical(int[] dropCols, boolean expected) {
        for (int col : dropCols) {
            connectFour.drop(col);
        }
        assertEquals(expected, connectFour.isGameOver());
    }

    static Stream<Arguments> isGameOver_vertical() {
        return Stream.of(
                Arguments.of(new int[]{0, 1, 0, 3, 0, 5, 0}, true),
                Arguments.of(new int[]{0, 0, 1, 0, 1, 0}, false),
                Arguments.of(new int[]{0, 1, 0, 1, 1, 0, 1, 0}, false),
                Arguments.of(new int[]{0, 1, 0, 1, 0, 2, 1, 2, 0, 2, 3, 2}, true),
                Arguments.of(new int[]{0, 1, 0, 3, 0, 5}, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void isGameOver_horizontal(int[] dropCols, boolean expected) {
        for (int col : dropCols) {
            connectFour.drop(col);
        }
        assertEquals(expected, connectFour.isGameOver());
    }

    static Stream<Arguments> isGameOver_horizontal() {
        return Stream.of(
                Arguments.of(new int[]{0, 0, 1, 1, 2, 2, 3}, true),
                Arguments.of(new int[]{0, 1, 2, 3, 0, 5, 1, 4, 2, 4}, false),
                Arguments.of(new int[]{0, 1, 2, 3, 4, 5, 6}, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void isGameOver_diagonal(int[] dropCols, boolean expected) {
        for (int col : dropCols) {
            connectFour.drop(col);
            System.out.println(connectFour.toString());
        }
        assertEquals(expected, connectFour.isGameOver());
    }

    static Stream<Arguments> isGameOver_diagonal() {
        return Stream.of(
                // bottom-left to top-right
                Arguments.of(new int[]{0, 1, 2, 3, 1, 2, 3, 3, 3, 4, 2}, true),
                Arguments.of(new int[]{0, 1, 2, 3, 1, 2, 3, 3, 3, 4}, false),
                Arguments.of(new int[]{0, 1, 2, 3, 1, 2, 3, 3, 6, 4, 2}, false),

                // top-left to bottom-right
                Arguments.of(new int[]{0, 0, 0, 0, 6, 1, 1, 1, 2, 2, 5, 3}, true),
                Arguments.of(new int[]{0, 0, 0, 0, 6, 1, 1, 1, 2, 2, 5}, false),
                Arguments.of(new int[]{0, 0, 0, 0, 6, 1, 1, 1, 2, 5, 5, 3}, false)
        );
    }

    @Test
    void getPlayerOnTurn_afterGameOver() {
        for (int i : new int[]{0, 1, 2, 3, 1, 2, 3, 3, 3, 4, 2}) {
            connectFour.drop(i);
        }

        assertEquals(Player.none, connectFour.getPlayerOnTurn());
    }


    @Test
    void getWinner_afterGameOver() {
        for (int i : new int[]{0, 1, 2, 3, 1, 2, 3, 3, 3, 4, 2}) {
            connectFour.drop(i);
        }

        assertEquals(Player.red, connectFour.getWinner());
    }

    @Test
    void getWinner_beforeGameOver_isNone() {
        for (int i : new int[]{0, 1, 2, 3, 1, 2}) {
            connectFour.drop(i);
        }

        assertEquals(Player.none, connectFour.getWinner());
    }

    @Test
    void drop_noSpaceAvailable_throwException() {
        assertThrows(IllegalArgumentException.class, () -> {
            for (int i = 0; i <= Constants.ROWS; i++) {
                connectFour.drop(0);
            }
        });
    }

    @Test
    void gameOver_noWinner() {

        for (int y = 0; y < Constants.ROWS; y++) {
            for (int x = 0; x < Constants.COLS; x++) {
                if(y == 0 || y == 3)
                    connectFour.drop((x+1) % Constants.COLS);
                else
                    connectFour.drop(x);
            }
        }

        assertAll(() -> {
            assertTrue(connectFour.isGameOver());
            assertEquals(Player.none, connectFour.getWinner());
        });
    }
}
