package osmosis.chessdemo.chess.pieces;


import osmosis.chessdemo.chess.helper.PieceDragListener;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.functionailties.DraggableImageView;

public abstract class Piece {
	protected final PieceColor color;
	private final DraggableImageView symbol;
	protected ChessPosition position;

	protected Piece(PieceColor color, ChessPosition position, DraggableImageView symbol) {
		this.color = color;
		this.position = position;
		this.symbol = symbol;
		this.symbol.setDragListener(new PieceDragListener(this));
	}

	public PieceColor getColor() {
		return color;
	}

	public ChessPosition getPosition() {
		return position;
	}

	public void setPosition(ChessPosition chessPosition) {
		this.position = chessPosition;
	}

	public abstract boolean isMovementValid(ChessPosition destinationPosition);

	public final DraggableImageView getImageView() {
		return this.symbol.copy();
	}

	public PieceDragListener getDragListener() {
		return getImageView().getDragListener();
	}

	public void setDragListener(PieceDragListener dragListener) {
		this.symbol.setDragListener(dragListener);
	}

	public abstract Piece copy();

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + "color=" + (color == PieceColor.BLACK ? "Black" : "White") + ", position=" + position + '}';
	}
}
