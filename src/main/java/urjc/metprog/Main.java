package main.java.urjc.metprog;

import main.java.urjc.metprog.controllers.Application;
import main.java.urjc.metprog.views.CliInterface;

public class Main {
    public static void main(String[] args){
        // En el main existen por separado, y CliInterface recibe Application como parámetro
        Application app = new Application();
        CliInterface cli = new CliInterface(app);
        
        System.out.println("Hola mundo");
    }
}
