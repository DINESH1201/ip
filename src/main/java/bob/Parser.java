package bob;

import bob.command.*;
import bob.exceptions.EmptyArgumentException;
import bob.exceptions.InvalidInputException;
import bob.exceptions.InvalidTaskNumberException;
import bob.exceptions.MissingArgumentException;
import bob.tasks.Deadline;
import bob.tasks.EventTask;
import bob.tasks.ToDo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Parser {

    public static Command parseCommand(String UserCommand) throws EmptyArgumentException, MissingArgumentException, InvalidInputException, InvalidTaskNumberException {
        Scanner scanner = new Scanner(UserCommand);
        String input = scanner.next();
        Command command = switch (input) {
            case "bye" -> new ByeCommand();
            case "list" -> new ListCommand();
            case "mark" -> new MarkCommand(scanner.nextInt() - 1);
            case "unmark" -> new UnmarkCommand(scanner.nextInt() - 1);
            case "todo" -> new AddTaskCommand(Parser.newToDo(scanner.nextLine().trim()));
            case "deadline" -> new AddTaskCommand(Parser.newDeadline(scanner.nextLine().trim()));
            case "event" -> new AddTaskCommand(Parser.newEvent(scanner.nextLine().trim()));
            case "delete" -> new DeleteCommand(scanner.nextInt() - 1);
            case "find" -> new FindCommand(scanner.nextLine().trim());
            default -> throw new InvalidInputException();
        };
        return command;
    }

    public static ToDo newToDo(String input) throws EmptyArgumentException {
        if (input.isEmpty()) {
            throw new EmptyArgumentException("description", "todo");
        }
        return new ToDo(input);
    }

    public static Deadline newDeadline(String input) throws EmptyArgumentException, MissingArgumentException, DateTimeParseException {

        if (!input.matches("^\\S.+")) {
            throw new EmptyArgumentException("description", "deadline");
        } else if (!input.matches("^.*/by.*$")) {
            throw new MissingArgumentException("by", "deadline");
        } else if (!input.matches("^\\S.+/by.*$")) {
            throw new EmptyArgumentException("description", "deadline");
        } else if (!input.matches("^\\S.+ /by \\S.+$")) {
            throw new EmptyArgumentException("by", "deadline");
        }

        String[] inputs = input.split("/by", 2);
        String[] dateTime = inputs[1].trim().split(" ");
        if (dateTime.length == 2) {
            return new Deadline(inputs[0].trim(),
                    LocalDate.parse(dateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    LocalTime.parse(dateTime[1], DateTimeFormatter.ofPattern("HHmm")));
        }
        return new Deadline(inputs[0].trim(), LocalDate.parse(dateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")));
    }

    public static EventTask newEvent(String input) throws EmptyArgumentException, MissingArgumentException, DateTimeParseException {

        if (!input.matches("^\\S.+")) {
            throw new EmptyArgumentException("description", "event");
        } else if (!input.matches("^.*/from.*$")) {
            throw new MissingArgumentException("from", "event");
        } else if (!input.matches("^\\S.+/from.*$")) {
            throw new EmptyArgumentException("description", "event");
        } else if (!input.matches("^.*/from.*/to.*$")) {
            throw new MissingArgumentException("to", "event");
        } else if (!input.matches("^\\S.+ /from \\S.+/to.*$")) {
            throw new EmptyArgumentException("from", "event");
        } else if (!input.matches("^\\S.+ /from \\S.+/to \\S.+$")) {
            throw new EmptyArgumentException("to", "event");
        }

        String[] inputs = input.split("/from", 2);
        String[] dates = inputs[1].split("/to", 2);
        String[] startDateTime = dates[0].trim().split(" ");
        String[] endDateTime = dates[1].trim().split(" ");
        if (startDateTime.length == 1 && endDateTime.length == 1) {
            return new EventTask(inputs[0].trim(),
                    LocalDate.parse(startDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    LocalDate.parse(endDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        } else if (startDateTime.length == 1 && endDateTime.length == 2) {
            return new EventTask(inputs[0].trim(),
                    LocalDate.parse(startDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    LocalDate.parse(endDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    LocalTime.parse(endDateTime[1], DateTimeFormatter.ofPattern("HHmm")));
        } else if (startDateTime.length == 2 && endDateTime.length == 1) {
            return new EventTask(inputs[0].trim(),
                    LocalDate.parse(startDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    LocalTime.parse(startDateTime[1], DateTimeFormatter.ofPattern("HHmm")),
                    LocalDate.parse(endDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        }
        return new EventTask(inputs[0].trim(),
                LocalDate.parse(startDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                LocalTime.parse(startDateTime[1], DateTimeFormatter.ofPattern("HHmm")),
                LocalDate.parse(endDateTime[0], DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                LocalTime.parse(endDateTime[1], DateTimeFormatter.ofPattern("HHmm")));
    }
}
