package osmosis.chessdemo.chess.pieces;


import javafx.scene.image.Image;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.functionailties.DraggableImageView;

import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getBlackPieceSymbol;
import static osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider.getWhitePieceSymbol;

public class Rook extends Piece {
	private static final Image BLACK_SYMBOL;
	private static final Image WHITE_SYMBOL;

	static {
		BLACK_SYMBOL = getBlackPieceSymbol(getPieceName());
		WHITE_SYMBOL = getWhitePieceSymbol(getPieceName());
	}

	private DraggableImageView symbol;

	public Rook(PieceColor color, ChessPosition position) {
		super(color, position);
		if (color == PieceColor.BLACK) {
			symbol = new DraggableImageView(BLACK_SYMBOL);
		} else {
			symbol = new DraggableImageView(WHITE_SYMBOL);
		}
	}

	private static String getPieceName() {
		return Rook.class.getSimpleName().toLowerCase();
	}

	public DraggableImageView getImageView() {
		return symbol = symbol.copy();
	}

	@Override
	public boolean isMovementValid(ChessPosition destinationPosition) {
		int fileDifference = Math.abs(destinationPosition.getFile().getFileNumber() - position.getFile().getFileNumber());
		int rankDifference = Math.abs(destinationPosition.getRank().getRankNumber() - position.getRank().getRankNumber());
		return (fileDifference == 0) ^ (rankDifference == 0);
	}

	@Override
	public Piece copy() {
		return new Rook(color, position);
	}
}
