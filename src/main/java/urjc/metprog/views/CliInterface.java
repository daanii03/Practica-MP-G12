package urjc.metprog.views;

import java.util.List;
import urjc.metprog.controllers.Application;

public class CliInterface {

    private Application app;

    public CliInterface(Application app) {
        this.app = app;
    }

    public String showMainMenu() {
        return null;
    }

    public String showCreationMenu() {
        return null;
    }

    public String showChooseSkillMenu() {
        return null;
    }

    public List showMinionSelectionMenu() {
        return null;
    }

    public void showLoginMenu() {
        // Todo: Implement logic
    }

    public String showPlayerMenu() {
        return null;
    }
}
