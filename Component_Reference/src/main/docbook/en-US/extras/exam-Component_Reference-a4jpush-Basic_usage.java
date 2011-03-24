public String getListSubtopic() {
   return this.getUserName() + SUBTOPIC_SEPARATOR + channelName + "List";
}

@Override
protected void onUserList(String channel, User[] users) {
   try {
      getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
   } catch (MessageException e) {
      LOGGER.error(e.getMessage(), e);
   }
}

@Override
protected void onJoin(String channel, String sender, String login, String hostname) {
   try {
      getTopicsContext().publish(new TopicKey("chat", getListSubtopic()), null);
      Message messageObject = new Message("joined channel", sender, 
         DateFormat.getInstance().format(new Date()));
      getTopicsContext().publish(new TopicKey("chat", getMessagesSubtopic()), messageObject);
   } catch (MessageException e) {
      LOGGER.error(e.getMessage(), e);
   }
}
