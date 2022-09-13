package osmosis.chessdemo.chess.board;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import osmosis.chessdemo.chess.exceptions.*;
import osmosis.chessdemo.chess.fen.FenParser;
import osmosis.chessdemo.chess.helper.PieceDragListener;
import osmosis.chessdemo.chess.pieces.*;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.chess.position.File;
import osmosis.chessdemo.chess.position.Rank;
import osmosis.chessdemo.functionailties.DraggableImageView;

import java.util.Collection;
import java.util.Optional;

import static osmosis.chessdemo.chess.move.validator.MoveValidator.isEmptyPath;

public class Board {
	private static final String NEW_GAME_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private final GridPane boardGridPane;
	private final BoardSquares boardSquares;
	private PieceColor currentTurn;

	private Board(GridPane boardGridPane, BoardSquares boardSquares) {
		this.boardGridPane = boardGridPane;
		this.boardSquares = boardSquares;
		this.currentTurn = PieceColor.WHITE;
		refreshBoard();
	}

	public static Board createChessBoard(GridPane boardGridPane) {
		try {
			return createChessBoard(NEW_GAME_FEN, boardGridPane);
		} catch (InvalidFenException e) {
			throw new RuntimeException("Internal new-game FEN is invalid or is not being parsed correctly");
		}
	}

	public static Board createChessBoard(String fen, GridPane boardGridPane) throws InvalidFenException {
		Collection<Piece> pieces = FenParser.parse(fen);
		pieces.forEach(piece -> piece.setDragListener(new PieceDragListener(piece)));
		BoardSquares boardSquares = new BoardSquares();
		pieces.forEach(boardSquares::put);
		return new Board(boardGridPane, boardSquares);
	}

	private static void validatePawnMovement(Piece piece, File destinationFile, boolean occupyingPiecePresent) {
		if (!(piece instanceof Pawn)) {
			return;
		}
		boolean pawnAdvance = isPawnAdvance(piece.getPosition().getFile(), destinationFile);
		if (pawnAdvance && occupyingPiecePresent) {
			throw new InvalidMoveException("Pawn advancing destination is occupied");
		}
		if (!pawnAdvance && !occupyingPiecePresent) {
			throw new InvalidNumberException("Pawn is not taking any piece");
		}
	}

	private static boolean isPawnAdvance(File currentFile, File destinationFile) {
		return currentFile.absoluteDifference(destinationFile) == 0;
	}

	private static boolean isKnight(Piece piece) {
		return piece instanceof Knight;
	}

	private static void validatePieceMovement(Piece piece, ChessPosition destinationPosition) {
		if (!piece.isMovementValid(destinationPosition)) {
			throw new InvalidMoveException("Invalid piece movement");
		}
	}

	private static Pane createEmptyCell(double sideLength) {
		Pane emptyCell = new Pane();
		emptyCell.minHeightProperty().set(sideLength);
		emptyCell.minWidthProperty().set(sideLength);
		return emptyCell;
	}

	public void makeMove(Piece piece, ChessPosition destinationPosition) {
		try {
			validateMove(piece, destinationPosition);
			executeMove(piece, destinationPosition);
		} finally {
			refreshBoard();
		}
	}

	private void executeMove(Piece piece, ChessPosition destinationPosition) {
		boardSquares.removePieceOn(destinationPosition);
		boardSquares.removePieceOn(piece.getPosition());
		piece.setPosition(destinationPosition);
		boardSquares.put(piece);
		currentTurn = currentTurn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
		if (piece instanceof Pawn) {
			Rank destinationRank = destinationPosition.getRank();
			if (Rank.FIRST.equals(destinationRank) || Rank.EIGHTH.equals(destinationRank)) {
				boardSquares.removePieceOn(piece.getPosition());
				// TODO: Ask user for promotion piece
				Piece promotionPiece = ((Pawn) piece).promote(Pawn.PromotionPiece.Queen, destinationPosition.getFile());
				boardSquares.put(promotionPiece);
			}
		}
	}

	public void validateMove(Piece piece, ChessPosition destinationPosition) {
		validateTurn(piece);
		validatePieceMovement(piece, destinationPosition);
		Optional<Piece> occupyingPiece = boardSquares.get(destinationPosition);
		validatePawnMovement(piece, destinationPosition.getFile(), occupyingPiece.isPresent());
		occupyingPiece.ifPresent(this::validateTakingOwnPiece);
		validatePathEmpty(piece, destinationPosition);
		validateKingCheck(piece, destinationPosition);
	}

	private void validateTurn(Piece piece) {
		if (!currentTurn.equals(piece.getColor())) {
			throw new WrongTurnException(currentTurn);
		}
	}

	private void validatePathEmpty(Piece piece, ChessPosition destinationPosition) {
		if (!isKnight(piece) && !isEmptyPath(piece.getPosition(), destinationPosition, boardSquares)) {
			throw new InvalidMoveException("Path is not empty");
		}
	}

	private void validateTakingOwnPiece(Piece occupyingPiece) {
		if (currentTurn.equals(occupyingPiece.getColor())) {
			throw new InvalidMoveException("Player is taking their own piece");
		}
	}

	private void validateKingCheck(Piece piece, ChessPosition destinationPosition) {
		if (kingInCheck(piece, destinationPosition)) {
			throw new KingInCheckException();
		}
	}

	private void refreshBoard() {
		boardGridPane.getChildren().clear();
		double sideLength = boardGridPane.getPrefHeight() / 8;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Pane emptyCell = createEmptyCell(sideLength);
				boardGridPane.add(emptyCell, x, y);
			}
		}
		for (Piece piece : boardSquares.getAll()) {
			DraggableImageView pieceImageView = piece.getImageView();
			pieceImageView.setFitWidth(sideLength);
			pieceImageView.setFitHeight(sideLength);
			ChessPosition position = piece.getPosition();
			boardGridPane.add(pieceImageView, (position.getFile().getFileNumber() - 1), (position.getRank().getInverse().getRankNumber() - 1));
		}
	}

	public boolean kingInCheck(Piece piece, ChessPosition destinationPosition) {
		BoardSquares mockPieces = boardSquares.copy();
		mockPieces.removePieceOn(destinationPosition);
		mockPieces.removePieceOn(piece.getPosition());
		Piece copyPiece = piece.copy();
		copyPiece.setPosition(destinationPosition);
		mockPieces.put(copyPiece);
		Piece king = mockPieces.getAll().stream().filter(this::isCurrentKing).findAny().orElseThrow(() -> new IllegalStateException("No king found"));
		for (Piece mockPiece : mockPieces.getAll()) {
			if (currentTurn.equals(mockPiece.getColor())) {
				continue;
			}
			if (mockPiece instanceof King || mockPiece instanceof Knight) {
				if (mockPiece.isMovementValid(king.getPosition())) {
					if (mockPiece instanceof Knight) {
						System.out.println("King in check by a Knight");
					} else {
						System.out.println("King restricted by the other king");
					}
					return true;
				}
			} else if (mockPiece instanceof Pawn) {
				int takeFileRight = mockPiece.getPosition().getFile().getFileNumber() + 1;
				int takeFileLeft = mockPiece.getPosition().getFile().getFileNumber() - 1;
				int takeRank;
				int kingFile = king.getPosition().getFile().getFileNumber();
				if (PieceColor.WHITE.equals(mockPiece.getColor())) {
					takeRank = mockPiece.getPosition().getRank().getRankNumber() + 1;
				} else {
					takeRank = mockPiece.getPosition().getRank().getRankNumber() - 1;
				}
				if (king.getPosition().getRank().getRankNumber() == takeRank) {
					if (kingFile == takeFileRight || kingFile == takeFileLeft) {
						System.out.println("King in check by a Pawn");
						return true;
					}
				}
			} else {
				if (mockPiece.isMovementValid(king.getPosition()) && isEmptyPath(mockPiece.getPosition(), king.getPosition(), mockPieces)) {
					System.out.println("King in check by a " + mockPiece.getClass().getSimpleName());
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCurrentKing(Piece mockPiece) {
		return currentTurn.equals(mockPiece.getColor()) && mockPiece instanceof King;
	}
}
