TopicConnection connection;
TopicSession session;
TopicPublisher publisher;

public void sendCurrentDate() throws JMSException {
    String currentDate = new Date().toString();
    ObjectMessage message = session.createObjectMessage(message);
    publisher.publish(message);
}

// messaging needs to be initialized before using method #sendCurrentDate()
private void initializeMessaging() throws JMSException, NamingException {
    if (connection == null) {
        TopicConnectionFactory tcf = (TopicConnectionFactory) InitialContext.doLookup("java:/ConnectionFactory");
        connection = tcf.createTopicConnection();
    }
    if (session == null) {
        session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    if (topic == null) {
        topic = InitialContext.doLookup("topic/datePush");
    }
    if (publisher == null) {
        publisher = session.createPublisher(topic);
    }
}