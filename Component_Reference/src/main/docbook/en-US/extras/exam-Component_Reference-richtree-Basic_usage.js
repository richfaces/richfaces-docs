private TreeNodeImpl<String> stationRoot = new TreeNodeImpl<String>();
private TreeNodeImpl<String> stationNodes = new TreeNodeImpl<String>(); 
private String[] kickRadioFeed = { "Hall & Oates - Kiss On My List",
                                   "David Bowie - Let's Dance",
                                   "Lyn Collins - Think (About It)",
                                   "Kim Carnes - Bette Davis Eyes",
                                   "KC & the Sunshine Band - Give It Up" };

stationRoot.setData("KickRadio");
stationNodes.addChild(0, stationRoot);
for (int i = 0; i < kickRadioFeed.length; i++){
   TreeNodeImpl<String> child = new TreeNodeImpl<String>();
   child.setData(kickRadioFeed[i]);
   stationRoot.addChild(i, child);
}
