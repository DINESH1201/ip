import java.util.Objects;
import java.util.Scanner;

public class Bob {
    private static final String DIVIDER = "____________________________________________________________\n";
    private static final String GREETINGS = "Hello! I'm Bob\n"
            + "What can I do for you?\n";
    private static final String EXIT = "Bye. Hope to see you again soon!\n";
    
    public static void main(String[] args) {

        System.out.print(DIVIDER + GREETINGS + DIVIDER);

        chatBot();

        System.out.print(EXIT + DIVIDER);
    }

    public static void chatBot() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        if (Objects.equals(input, "bye")) {
            System.out.print(DIVIDER);
            return;
        }
        System.out.print(DIVIDER + input + "\n" + DIVIDER);
        chatBot();
    }
}
