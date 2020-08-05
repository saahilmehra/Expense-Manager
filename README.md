# Expense Manager

An Expense Manager is a tool that allows the users to keep track of their expenses. The application allows the users to create and visualize their expenses on the basis of categories, months, mode of transaction etc.

The app has following features-
1. An Onboarding Screen: 
	The Onboarding Screen prompts the user to fill-in details such as their name, monthly budget, salary etc. The screen pop-ups only when the application is opened for the first time.
	
2. Home Screen: 
	The Home Screen is a tabbed layout, that allows the users to view tabs for different categories like personal and business. Each category tab contain the monthly budget, expenses for the category as well as a short description about the category. Each tab contains both past and upcoming transactions.
	
3. Net Balance and Mode based Segregation:
	The Home Screen displays the net balance of the user i.e. the current balance in the user's account along with the information about where the amount is stored. This is cash, card, cheques and others.
	
4. Upcoming Transactions:
	The home screen displays a list of upcoming transactions with a button to add more upcoming transactions. The income and expense amounts are highlighted with different colours. On clicking an upcoming transaction, user can complete, edit and delete them.
	
5. Past Transactions:
	The home screen displays a list of past transactions with a button to add more past transactions. The income and expense amounts are highlighted with different colours. On clicking an past transaction, user can edit and delete them.
	
6. Month Cards: 
	A month card is automatically added for each month once the expense for the month is added. The card displays at least 3 latest transactions for the month. If the expenses of the month exceed the budget, an error message is displayed on the card and the colour of the card reflects this as well. On clicking, a detailed view for the month is displayed.
	
7. Month Details:
	Month detail fragment shows net balance, amount saved and amount spent for that month. It shows only past transactions of that month.
	
8. Calendar View:
	The app calendar button in the menu bar opens a calendar view.
	
9. Add Transaction:
	User can add both past and upcoming transactions. Upcoming transaction has 3 additional fields ie start date, end date and recurring period.