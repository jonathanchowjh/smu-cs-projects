import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiLoader {

  public static int loadJokeAPI(String joke) {
    try {
      String jsonInputString = String.format("{\"joke\": \"%s\"}", joke);
      URL url = new URL("http://localhost:8080/api/model");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      OutputStream os = conn.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
      osw.write(jsonInputString);
      osw.flush();
      osw.close();
      os.close();

      int responseCode = conn.getResponseCode();
      conn.disconnect();
      if (responseCode != 201) throw new RuntimeException("Failed : HTTP error code : " + responseCode);
      return responseCode;
      // BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      // String output;
      // System.out.println("Output from Server .... \n");
      // while ((output = br.readLine()) != null) {
      //   System.out.println(output);
      // }
      
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }
  public static void main(String[] args) {
    String csvFile = "lemmatized.csv";
    BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ",";

    int h = 0;
    try {
      br = new BufferedReader(new FileReader(csvFile));
      br.readLine();
      while ((line = br.readLine()) != null && h < 150) {
        // use comma as separator
        line = line.toLowerCase();
        String[] joke = line.split(cvsSplitBy);
        String corpus = joke[0];
        corpus = corpus.replaceAll("[0-9]", "5");
        corpus = corpus.replace("["," ");
        corpus = corpus.replace("]"," ");
        corpus = corpus.replace("("," ");
        corpus = corpus.replace(")"," ");
        corpus = corpus.replace("*"," ");
        corpus = corpus.replace("\""," ");
        corpus = corpus.replace("\'"," ");
        corpus = corpus.replace("-"," ");
        corpus = corpus.replace("."," ");
        corpus = corpus.replace("?"," ");
        corpus = corpus.replace(","," ");
        corpus = corpus.replace("!"," ");
        corpus = corpus.replace(":"," ");
        System.out.print("code: " + loadJokeAPI(corpus));
        System.out.println(" | Joke=" + joke[0] + "]");
        h++;
      }
      System.out.println("\n===\nTrained with "+h+" data\n===\n");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}