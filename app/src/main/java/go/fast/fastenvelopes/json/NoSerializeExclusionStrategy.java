package go.fast.fastenvelopes.json;

import com.google.myjson.ExclusionStrategy;
import com.google.myjson.FieldAttributes;

public class NoSerializeExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(NoSeriaizle.class) != null;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes file) {
		return file.getAnnotation(NoSeriaizle.class) != null;
	}

}
