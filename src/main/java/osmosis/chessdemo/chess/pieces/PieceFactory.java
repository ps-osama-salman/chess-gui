package osmosis.chessdemo.chess.pieces;

import lombok.AllArgsConstructor;
import osmosis.chessdemo.chess.board.BoardProvider;
import osmosis.chessdemo.chess.helper.PieceDragListener;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.HashMap;
import java.util.function.BiFunction;

@AllArgsConstructor
public class PieceFactory {
	private static final HashMap<Character, BiFunction<PieceColor, ChessPosition, Piece>> PIECE_CREATOR_MAP = new HashMap<>() {
		{
			put('b', Bishop::new);
			put('k', King::new);
			put('n', Knight::new);
			put('p', Pawn::new);
			put('q', Queen::new);
			put('r', Rook::new);
		}
	};
	private final BoardProvider boardProvider;

	private static PieceColor getPieceColor(char c) {
		return Character.isLowerCase(c) ? PieceColor.BLACK : PieceColor.WHITE;
	}

	public Pawn createPawn(PieceColor color, ChessPosition position) {
		return createPiece(color, position, Pawn::new);
	}

	public King createKing(PieceColor color, ChessPosition position) {
		return createPiece(color, position, King::new);
	}

	public Bishop createBishop(PieceColor color, ChessPosition position) {
		return createPiece(color, position, Bishop::new);
	}

	public Queen createQueen(PieceColor color, ChessPosition position) {
		return createPiece(color, position, Queen::new);
	}

	public Rook createRook(PieceColor color, ChessPosition position) {
		return createPiece(color, position, Rook::new);
	}

	public Knight createKnight(PieceColor color, ChessPosition position) {
		return createPiece(color, position, Knight::new);
	}

	private <T extends Piece> T createPiece(PieceColor color, ChessPosition position, BiFunction<PieceColor, ChessPosition, T> supplier) {
		T piece = supplier.apply(color, position);
		piece.setDragListener(new PieceDragListener(boardProvider.get(), piece));
		return piece;
	}

	public Piece createPiece(char c, ChessPosition position) {
		return createPiece(getPieceColor(c), position, PIECE_CREATOR_MAP.get(Character.toLowerCase(c)));
	}
}
