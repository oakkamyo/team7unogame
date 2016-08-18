package iss.nus.edu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/table/{gid}")
public class UNOEndPoint {

    @OnOpen
    public void onOpen(Session session, @PathParam("gid") String gid) {
        System.out.println(">>> new connection: " + session.getId());
        Map<String, Object> sessObject = session.getUserProperties();
        sessObject.put("gid", gid);
        System.out.println("\tgid: " + gid);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println(">> close connection: " + session.getId());
        System.out.println("\t close reason: " + reason.getReasonPhrase());
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        System.out.println(">>> no of connection: "
                + session.getOpenSessions().size());
        //String name = (String) session.getUserProperties().get("name");
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        String topic = data.getString("gid");

        System.out.println("\t incoming GID: " + topic);

//        String newMessage = Json.createObjectBuilder()
//                .add("pname", data.getString("pname"))
//                .build()
//                .toString();

        //System.out.println("\toutgoing: " + newMessage+"session:"+session.toString());

        session.getOpenSessions().stream()
                .filter(s -> {
                    System.out.println(">>>filter" + msg);
                    return (topic.equals(
                            (String) s.getUserProperties().get("gid")));
                }).forEach(s -> {
                    try {
                        System.out.println(">>>try" + msg);
                        s.getBasicRemote().sendText(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(">>>catch" + msg);
                    }
        });
    }
}
