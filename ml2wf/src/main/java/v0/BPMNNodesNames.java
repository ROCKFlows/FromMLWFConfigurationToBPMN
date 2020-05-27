package v0;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public  class BPMNNodesNames extends TaskTagsSelector<BPMNNodesNames.Type>  {

  public enum Type { 
	TASK("bpmn2:task"), USERTASK("bpmn2:userTask"), INCOMING("bpmn2:incoming"), OUTGOING("bpmn2:outgoing"),
	EXTENSION("bpmn2:extensionElements"), STYLE("ext:style"), DOCUMENTATION("bpmn2:documentation"), SELECTOR("");

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
