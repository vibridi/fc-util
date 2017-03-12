package com.vibridi.fcutil;

import com.vibridi.fcutil.utils.AppContext;
import com.vibridi.fxmlutils.FXMLUtils;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
    public static void main( String[] args ) {
        launch();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLUtils.newView(this.getClass(), "view/mainview.fxml")
			.makeStage("Campionato Vaticano " + AppContext.versionNumber)
			.build()
			.show();
	}
}
