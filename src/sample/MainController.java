package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainController {
    final FileChooser fileChooser = new FileChooser();
    File in;
    File out;


    @FXML
    RadioButton rb1;
    @FXML
    TextField inputPath;
    @FXML
    TextField outputPath;
    @FXML
    PasswordField secret;



    public void code() {
        try {
            if(!performCheck()) return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = "";
        try {
            Scanner inn = new Scanner(in);
            String content = inn.useDelimiter("\\Z").next();
            inn.close();
            int mode = rb1.isSelected() ? 0 : 1;
            text = process(mode, content);
            PrintWriter writer = new PrintWriter(outputPath.getText());
            writer.println(text);
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Powodzenie");
            String smode = mode == 1 ? "odszyfrowany" : "zaszyfrowany";
            alert.setHeaderText("Plik został pomyślnie " + smode);
            alert.show();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void chooseInputFile() {
        in = getFile();
        if (in != null) {
            inputPath.setText(in.getPath());
        }
    }


    public void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Wybierz plik");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    public File getFile() {
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(new Stage());
        return file;
    }

    public boolean performCheck() throws IOException {
        out = new File(outputPath.getText());

        if (in == null || !out.createNewFile()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Nie podano pliku!");
            alert.setContentText("Wybierz plik wejścia i plik wyjścia.");
            alert.show();
            return false;
        } else if (secret.getText().length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Klucz jest polem wymaganym");
            alert.setContentText("Klucz musi się składać z conajmniej 8 znaków");
            alert.show();
            return false;
        }
        return true;
    }

    public String process(int mode, String text) {
        String key = secret.getText();
        int tl = text.length();
        int kl = key.length();
        int fill = kl - tl % kl;
        String block = "";
        List<String> result = new ArrayList<>();
        String rr = "";

        if (mode == 0) {
            for(int i = 0; i < fill; i++) {
                text += " ";
            }
            tl = text.length();
            for (int i = 0, j = 0; i < tl; i++) {
                if (j >= kl) {
                    j = 0;
                    result.add(block);
                    block = "";
                }
                char cText = text.charAt(i);
                char cKey = key.charAt(j);
                int caesar = cKey - 32;
                int sum = cText - 32 + caesar;
                char cc = (char) (sum % 95 + 32);
                block += cc;
                j++;
            }
            result.add(block);
            for(String str: result){
                for(int i = kl-1; i >= 0; i--){
                    rr += str.charAt(i);
                }
            }
        } else if (mode == 1) {
            for (int i = 0, j = 0; i < tl; i++) {
                if (j >= kl) {
                    j = 0;
                    result.add(block);
                    block = "";
                }
                block += text.charAt(i);
                j++;
            }
            result.add(block);
            List<String> revResult = new ArrayList<>();
            for(String str: result){
                String newBlock = "";
                for(int i = kl-1; i >= 0; i--){
                    newBlock += str.charAt(i);
                }
                revResult.add(newBlock);
            }
            for(String bl: revResult){
                for(int i = 0; i < kl; i++) {
                    int caesar = key.charAt(i) - 32;
                    int sum = bl.charAt(i) - 32 + (95 - caesar);
                    char cc = (char) (sum % 95 + 32);
                    rr += cc;
                }
            }
        }
        secret.setText("");
        return rr;
    }
}



//OLD
//
//    public void encodeText() {
//        if(!performCheck()) return;
//        String cipher = "";
//        try {
//            Scanner inn = new Scanner(in);
//            String content = inn.useDelimiter("\\Z").next();
//            inn.close();
//            cipher = process(0, content);
//            PrintWriter writer = new PrintWriter(outputPath.getText());
//            writer.println(cipher);
//            writer.close();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText("File encoded");
//            alert.show();
//        } catch(Exception e){
//            System.out.println(e);
//        }
//
//    }
//
//    public void decodeText() {
//        if(!performCheck()) return;
//        String text = "";
//        try {
//            Scanner inn = new Scanner(in);
//            String content = inn.useDelimiter("\\Z").next();
//            inn.close();
//            text = process(1, content);
//            PrintWriter writer = new PrintWriter(outputPath.getText());
//            writer.println(text);
//            writer.close();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText("File decoded");
//            alert.show();
//        } catch(Exception e){
//            System.out.println(e);
//        }
//    }