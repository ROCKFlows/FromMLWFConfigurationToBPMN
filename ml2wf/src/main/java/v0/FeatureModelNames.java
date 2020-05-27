package v0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class  FeatureModelNames extends TaskTagsSelector<FeatureModelNames.Type> {

	
	  public enum Type { 
		  FEATUREMODEL("extendedFeatureModel"),
	      PROPERTIES("properties"), 
	      STRUCT("struct"), 
	      AND("and"), 
	      GRAPHICS("graphics"),
	      FEATURE("feature"), ALT("alt"), DESCRIPTION("description"),
	      CONSTRAINTS("constraints"), RULE("rule"), SELECTOR("") ;
	 
	
	private String name;

	private Type(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	  }
	

	static public  List<String> getTaskTags(){
		return  Stream.of(Type.values()).map(Type::name).collect(Collectors.toList());
	}
	
}
