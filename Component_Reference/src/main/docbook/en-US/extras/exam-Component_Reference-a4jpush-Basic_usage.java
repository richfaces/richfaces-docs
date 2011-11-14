TopicSession session;
TopicPublisher publisher;

public void sendCurrentDate() throws JMSException {
    String currentDate = new Date().toString();
    ObjectMessage message = session.createObjectMessage(message);
    publisher.publish(message);
}