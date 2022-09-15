package osmosis.chessdemo.chess.board;

import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class BoardProvider implements Supplier<Board> {
	private final Board board;

	@Override
	public Board get() {
		return this.board;
	}
}
