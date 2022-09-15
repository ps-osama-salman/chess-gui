package osmosis.chessdemo.helpers;

import osmosis.chessdemo.chess.pieces.Piece;
import osmosis.chessdemo.chess.pieces.PieceColor;
import osmosis.chessdemo.chess.pieces.PieceFactory;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.List;
import java.util.function.BiFunction;

import static osmosis.chessdemo.helpers.ChessPositionHelper.createRandomPosition;
import static osmosis.chessdemo.helpers.PieceColorHelper.createRandomPieceColor;
import static osmosis.chessdemo.helpers.Random.getRandomNumberBetween;

public class PieceHelper {
	private static final PieceFactory pieceFactory = new PieceFactory(BoardProviderHelper.nullBoardProvider());
	private static final List<BiFunction<PieceColor, ChessPosition, Piece>> pieceCreators = List.of(pieceFactory::createBishop, pieceFactory::createKing, pieceFactory::createKnight, pieceFactory::createPawn, pieceFactory::createQueen, pieceFactory::createRook);

	public static Piece createRandomPiece() {
		return getRandomPieceCreator().apply(createRandomPieceColor(), createRandomPosition());
	}

	private static BiFunction<PieceColor, ChessPosition, Piece> getRandomPieceCreator() {
		return pieceCreators.get(getRandomNumberBetween(0, pieceCreators.size() - 1));
	}
}
