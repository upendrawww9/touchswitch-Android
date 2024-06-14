package bizmessage.in.touchswitch.utils;


public interface DialogButtonClickListener {

    void onPositiveButtonClicked(int dialogIdentifier, Object data);

    void onNegativeButtonClicked(int dialogIdentifier);

}
