import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static java.awt.Color.black;
import static java.awt.SystemColor.text;


public class mainBot extends TelegramLongPollingBot {

    HashMap<String, String> cities = new HashMap();
    ArrayList<User> users = new ArrayList<>();
    long колвоучастниов = 0;
    long колвоЗапросовНаКонецКонкурса = 0;


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {


           String сообщение = update.getMessage().getText();
           String message = update.getMessage().getText();
           long chatId = update.getMessage().getChatId();

           if(update.getMessage().hasPhoto()) {
               List<PhotoSize> photos = update.getMessage().getPhoto();
               SendPhoto(chatId, photos.get(0).getFileId());

           }

           switch (сообщение) {

               case "plsplaywithme":
                   sendMessage("Хотите поиграть в города?", chatId);
                   switch (update.getMessage().getText()) {
                       case "Да":
                           sendMessage("Ок, называйте город", chatId);
                           addCity(chatId);
                       default:
                           sendMessage("Плак плак. Мне обидно. Я одинок. Никто не хочет со мной играть.", chatId);
                   }


                   break;



               case "plshelp":
                   sendHelp(chatId);
                   break;


               case "plsreg":
                   колвоучастниов =+ 1;
                   User reg = update.getMessage().getFrom();
                   users.add(reg);
                   break;


               case "plsunreg":
                   колвоучастниов =- 1;
                   User unreg = update.getMessage().getFrom();
                   users.remove(unreg);
                   break;


               case "plsresult":
                   колвоЗапросовНаКонецКонкурса =+ 1;
                   int winnerid = (int) (Math.random()* users.size());

                   if (users.size() == 0) {
                       sendMessage("Никто не хочет играть со мной, *плач робота*. Если так продолжится, то я слом... бип буп бип бип", chatId);
                   }

                   if (колвоЗапросовНаКонецКонкурса == колвоучастниов && колвоЗапросовНаКонецКонкурса > 1) {
                       sendMessage("Количество участников: " + users.size() + ". Победитель: пользователь №" + winnerid, chatId);
                       users.clear();
                       sendMessage("Вы учавствуете одни. Вам не одиноко?", chatId);
                   }
                   break;

               case "test":
                   try {


                       URL url = new URL("http://www.oddsportal.com/soccer/spain/primera-division/rayo-vallecano-valencia-d4LVsDnC/");
                       InputStream is = url.openStream();
                       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }




                   break;


               case "plsinfo":
                   sendInfo(chatId);
                   break;

               case "/start":
                   OnStart(message, chatId);
                   break;

               case "plskeyboard":
                   System.out.println("Клавиатура показана");
                   showKeyboard(message, chatId);
               break;


               case "plshidekeyboard":
                    hideKeyboard(message, chatId);
                    break;


               case "plstime":
                    System.out.println("Время показано");
                    ShowTime(message, chatId);
               break;


               case "plsmeme":
                    SendMeme(chatId);
               break;


               default:
                    sendMessage("Сорян, но я просто бот и я не понимаю язык кожаных мешков.", chatId);
               break;
            }


           Date date = new Date();
           System.out.println("Сообщение: " + update.getMessage().getText() + " Дата отправления: " + date);


       }



    }

    @Override
    public String getBotUsername() {
        return "kaikkiallaBot";
    }

    @Override
    public String getBotToken() {
        return "494735321:AAF28WNWFGeGpCOvUrAJRv-d7qx39Hs0jVI";
    }



    private void sendMessage(String text, long chatId){
        SendMessage sendMessage = new SendMessage()
                .setText(text)
                .setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void ShowTime(String text, long chatId) {
        Date date = new Date();
        String time = date.toString();



        SendMessage sendMessage = new SendMessage()
                .setText(time)
                .setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void showKeyboard(String text, long chatId) {
        // создаём разметку
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup(); // new ReplyKeyboardRemove();
        // создаём клавиатуру
        List<KeyboardRow> keyboard = new ArrayList<>();
        // создаём первую строчку клавиатуры
        KeyboardRow row = new KeyboardRow();
        // добавляем ячейки
        row.add("Ага");
        row.add("plshidekeyboard");
        row.add("plstime");
        // добавляем первую строчку в клавиатуру
        keyboard.add(row);
        // добавляем клавиатуру в разметку
        rkm.setKeyboard(keyboard);

        // создаём запрос серверу
        SendMessage sendMessage = new SendMessage()
                .setText("Keyboard's active") // устанавливаем текст сообщения
                .setChatId(chatId) // устанавливаем идентификатор чата
                .setReplyMarkup(rkm); // прикладываем размётку с клавиатурой

        try {
            // отправляем запрос
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void OnStart(String text, long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setText("Добро пожаловать, я бот. Я ничего не умею, т.к. меня сделал полный мудак.")
                .setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void hideKeyboard(String text, long chatId) {
        // создаём разметку, удаляющую клавиатуру
        ReplyKeyboardRemove rkm = new ReplyKeyboardRemove();

        // создаём запрос серверу
        SendMessage sendMessage = new SendMessage()
                .setText("Keyboard's passive. xdlulhehe.") // устанавливаем текст сообщения
                .setChatId(chatId) // устанавливаем идентификатор чата
                .setReplyMarkup(rkm); // прикладываем разметку, удаляющую клавиатуру

        try {
            // отправляем запрос
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



   private void SendMeme(long chatId) {
        String memeLibrary[] = {"https://cdn.pastemagazine.com/www/system/images/photo_albums/harry-potter/large/harry-potter-meme-86.jpg?1384968217", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0jOndsXWhII0SXyNSBqix6kvzHq7FSMaalJt5qybqUoZ0W2pwLg", "https://assets.vogue.com/photos/5891c91ece34fb453af7d263/master/pass/06-kendrick-llama-memes.jpg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIM8LLobd_2IS6QkQo8SQr7vwGLXM7m9m4TU-Ll5ZfkY7IQs8wmQ"};

        SendPhoto request = new SendPhoto();
        request.setChatId(chatId);
        int i = (int) (Math.random() * memeLibrary.length );
        request.setPhoto(memeLibrary[i]);
        System.out.println(memeLibrary.length);

        try {
            sendPhoto(request);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
   private  void SendPhoto(long chatId, String photo) {
       SendPhoto request = new SendPhoto();
       request.setChatId(chatId);
       request.setPhoto(photo);

       try {
           sendPhoto(request);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
   }



    private void sendInfo(long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setText("Привет. Я бот. Меня зовут Тигет. Мне 27 дней. Меня создал вот этот вот даун: vk.com/tiiget. Для подробной информции напишите ему,  правда он не очень часто чекает вк. Но можете написать. Я хз, зачем.")
                .setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    private void sendHelp(long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setText("Привет. " +
                        "Вот список команд: " +
                        "Какая-то хрень, кстати она не работает - plsreg, plsunreg, plsresult. " +
                        "plsinfo - информация. " +
                        "plstime - неточное время. " +
                        "plsmeme - отправить мемосик. " +
                        "Открыть/закрыть кое-что - plskeyboard/plshidekeyboard. " +
                        " " +
                        " " +
                        "Связь со мной: vk.com/tiiget. Шутка. Я же бот, у меня нет вк. А у моего хозяина, величайшего програмиста всех времен, он есть." +
                        "   Еще он в дискорде есть(если задонатите, то и я в дискорде появлюсь), " +
                        "   Но ладно. " +
                        "          Пока " +
                        "               Пиши мне " +
                        "                   Бзфф... биип... бзз, шшшш... ")
                .setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void addCity (String text, long chatId) {
        String[] кусочки = text.split(" ");
        cities.put(кусочки[0], кусочки[1]);
        sendMessage("Город добавлен", chatId);
    }

    private void getCities (long chatId) {
        String result = "Города: \n";
        sendMessage(cities.toString(),chatId);
        for (Map.Entry<String, String> строчка : cities.entrySet()) {
            result += строчка.getKey() + " - " + строчка.getValue();
        }

        sendMessage(result, chatId);
    }


}
