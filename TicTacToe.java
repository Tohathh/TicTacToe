package XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;

public class TicTacToe {
    private static final char SIGN_X = 'x'; // переменная
    private static final char SIGN_O = 'o'; // переменная
    private static final char SIGN_EMPTY = '.'; // переменная
    String firstPlayer; // первый игрок
    String secondPlayer; // второй игрок
    char[][] table; //двумерный символьный массив, игровое поле
    int victory1;
    int defeat1;
    int victory2;
    int defeat2;
    Player player1 = new Player("",1, 'X');
    Player player2 = new Player("",2, '0');

    public static void main(String[] args) throws IOException {
        new TicTacToe().game();

    }

    public TicTacToe() { // конструктор
        Scanner scanner = new Scanner(System.in);
        int victory1 = 0;
        int defeat1 = 0;
        int victory2 = 0;
        int defeat2 = 0;
        table = new char[3][3];
    }

    public void game() throws IOException { // игровая логика
        Scanner scanner = new Scanner(System.in);
        int x = 1;
        initTable(); // инициализация таблицы
        while (true) {
            turnHumanOne(); // ход первого игрока
            if (checkWin(SIGN_X)) { // проверка: если победа или ничья:
                System.out.println(player1.getName() + " выиграл!"); // сообщить и выйти из цикла
                victory1 ++;
                defeat2 ++;
                break;
            }
            if (isTableFull()) {
                System.out.println("Ничья!");
                break;
            }
            printTable(); // вывод таблицы
            turnHumanTwo(); // ход второго игрока
            printTable(); // вывод таблицы
            if (checkWin(SIGN_O)) { // проверка: если победа или ничья
                System.out.println(player2.getName() + " выиграл!");  //    сообщить и выйти из цикла
                victory2 ++;
                defeat1 ++;
                break;
            }
            if (isTableFull()) {
                System.out.println("Ничья!");
                break;
            }
        }
        printTable(); // вывод таблицы
        System.out.print("Желаете сыграть снова? (введите 0 = ДА, 1 = НЕТ): ");
        x = scanner.nextInt();
        if (x == 0) {
            game();
        } else if (x == 1) {
            System.out.println("Конец игры.");
            try(FileWriter writer = new FileWriter("notes.txt", false)){
                String text = player1.getName() + ":" + " Победы " + victory1 + ";" + " Поражения " + defeat1 + ";";
                String text1 = player2.getName() + ":" + " Победы " + victory2 + ";" + " Поражения " + defeat2 + ";";
                writer.write(text);
                writer.append('\n');
                writer.write(text1);
            }
        }
        writer(player1, player2);
    }

    private void initTable() { //метод обеспечивает начальную инициализацию игровой таблицы, заполняя её ячейки «пустыми» символами
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                table[row][col] = SIGN_EMPTY;
    }

    public void printTable() { // метод, отображающий текущее состояние игровой таблицы
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++)
                System.out.print(table[row][col] + " ");
            System.out.println();
        }
    }

    private void turnHumanOne() { // метод, который позволяет пользователю сделать ход
        Scanner scanner = new Scanner(System.in);
        int x, y;
        do {
            System.out.print(player1.getName() + ", введите координаты X от 1 до 3: ");
            x = scanner.nextInt() - 1;
            System.out.print(player1.getName() + ", введите координаты Y от 1 до 3: ");
            y = scanner.nextInt() - 1;
            if (table[y][x] == SIGN_X || table[y][x] == SIGN_O) {
                System.out.println("Ячейка занята. Введите другие координаты!");
            }
        }
        while (!isCellValid(x, y)) ;
        table[y][x] = SIGN_X;
    }

    private void turnHumanTwo() { // метод, который позволяет пользователю сделать ход
        Scanner scanner = new Scanner(System.in);
        int x, y;
        do {
            System.out.print(player2.getName() + ", введите координаты X от 1 до 3: ");
            x = scanner.nextInt() - 1;
            System.out.print(player2.getName() + ", введите координаты Y от 1 до 3: ");
            y = scanner.nextInt() - 1;
            if (table[y][x] == SIGN_X || table[y][x] == SIGN_O){
                System.out.println("Ячейка занята. Введите другие координаты!");
            }
        } while (!isCellValid(x, y));
        table[y][x] = SIGN_O;
    }

    private boolean isCellValid(int x, int y) { // метод проверки валидности ячейки
        if (x < 0 || y < 0 || x >= 3 || y >= 3) {
            System.out.println("Числа не из диапазона от 1 до 3! Повторите ввод!");
            return false;
        }
        return table[y][x] == SIGN_EMPTY;
    }

    private boolean checkWin(char dot) { // метод проверки победы по горизонтали, вертикали и диагонали
        for (int i = 0; i < 3; i++) {
            if ((table[i][0] == dot && table[i][1] == dot &&
                    table[i][2] == dot) || (table[0][i] == dot && table[1][i] == dot && table[2][i] == dot))
                return true;
            if ((table[0][0] == dot && table[1][1] == dot &&
                    table[2][2] == dot) || (table[2][0] == dot && table[1][1] == dot && table[0][2] == dot))
                return true;
        }
        return false;
    }

    private boolean isTableFull() { // метод проверки на ничью
        for (int row = 0; row < 3; row++) // циклом  проходим по всем ячейкам игровой таблицы и, если они все заняты, возвращаем true
            for (int col = 0; col < 3; col++)
                if (table[row][col] == SIGN_EMPTY)
                    return false; // если хотя бы одна ячейка свободна, возвращаем false
        return true; // ничья
    }

class Player{
    private String name;
    private int id;
    private char symbol;

    public Player(String name, int id, char symbol){
        this.name = name;
        this.id = id;
        this.symbol = symbol;
        Scanner scanner = new Scanner(System.in);
        boolean ok = false; // флаг
        System.out.print("Введите ваше имя: ");
        if(scanner.hasNextLine()) { // условие проверки на ввод строки пользователем
            this.name = scanner.nextLine();
            while (!ok) {
                if (this.name.isBlank()) {
                    System.out.println("Ваше имя некорректно. Строка не должна быть пустой");
                    System.out.print("Введите ваше имя: ");
                    if(scanner.hasNextLine()) {
                        this.name = scanner.nextLine();
                    } else {
                        ok = true;
                    }
                } else {
                    ok = true;
                }
            }
            ok = false;
        } else {
            ok = true;
        }
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public char getSymbol() {
        return symbol;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}


    public void writer(Player player1, Player player2) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {
        builder = factory.newDocumentBuilder();

        // создаем пустой объект Document, в котором будем создавать наш xml-файл
        Document doc = builder.newDocument();
        // создаем корневой элемент
        Element rootElement = doc.createElement("Gameplay");
        doc.appendChild(rootElement);
        rootElement.appendChild(getPlayers(doc, player1.getName(), player1.getId(), player1.getSymbol()));
        rootElement.appendChild(getPlayers(doc, player2.getName(), player2.getId(), player2.getSymbol()));

        Element game = doc.createElement("Game");
        rootElement.appendChild(game);
        game.appendChild(step(doc, 1, 1, 1, 1));
        game.appendChild(step(doc, 2, 2, 2, 1));


        //создаем объект TransformerFactory для печати в консоль
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // для вывода в консоль
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        //печатаем в консоль или файл
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(new File("/Users/nizovcev_as/Documents/IdeaProjects/XML/TicTacToe.xml"));
        //записываем данные
        transformer.transform(source, console);
        transformer.transform(source, file);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private Node getPlayers(Document doc, String name, int id, char symbol) {
        Element player = doc.createElement("Player");
        player.setAttribute("id", String.valueOf(id));
        player.setAttribute("name", name);
        player.setAttribute("symbol", String.valueOf(symbol));
        return player;
    }

    public static Element step(Document doc, int num, int playerId, int x, int y){
        Element step = doc.createElement("Step");
        step.setAttribute("num", String.valueOf(num));
        step.setAttribute("playerId", String.valueOf(playerId));
        String move = x + " " + y;
        step.setTextContent(move);
        return step;
    }
}

