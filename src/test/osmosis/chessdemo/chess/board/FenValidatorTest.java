package osmosis.chessdemo.chess.board;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import osmosis.chessdemo.chess.exceptions.InvalidFenException;

import static org.junit.jupiter.api.Assertions.*;

class FenValidatorTest {
	private static final String VALID_FEN_1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String VALID_FEN_2 = "rnbqkbnr/ppppppp1/7p/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_RANDOM = "invalid";
	private static final String INVALID_FEN_NO_SLASHES = "rnbqkbnrpppppppp8888PPPPPPPPRNBQKBNR";
	private static final String INVALID_FEN_NO_CHARACTERS = "///////";
	private static final String INVALID_FEN_DOUBLE_SLASHES = "rnbqkbnr/pppppppp//8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_EMPTY_FIRST_FIELD = "/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_EMPTY_LAST_FIELD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/";
	private static final String INVALID_FEN_INVALID_CHARACTERS = "rnbqkbnr/pppppppp/s7/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_1 = "rnbqkbnr/ppppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_2 = "rnbqkbnr/pppppppp/7/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_3 = "rnbqkbnr/ppppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_4 = "rnbqkbnr/pppppppp/p7/7/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_5 = "rnbqkbnr/pppppppp/p5ppp/7/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_PIECE_PLACEMENT_1 = "rnbqkbnp/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private static final String INVALID_FEN_INVALID_PIECE_PLACEMENT_2 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNP";
	private static final String INVALID_FEN_INVALID_PIECE_PLACEMENT_3 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNp";
	private static final String INVALID_FEN_INVALID_PIECE_PLACEMENT_4 = "rnbqkbnP/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	public final FenValidator validator = new FenValidator();

	@ParameterizedTest
	@ValueSource(strings = {INVALID_FEN_RANDOM, INVALID_FEN_NO_SLASHES, INVALID_FEN_NO_CHARACTERS, INVALID_FEN_DOUBLE_SLASHES, INVALID_FEN_EMPTY_FIRST_FIELD, INVALID_FEN_EMPTY_LAST_FIELD, INVALID_FEN_INVALID_CHARACTERS})
	@NullAndEmptySource
	void givenInvalidFenFormatWhenValidateThenThrowException(String invalidFen) {
		InvalidFenException exception = assertThrows(InvalidFenException.class, () -> validator.validate(invalidFen));
		assertEquals("Invalid FEN format", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = {INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_1, INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_2, INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_3, INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_4, INVALID_FEN_INVALID_NUMBER_OF_PIECES_IN_RANK_5})
	void givenInvalidNumberOfPiecesInRowFenWhenValidateThenThrowException(String invalidFen) {
		InvalidFenException exception = assertThrows(InvalidFenException.class, () -> validator.validate(invalidFen));
		assertEquals("Invalid number of pieces in rank", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = {INVALID_FEN_INVALID_PIECE_PLACEMENT_1, INVALID_FEN_INVALID_PIECE_PLACEMENT_2, INVALID_FEN_INVALID_PIECE_PLACEMENT_3, INVALID_FEN_INVALID_PIECE_PLACEMENT_4})
	void givenInvalidPiecePlacementFenWhenValidateThenThrowException(String invalidFen) {
		InvalidFenException exception = assertThrows(InvalidFenException.class, () -> validator.validate(invalidFen));
		assertEquals("Invalid piece placement", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = {VALID_FEN_1, VALID_FEN_2})
	void givenValidFenWhenValidateThenValidationSucceeds(String validFen) {
		assertDoesNotThrow(() -> validator.validate(validFen));
	}
}
