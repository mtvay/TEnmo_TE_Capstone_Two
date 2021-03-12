package com.techelevator.tenmo;

import java.math.BigDecimal;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TransferService transferService;
    private AccountService accountService;
    private UserService userService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new TransferService(API_BASE_URL), new AccountService(API_BASE_URL), new UserService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, TransferService transferService, AccountService accountService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.transferService = transferService;
		this.accountService = accountService;
		this.userService = userService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		String token = currentUser.getToken();
		Account account = accountService.getBalance(currentUser.getUser().getId(), token);
		System.out.println("Your current account balance is: $" + account.getBalance());
	}

	private void viewTransferHistory() {
		String token = currentUser.getToken();
		Transfer[] transfers = transferService.getAllTransfersByUserId(currentUser.getUser().getId(), token);
		
		System.out.println("-------------------------------------------\n" + 
		"Transfers\n" + "ID \t From/To \t \t Amount \n" +
		"-------------------------------------------\n");
		
		for (Transfer transfer : transfers) {
			String output = "";
			if (transfer.getAccountFrom() == currentUser.getUser().getId()) {
				output = transfer.getId() + "\tFrom: " + currentUser.getUser().getUsername() + "\t \t $ ";
			} else if (transfer.getAccountTo() == currentUser.getUser().getId()) {
				output = transfer.getId() + "\tTo: " + accountService.getUsernameFromAccountId(transfer.getAccountTo(), token) + "\t \t $ ";
			}
			
			System.out.println(output + transfer.getAmount());
		}
		
		System.out.println("---------\n");
		String input = console.getUserInput("Please enter transfer ID to view details (0 to cancel)");
		
		//input validation
		if (input.matches("[\\D]") || !input.matches("[\\d]")) {
			System.out.println("Please select a valid transaction Id. Returning to Main Menu.");
			mainMenu();
		} else if (input.equals("0")) {
			mainMenu();
		}
		
		int transferId = Integer.parseInt(input);
		
		int matchCount = 0;
		for (Transfer transfer : transfers) {
			if (transferId == transfer.getId()) {
				matchCount++;
			}
		}
		
		if (matchCount == 0) {
			System.out.println("Please select a valid transaction Id. Returning to Main Menu.");
			mainMenu();
		}

		Transfer selectionTransfer = transferService.getTransferById(transferId, token);
		
		String output = "Id: " + selectionTransfer.getId() +
				"\nFrom: " + accountService.getUsernameFromAccountId(selectionTransfer.getAccountFrom(), token) +
				"\nTo: " + accountService.getUsernameFromAccountId(selectionTransfer.getAccountTo(), token) +
				"\nType: " + selectionTransfer.getTypeDesc() + 
				"\nStatus: " + selectionTransfer.getStatusDesc() +
				"\nAmount: $" + selectionTransfer.getAmount();

		System.out.println(output);
	}

	private void viewPendingRequests() {
		String token = currentUser.getToken();
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		String token = currentUser.getToken();
		User[] users = userService.getAll(token);
		
		System.out.println("-------------------------------------------\n" + 
				"Users\n" + "ID \t Name\n" +
				"-------------------------------------------\n");
		for (User user : users) {
			System.out.println(user.getId() + "\t" + user.getUsername());
		}

		System.out.println("---------\n\n");
		
		String input = console.getUserInput("Enter ID of user you are sending to (0 to cancel)");
		
		//input validation
		if (input.matches("[\\D]") || !input.matches("[\\d]")) {
			System.out.println("Please select a valid user Id. Returning to Main Menu.");
			mainMenu();
		} else if (input.equals("0")) {
			mainMenu();
		}
				
		int toUserId = Integer.parseInt(input);
				
		int matchCount = 0;
		for (User user : users) {
			if (toUserId == user.getId()) {
				matchCount++;
			}
		}
				
		if (matchCount == 0) {
			System.out.println("Please select a valid user Id. Returning to Main Menu.");
			mainMenu();
		}
	
		
		String amountInput = "";
		while(true) {
			amountInput = console.getUserInput("Enter amount");
			if (!amountInput.matches("(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$")){
				System.out.println("Please select a valid amount. Returning to Main Menu.");
				continue;
			} else if (amountInput.equals("0")) {
				System.out.println("Please enter an amount greater than 0. Returning to Main Menu");
				continue;
			}
			break;
		}
		
		BigDecimal amount = new BigDecimal(amountInput);
		int fromUserId = currentUser.getUser().getId();
		
		Account fromUser = accountService.getBalance(fromUserId, token);
		Account toUser = accountService.getBalance(toUserId, token);
		
		BigDecimal fromBalance = fromUser.getBalance();
		BigDecimal toBalance = toUser.getBalance();
		
		if (fromBalance.compareTo(amount) == -1) {
			System.out.println("Insufficient Funds: Returning to Main Menu");
			mainMenu();
		}
		
		BigDecimal newFromBalance = fromBalance.subtract(amount);
		BigDecimal newToBalance = toBalance.add(amount);
		
		fromUser.setBalance(newFromBalance);
		toUser.setBalance(newToBalance);
		
		accountService.update(fromUser, token);
		accountService.update(toUser, token);
		
		Transfer transfer = new Transfer();
		transfer.setAmount(amount);
		transfer.setAccountFrom(fromUserId);
		transfer.setAccountTo(toUserId);
		transfer.setStatusId(2);
		transfer.setTypeId(2);
		transferService.transfer(transfer, token);
	}

	private void requestBucks() {
		String token = currentUser.getToken();
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
