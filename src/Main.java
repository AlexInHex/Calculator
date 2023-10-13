import java.util.*;
public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите выражение (в формате 5 + 3 или V - II):");
            String input = scanner.nextLine().trim(); //считываем строку и убираем пробелы покраям
            scanner.close();

            String[] tokens = input.split(" "); //разделяем с помощью пробела операнды и оператор
            if (tokens.length != 3) // проверяем что введен верный формат выражения
                throw new IllegalArgumentException("Неверный формат выражения");

            //присваивам каждой части выражения значение
            String num1 = tokens[0];
            String operator = tokens[1];
            String num2 = tokens[2];

            //выясняем с какой системой счисления будем работать
            boolean isArabic = isArabicNumeral(num1) && isArabicNumeral(num2);
            boolean isRoman = isRomanNumeral(num1) && isRomanNumeral(num2);

            //проверяем, что данные принадлежат к одной из двух систем счисления.
            if (isArabic == isRoman)
                throw new IllegalArgumentException("Введите только арабские или римские только цифры, но не смешанные.");

            //получаем операнды с которыми будем производить вычисление
            int operand1 = isArabic ? Integer.parseInt(num1) : RomanToArabic(num1);
            int operand2 = isArabic ? Integer.parseInt(num2) : RomanToArabic(num2);

            //сюда запишем результат
            int result = switch (operator) {
                case "+" -> operand1 + operand2;
                case "-" -> operand1 - operand2;
                case "*" -> operand1 * operand2;
                case "/" -> {
                    if (operand2 == 0)
                        throw new ArithmeticException("Нельзя делить на ноль");
                    yield operand1 / operand2;
                }
                default -> throw new IllegalArgumentException("Неверный оператор: " + operator);
            };

            //выясняем операцию и производим вычисления. так же делаем проверку деления на ноль и на неверный символ операции.

            //берем строковое представление результата и выводим в консоль
            String output = isArabic ? String.valueOf(result) : ArabicToRoman(result); //и переводим в римские если нужно
            System.out.println("Результат: " + output);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    // проверка на систему счисления для арабских цифр
    private static boolean isArabicNumeral(String str) {
        try {
            int value = Integer.parseInt(str);
            return value >= 0 && value <= 10; // значение операнда согласно условию задачи
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // проверка на систему счисления для римских цифр
    private static boolean isRomanNumeral(String str) {
        return str.matches("^[IVXLCDM]+$");
    }

    //переводим римские в арабские
    private static int RomanToArabic(String roman) {

        //создаем словарь со значением для всех существующих римских цифр
        Map<Character, Integer> romanMap = new HashMap<>();
        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);


        int result = 0;
        int prevValue = 0; //Чтобы правильно преобразовать римские цифры, нам нужно отслеживать предыдущее значение, чтобы определить, должно ли происходить вычитание.


        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = romanMap.get(roman.charAt(i)); // получаем значение символа из словаря
            if (value < prevValue) {
                result -= value;
            } else {
                result += value;
            }
            prevValue = value;
        }

        return result;
    }

    // переводим ответ в римские цифры
    private static String ArabicToRoman(int num) {
        String[] romanNumerals = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C"};
        int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100};

        StringBuilder roman = new StringBuilder(); //сюда будем добавлять римские символы
        for (int i = values.length - 1; i >= 0; i--) {
            while (num >= values[i]) {
                roman.append(romanNumerals[i]);
                num -= values[i];
            }
        }

        return roman.toString();
    }
}