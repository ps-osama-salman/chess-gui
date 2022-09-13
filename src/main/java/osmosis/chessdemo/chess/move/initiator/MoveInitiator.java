package osmosis.chessdemo.chess.move.initiator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import osmosis.chessdemo.chess.board.Board;
import osmosis.chessdemo.chess.pieces.Piece;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveInitiator {
	private static final MoveInitiator instance = new MoveInitiator();
	private final Collection<Board> boards = new ArrayList<>();

	public static MoveInitiator getInstance() {
		return instance;
	}

	public void initiateMove(Piece piece, ChessPosition destinationPosition) {
		boards.forEach(board -> {
			try {
				board.makeMove(piece, destinationPosition);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		});
	}

	public void registerBoard(Board board) {
		boards.add(board);
	}
}
