package com.eduardo.dialoghttp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

public class MyGdxGame extends ApplicationAdapter {

	private Dialog endDialog;
	private Skin skin;
	private Stage stage;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		endDialog = new Dialog("End Game", skin) {
			@Override
			protected void result(Object object) {
				System.out.println("Option: " + object);
				// Improved readability with String comparison
				makeHttpRequest();

			}
		};
		endDialog.button("Option 1", 1L);
		endDialog.button("Option 2", 2L);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				endDialog.show(stage);
			}
		}, 1);
	}

	private void makeHttpRequest() {
		Net.HttpResponseListener listener = new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				System.out.println("REBUT: " + httpResponse.getResultAsString());
			}

			@Override
			public void failed(Throwable t) {
				System.out.println("ERROR (failed) " + t.toString());
			}

			@Override
			public void cancelled() {
				System.out.println("CANCELLED");
			}
		};

		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest()
				.method(Net.HttpMethods.GET)
				.url("https://api.myip.com/")
				.build();
		Gdx.net.sendHttpRequest(httpRequest, listener);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
