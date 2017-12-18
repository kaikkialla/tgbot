
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;



public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ApiContextInitializer.init();

        TelegramBotsApi api = new TelegramBotsApi();

        try {
            api.registerBot(new mainBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }



    }
}