import java.util.Random;
import java.util.Scanner;

public class NumberSeriesGameHub {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    static int difficulty = 1;
    static boolean mediumUnlocked = false;
    static boolean hardUnlocked = false;

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== Number Series Game Hub =====");
            System.out.println("1. Select Difficulty");
            System.out.println("2. Missing Number Game");
            System.out.println("3. Next Number Game");
            System.out.println("4. Pattern Guess Game");
            System.out.println("5. Rapid Fire Quiz");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch(choice) {
                case 1: setDifficulty(); break;
                case 2: playGame(1); break;
                case 3: playGame(2); break;
                case 4: playGame(3); break;
                case 5: rapidFire(); break;
                case 6: System.out.println("Exiting... Thank you!"); break;
                default: System.out.println("Invalid choice!");
            }

        } while(choice != 6);
    }

    public static void setDifficulty() {
        System.out.println("\nSelect Difficulty:");
        System.out.println("1. Easy (Unlocked)");
        System.out.println("2. Medium " + (mediumUnlocked ? "(Unlocked)" : "(Locked)"));
        System.out.println("3. Hard " + (hardUnlocked ? "(Unlocked)" : "(Locked)"));

        int choice = sc.nextInt();

        if(choice == 1) difficulty = 1;
        else if(choice == 2 && mediumUnlocked) difficulty = 2;
        else if(choice == 3 && hardUnlocked) difficulty = 3;
        else System.out.println("Level Locked!");
    }

    public static int getRange() {
        if(difficulty == 1) return 5;
        else if(difficulty == 2) return 10;
        else return 20;
    }

    // ===== NEW: HINT METHOD =====
    public static void printHint(String type) {
        System.out.println("Hint: " + type);
    }

    // TIMER
    public static Integer getAnswerWithTimer() {

        final Integer[] userAnswer = {null};

        Thread inputThread = new Thread(() -> {
            try {
                userAnswer[0] = sc.nextInt();
            } catch(Exception e) {}
        });

        inputThread.start();

        try {
            for(int i = 15; i >= 1; i--) {
                if(userAnswer[0] != null) break;
                System.out.print("\rTime left: " + i + " sec ");
                Thread.sleep(1000);
            }
            System.out.println();
            inputThread.join(15000);
        } catch (InterruptedException e) {}

        if(userAnswer[0] == null) {
            System.out.println("Time's up!");
        }

        return userAnswer[0];
    }

    // ===== MAIN GAME =====
    public static void playGame(int gameType) {

        int score = 0;

        for(int q = 1; q <= 3; q++) {

            System.out.println("\nQuestion " + q + ":");

            int range = getRange();
            int start = rand.nextInt(range) + 1;
            int diff = rand.nextInt(range) + 1;

            // ===== MISSING NUMBER =====
            if(gameType == 1) {

                int patternType = rand.nextInt(3);
                int[] series = new int[5];
                int answer = 0;

                if(patternType == 0) {
                    printHint("Arithmetic Pattern (constant difference)");
                    for(int i = 0; i < 5; i++)
                        series[i] = start + i * diff;
                }
                else if(patternType == 1) {
                    printHint("Geometric Pattern (multiplying pattern)");
                    int ratio = rand.nextInt(3) + 2;
                    series[0] = start;
                    for(int i = 1; i < 5; i++)
                        series[i] = series[i-1] * ratio;
                }
                else {
                    printHint("Alternating Pattern (+ and -)");
                    series[0] = start;
                    for(int i = 1; i < 5; i++) {
                        if(i % 2 == 0) series[i] = series[i-1] + diff;
                        else series[i] = series[i-1] - diff;
                    }
                }

                int miss = rand.nextInt(5);
                answer = series[miss];
                series[miss] = -1;

                for(int n : series)
                    System.out.print((n == -1 ? "_ " : n + " "));

                System.out.print("\nYour answer: ");
                Integer user = getAnswerWithTimer();

                if(user != null && user == answer) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Answer: " + answer);
                }
            }

            // ===== NEXT NUMBER =====
            else if(gameType == 2) {

                int patternType = rand.nextInt(3);
                int answer = 0;

                if(patternType == 0) {
                    printHint("Arithmetic Progression");
                    for(int i = 0; i < 5; i++)
                        System.out.print((start + i * diff) + " ");
                    answer = start + 5 * diff;
                }
                else if(patternType == 1) {
                    printHint("Geometric Progression");
                    int ratio = rand.nextInt(3) + 2;
                    int val = start;
                    for(int i = 0; i < 5; i++) {
                        System.out.print(val + " ");
                        val *= ratio;
                    }
                    answer = val;
                }
                else {
                    printHint("Square-based pattern");
                    for(int i = 1; i <= 5; i++)
                        System.out.print((i*i + start) + " ");
                    answer = 36 + start;
                }

                System.out.print("\nYour answer: ");
                Integer user = getAnswerWithTimer();

                if(user != null && user == answer) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Answer: " + answer);
                }
            }

            // ===== PATTERN GAME =====
            else {

                int type = rand.nextInt(4);
                int answer = 0;

                if(type == 0) {
                    printHint("Fibonacci Series");
                    int a = 0, b = 1;
                    System.out.print(a + " " + b + " ");
                    for(int i = 0; i < 3; i++) {
                        int c = a + b;
                        System.out.print(c + " ");
                        a = b; b = c;
                    }
                    answer = a + b;
                }
                else if(type == 1) {
                    printHint("Perfect Squares");
                    for(int i = 1; i <= 5; i++)
                        System.out.print((i*i) + " ");
                    answer = 36;
                }
                else if(type == 2) {
                    printHint("Alternating multiply/add pattern");
                    int val = 2;
                    System.out.print(val + " ");
                    for(int i = 1; i < 5; i++) {
                        if(i % 2 == 0) val += 3;
                        else val *= 2;
                        System.out.print(val + " ");
                    }
                    answer = val * 2;
                }
                else {
                    printHint("Increasing difference pattern");
                    int val = 1;
                    System.out.print(val + " ");
                    for(int i = 1; i < 5; i++) {
                        val += i;
                        System.out.print(val + " ");
                    }
                    answer = val + 5;
                }

                System.out.print("\nYour answer: ");
                Integer user = getAnswerWithTimer();

                if(user != null && user == answer) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Answer: " + answer);
                }
            }
        }

        System.out.println("\nScore: " + score + "/3");

        if(score >= 2) mediumUnlocked = true;
        if(score == 3) hardUnlocked = true;
    }

    public static void rapidFire() {
        int score = 0;

        for(int i = 0; i < 5; i++) {
            int start = rand.nextInt(10);
            int diff = rand.nextInt(5) + 1;

            printHint("Quick Arithmetic Pattern");

            for(int j = 0; j < 4; j++)
                System.out.print((start + j * diff) + " ");

            int ans = start + 4 * diff;

            System.out.print("\nYour answer: ");
            Integer user = getAnswerWithTimer();

            if(user != null && user == ans) score++;
        }

        System.out.println("\nFinal Score: " + score + "/5");
    }
}
