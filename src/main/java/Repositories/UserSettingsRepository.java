package Repositories;

import Objects.UserSettings;

public class UserSettingsRepository {
    private UserSettings userSettings;

    public UserSettingsRepository(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }
}
