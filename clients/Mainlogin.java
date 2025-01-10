package clients;

public class Mainlogin {

    public static void main(String[] args) {
        // Collect Id and Passwords 
        IDandPasswords idandPasswords = new IDandPasswords();
        LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());

     
        }
    }

