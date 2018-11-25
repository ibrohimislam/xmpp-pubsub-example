import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.pubsub.*;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class Main {
    private static String XMPP_SERVER = "xmpp.ehealth.co.id";
    private static String XMPP_HOST = "ehealth.co.id";
    private static String XMPP_USERNAME = "admin";
    private static String XMPP_PASSWORD = "your_password_here";

    public static void main(String[] args) {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(XMPP_SERVER)
                    .setXmppDomain(XMPP_HOST)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();

            AbstractXMPPConnection connection = new XMPPTCPConnection(config);
            connection.connect();
            connection.login(XMPP_USERNAME, XMPP_PASSWORD);

            PubSubManager manager = PubSubManager.getInstance(connection);
            LeafNode node = manager.getOrCreateLeafNode("testNode");

            ConfigureForm form = new ConfigureForm(DataForm.Type.submit);
            form.setPersistentItems(false);
            form.setDeliverPayloads(true);
            form.setAccessModel(AccessModel.open);
            form.setPublishModel(PublishModel.open);
            form.setSubscribe(true);

            node.sendConfigurationForm(form);

            SimplePayload payload = new SimplePayload(
                    "<ex:example xmlns:ex=\"http://example.com/example/\">Hello World!</ex:example>");
            node.publish(new PayloadItem<>(payload));

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
