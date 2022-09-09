module osmosis.chessdemo {
	requires javafx.controls;
	requires javafx.fxml;
	requires lombok;

	opens osmosis.chessdemo.controllers to javafx.fxml;
	exports osmosis.chessdemo;
}