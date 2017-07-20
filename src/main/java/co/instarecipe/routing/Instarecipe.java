package co.instarecipe.routing;
import java.sql.SQLException;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.instarecipe.db.DBContract;
import co.instarecipe.db.PostgresHelper;

public class Instarecipe {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		int portNumber = 1234;
		Router router = new Router();
		final String DOMAIN_EVENT = "domainevent";
		
		if (args.length > 0) {
			portNumber = Integer.parseInt(args[0]);
		}
		
		PostgresHelper.connect(DBContract.HOST, DBContract.DB_NAME, DBContract.USERNAME, DBContract.PASSWORD);

		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(portNumber);

		final SocketIOServer server = new SocketIOServer(config);
		
		server.addEventListener(DOMAIN_EVENT, String.class, new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
				Gson g = new Gson();

				System.out.println("Message received: " + data);

				String responseJSON = "ERROR";

				try {
					JsonElement jelement = new JsonParser().parse(data);
					JsonObject dataJsonObject = jelement.getAsJsonObject().getAsJsonObject("data");

					RouterPayload requestPayload = g.fromJson(data, RouterPayload.class);
					requestPayload.data = dataJsonObject;
					RouterPayload responsePayload = router.routePayload(requestPayload);

					JsonObject responseJsonObj = new JsonObject();
					responseJsonObj.addProperty("resource", responsePayload.resource);
					responseJsonObj.addProperty("action", responsePayload.action);
					responseJsonObj.add("data", responsePayload.data);

					responseJSON = g.toJson(responseJsonObj);
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("Message replied: " + responseJSON);
				client.sendEvent(DOMAIN_EVENT, responseJSON);

			}
		});

		server.start();

		// server.stop();

		System.out.println("Running...");

	}

}
