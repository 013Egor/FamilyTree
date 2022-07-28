package com.egor.familyTree.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "person")
public class Person {

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "day")
    private int day;
    @Column(name = "month")
    private int month;
    @Column(name = "year")
    private int year;
    @Column(name = "place")
    private String place;

    @Column(name = "weight")
    private double weight;
    
    @Column(name = "height")
    private double height;
    
    @Column(name = "bio")
    private String bio;
    
    @Column(name = "parent1")
    private int parent1;
    
    @Column(name = "parent2")
    private int parent2;
    
    @Column(name = "spouse")
    private int spouse;
    
    @Column(name = "photo")
    private String photo;

    @Column(name = "curX")
    public volatile int curX = 0;

    @Column(name = "curY")
    public volatile int curY = 0;

    public Person(Person person) {
        this.id = person.id;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.middleName = person.middleName;
        this.day = person.day;
        this.month = person.month;
        this.year = person.year;
        this.place = person.place;
        this.weight = person.weight;
        this.height = person.height;
        this.bio = person.bio;
        this.parent1 = person.parent1;
        this.parent2 = person.parent2;
        this.spouse = person.spouse;
        this.photo = person.photo;
        this.curX = person.curX;
        this.curY = person.curY;
    }

    public Person(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.bio = "";
        this.photo = "";
        this.parent1 = Constants.EMPTY;
        this.parent2 = Constants.EMPTY;
        this.spouse = Constants.EMPTY;
        this.place = "";
        this.day = Constants.EMPTY;
        this.year = Constants.EMPTY;
        this.month = Constants.EMPTY;
    }

    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.middleName = "";
        this.bio = "";
        this.photo = "";
        this.weight = Constants.EMPTY;
        this.height = Constants.EMPTY;
        this.parent1 = Constants.EMPTY;
        this.parent2 = Constants.EMPTY;
        this.spouse = Constants.EMPTY;
        this.place = "";
        this.day = Constants.EMPTY;
        this.year = Constants.EMPTY;
        this.month = Constants.EMPTY;
    }

    public void setPerson(Person person) {
        this.id = person.id;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.middleName = person.middleName;
        this.day = person.day;
        this.month = person.month;
        this.year = person.year;
        this.place = person.place;
        this.weight = person.weight;
        this.height = person.height;
        this.bio = person.bio;
        this.parent1 = person.parent1;
        this.parent2 = person.parent2;
        this.spouse = person.spouse;
        this.photo = person.photo;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getBodyParaments() {
        if (weight != Constants.EMPTY && height != Constants.EMPTY) {
            return weight + " кг; " + height + " см";
        } else if (weight == Constants.EMPTY && height == Constants.EMPTY) {
            return "";
        } else if (weight != Constants.EMPTY) {
            return weight + " кг";
        } else if (height != Constants.EMPTY) {
            return height + " см";
        } else {
            return "";
        }
    }

    public String getBio() {
        return bio;
    }

    public String getHtmlBio() {
        return "<html>" + bio.replaceAll("\n", "<br>");
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    public void setBirthDay(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setBirthDay(String date) throws NumberFormatException {
        String[] dateParts = date.split("[.]");
        if (dateParts.length != 3) {
            dateParts = date.split("[/]");
        }
        this.day = Integer.parseInt(dateParts[0]);
        this.month = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[2]);
    }

    public String getBirthDay() {
        return day + "." + month + "." + year;
    }

    public boolean hasBirthDay() {
        if (day == Constants.EMPTY || month == Constants.EMPTY || 
                year == Constants.EMPTY) {

            return false;
        } else {
            return true;
        }
    }

    public void setBirthPlace(String palce) {
        this.place = palce;
    }

    public String getBirthPlace() {
        return this.place;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getParent1() {
        return parent1;
    }

    public void setParent1(int parent1) {
        this.parent1 = parent1;
    }

    public int getParent2() {
        return parent2;
    }

    public void setParent2(int parent2) {
        this.parent2 = parent2;
    }

    public int getSpouse() {
        return spouse;
    }

    public void setSpouse(int spouse) {
        this.spouse = spouse;
    }

    public String getBirthInfo() {
        if (day == Constants.EMPTY || month == Constants.EMPTY || year == Constants.EMPTY) {
            return "";
        } else {
            return day + "." + month + "." + year + "; " + place;
        }
    }

    public String getShortName() {
        StringBuffer result = new StringBuffer();
        if (firstName.length() >= 1) {
            result.append(firstName.charAt(0) + ". ");
        }
        if (middleName.length() >= 1) {
            result.append(middleName.charAt(0) + ".");
        }
        return lastName + " " + result;
    }
    
    public String getCSV() {
        String photoName = "";
        if (photo.contains("/")) {
            String[] temp = photo.split("[/]");

            photoName = temp[temp.length - 1];
        }

        String bInfo = "";
        if (day != Constants.EMPTY && month != Constants.EMPTY && year != Constants.EMPTY) {
            bInfo = day + "." + month + "." + year;
        }
        
        return id + "," + firstName + "," + lastName + "," + middleName + "," + bInfo + "," + place + "," + weight + "," + height + "," + parent1 + "," + parent2 + "," + spouse + "," + photoName + "," + curX + "," + curY;
    }

    public int getYear() {
        return year;
    }

    public boolean hasParent() {
        return parent1 != Constants.EMPTY || parent2 != Constants.EMPTY;
    }

    public boolean isSameParent(Person person) {
        return parent1 != Constants.EMPTY && parent2 != Constants.EMPTY &&  
                    (parent1 == person.getParent1() && parent2 == person.getParent2() ||    
                    parent1 == person.getParent2() && parent2 == person.getParent1()); 
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj instanceof Person) {
            
            Person person = (Person) obj;

            return person.id == this.id ? true : false; 
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        
        return id + ": " + firstName + " " + lastName + " " + middleName;
    }
}