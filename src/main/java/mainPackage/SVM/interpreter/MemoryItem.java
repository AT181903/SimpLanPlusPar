package mainPackage.SVM.interpreter;

class MemoryItem {
    private Integer data;
    private boolean isUsed;

    public MemoryItem(int data, boolean isUsed) {
        this.data = data;
        this.isUsed = isUsed;
    }

    public MemoryItem() {
        data = null;
        isUsed = false;
    }

    public int getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
        this.isUsed = true;
    }

    public boolean isFree() {
        return !isUsed;
    }

    @Override
    public String toString() {
        return "| " + (data != null && data >= 0 ? " " : "") + data + "\t|";
    }
}
