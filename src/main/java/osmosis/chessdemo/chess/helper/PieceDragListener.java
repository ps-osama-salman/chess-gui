package osmosis.chessdemo.chess.helper;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import osmosis.chessdemo.chess.board.Board;
import osmosis.chessdemo.chess.pieces.Piece;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.functionailties.DragListener;
import osmosis.chessdemo.functionailties.MouseEventHandler;

import static osmosis.chessdemo.chess.position.File.getFile;
import static osmosis.chessdemo.chess.position.Rank.getRank;

public class PieceDragListener implements DragListener {
	private final Board board;
	private Piece piece;

	public PieceDragListener(Board board, Piece piece) {
		this.board = board;
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	@Override
	public void accept(Node node, MouseEventHandler.DragEvent dragEvent) {
		Bounds bounds = node.localToScene(node.getBoundsInLocal());
		Point2D center = new Point2D((bounds.getMinX() + bounds.getMaxX()) / 2, (bounds.getMinY() + bounds.getMaxY()) / 2);
		double sideLength = node.getBoundsInLocal().getHeight();
		int x = (int) (center.getX() / sideLength);
		int y = (int) (center.getY() / sideLength);
		ChessPosition destinationPosition = new ChessPosition(getFile(x).nextFile(), getRank(y + 1).getInverse());
		if (dragEvent.equals(MouseEventHandler.DragEvent.DragEnd)) {
			board.makeMove(getPiece(), destinationPosition);
		}
	}
}
