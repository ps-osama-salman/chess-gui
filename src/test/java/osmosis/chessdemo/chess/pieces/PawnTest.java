package osmosis.chessdemo.chess.pieces;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import osmosis.chessdemo.chess.exceptions.InvalidPromotionMoveException;
import osmosis.chessdemo.chess.helper.PieceDragListener;
import osmosis.chessdemo.chess.pieces.Pawn.PromotionPiece;
import osmosis.chessdemo.chess.pieces.symbol.PieceSymbolProvider;
import osmosis.chessdemo.chess.position.ChessPosition;
import osmosis.chessdemo.chess.position.File;
import osmosis.chessdemo.chess.position.Rank;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
	private MockedStatic<PieceSymbolProvider> pieceSymbolProviderMockedStatic;

	public static Stream<Arguments> invalidMovements() {
		return Stream.of(Arguments.of(new ChessPosition(File.E, Rank.FIRST)), Arguments.of(new ChessPosition(File.E, Rank.SECOND)), Arguments.of(new ChessPosition(File.E, Rank.FIFTH)), Arguments.of(new ChessPosition(File.E, Rank.SEVENTH)), Arguments.of(new ChessPosition(File.E, Rank.EIGHTH)), Arguments.of(new ChessPosition(File.A, Rank.THIRD)), Arguments.of(new ChessPosition(File.B, Rank.THIRD)), Arguments.of(new ChessPosition(File.C, Rank.THIRD)), Arguments.of(new ChessPosition(File.G, Rank.THIRD)), Arguments.of(new ChessPosition(File.H, Rank.THIRD)), Arguments.of(new ChessPosition(File.A, Rank.FOURTH)), Arguments.of(new ChessPosition(File.B, Rank.FOURTH)), Arguments.of(new ChessPosition(File.C, Rank.FOURTH)), Arguments.of(new ChessPosition(File.G, Rank.FOURTH)), Arguments.of(new ChessPosition(File.H, Rank.FOURTH)));
	}

	public static Stream<Arguments> validMovements() {
		return Stream.of(Arguments.of(new ChessPosition(File.E, Rank.THIRD)), Arguments.of(new ChessPosition(File.E, Rank.FOURTH)), Arguments.of(new ChessPosition(File.D, Rank.THIRD)), Arguments.of(new ChessPosition(File.F, Rank.THIRD)));
	}

	public static Stream<Arguments> promotionParameters() {
		return Stream.of(File.A, File.B, File.C, File.D, File.E, File.F, File.G, File.H).flatMap(file -> Stream.of(Arguments.of(Queen.class, file), Arguments.of(Rook.class, file), Arguments.of(Knight.class, file), Arguments.of(Bishop.class, file)));
	}

	@BeforeEach
	void setUp() {
		pieceSymbolProviderMockedStatic = Mockito.mockStatic(PieceSymbolProvider.class);
	}

	@AfterEach
	void tearDown() {
		pieceSymbolProviderMockedStatic.close();
	}

	@ParameterizedTest
	@MethodSource("invalidMovements")
	void givenInvalidChessMovementWhenIsMovementValidThenReturnFalse(ChessPosition destinationPosition) {
		Pawn pawn = new Pawn(PieceColor.WHITE, new ChessPosition(File.E, Rank.SECOND));
		assertFalse(pawn.isMovementValid(destinationPosition));
	}

	@ParameterizedTest
	@MethodSource("validMovements")
	void givenValidChessMovementWhenIsMovementValidThenReturnFalse(ChessPosition destinationPosition) {
		Pawn pawn = new Pawn(PieceColor.WHITE, new ChessPosition(File.E, Rank.SECOND));
		assertTrue(pawn.isMovementValid(destinationPosition));
	}

	@Test
	void givenInvalidFileWhenPromoteThenThrowException() {
		Pawn pawn = new Pawn(PieceColor.WHITE, new ChessPosition(File.A, Rank.SEVENTH));
		pawn.setDragListener(new PieceDragListener(null, pawn));
		assertThrows(InvalidPromotionMoveException.class, () -> pawn.promote(PromotionPiece.Queen, File.C));
	}

	@Test
	void givenInvalidCurrentRankWhenPromoteThenThrowException() {
		Pawn pawn = new Pawn(PieceColor.WHITE, new ChessPosition(File.A, Rank.SIXTH));
		pawn.setDragListener(new PieceDragListener(null, pawn));
		assertThrows(InvalidPromotionMoveException.class, () -> pawn.promote(PromotionPiece.Queen, File.A));
	}

	@ParameterizedTest
	@MethodSource("promotionParameters")
	void givenPromotionParametersForWhitePawnWhenPromoteThenReturnNewPieceWithCorrectPosition(Class<? extends Piece> promotionPieceClass, File file) {
		Pawn pawn = new Pawn(PieceColor.WHITE, new ChessPosition(file, Rank.SEVENTH));
		pawn.setDragListener(new PieceDragListener(null, pawn));
		Piece promotionPiece = pawn.promote(PromotionPiece.valueOf(promotionPieceClass.getSimpleName()), file);
		assertEquals(promotionPieceClass, promotionPiece.getClass());
		assertEquals(PieceColor.WHITE, promotionPiece.getColor());
		assertEquals(new ChessPosition(file, Rank.EIGHTH), promotionPiece.getPosition());
		assertEquals(pawn.getDragListener(), promotionPiece.getDragListener());
	}

	@ParameterizedTest
	@MethodSource("promotionParameters")
	void givenPromotionParametersForBlackPawnWhenPromoteThenReturnNewPieceWithCorrectPosition(Class<? extends Piece> promotionPieceClass, File file) {
		Pawn pawn = new Pawn(PieceColor.BLACK, new ChessPosition(file, Rank.SECOND));
		pawn.setDragListener(new PieceDragListener(null, pawn));
		Piece promotionPiece = pawn.promote(PromotionPiece.valueOf(promotionPieceClass.getSimpleName()), file);
		assertEquals(promotionPieceClass, promotionPiece.getClass());
		assertEquals(PieceColor.BLACK, promotionPiece.getColor());
		assertEquals(new ChessPosition(file, Rank.FIRST), promotionPiece.getPosition());
		assertEquals(pawn.getDragListener(), promotionPiece.getDragListener());
	}
}