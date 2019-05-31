package project.json;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaJsonTests {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public static void main(String[] args) {
		new JavaJsonTests().jsonToJava();
	}
	
	private void javaToJson() {
		Person person = mockPerson();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("person.txt"), person);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void jsonToJava() {
		try {
			Person person = objectMapper.readValue(new File("person.txt"), Person.class);
			Person person2 = objectMapper.readValue("{\"id\": 2}", Person.class);
			System.out.println(person);
			System.out.println(person2);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  Person mockPerson() {
		Person person = new Person();
		person.setId(1);
		person.setName("Laurent");
		person.setSalary(2500.00);
		person.setBirth(new Date());
		
		return person;
	}
}
