package com.example.demo.viewmodel;

import androidx.lifecycle.ViewModel;

public class SportActivityViewModel extends ViewModel {
    private boolean mIsSigningIn;

    public SportActivityViewModel(){
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
