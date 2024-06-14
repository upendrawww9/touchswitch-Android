package bizmessage.in.touchswitch.ui.adddevice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.FragmentSliderAddDeviceViewBinding;

public class SliderAddDeviceViewFragment extends Fragment {


    FragmentSliderAddDeviceViewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_slider_add_device_view, container, false);

        int position = getArguments().getInt("POSITION");
        if (position == 0){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg0));
        }else if (position == 1){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg));
        }else if (position == 2){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg3));
        }
        else if (position == 3){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg5));
        }
        else if (position == 4){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg6));
        }
        else if (position == 5){
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg7));
        }

        else {
            binding.imgDevice.setImageDrawable(getActivity().getDrawable(R.drawable.helpscanimg7));
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static SliderAddDeviceViewFragment getInstance(int position){
        SliderAddDeviceViewFragment deviceViewFragment = new SliderAddDeviceViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        deviceViewFragment.setArguments(bundle);
        return deviceViewFragment;
    }

}