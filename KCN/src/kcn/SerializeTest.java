/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kcn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpu11165-local
 */
public class SerializeTest {

    public static void main(String[] args) {
        Person person = new Person(1, "Thành Chung", (short) 21);
        System.out.println(person);
        try {
            byte[] data = SerialAndDeSerial.serialize(person);
            Person person1 = (Person) SerialAndDeSerial.deserialize(data);

            System.out.println("***********************************");
            System.out.println(person1);

        } catch (IOException ex) {
            Logger.getLogger(SerializeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

class SerialAndDeSerial {

    public static Object deserialize(byte[] encode) {
        Object obj = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bas = new ByteArrayInputStream(encode);
            ois = new ObjectInputStream(bas);
            obj = ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obj;
    }

    public static byte[] serialize(Object obj) throws IOException {
        byte[] encode = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            encode = bos.toByteArray();

        } catch (IOException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            oos.close();
        }
        return encode;
    }
}

class Person implements Serializable {

    private int id;
    private String name;
    private short age;

    public Person(int id, String name, short age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }
}
