package osmosis.chessdemo.helpers;

import osmosis.chessdemo.chess.board.BoardProvider;

public class BoardProviderHelper {
	public static BoardProvider nullBoardProvider() {
		return new BoardProvider(null);
	}
}
