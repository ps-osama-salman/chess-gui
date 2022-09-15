package osmosis.chessdemo.chess.pieces;

import javafx.scene.image.Image;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.functionailties.DraggableImageView;

import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getBlackPieceSymbol;
import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getWhitePieceSymbol;

public class Queen extends Piece {
	private static final Image BLACK_SYMBOL = getBlackPieceSymbol(getPieceName());
	private static final Image WHITE_SYMBOL = getWhitePieceSymbol(getPieceName());

	protected Queen(PieceColor color, ChessPosition position) {
		super(color, position, getSymbol(color));
	}

	private static DraggableImageView getSymbol(PieceColor color) {
		return new DraggableImageView(PieceColor.BLACK.equals(color) ? BLACK_SYMBOL : WHITE_SYMBOL);
	}

	private static String getPieceName() {
		return Queen.class.getSimpleName().toLowerCase();
	}

	@Override
	public boolean isMovementValid(ChessPosition destinationPosition) {
		int fileDifference = Math.abs(destinationPosition.getFile().getFileNumber() - position.getFile().getFileNumber());
		int rankDifference = Math.abs(destinationPosition.getRank().getRankNumber() - position.getRank().getRankNumber());
		if (fileDifference == 0 && rankDifference == 0) {
			return false;
		}
		return (rankDifference == fileDifference) || ((fileDifference == 0) ^ (rankDifference == 0));
	}

	@Override
	public Piece copy() {
		return new Queen(color, position);
	}
}
