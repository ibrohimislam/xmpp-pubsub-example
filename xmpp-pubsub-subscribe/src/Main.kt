import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.pubsub.LeafNode
import org.jivesoftware.smackx.pubsub.PayloadItem
import org.jivesoftware.smackx.pubsub.PubSubManager
import org.jivesoftware.smackx.pubsub.SimplePayload

var connection: XMPPTCPConnection? = null;

private val XMPP_SERVER = "xmpp.ehealth.co.id"
private val XMPP_HOST = "ehealth.co.id"
private val XMPP_USERNAME = "admin"
private val XMPP_PASSWORD = "your_password_here"

fun main() {

    Runtime.getRuntime().addShutdownHook(Thread {
        System.out.println("disconnecting...")
        connection!!.disconnect()
        System.out.println("disconnected.")
    })


    try {
        val config = XMPPTCPConnectionConfiguration.builder()
                .setHost(XMPP_SERVER)
                .setXmppDomain(XMPP_HOST)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build()

        val connection = XMPPTCPConnection(config)
        connection.connect()
        connection.login(XMPP_USERNAME, XMPP_PASSWORD)

        val manager = PubSubManager.getInstance(connection)
        val node = manager.getNode<LeafNode>("testNode")
        node.addItemEventListener {
            val items = it.items as List<PayloadItem<SimplePayload>>
            items.forEach {item ->
                System.out.println(item.payload.toString())
            }
        }
        node.subscribe(connection!!.user.asEntityBareJidString())

    } catch (e: Exception) {
        e.printStackTrace()
    }

    while (true);
}