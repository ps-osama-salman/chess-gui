package osmosis.chessdemo.chess.board;

import osmosis.chessdemo.chess.pieces.Piece;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static osmosis.chessdemo.chess.position.ChessNotationProvider.getChessNotation;

public class BoardSquares {
	private final Map<String, Piece> pieces;

	public BoardSquares() {
		pieces = new HashMap<>();
	}

	private static BoardSquares of(Collection<Piece> pieces) {
		BoardSquares boardSquares = new BoardSquares();
		pieces.forEach(boardSquares::put);
		return boardSquares;
	}

	public void put(Piece piece) {
		pieces.put(getChessNotation(piece.getPosition()), piece);
	}

	public Optional<Piece> get(ChessPosition position) {
		return Optional.ofNullable(pieces.get(getChessNotation(position)));
	}

	public void removePieceOn(ChessPosition chessPosition) {
		pieces.remove(getChessNotation(chessPosition));
	}

	public Collection<Piece> getAll() {
		return pieces.values();
	}

	public BoardSquares copy() {
		return of(pieces.values());
	}
}
