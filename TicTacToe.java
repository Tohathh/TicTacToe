import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TicTacToe {
    final char SIGN_X = 'x'; // переменная
    final char SIGN_O = 'o'; // переменная
    final char SIGN_EMPTY = '.'; // переменная
    String firstPlayer; // первый игрок
    String secondPlayer; // второй игрок
    char[][] table; //двумерный символьный массив, игровое поле
    int victory1;
    int defeat1;
    int victory2;
    int defeat2;

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
        boolean ok = false; // флаг
        System.out.print("Игрок №1 введите ваше имя: ");
        if(scanner.hasNextLine()) { // условие проверки на ввод строки пользователем
            firstPlayer = scanner.nextLine();
            while (!ok) {
                if (firstPlayer.isBlank()) {
                    System.out.println("Ваше имя некорректно. Строка не должна быть пустой");
                    System.out.print("Игрок №1: ");
                    if(scanner.hasNextLine()) {
                        firstPlayer = scanner.nextLine();
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
        System.out.print("Игрок №2 введите ваше имя: ");
        if(scanner.hasNextLine()) { // условие проверки на ввод строки пользователем
            secondPlayer = scanner.nextLine();
            while (!ok) {
                if (secondPlayer.isBlank()) {
                    System.out.println("Ваше имя некорректно. Строка не должна быть пустой");
                    System.out.print("Игрок №2: ");
                    if(scanner.hasNextLine()) {
                        secondPlayer = scanner.nextLine();
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
    public void game() throws IOException { // игровая логика
        Scanner scanner = new Scanner(System.in);
        int x = 1;
        initTable(); // инициализация таблицы
        boolean ok = true; // флаг
        while (ok) {
            turnHumanOne(); // ход первого игрока
            if (checkWin(SIGN_X)) { // проверка: если победа или ничья:
                System.out.println(firstPlayer + " выиграл!"); // сообщить и выйти из цикла
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
                System.out.println(secondPlayer + " выиграл!");  //    сообщить и выйти из цикла
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
                writer.append('\n');
                String text = firstPlayer + ":" + " Победы " + victory1 + ";" + " Поражения " + defeat1 + ";";
                String text1 = secondPlayer + ":" + " Победы " + victory2 + ";" + " Поражения " + defeat2 + ";";
                writer.write(text);
                writer.append('\n');
                writer.write(text1);
                writer.append('\n');
            }
        }
    }
    public void initTable() { //метод обеспечивает начальную инициализацию игровой таблицы, заполняя её ячейки «пустыми» символами
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
        boolean ok = false; // флаг
        int x, y;
        do {
            System.out.print(firstPlayer + ", введите координаты X от 1 до 3: ");
            x = scanner.nextInt() - 1;
            System.out.print(firstPlayer + ", введите координаты Y от 1 до 3: ");
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
            System.out.print(secondPlayer + ", введите координаты X от 1 до 3: ");
            x = scanner.nextInt() - 1;
            System.out.print(secondPlayer + ", введите координаты Y от 1 до 3: ");
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
        for (int i = 0; i < 3; i++)
            if ((table[i][0] == dot && table[i][1] == dot &&
                    table[i][2] == dot) || (table[0][i] == dot && table[1][i] == dot && table[2][i] == dot))
                return true;
        if ((table[0][0] == dot && table[1][1] == dot &&
                table[2][2] == dot) || (table[2][0] == dot && table[1][1] == dot && table[0][2] == dot))
            return true;
        return false;
    }
    private boolean isTableFull() { // метод проверки на ничью
        for (int row = 0; row < 3; row++) // циклом  проходим по всем ячейкам игровой таблицы и, если они все заняты, возвращаем true
            for (int col = 0; col < 3; col++)
                if (table[row][col] == SIGN_EMPTY)
                    return false; // если хотя бы одна ячейка свободна, возвращаем false
        return true; // ничья
    }
}