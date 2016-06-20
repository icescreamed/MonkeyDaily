package com.taiihc.monkey_daily.view;

import android.content.Context;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public Context getContext() {
        return context;
    }
}
