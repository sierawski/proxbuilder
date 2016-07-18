package pl.rrbs.proxbuilder.example;

import java.util.Date;

import pl.rrbs.proxbuilder.IProxBuilder;
import pl.rrbs.proxbuilder.ProxBuilder;

public class Main {
	public static void main(String[] args) {
		PersonBuilder builder = ProxBuilder.create(PersonBuilder.class);
		builder.setName("Juzek")
			.setEmail("juzek@juzoria.pl")
			.setHeight(122)
			.setDateOfBirth(new Date());
		Person person = builder.build();
		System.out.println(person.toString());
		
	}
}

interface PersonBuilder extends IProxBuilder<Person> {
	public PersonBuilder setName(String name);
	public PersonBuilder setEmail(String email);
	public PersonBuilder setHeight(int height);
	public PersonBuilder setDateOfBirth(Date dateOfBirth);
}

class Person {
	private String name;
	private String email;
	private int height;
	private Date dateOfBirth;
	
	public Person() {
	}
	
	public Person(String name, String email, int height, Date dateOfBirth) {
		this.name = name;
		this.email = email;
		this.height = height;
		this.dateOfBirth = dateOfBirth;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public int getHeight() {
		return height;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %scm, %s", 
				name, email, height, dateOfBirth);
	}
}