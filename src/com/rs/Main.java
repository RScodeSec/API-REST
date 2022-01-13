package com.rs;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        int option_menu = -1;
        String[] button = {"1. See cat's","2. See favorites" ,"3. Exit"};
        do {
            String option =(String) JOptionPane.showInputDialog(null, "Java Cat's","Menu", JOptionPane.INFORMATION_MESSAGE,
                    null, button, button[0]);
            for (int i = 0; i < button.length; i++) {
                if(option.equals(button[i])){
                    option_menu = i;
                }
            }

            switch (option_menu){
                case 0:
                    CatService.seeCat();
                    break;
                case 1:
                    Cat cat = new Cat();
                    CatService.seeFavoriteCat(cat.getApiKey());
                    break;
                default:
                    break;

            }

        }while (option_menu!=1);
    }
}
