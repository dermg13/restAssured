package Pojos;

public class loginPojos {

    /**
     * POJOS stand for Plain Old Java Object
     * A pojo class must be public and its variables must be private
     * A pojo can have a constructor with arguments, the variables should have getters and setters to access data
     * Pojo classes are used for creating JSON and XML Payloads - (DATA) for API
     */
        // empty constructor
        public loginPojos() {

        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        private String userName;

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        private String passWord;


    }

