# **BagCheck**

  A simple terminal ledger to help you organize your finances

## **How to install**

  You can find the latest version in the releases page, containing both the compiled program in a .jar file and the source code,
 as long as you have java in your machine you should be able to run the program right away.
  The program creates a hidden directory in the user's home directory to store any user data, if the data is moved or deleted the program will
 create new save data when started.

## **How to use**

  You can open the .jar file both in your Desktop normally or in your own Terminal and the program will recognize this and
 open the TUI within its launch terminal.
  Once on the main menu you'll find a menu next to a summarized display of possibly relevant details kept in track for the user.
  The menu will allow you to go both into your **Assets** and **Ledger**, responsible for aiding on your managing of bank accounts and transactions
 respectively.

###    **Assets**

       On this menu you'll be able to create your bank accounts and edit any of its details.
       You're able to edit any mistake when you input its information althought the balance is not
      meant to be updated manually, as when you register new transactions in your **Ledger** the
      program keeps track on any changes to your balance

###    **Ledger**

       In here you're able to register and manage any new expenses or deposits in the account, as mentioned 
      previously this ledger will keep track of your personal balance aswell as display all transactions by date.
       Since over time this list would get immense there are plenty of filter options available at the top,
      you need only to check which filter type you want and click search.

###    **Transactions**

       These are created on the ledger menu and are the individual entries of any deposit or expense.
       To differentiate between these two the amount introduced must be a positive (deposit) or negative (expense)
      number.
       When creating a transaction you can write any identification as most convenient in their name,
      description or tag fields, in which the latter can be a keyword to a certain type of transactions you
      may want to filter through.


## **About the Project**

  The main goal of this project was developing a simple personal tool that I could use to manage my personal finances along with
 learning how to implement SQLite databases in a program. I've been using it for the past few months and developing on top of it
 as I come up with ideas on how to improve its utility or ease of use.
  I've made the unpopular choice of writing this program in Java since it was the programming language I've gotten more familiar with
 during my studies at university, but for the simple program I intended and its planned updates it shouldn't prove to be much of a limitation.
  Feel free to try it out and leave your feedback, any suggested improvement is appreciated.

---
## **Third-Party Licenses**

 This project has dependancies on the following open source libraries:

### - **Lanterna** - Terminal UI library
    Licensed under the GNU Lesser General Public License v2.1.
    See [https://github.com/mabe02/lanterna](https://github.com/mabe02/lanterna)

### - **SQLite JDBC Driver**
    Licensed under the Apache License v2.0.
    See [https://github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)
    
---
