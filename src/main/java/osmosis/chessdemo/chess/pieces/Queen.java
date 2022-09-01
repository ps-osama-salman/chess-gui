package osmosis.chessdemo.chess.pieces;

import javafx.scene.image.Image;
import osmosis.chessdemo.chess.helper.ChessPosition;
import osmosis.chessdemo.functionailties.DraggableImageView;

import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getBlackPieceSymbol;
import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getWhitePieceSymbol;

public class Queen extends Piece {
	private static final Image BLACK_SYMBOL;
	private static final Image WHITE_SYMBOL;

	static {
		BLACK_SYMBOL = getBlackPieceSymbol(getPieceName());
		WHITE_SYMBOL = getWhitePieceSymbol(getPieceName());
	}

	private DraggableImageView symbol;

	public Queen(PieceColor color, ChessPosition position) {
		super(color, position);
		if (color == PieceColor.BLACK) {
			symbol = new DraggableImageView(BLACK_SYMBOL);
		} else {
			symbol = new DraggableImageView(WHITE_SYMBOL);
		}
	}

	private static String getPieceName() {
		return Queen.class.getSimpleName().toLowerCase();
	}

	public DraggableImageView getImageView() {
		return symbol = symbol.copy();
	}

	@Override
	public boolean validMovement(ChessPosition destinationPosition) {
		int fileDifference = Math.abs(destinationPosition.getFile() - position.getFile());
		int rankDifference = Math.abs(destinationPosition.getRank() - position.getRank());
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
