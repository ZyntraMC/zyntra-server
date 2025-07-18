package mc.zyntra.general.utils.mojang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PremiumUtils {

    public static boolean isPremium(String username) {
        try {
            HttpURLConnection conn = getConnection("https://api.mojang.com/users/profiles/minecraft/" + username);
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = reader.readLine();
                if (line != null && !line.equals("null")) {
                    return true;
                }
            }
        } catch (Exception exception) {
        }
        return false;
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Premium-Checker");
        return connection;
    }
}
