package osmosis.chessdemo.chess.board;

import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import osmosis.chessdemo.chess.exceptions.InvalidFenException;
import osmosis.chessdemo.chess.fen.FenParser;

@AllArgsConstructor
public class BoardFactory {
	private static final String NEW_GAME_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private final FenParser fenParser;

	public Board create(String fen, GridPane boardGridPane) throws InvalidFenException {
		return new Board(boardGridPane, fenParser.parse(fen));
	}

	public Board create(GridPane boardGridPane) {
		try {
			return create(NEW_GAME_FEN, boardGridPane);
		} catch (InvalidFenException e) {
			throw new RuntimeException("Internal new-game FEN is invalid or is not being parsed correctly");
		}
	}
}
