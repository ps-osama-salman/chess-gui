package osmosis.chessdemo.chess.fen;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import osmosis.chessdemo.chess.board.BoardSquares;
import osmosis.chessdemo.chess.exceptions.InvalidFenException;
import osmosis.chessdemo.chess.pieces.Piece;
import osmosis.chessdemo.chess.pieces.PieceFactory;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static osmosis.chessdemo.chess.position.File.getFile;
import static osmosis.chessdemo.chess.position.Rank.getRank;

@AllArgsConstructor
public class FenParser {
	private final PieceFactory pieceFactory;

	private static String[] getRanksStrings(String fen) {
		return fen.split("/");
	}

	private static Pair<Integer, String> createRankPair(String[] ranksStrings, int rankNumber) {
		return new Pair<>(rankNumber, ranksStrings[8 - rankNumber]);
	}

	public BoardSquares parse(String fen) throws InvalidFenException {
		FenValidator.validate(fen);
		return new BoardSquares(parsePieces(getRanksStrings(fen)));
	}

	private Collection<Piece> parsePieces(String rankString, int rankNumber) {
		Collection<Piece> rankPieces = new HashSet<>();
		Iterator<Character> iterator = rankString.chars().mapToObj(c -> (char) c).iterator();
		for (int fileNumber = 1; fileNumber <= 8; fileNumber++) {
			char character = iterator.next();
			if (Character.isDigit(character)) {
				fileNumber += Character.getNumericValue(character);
				continue;
			}
			ChessPosition position = new ChessPosition(getFile(fileNumber), getRank(rankNumber));
			rankPieces.add(getPiece(character, position));
		}
		return rankPieces;
	}

	private Collection<Piece> parsePieces(String[] ranksStrings) {
		return IntStream.rangeClosed(1, 8)
			.mapToObj(rankNumber -> createRankPair(ranksStrings, rankNumber))
			.map(pair -> parsePieces(pair.getValue(), pair.getKey()))
			.collect((Supplier<Collection<Piece>>) HashSet::new, Collection::addAll, Collection::addAll);
	}

	private Piece getPiece(char c, ChessPosition position) {
		return pieceFactory.createPiece(c, position);
	}
}
