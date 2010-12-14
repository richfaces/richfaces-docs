String[ ] components = {"< a4j:keepAlive >", "< a4j:actionParam >" };
String[ ][ ] attributes = {{"ajaxOnly", "beanName"},
                           {"name", "value", "assignTo"}};

data = new TreeNodeImpl<String>();

for (int i = 0; i < components.length; i++) {
   TreeNode<String> child = new TreeNodeImpl<String>();
   child.setData(components[i]);
   data.addChild(components[i], child);

   for (int j = 0; j < attributes[i].length; j++) {
      TreeNode<String> grandChild = new TreeNodeImpl<String>();
      grandChild.setData(attributes[i][j]);
      child.addChild(attributes[i][j], grandChild);
   }
}
