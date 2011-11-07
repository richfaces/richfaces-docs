import org.richfaces.model.TreeNodeImpl;

public class DataHolderTreeNodeImpl extends TreeNodeImpl {
    private Object data;

    public DataHolderTreeNodeImpl(boolean leaf, Object data) {
        super(leaf);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return super.toString() + " >> " + data;
    }
}