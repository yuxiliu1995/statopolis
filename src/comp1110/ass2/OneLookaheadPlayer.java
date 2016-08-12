package comp1110.ass2;

import java.util.Arrays;

/**
 * A player that looks one step ahead and play the minimax move.
 *
 * Created by Yuxi Liu (u5950011) on 8/13/16.
 */
public class OneLookaheadPlayer extends Player {
    OneLookaheadPlayer() {
        super("One Lookahead Player");
    }

    /**
     * An extremely ugly minimax function.
     *
     * @param placement The placement of game that the player faces now.
     * @param myPiece The piece that the player can play.
     * @param opponentsPiece The piece that the opponent can play next turn.
     * @return The minimax move, one step ahead.
     */
    @Override
    Move move(String placement, char myPiece, char opponentsPiece) {
        Color myColor = whatsMyColor(myPiece);
        Color theirColor = whatsMyColor(opponentsPiece);

        GameField field = StratoGame.placementToGameField(placement);
        Move[] myMoves = field.getPossibleMoves(myPiece);
        Move[] theirMoves;

        int[][] moveValue = new int[myMoves.length][];
        Arrays.fill(moveValue, 0);

        int bestMove;
        int bestMoveScore;
        int responseScore;
        int bestResponseScore;

        Move myMove;
        GameField nextField;
        Move theirMove;
        GameField nextNextField;

        myMove = myMoves[0];
        nextField = field.nextField(myMove);

        theirMoves = nextField.getPossibleMoves(opponentsPiece);

        theirMove = theirMoves[0];
        nextNextField = nextField.nextField(theirMove);
        responseScore = evaluationFunction(nextNextField, theirColor);

        bestResponseScore = responseScore;

        for (int j = 1; j < theirMoves.length; j++) {
            theirMove = theirMoves[j];
            nextNextField = nextField.nextField(theirMove);
            responseScore = evaluationFunction(nextNextField, theirColor);

            if (responseScore > bestResponseScore) {
                bestResponseScore = responseScore;
            }
        }
        bestMoveScore = bestResponseScore;
        bestMove = 0;

        for (int i = 1; i < myMoves.length; i++) {
            myMove = myMoves[i];
            nextField = field.nextField(myMove);

            theirMoves = nextField.getPossibleMoves(opponentsPiece);

            theirMove = theirMoves[0];
            nextNextField = nextField.nextField(theirMove);
            responseScore = evaluationFunction(nextNextField, theirColor);

            bestResponseScore = responseScore;

            for (int j = 1; j < theirMoves.length; j++) {
                theirMove = theirMoves[j];
                nextNextField = nextField.nextField(theirMove);
                responseScore = evaluationFunction(nextNextField, theirColor);

                if (responseScore > bestResponseScore) {
                    bestResponseScore = responseScore;
                }
            }
            if (bestResponseScore > bestMoveScore) {
                bestMoveScore = bestResponseScore;
                bestMove = i;
            }
        }

        return myMoves[bestMove];
    }

    private Color whatsMyColor(char myPiece) {
        return ("ABCDEFGHIJ".contains(Character.toString(myPiece))? Color.RED : Color.GREEN);
    }

    private int evaluationFunction(GameField field, Color myColor) {
        int[] greenScores = field.scoring(Color.GREEN);
        int greenScore = 0;
        for (int i : greenScores) {
            greenScore += i;
        }
        int[] redScores = field.scoring(Color.RED);
        int redScore = 0;
        for (int i : redScores) {
            redScore += i;
        }
        return (myColor == Color.GREEN ? greenScore-redScore : redScore-greenScore);
    }

    // TODO: Play against this bot and see if it actually plays good!
}
