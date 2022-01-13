package com.rs;

import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class CatService {
    public static void seeCat() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String theJson = response.body().string();
        //destructuring
        theJson = theJson.substring(1, theJson.length());
        theJson = theJson.substring(0, theJson.length()-1);

        Gson gson = new Gson();
        Cat cat = gson.fromJson(theJson, Cat.class);

        //resize image
        Image image = null;
        try {
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);
            ImageIcon imageIcon = new ImageIcon(image);
            if (imageIcon.getIconWidth()>400){
                Image image1 = imageIcon.getImage();
                Image imageUpdate = image1.getScaledInstance(300,300, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(imageUpdate);
            }
            String menu  = "Options: \n"+
                    " 1. See other image \n"+
                    " 2. Favorite \n"+
                    " 3. Return\n";
            String[] buttons = {"See other image", "Favorite", "Return"};
            String id_cat = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null, menu, id_cat, JOptionPane.INFORMATION_MESSAGE, imageIcon, buttons, buttons[0]);
            int optionSelected = -1;

            for (int i = 0; i < buttons.length; i++) {
                if(option.equals(buttons[i])){
                    optionSelected = i;
                }
            }
            switch (optionSelected){
                case 0:
                    seeCat();
                    break;
                case 1:
                    favoriteCat(cat);
                    break;
                default:
                    break;
            }


        }catch (IOException e){
            System.out.println(e);

        }

    }
    public static void favoriteCat(Cat cat){
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"image_id\": \""+cat.getId() +"\"\r\n  \r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .post(body)
                    .addHeader("x-api-key", cat.getApiKey())
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

        }catch (IOException e){
            System.out.println(e);
        }

    }
    public static void seeFavoriteCat(String apiKey){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("x-api-key", apiKey)
                    .build();

            Response response = client.newCall(request).execute();

            String theJson = response.body().string();
            //parser the Json
            Gson gson = new Gson();
            CatFav[] catFavs = gson.fromJson(theJson,CatFav[].class);

            if (catFavs.length>0){
                int min =1;
                int max = catFavs.length;
                int nuRandom = (int) (Math.random()*((max-min)+1))+min;
                int indice = nuRandom-1;
                CatFav catFav = catFavs[indice];

                //resize image
                Image image = null;
                try {
                    URL url = new URL(catFav.image.getUrl());
                    image = ImageIO.read(url);
                    ImageIcon imageIcon = new ImageIcon(image);
                    if (imageIcon.getIconWidth()>400){
                        Image image1 = imageIcon.getImage();
                        Image imageUpdate = image1.getScaledInstance(300,300, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(imageUpdate);
                    }
                    String menu  = "Options: \n"+
                            " 1. See other image \n"+
                            " 2. Delete favorite \n"+
                            " 3. Return\n";
                    String[] buttons = {"See other image", "Delete favorite", "Return"};
                    String id_cat = catFav.getId();
                    String option = (String) JOptionPane.showInputDialog(null, menu, id_cat, JOptionPane.INFORMATION_MESSAGE, imageIcon, buttons, buttons[0]);
                    int optionSelected = -1;

                    for (int i = 0; i < buttons.length; i++) {
                        if(option.equals(buttons[i])){
                            optionSelected = i;
                        }
                    }
                    switch (optionSelected){
                        case 0:
                            seeFavoriteCat(apiKey);
                            break;
                        case 1:
                            deleteFavoriteCat(catFav);
                            break;
                        default:
                            break;
                    }


                }catch (IOException e){
                    System.out.println(e);

                }

            }

        }catch (IOException e){
            System.out.println(e);
        }

    }
    public static void deleteFavoriteCat(CatFav catFav){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+catFav.getId())
                    .delete(null)
                    .addHeader("x-api-key", catFav.getApiKey())
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

        }catch (IOException e){
            System.out.println(e);
        }



    }

}
