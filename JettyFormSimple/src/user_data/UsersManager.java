/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cpu11165-local
 */
public class UsersManager {
   private List<User> list;
   public UsersManager(){
       list = new ArrayList<User>();
   }
   
   public boolean addNewUser(User newUser){
       if(!isExistedUser(newUser.username) && !isExistedEmail(newUser.email) && 
               !isExistedPhone(newUser.phone)){
           list.add(newUser);
           return true;
       }
       return false;
   }
   
   public boolean isExistedUser(String username){
       int size = list.size();
       for(int i =0; i< size; i++){          
           if(list.get(i).username.equals(username)){
               return true;
           }
       }
       return false;
   }
   
   public boolean isExistedPhone(String phone){
       int size = list.size();
       for(int i =0; i< size; i++){
           if(list.get(i).phone.equals(phone)){
               return true;
           }
       }
       return false;
   }
   
   public boolean isExistedEmail(String email){
       int size = list.size();
       for(int i =0; i< size; i++){
           if(list.get(i).email.equals(email)){
               return true;
           }
       }
       return false;
   }
   
   public boolean isValidAccount(String username, String password){
       System.out.println("Login username: " + username + " - password: " + password);
       int size = list.size();
       for(int i =0; i< size; i++){
           User user = list.get(i);           
           if(user.username.equals(username) && user.password.equals(password)){
               return true;
           }
       }
       return false;
   }
   
   public UserResult getInformationUser(String username, String password){
       System.out.println("Login username: " + username + " - password: " + password);
       UserResult ur = new UserResult();
       int size = list.size();
       for(int i =0; i< size; i++){
           User user = list.get(i);           
           if(user.username.equals(username) && user.password.equals(password)){
               ur.error = 0;
               ur.user = user;
           }
       }      
       return ur;
   }
}

