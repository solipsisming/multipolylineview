package solipsisming.multipolylineview;


public class Type {

    private float[] data;
    private String typeName;
    float max;
    String maxStr;
    float min;
    String minStr;

    public Type() {
    }

    public Type(float[] data) {
        this.data = data;
    }

    public Type(float[] data, String typeName) {
        this.data = data;
        this.typeName = typeName;
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
