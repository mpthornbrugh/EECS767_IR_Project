import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.*;

public class Main extends Application{

	public static void main(String [] args) {
		launch(args);
	}

	Button button1;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("This is my title");
		button1 = new Button("This is a button.");
		StackPane layout = new StackPane();
		layout.getChildren().add(button1);
		Scene scene = new Scene(layout, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}