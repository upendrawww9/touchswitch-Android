package bizmessage.in.touchswitch.enums;

public enum Count {
    StartCount(0),
    EndCount(1),
    AddedCount(2);

    public int value;

    Count(int value) {
        this.value = value;
    }
}
