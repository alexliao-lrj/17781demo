package com.example.demo.viewmodel;

import androidx.lifecycle.ViewModel;

public class FirestoreActivityViewModel extends ViewModel {
    private boolean mIsSigningIn;

    public FirestoreActivityViewModel(){
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
