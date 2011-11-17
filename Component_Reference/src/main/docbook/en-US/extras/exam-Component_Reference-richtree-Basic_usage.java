private DataHolderTreeNodeImpl stationRoot;
private DataHolderTreeNodeImpl rootNodes;

public DataHolderTreeNodeImpl getRootNodes() {
    if (rootNodes == null) {
        String[] kickRadioFeed = {"Hall & Oates - Kiss On My List",
                "David Bowie - Let's Dance",
                "Lyn Collins - Think (About It)",
                "Kim Carnes - Bette Davis Eyes",
                "KC & the Sunshine Band - Give It Up"};
        stationRoot = new DataHolderTreeNodeImpl(false, "KickRadio");
        for (int i = 0; i<kickRadioFeed.length; i++) {
            DataHolderTreeNodeImpl child = new DataHolderTreeNodeImpl(true, kickRadioFeed[i]);
            stationRoot.addChild(i, child);
        }
        rootNodes = new DataHolderTreeNodeImpl();
        rootNodes.addChild(0, stationRoot);
    }
    return rootNodes;
}
