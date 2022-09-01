package osmosis.chessdemo.chess.pieces;

import javafx.scene.image.Image;
import osmosis.chessdemo.chess.helper.ChessPosition;
import osmosis.chessdemo.functionailties.DraggableImageView;

import java.util.Objects;

public class Bishop extends Piece {
	private static final Image BLACK_SYMBOL;
	private static final Image WHITE_SYMBOL;

	static {
		BLACK_SYMBOL = new Image(Objects.requireNonNull(Rook.class.getResource("/osmosis/chessdemo/images/pieces/black_bishop.png")).toExternalForm());
		WHITE_SYMBOL = new Image(Objects.requireNonNull(Rook.class.getResource("/osmosis/chessdemo/images/pieces/white_bishop.png")).toExternalForm());
	}

	private DraggableImageView symbol;

	public Bishop(PieceColor color, ChessPosition position) {
		super(color, position);
		if (color == PieceColor.BLACK) {
			symbol = new DraggableImageView(BLACK_SYMBOL);
		} else {
			symbol = new DraggableImageView(WHITE_SYMBOL);
		}
	}

	public DraggableImageView getImageView() {
		return symbol = symbol.copy();
	}

	@Override
	public boolean validMovement(ChessPosition destinationPosition) {
		int fileDifference = Math.abs(destinationPosition.getFile() - position.getFile());
		int rankDifference = Math.abs(destinationPosition.getRank() - position.getRank());
		if (fileDifference == 0 || rankDifference == 0) {
			return false;
		}
		return rankDifference == fileDifference;
	}

	@Override
	public Piece copy() {
		return new Bishop(color, position);
	}
}
