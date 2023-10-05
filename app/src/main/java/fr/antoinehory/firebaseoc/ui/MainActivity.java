package fr.antoinehory.firebaseoc.ui;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import fr.antoinehory.firebaseoc.R;
import fr.antoinehory.firebaseoc.databinding.ActivityMainBinding;
import fr.antoinehory.firebaseoc.manager.UserManager;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final int RC_SIGN_IN = 123;
    private UserManager userManager = UserManager.getInstance();

    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
    }

    private void updateLoginButton() {
        if (userManager.isCurrentUserLogged()) {
            binding.loginButton.setText((String) getString(R.string.button_login_text_logged));
        } else {
            binding.loginButton.setText((String) getString(R.string.button_login_text_not_logged));
        }
    }

    private void setupListeners() {
        // LOGIN BUTTON
        binding.loginButton.setOnClickListener(view -> {
            if (userManager.isCurrentUserLogged()) {
                startProfileActivity();
            } else {
                startSignInActivity();
            }
        });

        // Chat Button
        binding.chatButton.setOnClickListener(view -> {
            if(userManager.isCurrentUserLogged()){
                startMentorChatActivity();
            }else{
                showSnackBar(getString(R.string.error_not_connected));
            }
        });
    }

    private void startMentorChatActivity() {
        Intent intent = new Intent(this, MentorChatActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startSignInActivity() {
        // CHOOSE AUTHENT PROVIDERS
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        // LAUNCH THE ACTIVITY
        startActivityForResult(
                AuthUI
                        .getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_logo_auth)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // METHOD THAT HANDLES RESPONSE AFTER SIGNIN ACTIVITY CLOSE
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                userManager.createUser();
                showSnackBar(getString(R.string.connection_succeed));
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }

                }
            }
        }
    }

    // SHOW SNACK BAR WITH MESSAGE
    private void showSnackBar(String message) {
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}