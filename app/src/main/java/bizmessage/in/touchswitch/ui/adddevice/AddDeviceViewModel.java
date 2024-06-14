package bizmessage.in.touchswitch.ui.adddevice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDeviceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddDeviceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}