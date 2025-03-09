package com.allali.BrainCLI;

import com.allali.BrainCLI.services.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Scanner;

@SpringBootApplication
@ShellComponent
public class BrainCliApplication implements CommandLineRunner {

	private final ChatService chatService;

	public BrainCliApplication(ChatService chatService) {
		this.chatService = chatService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BrainCliApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Set up a scanner to read from the terminal
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		System.out.println("BrainCLI started. Type 'exit' to quit.");

		while (running) {
			System.out.print("> ");
			String input = scanner.nextLine().trim();

			if (input.equalsIgnoreCase("exit")) {
				running = false;
				System.out.println("Goodbye!");
			} else if (input.startsWith("ask ")) {
				// Extract the question (everything after "ask ")
				String question = input.substring(4).trim();
				if (question.startsWith("\"") && question.endsWith("\"")) {
					question = question.substring(1, question.length()-1);
				}

				System.out.println("Asking: " + question);

				try {
					String response = chatService.askQuestion(question).block();
					System.out.println("Response: " + response);
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
			} else if (input.equals("help")) {
				System.out.println("Available commands:");
				System.out.println("  ask \"<question>\" - Ask a question to OpenAI");
				System.out.println("  help - Show this help message");
				System.out.println("  exit - Exit the application");
			} else {
				System.out.println("Unknown command. Type 'help' for available commands.");
			}
		}

		scanner.close();
		System.exit(0);
	}
}