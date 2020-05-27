package v0;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import v0.FeatureModelNames.Type;

/**
 * The {@code TaskTagsSelector} interface provides a method for selecting xml
 * task tags.
 *
 * <p>
 *
 * {@code Node} task tags can change considering the type of xml file
 * (<b>FeatureModel</b> or <b>BPMN</b> for instance)
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see <a href="https://featureide.github.io/">FeatureIDE framework</a>
 * @see <a href="https://www.bpmn.org/">BPMN</a>
 *
 */
public abstract class TaskTagsSelector  <T extends Enum <T> > {
	 static  public List<String> getTaskTags(){
		return null;
	}
}
