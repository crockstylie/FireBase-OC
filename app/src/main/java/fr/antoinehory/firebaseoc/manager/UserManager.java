package fr.antoinehory.firebaseoc.manager;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import fr.antoinehory.firebaseoc.repository.UserRepository;

public class UserManager {
    private static volatile UserManager instance;
    private UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public Boolean isCurrentUserLogged() {
        return (UserRepository.getCurrentUser() != null);
    }

    public FirebaseUser getCurrentUser() {
        return UserRepository.getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return UserRepository.signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return UserRepository.deleteUser(context);
    }

}
