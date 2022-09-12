package osmosis.chessdemo.chess.pieces;

import javafx.scene.image.Image;
import osmosis.chessdemo.chess.exceptions.InvalidPromotionMoveException;
import osmosis.chessdemo.chess.helper.PieceDragListener;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.chess.position.File;
import osmosis.chessdemo.chess.position.Rank;
import osmosis.chessdemo.functionailties.DraggableImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getBlackPieceSymbol;
import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getWhitePieceSymbol;

public class Pawn extends Piece {
	private static final Image BLACK_SYMBOL = getBlackPieceSymbol(getPieceName());
	private static final Image WHITE_SYMBOL = getWhitePieceSymbol(getPieceName());
	private static final Map<PromotionPiece, BiFunction<PieceColor, ChessPosition, Piece>> PROMOTION_MAP = new HashMap<>();

	static {
		PROMOTION_MAP.put(PromotionPiece.Queen, Queen::new);
		PROMOTION_MAP.put(PromotionPiece.Rook, Rook::new);
		PROMOTION_MAP.put(PromotionPiece.Knight, Knight::new);
		PROMOTION_MAP.put(PromotionPiece.Bishop, Bishop::new);
	}

	public Pawn(PieceColor color, ChessPosition position) {
		super(color, position, getSymbol(color));
	}

	private static DraggableImageView getSymbol(PieceColor color) {
		return new DraggableImageView(PieceColor.BLACK.equals(color) ? BLACK_SYMBOL : WHITE_SYMBOL);
	}

	private static String getPieceName() {
		return Pawn.class.getSimpleName().toLowerCase();
	}

	@Override
	public boolean isMovementValid(ChessPosition destinationPosition) {
		int fileDifference = Math.abs(destinationPosition.getFile().getFileNumber() - position.getFile().getFileNumber());
		int rankDifference = destinationPosition.getRank().getRankNumber() - position.getRank().getRankNumber();
		if (color == PieceColor.BLACK) {
			rankDifference *= -1;
		}
		return isAdvance(rankDifference, fileDifference) || isTake(rankDifference, fileDifference) || isTwoSquareMove(rankDifference, fileDifference);
	}

	private boolean isAdvance(int rankDifference, int fileDifference) {
		return rankDifference == 1 && fileDifference == 0;
	}

	private boolean isTake(int rankDifference, int fileDifference) {
		return rankDifference == 1 && fileDifference == 1;
	}

	private boolean isTwoSquareMove(int rankDifference, int fileDifference) {
		return fileDifference == 0 && rankDifference == 2 && isRankValidForTwoSquareMove();
	}

	private boolean isRankValidForTwoSquareMove() {
		return (color == PieceColor.BLACK && Rank.SEVENTH.equals(position.getRank())) || (color == PieceColor.WHITE && Rank.SECOND.equals(position.getRank()));
	}

	public Piece promote(PromotionPiece promotionPiece, File file) {
		Piece piece = getPromotionPiece(promotionPiece, file);
		PieceDragListener dragListener = getDragListener();
		dragListener.setPiece(piece);
		piece.setDragListener(dragListener);
		return piece;
	}

	private Piece getPromotionPiece(PromotionPiece promotionPiece, File file) {
		ChessPosition position = getPromotionPosition(file);
		if (!isMovementValid(position)) {
			throw new InvalidPromotionMoveException();
		}
		return PROMOTION_MAP.get(promotionPiece).apply(color, position);
	}

	private ChessPosition getPromotionPosition(File file) {
		return new ChessPosition(file, PieceColor.WHITE.equals(color) ? Rank.EIGHTH : Rank.FIRST);
	}

	@Override
	public Piece copy() {
		return new Pawn(color, position);
	}

	public enum PromotionPiece {
		Queen, Rook, Knight, Bishop
	}
}
