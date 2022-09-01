module osmosis.chessdemo {
	requires javafx.controls;
	requires javafx.fxml;

	opens osmosis.chessdemo.controllers to javafx.fxml;
	exports osmosis.chessdemo;
}